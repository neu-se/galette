package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import edu.neu.ccs.prl.phosphor.internal.runtime.Tag;
import edu.neu.ccs.prl.phosphor.internal.runtime.collection.SimpleList;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

class ShadowLocals extends MethodVisitor {
    /**
     * Internal name for {@link PhosphorFrame}.
     * <p>
     * Non-null.
     */
    private static final String FRAME_INTERNAL_NAME = Type.getInternalName(PhosphorFrame.class);
    /**
     * Descriptor for {@link PhosphorFrame}.
     * <p>
     * Non-null.
     */
    private static final String FRAME_DESCRIPTOR = Type.getDescriptor(PhosphorFrame.class);
    /**
     * Internal name for {@link Tag}.
     * <p>
     * Non-null.
     */
    private static final String TAG_INTERNAL_NAME = Type.getInternalName(Tag.class);
    /**
     * Descriptor for {@link Tag}.
     * <p>
     * Non-null.
     */
    private static final String TAG_DESCRIPTOR = Type.getDescriptor(Tag.class);
    /**
     * {@code true} if the method being visited was passed a
     * {@link PhosphorFrame} as an argument.
     */
    private final boolean isShadow;
    /**
     * The uninstrumented version of the method being visited.
     * <p>
     * Non-null.
     */
    private final MethodNode original;
    /**
     * Local variable index used to store the {@link PhosphorFrame}
     * for this method.
     */
    private final int frameIndex;
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
     * Index of the first local variable used to the {@link Tag} for an element of the runtime stack.
     * This will always be the index of the local variable used to store the {@link Tag} for the bottom of the stack.
     */
    private final int shadowStackStart;
    /**
     * Number of elements currently in the shadow stack.
     */
    private int shadowStackSize;

    ShadowLocals(MethodVisitor mv, MethodNode original, boolean isShadow) {
        super(PhosphorTransformer.ASM_VERSION, mv);
        if (original == null) {
            throw new NullPointerException();
        }
        this.original = original;
        this.isShadow = isShadow;
        this.frameIndex = original.maxLocals;
        this.shadowVariablesStart = frameIndex + 1;
        this.shadowStackStart = shadowVariablesStart + original.maxLocals;
        this.shadowStackSize = 0;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        findAndStorePhosphorFrame();
        int varIndex = isShadow ? initializeArgumentTags() : shadowVariablesStart;
        // Initialize remaining shadow variables
        for (; varIndex < shadowStackStart; varIndex++) {
            super.visitInsn(Opcodes.ACONST_NULL);
            super.visitVarInsn(Opcodes.ASTORE, varIndex);
        }
    }

    private int initializeArgumentTags() {
        int varIndex = shadowVariablesStart;
        loadPhosphorFrame();
        if (!AsmUtil.isSet(original.access, Opcodes.ACC_STATIC)) {
            mv.visitInsn(Opcodes.DUP);
            Handle.FRAME_POP.accept(mv);
            super.visitVarInsn(Opcodes.ASTORE, varIndex++);
        }
        for (Type argument : Type.getArgumentTypes(original.desc)) {
            mv.visitInsn(Opcodes.DUP);
            Handle.FRAME_POP.accept(mv);
            super.visitVarInsn(Opcodes.ASTORE, varIndex++);
            if (argument.getSize() == 2) {
                super.visitInsn(Opcodes.ACONST_NULL);
                super.visitVarInsn(Opcodes.ASTORE, varIndex++);
            }
        }
        mv.visitInsn(Opcodes.POP);
        return varIndex;
    }

    private void findAndStorePhosphorFrame() {
        Label frameStart = new Label();
        super.visitLabel(frameStart);
        super.visitLocalVariable(
                getShadowVariableName("phosphorFrame"), FRAME_DESCRIPTOR, null, frameStart, frameEnd, frameIndex);
        if (isShadow) {
            int varIndex = AsmUtil.isSet(original.access, Opcodes.ACC_STATIC) ? 0 : 1;
            for (Type argument : Type.getArgumentTypes(original.desc)) {
                varIndex += argument.getSize();
            }
            super.visitVarInsn(Opcodes.ALOAD, varIndex);
        } else {
            Handle.FRAME_GET_INSTANCE.accept(mv);
        }
        super.visitVarInsn(Opcodes.ASTORE, frameIndex);
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        SimpleList<Object> newLocal = new SimpleList<>();
        int varIndex = 0;
        // Copy the original locals
        for (int i = 0; i < numLocal; i++) {
            Object localElement = local[i];
            newLocal.add(localElement);
            if (localElement == Opcodes.LONG || localElement == Opcodes.DOUBLE) {
                // Longs and doubles take two variable slots but are only represented once in the frame
                varIndex += 2;
            } else {
                varIndex++;
            }
        }
        // Fill in TOP until we get to the frame index
        for (; varIndex < frameIndex; varIndex++) {
            newLocal.add(Opcodes.TOP);
        }
        // Add the frame
        newLocal.add(FRAME_INTERNAL_NAME);
        varIndex++;
        // Add Tags for the shadow local variables and shadow runtime stack
        for (; varIndex < shadowStackStart + shadowStackSize; varIndex++) {
            newLocal.add(TAG_INTERNAL_NAME);
        }
        super.visitFrame(type, newLocal.size(), newLocal.toArray(new Object[newLocal.size()]), numStack, stack);
    }

    @Override
    public void visitLocalVariable(
            String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
        if (name.equals("this") && index == 0) {
            // The $ prefix is necessary for some debuggers to pick up this local variable entry
            name = "$this";
        }
        super.visitLocalVariable(getShadowVariableName(name), TAG_DESCRIPTOR, null, start, end, getShadowIndex(index));
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitLabel(frameEnd);
        super.visitMaxs(maxStack, maxLocals);
    }

    public void storeShadowVar(int index) {
        // [Tag] -> []
        super.visitVarInsn(Opcodes.ASTORE, getShadowIndex(index));
    }

    public void loadShadowVar(int index) {
        // [] -> [Tag]
        super.visitVarInsn(Opcodes.ALOAD, getShadowIndex(index));
    }

    private int getShadowIndex(int index) {
        return index + shadowVariablesStart;
    }

    public void loadPhosphorFrame() {
        super.visitVarInsn(Opcodes.ALOAD, frameIndex);
    }

    private static String getShadowVariableName(String name) {
        return name + "$$PHOSPHOR";
    }
}
