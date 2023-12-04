package edu.neu.ccs.prl.phosphor.internal.patch;

import edu.neu.ccs.prl.phosphor.internal.runtime.unsafe.UnsafeWrapper;
import edu.neu.ccs.prl.phosphor.internal.transform.AsmUtil;
import edu.neu.ccs.prl.phosphor.internal.transform.PhosphorTransformer;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class UnsafeWrapperPatcher extends ClassVisitor {
    private static final String JDK_UNSAFE_INTERNAL_NAME = "jdk/internal/misc/Unsafe";
    private static final String SUN_UNSAFE_INTERNAL_NAME = "sun/misc/Unsafe";
    private static final String UNSAFE_WRAPPER_INTERNAL_NAME = Type.getInternalName(UnsafeWrapper.class);
    private static final String INVALID_FIELD_OFFSET_METHOD_NAME = "getInvalidFieldOffset";
    private final Type unsafeType;

    private UnsafeWrapperPatcher(ClassVisitor cv, String unsafeInternalName) {
        super(PhosphorTransformer.ASM_VERSION, cv);
        unsafeType = Type.getObjectType(unsafeInternalName);
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor target = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (AsmUtil.isSet(access, Opcodes.ACC_PUBLIC) && !name.equals("<init>")) {
            // Set the delegate method visitor to null to replace the body of the method
            return new MethodVisitor(PhosphorTransformer.ASM_VERSION, null) {
                @Override
                public void visitCode() {
                    replaceBody(access, name, descriptor, target);
                }
            };
        }
        return target;
    }

    private void replaceBody(int access, String name, String descriptor, MethodVisitor target) {
        target.visitCode();
        if (name.equals(INVALID_FIELD_OFFSET_METHOD_NAME)) {
            target.visitFieldInsn(Opcodes.GETSTATIC, unsafeType.getInternalName(), "INVALID_FIELD_OFFSET", "I");
        } else {
            // Get the Unsafe instance
            target.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    unsafeType.getInternalName(),
                    "getUnsafe",
                    "()" + unsafeType.getDescriptor(),
                    false);
            // Load the method arguments from the parameters
            AsmUtil.loadArguments(target, access, descriptor);
            // Call the corresponding Unsafe method
            target.visitMethodInsn(Opcodes.INVOKEVIRTUAL, unsafeType.getInternalName(), name, descriptor, false);
        }
        target.visitInsn(Type.getReturnType(descriptor).getOpcode(Opcodes.IRETURN));
        target.visitMaxs(-1, -1);
        target.visitEnd();
    }

    public static boolean isApplicable(String className) {
        return UNSAFE_WRAPPER_INTERNAL_NAME.equals(className);
    }

    public static UnsafeWrapperPatcher createForStandard(ClassVisitor cv) {
        return new UnsafeWrapperPatcher(cv, SUN_UNSAFE_INTERNAL_NAME);
    }

    public static UnsafeWrapperPatcher createForEmbedded(ClassVisitor cv) {
        return new UnsafeWrapperPatcher(cv, JDK_UNSAFE_INTERNAL_NAME);
    }
}
