package edu.neu.ccs.prl.phosphor.internal.transform;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public final class AsmUtil {
    private AsmUtil() {
        throw new AssertionError();
    }

    public static boolean isSet(int access, int flag) {
        return (access & flag) != 0;
    }

    public static int makePublic(int access) {
        access &= ~Opcodes.ACC_PRIVATE;
        access &= ~Opcodes.ACC_PROTECTED;
        return access | Opcodes.ACC_PUBLIC;
    }

    public static byte[] toBytes(ClassNode cn) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }

    /**
     * Loads the specified int value onto the stack. If the specified delegate if {@code null} does nothing.
     *
     * @param delegate the method visitor that should be used to load the specified value onto the stack
     * @param value    the value to be pushed onto the stack
     */
    public static void pushInt(MethodVisitor delegate, int value) {
        if (delegate == null) {
            return;
        }
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

    public static void loadThisAndArguments(MethodVisitor mv, int access, String descriptor) {
        if (mv == null) {
            return;
        }
        if (!AsmUtil.isSet(access, Opcodes.ACC_STATIC)) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
        }
        loadArguments(mv, access, descriptor);
    }

    public static void loadArguments(MethodVisitor mv, int access, String descriptor) {
        if (mv == null) {
            return;
        }
        // Skip "this" for virtual methods
        int varIndex = AsmUtil.isSet(access, Opcodes.ACC_STATIC) ? 0 : 1;
        for (Type argument : Type.getArgumentTypes(descriptor)) {
            mv.visitVarInsn(argument.getOpcode(Opcodes.ILOAD), varIndex);
            varIndex += argument.getSize();
        }
    }
}
