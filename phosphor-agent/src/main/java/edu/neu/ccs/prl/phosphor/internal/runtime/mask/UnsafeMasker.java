package edu.neu.ccs.prl.phosphor.internal.runtime.mask;

import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import edu.neu.ccs.prl.phosphor.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import edu.neu.ccs.prl.phosphor.internal.transform.PhosphorTransformer;
import java.security.ProtectionDomain;

public final class UnsafeMasker {
    @InvokedViaHandle(handle = Handle.UNSAFE_MASKER_DEFINE_ANONYMOUS)
    public static Class<?> defineAnonymousClass(
            Object unsafe, Class<?> hostClass, byte[] data, Object[] cpPatches, PhosphorFrame frame) {
        byte[] instrumented = PhosphorTransformer.getInstanceAndTransform(data);
        return UnsafeAdapter.defineAnonymousClass(hostClass, instrumented, cpPatches);
    }

    @InvokedViaHandle(handle = Handle.UNSAFE_MASKER_DEFINE_CLASS)
    public static Class<?> defineClass(
            Object unsafe,
            String name,
            byte[] b,
            int off,
            int len,
            ClassLoader loader,
            ProtectionDomain protectionDomain,
            PhosphorFrame frame) {
        if (b != null && off >= 0 && len >= 0 && off + len <= b.length) {
            byte[] buffer = new byte[len];
            System.arraycopy(b, off, buffer, 0, len);
            byte[] instrumented = PhosphorTransformer.getInstanceAndTransform(buffer);
            return UnsafeAdapter.defineClass(name, instrumented, 0, instrumented.length, loader, protectionDomain);
        }
        return UnsafeAdapter.defineClass(name, b, off, len, loader, protectionDomain);
    }
}
