package edu.neu.ccs.prl.galette.internal.transform;

import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.F_NEW;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import org.objectweb.asm.Label;
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
        if (ShadowMethodCreator.isShadowMethod(methodDescriptor)) {
            generateNativeWrapper();
        } else if (methodName.equals("<init>")) {
            generateShadowWrapper();
        } else {
            generatePoppingShadowWrapper();
        }
        super.visitMaxs(-1, -1);
        super.visitEnd();
    }

    private void generateNativeWrapper() {
        // Wrapping a native method; pop the frame
        AsmUtil.loadThisAndArguments(mv, methodAccess, methodDescriptor);
        super.visitInsn(Opcodes.POP);
        String calleeDesc = ShadowMethodCreator.getOriginalMethodDescriptor(methodDescriptor);
        // Add the call to the wrapped method
        super.visitMethodInsn(computeOpcode(), owner, methodName, calleeDesc, isInterface);
        super.visitInsn(Type.getReturnType(methodDescriptor).getOpcode(Opcodes.IRETURN));
    }

    private void generateShadowWrapper() {
        // Wrapping a shadow; load all arguments
        AsmUtil.loadThisAndArguments(mv, methodAccess, methodDescriptor);
        // Fetch the frame
        super.visitLdcInsn(methodDescriptor);
        Handle.FRAME_STACK_PEEK.accept(mv);
        // If the original method called getCallerClass, compute the caller class now and push to the frame
        fixer.fix(mv);
        String calleeDesc = ShadowMethodCreator.getShadowMethodDescriptor(methodDescriptor);
        // Add the call to the wrapped method
        super.visitMethodInsn(computeOpcode(), owner, methodName, calleeDesc, isInterface);
        super.visitInsn(Type.getReturnType(methodDescriptor).getOpcode(Opcodes.IRETURN));
    }

    private void generatePoppingShadowWrapper() {
        Label frameEnd = new Label();
        Label scopeStart = new Label();
        Label scopeEnd = new Label();
        Label handler = new Label();
        super.visitTryCatchBlock(scopeStart, scopeEnd, handler, null);
        // Wrapping a shadow; load all arguments
        AsmUtil.loadThisAndArguments(mv, methodAccess, methodDescriptor);
        // Fetch the frame
        super.visitLdcInsn(methodDescriptor);
        Handle.FRAME_STACK_PEEK.accept(mv);
        // Store the frame to a local variable
        int frameVar = storeFrame(frameEnd);
        // Pop the frame from the frame stack to prevent the callee from accessing it
        Handle.FRAME_STACK_POP.accept(mv);
        // Start the scope of the exception handler
        super.visitLabel(scopeStart);
        // If the original method called getCallerClass, compute the caller class now and push to the frame
        fixer.fix(mv);
        String calleeDesc = ShadowMethodCreator.getShadowMethodDescriptor(methodDescriptor);
        // Add the call to the wrapped method
        super.visitMethodInsn(computeOpcode(), owner, methodName, calleeDesc, isInterface);
        // End the scope of the exception handler
        super.visitLabel(scopeEnd);
        // Restore the frame stack
        super.visitVarInsn(Opcodes.ALOAD, frameVar);
        Handle.FRAME_STACK_PUSH.accept(mv);
        // Return
        super.visitInsn(Type.getReturnType(methodDescriptor).getOpcode(Opcodes.IRETURN));
        // Visit the exception handler
        super.visitLabel(handler);
        Object[] locals = AsmUtil.createTopArray(frameVar + 1);
        locals[frameVar] = ShadowLocals.FRAME_INTERNAL_NAME;
        super.visitFrame(F_NEW, locals.length, locals, 1, new Object[] {"java/lang/Throwable"});
        // Restore the frame stack
        super.visitVarInsn(Opcodes.ALOAD, frameVar);
        Handle.FRAME_STACK_PUSH.accept(mv);
        // Rethrow the exception
        super.visitInsn(ATHROW);
        super.visitLabel(frameEnd);
    }

    private int storeFrame(Label frameEnd) {
        // frame
        Label frameStart = new Label();
        int frameVar = AsmUtil.countLocalVariables(methodAccess, methodDescriptor);
        mv.visitLocalVariable(
                ShadowLocals.getShadowVariableName("frame"),
                ShadowLocals.FRAME_DESCRIPTOR,
                null,
                frameStart,
                frameEnd,
                frameVar);
        super.visitLabel(frameStart);
        super.visitInsn(Opcodes.DUP);
        super.visitVarInsn(Opcodes.ASTORE, frameVar);
        return frameVar;
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
