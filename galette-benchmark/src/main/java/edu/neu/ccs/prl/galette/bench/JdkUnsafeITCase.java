package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@FlowBench
@EnabledForJreRange(min = JRE.JAVA_9)
public class JdkUnsafeITCase extends UnsafeBaseITCase {
    @ParameterizedTest(name = "compareAndExchangeInt(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndExchangeInt(boolean compareSucceeds, boolean taintValue, VariableLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        int original = location.getCategory().getInt(holder);
        int expected = compareSucceeds ? original : original + 50;
        int update = taintValue ? manager.setLabels(9, new Object[] {"update"}) : 9;
        int witness = getUnsafe()
                .compareAndExchangeInt(
                        location.getBase(getUnsafe(), holder, int.class),
                        location.getOffset(getUnsafe(), int.class),
                        expected,
                        update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        checkWitnessLabels(taintValue, location, int.class, manager.getLabels(witness));
        int actual = location.getCategory().getInt(holder);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, int.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndExchangeLong(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndExchangeLong(boolean compareSucceeds, boolean taintValue, VariableLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        long original = location.getCategory().getLong(holder);
        long expected = compareSucceeds ? original : original + 50;
        long update = taintValue ? manager.setLabels(9L, new Object[] {"update"}) : 9L;
        long witness = getUnsafe()
                .compareAndExchangeLong(
                        location.getBase(getUnsafe(), holder, long.class),
                        location.getOffset(getUnsafe(), long.class),
                        expected,
                        update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        checkWitnessLabels(taintValue, location, long.class, manager.getLabels(witness));
        long actual = location.getCategory().getLong(holder);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, long.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndExchangeObject(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndExchangeObject(boolean compareSucceeds, boolean taintValue, VariableLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        Object original = location.getCategory().getObject(holder);
        Object expected = compareSucceeds ? original : new Object();
        Object update = taintValue ? manager.setLabels("hello", new Object[] {"update"}) : "hello";
        Object witness = getUnsafe()
                .compareAndExchangeObject(
                        location.getBase(getUnsafe(), holder, Object.class),
                        location.getOffset(getUnsafe(), Object.class),
                        expected,
                        update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        checkWitnessLabels(taintValue, location, Object.class, manager.getLabels(witness));
        Object actual = location.getCategory().getObject(holder);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, Object.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getLongUnaligned(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getLongUnaligned(boolean taintValue) {
        byte[] array = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 12, 13, 14, 15};
        Object[] expectLabels = new Object[0];
        if (taintValue) {
            expectLabels = IntStream.range(2, 10).mapToObj(String::valueOf).toArray();
            BenchUtil.taintWithIndices(manager, array);
        }
        int offset = JdkUnsafeAdapter.UNSAFE.arrayBaseOffset(byte[].class);
        int scale = JdkUnsafeAdapter.UNSAFE.arrayIndexScale(byte[].class);
        long index = offset + scale * 2L;
        long expected = 0x0203040506070809L;
        long actual = JdkUnsafeAdapter.UNSAFE.getLongUnaligned(array, index, true);
        Assertions.assertEquals(expected, actual);
        Object[] actualLabels = manager.getLabels(actual);
        checker.check(expectLabels, actualLabels);
    }

    @ParameterizedTest(name = "getIntUnaligned(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getIntUnaligned(boolean taintValue) {
        byte[] array = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 12, 13, 14, 15};
        Object[] expectLabels = new Object[0];
        if (taintValue) {
            expectLabels = IntStream.range(4, 8).mapToObj(String::valueOf).toArray();
            BenchUtil.taintWithIndices(manager, array);
        }
        int offset = JdkUnsafeAdapter.UNSAFE.arrayBaseOffset(byte[].class);
        int scale = JdkUnsafeAdapter.UNSAFE.arrayIndexScale(byte[].class);
        long index = offset + scale * 4L;
        int expected = 0x04050607;
        int actual = JdkUnsafeAdapter.UNSAFE.getIntUnaligned(array, index, true);
        Assertions.assertEquals(expected, actual);
        Object[] actualLabels = manager.getLabels(actual);
        checker.check(expectLabels, actualLabels);
    }

    @ParameterizedTest(name = "getShortUnaligned(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getShortUnaligned(boolean taintValue) {
        byte[] array = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Object[] expectLabels = new Object[0];
        if (taintValue) {
            expectLabels = IntStream.range(5, 7).mapToObj(String::valueOf).toArray();
            BenchUtil.taintWithIndices(manager, array);
        }
        int offset = JdkUnsafeAdapter.UNSAFE.arrayBaseOffset(byte[].class);
        int scale = JdkUnsafeAdapter.UNSAFE.arrayIndexScale(byte[].class);
        long index = offset + scale * 5L;
        short expected = 0x0506;
        short actual = JdkUnsafeAdapter.UNSAFE.getShortUnaligned(array, index, true);
        Assertions.assertEquals(expected, actual);
        Object[] actualLabels = manager.getLabels(actual);
        checker.check(expectLabels, actualLabels);
    }

    @ParameterizedTest(name = "getCharUnaligned(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getCharUnaligned(boolean taintValue) {
        byte[] array = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Object[] expectLabels = new Object[0];
        if (taintValue) {
            expectLabels = IntStream.range(1, 3).mapToObj(String::valueOf).toArray();
            BenchUtil.taintWithIndices(manager, array);
        }
        int offset = JdkUnsafeAdapter.UNSAFE.arrayBaseOffset(byte[].class);
        int scale = JdkUnsafeAdapter.UNSAFE.arrayIndexScale(byte[].class);
        long index = offset + (long) scale;
        char expected = 0x0102;
        char actual = JdkUnsafeAdapter.UNSAFE.getCharUnaligned(array, index, true);
        Assertions.assertEquals(expected, actual);
        Object[] actualLabels = manager.getLabels(actual);
        checker.check(expectLabels, actualLabels);
    }

    @ParameterizedTest(name = "putLongUnaligned(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void putLongUnaligned(boolean taintValue) {
        byte[] expected = new byte[] {0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 0, 0, 0};
        byte[] array = new byte[expected.length];
        long value = 0x0102030405060708L;
        if (!taintValue) {
            // Overwrite tainted values
            BenchUtil.taintWithIndices(manager, array);
        } else {
            value = manager.setLabel(value, "label");
        }
        int offset = JdkUnsafeAdapter.UNSAFE.arrayBaseOffset(byte[].class);
        int scale = JdkUnsafeAdapter.UNSAFE.arrayIndexScale(byte[].class);
        long index = offset + scale * 3L;
        JdkUnsafeAdapter.UNSAFE.putLongUnaligned(array, index, value, true);
        Assertions.assertArrayEquals(expected, array);
        checkPutUnalignedTags(taintValue, array);
    }

    @ParameterizedTest(name = "putIntUnaligned(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void putIntUnaligned(boolean taintValue) {
        byte[] expected = new byte[] {0, 0, 1, 2, 3, 4, 0, 0, 0};
        byte[] array = new byte[expected.length];
        int value = 0x01020304;
        if (!taintValue) {
            // Overwrite tainted values
            BenchUtil.taintWithIndices(manager, array);
        } else {
            value = manager.setLabel(value, "label");
        }
        int offset = JdkUnsafeAdapter.UNSAFE.arrayBaseOffset(byte[].class);
        int scale = JdkUnsafeAdapter.UNSAFE.arrayIndexScale(byte[].class);
        long index = offset + scale * 2L;
        JdkUnsafeAdapter.UNSAFE.putIntUnaligned(array, index, value, true);
        Assertions.assertArrayEquals(expected, array);
        checkPutUnalignedTags(taintValue, array);
    }

    @ParameterizedTest(name = "putShortUnaligned(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void putShortUnaligned(boolean taintValue) {
        byte[] expected = new byte[] {0, 1, 2, 0, 0, 0};
        byte[] array = new byte[expected.length];
        short value = 0x0102;
        if (!taintValue) {
            // Overwrite tainted values
            BenchUtil.taintWithIndices(manager, array);
        } else {
            value = manager.setLabel(value, "label");
        }
        int offset = JdkUnsafeAdapter.UNSAFE.arrayBaseOffset(byte[].class);
        int scale = JdkUnsafeAdapter.UNSAFE.arrayIndexScale(byte[].class);
        long index = offset + (long) scale;
        JdkUnsafeAdapter.UNSAFE.putShortUnaligned(array, index, value, true);
        Assertions.assertArrayEquals(expected, array);
        checkPutUnalignedTags(taintValue, array);
    }

    @ParameterizedTest(name = "putCharUnaligned(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void putCharUnaligned(boolean taintValue) {
        byte[] expected = new byte[] {0, 1, 2, 0, 0, 0};
        byte[] array = new byte[expected.length];
        char value = 0x0102;
        if (!taintValue) {
            // Overwrite tainted values
            BenchUtil.taintWithIndices(manager, array);
        } else {
            value = manager.setLabel(value, "label");
        }
        int offset = JdkUnsafeAdapter.UNSAFE.arrayBaseOffset(byte[].class);
        int scale = JdkUnsafeAdapter.UNSAFE.arrayIndexScale(byte[].class);
        long index = offset + (long) scale;
        JdkUnsafeAdapter.UNSAFE.putCharUnaligned(array, index, value, true);
        Assertions.assertArrayEquals(expected, array);
        checkPutUnalignedTags(taintValue, array);
    }

    private void checkPutUnalignedTags(boolean taintValue, byte[] array) {
        for (int i = 0; i < array.length; i++) {
            byte b = array[i];
            Object[] actualLabels = manager.getLabels(b);
            Object[] expectedLabels = taintValue ? new Object[] {"label"} : new Object[] {String.valueOf(i)};
            if ((b == 0) == taintValue) {
                checker.checkEmpty(actualLabels);
            } else {
                checker.check(expectedLabels, actualLabels);
            }
        }
    }

    @Override
    UnsafeAdapter getUnsafe() {
        return new JdkUnsafeAdapter();
    }
}
