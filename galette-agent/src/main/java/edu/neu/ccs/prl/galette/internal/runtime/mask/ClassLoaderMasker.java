package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
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
            // isHostedAnonymous appears to only be an issue for Java 11
            // ClassLoader#defineClass0 is not defined for Java 11
            byte[] instrumented = GaletteTransformer.getInstanceAndTransform(buffer, false);
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
            byte[] instrumented = GaletteTransformer.getInstanceAndTransform(buffer, false);
            return ClassLoaderAdapter.defineClass1(loader, name, instrumented, 0, instrumented.length, pd, source);
        }
        return ClassLoaderAdapter.defineClass1(loader, name, b, off, len, pd, source);
    }

    @Mask(owner = "java/lang/ClassLoader", name = "defineClass2", isStatic = true)
    public static Class<?> defineClass2(
            ClassLoader loader, String name, ByteBuffer b, int off, int len, ProtectionDomain pd, String source) {
        if (b != null && off >= 0 && len >= 0 && len + off <= b.limit()) {
            byte[] classFileBuffer = new byte[len];
            for (int i = 0; i < len; i++) {
                classFileBuffer[i] = b.get(i + off);
            }
            byte[] instrumented = GaletteTransformer.getInstanceAndTransform(classFileBuffer, false);
            ByteBuffer buffer = ByteBuffer.allocateDirect(instrumented.length);
            buffer.put(instrumented);
            buffer.rewind();
            return ClassLoaderAdapter.defineClass2(loader, name, buffer, 0, instrumented.length, pd, source);
        }
        return ClassLoaderAdapter.defineClass2(loader, name, b, off, len, pd, source);
    }
}
