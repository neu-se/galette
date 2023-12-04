package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import org.objectweb.asm.MethodVisitor;

// TODO
class TagPropagator extends MethodVisitor {
    TagPropagator(MethodVisitor mv) {
        super(PhosphorTransformer.ASM_VERSION, mv);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        // TODO
        if (opcode == -1 && !owner.equals("java/lang/Object") && !owner.startsWith("[")) {
            name = ShadowMethodAdder.getShadowMethodName(name);
            descriptor = ShadowMethodAdder.getShadowMethodDescriptor(descriptor);
            HandleRegistry.accept(mv, Handle.FRAME_GET_INSTANCE);
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
}
