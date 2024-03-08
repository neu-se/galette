package edu.neu.ccs.prl.galette.bench;

import java.lang.reflect.Field;

public interface UnsafeWrapper {
    void putInt(Object o, long offset, int x);

    void putObject(Object o, long offset, Object x);

    void putBoolean(Object o, long offset, boolean x);

    void putByte(Object o, long offset, byte x);

    void putShort(Object o, long offset, short x);

    void putChar(Object o, long offset, char x);

    void putLong(Object o, long offset, long x);

    void putFloat(Object o, long offset, float x);

    void putDouble(Object o, long offset, double x);

    long objectFieldOffset(Field f);

    long staticFieldOffset(Field f);

    Object staticFieldBase(Field f);

    int arrayBaseOffset(Class<?> arrayClass);

    boolean compareAndSwapObject(Object o, long offset, Object expected, Object x);

    boolean compareAndSwapInt(Object o, long offset, int expected, int x);

    boolean compareAndSwapLong(Object o, long offset, long expected, long x);

    Object getObjectVolatile(Object o, long offset);

    void putObjectVolatile(Object o, long offset, Object x);

    int getIntVolatile(Object o, long offset);

    void putIntVolatile(Object o, long offset, int x);

    boolean getBooleanVolatile(Object o, long offset);

    void putBooleanVolatile(Object o, long offset, boolean x);

    byte getByteVolatile(Object o, long offset);

    void putByteVolatile(Object o, long offset, byte x);

    short getShortVolatile(Object o, long offset);

    void putShortVolatile(Object o, long offset, short x);

    char getCharVolatile(Object o, long offset);

    void putCharVolatile(Object o, long offset, char x);

    long getLongVolatile(Object o, long offset);

    void putLongVolatile(Object o, long offset, long x);

    float getFloatVolatile(Object o, long offset);

    void putFloatVolatile(Object o, long offset, float x);

    double getDoubleVolatile(Object o, long offset);

    void putDoubleVolatile(Object o, long offset, double x);

    void putOrderedObject(Object o, long offset, Object x);

    void putOrderedInt(Object o, long offset, int x);

    void putOrderedLong(Object o, long offset, long x);
}
