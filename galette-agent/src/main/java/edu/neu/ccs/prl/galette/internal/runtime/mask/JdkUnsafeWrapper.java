package edu.neu.ccs.prl.galette.internal.runtime.mask;

import java.lang.reflect.Field;
import jdk.internal.misc.Unsafe;
import org.objectweb.asm.Opcodes;

public final class JdkUnsafeWrapper implements UnsafeWrapper {
    private static final Unsafe UNSAFE = Unsafe.getUnsafe();

    @Override
    public int getInvalidFieldOffset() {
        return Unsafe.INVALID_FIELD_OFFSET;
    }

    @Override
    public long objectFieldOffset(Field f) {
        return UNSAFE.objectFieldOffset(f);
    }

    @Override
    public long staticFieldOffset(Field f) {
        return UNSAFE.staticFieldOffset(f);
    }

    @Override
    public long arrayIndexScale(Class<?> clazz) {
        return UNSAFE.arrayIndexScale(clazz);
    }

    @Override
    public int arrayBaseOffset(Class<?> arrayClass) {
        return UNSAFE.arrayBaseOffset(arrayClass);
    }

    @SuppressWarnings({"unused", "SameParameterValue"})
    @MemberAccess(owner = "jdk/internal/misc/Unsafe", name = "defineAnonymousClass0", opcode = Opcodes.INVOKEVIRTUAL)
    private static Class<?> defineAnonymousClass0(Unsafe unsafe, Class<?> hostClass, byte[] data, Object[] cpPatches) {
        throw new AssertionError("Placeholder method was called");
    }

    @Override
    public void putObject(Object o, long offset, Object x) {
        UNSAFE.putObject(o, offset, x);
    }

    @Override
    public void putObjectVolatile(Object o, long offset, Object x) {
        UNSAFE.putObjectVolatile(o, offset, x);
    }

    @Override
    public Object getObject(Object o, long offset) {
        return UNSAFE.getObject(o, offset);
    }

    @Override
    public Object getObjectVolatile(Object o, long offset) {
        return UNSAFE.getObjectVolatile(o, offset);
    }
}
