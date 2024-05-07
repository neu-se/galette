package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.ASTORE;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
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
     * Label marking the start of local variables added by this visitor.
     * <p>
     * Non-null.
     */
    protected final Label localsStart = new Label();
    /**
     * Label marking the end of local variables added by this visitor.
     * <p>
     * Non-null.
     */
    protected final Label localsEnd = new Label();

    FrameInitializer(MethodVisitor mv, int frameIndex) {
        super(GaletteTransformer.ASM_VERSION, mv);
        this.frameIndex = frameIndex;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        super.visitLabel(localsStart);
        super.visitLocalVariable(
                GaletteNames.getShadowVariableName("frame"),
                GaletteNames.FRAME_DESCRIPTOR,
                null,
                localsStart,
                localsEnd,
                frameIndex);
        initializeFrame();
        super.visitVarInsn(ASTORE, frameIndex);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitLabel(localsEnd);
        super.visitMaxs(maxStack, maxLocals);
    }

    protected abstract void initializeFrame();

    public int getFrameIndex() {
        return frameIndex;
    }

    public int lastAddedLocalIndex() {
        return frameIndex;
    }

    public void appendAddedLocals(SimpleList<Object> locals) {
        locals.add(GaletteNames.FRAME_INTERNAL_NAME);
    }
}
