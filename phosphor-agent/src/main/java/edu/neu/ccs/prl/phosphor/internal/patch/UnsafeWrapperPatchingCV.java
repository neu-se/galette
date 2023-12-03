package edu.neu.ccs.prl.phosphor.internal.patch;

import edu.neu.ccs.prl.phosphor.internal.agent.AccessUtil;
import edu.neu.ccs.prl.phosphor.internal.agent.PhosphorAgent;
import edu.neu.ccs.prl.phosphor.internal.runtime.unsafe.JdkUnsafeWrapper;
import edu.neu.ccs.prl.phosphor.internal.runtime.unsafe.SunUnsafeWrapper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class UnsafeWrapperPatchingCV extends ClassVisitor {
    private static final String JDK_UNSAFE_INTERNAL_NAME = "jdk/internal/misc/Unsafe";
    private static final String SUN_UNSAFE_INTERNAL_NAME = "sun/misc/Unsafe";
    private static final String SUN_UNSAFE_WRAPPER_INTERNAL_NAME = Type.getInternalName(SunUnsafeWrapper.class);
    private static final String JDK_UNSAFE_WRAPPER_INTERNAL_NAME = Type.getInternalName(JdkUnsafeWrapper.class);
    private Type unsafeType;

    public UnsafeWrapperPatchingCV(ClassVisitor cv) {
        super(PhosphorAgent.ASM_VERSION, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        unsafeType = name.equals(SUN_UNSAFE_WRAPPER_INTERNAL_NAME)
                ? Type.getObjectType(SUN_UNSAFE_INTERNAL_NAME)
                : Type.getObjectType(JDK_UNSAFE_INTERNAL_NAME);
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor target = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (AccessUtil.isSet(access, Opcodes.ACC_PUBLIC) && !name.equals("<init>")) {
            // Set the delegate method visitor to null to replace the body of the method
            return new MethodVisitor(PhosphorAgent.ASM_VERSION, null) {
                @Override
                public void visitCode() {
                    target.visitCode();
                    // Get the Unsafe instance
                    target.visitMethodInsn(
                            Opcodes.INVOKESTATIC,
                            unsafeType.getInternalName(),
                            "getUnsafe",
                            "()" + unsafeType.getDescriptor(),
                            false);
                    // Load the method arguments from the parameters
                    loadArguments(target, access, descriptor);
                    // Call the corresponding Unsafe method
                    target.visitMethodInsn(
                            Opcodes.INVOKEVIRTUAL, unsafeType.getInternalName(), name, descriptor, false);
                    target.visitInsn(Type.getReturnType(descriptor).getOpcode(Opcodes.IRETURN));
                    target.visitMaxs(-1, -1);
                    target.visitEnd();
                }
            };
        }
        return target;
    }

    private static void loadArguments(MethodVisitor target, int access, String descriptor) {
        // Skip "this" for virtual methods
        int varIndex = AccessUtil.isSet(access, Opcodes.ACC_STATIC) ? 0 : 1;
        for (Type argument : Type.getArgumentTypes(descriptor)) {
            target.visitVarInsn(argument.getOpcode(Opcodes.ILOAD), varIndex);
            varIndex += argument.getSize();
        }
    }

    public static boolean isApplicable(String className) {
        return SUN_UNSAFE_WRAPPER_INTERNAL_NAME.equals(className) || JDK_UNSAFE_WRAPPER_INTERNAL_NAME.equals(className);
    }
}
