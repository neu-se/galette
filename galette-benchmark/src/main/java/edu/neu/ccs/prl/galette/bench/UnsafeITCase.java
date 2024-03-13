package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@FlowBench
@Disabled("Unimplemented")
public class UnsafeITCase {
    private final UnsafeAdapter unsafe =
            JRE.currentVersion() == JRE.JAVA_8 ? new SunUnsafeAdapter() : new JdkUnsafeAdapter();

    @SuppressWarnings("unused")
    private TagManager manager;

    @SuppressWarnings("unused")
    private FlowChecker checker;

    @ParameterizedTest(name = "compareAndSwapInt(compareSucceeds={0}, taintedValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndSwapInt(boolean compareSucceeds, boolean taintedValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        int original = location.getInt(holder);
        int expected = compareSucceeds ? original : original + 50;
        int update = taintedValue ? manager.setLabels(9, new Object[] {"update"}) : 9;
        boolean result = unsafe.compareAndSwapInt(
                location.getBase(unsafe, holder, int.class), location.getOffset(unsafe, int.class), expected, update);
        Assertions.assertEquals(compareSucceeds, result);
        int actual = location.getInt(holder);
        Assertions.assertEquals(compareSucceeds ? update : original, actual);
        checkCompareAndSwapLabels(compareSucceeds, taintedValue, location, int.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSwapLong(compareSucceeds={0}, taintedValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndSwapLong(boolean compareSucceeds, boolean taintedValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        long original = location.getLong(holder);
        long expected = compareSucceeds ? original : original + 50;
        long update = taintedValue ? manager.setLabels(9, new Object[] {"update"}) : 9;
        boolean result = unsafe.compareAndSwapLong(
                location.getBase(unsafe, holder, long.class), location.getOffset(unsafe, long.class), expected, update);
        Assertions.assertEquals(compareSucceeds, result);
        long actual = location.getLong(holder);
        Assertions.assertEquals(compareSucceeds ? update : original, actual);
        checkCompareAndSwapLabels(compareSucceeds, taintedValue, location, long.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSwapObject(compareSucceeds={0}, taintedValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndSwapObject(boolean compareSucceeds, boolean taintedValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        Object original = location.getObject(holder);
        Object expected = compareSucceeds ? original : new Object();
        Object update = taintedValue ? manager.setLabels("hello", new Object[] {"update"}) : "hello";
        boolean result = unsafe.compareAndSwapObject(
                location.getBase(unsafe, holder, Object.class),
                location.getOffset(unsafe, Object.class),
                expected,
                update);
        Assertions.assertEquals(compareSucceeds, result);
        Object actual = location.getObject(holder);
        Assertions.assertEquals(compareSucceeds ? update : original, actual);
        checkCompareAndSwapLabels(compareSucceeds, taintedValue, location, Object.class, manager.getLabels(actual));
    }

    @EnabledForJreRange(min = JRE.JAVA_9)
    @ParameterizedTest(name = "compareAndExchangeInt(compareSucceeds={0}, taintedValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndExchangeInt(boolean compareSucceeds, boolean taintedValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        int original = location.getInt(holder);
        int expected = compareSucceeds ? original : original + 50;
        int update = taintedValue ? manager.setLabels(9, new Object[] {"update"}) : 9;
        // TODO check witness' labels
        int witness = unsafe.compareAndExchangeInt(
                location.getBase(unsafe, holder, int.class), location.getOffset(unsafe, int.class), expected, update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        int actual = location.getInt(holder);
        checkCompareAndSwapLabels(compareSucceeds, taintedValue, location, int.class, manager.getLabels(actual));
    }

    @EnabledForJreRange(min = JRE.JAVA_9)
    @ParameterizedTest(name = "compareAndExchangeLong(compareSucceeds={0}, taintedValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndExchangeLong(boolean compareSucceeds, boolean taintedValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        long original = location.getLong(holder);
        long expected = compareSucceeds ? original : original + 50;
        long update = taintedValue ? manager.setLabels(9L, new Object[] {"update"}) : 9L;
        // TODO check witness' labels
        long witness = unsafe.compareAndExchangeLong(
                location.getBase(unsafe, holder, long.class), location.getOffset(unsafe, long.class), expected, update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        long actual = location.getInt(holder);
        checkCompareAndSwapLabels(compareSucceeds, taintedValue, location, long.class, manager.getLabels(actual));
    }

    @EnabledForJreRange(min = JRE.JAVA_9)
    @ParameterizedTest(name = "compareAndExchangeObject(compareSucceeds={0}, taintedValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndExchangeObject(boolean compareSucceeds, boolean taintedValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        Object original = location.getObject(holder);
        Object expected = compareSucceeds ? original : new Object();
        Object update = taintedValue ? manager.setLabels("hello", new Object[] {"update"}) : "hello";
        // TODO check witness' labels
        Object witness = unsafe.compareAndExchangeObject(
                location.getBase(unsafe, holder, Object.class),
                location.getOffset(unsafe, Object.class),
                expected,
                update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        Object actual = location.getInt(holder);
        checkCompareAndSwapLabels(compareSucceeds, taintedValue, location, Object.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getBoolean(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("getArguments")
    void getBoolean(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintedValue);
        boolean expected = location.getBoolean(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintedValue, boolean.class);
        Object base = location.getBase(unsafe, holder, boolean.class);
        long offset = location.getOffset(unsafe, boolean.class);
        boolean actual = policy.getBoolean(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getByte(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("getArguments")
    void getByte(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintedValue);
        byte expected = location.getByte(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintedValue, byte.class);
        Object base = location.getBase(unsafe, holder, byte.class);
        long offset = location.getOffset(unsafe, byte.class);
        byte actual = policy.getByte(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getChar(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("getArguments")
    void getChar(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintedValue);
        char expected = location.getChar(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintedValue, char.class);
        Object base = location.getBase(unsafe, holder, char.class);
        long offset = location.getOffset(unsafe, char.class);
        char actual = policy.getChar(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getDouble(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("getArguments")
    void getDouble(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintedValue);
        double expected = location.getDouble(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintedValue, double.class);
        Object base = location.getBase(unsafe, holder, double.class);
        long offset = location.getOffset(unsafe, double.class);
        double actual = policy.getDouble(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getFloat(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("getArguments")
    void getFloat(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintedValue);
        float expected = location.getFloat(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintedValue, float.class);
        Object base = location.getBase(unsafe, holder, float.class);
        long offset = location.getOffset(unsafe, float.class);
        float actual = policy.getFloat(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getInt(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("getArguments")
    void getInt(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintedValue);
        int expected = location.getInt(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintedValue, int.class);
        Object base = location.getBase(unsafe, holder, int.class);
        long offset = location.getOffset(unsafe, int.class);
        int actual = policy.getInt(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getLong(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("getArguments")
    void getLong(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintedValue);
        long expected = location.getLong(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintedValue, long.class);
        Object base = location.getBase(unsafe, holder, long.class);
        long offset = location.getOffset(unsafe, long.class);
        long actual = policy.getLong(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getShort(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("getArguments")
    void getShort(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintedValue);
        short expected = location.getShort(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintedValue, short.class);
        Object base = location.getBase(unsafe, holder, short.class);
        long offset = location.getOffset(unsafe, short.class);
        short actual = policy.getShort(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getObject(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("getArguments")
    void getObject(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintedValue);
        Object expected = location.getObject(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintedValue, Object.class);
        Object base = location.getBase(unsafe, holder, Object.class);
        long offset = location.getOffset(unsafe, Object.class);
        Object actual = policy.getObject(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    private void checkCompareAndSwapLabels(
            boolean compareSucceeds, boolean taintedValue, UnsafeLocation location, Class<?> type, Object[] actual) {
        if (taintedValue && compareSucceeds) {
            checker.check(new Object[] {"update"}, actual);
        } else if (!taintedValue && !compareSucceeds) {
            checker.check(location.getExpectedLabels(true, type), actual);
        } else {
            checker.checkEmpty(actual);
        }
    }

    static Stream<Arguments> compareAndSwapArguments() {
        return BenchUtil.cartesianProduct(
                new Boolean[] {true, false}, new Boolean[] {true, false}, UnsafeLocation.values());
    }

    static Stream<Arguments> getArguments() {
        return BenchUtil.cartesianProduct(new Boolean[] {true, false}, UnsafeLocation.values(), new AccessPolicy[] {
            AccessPolicy.NORMAL, AccessPolicy.VOLATILE
        });
    }
}
