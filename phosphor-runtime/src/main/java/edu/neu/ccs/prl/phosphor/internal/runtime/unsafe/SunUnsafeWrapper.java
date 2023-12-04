package edu.neu.ccs.prl.phosphor.internal.runtime.unsafe;

import edu.neu.ccs.prl.phosphor.internal.runtime.Patched;
import java.security.ProtectionDomain;

public class SunUnsafeWrapper implements UnsafeWrapper {
    @Patched
    @Override
    public Class<?> defineClass(
            String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) {
        return null;
    }
}
