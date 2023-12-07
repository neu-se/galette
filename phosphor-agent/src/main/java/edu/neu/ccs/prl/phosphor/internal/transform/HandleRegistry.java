package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.patch.RegistryPatcher;
import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import edu.neu.ccs.prl.phosphor.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.phosphor.internal.runtime.Patched;
import edu.neu.ccs.prl.phosphor.internal.runtime.collection.SimpleMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class HandleRegistry {
    private static final SimpleMap<Handle, MethodRecord> handles = new SimpleMap<>();

    static {
        initialize();
    }

    public static MethodRecord getRecord(Handle handle) {
        return handles.get(handle);
    }

    /**
     * The body of this method is replaced by {@link RegistryPatcher} to avoid uses of reflection.
     */
    @Patched
    private static void initialize() {
        for (Handle handle : Handle.values()) {
            handles.put(handle, createRecord(handle));
        }
    }

    @InvokedViaHandle(handle = Handle.HANDLE_REGISTRY_PUT)
    private static void put(int index, int opcode, String owner, String name, String descriptor, boolean isInterface) {
        handles.put(Handle.values()[index], new MethodRecord(opcode, owner, name, descriptor, isInterface));
    }

    private static MethodRecord createRecord(Handle handle) {
        Class<?> owner = handle.getOwner();
        for (Method method : owner.getDeclaredMethods()) {
            InvokedViaHandle annotation = method.getAnnotation(InvokedViaHandle.class);
            if (annotation != null && annotation.handle().equals(handle)) {
                return MethodRecord.createRecord(owner, method);
            }
        }
        for (Constructor<?> constructor : owner.getDeclaredConstructors()) {
            InvokedViaHandle annotation = constructor.getAnnotation(InvokedViaHandle.class);
            if (annotation != null && annotation.handle().equals(handle)) {
                return MethodRecord.createRecord(owner, constructor);
            }
        }
        throw new IllegalArgumentException("Member for handle not found: " + handle);
    }
}
