package edu.neu.ccs.prl.phosphor.internal.agent;

import org.objectweb.asm.Opcodes;

public final class AccessUtil {
    private AccessUtil() {
        throw new AssertionError();
    }

    public static boolean isSet(int access, int flag) {
        return ((access & flag) != 0);
    }

    public static int makePublic(int access) {
        access &= ~Opcodes.ACC_PRIVATE;
        access &= ~Opcodes.ACC_PROTECTED;
        return access | Opcodes.ACC_PUBLIC;
    }
}
