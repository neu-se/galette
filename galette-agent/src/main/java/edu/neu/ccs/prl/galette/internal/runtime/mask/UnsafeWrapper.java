package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.transform.Configuration;
import java.lang.reflect.Field;
import java.security.ProtectionDomain;

public interface UnsafeWrapper {
    int getInvalidFieldOffset();

    long objectFieldOffset(Field f);

    long staticFieldOffset(Field f);

    long arrayIndexScale(Class<?> clazz);

    int arrayBaseOffset(Class<?> arrayClass);

    Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches);

    Class<?> defineClass(
            String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain);

    void putBoolean(Object o, long offset, boolean x);

    void putByte(Object o, long offset, byte x);

    void putChar(Object o, long offset, char x);

    void putDouble(Object o, long offset, double x);

    void putFloat(Object o, long offset, float x);

    void putInt(Object o, long offset, int x);

    void putLong(Object o, long offset, long x);

    void putShort(Object o, long offset, short x);

    void putObject(Object o, long offset, Object x);

    void putBooleanVolatile(Object o, long offset, boolean x);

    void putByteVolatile(Object o, long offset, byte x);

    void putCharVolatile(Object o, long offset, char x);

    void putDoubleVolatile(Object o, long offset, double x);

    void putFloatVolatile(Object o, long offset, float x);

    void putIntVolatile(Object o, long offset, int x);

    void putLongVolatile(Object o, long offset, long x);

    void putShortVolatile(Object o, long offset, short x);

    void putObjectVolatile(Object o, long offset, Object x);

    boolean getBoolean(Object o, long offset);

    byte getByte(Object o, long offset);

    char getChar(Object o, long offset);

    double getDouble(Object o, long offset);

    float getFloat(Object o, long offset);

    int getInt(Object o, long offset);

    long getLong(Object o, long offset);

    short getShort(Object o, long offset);

    Object getObject(Object o, long offset);

    boolean getBooleanVolatile(Object o, long offset);

    byte getByteVolatile(Object o, long offset);

    char getCharVolatile(Object o, long offset);

    double getDoubleVolatile(Object o, long offset);

    float getFloatVolatile(Object o, long offset);

    int getIntVolatile(Object o, long offset);

    long getLongVolatile(Object o, long offset);

    short getShortVolatile(Object o, long offset);

    Object getObjectVolatile(Object o, long offset);

    void putOrderedInt(Object o, long offset, int x);

    void putOrderedLong(Object o, long offset, long x);

    void putOrderedObject(Object o, long offset, Object x);

    boolean compareAndSwapInt(Object o, long offset, int expected, int x);

    boolean compareAndSwapLong(Object o, long offset, long expected, long x);

    boolean compareAndSwapObject(Object o, long offset, Object expected, Object x);

    int compareAndExchangeInt(Object o, long offset, int expected, int x);

    long compareAndExchangeLong(Object o, long offset, long expected, long x);

    Object compareAndExchangeObject(Object o, long offset, Object expected, Object x);

    static UnsafeWrapper createInstance() {
        return Configuration.isJava8() ? new SunUnsafeWrapper() : new JdkUnsafeWrapper();
    }
}
