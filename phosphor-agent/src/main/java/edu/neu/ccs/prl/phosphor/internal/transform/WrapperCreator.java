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
    private final MethodNode mn;
    private final String calleeName;

    WrapperCreator(String owner, boolean isInterface, MethodNode mn, String calleeName) {
        super(PhosphorTransformer.ASM_VERSION, mn);
        this.isInterface = isInterface;
        this.owner = owner;
        this.mn = mn;
        this.calleeName = calleeName;
    }

    @Override
    public void visitCode() {
        // Suppress the original method body by clearing the delegate
        mv = null;
    }

    @Override
    public void visitEnd() {
        // Restore the delegate
        mv = mn;
        super.visitCode();
        String calleeDesc;
        if (mn.desc.contains(FRAME_TYPE.getDescriptor())) {
            // Wrapping a native method; pop the frame
            AsmUtil.loadThisAndArguments(mv, mn.access, mn.desc);
            super.visitInsn(Opcodes.POP);
            calleeDesc = ShadowMethodCreator.getOriginalMethodDescriptor(mn.desc);
        } else {
            // Wrapping a shadow; load all arguments and add a frame
            AsmUtil.loadThisAndArguments(mv, mn.access, mn.desc);
            HandleRegistry.accept(mv, Handle.FRAME_GET_INSTANCE);
            calleeDesc = ShadowMethodCreator.getShadowMethodDescriptor(mn.desc);
        }
        // We do not want the call dynamically dispatched so use INVOKESPECIAL
        int opcode = AsmUtil.isSet(mn.access, Opcodes.ACC_STATIC) ? Opcodes.INVOKESTATIC : Opcodes.INVOKESPECIAL;
        // Add the method call
        super.visitMethodInsn(opcode, owner, calleeName, calleeDesc, isInterface);
        super.visitInsn(Type.getReturnType(mn.desc).getOpcode(Opcodes.IRETURN));
        super.visitMaxs(-1, -1);
        super.visitEnd();
    }
}
