package edu.neu.ccs.prl.galette.internal.transform;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

/**
 * Creates a shadow method that wraps a call to an original method.
 */
class NativeWrapperCreator extends MethodVisitor {
    private final MethodVisitor delegate;
    private final MethodRecord callee;

    private NativeWrapperCreator(MethodVisitor mv, MethodRecord callee) {
        super(GaletteTransformer.ASM_VERSION, mv);
        this.delegate = getDelegate();
        this.callee = callee;
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
        // Load the original arguments
        AsmUtil.loadThisAndArguments(mv, callee.getOpcode() == Opcodes.INVOKESTATIC, callee.getDescriptor());
        // Call the wrapped method
        callee.accept(mv);
        super.visitInsn(Type.getReturnType(callee.getDescriptor()).getOpcode(Opcodes.IRETURN));
        super.visitMaxs(-1, -1);
        super.visitEnd();
    }

    /**
     * Returns the opcode that should be used to invoke a wrapped method from the wrapper.
     * @param access the access modifier of the wrapped method
     * @param name the name of the wrapped method
     * @param isHostedAnonymous {@code true} if the method being visited is contained in a class that will be defined
     * using {@link jdk.internal.misc.Unsafe#defineAnonymousClass}
     * @return the opcode that should be used to invoke a wrapped method from the wrapper
     */
    static int computeWrappedCallOpcode(int access, String name, boolean isHostedAnonymous) {
        int opcode;
        if (AsmUtil.isSet(access, Opcodes.ACC_STATIC)) {
            opcode = Opcodes.INVOKESTATIC;
        } else if (isHostedAnonymous) {
            // Hosted anonymous classes should not be subclassed, so INVOKEVIRTUAL should be safe.
            // Based on src/hotspot/share/interpreter/linkResolver.cpp from Eclipse Temurin JDK (version 11.0.21+9):
            // We cannot use INVOKESPECIAL for non-instance initialization methods because the "context" for checking
            // the INVOKESPECIAL is the host class, so the receiver of the INVOKESPECIAL must be an instance of the
            // host class or subclass of the host class.
            // An instance of the anonymous class will be the receiver, and it may not extend the host class.
            opcode = name.equals("<init>") ? Opcodes.INVOKESPECIAL : Opcodes.INVOKEVIRTUAL;
        } else {
            // We do not want the call dynamically dispatched; use INVOKESPECIAL.
            opcode = Opcodes.INVOKESPECIAL;
        }
        return opcode;
    }

    static NativeWrapperCreator newInstance(
            String owner, boolean isInterface, MethodVisitor mv, MethodNode mn, boolean isHostedAnonymous) {
        if (!AsmUtil.hasMethodBody(mn.access)) {
            throw new IllegalArgumentException("Methods that are native or abstract cannot be wrappers");
        } else if (!ShadowMethodCreator.isShadowMethod(mn.desc)) {
            throw new IllegalArgumentException("Expected shadow method");
        }
        int opcode = computeWrappedCallOpcode(mn.access, mn.name, isHostedAnonymous);
        String calleeDesc = ShadowMethodCreator.getOriginalMethodDescriptor(mn.desc);
        MethodRecord callee = new MethodRecord(opcode, owner, mn.name, calleeDesc, isInterface);
        return new NativeWrapperCreator(mv, callee);
    }
}
