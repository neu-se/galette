package edu.neu.ccs.prl.phosphor.internal.runtime.unsafe;

import java.security.ProtectionDomain;

public interface UnsafeWrapper {
    UnsafeWrapper INSTANCE = getInstance();

    Class<?> defineClass(
            String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain);

    static UnsafeWrapper getInstance() {
        try {
            Class.forName("jdk.internal.misc.Unsafe");
        } catch (Throwable t) {
            return new SunUnsafeWrapper();
        }
        return new JdkUnsafeWrapper();
    }
}
