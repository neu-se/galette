package edu.neu.ccs.prl.phosphor.internal.runtime.unsafe;

import java.security.ProtectionDomain;

public interface UnsafeWrapper {
    UnsafeWrapper INSTANCE = getInstance();

    Class<?> defineClass(
            String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain);

    @SuppressWarnings("unused")
    static void defineAndLoadShadow(Class<?> originalClass, String name, byte[] buffer) {
        INSTANCE.defineClass(name, buffer, 0, buffer.length, originalClass.getClassLoader(), null);
    }

    static UnsafeWrapper getInstance() {
        try {
            Class.forName("jdk.internal.misc.Unsafe");
        } catch (Throwable t) {
            return new SunUnsafeWrapper();
        }
        return new JdkUnsafeWrapper();
    }
}
