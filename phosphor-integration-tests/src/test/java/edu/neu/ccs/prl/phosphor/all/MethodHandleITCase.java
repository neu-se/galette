package edu.neu.ccs.prl.phosphor.all;

import static java.lang.invoke.MethodType.genericMethodType;
import static java.lang.invoke.MethodType.methodType;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MethodHandleITCase {
    @Test
    void invokeExactVirtual() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType mt = methodType(String.class, char.class, char.class);
        MethodHandle mh = lookup.findVirtual(String.class, "replace", mt);
        String s = (String) mh.invokeExact("function", 'f', 'j');
        assertEquals("junction", s);
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
        assertEquals(result1, result2);
    }

    @Test
    void filter() throws Throwable {
        MethodHandle toString = MethodHandles.lookup().findVirtual(String.class, "toString", methodType(String.class));
        Holder holder = new Holder();
        MethodHandle setX = MethodHandles.lookup()
                .findVirtual(Holder.class, "setX", methodType(void.class, int.class))
                .bindTo(holder);
        MethodHandle collect = MethodHandles.collectArguments(toString, 1, setX);
        assertEquals("hello", (String) collect.invokeExact("hello", 7));
        Assertions.assertEquals(7, holder.getX());
    }

    interface Example {
        Object apply(Object a1, Object a2);
    }

    public static class Holder {
        private int x;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }
    }
}
