package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class CallerSensitiveFixer extends MethodVisitor {
    private static final String JDK_CALLER_SENSITIVE_DESCRIPTOR = "Ljdk/internal/reflect/CallerSensitive;";
    private static final String SUN_CALLER_SENSITIVE_DESCRIPTOR = "Lsun/reflect/CallerSensitive;";
    private static final String JDK_REFLECTION_INTERNAL_NAME = "jdk/internal/reflect/Reflection";
    private static final String SUN_REFLECTION_INTERNAL_NAME = "sun/reflect/Reflection";
    private String reflectionInternalName = null;

    CallerSensitiveFixer(MethodVisitor mv) {
        super(PhosphorTransformer.ASM_VERSION, mv);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals(JDK_CALLER_SENSITIVE_DESCRIPTOR)) {
            reflectionInternalName = JDK_REFLECTION_INTERNAL_NAME;
        } else if (reflectionInternalName == null && descriptor.equals(SUN_CALLER_SENSITIVE_DESCRIPTOR)) {
            reflectionInternalName = SUN_REFLECTION_INTERNAL_NAME;
        }
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (reflectionInternalName != null && ShadowMethodCreator.isShadowMethod(descriptor)) {
            // Top of stack should be the frame
            super.visitMethodInsn(
                    Opcodes.INVOKESTATIC, reflectionInternalName, "getCallerClass", "()Ljava/lang/Class;", false);
            HandleRegistry.accept(getDelegate(), Handle.FRAME_SET_CALLER_CLASS);
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
}
