package edu.neu.ccs.prl.galette.bench;

import static edu.neu.ccs.prl.galette.bench.VarHandleHelper.*;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@FlowBench
@EnabledForJreRange(min = JRE.JAVA_9)
public class VarHandleITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    private final MethodHandles.Lookup lookup = MethodHandles.lookup();

    @ParameterizedTest(name = "byteArrayViewVarHandle(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void byteArrayViewVarHandle(boolean taintValue) {
        // Big endian, most significant byte at the smallest address
        VarHandle handle = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.BIG_ENDIAN);
        int original = !taintValue ? manager.setLabel(1, "original") : 1;
        int expected = taintValue ? manager.setLabel(4, "expected") : 4;
        byte[] array = new byte[] {0, 0, 0, 0, 0, 0, 0, (byte) original, 0, 0, 0, 2};
        handle.set(array, 4, expected);
        int actual = array[7];
        Assertions.assertEquals(expected, actual);
        Object[] actualLabels = manager.getLabels(actual);
        if (taintValue) {
            checker.check(new Object[] {"expected"}, actualLabels);
        } else {
            checker.checkEmpty(actualLabels);
        }
    }

    @ParameterizedTest(name = "byteBufferViewVarHandle(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void byteBufferViewVarHandle(boolean taintValue) {
        // Big endian, most significant byte at the smallest address
        VarHandle handle = MethodHandles.byteBufferViewVarHandle(long[].class, ByteOrder.BIG_ENDIAN);
        int original = !taintValue ? manager.setLabel(1, "original") : 1;
        int expected = taintValue ? manager.setLabel(4, "expected") : 4;
        byte[] array =
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) original, 0, 0, 0, 0, 0, 0, 0, 2};
        ByteBuffer buffer = ByteBuffer.wrap(array);
        handle.set(buffer, 8, expected);
        int actual = buffer.get(15);
        Assertions.assertEquals(expected, actual);
        Object[] actualLabels = manager.getLabels(actual);
        if (taintValue) {
            checker.check(new Object[] {"expected"}, actualLabels);
        } else {
            checker.checkEmpty(actualLabels);
        }
    }

    @ParameterizedTest(name = "unreflectVarHandle(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void unreflectVarHandle(boolean taintValue) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        short expected = HolderValueCategory.BASIC.getShort(source);
        VarHandle handle = lookup.unreflectVarHandle(HolderValueCategory.BASIC.getField(short.class));
        setInstanceFieldShort(handle, holder, AccessMode.SET, expected);
        short actual = HolderValueCategory.BASIC.getShort(holder);
        Assertions.assertEquals(expected, actual);
        checker.check(
                VariableLocation.INSTANCE_FIELD.getExpectedLabels(taintValue, short.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getBoolean(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getArguments")
    void getBoolean(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        VarHandle handle = location.getVarHandle(lookup, boolean.class);
        boolean actual;
        switch (location) {
            case INSTANCE_FIELD:
                actual = getInstanceFieldBoolean(handle, holder, mode);
                break;
            case STATIC_FIELD:
                actual = getStaticFieldBoolean(handle, mode);
                break;
            case ARRAY_ELEMENT:
                actual = getArrayElementBoolean(handle, holder, mode);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertEquals(location.getCategory().getBoolean(holder), actual);
        checker.check(location.getExpectedLabels(taintValue, boolean.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getByte(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getArguments")
    void getByte(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        VarHandle handle = location.getVarHandle(lookup, byte.class);
        byte actual;
        switch (location) {
            case INSTANCE_FIELD:
                actual = getInstanceFieldByte(handle, holder, mode);
                break;
            case STATIC_FIELD:
                actual = getStaticFieldByte(handle, mode);
                break;
            case ARRAY_ELEMENT:
                actual = getArrayElementByte(handle, holder, mode);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertEquals(location.getCategory().getByte(holder), actual);
        checker.check(location.getExpectedLabels(taintValue, byte.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getChar(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getArguments")
    void getChar(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        VarHandle handle = location.getVarHandle(lookup, char.class);
        char actual;
        switch (location) {
            case INSTANCE_FIELD:
                actual = getInstanceFieldChar(handle, holder, mode);
                break;
            case STATIC_FIELD:
                actual = getStaticFieldChar(handle, mode);
                break;
            case ARRAY_ELEMENT:
                actual = getArrayElementChar(handle, holder, mode);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertEquals(location.getCategory().getChar(holder), actual);
        checker.check(location.getExpectedLabels(taintValue, char.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getShort(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getArguments")
    void getShort(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        VarHandle handle = location.getVarHandle(lookup, short.class);
        short actual;
        switch (location) {
            case INSTANCE_FIELD:
                actual = getInstanceFieldShort(handle, holder, mode);
                break;
            case STATIC_FIELD:
                actual = getStaticFieldShort(handle, mode);
                break;
            case ARRAY_ELEMENT:
                actual = getArrayElementShort(handle, holder, mode);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertEquals(location.getCategory().getShort(holder), actual);
        checker.check(location.getExpectedLabels(taintValue, short.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getInt(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getArguments")
    void getInt(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        VarHandle handle = location.getVarHandle(lookup, int.class);
        int actual;
        switch (location) {
            case INSTANCE_FIELD:
                actual = getInstanceFieldInt(handle, holder, mode);
                break;
            case STATIC_FIELD:
                actual = getStaticFieldInt(handle, mode);
                break;
            case ARRAY_ELEMENT:
                actual = getArrayElementInt(handle, holder, mode);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertEquals(location.getCategory().getInt(holder), actual);
        checker.check(location.getExpectedLabels(taintValue, int.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getLong(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getArguments")
    void getLong(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        VarHandle handle = location.getVarHandle(lookup, long.class);
        long actual;
        switch (location) {
            case INSTANCE_FIELD:
                actual = getInstanceFieldLong(handle, holder, mode);
                break;
            case STATIC_FIELD:
                actual = getStaticFieldLong(handle, mode);
                break;
            case ARRAY_ELEMENT:
                actual = getArrayElementLong(handle, holder, mode);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertEquals(location.getCategory().getLong(holder), actual);
        checker.check(location.getExpectedLabels(taintValue, long.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getFloat(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getArguments")
    void getFloat(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        VarHandle handle = location.getVarHandle(lookup, float.class);
        float actual;
        switch (location) {
            case INSTANCE_FIELD:
                actual = getInstanceFieldFloat(handle, holder, mode);
                break;
            case STATIC_FIELD:
                actual = getStaticFieldFloat(handle, mode);
                break;
            case ARRAY_ELEMENT:
                actual = getArrayElementFloat(handle, holder, mode);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertEquals(location.getCategory().getFloat(holder), actual);
        checker.check(location.getExpectedLabels(taintValue, float.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getDouble(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getArguments")
    void getDouble(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        VarHandle handle = location.getVarHandle(lookup, double.class);
        double actual;
        switch (location) {
            case INSTANCE_FIELD:
                actual = getInstanceFieldDouble(handle, holder, mode);
                break;
            case STATIC_FIELD:
                actual = getStaticFieldDouble(handle, mode);
                break;
            case ARRAY_ELEMENT:
                actual = getArrayElementDouble(handle, holder, mode);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertEquals(location.getCategory().getDouble(holder), actual);
        checker.check(location.getExpectedLabels(taintValue, double.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getObject(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getArguments")
    void getObject(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue);
        VarHandle handle = location.getVarHandle(lookup, Object.class);
        Object actual;
        switch (location) {
            case INSTANCE_FIELD:
                actual = getInstanceFieldObject(handle, holder, mode);
                break;
            case STATIC_FIELD:
                actual = getStaticFieldObject(handle, mode);
                break;
            case ARRAY_ELEMENT:
                actual = getArrayElementObject(handle, holder, mode);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertEquals(location.getCategory().getObject(holder), actual);
        checker.check(location.getExpectedLabels(taintValue, Object.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setBoolean(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setBoolean(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        VarHandle handle = location.getVarHandle(lookup, boolean.class);
        boolean expected = location.getCategory().getBoolean(source);
        switch (location) {
            case INSTANCE_FIELD:
                setInstanceFieldBoolean(handle, holder, mode, expected);
                break;
            case STATIC_FIELD:
                setStaticFieldBoolean(handle, mode, expected);
                break;
            case ARRAY_ELEMENT:
                setArrayElementBoolean(handle, holder, mode, expected);
                break;
            default:
                throw new AssertionError();
        }
        boolean actual = location.getCategory().getBoolean(holder);
        Assertions.assertEquals(expected, actual);
        checker.check(location.getExpectedLabels(taintValue, boolean.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setByte(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setByte(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        VarHandle handle = location.getVarHandle(lookup, byte.class);
        byte expected = location.getCategory().getByte(source);
        switch (location) {
            case INSTANCE_FIELD:
                setInstanceFieldByte(handle, holder, mode, expected);
                break;
            case STATIC_FIELD:
                setStaticFieldByte(handle, mode, expected);
                break;
            case ARRAY_ELEMENT:
                setArrayElementByte(handle, holder, mode, expected);
                break;
            default:
                throw new AssertionError();
        }
        byte actual = location.getCategory().getByte(holder);
        Assertions.assertEquals(expected, actual);
        checker.check(location.getExpectedLabels(taintValue, byte.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setChar(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setChar(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        VarHandle handle = location.getVarHandle(lookup, char.class);
        char expected = location.getCategory().getChar(source);
        switch (location) {
            case INSTANCE_FIELD:
                setInstanceFieldChar(handle, holder, mode, expected);
                break;
            case STATIC_FIELD:
                setStaticFieldChar(handle, mode, expected);
                break;
            case ARRAY_ELEMENT:
                setArrayElementChar(handle, holder, mode, expected);
                break;
            default:
                throw new AssertionError();
        }
        char actual = location.getCategory().getChar(holder);
        Assertions.assertEquals(expected, actual);
        checker.check(location.getExpectedLabels(taintValue, char.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setShort(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setShort(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        VarHandle handle = location.getVarHandle(lookup, short.class);
        short expected = location.getCategory().getShort(source);
        switch (location) {
            case INSTANCE_FIELD:
                setInstanceFieldShort(handle, holder, mode, expected);
                break;
            case STATIC_FIELD:
                setStaticFieldShort(handle, mode, expected);
                break;
            case ARRAY_ELEMENT:
                setArrayElementShort(handle, holder, mode, expected);
                break;
            default:
                throw new AssertionError();
        }
        short actual = location.getCategory().getShort(holder);
        Assertions.assertEquals(expected, actual);
        checker.check(location.getExpectedLabels(taintValue, short.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setInt(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setInt(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        VarHandle handle = location.getVarHandle(lookup, int.class);
        int expected = location.getCategory().getInt(source);
        switch (location) {
            case INSTANCE_FIELD:
                setInstanceFieldInt(handle, holder, mode, expected);
                break;
            case STATIC_FIELD:
                setStaticFieldInt(handle, mode, expected);
                break;
            case ARRAY_ELEMENT:
                setArrayElementInt(handle, holder, mode, expected);
                break;
            default:
                throw new AssertionError();
        }
        int actual = location.getCategory().getInt(holder);
        Assertions.assertEquals(expected, actual);
        checker.check(location.getExpectedLabels(taintValue, int.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setLong(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setLong(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        VarHandle handle = location.getVarHandle(lookup, long.class);
        long expected = location.getCategory().getLong(source);
        switch (location) {
            case INSTANCE_FIELD:
                setInstanceFieldLong(handle, holder, mode, expected);
                break;
            case STATIC_FIELD:
                setStaticFieldLong(handle, mode, expected);
                break;
            case ARRAY_ELEMENT:
                setArrayElementLong(handle, holder, mode, expected);
                break;
            default:
                throw new AssertionError();
        }
        long actual = location.getCategory().getLong(holder);
        Assertions.assertEquals(expected, actual);
        checker.check(location.getExpectedLabels(taintValue, long.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setFloat(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setFloat(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        VarHandle handle = location.getVarHandle(lookup, float.class);
        float expected = location.getCategory().getFloat(source);
        switch (location) {
            case INSTANCE_FIELD:
                setInstanceFieldFloat(handle, holder, mode, expected);
                break;
            case STATIC_FIELD:
                setStaticFieldFloat(handle, mode, expected);
                break;
            case ARRAY_ELEMENT:
                setArrayElementFloat(handle, holder, mode, expected);
                break;
            default:
                throw new AssertionError();
        }
        float actual = location.getCategory().getFloat(holder);
        Assertions.assertEquals(expected, actual);
        checker.check(location.getExpectedLabels(taintValue, float.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setDouble(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setDouble(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        VarHandle handle = location.getVarHandle(lookup, double.class);
        double expected = location.getCategory().getDouble(source);
        switch (location) {
            case INSTANCE_FIELD:
                setInstanceFieldDouble(handle, holder, mode, expected);
                break;
            case STATIC_FIELD:
                setStaticFieldDouble(handle, mode, expected);
                break;
            case ARRAY_ELEMENT:
                setArrayElementDouble(handle, holder, mode, expected);
                break;
            default:
                throw new AssertionError();
        }
        double actual = location.getCategory().getDouble(holder);
        Assertions.assertEquals(expected, actual);
        checker.check(location.getExpectedLabels(taintValue, double.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setObject(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setObject(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        VarHandle handle = location.getVarHandle(lookup, Object.class);
        Object expected = location.getCategory().getObject(source);
        switch (location) {
            case INSTANCE_FIELD:
                setInstanceFieldObject(handle, holder, mode, expected);
                break;
            case STATIC_FIELD:
                setStaticFieldObject(handle, mode, expected);
                break;
            case ARRAY_ELEMENT:
                setArrayElementObject(handle, holder, mode, expected);
                break;
            default:
                throw new AssertionError();
        }
        Object actual = location.getCategory().getObject(holder);
        Assertions.assertEquals(expected, actual);
        checker.check(location.getExpectedLabels(taintValue, Object.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetBoolean(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetBoolean(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        boolean original = location.getCategory().getBoolean(holder);
        VarHandle handle = location.getVarHandle(lookup, boolean.class);
        boolean update = location.getCategory().getBoolean(source);
        boolean result;
        switch (location) {
            case INSTANCE_FIELD:
                result = compareAndSetInstanceFieldBoolean(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                result = compareAndSetStaticFieldBoolean(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                result = compareAndSetArrayElementBoolean(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertTrue(result);
        boolean actual = location.getCategory().getBoolean(holder);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(taintValue, boolean.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetByte(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetByte(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        byte original = location.getCategory().getByte(holder);
        VarHandle handle = location.getVarHandle(lookup, byte.class);
        byte update = location.getCategory().getByte(source);
        boolean result;
        switch (location) {
            case INSTANCE_FIELD:
                result = compareAndSetInstanceFieldByte(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                result = compareAndSetStaticFieldByte(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                result = compareAndSetArrayElementByte(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertTrue(result);
        byte actual = location.getCategory().getByte(holder);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(taintValue, byte.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetChar(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetChar(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        char original = location.getCategory().getChar(holder);
        VarHandle handle = location.getVarHandle(lookup, char.class);
        char update = location.getCategory().getChar(source);
        boolean result;
        switch (location) {
            case INSTANCE_FIELD:
                result = compareAndSetInstanceFieldChar(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                result = compareAndSetStaticFieldChar(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                result = compareAndSetArrayElementChar(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertTrue(result);
        char actual = location.getCategory().getChar(holder);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(taintValue, char.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetShort(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetShort(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        short original = location.getCategory().getShort(holder);
        VarHandle handle = location.getVarHandle(lookup, short.class);
        short update = location.getCategory().getShort(source);
        boolean result;
        switch (location) {
            case INSTANCE_FIELD:
                result = compareAndSetInstanceFieldShort(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                result = compareAndSetStaticFieldShort(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                result = compareAndSetArrayElementShort(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertTrue(result);
        short actual = location.getCategory().getShort(holder);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(taintValue, short.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetInt(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetInt(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        int original = location.getCategory().getInt(holder);
        VarHandle handle = location.getVarHandle(lookup, int.class);
        int update = location.getCategory().getInt(source);
        boolean result;
        switch (location) {
            case INSTANCE_FIELD:
                result = compareAndSetInstanceFieldInt(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                result = compareAndSetStaticFieldInt(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                result = compareAndSetArrayElementInt(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertTrue(result);
        int actual = location.getCategory().getInt(holder);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(taintValue, int.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetLong(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetLong(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        long original = location.getCategory().getLong(holder);
        VarHandle handle = location.getVarHandle(lookup, long.class);
        long update = location.getCategory().getLong(source);
        boolean result;
        switch (location) {
            case INSTANCE_FIELD:
                result = compareAndSetInstanceFieldLong(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                result = compareAndSetStaticFieldLong(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                result = compareAndSetArrayElementLong(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertTrue(result);
        long actual = location.getCategory().getLong(holder);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(taintValue, long.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetFloat(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetFloat(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        float original = location.getCategory().getFloat(holder);
        VarHandle handle = location.getVarHandle(lookup, float.class);
        float update = location.getCategory().getFloat(source);
        boolean result;
        switch (location) {
            case INSTANCE_FIELD:
                result = compareAndSetInstanceFieldFloat(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                result = compareAndSetStaticFieldFloat(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                result = compareAndSetArrayElementFloat(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertTrue(result);
        float actual = location.getCategory().getFloat(holder);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(taintValue, float.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetDouble(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetDouble(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        double original = location.getCategory().getDouble(holder);
        VarHandle handle = location.getVarHandle(lookup, double.class);
        double update = location.getCategory().getDouble(source);
        boolean result;
        switch (location) {
            case INSTANCE_FIELD:
                result = compareAndSetInstanceFieldDouble(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                result = compareAndSetStaticFieldDouble(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                result = compareAndSetArrayElementDouble(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertTrue(result);
        double actual = location.getCategory().getDouble(holder);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(taintValue, double.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetObject(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetObject(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        Object original = location.getCategory().getObject(holder);
        VarHandle handle = location.getVarHandle(lookup, Object.class);
        Object update = location.getCategory().getObject(source);
        boolean result;
        switch (location) {
            case INSTANCE_FIELD:
                result = compareAndSetInstanceFieldObject(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                result = compareAndSetStaticFieldObject(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                result = compareAndSetArrayElementObject(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        Assertions.assertTrue(result);
        Object actual = location.getCategory().getObject(holder);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(taintValue, Object.class), manager.getLabels(actual));
    }

    public static void access(AccessMode mode) {
        // TODO
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
            case COMPARE_AND_EXCHANGE_ACQUIRE:
            case COMPARE_AND_EXCHANGE_RELEASE:
                break;
            case GET_AND_SET:
            case GET_AND_SET_ACQUIRE:
            case GET_AND_SET_RELEASE:
                break;
            case GET_AND_ADD:
            case GET_AND_ADD_ACQUIRE:
            case GET_AND_ADD_RELEASE:
                break;
            case GET_AND_BITWISE_OR:
            case GET_AND_BITWISE_OR_RELEASE:
            case GET_AND_BITWISE_OR_ACQUIRE:
                break;
            case GET_AND_BITWISE_AND:
            case GET_AND_BITWISE_AND_RELEASE:
            case GET_AND_BITWISE_AND_ACQUIRE:
                break;
            case GET_AND_BITWISE_XOR:
            case GET_AND_BITWISE_XOR_RELEASE:
            case GET_AND_BITWISE_XOR_ACQUIRE:
                break;
        }
    }

    static Stream<Arguments> getArguments() {
        return BenchUtil.cartesianProduct(new Boolean[] {true, false}, VariableLocation.values(), new AccessMode[] {
            AccessMode.GET, AccessMode.GET_VOLATILE, AccessMode.GET_ACQUIRE, AccessMode.GET_OPAQUE
        });
    }

    static Stream<Arguments> setArguments() {
        return BenchUtil.cartesianProduct(new Boolean[] {true, false}, VariableLocation.values(), new AccessMode[] {
            AccessMode.SET, AccessMode.SET_VOLATILE, AccessMode.SET_RELEASE, AccessMode.SET_OPAQUE
        });
    }

    static Stream<Arguments> compareAndSetArguments() {
        return BenchUtil.cartesianProduct(new Boolean[] {true, false}, VariableLocation.values(), new AccessMode[] {
            AccessMode.COMPARE_AND_SET,
            AccessMode.WEAK_COMPARE_AND_SET_PLAIN,
            AccessMode.WEAK_COMPARE_AND_SET,
            AccessMode.WEAK_COMPARE_AND_SET_ACQUIRE,
            AccessMode.WEAK_COMPARE_AND_SET_RELEASE
        });
    }
}
