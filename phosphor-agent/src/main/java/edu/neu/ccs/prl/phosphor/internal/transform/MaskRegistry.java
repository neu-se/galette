package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.patch.RegistryPatcher;
import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import edu.neu.ccs.prl.phosphor.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.phosphor.internal.runtime.Patched;
import edu.neu.ccs.prl.phosphor.internal.runtime.collection.SimpleList;
import edu.neu.ccs.prl.phosphor.internal.runtime.collection.SimpleMap;
import edu.neu.ccs.prl.phosphor.internal.runtime.mask.Mask;
import edu.neu.ccs.prl.phosphor.internal.runtime.mask.MaskType;
import edu.neu.ccs.prl.phosphor.internal.runtime.mask.ReflectionMasker;
import edu.neu.ccs.prl.phosphor.internal.runtime.mask.UnsafeMasker;
import java.lang.reflect.Method;
import org.objectweb.asm.Type;

public final class MaskRegistry {
    private static final Class<?>[] SOURCES = new Class[] {UnsafeMasker.class, ReflectionMasker.class};
    private static final SimpleMap<String, MaskInfo> masks = new SimpleMap<>();

    private MaskRegistry() {
        throw new AssertionError();
    }

    static {
        initialize();
    }

    public static MaskInfo getMask(String className, String methodName, String descriptor) {
        return getMask(getKey(className, methodName, descriptor));
    }

    public static MaskInfo getMask(String key) {
        return masks.get(key);
    }

    public static String getKey(String className, String methodName, String descriptor) {
        return className + "." + methodName + descriptor;
    }

    public static SimpleList<String> getKeys() {
        return masks.getKeys();
    }

    /**
     * The body of this method is replaced by {@link RegistryPatcher} to avoid uses of reflection.
     */
    @Patched
    private static void initialize() {
        for (Class<?> clazz : SOURCES) {
            for (Method method : clazz.getDeclaredMethods()) {
                for (Mask mask : method.getAnnotationsByType(Mask.class)) {
                    String key = createKey(method, mask);
                    MethodRecord record = MethodRecord.createRecord(clazz, method);
                    masks.put(key, new MaskInfo(mask.type(), record));
                }
            }
        }
    }

    @InvokedViaHandle(handle = Handle.MASK_REGISTRY_PUT)
    private static void put(
            String key,
            int typeOrdinal,
            int opcode,
            String owner,
            String name,
            String descriptor,
            boolean isInterface) {
        MethodRecord record = new MethodRecord(opcode, owner, name, descriptor, isInterface);
        masks.put(key, new MaskInfo(MaskType.values()[typeOrdinal], record));
    }

    private static String createKey(Method method, Mask mask) {
        String descriptor = Type.getMethodDescriptor(method);
        if (!mask.isStatic()) {
            // Remove the parameter for the receiver
            descriptor = '(' + descriptor.substring(descriptor.indexOf(';') + 1);
        }
        return MaskRegistry.getKey(mask.owner(), mask.name(), descriptor);
    }

    public static final class MaskInfo {
        private final MaskType type;
        private final MethodRecord record;

        public MaskInfo(MaskType type, MethodRecord record) {
            this.type = type;
            this.record = record;
        }

        public MaskType getType() {
            return type;
        }

        public MethodRecord getRecord() {
            return record;
        }
    }
}
