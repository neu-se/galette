package edu.neu.ccs.prl.galette.internal.runtime.mask;

import java.lang.reflect.Field;
import java.security.ProtectionDomain;
import org.objectweb.asm.Opcodes;
import sun.misc.Unsafe;

public final class SunUnsafeWrapper implements UnsafeWrapper {
    private static final Unsafe UNSAFE = getUnsafe();

    @MemberAccess(owner = "sun/misc/Unsafe", name = "getUnsafe", opcode = Opcodes.INVOKESTATIC)
    public static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

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
