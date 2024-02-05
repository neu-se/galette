package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Patched;
import java.security.ProtectionDomain;

@SuppressWarnings("unused")
public final class UnsafeAccessor {
    @Patched
    public static int getInvalidFieldOffset() {
        throw new AssertionError("Placeholder method was called");
    }

    @Patched
    public static Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches) {
        throw new AssertionError("Placeholder method was called");
    }

    @Patched
    public static Class<?> defineClass(
            String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) {
        throw new AssertionError("Placeholder method was called");
    }
}
