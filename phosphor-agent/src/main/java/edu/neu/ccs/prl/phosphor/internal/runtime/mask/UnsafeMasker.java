package edu.neu.ccs.prl.phosphor.internal.runtime.mask;

import edu.neu.ccs.prl.phosphor.internal.transform.PhosphorTransformer;
import java.security.ProtectionDomain;

@SuppressWarnings("unused")
public final class UnsafeMasker {
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "defineAnonymousClass0")
    @Mask(owner = "sun/misc/Unsafe", name = "defineAnonymousClass")
    public static Class<?> defineAnonymousClass(Object unsafe, Class<?> hostClass, byte[] data, Object[] cpPatches) {
        byte[] instrumented = PhosphorTransformer.getInstanceAndTransform(data, true);
        return UnsafeAdapter.defineAnonymousClass(hostClass, instrumented, cpPatches);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "defineClass0")
    @Mask(owner = "sun/misc/Unsafe", name = "defineClass")
    public static Class<?> defineClass(
            Object unsafe, String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain domain) {
        if (b != null && off >= 0 && len >= 0 && off + len <= b.length) {
            byte[] buffer = new byte[len];
            System.arraycopy(b, off, buffer, 0, len);
            byte[] instrumented = PhosphorTransformer.getInstanceAndTransform(buffer, false);
            return UnsafeAdapter.defineClass(name, instrumented, 0, instrumented.length, loader, domain);
        }
        return UnsafeAdapter.defineClass(name, b, off, len, loader, domain);
    }
}
