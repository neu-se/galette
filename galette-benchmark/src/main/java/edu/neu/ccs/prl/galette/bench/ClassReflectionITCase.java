package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class ClassReflectionITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    @Test
    void getDeclaredMethods() {
        Method[] methods = Parent.class.getDeclaredMethods();
        Set<List<Object>> expected = new HashSet<>(Arrays.asList(
                Arrays.asList("sum", int.class, int.class, int.class),
                Arrays.asList("getX", int.class),
                Arrays.asList("getJ", long.class),
                Arrays.asList("setJ", void.class, long.class)));

        Set<List<Object>> actual = collectMethods(methods);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getMethods() {
        Method[] methods = Parent.class.getMethods();
        Set<List<Object>> expected = new HashSet<>(Arrays.asList(
                Arrays.asList("sum", int.class, int.class, int.class),
                Arrays.asList("setJ", void.class, long.class),
                Arrays.asList("notify", void.class),
                Arrays.asList("wait", void.class, long.class),
                Arrays.asList("notifyAll", void.class),
                Arrays.asList("wait", void.class),
                Arrays.asList("equals", boolean.class, Object.class),
                Arrays.asList("hashCode", int.class),
                Arrays.asList("wait", void.class, long.class, int.class),
                Arrays.asList("getClass", Class.class),
                Arrays.asList("toString", String.class)));
        Set<List<Object>> actual = collectMethods(methods);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getDeclaredFields() {
        Field[] fields = Child.class.getDeclaredFields();
        Set<List<Object>> expected =
                new HashSet<>(Arrays.asList(Arrays.asList("o", Object.class), Arrays.asList("c", char.class)));

        Set<List<Object>> actual = collectFields(fields);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getFields() {
        Field[] fields = Child.class.getFields();
        Set<List<Object>> expected =
                new HashSet<>(Arrays.asList(Arrays.asList("x", int.class), Arrays.asList("o", Object.class)));
        Set<List<Object>> actual = collectFields(fields);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getSuperclass() {
        Assertions.assertEquals(Object.class, Parent.class.getSuperclass());
        Assertions.assertEquals(Parent.class, Child.class.getSuperclass());
    }

    @Test
    void getInterfaces() {
        Assertions.assertArrayEquals(new Object[] {A.class}, Child.class.getInterfaces());
    }

    @Test
    void getGenericInterfaces() {
        Assertions.assertArrayEquals(new Object[] {A.class}, Child.class.getGenericInterfaces());
    }

    private static Set<List<Object>> collectMethods(Method[] methods) {
        Set<List<Object>> actual = new HashSet<>();
        for (Method m : methods) {
            List<Object> l = new ArrayList<>();
            l.add(m.getName());
            l.add(m.getReturnType());
            l.addAll(Arrays.asList(m.getParameterTypes()));
            actual.add(l);
        }
        return actual;
    }

    private static Set<List<Object>> collectFields(Field[] fields) {
        Set<List<Object>> actual = new HashSet<>();
        for (Field f : fields) {
            actual.add(Arrays.asList(f.getName(), f.getType()));
        }
        return actual;
    }

    interface A {}

    @SuppressWarnings("unused")
    static class Parent {
        public final int x;
        private long j;

        Parent(int x) {
            this.x = x;
        }

        public int sum(int a, int b) {
            return a + b;
        }

        int getX() {
            return x;
        }

        private long getJ() {
            return j;
        }

        public void setJ(long j) {
            this.j = j;
        }
    }

    @SuppressWarnings("unused")
    static class Child extends Parent implements A {
        public Object o;
        private char c;

        Child(int x) {
            super(x);
        }
    }
}
