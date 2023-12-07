package edu.neu.ccs.prl.phosphor.internal.runtime.mask;

import edu.neu.ccs.prl.phosphor.internal.runtime.Patched;
import java.nio.ByteBuffer;
import java.security.ProtectionDomain;

@SuppressWarnings("unused")
public class ClassLoaderAdapter {
    @Patched
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
        return null;
    }

    @Patched
    public static Class<?> defineClass1(
            ClassLoader loader, String name, byte[] b, int off, int len, ProtectionDomain pd, String source) {
        return null;
    }

    @Patched
    public static Class<?> defineClass2(
            ClassLoader loader, String name, ByteBuffer b, int off, int len, ProtectionDomain pd, String source) {
        return null;
    }
}
