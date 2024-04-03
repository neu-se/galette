package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@FlowBench
public abstract class UnsafeBaseITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    @ParameterizedTest(name = "compareAndSwapInt(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndSwapInt(boolean compareSucceeds, boolean taintValue, VariableLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        int original = location.getCategory().getInt(holder);
        int expected = compareSucceeds ? original : original + 50;
        int update = taintValue ? manager.setLabels(9, new Object[] {"update"}) : 9;
        boolean result = getUnsafe()
                .compareAndSwapInt(
                        location.getBase(getUnsafe(), holder, int.class),
                        location.getOffset(getUnsafe(), int.class),
                        expected,
                        update);
        Assertions.assertEquals(compareSucceeds, result);
        int actual = location.getCategory().getInt(holder);
        Assertions.assertEquals(compareSucceeds ? update : original, actual);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, int.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSwapLong(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndSwapLong(boolean compareSucceeds, boolean taintValue, VariableLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        long original = location.getCategory().getLong(holder);
        long expected = compareSucceeds ? original : original + 50;
        long update = taintValue ? manager.setLabels(9, new Object[] {"update"}) : 9;
        boolean result = getUnsafe()
                .compareAndSwapLong(
                        location.getBase(getUnsafe(), holder, long.class),
                        location.getOffset(getUnsafe(), long.class),
                        expected,
                        update);
        Assertions.assertEquals(compareSucceeds, result);
        long actual = location.getCategory().getLong(holder);
        Assertions.assertEquals(compareSucceeds ? update : original, actual);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, long.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSwapObject(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndSwapObject(boolean compareSucceeds, boolean taintValue, VariableLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        Object original = location.getCategory().getObject(holder);
        Object expected = compareSucceeds ? original : new Object();
        Object update = taintValue ? manager.setLabels("hello", new Object[] {"update"}) : "hello";
        boolean result = getUnsafe()
                .compareAndSwapObject(
                        location.getBase(getUnsafe(), holder, Object.class),
                        location.getOffset(getUnsafe(), Object.class),
                        expected,
                        update);
        Assertions.assertEquals(compareSucceeds, result);
        Object actual = location.getCategory().getObject(holder);
        Assertions.assertEquals(compareSucceeds ? update : original, actual);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, Object.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getBoolean(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getBoolean(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        boolean expected = location.getCategory().getBoolean(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, boolean.class);
        Object base = location.getBase(getUnsafe(), holder, boolean.class);
        long offset = location.getOffset(getUnsafe(), boolean.class);
        boolean actual = policy.getBoolean(base, offset, getUnsafe());
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getByte(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getByte(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        byte expected = location.getCategory().getByte(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, byte.class);
        Object base = location.getBase(getUnsafe(), holder, byte.class);
        long offset = location.getOffset(getUnsafe(), byte.class);
        byte actual = policy.getByte(base, offset, getUnsafe());
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getChar(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getChar(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        char expected = location.getCategory().getChar(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, char.class);
        Object base = location.getBase(getUnsafe(), holder, char.class);
        long offset = location.getOffset(getUnsafe(), char.class);
        char actual = policy.getChar(base, offset, getUnsafe());
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getDouble(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getDouble(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        double expected = location.getCategory().getDouble(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, double.class);
        Object base = location.getBase(getUnsafe(), holder, double.class);
        long offset = location.getOffset(getUnsafe(), double.class);
        double actual = policy.getDouble(base, offset, getUnsafe());
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getFloat(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getFloat(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        float expected = location.getCategory().getFloat(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, float.class);
        Object base = location.getBase(getUnsafe(), holder, float.class);
        long offset = location.getOffset(getUnsafe(), float.class);
        float actual = policy.getFloat(base, offset, getUnsafe());
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getInt(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getInt(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        int expected = location.getCategory().getInt(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, int.class);
        Object base = location.getBase(getUnsafe(), holder, int.class);
        long offset = location.getOffset(getUnsafe(), int.class);
        int actual = policy.getInt(base, offset, getUnsafe());
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getLong(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getLong(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        long expected = location.getCategory().getLong(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, long.class);
        Object base = location.getBase(getUnsafe(), holder, long.class);
        long offset = location.getOffset(getUnsafe(), long.class);
        long actual = policy.getLong(base, offset, getUnsafe());
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getShort(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getShort(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        short expected = location.getCategory().getShort(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, short.class);
        Object base = location.getBase(getUnsafe(), holder, short.class);
        long offset = location.getOffset(getUnsafe(), short.class);
        short actual = policy.getShort(base, offset, getUnsafe());
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "getObject(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void getObject(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        Object expected = location.getCategory().getObject(holder);
        Object[] expectedLabels = location.getExpectedLabels(taintValue, Object.class);
        Object base = location.getBase(getUnsafe(), holder, Object.class);
        long offset = location.getOffset(getUnsafe(), Object.class);
        Object actual = policy.getObject(base, offset, getUnsafe());
        Object[] actualLabels = manager.getLabels(actual);
        Assertions.assertEquals(expected, actual);
        checker.check(expectedLabels, actualLabels);
    }

    @ParameterizedTest(name = "putBoolean(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putBoolean(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        Object[] labels = new Object[] {"set"};
        //noinspection SimplifiableConditionalExpression
        boolean expected = taintValue ? manager.setLabels(false, labels) : false;
        Object base = location.getBase(getUnsafe(), holder, boolean.class);
        long offset = location.getOffset(getUnsafe(), boolean.class);
        policy.putBoolean(base, offset, expected, getUnsafe());
        boolean actual = location.getCategory().getBoolean(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putByte(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putByte(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        byte expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(getUnsafe(), holder, byte.class);
        long offset = location.getOffset(getUnsafe(), byte.class);
        policy.putByte(base, offset, expected, getUnsafe());
        byte actual = location.getCategory().getByte(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putChar(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putChar(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        char expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(getUnsafe(), holder, char.class);
        long offset = location.getOffset(getUnsafe(), char.class);
        policy.putChar(base, offset, expected, getUnsafe());
        char actual = location.getCategory().getChar(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putDouble(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putDouble(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        double expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(getUnsafe(), holder, double.class);
        long offset = location.getOffset(getUnsafe(), double.class);
        policy.putDouble(base, offset, expected, getUnsafe());
        double actual = location.getCategory().getDouble(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putFloat(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putFloat(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        float expected = 55.0f;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(getUnsafe(), holder, float.class);
        long offset = location.getOffset(getUnsafe(), float.class);
        policy.putFloat(base, offset, expected, getUnsafe());
        float actual = location.getCategory().getFloat(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putInt(taintValue={0}, location={1}, policy={2})")
    @MethodSource("extendedArguments")
    void putInt(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        int expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(getUnsafe(), holder, int.class);
        long offset = location.getOffset(getUnsafe(), int.class);
        policy.putInt(base, offset, expected, getUnsafe());
        int actual = location.getCategory().getInt(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putLong(taintValue={0}, location={1}, policy={2})")
    @MethodSource("extendedArguments")
    void putLong(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        long expected = 55L;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(getUnsafe(), holder, long.class);
        long offset = location.getOffset(getUnsafe(), long.class);
        policy.putLong(base, offset, expected, getUnsafe());
        long actual = location.getCategory().getLong(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putShort(taintValue={0}, location={1}, policy={2})")
    @MethodSource("arguments")
    void putShort(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        short expected = 55;
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(getUnsafe(), holder, short.class);
        long offset = location.getOffset(getUnsafe(), short.class);
        policy.putShort(base, offset, expected, getUnsafe());
        short actual = location.getCategory().getShort(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "putObject(taintValue={0}, location={1}, policy={2})")
    @MethodSource("extendedArguments")
    void putObject(boolean taintValue, VariableLocation location, AccessPolicy policy)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        Object expected = "world";
        Object[] labels = new Object[] {"set"};
        if (taintValue) {
            expected = manager.setLabels(expected, labels);
        }
        Object base = location.getBase(getUnsafe(), holder, Object.class);
        long offset = location.getOffset(getUnsafe(), Object.class);
        policy.putObject(base, offset, expected, getUnsafe());
        Object actual = location.getCategory().getObject(holder);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(labels, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    void checkWitnessLabels(boolean taintValue, VariableLocation location, Class<?> type, Object[] actual) {
        if (taintValue) {
            checker.checkEmpty(actual);
        } else {
            checker.check(location.getExpectedLabels(true, type), actual);
        }
    }

    void checkCompareAndSwapLabels(
            boolean compareSucceeds, boolean taintValue, VariableLocation location, Class<?> type, Object[] actual) {
        if (taintValue && compareSucceeds) {
            checker.check(new Object[] {"update"}, actual);
        } else if (!taintValue && !compareSucceeds) {
            checker.check(location.getExpectedLabels(true, type), actual);
        } else {
            checker.checkEmpty(actual);
        }
    }

    abstract UnsafeAdapter getUnsafe();

    static Stream<Arguments> compareAndSwapArguments() {
        return BenchUtil.cartesianProduct(
                new Boolean[] {true, false}, new Boolean[] {true, false}, VariableLocation.values());
    }

    static Stream<Arguments> arguments() {
        return BenchUtil.cartesianProduct(new Boolean[] {true, false}, VariableLocation.values(), new AccessPolicy[] {
            AccessPolicy.NORMAL, AccessPolicy.VOLATILE
        });
    }

    static Stream<Arguments> extendedArguments() {
        return BenchUtil.cartesianProduct(
                new Boolean[] {true, false}, VariableLocation.values(), AccessPolicy.values());
    }
}
