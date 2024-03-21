package edu.neu.ccs.prl.galette.internal.transform;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class PrimitiveBoxer {
    private static final Type BYTE_TYPE = Type.getObjectType("java/lang/Byte");

    private static final Type BOOLEAN_TYPE = Type.getObjectType("java/lang/Boolean");

    private static final Type SHORT_TYPE = Type.getObjectType("java/lang/Short");

    private static final Type CHARACTER_TYPE = Type.getObjectType("java/lang/Character");

    private static final Type INTEGER_TYPE = Type.getObjectType("java/lang/Integer");

    private static final Type FLOAT_TYPE = Type.getObjectType("java/lang/Float");

    private static final Type LONG_TYPE = Type.getObjectType("java/lang/Long");

    private static final Type DOUBLE_TYPE = Type.getObjectType("java/lang/Double");

    private PrimitiveBoxer() {
        throw new AssertionError(getClass() + " is a static utility class");
    }

    public static Type getBoxedType(Type type) {
        switch (type.getSort()) {
            case Type.BYTE:
                return BYTE_TYPE;
            case Type.BOOLEAN:
                return BOOLEAN_TYPE;
            case Type.SHORT:
                return SHORT_TYPE;
            case Type.CHAR:
                return CHARACTER_TYPE;
            case Type.INT:
                return INTEGER_TYPE;
            case Type.FLOAT:
                return FLOAT_TYPE;
            case Type.LONG:
                return LONG_TYPE;
            case Type.DOUBLE:
                return DOUBLE_TYPE;
            default:
                return type;
        }
    }

    public static void box(MethodVisitor delegate, Type type) {
        if (type == Type.VOID_TYPE) {
            throw new IllegalArgumentException();
        } else if (delegate != null && type.getSort() != Type.OBJECT && type.getSort() != Type.ARRAY) {
            Type boxedType = getBoxedType(type);
            delegate.visitTypeInsn(Opcodes.NEW, boxedType.getInternalName());
            if (type.getSize() == 2) {
                delegate.visitInsn(Opcodes.DUP_X2);
                delegate.visitInsn(Opcodes.DUP_X2);
                delegate.visitInsn(Opcodes.POP);
            } else {
                delegate.visitInsn(Opcodes.DUP_X1);
                delegate.visitInsn(Opcodes.SWAP);
            }
            String descriptor = Type.getMethodDescriptor(Type.VOID_TYPE, type);
            delegate.visitMethodInsn(Opcodes.INVOKESPECIAL, boxedType.getInternalName(), "<init>", descriptor, false);
        }
    }
}
