package edu.neu.ccs.prl.phosphor.internal.runtime.unsafe;

import edu.neu.ccs.prl.phosphor.internal.runtime.Patched;
import java.security.ProtectionDomain;

public final class UnsafeWrapper {
    @Patched
    public static long getInvalidFieldOffset() {
        return -1;
    }

    @Patched
    public static Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches) {
        return null;
    }

    @Patched
    public static Class<?> defineClass(
            String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) {
        return null;
    }
}
