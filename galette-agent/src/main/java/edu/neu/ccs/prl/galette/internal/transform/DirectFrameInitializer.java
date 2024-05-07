package edu.neu.ccs.prl.galette.internal.transform;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class DirectFrameInitializer extends FrameInitializer {
    /**
     * Access modifier for the uninstrumented version of the method being visited.
     */
    private final int access;
    /**
     * Descriptor for the uninstrumented version of the method being visited.
     * <p>
     * Non-null.
     */
    private final String descriptor;

    DirectFrameInitializer(MethodVisitor mv, int frameIndex, int access, String descriptor) {
        super(mv, frameIndex);
        if (descriptor == null) {
            throw new NullPointerException();
        }
        this.access = access;
        this.descriptor = descriptor;
    }

    @Override
    protected void initializeFrame() {
        int varIndex = AsmUtil.countArgumentSlots(access, descriptor);
        super.visitVarInsn(Opcodes.ALOAD, varIndex);
    }
}
