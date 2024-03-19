package edu.neu.ccs.prl.galette.internal.transform;

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
     * Internal name for {@link TagFrame}.
     * <p>
     * Non-null.
     */
    static final String FRAME_INTERNAL_NAME = Type.getInternalName(TagFrame.class);
    /**
     * Descriptor for {@link TagFrame}.
     * <p>
     * Non-null.
     */
    static final String FRAME_DESCRIPTOR = Type.getDescriptor(TagFrame.class);
    /**
     * Internal name for {@link Tag}.
     * <p>
     * Non-null.
     */
    static final String TAG_INTERNAL_NAME = Type.getInternalName(Tag.class);
    /**
     * Descriptor for {@link Tag}.
     * <p>
     * Non-null.
     */
    static final String TAG_DESCRIPTOR = Type.getDescriptor(Tag.class);
    /**
     * {@code true} if the method being visited was passed a
     * {@link TagFrame} as an argument.
     */
    private final boolean isShadow;
    /**
     * The uninstrumented version of the method being visited.
     * <p>
     * Non-null.
     */
    private final MethodNode original;
    /**
     * Local variable index used to store the {@link TagFrame}
     * for this method.
     */
    private final int frameIndex;
    /**
     * Local variable index used to store the {@link TagFrame}
     * for methods called by this method.
     */
    private final int childFrameIndex;
    /**
     * Label marking the end of the frame local variable's scope.
     * <p>
     * Non-null.
     */
    private final Label frameEnd = new Label();
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

    ShadowLocals(MethodVisitor mv, MethodNode original, boolean isShadow) {
        super(GaletteTransformer.ASM_VERSION, mv);
        if (original == null) {
            throw new NullPointerException();
        }
        this.original = original;
        this.isShadow = isShadow;
        this.frameIndex = original.maxLocals;
        this.childFrameIndex = frameIndex + 1;
        this.shadowVariablesStart = childFrameIndex + 1;
        this.shadowStackStart = shadowVariablesStart + original.maxLocals;
        this.shadowStackSize = 0;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        findAndStoreTagFrame();
        initializeChildFrameSlot();
        int varIndex = isShadow ? initializeArgumentTags() : shadowVariablesStart;
        // Initialize remaining shadow variables
        for (; varIndex < shadowStackStart; varIndex++) {
            super.visitInsn(Opcodes.ACONST_NULL);
            super.visitVarInsn(Opcodes.ASTORE, varIndex);
        }
    }

    private void findAndStoreTagFrame() {
        Label frameStart = new Label();
        super.visitLabel(frameStart);
        super.visitLocalVariable(
                getShadowVariableName("frame"), FRAME_DESCRIPTOR, null, frameStart, frameEnd, frameIndex);
        if (isShadow) {
            int varIndex = AsmUtil.countLocalVariables(original.access, original.desc);
            super.visitVarInsn(Opcodes.ALOAD, varIndex);
        } else if (original.name.equals("<clinit>")) {
            Handle.FRAME_CREATE_EMPTY.accept(mv);
        } else if (original.name.equals("<init>")) {
            mv.visitLdcInsn(original.desc);
            Handle.FRAME_STACK_PEEK.accept(mv);
        } else {
            // TODO Pop the frame and restore before return
            mv.visitLdcInsn(original.desc);
            Handle.FRAME_STACK_PEEK.accept(mv);
        }
        super.visitVarInsn(Opcodes.ASTORE, frameIndex);
    }

    private void initializeChildFrameSlot() {
        Label frameStart = new Label();
        super.visitLabel(frameStart);
        super.visitLocalVariable(
                getShadowVariableName("childFrame"), FRAME_DESCRIPTOR, null, frameStart, frameEnd, childFrameIndex);
        super.visitInsn(Opcodes.ACONST_NULL);
        super.visitVarInsn(Opcodes.ASTORE, childFrameIndex);
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
        // Fill in TOP until we get to the frame index
        for (; varIndex < frameIndex; varIndex++) {
            newLocal.add(Opcodes.TOP);
        }
        // Add the frame and child frame slots
        newLocal.add(FRAME_INTERNAL_NAME);
        newLocal.add(FRAME_INTERNAL_NAME);
        varIndex += 2;
        // Recompute the shadow stack size
        shadowStackSize = startingHandler ? 0 : computeNumberOfSlots(numStack, stack);
        // Add tags for the shadow local variables and shadow runtime stack
        for (; varIndex < shadowStackStart + shadowStackSize; varIndex++) {
            newLocal.add(TAG_INTERNAL_NAME);
        }
        super.visitFrame(type, newLocal.size(), newLocal.toArray(new Object[newLocal.size()]), numStack, stack);
        if (startingHandler) {
            // Add a tag for the exception
            Handle.TAG_GET_EMPTY.accept(mv);
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
                getShadowVariableName(name), TAG_DESCRIPTOR, null, start, end, getShadowVariableIndex(index));
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitLabel(frameEnd);
        super.visitMaxs(maxStack, maxLocals);
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
        if (index >= frameIndex) {
            throw new IllegalArgumentException();
        }
        return index + shadowVariablesStart;
    }

    public void loadTagFrame() {
        super.visitVarInsn(Opcodes.ALOAD, frameIndex);
    }

    static String getShadowVariableName(String name) {
        return name + "$$GALETTE";
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

    private int initializeArgumentTags() {
        int varIndex = shadowVariablesStart;
        loadTagFrame();
        // frame
        boolean isStatic = AsmUtil.isSet(original.access, Opcodes.ACC_STATIC);
        if (!isStatic) {
            // Initialize local variable for receiver
            mv.visitInsn(Opcodes.DUP);
            // frame, frame
            Handle.FRAME_DEQUEUE.accept(mv);
            // frame, frame, tag
            super.visitVarInsn(Opcodes.ASTORE, varIndex++);
        }
        for (Type argument : Type.getArgumentTypes(original.desc)) {
            mv.visitInsn(Opcodes.DUP);
            // frame, frame
            Handle.FRAME_DEQUEUE.accept(mv);
            // frame, frame, tag
            super.visitVarInsn(Opcodes.ASTORE, varIndex++);
            // frame
            // Add extra slot used for wide types (double/long)
            if (argument.getSize() == 2) {
                super.visitInsn(Opcodes.ACONST_NULL);
                super.visitVarInsn(Opcodes.ASTORE, varIndex++);
            }
        }
        // frame
        mv.visitInsn(Opcodes.POP);
        return varIndex;
    }

    public void prepareForCall(boolean isStatic, String descriptor, boolean createFrame) {
        int slots = AsmUtil.countLocalVariables(isStatic, descriptor);
        if (createFrame) {
            loadTagFrame();
            // frame
            Handle.FRAME_CREATE_FOR_CALL.accept(mv);
            // frame (child)
            int current = slots - 1;
            if (!isStatic) {
                peek(current--);
                // frame, tag
                Handle.FRAME_ENQUEUE.accept(mv);
                // frame
            }
            for (Type argument : Type.getArgumentTypes(descriptor)) {
                peek(current);
                // frame, tag
                Handle.FRAME_ENQUEUE.accept(mv);
                // frame
                // Skip over the extra slot used for wide types (double/long)
                current -= argument.getSize();
            }
            super.visitInsn(Opcodes.DUP);
            // frame, frame
            super.visitVarInsn(Opcodes.ASTORE, childFrameIndex);
            // frame
        }
        pop(slots);
    }

    public void restoreFromCall(String descriptor, boolean hasFrame) {
        Type returnType = Type.getReturnType(descriptor);
        if (returnType.getSort() != Type.VOID) {
            if (hasFrame) {
                // Get the frame from the shadow stack
                super.visitVarInsn(Opcodes.ALOAD, childFrameIndex);
                Handle.FRAME_GET_RETURN_TAG.accept(mv);
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
}
