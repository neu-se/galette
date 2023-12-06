package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.patch.RegistryPatcher;
import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import edu.neu.ccs.prl.phosphor.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.phosphor.internal.runtime.Patched;
import edu.neu.ccs.prl.phosphor.internal.runtime.collection.SimpleList;
import edu.neu.ccs.prl.phosphor.internal.runtime.collection.SimpleMap;
import edu.neu.ccs.prl.phosphor.internal.runtime.mask.Mask;
import edu.neu.ccs.prl.phosphor.internal.runtime.mask.UnsafeMasker;
import java.lang.reflect.Method;
import org.objectweb.asm.Type;

public final class MaskRegistry {
    private static final Class<?>[] SOURCES = new Class[] {UnsafeMasker.class};
    private static final SimpleMap<String, MethodRecord> masks = new SimpleMap<>();

    private MaskRegistry() {
        throw new AssertionError();
    }

    static {
        initialize();
    }

    public static MethodRecord getMask(String className, String methodName, String descriptor) {
        return getMask(getKey(className, methodName, descriptor));
    }

    public static MethodRecord getMask(String key) {
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
                    masks.put(key, MethodRecord.createRecord(clazz, method));
                }
            }
        }
    }

    @InvokedViaHandle(handle = Handle.MASK_REGISTRY_PUT)
    private static void put(String key, int opcode, String owner, String name, String descriptor, boolean isInterface) {
        masks.put(key, new MethodRecord(opcode, owner, name, descriptor, isInterface));
    }

    private static String createKey(Method method, Mask mask) {
        String descriptor = Type.getMethodDescriptor(method);
        if (!mask.isStatic()) {
            // Remove the parameter for the receiver
            descriptor = '(' + descriptor.substring(descriptor.indexOf(';') + 1);
        }
        return MaskRegistry.getKey(mask.name(), mask.owner(), descriptor);
    }
}
