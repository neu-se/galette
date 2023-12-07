package edu.neu.ccs.prl.phosphor.internal.runtime.mask;

import edu.neu.ccs.prl.phosphor.internal.transform.PhosphorTransformer;
import java.security.ProtectionDomain;

@SuppressWarnings("unused")
public final class ClassLoaderMasker {
    @Mask(owner = "java/lang/ClassLoader", name = "defineClass0", isStatic = true)
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
        if (b != null && off >= 0 && len >= 0 && off + len <= b.length) {
            byte[] buffer = new byte[len];
            System.arraycopy(b, off, buffer, 0, len);
            // TODO isHostedAnonymous?
            byte[] instrumented = PhosphorTransformer.getInstanceAndTransform(buffer, true);
            return ClassLoaderAdapter.defineClass0(
                    loader, lookup, name, instrumented, 0, instrumented.length, domain, initialize, flags, classData);
        }
        return ClassLoaderAdapter.defineClass0(loader, lookup, name, b, off, len, domain, initialize, flags, classData);
    }
}
