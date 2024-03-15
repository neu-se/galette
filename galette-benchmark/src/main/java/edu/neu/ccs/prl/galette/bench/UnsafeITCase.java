package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@FlowBench
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
        int witness = unsafe.compareAndExchangeInt(
                location.getBase(unsafe, holder, int.class), location.getOffset(unsafe, int.class), expected, update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        checkWitnessLabels(taintedValue, location, int.class, manager.getLabels(witness));
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
        long witness = unsafe.compareAndExchangeLong(
                location.getBase(unsafe, holder, long.class), location.getOffset(unsafe, long.class), expected, update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        checkWitnessLabels(taintedValue, location, long.class, manager.getLabels(witness));
        long actual = location.getLong(holder);
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
        Object witness = unsafe.compareAndExchangeObject(
                location.getBase(unsafe, holder, Object.class),
                location.getOffset(unsafe, Object.class),
                expected,
                update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        checkWitnessLabels(taintedValue, location, Object.class, manager.getLabels(witness));
        Object actual = location.getObject(holder);
        checkCompareAndSwapLabels(compareSucceeds, taintedValue, location, Object.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getBoolean(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
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
    @MethodSource("arguments")
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
    @MethodSource("arguments")
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
    @MethodSource("arguments")
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
    @MethodSource("arguments")
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
    @MethodSource("arguments")
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
    @MethodSource("arguments")
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
    @MethodSource("arguments")
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
    @MethodSource("arguments")
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

    @ParameterizedTest(name = "putBoolean(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putBoolean(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        Object[] labels = new Object[] {"set"};
        //noinspection SimplifiableConditionalExpression
        boolean expected = taintedValue ? manager.setLabels(false, labels) : false;
        Object base = location.getBase(unsafe, holder, boolean.class);
        long offset = location.getOffset(unsafe, boolean.class);
        policy.putBoolean(base, offset, expected, unsafe);
        boolean actual = location.getBoolean(holder);
        Assertions.assertEquals(expected, actual);
        if (taintedValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putByte(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putByte(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        byte expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintedValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, byte.class);
        long offset = location.getOffset(unsafe, byte.class);
        policy.putByte(base, offset, expected, unsafe);
        byte actual = location.getByte(holder);
        Assertions.assertEquals(expected, actual);
        if (taintedValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putChar(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putChar(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        char expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintedValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, char.class);
        long offset = location.getOffset(unsafe, char.class);
        policy.putChar(base, offset, expected, unsafe);
        char actual = location.getChar(holder);
        Assertions.assertEquals(expected, actual);
        if (taintedValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putDouble(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putDouble(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        double expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintedValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, double.class);
        long offset = location.getOffset(unsafe, double.class);
        policy.putDouble(base, offset, expected, unsafe);
        double actual = location.getDouble(holder);
        Assertions.assertEquals(expected, actual);
        if (taintedValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putFloat(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putFloat(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        float expected = 55.0f;
        Object[] labels = new Object[] {"set"};
        if (taintedValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, float.class);
        long offset = location.getOffset(unsafe, float.class);
        policy.putFloat(base, offset, expected, unsafe);
        float actual = location.getFloat(holder);
        Assertions.assertEquals(expected, actual);
        if (taintedValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putInt(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("extendedArguments")
    void putInt(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        int expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintedValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, int.class);
        long offset = location.getOffset(unsafe, int.class);
        policy.putInt(base, offset, expected, unsafe);
        int actual = location.getInt(holder);
        Assertions.assertEquals(expected, actual);
        if (taintedValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putLong(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("extendedArguments")
    void putLong(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        long expected = 55L;
        Object[] labels = new Object[] {"set"};
        if (taintedValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, long.class);
        long offset = location.getOffset(unsafe, long.class);
        policy.putLong(base, offset, expected, unsafe);
        long actual = location.getLong(holder);
        Assertions.assertEquals(expected, actual);
        if (taintedValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putShort(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putShort(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        short expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintedValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, short.class);
        long offset = location.getOffset(unsafe, short.class);
        policy.putShort(base, offset, expected, unsafe);
        short actual = location.getShort(holder);
        Assertions.assertEquals(expected, actual);
        if (taintedValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putObject(taintedValue={0}, location={1}, policy={2})")
    @MethodSource("extendedArguments")
    void putObject(boolean taintedValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        Object expected = "world";
        Object[] labels = new Object[] {"set"};
        if (taintedValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, Object.class);
        long offset = location.getOffset(unsafe, Object.class);
        policy.putObject(base, offset, expected, unsafe);
        Object actual = location.getObject(holder);
        Assertions.assertEquals(expected, actual);
        if (taintedValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    private void checkWitnessLabels(boolean taintedValue, UnsafeLocation location, Class<?> type, Object[] actual) {
        if (taintedValue) {
            checker.checkEmpty(actual);
        } else {
            checker.check(location.getExpectedLabels(true, type), actual);
        }
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

    static Stream<Arguments> arguments() {
        return BenchUtil.cartesianProduct(new Boolean[] {true, false}, UnsafeLocation.values(), new AccessPolicy[] {
            AccessPolicy.NORMAL, AccessPolicy.VOLATILE
        });
    }

    static Stream<Arguments> extendedArguments() {
        return BenchUtil.cartesianProduct(new Boolean[] {true, false}, UnsafeLocation.values(), AccessPolicy.values());
    }
}
