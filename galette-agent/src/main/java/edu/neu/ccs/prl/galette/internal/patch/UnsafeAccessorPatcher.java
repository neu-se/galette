package edu.neu.ccs.prl.galette.internal.patch;

import edu.neu.ccs.prl.galette.internal.runtime.mask.UnsafeAccessor;
import edu.neu.ccs.prl.galette.internal.transform.AsmUtil;
import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Function;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

final class UnsafeAccessorPatcher extends ClassVisitor {
    private static final String JDK_UNSAFE_INTERNAL_NAME = "jdk/internal/misc/Unsafe";
    private static final String SUN_UNSAFE_INTERNAL_NAME = "sun/misc/Unsafe";
    private final Type unsafeType;
    private final boolean useReferenceName;
    private final boolean isJava8;

    UnsafeAccessorPatcher(ClassVisitor cv, Function<String, byte[]> entryLocator) {
        super(GaletteTransformer.ASM_VERSION, cv);
        this.isJava8 = entryLocator == null;
        this.unsafeType =
                Type.getObjectType(entryLocator == null ? SUN_UNSAFE_INTERNAL_NAME : JDK_UNSAFE_INTERNAL_NAME);
        this.useReferenceName = shouldUseReferenceName(entryLocator);
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor target = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (AsmUtil.isSet(access, Opcodes.ACC_PUBLIC) && !name.equals("<init>") && !isUndefined(name)) {
            // Set the delegate method visitor to null to replace the body of the method
            return new MethodVisitor(GaletteTransformer.ASM_VERSION, null) {
                @Override
                public void visitCode() {
                    replaceBody(access, name, descriptor, target);
                }
            };
        }
        return target;
    }

    private void replaceBody(int access, String name, String descriptor, MethodVisitor mv) {
        mv.visitCode();
        if (name.equals("getInvalidFieldOffset")) {
            // Special case for the field access
            mv.visitFieldInsn(Opcodes.GETSTATIC, unsafeType.getInternalName(), "INVALID_FIELD_OFFSET", "I");
        } else {
            if (useReferenceName && name.contains("Object")) {
                name = name.replace("Object", "Reference");
            } else if (!isJava8 && name.contains("compareAndSwap")) {
                name = name.replace("compareAndSwap", "compareAndSet");
            }
            // Get the Unsafe instance
            mv.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    unsafeType.getInternalName(),
                    "getUnsafe",
                    "()" + unsafeType.getDescriptor(),
                    false);
            // Load the method arguments from the parameters
            AsmUtil.loadArguments(mv, access, descriptor);
            // Call the corresponding Unsafe method
            if (!isJava8 && name.startsWith("define")) {
                // For Java 9+ the native method has a suffix appended
                name += "0";
            }
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, unsafeType.getInternalName(), name, descriptor, false);
        }
        mv.visitInsn(Type.getReturnType(descriptor).getOpcode(Opcodes.IRETURN));
        mv.visitMaxs(-1, -1);
        mv.visitEnd();
    }

    private boolean isUndefined(String methodName) {
        // putOrderX methods are not defined in jdk.internal.misc.Unsafe
        // compareAndExchangeX methods are not defined in sun.misc.Unsafe
        return (methodName.startsWith("putOrdered") && !isJava8)
                || (methodName.startsWith("compareAndExchange") && isJava8);
    }

    private static boolean shouldUseReferenceName(Function<String, byte[]> entryLocator) {
        if (entryLocator != null) {
            byte[] classFileBuffer = entryLocator.apply(JDK_UNSAFE_INTERNAL_NAME);
            ByteArrayInputStream in = new ByteArrayInputStream(classFileBuffer);
            ClassNode cn = new ClassNode();
            try {
                new ClassReader(in).accept(cn, ClassReader.SKIP_CODE);
            } catch (IOException e) {
                throw new AssertionError(in.getClass() + " threw " + e.getClass());
            }
            for (MethodNode mn : cn.methods) {
                if (mn.name.contains("putReference")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isApplicable(String className) {
        return Type.getInternalName(UnsafeAccessor.class).equals(className);
    }
}
