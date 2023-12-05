package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import edu.neu.ccs.prl.phosphor.internal.runtime.collection.ObjectIntMap;
import edu.neu.ccs.prl.phosphor.internal.runtime.mask.UnsafeMasker;

public final class MaskRegistry {
    private static final String JDK_UNSAFE_INTERNAL_NAME = "jdk/internal/misc/Unsafe";
    private static final String SUN_UNSAFE_INTERNAL_NAME = "sun/misc/Unsafe";
    private static final ObjectIntMap<String> maskMap = new ObjectIntMap<>();

    private MaskRegistry() {
        throw new AssertionError();
    }

    static {
        initialize();
    }

    public static MethodRecord getMask(String className, String methodName, String descriptor) {
        String key = getKey(className, methodName, descriptor);
        int index = maskMap.getOrDefault(key, -1);
        if (index != -1) {
            Handle handle = Handle.values()[index];
            return HandleRegistry.getRecord(handle);
        }
        return null;
    }

    public static String getKey(String className, String methodName, String descriptor) {
        return className + "." + methodName + descriptor;
    }

    private static void initialize() {
        for (Handle handle : Handle.values()) {
            if (handle.getOwner().equals(UnsafeMasker.class)) {
                MethodRecord record = HandleRegistry.getRecord(handle);
                maskMap.put(createKey(JDK_UNSAFE_INTERNAL_NAME, record), handle.ordinal());
                maskMap.put(createKey(SUN_UNSAFE_INTERNAL_NAME, record), handle.ordinal());
            }
        }
    }

    private static String createKey(String owner, MethodRecord record) {
        String descriptor = removeFirstParameter(record.getDescriptor());
        return MaskRegistry.getKey(owner, record.getName(), descriptor);
    }

    private static String removeFirstParameter(String descriptor) {
        return '(' + descriptor.substring(descriptor.indexOf(';') + 1);
    }
}
