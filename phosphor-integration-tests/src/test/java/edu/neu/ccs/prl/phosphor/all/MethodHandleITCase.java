package edu.neu.ccs.prl.phosphor.all;

import static java.lang.invoke.MethodType.genericMethodType;

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

    @Test
    void boundMethodHandle() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = lookup.findVirtual(Example.class, "apply", genericMethodType(2));
        Object a1 = "hello";
        Object a2 = "world";
        Example val = (x1, x2) -> "present";
        MethodHandle bmh = mh.bindTo(val);
        Object result1 = bmh.invokeExact(a1, a2);
        Object result2 = mh.invokeExact(val, a1, a2);
        Assertions.assertEquals(result1, result2);
    }

    interface Example {
        Object apply(Object a1, Object a2);
    }
}
