package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.*;

import edu.neu.ccs.prl.galette.internal.transform.MaskRegistry.MaskInfo;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.tree.MethodNode;

class MaskApplier extends GeneratorAdapter {
    MaskApplier(MethodNode mn) {
        this(mn.access, mn.name, mn.desc, mn);
    }

    MaskApplier(int access, String name, String descriptor, MethodVisitor mv) {
        super(GaletteTransformer.ASM_VERSION, mv, access, name, descriptor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        MaskInfo mask = MaskRegistry.getMask(owner, name, descriptor);
        if (mask != null) {
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
                default:
                    throw new AssertionError();
            }
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
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
