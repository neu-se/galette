package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.transform.Configuration;
import java.lang.reflect.Field;

public interface UnsafeWrapper {
    int getInvalidFieldOffset();

    long objectFieldOffset(Field f);

    long staticFieldOffset(Field f);

    long arrayIndexScale(Class<?> clazz);

    int arrayBaseOffset(Class<?> arrayClass);

    void putObject(Object o, long offset, Object x);

    void putObjectVolatile(Object o, long offset, Object x);

    Object getObject(Object o, long offset);

    Object getObjectVolatile(Object o, long offset);

    static UnsafeWrapper createInstance() {
        return Configuration.isJava8() ? new SunUnsafeWrapper() : new JdkUnsafeWrapper();
    }
}
