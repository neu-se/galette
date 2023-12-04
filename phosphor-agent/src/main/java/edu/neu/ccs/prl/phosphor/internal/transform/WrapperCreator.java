package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

class WrapperCreator extends MethodVisitor {
    /**
     * Type for {@link PhosphorFrame}.
     * <p>
     * Non-null.
     */
    private static final Type FRAME_TYPE = Type.getType(PhosphorFrame.class);

    private final String owner;
    private final boolean isInterface;
    private final MethodNode caller;
    private final MethodNode callee;

    WrapperCreator(String owner, boolean isInterface, MethodNode caller, MethodNode callee) {
        super(PhosphorTransformer.ASM_VERSION, caller);
        this.isInterface = isInterface;
        this.owner = owner;
        this.caller = caller;
        this.callee = callee;
    }

    @Override
    public void visitEnd() {
        super.visitCode();
        if (callee.desc.contains(FRAME_TYPE.getDescriptor())) {
            // Wrapping a shadow; load all arguments and add a frame
            AsmUtil.loadThisAndArguments(mv, caller.access, caller.desc);
            HandleRegistry.accept(mv, Handle.FRAME_GET_INSTANCE);
        } else {
            // Wrapping a native method; skip the frame
            AsmUtil.loadThisAndArguments(mv, caller.access, callee.desc);
        }
        int opcode = getOpcode(callee, isInterface);
        // Add the method call
        super.visitMethodInsn(opcode, owner, callee.name, callee.desc, isInterface);
        super.visitInsn(Type.getReturnType(caller.desc).getOpcode(Opcodes.IRETURN));
        super.visitMaxs(-1, -1);
        super.visitEnd();
    }

    static int getOpcode(MethodNode mn, boolean isInterface) {
        if (mn.name.equals("<init>")) {
            return Opcodes.INVOKESPECIAL;
        } else if (AsmUtil.isSet(mn.access, Opcodes.ACC_STATIC)) {
            return Opcodes.INVOKESTATIC;
        } else {
            return isInterface ? Opcodes.INVOKEINTERFACE : Opcodes.INVOKEVIRTUAL;
        }
    }
}
