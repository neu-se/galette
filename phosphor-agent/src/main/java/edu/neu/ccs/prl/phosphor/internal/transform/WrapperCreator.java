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
    private final int methodAccess;
    private final String methodName;
    private final String methodDescriptor;
    private final boolean isInterface;
    private final MethodVisitor delegate;
    /**
     * {@code true} if this class will be defined using {@link jdk.internal.misc.Unsafe#defineAnonymousClass}.
     */
    private final boolean isHostedAnonymous;

    WrapperCreator(String owner, boolean isInterface, MethodVisitor mv, MethodNode mn, boolean isHostedAnonymous) {
        this(owner, isInterface, mv, mn.access, mn.name, mn.desc, isHostedAnonymous);
    }

    WrapperCreator(
            String owner,
            boolean isInterface,
            MethodVisitor mv,
            int methodAccess,
            String methodName,
            String methodDescriptor,
            boolean isHostedAnonymous) {
        super(PhosphorTransformer.ASM_VERSION, mv);
        this.isInterface = isInterface;
        this.owner = owner;
        this.methodAccess = methodAccess;
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
        this.isHostedAnonymous = isHostedAnonymous;
        this.delegate = getDelegate();
    }

    @Override
    public void visitCode() {
        // Suppress the original method body by clearing the delegate
        mv = null;
    }

    @Override
    public void visitEnd() {
        // Restore the delegate
        mv = delegate;
        // Create a replacement method body
        super.visitCode();
        String calleeDesc;
        String calleeName;
        if (methodDescriptor.contains(FRAME_TYPE.getDescriptor())) {
            // Wrapping a native method; pop the frame
            AsmUtil.loadThisAndArguments(mv, methodAccess, methodDescriptor);
            super.visitInsn(Opcodes.POP);
            calleeName = ShadowMethodCreator.getOriginalMethodName(methodName);
            calleeDesc = ShadowMethodCreator.getOriginalMethodDescriptor(methodDescriptor);
        } else {
            // Wrapping a shadow; load all arguments and add a frame
            AsmUtil.loadThisAndArguments(mv, methodAccess, methodDescriptor);
            HandleRegistry.accept(mv, Handle.FRAME_GET_INSTANCE);
            calleeName = ShadowMethodCreator.getShadowMethodName(methodName);
            calleeDesc = ShadowMethodCreator.getShadowMethodDescriptor(methodDescriptor);
        }
        int opcode = computeOpcode();
        // Add the call to the wrapped method
        super.visitMethodInsn(opcode, owner, calleeName, calleeDesc, isInterface);
        super.visitInsn(Type.getReturnType(methodDescriptor).getOpcode(Opcodes.IRETURN));
        super.visitMaxs(-1, -1);
        super.visitEnd();
    }

    private int computeOpcode() {
        int opcode;
        if (AsmUtil.isSet(methodAccess, Opcodes.ACC_STATIC)) {
            opcode = Opcodes.INVOKESTATIC;
        } else if (isHostedAnonymous) {
            // Hosted anonymous classes should not be subclassed
            // so INVOKEVIRTUAL should be safe.
            // We cannot use INVOKESPECIAL because the "context" for checking the INVOKESPECIAL is the host class,
            // so the receiver of the INVOKESPECIAL must be an instance of the host class.
            opcode = Opcodes.INVOKEVIRTUAL;
        } else {
            // We do not want the call dynamically dispatched; use INVOKESPECIAL.
            opcode = Opcodes.INVOKESPECIAL;
        }
        return opcode;
    }
}
