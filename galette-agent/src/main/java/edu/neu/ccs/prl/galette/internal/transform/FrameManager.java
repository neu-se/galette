package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class FrameManager extends MethodVisitor {
    /**
     * Label marking the end of local variables added by this visitor.
     * <p>
     * Non-null.
     */
    private final Label localsEnd = new Label();
    /**
     * {@link MethodVisitor} responsible for initializing the {@link TagFrame} managed by the visitor.
     * <p>
     * Non-null.
     */
    private final FrameInitializer initializer;
    /**
     * Local variable index used to store the caller class for this method.
     */
    private final int callerIndex;

    FrameManager(FrameInitializer initializer) {
        super(GaletteTransformer.ASM_VERSION, initializer);
        this.initializer = initializer;
        this.callerIndex = initializer.lastAddedLocalIndex() + 1;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        Label localsStart = new Label();
        // Start the scope for added local variables
        super.visitLabel(localsStart);
        // Create and initialize the caller local variable
        super.visitLocalVariable(
                GaletteNames.getShadowVariableName("caller"),
                GaletteNames.CLASS_DESCRIPTOR,
                null,
                localsStart,
                localsEnd,
                callerIndex);
        loadFrame();
        Handle.FRAME_GET_CALLER.accept(mv);
        super.visitVarInsn(Opcodes.ASTORE, callerIndex);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        // End the scope for added local variables
        super.visitLabel(localsEnd);
        super.visitMaxs(maxStack, maxLocals);
    }

    int initializeShadowArguments(boolean isStatic, String descriptor, int shadowVariablesStart) {
        int variableIndex = shadowVariablesStart;
        loadFrame();
        // ..., frame
        int argumentIndex = 0;
        if (!isStatic) {
            // Initialize the local variable for receiver
            initializeShadowArgument(argumentIndex++, variableIndex++);
        }
        for (Type argument : Type.getArgumentTypes(descriptor)) {
            initializeShadowArgument(argumentIndex++, variableIndex++);
            // Add a variable for wide types (double/long)
            if (argument.getSize() == 2) {
                super.visitInsn(Opcodes.ACONST_NULL);
                super.visitVarInsn(Opcodes.ASTORE, variableIndex++);
            }
        }
        // ..., frame
        super.visitInsn(Opcodes.POP);
        return variableIndex;
    }

    private void initializeShadowArgument(int argumentIndex, int variableIndex) {
        // ..., frame
        super.visitInsn(Opcodes.DUP);
        AsmUtil.pushInt(mv, argumentIndex);
        // ..., frame, frame, index
        Handle.FRAME_GET_TAG.accept(mv);
        // ..., frame, tag
        super.visitVarInsn(Opcodes.ASTORE, variableIndex);
        // ..., frame
    }

    public int firstAddedLocalIndex() {
        return initializer.getFrameIndex();
    }

    public int lastAddedLocalIndex() {
        return callerIndex;
    }

    public void appendAddedLocals(SimpleList<Object> locals) {
        initializer.appendAddedLocals(locals);
        locals.add(GaletteNames.CLASS_INTERNAL_NAME);
    }

    /**
     * stack: ... -> frame
     */
    public void loadFrame() {
        super.visitVarInsn(Opcodes.ALOAD, initializer.getFrameIndex());
    }

    /**
     * stack: ... -> caller-class
     */
    public void loadCaller() {
        super.visitVarInsn(Opcodes.ALOAD, callerIndex);
    }

    /**
     * stack: ..., tag -> ...
     */
    public void setReturnTag() {
        loadFrame();
        mv.visitInsn(Opcodes.SWAP);
        // ..., frame, tag
        Handle.FRAME_SET_RETURN_TAG.accept(mv);
    }

    /**
     * stack: ... -> ..., tag
     */
    void getReturnTag() {
        loadFrame();
        Handle.FRAME_GET_RETURN_TAG.accept(mv);
    }

    void prepareForCall(ShadowLocals shadows, boolean isStatic, String descriptor) {
        int slots = AsmUtil.countArgumentSlots(isStatic, descriptor);
        int count = Type.getArgumentCount(descriptor);
        if (!isStatic) {
            count++;
        }
        loadFrame();
        AsmUtil.pushInt(mv, count);
        // ..., frame, count
        Handle.FRAME_ACQUIRE.accept(mv);
        // ..., frame
        int current = slots - 1;
        int index = 0;
        if (!isStatic) {
            // ..., frame
            AsmUtil.pushInt(mv, index++);
            shadows.peek(current--);
            // ..., frame, index, tag
            Handle.FRAME_SET_TAG.accept(mv);
            // ..., frame
        }
        for (Type argument : Type.getArgumentTypes(descriptor)) {
            // ..., frame
            AsmUtil.pushInt(mv, index++);
            shadows.peek(current);
            // ..., frame, index, tag
            Handle.FRAME_SET_TAG.accept(mv);
            // ..., frame
            // Skip over the extra slot used for wide types (double/long)
            current -= argument.getSize();
        }
        // ..., frame
    }
}
