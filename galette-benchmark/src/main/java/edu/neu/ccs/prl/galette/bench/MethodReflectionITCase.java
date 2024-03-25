package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.lang.reflect.Method;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class MethodReflectionITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    @Test
    void getDeclaredMethods() {
        Method[] methods = Example.class.getDeclaredMethods();
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
        Method[] methods = Example.class.getMethods();
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

    @Test
    void objectHashCode() throws ReflectiveOperationException {
        int a = manager.setLabels(7, new Object[] {"labels1"});
        Object o = new Object() {
            private final int j = a;

            @Override
            public int hashCode() {
                return j;
            }
        };
        Method m = Object.class.getDeclaredMethod("hashCode");
        int actual = (int) m.invoke(o);
        Assertions.assertEquals(7, actual);
        checker.check(new Object[] {"labels1"}, manager.getLabels(actual));
    }

    void intArgument() {
        // TODO static, virtual, interface, taint receiver, etc.
    }

    void longArgument() {}

    void objectArgument() {}

    void primitiveArrayArgument() {}

    void multidimensionalArrayArgument() {}

    void boxedTypeArgument() {
        Integer i = new Integer(7);
    }

    void intReturn() {}

    void longReturn() {}

    void objectReturn() {}

    void primitiveArrayReturn() {}

    void multidimensionalArrayReturn() {}

    void boxedTypeReturn() {
        Integer i = new Integer(7);
    }

    static class Example {
        private final int x;
        private long j;

        Example(int x) {
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
}
