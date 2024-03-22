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

    @ParameterizedTest(name = "compareAndSwapInt(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndSwapInt(boolean compareSucceeds, boolean taintValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        int original = location.getInt(holder);
        int expected = compareSucceeds ? original : original + 50;
        int update = taintValue ? manager.setLabels(9, new Object[] {"update"}) : 9;
        boolean result = unsafe.compareAndSwapInt(
                location.getBase(unsafe, holder, int.class), location.getOffset(unsafe, int.class), expected, update);
        Assertions.assertEquals(compareSucceeds, result);
        int actual = location.getInt(holder);
        Assertions.assertEquals(compareSucceeds ? update : original, actual);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, int.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSwapLong(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndSwapLong(boolean compareSucceeds, boolean taintValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        long original = location.getLong(holder);
        long expected = compareSucceeds ? original : original + 50;
        long update = taintValue ? manager.setLabels(9, new Object[] {"update"}) : 9;
        boolean result = unsafe.compareAndSwapLong(
                location.getBase(unsafe, holder, long.class), location.getOffset(unsafe, long.class), expected, update);
        Assertions.assertEquals(compareSucceeds, result);
        long actual = location.getLong(holder);
        Assertions.assertEquals(compareSucceeds ? update : original, actual);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, long.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSwapObject(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndSwapObject(boolean compareSucceeds, boolean taintValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        Object original = location.getObject(holder);
        Object expected = compareSucceeds ? original : new Object();
        Object update = taintValue ? manager.setLabels("hello", new Object[] {"update"}) : "hello";
        boolean result = unsafe.compareAndSwapObject(
                location.getBase(unsafe, holder, Object.class),
                location.getOffset(unsafe, Object.class),
                expected,
                update);
        Assertions.assertEquals(compareSucceeds, result);
        Object actual = location.getObject(holder);
        Assertions.assertEquals(compareSucceeds ? update : original, actual);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, Object.class, manager.getLabels(actual));
    }

    @EnabledForJreRange(min = JRE.JAVA_9)
    @ParameterizedTest(name = "compareAndExchangeInt(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndExchangeInt(boolean compareSucceeds, boolean taintValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        int original = location.getInt(holder);
        int expected = compareSucceeds ? original : original + 50;
        int update = taintValue ? manager.setLabels(9, new Object[] {"update"}) : 9;
        int witness = unsafe.compareAndExchangeInt(
                location.getBase(unsafe, holder, int.class), location.getOffset(unsafe, int.class), expected, update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        checkWitnessLabels(taintValue, location, int.class, manager.getLabels(witness));
        int actual = location.getInt(holder);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, int.class, manager.getLabels(actual));
    }

    @EnabledForJreRange(min = JRE.JAVA_9)
    @ParameterizedTest(name = "compareAndExchangeLong(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndExchangeLong(boolean compareSucceeds, boolean taintValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        long original = location.getLong(holder);
        long expected = compareSucceeds ? original : original + 50;
        long update = taintValue ? manager.setLabels(9L, new Object[] {"update"}) : 9L;
        long witness = unsafe.compareAndExchangeLong(
                location.getBase(unsafe, holder, long.class), location.getOffset(unsafe, long.class), expected, update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        checkWitnessLabels(taintValue, location, long.class, manager.getLabels(witness));
        long actual = location.getLong(holder);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, long.class, manager.getLabels(actual));
    }

    @EnabledForJreRange(min = JRE.JAVA_9)
    @ParameterizedTest(name = "compareAndExchangeObject(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndExchangeObject(boolean compareSucceeds, boolean taintValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        Object original = location.getObject(holder);
        Object expected = compareSucceeds ? original : new Object();
        Object update = taintValue ? manager.setLabels("hello", new Object[] {"update"}) : "hello";
        Object witness = unsafe.compareAndExchangeObject(
                location.getBase(unsafe, holder, Object.class),
                location.getOffset(unsafe, Object.class),
                expected,
                update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        checkWitnessLabels(taintValue, location, Object.class, manager.getLabels(witness));
        Object actual = location.getObject(holder);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, Object.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getBoolean(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getBoolean(boolean taintValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        boolean expected = location.getBoolean(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, boolean.class);
        Object base = location.getBase(unsafe, holder, boolean.class);
        long offset = location.getOffset(unsafe, boolean.class);
        boolean actual = policy.getBoolean(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getByte(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getByte(boolean taintValue, UnsafeLocation location, AccessPolicy policy) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        byte expected = location.getByte(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, byte.class);
        Object base = location.getBase(unsafe, holder, byte.class);
        long offset = location.getOffset(unsafe, byte.class);
        byte actual = policy.getByte(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getChar(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getChar(boolean taintValue, UnsafeLocation location, AccessPolicy policy) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        char expected = location.getChar(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, char.class);
        Object base = location.getBase(unsafe, holder, char.class);
        long offset = location.getOffset(unsafe, char.class);
        char actual = policy.getChar(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getDouble(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getDouble(boolean taintValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        double expected = location.getDouble(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, double.class);
        Object base = location.getBase(unsafe, holder, double.class);
        long offset = location.getOffset(unsafe, double.class);
        double actual = policy.getDouble(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getFloat(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getFloat(boolean taintValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        float expected = location.getFloat(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, float.class);
        Object base = location.getBase(unsafe, holder, float.class);
        long offset = location.getOffset(unsafe, float.class);
        float actual = policy.getFloat(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getInt(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getInt(boolean taintValue, UnsafeLocation location, AccessPolicy policy) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        int expected = location.getInt(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, int.class);
        Object base = location.getBase(unsafe, holder, int.class);
        long offset = location.getOffset(unsafe, int.class);
        int actual = policy.getInt(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getLong(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getLong(boolean taintValue, UnsafeLocation location, AccessPolicy policy) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        long expected = location.getLong(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, long.class);
        Object base = location.getBase(unsafe, holder, long.class);
        long offset = location.getOffset(unsafe, long.class);
        long actual = policy.getLong(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getShort(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getShort(boolean taintValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        short expected = location.getShort(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, short.class);
        Object base = location.getBase(unsafe, holder, short.class);
        long offset = location.getOffset(unsafe, short.class);
        short actual = policy.getShort(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getObject(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getObject(boolean taintValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        Object expected = location.getObject(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, Object.class);
        Object base = location.getBase(unsafe, holder, Object.class);
        long offset = location.getOffset(unsafe, Object.class);
        Object actual = policy.getObject(base, offset, unsafe);
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "putBoolean(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putBoolean(boolean taintValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        Object[] labels = new Object[] {"set"};
        //noinspection SimplifiableConditionalExpression
        boolean expected = taintValue ? manager.setLabels(false, labels) : false;
        Object base = location.getBase(unsafe, holder, boolean.class);
        long offset = location.getOffset(unsafe, boolean.class);
        policy.putBoolean(base, offset, expected, unsafe);
        boolean actual = location.getBoolean(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putByte(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putByte(boolean taintValue, UnsafeLocation location, AccessPolicy policy) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        byte expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, byte.class);
        long offset = location.getOffset(unsafe, byte.class);
        policy.putByte(base, offset, expected, unsafe);
        byte actual = location.getByte(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putChar(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putChar(boolean taintValue, UnsafeLocation location, AccessPolicy policy) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        char expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, char.class);
        long offset = location.getOffset(unsafe, char.class);
        policy.putChar(base, offset, expected, unsafe);
        char actual = location.getChar(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putDouble(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putDouble(boolean taintValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        double expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, double.class);
        long offset = location.getOffset(unsafe, double.class);
        policy.putDouble(base, offset, expected, unsafe);
        double actual = location.getDouble(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putFloat(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putFloat(boolean taintValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        float expected = 55.0f;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, float.class);
        long offset = location.getOffset(unsafe, float.class);
        policy.putFloat(base, offset, expected, unsafe);
        float actual = location.getFloat(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putInt(taintValue={0}, location={1}, policy={2})")
    @MethodSource("extendedArguments")
    void putInt(boolean taintValue, UnsafeLocation location, AccessPolicy policy) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        int expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, int.class);
        long offset = location.getOffset(unsafe, int.class);
        policy.putInt(base, offset, expected, unsafe);
        int actual = location.getInt(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putLong(taintValue={0}, location={1}, policy={2})")
    @MethodSource("extendedArguments")
    void putLong(boolean taintValue, UnsafeLocation location, AccessPolicy policy) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        long expected = 55L;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, long.class);
        long offset = location.getOffset(unsafe, long.class);
        policy.putLong(base, offset, expected, unsafe);
        long actual = location.getLong(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putShort(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putShort(boolean taintValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        short expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, short.class);
        long offset = location.getOffset(unsafe, short.class);
        policy.putShort(base, offset, expected, unsafe);
        short actual = location.getShort(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putObject(taintValue={0}, location={1}, policy={2})")
    @MethodSource("extendedArguments")
    void putObject(boolean taintValue, UnsafeLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        Object expected = "world";
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(unsafe, holder, Object.class);
        long offset = location.getOffset(unsafe, Object.class);
        policy.putObject(base, offset, expected, unsafe);
        Object actual = location.getObject(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    private void checkWitnessLabels(boolean taintValue, UnsafeLocation location, Class<?> type, Object[] actual) {
        if (taintValue) {
            checker.checkEmpty(actual);
        } else {
            checker.check(location.getExpectedLabels(true, type), actual);
        }
    }

    private void checkCompareAndSwapLabels(
            boolean compareSucceeds, boolean taintValue, UnsafeLocation location, Class<?> type, Object[] actual) {
        if (taintValue && compareSucceeds) {
            checker.check(new Object[] {"update"}, actual);
        } else if (!taintValue && !compareSucceeds) {
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
