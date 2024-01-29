package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

class WrapperCreator extends MethodVisitor {
    private final String owner;
    private final int methodAccess;
    private final String methodName;
    private final String methodDescriptor;
    private final boolean isInterface;
    private final MethodVisitor delegate;
    /**
     * {@code true} if this class will be defined using {@code jdk.internal.misc.Unsafe#defineAnonymousClass}.
     */
    private final boolean isHostedAnonymous;
    /**
     * Instance used to "fix" issues caused by wrapping a shadow for an original method that calls
     * {@code Reflection.getCallerClass()}.
     */
    private final CallerSensitiveFixer fixer = new CallerSensitiveFixer();

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
        super(GaletteTransformer.ASM_VERSION, mv);
        if (!AsmUtil.hasMethodBody(methodAccess)) {
            throw new IllegalArgumentException("Methods that are native or abstract cannot be wrappers");
        }
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
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        fixer.visitMethodInsn(owner, name, descriptor);
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitEnd() {
        // Restore the delegate
        mv = delegate;
        // Create a replacement method body
        super.visitCode();
        String calleeDesc;
        if (ShadowMethodCreator.isShadowMethod(methodDescriptor)) {
            // Wrapping a native method; pop the frame
            AsmUtil.loadThisAndArguments(mv, methodAccess, methodDescriptor);
            super.visitInsn(Opcodes.POP);
            calleeDesc = ShadowMethodCreator.getOriginalMethodDescriptor(methodDescriptor);
        } else {
            // Wrapping a shadow; load all arguments and add a frame
            AsmUtil.loadThisAndArguments(mv, methodAccess, methodDescriptor);
            Handle.FRAME_GET_INSTANCE.accept(mv);
            // If the original method called getCallerClass, compute the caller class now and push to the frame
            fixer.fix(mv);
            calleeDesc = ShadowMethodCreator.getShadowMethodDescriptor(methodDescriptor);
        }
        int opcode = computeOpcode();
        // Add the call to the wrapped method
        super.visitMethodInsn(opcode, owner, methodName, calleeDesc, isInterface);
        super.visitInsn(Type.getReturnType(methodDescriptor).getOpcode(Opcodes.IRETURN));
        super.visitMaxs(-1, -1);
        super.visitEnd();
    }

    private int computeOpcode() {
        int opcode;
        if (AsmUtil.isSet(methodAccess, Opcodes.ACC_STATIC)) {
            opcode = Opcodes.INVOKESTATIC;
        } else if (isHostedAnonymous) {
            // Hosted anonymous classes should not be subclassed, so INVOKEVIRTUAL should be safe.
            // Based on src/hotspot/share/interpreter/linkResolver.cpp from Eclipse Temurin JDK (version 11.0.21+9):
            // We cannot use INVOKESPECIAL for non-instance initialization methods because the "context" for checking
            // the INVOKESPECIAL is the host class, so the receiver of the INVOKESPECIAL must be an instance of the
            // host class or subclass of the host class.
            // An instance of the anonymous class will be the receiver, and it may not extend the host class.
            opcode = methodName.equals("<init>") ? Opcodes.INVOKESPECIAL : Opcodes.INVOKEVIRTUAL;
        } else {
            // We do not want the call dynamically dispatched; use INVOKESPECIAL.
            opcode = Opcodes.INVOKESPECIAL;
        }
        return opcode;
    }

    private static final class CallerSensitiveFixer {
        private static final String JDK_REFLECTION_INTERNAL_NAME = "jdk/internal/reflect/Reflection";
        private static final String SUN_REFLECTION_INTERNAL_NAME = "sun/reflect/Reflection";

        private static final String TARGET_METHOD_NAME = "getCallerClass";
        private static final String TARGET_METHOD_DESCRIPTOR = "()Ljava/lang/Class;";
        private boolean foundJdkCall = false;
        private boolean foundSunCall = false;

        void visitMethodInsn(String owner, String name, String descriptor) {
            if (name.equals(TARGET_METHOD_NAME) && TARGET_METHOD_DESCRIPTOR.equals(descriptor)) {
                if (JDK_REFLECTION_INTERNAL_NAME.equals(owner)) {
                    foundJdkCall = true;
                } else if (SUN_REFLECTION_INTERNAL_NAME.equals(owner)) {
                    foundSunCall = true;
                }
            }
        }

        void fix(MethodVisitor mv) {
            // Top of stack should be the frame
            if (foundJdkCall) {
                setCallerClass(mv, JDK_REFLECTION_INTERNAL_NAME);
            } else if (foundSunCall) {
                setCallerClass(mv, SUN_REFLECTION_INTERNAL_NAME);
            }
        }

        private static void setCallerClass(MethodVisitor mv, String reflectionInternalName) {
            mv.visitMethodInsn(
                    Opcodes.INVOKESTATIC, reflectionInternalName, TARGET_METHOD_NAME, TARGET_METHOD_DESCRIPTOR, false);
            Handle.FRAME_SET_CALLER.accept(mv);
        }
    }
}
