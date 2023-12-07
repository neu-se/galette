package edu.neu.ccs.prl.phosphor.internal.transform;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

class AccessModifier extends ClassVisitor {
    AccessModifier(ClassVisitor classVisitor) {
        super(PhosphorTransformer.ASM_VERSION, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, AsmUtil.makePublic(access), name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String descriptor, String signature, String[] exceptions) {
        return super.visitMethod(AsmUtil.makePublic(access), name, descriptor, signature, exceptions);
    }

    public static boolean isApplicable(String className) {
        return "sun/misc/Unsafe".equals(className)
                || "jdk/internal/misc/Unsafe".equals(className)
                || "java/lang/ClassLoader".equals(className);
    }
}
