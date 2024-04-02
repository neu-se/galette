package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.*;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

public final class AsmUtil {
    private AsmUtil() {
        throw new AssertionError(getClass() + " is a static utility class");
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

    public static void storeReceiverAndArguments(
            MethodVisitor delegate, boolean isStatic, String descriptor, int varIndex) {
        if (delegate == null) {
            return;
        }
        // stack: ..., receiver?, arg_0, arg_1, ..., arg_{n-1}
        int index = varIndex + countLocalVariables(isStatic, descriptor);
        Type[] arguments = Type.getArgumentTypes(descriptor);
        // Last argument is on the top of the stack; visit types in reverse order
        for (int i = arguments.length - 1; i >= 0; i--) {
            // stack: receiver?, arg_0, arg_1, ..., arg_i
            Type argument = arguments[i];
            index -= argument.getSize();
            delegate.visitVarInsn(argument.getOpcode(ISTORE), index);
        }
        if (!isStatic) {
            delegate.visitVarInsn(ASTORE, --index);
        }
        assert index == varIndex;
        // stack: ...
    }

    public static void loadReceiverAndArguments(
            MethodVisitor delegate, boolean isStatic, String descriptor, int varIndex) {
        // stack: ...
        if (delegate == null) {
            return;
        }
        if (!isStatic) {
            delegate.visitVarInsn(ALOAD, varIndex++);
        }
        loadArguments(delegate, descriptor, varIndex);
        // stack: ..., receiver?, arg_0, arg_1, ..., arg_{n-1}
    }

    public static void loadArguments(MethodVisitor delegate, String descriptor, int varIndex) {
        // stack: ...
        if (delegate == null) {
            return;
        }
        for (Type argument : Type.getArgumentTypes(descriptor)) {
            delegate.visitVarInsn(argument.getOpcode(ILOAD), varIndex);
            varIndex += argument.getSize();
        }
        // stack: ..., arg_0, arg_1, ..., arg_{n-1}
    }

    public static void collectArguments(MethodVisitor delegate, boolean isStatic, String descriptor) {
        // stack: ..., receiver?, arg_0, arg_1, ..., arg_{n-1}
        if (delegate == null) {
            return;
        }
        Type[] arguments = Type.getArgumentTypes(descriptor);
        int length = arguments.length + (isStatic ? 0 : 1);
        pushInt(delegate, length);
        delegate.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
        int index = length - 1;
        for (int i = arguments.length - 1; i >= 0; i--) {
            // stack: ..., receiver?, arg_0, arg_1, ..., arg_{i-1}, arg_{i}, arrayref
            Type argument = arguments[i];
            if (argument.getSize() == 1) {
                delegate.visitInsn(DUP_X1);
                delegate.visitInsn(SWAP);
            } else {
                delegate.visitInsn(DUP_X2);
                delegate.visitInsn(DUP_X2);
                delegate.visitInsn(POP);
            }
            box(delegate, argument);
            // stack: ..., receiver?, arg_0, arg_1, ..., arg_{i-1}, arrayref, arrayref, arg_{i}
            pushInt(delegate, index--);
            delegate.visitInsn(SWAP);
            // stack: ..., receiver?, arg_0, arg_1, ..., arg_{i-1}, arrayref, arrayref, index, arg_{i}
            delegate.visitInsn(AASTORE);
        }
        if (!isStatic) {
            // stack: ..., receiver, arrayref
            delegate.visitInsn(DUP_X1);
            delegate.visitInsn(SWAP);
            pushInt(delegate, index);
            delegate.visitInsn(SWAP);
            delegate.visitInsn(AASTORE);
        }
        // stack: ..., arrayref
    }

    public static void spreadArguments(MethodVisitor delegate, String owner, boolean isStatic, String descriptor) {
        // stack: ..., arrayref
        int index = 0;
        if (!isStatic) {
            delegate.visitInsn(DUP);
            pushInt(delegate, index++);
            // ..., arrayref, arrayref, index
            delegate.visitInsn(AALOAD);
            // ..., arrayref, receiver
            delegate.visitTypeInsn(CHECKCAST, owner);
            delegate.visitInsn(SWAP);
        }
        Type[] arguments = Type.getArgumentTypes(descriptor);
        for (Type argument : arguments) {
            // ..., receiver?, arg0, arg1_, ..., arg_{i-1}, arrayref
            delegate.visitInsn(DUP);
            pushInt(delegate, index++);
            // ..., receiver?, arg0, arg1_, ..., arg_{i - 1}, arrayref, arrayref, index
            delegate.visitInsn(AALOAD);
            // ..., receiver?, arg0, arg1_, ..., arg_{i - 1}, arrayref, arg_{i}
            unbox(delegate, argument);
            if (argument.getSize() == 1) {
                delegate.visitInsn(SWAP);
            } else {
                // arrayref, value, top
                delegate.visitInsn(DUP2_X1);
                delegate.visitInsn(POP2);
            }
            // ..., receiver?, arg0, arg1_, ..., arg_{i}, arrayref
        }
        delegate.visitInsn(POP);
        // stack: ..., receiver?, arg_0, arg_1, ..., arg_{n-1}
    }

    public static void box(MethodVisitor delegate, Type type) {
        if (delegate != null) {
            Handle boxer = getBoxer(type);
            if (boxer != null) {
                // stack: ..., primitive
                boxer.accept(delegate);
                // stack: ..., boxed
            }
        }
    }

    public static Handle getBoxer(Type type) {
        switch (type.getSort()) {
            case Type.CHAR:
                return Handle.BOX_CHAR;
            case Type.BOOLEAN:
                return Handle.BOX_BOOLEAN;
            case Type.DOUBLE:
                return Handle.BOX_DOUBLE;
            case Type.FLOAT:
                return Handle.BOX_FLOAT;
            case Type.LONG:
                return Handle.BOX_LONG;
            case Type.INT:
                return Handle.BOX_INT;
            case Type.SHORT:
                return Handle.BOX_SHORT;
            case Type.BYTE:
                return Handle.BOX_BYTE;
            default:
                return null;
        }
    }

    public static void unbox(MethodVisitor delegate, Type type) {
        if (delegate != null) {
            Handle unboxer = getUnboxer(type);
            if (unboxer != null) {
                String name =
                        Type.getReturnType(unboxer.getRecord().getDescriptor()).getInternalName();
                delegate.visitTypeInsn(CHECKCAST, name);
                unboxer.accept(delegate);
            } else {
                delegate.visitTypeInsn(CHECKCAST, type.getInternalName());
            }
        }
    }

    public static Handle getUnboxer(Type type) {
        switch (type.getSort()) {
            case Type.CHAR:
                return Handle.UNBOX_CHAR;
            case Type.BOOLEAN:
                return Handle.UNBOX_BOOLEAN;
            case Type.DOUBLE:
                return Handle.UNBOX_DOUBLE;
            case Type.FLOAT:
                return Handle.UNBOX_FLOAT;
            case Type.LONG:
                return Handle.UNBOX_LONG;
            case Type.INT:
                return Handle.UNBOX_INT;
            case Type.SHORT:
                return Handle.UNBOX_SHORT;
            case Type.BYTE:
                return Handle.UNBOX_BYTE;
            default:
                return null;
        }
    }
}
