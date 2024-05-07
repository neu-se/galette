package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.*;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.collection.ObjectIntMap;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import java.util.NoSuchElementException;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

class ShadowLocals extends MethodVisitor {
    /**
     * The uninstrumented version of the method being visited.
     * <p>
     * Non-null.
     */
    private final MethodNode original;
    /**
     * Index of the first local variable used to store the {@link Tag} for a local variable.
     */
    private final int shadowVariablesStart;
    /**
     * Index of the first (the lowest value) local variable used to the {@link Tag} for an element of the runtime stack.
     * The {@link Tag} for the bottom element of the stack is stored in the local variable slot at this index.
     */
    private final int shadowStackStart;
    /**
     * Number of elements currently in the shadow stack.
     */
    private int shadowStackSize;
    /**
     * Set of labels that mark the beginning of exception handlers.
     * <p>
     * Non-null.
     */
    private final ObjectIntMap<Label> handlers = new ObjectIntMap<>();
    /**
     * {@code true} if the next frame visited is the frame at the start of an exception handler
     */
    private boolean startingHandler = false;
    /**
     * {@link MethodVisitor} that managers the {@link TagFrame} for the method being visited.
     * <p>
     * Non-null.
     */
    private final FrameManager frameManager;

    ShadowLocals(FrameManager frameManager, MethodNode original) {
        super(GaletteTransformer.ASM_VERSION, frameManager);
        if (original == null || frameManager == null) {
            throw new NullPointerException();
        }
        this.original = original;
        this.frameManager = frameManager;
        this.shadowVariablesStart = frameManager.lastAddedLocalIndex() + 1;
        this.shadowStackStart = shadowVariablesStart + original.maxLocals;
        this.shadowStackSize = 0;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        boolean isStatic = AsmUtil.isSet(original.access, Opcodes.ACC_STATIC);
        // Initialize shadow variables for arguments
        int varIndex = frameManager.initializeShadowArguments(isStatic, original.desc, shadowVariablesStart);
        // Initialize remaining shadow variables
        for (; varIndex < shadowStackStart; varIndex++) {
            super.visitInsn(Opcodes.ACONST_NULL);
            super.visitVarInsn(Opcodes.ASTORE, varIndex);
        }
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type);
        handlers.put(handler, 1);
    }

    @Override
    public void visitLabel(Label label) {
        if (handlers.containsKey(label)) {
            startingHandler = true;
        }
        super.visitLabel(label);
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        SimpleList<Object> newLocal = new SimpleList<>();
        // Copy the original locals
        for (int i = 0; i < numLocal; i++) {
            newLocal.add(local[i]);
        }
        int varIndex = computeNumberOfSlots(numLocal, local);
        // Fill in TOP until we get to the added local variables
        for (; varIndex < frameManager.firstAddedLocalIndex(); varIndex++) {
            newLocal.add(Opcodes.TOP);
        }
        // Add the locals from the frame manager
        frameManager.appendAddedLocals(newLocal);
        // Recompute the shadow stack size
        shadowStackSize = startingHandler ? 0 : computeNumberOfSlots(numStack, stack);
        // Add tags for the shadow local variables and shadow runtime stack
        for (varIndex = shadowVariablesStart; varIndex < shadowStackStart + shadowStackSize; varIndex++) {
            newLocal.add(GaletteNames.TAG_INTERNAL_NAME);
        }
        super.visitFrame(type, newLocal.size(), newLocal.toArray(new Object[newLocal.size()]), numStack, stack);
        if (startingHandler) {
            // Add a tag for the exception
            super.visitInsn(DUP);
            Handle.EXCEPTION_STORE_GET.accept(mv);
            push();
            startingHandler = false;
        }
    }

    private static int computeNumberOfSlots(int num, Object[] elements) {
        int slots = 0;
        for (int i = 0; i < num; i++) {
            Object element = elements[i];
            if (element == Opcodes.LONG || element == Opcodes.DOUBLE) {
                // Longs and doubles take two variable slots but are only represented once in the frame
                slots += 2;
            } else {
                slots++;
            }
        }
        return slots;
    }

    @Override
    public void visitLocalVariable(
            String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
        if (name.equals("this") && index == 0) {
            // The $ prefix is necessary for some debuggers to pick up this local variable entry
            name = "$this";
        }
        super.visitLocalVariable(
                GaletteNames.getShadowVariableName(name),
                GaletteNames.TAG_DESCRIPTOR,
                null,
                start,
                end,
                getShadowVariableIndex(index));
    }

    public void storeShadowVar(int index) {
        // [Tag] -> []
        super.visitVarInsn(Opcodes.ASTORE, getShadowVariableIndex(index));
    }

    public void loadShadowVar(int index) {
        // [] -> [Tag]
        super.visitVarInsn(Opcodes.ALOAD, getShadowVariableIndex(index));
    }

    /**
     * Pushes the top element of the runtime stack onto the top slot of the shadow stack.
     */
    public void push() {
        shadowStackSize++;
        super.visitVarInsn(Opcodes.ASTORE, getShadowStackIndex(0));
    }

    /**
     * Pushes the top element of the runtime stack onto the top two slots of shadow stack.
     * This is used to match against elements of the runtime stack that consume two slots (doubles and longs).
     */
    public void pushWide() {
        push();
        super.visitInsn(Opcodes.ACONST_NULL);
        push();
    }

    /**
     * Removes the top {@code n} elements from the shadow stack
     *
     * @param n the number of elements to be popped from the shadow stack
     */
    public void pop(int n) {
        if (n > shadowStackSize) {
            throw new NoSuchElementException();
        }
        shadowStackSize -= n;
    }

    /**
     * Returns the index of local variable for the n<sup>th</sup> element from the top of the shadow stack.
     * When {@code n == 0}, the index for the top element of the shadow stack is returned.
     */
    private int getShadowStackIndex(int n) {
        return shadowStackStart + shadowStackSize - 1 - n;
    }

    /**
     * Returns the index of the local variable used to hold the tag for the local variable at the specified index.
     */
    private int getShadowVariableIndex(int index) {
        if (index >= frameManager.firstAddedLocalIndex()) {
            throw new IllegalArgumentException();
        }
        return index + shadowVariablesStart;
    }

    /**
     * Loads the n<sup>th</sup> element from the top of the shadow stack onto the runtime stack
     * When {@code n == 0}, the top element of the shadow stack is loaded.
     */
    public void peek(int n) {
        if (n >= shadowStackSize) {
            throw new NoSuchElementException();
        }
        super.visitVarInsn(Opcodes.ALOAD, getShadowStackIndex(n));
    }

    FrameManager getFrameManager() {
        return frameManager;
    }

    public void prepareForCall(boolean isStatic, String descriptor, boolean createFrame) {
        if (createFrame) {
            frameManager.prepareForCall(this, isStatic, descriptor);
        }
        pop(AsmUtil.countArgumentSlots(isStatic, descriptor));
    }

    public void restoreFromCall(String descriptor, boolean hasFrame) {
        Type returnType = Type.getReturnType(descriptor);
        if (returnType.getSort() != Type.VOID) {
            if (hasFrame) {
                frameManager.getReturnTag();
            } else {
                super.visitInsn(Opcodes.ACONST_NULL);
            }
            if (returnType.getSize() == 2) {
                pushWide();
            } else {
                push();
            }
        }
    }

    public void performOperation(int opcode, int consumes, int produces) {
        if (consumes > shadowStackSize) {
            throw new NoSuchElementException();
        }
        for (int i = consumes - 1; i >= 0; i--) {
            super.visitVarInsn(Opcodes.ALOAD, getShadowStackIndex(i));
        }
        super.visitInsn(opcode);
        shadowStackSize += (produces - consumes);
        for (int i = 0; i < produces; i++) {
            super.visitVarInsn(Opcodes.ASTORE, getShadowStackIndex(i));
        }
    }

    public int getNextFreeVariable() {
        // Place the local variable after the top of the shadow stack
        return getShadowStackIndex(0) + 1;
    }

    static ShadowLocals newInstance(MethodVisitor mv, MethodNode original, boolean isShadow) {
        int frameIndex = original.maxLocals;
        FrameInitializer initializer;
        if (!isShadow) {
            int handlers = original.tryCatchBlocks == null ? 0 : original.tryCatchBlocks.size();
            initializer = new IndirectFrameInitializer(
                    mv, original.name.equals("<init>"), frameIndex, handlers, original.desc, original.access);
        } else {
            initializer = new DirectFrameInitializer(mv, frameIndex, original.access, original.desc);
        }
        return new ShadowLocals(new FrameManager(initializer), original);
    }
}
