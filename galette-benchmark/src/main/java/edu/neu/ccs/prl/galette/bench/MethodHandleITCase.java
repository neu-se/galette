package edu.neu.ccs.prl.galette.bench;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntToLongFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

@FlowBench
public class MethodHandleITCase {
    private final Lookup lookup = lookup();

    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    @Test
    void lookupFindStatic() throws Throwable {
        MethodType mt = methodType(int.class, int.class, int.class);
        MethodHandle mh = lookup.findStatic(Math.class, "max", mt);
        int a = manager.setLabels(5, new Object[] {"a"});
        int b = manager.setLabels(90, new Object[] {"b"});
        int actual = (int) mh.invokeExact(a, b);
        Assertions.assertEquals(90, actual);
        checker.check(new Object[] {"b"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindVirtual() throws Throwable {
        Parent p = new Parent(manager.setLabels(9, new Object[] {"x"}));
        MethodType mt = methodType(int.class);
        MethodHandle mh = lookup.findVirtual(Parent.class, "getX", mt);
        int actual = (int) mh.invokeExact(p);
        Assertions.assertEquals(9, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindConstructor() throws Throwable {
        int x = manager.setLabels(22, new Object[] {"x"});
        MethodType mt = methodType(void.class, int.class);
        MethodHandle mh = lookup.findConstructor(Parent.class, mt);
        Parent p = (Parent) mh.invokeExact(x);
        int actual = p.getX();
        Assertions.assertEquals(22, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindSpecial() throws Throwable {
        int x = manager.setLabels(75, new Object[] {"x"});
        int y = manager.setLabels(25, new Object[] {"y"});
        MethodType mt = methodType(int.class, int.class);
        MethodHandle mh = Child.lookup().findSpecial(Parent.class, "getXPlus", mt, Child.class);
        Child c = new Child();
        c.setX(x);
        int actual = (int) mh.invokeExact(c, y);
        Assertions.assertEquals(100, actual);
        checker.check(new Object[] {"x", "y"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindGetter() throws Throwable {
        int x = manager.setLabels(75, new Object[] {"x"});
        MethodHandle mh = lookup.findGetter(Parent.class, "x", int.class);
        Parent p = new Parent(x);
        int actual = (int) mh.invokeExact(p);
        Assertions.assertEquals(75, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindSetter() throws Throwable {
        int x = manager.setLabels(75, new Object[] {"x"});
        MethodHandle mh = lookup.findSetter(Parent.class, "x", int.class);
        Parent p = new Parent();
        mh.invokeExact(p, x);
        int actual = p.x;
        Assertions.assertEquals(75, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindStaticGetter() throws Throwable {
        long j = manager.setLabels(8L, new Object[] {"j"});
        MethodHandle mh = lookup.findStaticGetter(Parent.class, "j", long.class);
        Parent.j = j;
        long actual = (long) mh.invokeExact();
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindStaticSetter() throws Throwable {
        long j = manager.setLabels(8L, new Object[] {"j"});
        MethodHandle mh = lookup.findStaticSetter(Parent.class, "j", long.class);
        mh.invokeExact(j);
        long actual = Parent.j;
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void lookupBind() throws Throwable {
        Parent p = new Parent(manager.setLabels(9, new Object[] {"x"}));
        MethodType mt = methodType(int.class);
        MethodHandle mh = lookup.bind(p, "getX", mt);
        int actual = (int) mh.invokeExact();
        Assertions.assertEquals(9, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflect() throws Throwable {
        int x = manager.setLabels(75, new Object[] {"x"});
        int y = manager.setLabels(25, new Object[] {"y"});
        Method m = Parent.class.getDeclaredMethod("getXPlus", int.class);
        MethodHandle mh = lookup.unreflect(m);
        Parent parent = new Parent(x);
        int actual = (int) mh.invokeExact(parent, y);
        Assertions.assertEquals(100, actual);
        checker.check(new Object[] {"x", "y"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflectSpecial() throws Throwable {
        int x = manager.setLabels(75, new Object[] {"x"});
        int y = manager.setLabels(25, new Object[] {"y"});
        Method m = Parent.class.getDeclaredMethod("getXPlus", int.class);
        MethodHandle mh = Child.lookup().unreflectSpecial(m, Child.class);
        Child c = new Child();
        c.setX(x);
        int actual = (int) mh.invokeExact(c, y);
        Assertions.assertEquals(100, actual);
        checker.check(new Object[] {"x", "y"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflectConstructor() throws Throwable {
        MethodHandle mh = lookup.unreflectConstructor(Parent.class.getDeclaredConstructor(int.class));
        int x = manager.setLabels(22, new Object[] {"x"});
        Parent p = (Parent) mh.invokeExact(x);
        int actual = p.getX();
        Assertions.assertEquals(22, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflectGetter() throws Throwable {
        long j = manager.setLabels(8L, new Object[] {"j"});
        MethodHandle mh = lookup.unreflectGetter(Parent.class.getDeclaredField("j"));
        Parent.j = j;
        long actual = (long) mh.invokeExact();
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflectSetter() throws Throwable {
        long j = manager.setLabels(8L, new Object[] {"j"});
        MethodHandle mh = lookup.unreflectSetter(Parent.class.getDeclaredField("j"));
        mh.invokeExact(j);
        long actual = Parent.j;
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleInvokeExact() throws Throwable {
        MethodType mt = methodType(long.class, long.class, long.class);
        MethodHandle mh = lookup.findStatic(Math.class, "max", mt);
        long a = manager.setLabels(5L, new Object[] {"a"});
        long b = manager.setLabels(90L, new Object[] {"b"});
        long actual = (long) mh.invokeExact(a, b);
        Assertions.assertEquals(90, actual);
        checker.check(new Object[] {"b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleInvoke() throws Throwable {
        MethodType mt = methodType(long.class, long.class, long.class);
        MethodHandle mh = lookup.findStatic(Math.class, "max", mt);
        int a = manager.setLabels(5, new Object[] {"a"});
        int b = manager.setLabels(90, new Object[] {"b"});
        long actual = (long) mh.invoke(a, b);
        Assertions.assertEquals(90, actual);
        checker.check(new Object[] {"b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleInvokeWithArguments() throws Throwable {
        MethodType mt = methodType(long.class, long.class, long.class);
        MethodHandle mh = lookup.findStatic(Math.class, "max", mt);
        int a = manager.setLabels(5, new Object[] {"a"});
        int b = manager.setLabels(90, new Object[] {"b"});
        List<Object> arguments = Arrays.asList(a, b);
        long actual = (long) mh.invokeWithArguments(arguments);
        Assertions.assertEquals(90, actual);
        checker.check(new Object[] {"b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleAsType() throws Throwable {
        MethodType mt = methodType(long.class, long.class, long.class);
        MethodHandle mh = lookup.findStatic(Math.class, "max", mt).asType(methodType(long.class, int.class, int.class));
        int a = manager.setLabels(5, new Object[] {"a"});
        int b = manager.setLabels(90, new Object[] {"b"});
        long actual = (long) mh.invokeExact(a, b);
        Assertions.assertEquals(90, actual);
        checker.check(new Object[] {"b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleAsSpreader() throws Throwable {
        MethodType mt = methodType(double.class, double.class, double.class);
        MethodHandle mh = lookup.findStatic(Double.class, "sum", mt).asSpreader(double[].class, 2);
        double a = manager.setLabels(5.0, new Object[] {"a"});
        double b = manager.setLabels(90, new Object[] {"b"});
        double actual = (double) mh.invokeExact(new double[] {a, b});
        Assertions.assertEquals(95.0, actual);
        checker.check(new Object[] {"a", "b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleAsCollector() throws Throwable {
        MethodType mt = methodType(int.class, int[].class);
        MethodHandle mh = lookup.findStatic(MethodHandleITCase.class, "sum", mt).asCollector(int[].class, 2);
        int x = manager.setLabels(75, new Object[] {"x"});
        int y = manager.setLabels(25, new Object[] {"y"});
        int actual = (int) mh.invokeExact(x, y);
        Assertions.assertEquals(100, actual);
        checker.check(new Object[] {"x", "y"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleAsVarargsCollector() throws Throwable {
        MethodType mt = methodType(int.class, int[].class);
        MethodHandle mh = lookup.findStatic(MethodHandleITCase.class, "sum", mt).asVarargsCollector(int[].class);
        int x = manager.setLabels(75, new Object[] {"x"});
        int y = manager.setLabels(25, new Object[] {"y"});
        int actual = (int) mh.invoke(x, y);
        Assertions.assertEquals(100, actual);
        checker.check(new Object[] {"x", "y"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleBindTo() throws Throwable {
        MethodHandle mh = lookup.findVirtual(IntToLongFunction.class, "applyAsLong", methodType(long.class, int.class));
        int x = manager.setLabels(75, new Object[] {"x"});
        IntToLongFunction val = (i) -> i;
        MethodHandle bmh = mh.bindTo(val);
        long actual = (long) bmh.invokeExact(x);
        Assertions.assertEquals(75, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    @EnabledForJreRange(min = JRE.JAVA_9)
    @SuppressWarnings("Since15")
    void methodHandlesArrayConstructor() throws Throwable {
        MethodHandle mh = MethodHandles.arrayConstructor(int[].class);
        int x = manager.setLabels(5, new Object[] {"x"});
        int[] array = (int[]) mh.invokeExact(x);
        Assertions.assertEquals(5, array.length);
        checker.check(new Object[] {"x"}, manager.getLabels(array.length));
    }

    @Test
    @EnabledForJreRange(min = JRE.JAVA_9)
    @SuppressWarnings("Since15")
    void methodHandlesArrayLength() throws Throwable {
        MethodHandle mh = MethodHandles.arrayLength(long[].class);
        int x = manager.setLabels(5, new Object[] {"x"});
        long[] array = new long[x];
        int length = (int) mh.invokeExact(array);
        Assertions.assertEquals(5, length);
        checker.check(new Object[] {"x"}, manager.getLabels(length));
    }

    @Test
    void methodHandlesArrayElementGetter() throws Throwable {
        MethodHandle mh = MethodHandles.arrayElementGetter(int[].class);
        int x = manager.setLabels(5, new Object[] {"x"});
        int[] array = new int[] {0, x};
        int element = (int) mh.invokeExact(array, 1);
        Assertions.assertEquals(5, element);
        checker.check(new Object[] {"x"}, manager.getLabels(element));
    }

    @Test
    void methodHandlesArrayElementSetter() throws Throwable {
        MethodHandle mh = MethodHandles.arrayElementSetter(int[].class);
        int x = manager.setLabels(5, new Object[] {"x"});
        int[] array = new int[2];
        mh.invokeExact(array, 1, x);
        int element = array[1];
        Assertions.assertEquals(5, element);
        checker.check(new Object[] {"x"}, manager.getLabels(element));
    }

    @Test
    void methodHandlesSpreadInvoker() throws Throwable {
        checkMethodHandlesInvoker(MethodHandles.spreadInvoker(methodType(int.class, int.class, int.class), 1), true);
    }

    @Test
    void methodHandlesExactInvoker() throws Throwable {
        checkMethodHandlesInvoker(MethodHandles.exactInvoker(methodType(int.class, int.class, int.class)), false);
    }

    @Test
    void methodHandlesInvoker() throws Throwable {
        checkMethodHandlesInvoker(MethodHandles.invoker(methodType(int.class, int.class, int.class)), false);
    }

    private void checkMethodHandlesInvoker(MethodHandle invoker, boolean spread) throws Throwable {
        MethodType mt = methodType(int.class, int.class, int.class);
        MethodHandle mh = lookup.findStatic(Integer.class, "sum", mt);
        int a = manager.setLabels(5, new Object[] {"a"});
        int b = manager.setLabels(90, new Object[] {"b"});
        int actual;
        if (spread) {
            actual = (int) invoker.invokeExact(mh, a, new Object[] {b});
        } else {
            actual = (int) invoker.invokeExact(mh, a, b);
        }
        Assertions.assertEquals(95, actual);
        checker.check(new Object[] {"a", "b"}, manager.getLabels(actual));
    }

    @Test
    @SuppressWarnings("Since15")
    @EnabledForJreRange(min = JRE.JAVA_9)
    void methodHandlesVarHandleExactInvoker() throws Throwable {
        checkVarHandleInvoker(MethodHandles.varHandleExactInvoker(VarHandle.AccessMode.GET, methodType(long.class)));
    }

    @Test
    @SuppressWarnings("Since15")
    @EnabledForJreRange(min = JRE.JAVA_9)
    void methodHandlesVarHandleInvoker() throws Throwable {
        checkVarHandleInvoker(MethodHandles.varHandleInvoker(VarHandle.AccessMode.GET, methodType(long.class)));
    }

    @SuppressWarnings("Since15")
    private void checkVarHandleInvoker(MethodHandle mh) throws Throwable {
        VarHandle vh = lookup.findStaticVarHandle(Parent.class, "j", long.class);
        Parent.j = manager.setLabels(8L, new Object[] {"j"});
        long actual = (long) mh.invokeExact(vh);
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void methodHandlesExplicitCastArguments() throws Throwable {}

    @Test
    void methodHandlesPermuteArguments() throws Throwable {}

    @Test
    void methodHandlesConstant() throws Throwable {}

    @Test
    void methodHandlesIdentity() throws Throwable {}

    @Test
    void methodHandlesZero() throws Throwable {}

    @Test
    void methodHandlesEmpty() throws Throwable {}

    @Test
    void methodHandlesInsertArguments() throws Throwable {}

    @Test
    void methodHandlesDropArguments() throws Throwable {}

    @Test
    void methodHandlesDropArgumentsToMatch() throws Throwable {}

    @Test
    void methodHandlesFilterArguments() throws Throwable {}

    @Test
    void methodHandlesCollectArguments() throws Throwable {
        MethodHandle toString = lookup.findVirtual(String.class, "toString", methodType(String.class));
        Parent p = new Parent();
        MethodHandle setX = lookup.findVirtual(Parent.class, "setX", methodType(void.class, int.class))
                .bindTo(p);
        MethodHandle collect = MethodHandles.collectArguments(toString, 1, setX);
        Assertions.assertEquals("hello", (String) collect.invokeExact("hello", 7));
        Assertions.assertEquals(7, p.getX());
    }

    @Test
    void methodHandlesFilterReturnValue() throws Throwable {}

    @Test
    void methodHandlesFoldArguments() throws Throwable {}

    @Test
    void methodHandlesGuardWithTest() throws Throwable {
        int a = manager.setLabels(10, new Object[] {"a"});
        int b = manager.setLabels(3, new Object[] {"b"});
        MethodType mt = methodType(int.class, int.class, int.class);
        MethodHandle target = lookup.findVirtual(GuardWithHelper.class, "sum", mt);
        MethodHandle fallback = lookup.findVirtual(GuardWithHelper.class, "max", mt);
        MethodHandle test = lookup.findVirtual(GuardWithHelper.class, "getFlag", methodType(boolean.class));
        MethodHandle guarded = MethodHandles.guardWithTest(test, target, fallback);
        GuardWithHelper helper = new GuardWithHelper(true);
        int actual = (int) guarded.invoke(helper, a, b);
        Assertions.assertEquals(13, actual);
        checker.check(new Object[] {"a", "b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandlesCatchException() throws Throwable {}

    @Test
    void methodHandlesThrowException() throws Throwable {}

    @Test
    void methodHandlesLoop() throws Throwable {}

    @Test
    void methodHandlesWhileLoop() throws Throwable {}

    @Test
    void methodHandlesDoWhileLoop() throws Throwable {}

    @Test
    void methodHandlesCountedLoop() throws Throwable {}

    @Test
    void methodHandlesIteratedLoop() throws Throwable {}

    @Test
    void methodHandlesTryFinally() throws Throwable {}

    @Test
    void varHandleToMethodHandle() throws Throwable {}

    static int sum(int[] values) {
        int sum = 0;
        for (int value : values) {
            sum += value;
        }
        return sum;
    }

    private static class GuardWithHelper {
        private final boolean flag;

        private GuardWithHelper(boolean flag) {
            this.flag = flag;
        }

        public int max(int a, int b) {
            return Math.max(a, b);
        }

        public int sum(int a, int b) {
            return a + b;
        }

        public boolean getFlag() {
            return flag;
        }
    }

    private static class Parent {
        public int x;
        public static long j;

        public Parent() {
            this(0);
        }

        public Parent(int x) {
            this.x = x;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getXPlus(int y) {
            return x + y;
        }
    }

    private static class Child extends Parent {
        @Override
        public int getXPlus(int y) {
            return 7;
        }

        public static Lookup lookup() {
            return MethodHandles.lookup();
        }
    }
}
