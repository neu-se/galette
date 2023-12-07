package edu.neu.ccs.prl.phosphor.internal.patch;

import edu.neu.ccs.prl.phosphor.internal.runtime.mask.UnsafeAdapter;
import edu.neu.ccs.prl.phosphor.internal.transform.AsmUtil;
import java.util.function.Function;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

final class UnsafeAdapterPatcher extends AdapterPatcher {
    private final Function<String, byte[]> entryLocator;

    UnsafeAdapterPatcher(ClassVisitor cv, Function<String, byte[]> entryLocator) {
        super(cv);
        this.entryLocator = entryLocator;
    }

    protected void replaceBody(int access, String name, String descriptor, MethodVisitor mv) {
        // TODO fix getObject vs getReference for Java 11 vs 17
        // Use the JDK version for embedded Phosphor
        Type unsafeType = Type.getObjectType(entryLocator == null ? "sun/misc/Unsafe" : "jdk/internal/misc/Unsafe");
        mv.visitCode();
        // Special case for the field access
        if (name.equals("getInvalidFieldOffset")) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, unsafeType.getInternalName(), "INVALID_FIELD_OFFSET", "I");
        } else {
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
            if (entryLocator != null && name.startsWith("define")) {
                // For Java 9+ the native method has a suffix appended
                name += "0";
            }
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, unsafeType.getInternalName(), name, descriptor, false);
        }
        mv.visitInsn(Type.getReturnType(descriptor).getOpcode(Opcodes.IRETURN));
        mv.visitMaxs(-1, -1);
        mv.visitEnd();
    }

    public static boolean isApplicable(String className) {
        return Type.getInternalName(UnsafeAdapter.class).equals(className);
    }
}
