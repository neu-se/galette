package edu.neu.ccs.prl.galette.internal.runtime.mask;

import java.nio.ByteBuffer;
import java.security.ProtectionDomain;
import org.objectweb.asm.Opcodes;

@SuppressWarnings("unused")
public final class ClassLoaderAccessor {
    @MemberAccess(owner = "java/lang/ClassLoader", name = "defineClass0", opcode = Opcodes.INVOKESTATIC)
    public static Class<?> defineClass0(
            ClassLoader loader,
            Class<?> lookup,
            String name,
            byte[] b,
            int off,
            int len,
            ProtectionDomain domain,
            boolean initialize,
            int flags,
            Object classData) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/ClassLoader", name = "defineClass1", opcode = Opcodes.INVOKESTATIC)
    public static Class<?> defineClass1(
            ClassLoader loader, String name, byte[] b, int off, int len, ProtectionDomain pd, String source) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/ClassLoader", name = "defineClass2", opcode = Opcodes.INVOKESTATIC)
    public static Class<?> defineClass2(
            ClassLoader loader, String name, ByteBuffer b, int off, int len, ProtectionDomain pd, String source) {
        throw new AssertionError("Placeholder method was called");
    }
}
