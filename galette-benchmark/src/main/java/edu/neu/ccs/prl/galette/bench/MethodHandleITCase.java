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
        MethodHandle mh = getMaxIntHandle();
        int a = manager.setLabel(5, "a");
        int b = manager.setLabel(90, "b");
        int actual = (int) mh.invokeExact(a, b);
        Assertions.assertEquals(90, actual);
        checker.check(new Object[] {"b"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindVirtual() throws Throwable {
        Parent p = new Parent(manager.setLabel(9, "x"));
        MethodType mt = methodType(int.class);
        MethodHandle mh = lookup.findVirtual(Parent.class, "getX", mt);
        int actual = (int) mh.invokeExact(p);
        Assertions.assertEquals(9, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindConstructor() throws Throwable {
        int x = manager.setLabel(22, "x");
        MethodType mt = methodType(void.class, int.class);
        MethodHandle mh = lookup.findConstructor(Parent.class, mt);
        Parent p = (Parent) mh.invokeExact(x);
        int actual = p.getX();
        Assertions.assertEquals(22, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindSpecial() throws Throwable {
        int x = manager.setLabel(75, "x");
        int y = manager.setLabel(25, "y");
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
        int x = manager.setLabel(75, "x");
        MethodHandle mh = lookup.findGetter(Parent.class, "x", int.class);
        Parent p = new Parent(x);
        int actual = (int) mh.invokeExact(p);
        Assertions.assertEquals(75, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindSetter() throws Throwable {
        int x = manager.setLabel(75, "x");
        MethodHandle mh = lookup.findSetter(Parent.class, "x", int.class);
        Parent p = new Parent();
        mh.invokeExact(p, x);
        int actual = p.x;
        Assertions.assertEquals(75, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindStaticGetter() throws Throwable {
        long j = manager.setLabel(8L, "j");
        MethodHandle mh = lookup.findStaticGetter(Parent.class, "j", long.class);
        Parent.j = j;
        long actual = (long) mh.invokeExact();
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void lookupFindStaticSetter() throws Throwable {
        long j = manager.setLabel(8L, "j");
        MethodHandle mh = lookup.findStaticSetter(Parent.class, "j", long.class);
        mh.invokeExact(j);
        long actual = Parent.j;
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void lookupBind() throws Throwable {
        Parent p = new Parent(manager.setLabel(9, "x"));
        MethodType mt = methodType(int.class);
        MethodHandle mh = lookup.bind(p, "getX", mt);
        int actual = (int) mh.invokeExact();
        Assertions.assertEquals(9, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflect() throws Throwable {
        int x = manager.setLabel(75, "x");
        int y = manager.setLabel(25, "y");
        Method m = Parent.class.getDeclaredMethod("getXPlus", int.class);
        MethodHandle mh = lookup.unreflect(m);
        Parent parent = new Parent(x);
        int actual = (int) mh.invokeExact(parent, y);
        Assertions.assertEquals(100, actual);
        checker.check(new Object[] {"x", "y"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflectSpecial() throws Throwable {
        int x = manager.setLabel(75, "x");
        int y = manager.setLabel(25, "y");
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
        int x = manager.setLabel(22, "x");
        Parent p = (Parent) mh.invokeExact(x);
        int actual = p.getX();
        Assertions.assertEquals(22, actual);
        checker.check(new Object[] {"x"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflectGetter() throws Throwable {
        long j = manager.setLabel(8L, "j");
        MethodHandle mh = lookup.unreflectGetter(Parent.class.getDeclaredField("j"));
        Parent.j = j;
        long actual = (long) mh.invokeExact();
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void lookupUnreflectSetter() throws Throwable {
        long j = manager.setLabel(8L, "j");
        MethodHandle mh = lookup.unreflectSetter(Parent.class.getDeclaredField("j"));
        mh.invokeExact(j);
        long actual = Parent.j;
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleInvokeExact() throws Throwable {
        MethodHandle mh = getMaxLongHandle();
        long a = manager.setLabel(5L, "a");
        long b = manager.setLabel(90L, "b");
        long actual = (long) mh.invokeExact(a, b);
        Assertions.assertEquals(90, actual);
        checker.check(new Object[] {"b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleInvoke() throws Throwable {
        MethodHandle mh = getMaxLongHandle();
        int a = manager.setLabel(5, "a");
        int b = manager.setLabel(90, "b");
        long actual = (long) mh.invoke(a, b);
        Assertions.assertEquals(90, actual);
        checker.check(new Object[] {"b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleInvokeWithArguments() throws Throwable {
        MethodHandle mh = getMaxLongHandle();
        int a = manager.setLabel(5, "a");
        int b = manager.setLabel(90, "b");
        List<Object> arguments = Arrays.asList(a, b);
        long actual = (long) mh.invokeWithArguments(arguments);
        Assertions.assertEquals(90, actual);
        checker.check(new Object[] {"b"}, manager.getLabels(actual));
    }

    private MethodHandle getMaxLongHandle() throws NoSuchMethodException, IllegalAccessException {
        MethodType mt = methodType(long.class, long.class, long.class);
        return lookup.findStatic(Math.class, "max", mt);
    }

    private MethodHandle getMaxIntHandle() throws NoSuchMethodException, IllegalAccessException {
        MethodType mt = methodType(int.class, int.class, int.class);
        return lookup.findStatic(Math.class, "max", mt);
    }

    @Test
    void methodHandleAsType() throws Throwable {
        MethodType mt = methodType(long.class, long.class, long.class);
        MethodHandle mh = getMaxLongHandle().asType(methodType(long.class, int.class, int.class));
        int a = manager.setLabel(5, "a");
        int b = manager.setLabel(90, "b");
        long actual = (long) mh.invokeExact(a, b);
        Assertions.assertEquals(90, actual);
        checker.check(new Object[] {"b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleAsSpreader() throws Throwable {
        MethodType mt = methodType(double.class, double.class, double.class);
        MethodHandle mh = lookup.findStatic(Double.class, "sum", mt).asSpreader(double[].class, 2);
        double a = manager.setLabel(5.0, "a");
        double b = manager.setLabel(90, "b");
        double actual = (double) mh.invokeExact(new double[] {a, b});
        Assertions.assertEquals(95.0, actual);
        checker.check(new Object[] {"a", "b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleAsCollector() throws Throwable {
        MethodType mt = methodType(int.class, int[].class);
        MethodHandle mh = lookup.findStatic(MethodHandleITCase.class, "sum", mt).asCollector(int[].class, 2);
        int x = manager.setLabel(75, "x");
        int y = manager.setLabel(25, "y");
        int actual = (int) mh.invokeExact(x, y);
        Assertions.assertEquals(100, actual);
        checker.check(new Object[] {"x", "y"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleAsVarargsCollector() throws Throwable {
        MethodType mt = methodType(int.class, int[].class);
        MethodHandle mh = lookup.findStatic(MethodHandleITCase.class, "sum", mt).asVarargsCollector(int[].class);
        int x = manager.setLabel(75, "x");
        int y = manager.setLabel(25, "y");
        int actual = (int) mh.invoke(x, y);
        Assertions.assertEquals(100, actual);
        checker.check(new Object[] {"x", "y"}, manager.getLabels(actual));
    }

    @Test
    void methodHandleBindTo() throws Throwable {
        MethodHandle mh = lookup.findVirtual(IntToLongFunction.class, "applyAsLong", methodType(long.class, int.class));
        int x = manager.setLabel(75, "x");
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
        int x = manager.setLabel(5, "x");
        int[] array = (int[]) mh.invokeExact(x);
        Assertions.assertEquals(5, array.length);
        checker.check(new Object[] {"x"}, manager.getLabels(array.length));
    }

    @Test
    @EnabledForJreRange(min = JRE.JAVA_9)
    @SuppressWarnings("Since15")
    void methodHandlesArrayLength() throws Throwable {
        MethodHandle mh = MethodHandles.arrayLength(long[].class);
        int x = manager.setLabel(5, "x");
        long[] array = new long[x];
        int length = (int) mh.invokeExact(array);
        Assertions.assertEquals(5, length);
        checker.check(new Object[] {"x"}, manager.getLabels(length));
    }

    @Test
    void methodHandlesArrayElementGetter() throws Throwable {
        MethodHandle mh = MethodHandles.arrayElementGetter(int[].class);
        int x = manager.setLabel(5, "x");
        int[] array = new int[] {0, x};
        int element = (int) mh.invokeExact(array, 1);
        Assertions.assertEquals(5, element);
        checker.check(new Object[] {"x"}, manager.getLabels(element));
    }

    @Test
    void methodHandlesArrayElementSetter() throws Throwable {
        MethodHandle mh = MethodHandles.arrayElementSetter(int[].class);
        int x = manager.setLabel(5, "x");
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
        MethodHandle mh = getIntegerSumHandle();
        int a = manager.setLabel(5, "a");
        int b = manager.setLabel(90, "b");
        int actual;
        if (spread) {
            actual = (int) invoker.invokeExact(mh, a, new Object[] {b});
        } else {
            actual = (int) invoker.invokeExact(mh, a, b);
        }
        Assertions.assertEquals(95, actual);
        checker.check(new Object[] {"a", "b"}, manager.getLabels(actual));
    }

    private MethodHandle getIntegerSumHandle() throws NoSuchMethodException, IllegalAccessException {
        MethodType mt = methodType(int.class, int.class, int.class);
        return lookup.findStatic(Integer.class, "sum", mt);
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
        Parent.j = manager.setLabel(8L, "j");
        long actual = (long) mh.invokeExact(vh);
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void methodHandlesExplicitCastArguments() throws Throwable {
        MethodHandle mh = getIntegerSumHandle();
        mh = MethodHandles.explicitCastArguments(mh, methodType(long.class, long.class, long.class));
        check_JJ_J(mh, 95, new String[] {"a", "b"});
    }

    @Test
    void methodHandlesPermuteArguments() throws Throwable {
        MethodHandle mh = getMaxLongHandle();
        mh = MethodHandles.permuteArguments(mh, methodType(long.class, long.class, long.class), 1, 0);
        check_JJ_J(mh, 90, new String[] {"b"});
    }

    private void check_JJ_J(MethodHandle mh, int expected, Object[] x) throws Throwable {
        long a = manager.setLabel(5, "a");
        long b = manager.setLabel(90, "b");
        long actual = (long) mh.invokeExact(a, b);
        Assertions.assertEquals(expected, actual);
        checker.check(x, manager.getLabels(actual));
    }

    @Test
    void methodHandlesConstant() throws Throwable {
        int a = manager.setLabel(5, "a");
        MethodHandle mh = MethodHandles.constant(int.class, a);
        int actual = (int) mh.invokeExact();
        Assertions.assertEquals(a, actual);
        checker.check(new Object[] {"a"}, manager.getLabels(actual));
    }

    @Test
    void methodHandlesIdentity() throws Throwable {
        int a = manager.setLabel(5, "a");
        MethodHandle mh = MethodHandles.identity(int.class);
        int actual = (int) mh.invokeExact(a);
        Assertions.assertEquals(a, actual);
        checker.check(new Object[] {"a"}, manager.getLabels(actual));
    }

    @Test
    @SuppressWarnings("Since15")
    @EnabledForJreRange(min = JRE.JAVA_9)
    void methodHandlesZero() throws Throwable {
        MethodHandle mh = MethodHandles.zero(int.class);
        int actual = (int) mh.invokeExact();
        Assertions.assertEquals(0, actual);
        checker.checkEmpty(manager.getLabels(actual));
    }

    @Test
    @SuppressWarnings("Since15")
    @EnabledForJreRange(min = JRE.JAVA_9)
    void methodHandlesEmpty() throws Throwable {
        int a = manager.setLabel(5, "a");
        MethodHandle mh = MethodHandles.empty(methodType(int.class, int.class));
        int actual = (int) mh.invokeExact(a);
        Assertions.assertEquals(0, actual);
        checker.checkEmpty(manager.getLabels(actual));
    }

    @Test
    void methodHandlesInsertArguments() throws Throwable {
        MethodHandle mh = getIntegerSumHandle();
        int a = manager.setLabel(5, "a");
        mh = MethodHandles.insertArguments(mh, 0, a);
        int b = manager.setLabel(90, "b");
        int actual = (int) mh.invokeExact(b);
        Assertions.assertEquals(95, actual);
        checker.check(new Object[] {"a", "b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandlesDropArguments() throws Throwable {
        MethodHandle mh = getIntegerSumHandle();
        mh = MethodHandles.dropArguments(mh, 1, int.class);
        int a = manager.setLabel(5, "a");
        int b = manager.setLabel(90, "b");
        int c = manager.setLabel(60, "c");
        int actual = (int) mh.invokeExact(a, c, b);
        Assertions.assertEquals(95, actual);
        checker.check(new Object[] {"a", "b"}, manager.getLabels(actual));
    }

    @Test
    @SuppressWarnings("Since15")
    @EnabledForJreRange(min = JRE.JAVA_9)
    void methodHandlesDropArgumentsToMatch() throws Throwable {
        MethodHandle mh = getIntegerSumHandle();
        MethodType bigType = mh.type().insertParameterTypes(2, long.class);
        mh = MethodHandles.dropArgumentsToMatch(mh, 0, bigType.parameterList(), 0);
        int a = manager.setLabel(5, "a");
        int b = manager.setLabel(90, "b");
        long c = manager.setLabel(60, "c");
        int actual = (int) mh.invokeExact(a, b, c);
        Assertions.assertEquals(95, actual);
        checker.check(new Object[] {"a", "b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandlesFilterArguments() throws Throwable {
        int a = manager.setLabel(5, "a");
        int b = manager.setLabel(90, "b");
        MethodHandle mh = MethodHandles.arrayElementGetter(int[].class);
        mh = mh.bindTo(new int[] {a, b});
        mh = MethodHandles.filterArguments(getIntegerSumHandle(), 0, mh, mh);
        int actual = (int) mh.invokeExact(0, 1);
        Assertions.assertEquals(95, actual);
        checker.check(new Object[] {"a", "b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandlesCollectArguments() throws Throwable {
        int a = manager.setLabel(5, "a");
        MethodHandle toString = lookup.findVirtual(String.class, "toString", methodType(String.class));
        Parent p = new Parent();
        MethodHandle setX = lookup.findVirtual(Parent.class, "setX", methodType(void.class, int.class))
                .bindTo(p);
        MethodHandle collect = MethodHandles.collectArguments(toString, 1, setX);
        Assertions.assertEquals("hello", (String) collect.invokeExact("hello", a));
        int actual = p.getX();
        Assertions.assertEquals(5, actual);
        checker.check(new Object[] {"a"}, manager.getLabels(actual));
    }

    @Test
    void methodHandlesFilterReturnValue() throws Throwable {
        int a = manager.setLabel(5, "a");
        int b = manager.setLabel(90, "b");
        MethodHandle mh = MethodHandles.arrayElementGetter(int[].class);
        mh = mh.bindTo(new int[] {a, b});
        mh = MethodHandles.filterReturnValue(getIntegerSumHandle(), mh);
        int actual = (int) mh.invokeExact(0, 1);
        Assertions.assertEquals(90, actual);
        checker.check(new Object[] {"b"}, manager.getLabels(actual));
    }

    @Test
    void methodHandlesFoldArguments() throws Throwable {
        MethodHandle mh = MethodHandles.foldArguments(getIntegerSumHandle(), MethodHandles.identity(int.class));
        int a = manager.setLabel(5, "a");
        int actual = (int) mh.invokeExact(a);
        Assertions.assertEquals(10, actual);
        checker.check(new Object[] {"a"}, manager.getLabels(actual));
    }

    @Test
    void methodHandlesGuardWithTest() throws Throwable {
        int a = manager.setLabel(10, "a");
        int b = manager.setLabel(3, "b");
        GuardWithHelper helper = new GuardWithHelper(true);
        MethodHandle test = lookup.findVirtual(GuardWithHelper.class, "getFlag", methodType(boolean.class))
                .bindTo(helper);
        MethodHandle guarded = MethodHandles.guardWithTest(test, getIntegerSumHandle(), getMaxIntHandle());
        int actual = (int) guarded.invoke(a, b);
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
