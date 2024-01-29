package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.transform.MaskRegistry.MaskInfo;
import org.objectweb.asm.MethodVisitor;

class MaskApplier extends MethodVisitor {
    MaskApplier(MethodVisitor mv) {
        super(GaletteTransformer.ASM_VERSION, mv);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        MaskInfo mask = MaskRegistry.getMask(owner, name, descriptor);
        if (mask != null) {
            switch (mask.getType()) {
                case REPLACE:
                    mask.getRecord().accept(getDelegate());
                    return;
                case REPAIR_RETURN:
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    mask.getRecord().accept(getDelegate());
                    return;
                default:
                    throw new AssertionError();
            }
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
}
