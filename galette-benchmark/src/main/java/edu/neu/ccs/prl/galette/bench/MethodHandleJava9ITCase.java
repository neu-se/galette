package edu.neu.ccs.prl.galette.bench;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

@FlowBench
@SuppressWarnings("Since15")
@EnabledForJreRange(min = JRE.JAVA_9)
public class MethodHandleJava9ITCase {
    private final MethodHandles.Lookup lookup = lookup();

    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    @Test
    void methodHandlesArrayConstructor() throws Throwable {
        MethodHandle mh = MethodHandles.arrayConstructor(int[].class);
        int x = manager.setLabel(5, "x");
        int[] array = (int[]) mh.invokeExact(x);
        Assertions.assertEquals(5, array.length);
        checker.check(new Object[] {"x"}, manager.getLabels(array.length));
    }

    @Test
    void methodHandlesArrayLength() throws Throwable {
        MethodHandle mh = MethodHandles.arrayLength(long[].class);
        int x = manager.setLabel(5, "x");
        long[] array = new long[x];
        int length = (int) mh.invokeExact(array);
        Assertions.assertEquals(5, length);
        checker.check(new Object[] {"x"}, manager.getLabels(length));
    }

    @Test
    void methodHandlesVarHandleExactInvoker() throws Throwable {
        checkVarHandleInvoker(MethodHandles.varHandleExactInvoker(VarHandle.AccessMode.GET, methodType(long.class)));
    }

    @Test
    void methodHandlesVarHandleInvoker() throws Throwable {
        checkVarHandleInvoker(MethodHandles.varHandleInvoker(VarHandle.AccessMode.GET, methodType(long.class)));
    }

    private void checkVarHandleInvoker(MethodHandle mh) throws Throwable {
        VarHandle vh = lookup.findStaticVarHandle(MethodHandleITCase.Parent.class, "j", long.class);
        MethodHandleITCase.Parent.j = manager.setLabel(8L, "j");
        long actual = (long) mh.invokeExact(vh);
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }

    @Test
    void methodHandlesZero() throws Throwable {
        MethodHandle mh = MethodHandles.zero(int.class);
        int actual = (int) mh.invokeExact();
        Assertions.assertEquals(0, actual);
        checker.checkEmpty(manager.getLabels(actual));
    }

    @Test
    void methodHandlesEmpty() throws Throwable {
        int a = manager.setLabel(5, "a");
        MethodHandle mh = MethodHandles.empty(methodType(int.class, int.class));
        int actual = (int) mh.invokeExact(a);
        Assertions.assertEquals(0, actual);
        checker.checkEmpty(manager.getLabels(actual));
    }

    @Test
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

    private MethodHandle getIntegerSumHandle() throws NoSuchMethodException, IllegalAccessException {
        MethodType mt = methodType(int.class, int.class, int.class);
        return lookup.findStatic(Integer.class, "sum", mt);
    }

    @Test
    void methodHandlesWhileLoop() throws Throwable {
        MethodHandle init = lookup.findStatic(getClass(), "loopInit", methodType(int[].class, int[].class));
        MethodHandle pred =
                lookup.findStatic(getClass(), "loopPredicate", methodType(boolean.class, int[].class, int[].class));
        MethodHandle body =
                lookup.findStatic(getClass(), "loopBody", methodType(int[].class, int[].class, int[].class));
        MethodHandle mh = MethodHandles.whileLoop(init, pred, body);
        MethodHandle accessor = MethodHandles.insertArguments(MethodHandles.arrayElementGetter(int[].class), 1, 1);
        mh = MethodHandles.filterReturnValue(mh, accessor);
        checkArraySum(mh);
    }

    @Test
    void methodHandlesDoWhileLoop() throws Throwable {
        MethodHandle init = lookup.findStatic(getClass(), "loopInit", methodType(int[].class, int[].class));
        MethodHandle pred =
                lookup.findStatic(getClass(), "loopPredicate", methodType(boolean.class, int[].class, int[].class));
        MethodHandle body =
                lookup.findStatic(getClass(), "loopBody", methodType(int[].class, int[].class, int[].class));
        MethodHandle mh = MethodHandles.doWhileLoop(init, body, pred);
        MethodHandle accessor = MethodHandles.insertArguments(MethodHandles.arrayElementGetter(int[].class), 1, 1);
        mh = MethodHandles.filterReturnValue(mh, accessor);
        checkArraySum(mh);
    }

    @Test
    void methodHandlesCountedLoop() throws Throwable {
        MethodHandle start = MethodHandles.dropArguments(MethodHandles.zero(int.class), 0, int[].class);
        MethodHandle end = MethodHandles.arrayLength(int[].class);
        MethodHandle body =
                lookup.findStatic(getClass(), "loopBody", methodType(int.class, int.class, int.class, int[].class));
        MethodHandle mh = MethodHandles.countedLoop(start, end, null, body);
        checkArraySum(mh);
    }

    @Test
    void methodHandlesIteratedLoop() throws Throwable {
        List<Integer> list = IntStream.range(0, 9).boxed().collect(Collectors.toList());
        MethodHandle body =
                lookup.findStatic(getClass(), "loopBody", methodType(int.class, int.class, int.class, int[].class));
        body = MethodHandles.dropArguments(body, 2, Iterable.class);
        MethodHandle mh = MethodHandles.iteratedLoop(null, MethodHandles.zero(int.class), body);
        checkArraySum(mh.bindTo(list));
    }

    private void checkArraySum(MethodHandle mh) throws Throwable {
        int[] a = new int[] {8, 10, 16, 32, -9, 14, 0, 1, 6};
        BenchUtil.taintWithIndices(manager, a);
        int sum = (int) mh.invokeExact(a);
        Assertions.assertEquals(78, sum);
        Object[] labels = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        checker.check(labels, manager.getLabels(sum));
    }

    private static int[] loopInit(@SuppressWarnings("unused") int[] array) {
        return new int[2];
    }

    private static int[] loopBody(int[] state, int[] array) {
        state[1] += array[state[0]++];
        return state;
    }

    private static int loopBody(int sum, int index, int[] array) {
        sum += array[index];
        return sum;
    }

    private static boolean loopPredicate(int[] state, int[] array) {
        return state[0] < array.length;
    }

    @Test
    void methodHandlesTryFinally() throws Throwable {
        int a = manager.setLabel(5, "a");
        MethodHandle handler =
                lookup.findStatic(getClass(), "cleanUp", methodType(int.class, Throwable.class, int.class, int.class));
        MethodHandle mh = MethodHandles.tryFinally(MethodHandles.identity(int.class), handler);
        int actual = (int) mh.invokeExact(a);
        Assertions.assertEquals(5, actual);
        checker.check(new Object[] {"a"}, manager.getLabels(actual));
    }

    @SuppressWarnings("unused")
    private static int cleanUp(Throwable t, int originalResult, int originalValue) {
        return originalValue;
    }

    @Test
    void varHandleToMethodHandle() throws Throwable {
        VarHandle vh = lookup.findStaticVarHandle(MethodHandleITCase.Parent.class, "j", long.class);
        MethodHandle mh = vh.toMethodHandle(VarHandle.AccessMode.GET);
        MethodHandleITCase.Parent.j = manager.setLabel(8L, "j");
        long actual = (long) mh.invokeExact();
        Assertions.assertEquals(8, actual);
        checker.check(new Object[] {"j"}, manager.getLabels(actual));
    }
}
