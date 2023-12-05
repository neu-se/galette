package edu.neu.ccs.prl.phosphor.internal.transform;

import org.objectweb.asm.MethodVisitor;

class MaskApplier extends MethodVisitor {
    MaskApplier(MethodVisitor mv) {
        super(PhosphorTransformer.ASM_VERSION, mv);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        MethodRecord mask = MaskRegistry.getMask(owner, name, descriptor);
        if (mask != null) {
            mask.accept(getDelegate());
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }
}
