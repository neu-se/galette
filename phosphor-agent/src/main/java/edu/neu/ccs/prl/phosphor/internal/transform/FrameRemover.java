package edu.neu.ccs.prl.phosphor.internal.transform;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

class FrameRemover extends ClassVisitor {
    FrameRemover(ClassVisitor cv) {
        super(PhosphorTransformer.ASM_VERSION, cv);
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor delegate = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new MethodVisitor(api, delegate) {
            @Override
            public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
                // Remove the frame
            }
        };
    }
}
