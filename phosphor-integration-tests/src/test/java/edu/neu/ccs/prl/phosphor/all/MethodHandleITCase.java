package edu.neu.ccs.prl.phosphor.all;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MethodHandleITCase {
    @Test
    void invokeExactVirtual() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType mt = MethodType.methodType(String.class, char.class, char.class);
        MethodHandle mh = lookup.findVirtual(String.class, "replace", mt);
        String s = (String) mh.invokeExact("function", 'f', 'j');
        Assertions.assertEquals("junction", s);
    }
}
