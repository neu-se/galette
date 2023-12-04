package edu.neu.ccs.prl.phosphor.internal.agent;

import edu.neu.ccs.prl.phosphor.internal.patch.HandleRegistryPatcher;
import edu.neu.ccs.prl.phosphor.internal.runtime.Handle;
import edu.neu.ccs.prl.phosphor.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.phosphor.internal.runtime.Patched;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class HandleRegistry {
    private static final MethodRecord[] records = new MethodRecord[Handle.values().length];

    static {
        initialize();
    }

    public static MethodRecord getRecord(Handle handle) {
        return records[handle.ordinal()];
    }

    /**
     * The body of this method is replaced by {@link HandleRegistryPatcher} to avoid uses of reflection.
     */
    @Patched
    private static void initialize() {
        for (Handle handle : Handle.values()) {
            records[handle.ordinal()] = createRecord(handle);
        }
    }

    /**
     * Calls to the method are added by {@link HandleRegistryPatcher}.
     */
    @SuppressWarnings("unused")
    private static void addRecord(
            int index, int opcode, String owner, String name, String descriptor, boolean isInterface) {
        records[index] = new MethodRecord(opcode, owner, name, descriptor, isInterface);
    }

    private static MethodRecord createRecord(Handle handle) {
        Class<?> owner = handle.getOwner();
        for (Method method : owner.getDeclaredMethods()) {
            InvokedViaHandle annotation = method.getAnnotation(InvokedViaHandle.class);
            if (annotation != null && annotation.handle().equals(handle)) {
                return createRecord(owner, method);
            }
        }
        for (Constructor<?> constructor : owner.getDeclaredConstructors()) {
            InvokedViaHandle annotation = constructor.getAnnotation(InvokedViaHandle.class);
            if (annotation != null && annotation.handle().equals(handle)) {
                return createRecord(owner, constructor);
            }
        }
        throw new IllegalArgumentException("Member for handle not found: " + handle);
    }

    private static MethodRecord createRecord(Class<?> owner, Constructor<?> c) {
        return new MethodRecord(
                Opcodes.INVOKESPECIAL, Type.getInternalName(owner), "<init>", Type.getConstructorDescriptor(c), false);
    }

    private static MethodRecord createRecord(Class<?> owner, Method m) {
        boolean isInterface = Modifier.isInterface(owner.getModifiers());
        int opcode = isInterface ? Opcodes.INVOKEINTERFACE : Opcodes.INVOKEVIRTUAL;
        if (Modifier.isStatic(m.getModifiers())) {
            opcode = Opcodes.INVOKESTATIC;
        }
        return new MethodRecord(
                opcode, Type.getInternalName(owner), m.getName(), Type.getMethodDescriptor(m), isInterface);
    }
}
