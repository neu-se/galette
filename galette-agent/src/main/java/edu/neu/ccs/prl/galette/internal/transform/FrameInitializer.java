package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.ASTORE;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * Initializes the tag frame of methods.
 */
abstract class FrameInitializer extends MethodVisitor {
    /**
     * Local variable index used to store the {@link TagFrame} for this method.
     */
    private final int frameIndex;
    /**
     * Label marking the end of the frame local variable's scope.
     * <p>
     * Non-null.
     */
    private final Label frameEnd = new Label();

    FrameInitializer(MethodVisitor mv, int frameIndex) {
        super(GaletteTransformer.ASM_VERSION, mv);
        this.frameIndex = frameIndex;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        Label frameStart = new Label();
        super.visitLabel(frameStart);
        super.visitLocalVariable(
                GaletteNames.getShadowVariableName("frame"),
                GaletteNames.FRAME_DESCRIPTOR,
                null,
                frameStart,
                frameEnd,
                frameIndex);
        loadFrame();
        super.visitVarInsn(ASTORE, frameIndex);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitLabel(frameEnd);
        super.visitMaxs(maxStack, maxLocals);
    }

    protected abstract void loadFrame();

    public int getFrameIndex() {
        return frameIndex;
    }
}
