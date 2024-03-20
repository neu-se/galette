package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

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
        loadThisAndArguments(mv, isSet(access, Opcodes.ACC_STATIC), descriptor);
    }

    public static void loadThisAndArguments(MethodVisitor mv, boolean isStatic, String descriptor) {
        if (mv == null) {
            return;
        }
        if (!isStatic) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
        }
        loadArguments(mv, isStatic, descriptor);
    }

    public static void loadArguments(MethodVisitor mv, int access, String descriptor) {
        loadArguments(mv, isSet(access, Opcodes.ACC_STATIC), descriptor);
    }

    public static void loadArguments(MethodVisitor mv, boolean isStatic, String descriptor) {
        if (mv == null) {
            return;
        }
        // Skip "this" for virtual methods
        int varIndex = isStatic ? 0 : 1;
        for (Type argument : Type.getArgumentTypes(descriptor)) {
            mv.visitVarInsn(argument.getOpcode(Opcodes.ILOAD), varIndex);
            varIndex += argument.getSize();
        }
    }

    public static String[] copyExceptions(MethodNode mn) {
        return mn.exceptions == null ? null : mn.exceptions.toArray(new String[0]);
    }

    public static boolean hasMethodBody(int access) {
        return !isSet(access, Opcodes.ACC_NATIVE) && !isSet(access, Opcodes.ACC_ABSTRACT);
    }

    /**
     * Returns the number of local variables used for a method's receiver (if non-static) and arguments.
     */
    public static int countLocalVariables(int access, String descriptor) {
        return countLocalVariables(isSet(access, Opcodes.ACC_STATIC), descriptor);
    }

    /**
     * Returns the number of local variables used for a method's receiver (if non-static) and arguments.
     */
    public static int countLocalVariables(boolean isStatic, String descriptor) {
        int count = 0;
        for (Type argument : Type.getArgumentTypes(descriptor)) {
            count += argument.getSize();
        }
        return isStatic ? count : count + 1;
    }

    @SuppressWarnings("ExplicitArrayFilling")
    public static Object[] createTopArray(int length) {
        Object[] array = new Object[length];
        for (int i = 0; i < array.length; i++) {
            array[i] = Opcodes.TOP;
        }
        return array;
    }

    public static boolean isReturn(int opcode) {
        return IRETURN <= opcode && opcode <= RETURN;
    }
}
