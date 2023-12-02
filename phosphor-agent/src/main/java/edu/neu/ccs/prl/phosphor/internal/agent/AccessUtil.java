package edu.neu.ccs.prl.phosphor.internal.agent;

public final class AccessUtil {
    private AccessUtil() {
        throw new AssertionError();
    }

    public static boolean isSet(int access, int flag) {
        return ((access & flag) != 0);
    }

    public static int set(int access, int flag) {
        return access | flag;
    }
}
