package edu.neu.ccs.prl.phosphor.internal.runtime.mask;

import edu.neu.ccs.prl.phosphor.internal.transform.PhosphorTransformer;

import java.nio.ByteBuffer;
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
        byte[] buffer = UnsafeMasker.copy(b, off, len);
        if (buffer != null) {
            // TODO isHostedAnonymous?
            byte[] instrumented = PhosphorTransformer.getInstanceAndTransform(buffer, true);
            return ClassLoaderAdapter.defineClass0(
                    loader, lookup, name, instrumented, 0, instrumented.length, domain, initialize, flags, classData);
        }
        return ClassLoaderAdapter.defineClass0(loader, lookup, name, b, off, len, domain, initialize, flags, classData);
    }

    @Mask(owner = "java/lang/ClassLoader", name = "defineClass1", isStatic = true)
    public static Class<?> defineClass1(
            ClassLoader loader, String name, byte[] b, int off, int len, ProtectionDomain pd, String source) {
        byte[] buffer = UnsafeMasker.copy(b, off, len);
        if (buffer != null) {
            // TODO isHostedAnonymous?
            byte[] instrumented = PhosphorTransformer.getInstanceAndTransform(buffer, false);
            return ClassLoaderAdapter.defineClass1(loader, name, instrumented, 0, instrumented.length, pd, source);
        }
        return ClassLoaderAdapter.defineClass1(loader, name, b, off, len, pd, source);
    }

    @Mask(owner = "java/lang/ClassLoader", name = "defineClass2", isStatic = true)
    public static Class<?> defineClass2(
            ClassLoader loader, String name, ByteBuffer b, int off, int len, ProtectionDomain pd, String source) {
        if (b != null && off >= 0 && len >= 0) {
            // TODO isHostedAnonymous?
            byte[] buffer = new byte[len];
            b.get(buffer, off, len);
            byte[] instrumented = PhosphorTransformer.getInstanceAndTransform(buffer, false);
            return ClassLoaderAdapter.defineClass2(
                    loader, name, ByteBuffer.wrap(instrumented), 0, instrumented.length, pd, source);
        }
        return ClassLoaderAdapter.defineClass2(loader, name, b, off, len, pd, source);
    }
}
