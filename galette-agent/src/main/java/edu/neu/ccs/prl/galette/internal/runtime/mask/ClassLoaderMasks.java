package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.PrimitiveBoxer;
import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
import java.nio.ByteBuffer;
import java.security.ProtectionDomain;

public final class ClassLoaderMasks {
    @Mask(
            owner = "java/lang/ClassLoader",
            name = "defineClass0",
            returnDescriptor = "Ljava/lang/Class;",
            isStatic = true,
            type = MaskType.FIX_ARGUMENTS)
    public static Object[] defineClass0(
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
        byte[] buffer = UnsafeMasks.copy(b, off, len);
        if (buffer != null) {
            // isHostedAnonymous appears to only be an issue for Java 11
            // ClassLoader#defineClass0 is not defined for Java 11
            b = GaletteTransformer.getInstanceAndTransform(buffer, false);
            off = 0;
            len = b.length;
        }
        return new Object[] {
            loader,
            lookup,
            name,
            b,
            PrimitiveBoxer.box(off),
            PrimitiveBoxer.box(len),
            domain,
            PrimitiveBoxer.box(initialize),
            PrimitiveBoxer.box(flags),
            classData
        };
    }

    @Mask(
            owner = "java/lang/ClassLoader",
            name = "defineClass1",
            returnDescriptor = "Ljava/lang/Class;",
            isStatic = true,
            type = MaskType.FIX_ARGUMENTS)
    public static Object[] defineClass1(
            ClassLoader loader, String name, byte[] b, int off, int len, ProtectionDomain pd, String source) {
        byte[] buffer = UnsafeMasks.copy(b, off, len);
        if (buffer != null) {
            b = GaletteTransformer.getInstanceAndTransform(buffer, false);
            off = 0;
            len = b.length;
        }
        return new Object[] {loader, name, b, PrimitiveBoxer.box(off), PrimitiveBoxer.box(len), pd, source};
    }

    @Mask(
            owner = "java/lang/ClassLoader",
            name = "defineClass2",
            returnDescriptor = "Ljava/lang/Class;",
            isStatic = true,
            type = MaskType.FIX_ARGUMENTS)
    @Mask(owner = "java/lang/ClassLoader", name = "defineClass2", isStatic = true)
    public static Object[] defineClass2(
            ClassLoader loader, String name, ByteBuffer b, int off, int len, ProtectionDomain pd, String source) {
        if (b != null && off >= 0 && len >= 0 && len + off <= b.limit()) {
            byte[] classFileBuffer = new byte[len];
            for (int i = 0; i < len; i++) {
                classFileBuffer[i] = b.get(i + off);
            }
            byte[] instrumented = GaletteTransformer.getInstanceAndTransform(classFileBuffer, false);
            b = ByteBuffer.allocateDirect(instrumented.length);
            b.put(instrumented);
            b.rewind();
            off = 0;
            len = instrumented.length;
        }
        return new Object[] {loader, name, b, PrimitiveBoxer.box(off), PrimitiveBoxer.box(len), pd, source};
    }
}
