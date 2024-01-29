package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Patched;
import java.security.ProtectionDomain;

@SuppressWarnings("unused")
public final class UnsafeAdapter {
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
