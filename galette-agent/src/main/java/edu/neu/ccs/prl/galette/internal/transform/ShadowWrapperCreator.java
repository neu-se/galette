package edu.neu.ccs.prl.galette.internal.transform;

import static edu.neu.ccs.prl.galette.internal.transform.NativeWrapperCreator.computeWrappedCallOpcode;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

class ShadowWrapperCreator extends MethodVisitor {
    private final MethodVisitor delegate;
    private final MethodRecord callee;
    private final CallerSensitiveFixer fixer = new CallerSensitiveFixer();

    private ShadowWrapperCreator(MethodVisitor mv, MethodRecord callee) {
        super(GaletteTransformer.ASM_VERSION, mv);
        this.callee = callee;
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
        // Wrapping a shadow; load all arguments and the added frame (descriptor is for the shadow and the frame will be
        // added at the next available index by the initializer
        AsmUtil.loadReceiverAndArguments(mv, callee.getOpcode() == Opcodes.INVOKESTATIC, callee.getDescriptor(), 0);
        // If the original method called getCallerClass, compute the caller class now and push to the frame
        fixer.fix(mv);
        // Call to the wrapped method
        callee.accept(mv);
        super.visitInsn(Type.getReturnType(callee.getDescriptor()).getOpcode(Opcodes.IRETURN));
        super.visitMaxs(-1, -1);
        super.visitEnd();
    }

    static ShadowWrapperCreator newInstance(
            String owner, boolean isInterface, MethodVisitor mv, MethodNode mn, boolean isHostedAnonymous) {
        if (!AsmUtil.hasMethodBody(mn.access)) {
            throw new IllegalArgumentException("Methods that are native or abstract cannot be wrappers");
        } else if (ShadowMethodCreator.isShadowMethod(mn.desc)) {
            throw new IllegalArgumentException("Expected original method");
        }
        int opcode = computeWrappedCallOpcode(mn.access, mn.name, isHostedAnonymous);
        String calleeDesc = ShadowMethodCreator.getShadowMethodDescriptor(mn.desc);
        // Store the frame after the arguments
        int frameIndex = AsmUtil.countLocalVariables(mn.access, mn.desc);
        MethodRecord callee = new MethodRecord(opcode, owner, mn.name, calleeDesc, isInterface);
        FrameInitializer initializer =
                new IndirectFrameInitializer(mv, mn.name.equals("<init>"), frameIndex, 0, mn.desc, mn.access);
        return new ShadowWrapperCreator(initializer, callee);
    }

    /**
     * Fixed issues caused by wrapping a shadow for an original method that calls
     * {@code Reflection.getCallerClass()}.
     */
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
