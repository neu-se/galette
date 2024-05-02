package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
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
    public int arrayIndexScale(Class<?> clazz) {
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

    @SuppressWarnings({"unused", "SameParameterValue"})
    @MemberAccess(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeByte", opcode = Opcodes.INVOKEVIRTUAL)
    private static byte compareAndExchangeByte(
            Unsafe unsafe, Object o, long offset, byte expected, byte x, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @Override
    public byte compareAndExchangeByte(Object o, long offset, byte expected, byte x) {
        return compareAndExchangeByte(UNSAFE, o, offset, expected, x, TagFrame.emptyFrame());
    }

    @SuppressWarnings({"unused", "SameParameterValue"})
    @MemberAccess(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeShort", opcode = Opcodes.INVOKEVIRTUAL)
    private static short compareAndExchangeShort(
            Unsafe unsafe, Object o, long offset, short expected, short x, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @Override
    public short compareAndExchangeShort(Object o, long offset, short expected, short x) {
        return compareAndExchangeShort(UNSAFE, o, offset, expected, x, TagFrame.emptyFrame());
    }
}
