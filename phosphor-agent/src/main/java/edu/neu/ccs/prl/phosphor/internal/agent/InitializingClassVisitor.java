package edu.neu.ccs.prl.phosphor.internal.agent;

import edu.neu.ccs.prl.phosphor.internal.runtime.unsafe.UnsafeWrapper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Adds a class initialization method to a class if it does not already have one.
 * Adds code to the class initialization method to define and load its shadow class.
 */
final class InitializingClassVisitor extends ClassVisitor {
    /**
     * True if a class initialization method has been visited.
     */
    private boolean visitedClassInitializer;
    /**
     * Name of the class being visited.
     */
    private String className;
    /**
     * {@code true} if the version of the class being visited is not at least the required minimum version for the LDC
     * of a constant class.
     */
    private boolean fixLdcClass;

    private final byte[] shadowClassBuffer;

    InitializingClassVisitor(ClassVisitor cv, byte[] shadowClassBuffer) {
        super(PhosphorAgent.ASM_VERSION, cv);
        this.shadowClassBuffer = shadowClassBuffer;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.fixLdcClass = (version & 0xFFFF) < Opcodes.V1_5;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if ("<clinit>".equals(name)) {
            visitedClassInitializer = true;
            mv = new InitializingMethodVisitor(mv);
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        // Add a class initialization method if one was not visited
        if (!visitedClassInitializer) {
            MethodVisitor mv =
                    super.visitMethod(Opcodes.ACC_SYNTHETIC | Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
            mv = new InitializingMethodVisitor(mv);
            mv.visitCode();
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        super.visitEnd();
    }

    private void loadClass(MethodVisitor mv) {
        if (fixLdcClass) {
            // Since the class is not at least the required version 1.5 for the ldc of a constant class, push the class
            // onto the stack by making a call to Class.forName
            mv.visitLdcInsn(className.replace("/", "."));
            mv.visitInsn(Opcodes.ACONST_NULL);

            mv.visitMethodInsn(
                    Opcodes.INVOKESTATIC, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;", false);
        } else {
            // Directly push the class onto the stack
            mv.visitLdcInsn(Type.getObjectType(className));
        }
    }

    private final class InitializingMethodVisitor extends MethodVisitor {
        private InitializingMethodVisitor(MethodVisitor mv) {
            super(PhosphorAgent.ASM_VERSION, mv);
        }

        @Override
        public void visitCode() {
            super.visitCode();
            loadClass(this);
            super.visitLdcInsn(ShadowClassBuilder.getShadowClassName(className));
            pushByteArray(this, shadowClassBuffer);
            super.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    Type.getInternalName(UnsafeWrapper.class),
                    "defineAndLoadShadow",
                    "(Ljava/lang/Class;Ljava/lang/String;[B)V",
                    true);
        }
    }

    public static void pushInt(MethodVisitor delegate, int value) {
        if (value >= -1 && value <= 5) {
            delegate.visitInsn(Opcodes.ICONST_0 + value);
        } else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            delegate.visitIntInsn(Opcodes.BIPUSH, value);
        } else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
            delegate.visitIntInsn(Opcodes.SIPUSH, value);
        } else {
            delegate.visitLdcInsn(value);
        }
    }

    public static void pushByteArray(MethodVisitor mv, byte[] a) {
        pushInt(mv, a.length);
        mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_BYTE);
        for (int i = 0; i < a.length; i++) {
            mv.visitInsn(Opcodes.DUP);
            pushInt(mv, i);
            pushInt(mv, a[i]);
            mv.visitInsn(Opcodes.BASTORE);
        }
    }
}
