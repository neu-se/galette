package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.*;

import edu.neu.ccs.prl.galette.internal.runtime.mask.ClassMasks;
import edu.neu.ccs.prl.galette.internal.runtime.mask.UnsafeMasks;
import edu.neu.ccs.prl.galette.internal.transform.MaskRegistry.MaskInfo;
import java.io.ObjectStreamClass;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.tree.MethodNode;

class MaskApplier extends GeneratorAdapter {
    /**
     * Internal name for {@link ObjectStreamClass}.
     * <p>
     * Non-null.
     */
    static final String OBJECT_STREAM_CLASS_INTERNAL_NAME = Type.getInternalName(ObjectStreamClass.class);
    /**
     * Internal name for FieldReflector.
     * <p>
     * Non-null.
     */
    static final String FIELD_REFLECTOR_INTERNAL_NAME = "java/io/ObjectStreamClass$FieldReflector";
    /**
     * Internal name for {@link ClassMasks}.
     * <p>
     * Non-null.
     */
    static final String CLASS_MASKS_INTERNAL_NAME = Type.getInternalName(ClassMasks.class);
    /**
     * Internal name for {@link edu.neu.ccs.prl.galette.internal.runtime.mask.UnsafeMasks]}.
     * <p>
     * Non-null.
     */
    static final String UNSAFE_MASKS_INTERNAL_NAME = Type.getInternalName(UnsafeMasks.class);
    /**
     * Internal name of the class being visited.
     * <p>
     * Non-null.
     */
    private final String className;

    MaskApplier(String className, MethodNode mn) {
        this(className, mn.access, mn.name, mn.desc, mn);
    }

    MaskApplier(String className, int access, String name, String descriptor, MethodVisitor mv) {
        super(GaletteTransformer.ASM_VERSION, mv, access, name, descriptor);
        if (className == null) {
            throw new NullPointerException();
        }
        this.className = className;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        MaskInfo mask = MaskRegistry.getMask(owner, name, descriptor);
        if (mask != null && allowMask(mask)) {
            switch (mask.getType()) {
                case REPLACE:
                    mask.getRecord().accept(getDelegate());
                    return;
                case REPAIR_RETURN:
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    mask.getRecord().accept(getDelegate());
                    return;
                case FIX_ARGUMENTS:
                    mask.getRecord().accept(getDelegate());
                    spreadArguments(owner, opcode == INVOKESTATIC, descriptor);
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    return;
                case POST_PROCESS:
                    collectArguments(opcode == INVOKESTATIC, descriptor);
                    super.visitInsn(DUP);
                    // ..., arrayref, arrayref
                    spreadArguments(owner, opcode == INVOKESTATIC, descriptor);
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    // ..., arrayref, ret
                    if (!Type.getReturnType(descriptor).equals(Type.VOID_TYPE)) {
                        swap(Type.INT_TYPE, Type.getReturnType(descriptor));
                    }
                    // ..., ret, arrayref
                    spreadArguments(owner, opcode == INVOKESTATIC, descriptor);
                    mask.getRecord().accept(getDelegate());
                    return;
                default:
                    throw new AssertionError();
            }
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    private boolean allowMask(MaskInfo mask) {
        if (Configuration.isPropagateThroughSerialization()) {
            return !isDisabledForObjectStreamClass(mask) && !isDisabledForFieldReflector(mask);
        }
        return true;
    }

    private boolean isDisabledForFieldReflector(MaskInfo mask) {
        return className.equals(FIELD_REFLECTOR_INTERNAL_NAME)
                && UNSAFE_MASKS_INTERNAL_NAME.equals(mask.getRecord().getOwner());
    }

    private boolean isDisabledForObjectStreamClass(MaskInfo mask) {
        return className.equals(OBJECT_STREAM_CLASS_INTERNAL_NAME)
                && CLASS_MASKS_INTERNAL_NAME.equals(mask.getRecord().getOwner())
                && "getFields".equals(mask.getRecord().getName());
    }

    private void collectArguments(boolean isStatic, String descriptor) {
        // stack: ..., receiver?, arg_0, arg_1, ..., arg_{n-1}
        Type[] arguments = Type.getArgumentTypes(descriptor);
        int length = arguments.length + (isStatic ? 0 : 1);
        AsmUtil.pushInt(mv, length);
        super.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
        int index = length - 1;
        for (int i = arguments.length - 1; i >= 0; i--) {
            // stack: ..., receiver?, arg_0, arg_1, ..., arg_{i-1}, arg_{i}, arrayref
            Type argument = arguments[i];
            if (argument.getSize() == 1) {
                super.visitInsn(DUP_X1);
                super.visitInsn(SWAP);
            } else {
                super.visitInsn(DUP_X2);
                super.visitInsn(DUP_X2);
                super.visitInsn(POP);
            }
            box(argument);
            // stack: ..., receiver?, arg_0, arg_1, ..., arg_{i-1}, arrayref, arrayref, arg_{i}
            AsmUtil.pushInt(mv, index--);
            super.visitInsn(SWAP);
            // stack: ..., receiver?, arg_0, arg_1, ..., arg_{i-1}, arrayref, arrayref, index, arg_{i}
            super.visitInsn(AASTORE);
        }
        if (!isStatic) {
            // stack: ..., receiver, arrayref
            super.visitInsn(DUP_X1);
            super.visitInsn(SWAP);
            AsmUtil.pushInt(mv, index);
            super.visitInsn(SWAP);
            super.visitInsn(AASTORE);
        }
        // stack: ..., arrayref
    }

    private void spreadArguments(String owner, boolean isStatic, String descriptor) {
        // stack: ..., arrayref
        int index = 0;
        if (!isStatic) {
            super.visitInsn(DUP);
            AsmUtil.pushInt(mv, index++);
            // ..., arrayref, arrayref, index
            super.visitInsn(AALOAD);
            // ..., arrayref, receiver
            super.visitTypeInsn(CHECKCAST, owner);
            super.visitInsn(SWAP);
        }
        Type[] arguments = Type.getArgumentTypes(descriptor);
        for (Type argument : arguments) {
            // ..., receiver?, arg0, arg1_, ..., arg_{i-1}, arrayref
            super.visitInsn(DUP);
            AsmUtil.pushInt(mv, index++);
            // ..., receiver?, arg0, arg1_, ..., arg_{i - 1}, arrayref, arrayref, index
            super.visitInsn(AALOAD);
            // ..., receiver?, arg0, arg1_, ..., arg_{i - 1}, arrayref, arg_{i}
            unbox(argument);
            swap(Type.INT_TYPE, argument);
            // ..., receiver?, arg0, arg1_, ..., arg_{i}, arrayref
        }
        super.visitInsn(POP);
        // stack: ..., receiver?, arg_0, arg_1, ..., arg_{n-1}
    }
}
