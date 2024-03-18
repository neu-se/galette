package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class MethodCallITCase {
    @Test
    void invokeStatic(TagManager manager, FlowChecker checker) {
        int a = manager.setLabels(7, new Object[] {"labels1"});
        int b = manager.setLabels(20, new Object[] {"labels2"});
        int actual = X.overriddenStatic(a, b);
        Assertions.assertEquals(20, actual);
        checker.check(new Object[] {"labels2"}, manager.getLabels(actual));
    }

    @Test
    void invokeStaticInherited(TagManager manager, FlowChecker checker) {
        int a = manager.setLabels(7, new Object[] {"labels1"});
        long b = manager.setLabels(20L, new Object[] {"labels2"});
        long actual = Y.inheritedStatic(a, b);
        Assertions.assertEquals(27, actual);
        checker.check(new Object[] {"labels1", "labels2"}, manager.getLabels(actual));
    }

    @Test
    void invokeStaticOverridden(TagManager manager, FlowChecker checker) {
        int a = manager.setLabels(7, new Object[] {"labels1"});
        int b = manager.setLabels(20, new Object[] {"labels2"});
        int actual = Y.overriddenStatic(a, b);
        Assertions.assertEquals(7, actual);
        checker.check(new Object[] {"labels1"}, manager.getLabels(actual));
    }

    @Test
    void invokeVirtual(TagManager manager, FlowChecker checker) {
        int a = manager.setLabels(7, new Object[] {"labels1"});
        int b = manager.setLabels(20, new Object[] {"labels2"});
        X x = new X();
        int actual = x.overriddenInstance(a, b);
        Assertions.assertEquals(20, actual);
        checker.check(new Object[] {"labels2"}, manager.getLabels(actual));
    }

    @Test
    void invokeVirtualInherited(TagManager manager, FlowChecker checker) {
        int a = manager.setLabels(7, new Object[] {"labels1"});
        int b = manager.setLabels(20, new Object[] {"labels2"});
        X x = new Y();
        int actual = x.inheritedInstance(a, b);
        Assertions.assertEquals(27, actual);
        checker.check(new Object[] {"labels1", "labels2"}, manager.getLabels(actual));
    }

    @Test
    void invokeVirtualOverridden(TagManager manager, FlowChecker checker) {
        int a = manager.setLabels(7, new Object[] {"labels1"});
        int b = manager.setLabels(20, new Object[] {"labels2"});
        X x = new Y();
        int actual = x.overriddenInstance(a, b);
        Assertions.assertEquals(7, actual);
        checker.check(new Object[] {"labels1"}, manager.getLabels(actual));
    }

    @Test
    void invokeInterface(TagManager manager, FlowChecker checker) {
        int a = manager.setLabels(7, new Object[] {"labels1"});
        int b = manager.setLabels(20, new Object[] {"labels2"});
        P p = new Z();
        int actual = p.interfaceMethod(a, b);
        Assertions.assertEquals(7, actual);
        checker.check(new Object[] {"labels1"}, manager.getLabels(actual));
    }

    @Test
    void invokeInterfaceDefault(TagManager manager, FlowChecker checker) {
        int a = manager.setLabels(7, new Object[] {"labels1"});
        int b = manager.setLabels(20, new Object[] {"labels2"});
        P p = new Z();
        int actual = p.defaultInterface(a, b);
        Assertions.assertEquals(27, actual);
        checker.check(new Object[] {"labels1", "labels2"}, manager.getLabels(actual));
    }

    @Test
    void invokeSpecialPrivateMethod(TagManager manager, FlowChecker checker) {
        int a = manager.setLabels(7, new Object[] {"labels1"});
        long b = manager.setLabels(20L, new Object[] {"labels2"});
        int c = manager.setLabels(100, new Object[] {"labels3"});
        long actual = privateMethod(a, b, c);
        Assertions.assertEquals(120, actual);
        checker.check(new Object[] {"labels2", "labels3"}, manager.getLabels(actual));
    }

    @Test
    void invokeSpecialConstructor(TagManager manager, FlowChecker checker) {
        int r = manager.setLabels(7, new Object[] {"labels1"});
        X x = new X(r);
        long actual = x.r;
        Assertions.assertEquals(7, actual);
        checker.check(new Object[] {"labels1"}, manager.getLabels(actual));
    }

    @Test
    void invokeSpecialSuper(TagManager manager, FlowChecker checker) {
        int a = manager.setLabels(7, new Object[] {"labels1"});
        int b = manager.setLabels(20, new Object[] {"labels2"});
        Z z = new Z();
        int actual = z.common(a, b);
        Assertions.assertEquals(20, actual);
        checker.check(new Object[] {"labels2"}, manager.getLabels(actual));
    }

    @SuppressWarnings("unused")
    private long privateMethod(int a, long b, int c) {
        return b + c;
    }

    interface P {
        default int defaultInterface(int a, int b) {
            return a + b;
        }

        int interfaceMethod(int a, int b);

        default int common(int a, int b) {
            return a;
        }
    }

    interface Q {
        default int common(int a, int b) {
            return b;
        }
    }

    static class Z implements P, Q {
        @Override
        public int interfaceMethod(int a, int b) {
            return a;
        }

        @Override
        public int common(int a, int b) {
            return Q.super.common(a, b);
        }
    }

    static class X {
        private int r;

        public X(int r) {
            this.r = r;
        }

        public X() {}

        public static long inheritedStatic(int a, long b) {
            return a + b;
        }

        @SuppressWarnings("unused")
        public static int overriddenStatic(int a, int b) {
            return b;
        }

        public int inheritedInstance(int a, int b) {
            return a + b;
        }

        public int overriddenInstance(int a, int b) {
            return b;
        }
    }

    static class Y extends X {
        @SuppressWarnings("unused")
        public static int overriddenStatic(int a, int b) {
            return a;
        }

        public int overriddenInstance(int a, int b) {
            return a;
        }
    }
}
