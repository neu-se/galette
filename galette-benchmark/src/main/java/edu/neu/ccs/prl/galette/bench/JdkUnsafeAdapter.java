package edu.neu.ccs.prl.galette.bench;

import java.lang.reflect.Field;
import jdk.internal.misc.Unsafe;

public class JdkUnsafeAdapter implements UnsafeAdapter {
    private static final Unsafe UNSAFE = Unsafe.getUnsafe();

    @Override
    public long objectFieldOffset(Field f) {
        return UNSAFE.objectFieldOffset(f);
    }

    @Override
    public long staticFieldOffset(Field f) {
        return UNSAFE.staticFieldOffset(f);
    }

    @Override
    public Object staticFieldBase(Field f) {
        return UNSAFE.staticFieldBase(f);
    }

    @Override
    public int arrayBaseOffset(Class<?> arrayClass) {
        return UNSAFE.arrayBaseOffset(arrayClass);
    }

    @Override
    public void putBoolean(Object o, long offset, boolean x) {
        UNSAFE.putBoolean(o, offset, x);
    }

    @Override
    public void putByte(Object o, long offset, byte x) {
        UNSAFE.putByte(o, offset, x);
    }

    @Override
    public void putChar(Object o, long offset, char x) {
        UNSAFE.putChar(o, offset, x);
    }

    @Override
    public void putDouble(Object o, long offset, double x) {
        UNSAFE.putDouble(o, offset, x);
    }

    @Override
    public void putFloat(Object o, long offset, float x) {
        UNSAFE.putFloat(o, offset, x);
    }

    @Override
    public void putInt(Object o, long offset, int x) {
        UNSAFE.putInt(o, offset, x);
    }

    @Override
    public void putLong(Object o, long offset, long x) {
        UNSAFE.putLong(o, offset, x);
    }

    @Override
    public void putShort(Object o, long offset, short x) {
        UNSAFE.putShort(o, offset, x);
    }

    @Override
    public void putObject(Object o, long offset, Object x) {
        UNSAFE.putObject(o, offset, x);
    }

    @Override
    public void putBooleanVolatile(Object o, long offset, boolean x) {
        UNSAFE.putBooleanVolatile(o, offset, x);
    }

    @Override
    public void putByteVolatile(Object o, long offset, byte x) {
        UNSAFE.putByteVolatile(o, offset, x);
    }

    @Override
    public void putCharVolatile(Object o, long offset, char x) {
        UNSAFE.putCharVolatile(o, offset, x);
    }

    @Override
    public void putDoubleVolatile(Object o, long offset, double x) {
        UNSAFE.putDoubleVolatile(o, offset, x);
    }

    @Override
    public void putFloatVolatile(Object o, long offset, float x) {
        UNSAFE.putFloatVolatile(o, offset, x);
    }

    @Override
    public void putIntVolatile(Object o, long offset, int x) {
        UNSAFE.putIntVolatile(o, offset, x);
    }

    @Override
    public void putLongVolatile(Object o, long offset, long x) {
        UNSAFE.putLongVolatile(o, offset, x);
    }

    @Override
    public void putShortVolatile(Object o, long offset, short x) {
        UNSAFE.putShortVolatile(o, offset, x);
    }

    @Override
    public void putObjectVolatile(Object o, long offset, Object x) {
        UNSAFE.putObjectVolatile(o, offset, x);
    }

    @Override
    public boolean getBoolean(Object o, long offset) {
        return UNSAFE.getBoolean(o, offset);
    }

    @Override
    public byte getByte(Object o, long offset) {
        return UNSAFE.getByte(o, offset);
    }

    @Override
    public char getChar(Object o, long offset) {
        return UNSAFE.getChar(o, offset);
    }

    @Override
    public double getDouble(Object o, long offset) {
        return UNSAFE.getDouble(o, offset);
    }

    @Override
    public float getFloat(Object o, long offset) {
        return UNSAFE.getFloat(o, offset);
    }

    @Override
    public int getInt(Object o, long offset) {
        return UNSAFE.getInt(o, offset);
    }

    @Override
    public long getLong(Object o, long offset) {
        return UNSAFE.getLong(o, offset);
    }

    @Override
    public short getShort(Object o, long offset) {
        return UNSAFE.getShort(o, offset);
    }

    @Override
    public Object getObject(Object o, long offset) {
        return UNSAFE.getObject(o, offset);
    }

    @Override
    public boolean getBooleanVolatile(Object o, long offset) {
        return UNSAFE.getBooleanVolatile(o, offset);
    }

    @Override
    public byte getByteVolatile(Object o, long offset) {
        return UNSAFE.getByteVolatile(o, offset);
    }

    @Override
    public char getCharVolatile(Object o, long offset) {
        return UNSAFE.getCharVolatile(o, offset);
    }

    @Override
    public double getDoubleVolatile(Object o, long offset) {
        return UNSAFE.getDoubleVolatile(o, offset);
    }

    @Override
    public float getFloatVolatile(Object o, long offset) {
        return UNSAFE.getFloatVolatile(o, offset);
    }

    @Override
    public int getIntVolatile(Object o, long offset) {
        return UNSAFE.getIntVolatile(o, offset);
    }

    @Override
    public long getLongVolatile(Object o, long offset) {
        return UNSAFE.getLongVolatile(o, offset);
    }

    @Override
    public short getShortVolatile(Object o, long offset) {
        return UNSAFE.getShortVolatile(o, offset);
    }

    @Override
    public Object getObjectVolatile(Object o, long offset) {
        return UNSAFE.getObjectVolatile(o, offset);
    }

    @Override
    public void putOrderedInt(Object o, long offset, int x) {
        UNSAFE.putIntRelease(o, offset, x);
    }

    @Override
    public void putOrderedLong(Object o, long offset, long x) {
        UNSAFE.putLongRelease(o, offset, x);
    }

    @Override
    public void putOrderedObject(Object o, long offset, Object x) {
        UNSAFE.putObjectRelease(o, offset, x);
    }

    @Override
    public boolean compareAndSwapInt(Object o, long offset, int expected, int x) {
        return UNSAFE.compareAndSetInt(o, offset, expected, x);
    }

    @Override
    public boolean compareAndSwapLong(Object o, long offset, long expected, long x) {
        return UNSAFE.compareAndSetLong(o, offset, expected, x);
    }

    @Override
    public boolean compareAndSwapObject(Object o, long offset, Object expected, Object x) {
        return UNSAFE.compareAndSetObject(o, offset, expected, x);
    }

    @Override
    public int compareAndExchangeInt(Object o, long offset, int expected, int x) {
        return UNSAFE.compareAndExchangeInt(o, offset, expected, x);
    }

    @Override
    public long compareAndExchangeLong(Object o, long offset, long expected, long x) {
        return UNSAFE.compareAndExchangeLong(o, offset, expected, x);
    }

    @Override
    public Object compareAndExchangeObject(Object o, long offset, Object expected, Object x) {
        return UNSAFE.compareAndExchangeObject(o, offset, expected, x);
    }
}
