package edu.neu.ccs.prl.galette.bench;

import static edu.neu.ccs.prl.galette.bench.VarHandleHelper.*;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
        int expected = taintValue ? manager.setLabel(4, "label") : 4;
        byte[] array = new byte[] {0, 0, 0, 0, 0, 0, 0, (byte) original, 0, 0, 0, 2};
        handle.set(array, 4, expected);
        int actual = array[7];
        Assertions.assertEquals(expected, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "byteBufferViewVarHandle(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void byteBufferViewVarHandle(boolean taintValue) {
        // Big endian, most significant byte at the smallest address
        VarHandle handle = MethodHandles.byteBufferViewVarHandle(long[].class, ByteOrder.BIG_ENDIAN);
        int original = !taintValue ? manager.setLabel(1, "original") : 1;
        int expected = taintValue ? manager.setLabel(4, "label") : 4;
        byte[] array =
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) original, 0, 0, 0, 0, 0, 0, 0, 2};
        ByteBuffer buffer = ByteBuffer.wrap(array);
        handle.set(buffer, 8, expected);
        int actual = buffer.get(15);
        Assertions.assertEquals(expected, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "unreflectVarHandle(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void unreflectVarHandle(boolean taintValue) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        short expected = taintValue ? manager.setLabel((short) 4, "label") : 4;
        VarHandle handle = lookup.unreflectVarHandle(HolderValueCategory.BASIC.getField(short.class));
        setInstanceFieldShort(handle, holder, AccessMode.SET, expected);
        short actual = HolderValueCategory.BASIC.getShort(holder);
        Assertions.assertEquals(expected, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
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
        VarHandle handle = location.getVarHandle(lookup, boolean.class);
        //noinspection SimplifiableConditionalExpression
        boolean expected = taintValue ? manager.setLabel(true, "label") : true;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setByte(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setByte(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        VarHandle handle = location.getVarHandle(lookup, byte.class);
        byte expected = taintValue ? manager.setLabel((byte) 4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setChar(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setChar(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        VarHandle handle = location.getVarHandle(lookup, char.class);
        char expected = taintValue ? manager.setLabel((char) 4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setShort(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setShort(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        VarHandle handle = location.getVarHandle(lookup, short.class);
        short expected = taintValue ? manager.setLabel((short) 4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setInt(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setInt(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        VarHandle handle = location.getVarHandle(lookup, int.class);
        int expected = taintValue ? manager.setLabel(4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setLong(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setLong(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        VarHandle handle = location.getVarHandle(lookup, long.class);
        long expected = taintValue ? manager.setLabel(4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setFloat(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setFloat(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        VarHandle handle = location.getVarHandle(lookup, float.class);
        float expected = taintValue ? manager.setLabel(4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setDouble(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setDouble(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        VarHandle handle = location.getVarHandle(lookup, double.class);
        double expected = taintValue ? manager.setLabel(4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "setObject(taintValue={0}, location={1}, mode={2})")
    @MethodSource("setArguments")
    void setObject(boolean taintValue, VariableLocation location, AccessMode mode) throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        VarHandle handle = location.getVarHandle(lookup, Object.class);
        Object expected = taintValue ? manager.setLabel(new Object(), "label") : new Object();
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetBoolean(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetBoolean(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        boolean original = location.getCategory().getBoolean(holder);
        VarHandle handle = location.getVarHandle(lookup, boolean.class);
        //noinspection SimplifiableConditionalExpression
        boolean update = taintValue ? manager.setLabel(true, "label") : true;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetByte(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetByte(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        byte original = location.getCategory().getByte(holder);
        VarHandle handle = location.getVarHandle(lookup, byte.class);
        byte update = taintValue ? manager.setLabel((byte) 4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetChar(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetChar(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        char original = location.getCategory().getChar(holder);
        VarHandle handle = location.getVarHandle(lookup, char.class);
        char update = taintValue ? manager.setLabel((char) 4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetShort(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetShort(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        short original = location.getCategory().getShort(holder);
        VarHandle handle = location.getVarHandle(lookup, short.class);
        short update = taintValue ? manager.setLabel((short) 4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetInt(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetInt(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        int original = location.getCategory().getInt(holder);
        VarHandle handle = location.getVarHandle(lookup, int.class);
        int update = taintValue ? manager.setLabel(4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetLong(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetLong(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        long original = location.getCategory().getLong(holder);
        VarHandle handle = location.getVarHandle(lookup, long.class);
        long update = taintValue ? manager.setLabel(4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetFloat(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetFloat(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        float original = location.getCategory().getFloat(holder);
        VarHandle handle = location.getVarHandle(lookup, float.class);
        float update = taintValue ? manager.setLabel(4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetDouble(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetDouble(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        double original = location.getCategory().getDouble(holder);
        VarHandle handle = location.getVarHandle(lookup, double.class);
        double update = taintValue ? manager.setLabel(4, "label") : 4;
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndSetObject(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndSetArguments")
    void compareAndSetObject(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Object original = location.getCategory().getObject(holder);
        VarHandle handle = location.getVarHandle(lookup, Object.class);
        Object update = taintValue ? manager.setLabel(new Object(), "label") : new Object();
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
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndSetBoolean(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getAndSetArguments")
    void getAndSetBoolean(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        boolean original = location.getCategory().getBoolean(holder);
        VarHandle handle = location.getVarHandle(lookup, boolean.class);
        //noinspection SimplifiableConditionalExpression
        boolean update = taintValue ? manager.setLabel(true, "label") : true;
        boolean witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndSetInstanceFieldBoolean(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndSetStaticFieldBoolean(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndSetArrayElementBoolean(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        boolean actual = location.getCategory().getBoolean(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, boolean.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndSetByte(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getAndSetArguments")
    void getAndSetByte(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        byte original = location.getCategory().getByte(holder);
        VarHandle handle = location.getVarHandle(lookup, byte.class);
        byte update = taintValue ? manager.setLabel((byte) 4, "label") : 4;
        byte witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndSetInstanceFieldByte(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndSetStaticFieldByte(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndSetArrayElementByte(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        byte actual = location.getCategory().getByte(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, byte.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndSetChar(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getAndSetArguments")
    void getAndSetChar(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        char original = location.getCategory().getChar(holder);
        VarHandle handle = location.getVarHandle(lookup, char.class);
        char update = taintValue ? manager.setLabel((char) 4, "label") : 4;
        char witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndSetInstanceFieldChar(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndSetStaticFieldChar(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndSetArrayElementChar(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        char actual = location.getCategory().getChar(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, char.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndSetShort(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getAndSetArguments")
    void getAndSetShort(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        short original = location.getCategory().getShort(holder);
        VarHandle handle = location.getVarHandle(lookup, short.class);
        short update = taintValue ? manager.setLabel((short) 4, "label") : 4;
        short witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndSetInstanceFieldShort(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndSetStaticFieldShort(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndSetArrayElementShort(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        short actual = location.getCategory().getShort(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, short.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndSetInt(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getAndSetArguments")
    void getAndSetInt(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        int original = location.getCategory().getInt(holder);
        VarHandle handle = location.getVarHandle(lookup, int.class);
        int update = taintValue ? manager.setLabel(4, "label") : 4;
        int witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndSetInstanceFieldInt(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndSetStaticFieldInt(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndSetArrayElementInt(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        int actual = location.getCategory().getInt(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, int.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndSetLong(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getAndSetArguments")
    void getAndSetLong(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        long original = location.getCategory().getLong(holder);
        VarHandle handle = location.getVarHandle(lookup, long.class);
        long update = taintValue ? manager.setLabel((long) 4, "label") : 4;
        long witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndSetInstanceFieldLong(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndSetStaticFieldLong(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndSetArrayElementLong(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        long actual = location.getCategory().getLong(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, long.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndSetFloat(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getAndSetArguments")
    void getAndSetFloat(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        float original = location.getCategory().getFloat(holder);
        VarHandle handle = location.getVarHandle(lookup, float.class);
        float update = taintValue ? manager.setLabel((float) 4, "label") : 4;
        float witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndSetInstanceFieldFloat(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndSetStaticFieldFloat(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndSetArrayElementFloat(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        float actual = location.getCategory().getFloat(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, float.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndSetDouble(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getAndSetArguments")
    void getAndSetDouble(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        double original = location.getCategory().getDouble(holder);
        VarHandle handle = location.getVarHandle(lookup, double.class);
        double update = taintValue ? manager.setLabel((double) 4, "label") : 4;
        double witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndSetInstanceFieldDouble(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndSetStaticFieldDouble(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndSetArrayElementDouble(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        double actual = location.getCategory().getDouble(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, double.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndSetObject(taintValue={0}, location={1}, mode={2})")
    @MethodSource("getAndSetArguments")
    void getAndSetObject(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Object original = location.getCategory().getObject(holder);
        VarHandle handle = location.getVarHandle(lookup, Object.class);
        Object update = taintValue ? manager.setLabel(new Object(), "label") : new Object();
        Object witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndSetInstanceFieldObject(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndSetStaticFieldObject(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndSetArrayElementObject(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        Object actual = location.getCategory().getObject(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, Object.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndExchangeBoolean(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndExchangeArguments")
    void compareAndExchangeBoolean(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        boolean original = location.getCategory().getBoolean(holder);
        VarHandle handle = location.getVarHandle(lookup, boolean.class);
        //noinspection SimplifiableConditionalExpression
        boolean update = taintValue ? manager.setLabel(true, "label") : true;
        boolean witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = compareAndExchangeInstanceFieldBoolean(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                witness = compareAndExchangeStaticFieldBoolean(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                witness = compareAndExchangeArrayElementBoolean(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        boolean actual = location.getCategory().getBoolean(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, boolean.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndExchangeByte(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndExchangeArguments")
    void compareAndExchangeByte(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        byte original = location.getCategory().getByte(holder);
        VarHandle handle = location.getVarHandle(lookup, byte.class);
        byte update = taintValue ? manager.setLabel((byte) 4, "label") : 4;
        byte witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = compareAndExchangeInstanceFieldByte(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                witness = compareAndExchangeStaticFieldByte(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                witness = compareAndExchangeArrayElementByte(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        byte actual = location.getCategory().getByte(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, byte.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndExchangeChar(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndExchangeArguments")
    void compareAndExchangeChar(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        char original = location.getCategory().getChar(holder);
        VarHandle handle = location.getVarHandle(lookup, char.class);
        char update = taintValue ? manager.setLabel((char) 4, "label") : 4;
        char witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = compareAndExchangeInstanceFieldChar(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                witness = compareAndExchangeStaticFieldChar(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                witness = compareAndExchangeArrayElementChar(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        char actual = location.getCategory().getChar(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, char.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndExchangeShort(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndExchangeArguments")
    void compareAndExchangeShort(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        short original = location.getCategory().getShort(holder);
        VarHandle handle = location.getVarHandle(lookup, short.class);
        short update = taintValue ? manager.setLabel((short) 4, "label") : 4;
        short witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = compareAndExchangeInstanceFieldShort(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                witness = compareAndExchangeStaticFieldShort(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                witness = compareAndExchangeArrayElementShort(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        short actual = location.getCategory().getShort(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, short.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndExchangeInt(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndExchangeArguments")
    void compareAndExchangeInt(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        int original = location.getCategory().getInt(holder);
        VarHandle handle = location.getVarHandle(lookup, int.class);
        int update = taintValue ? manager.setLabel(4, "label") : 4;
        int witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = compareAndExchangeInstanceFieldInt(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                witness = compareAndExchangeStaticFieldInt(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                witness = compareAndExchangeArrayElementInt(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        int actual = location.getCategory().getInt(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, int.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndExchangeLong(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndExchangeArguments")
    void compareAndExchangeLong(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        long original = location.getCategory().getLong(holder);
        VarHandle handle = location.getVarHandle(lookup, long.class);
        long update = taintValue ? manager.setLabel((long) 4, "label") : 4;
        long witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = compareAndExchangeInstanceFieldLong(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                witness = compareAndExchangeStaticFieldLong(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                witness = compareAndExchangeArrayElementLong(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        long actual = location.getCategory().getLong(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, long.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndExchangeFloat(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndExchangeArguments")
    void compareAndExchangeFloat(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        float original = location.getCategory().getFloat(holder);
        VarHandle handle = location.getVarHandle(lookup, float.class);
        float update = taintValue ? manager.setLabel((float) 4, "label") : 4;
        float witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = compareAndExchangeInstanceFieldFloat(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                witness = compareAndExchangeStaticFieldFloat(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                witness = compareAndExchangeArrayElementFloat(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        float actual = location.getCategory().getFloat(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, float.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndExchangeDouble(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndExchangeArguments")
    void compareAndExchangeDouble(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        double original = location.getCategory().getDouble(holder);
        VarHandle handle = location.getVarHandle(lookup, double.class);
        double update = taintValue ? manager.setLabel((double) 4, "label") : 4;
        double witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = compareAndExchangeInstanceFieldDouble(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                witness = compareAndExchangeStaticFieldDouble(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                witness = compareAndExchangeArrayElementDouble(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        double actual = location.getCategory().getDouble(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, double.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndExchangeObject(taintValue={0}, location={1}, mode={2})")
    @MethodSource("compareAndExchangeArguments")
    void compareAndExchangeObject(boolean taintValue, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Object original = location.getCategory().getObject(holder);
        VarHandle handle = location.getVarHandle(lookup, Object.class);
        Object update = taintValue ? manager.setLabel(new Object(), "label") : new Object();
        Object witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = compareAndExchangeInstanceFieldObject(handle, holder, mode, original, update);
                break;
            case STATIC_FIELD:
                witness = compareAndExchangeStaticFieldObject(handle, mode, original, update);
                break;
            case ARRAY_ELEMENT:
                witness = compareAndExchangeArrayElementObject(handle, holder, mode, original, update);
                break;
            default:
                throw new AssertionError();
        }
        Object actual = location.getCategory().getObject(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update, actual);
        checker.check(location.getExpectedLabels(!taintValue, Object.class), manager.getLabels(witness));
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndAddByte(taintValue={0}, taintUpdate={1}, location={2}, mode={3})")
    @MethodSource("getAndAddArguments")
    void getAndAddByte(boolean taintValue, boolean taintUpdate, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        byte original = location.getCategory().getByte(holder);
        VarHandle handle = location.getVarHandle(lookup, byte.class);
        byte update = taintUpdate ? manager.setLabel((byte) 4, "label") : 4;
        byte witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndAddInstanceFieldByte(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndAddStaticFieldByte(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndAddArrayElementByte(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        byte actual = location.getCategory().getByte(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update + original, actual);
        checker.check(location.getExpectedLabels(taintValue, byte.class), manager.getLabels(witness));
        checker.check(getAndApplyLabels(taintValue, taintUpdate, location, byte.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndAddChar(taintValue={0}, taintUpdate={1}, location={2}, mode={3})")
    @MethodSource("getAndAddArguments")
    void getAndAddChar(boolean taintValue, boolean taintUpdate, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        char original = location.getCategory().getChar(holder);
        VarHandle handle = location.getVarHandle(lookup, char.class);
        char update = taintUpdate ? manager.setLabel((char) 4, "label") : 4;
        char witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndAddInstanceFieldChar(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndAddStaticFieldChar(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndAddArrayElementChar(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        char actual = location.getCategory().getChar(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update + original, actual);
        checker.check(location.getExpectedLabels(taintValue, char.class), manager.getLabels(witness));
        checker.check(getAndApplyLabels(taintValue, taintUpdate, location, char.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndAddShort(taintValue={0}, taintUpdate={1}, location={2}, mode={3})")
    @MethodSource("getAndAddArguments")
    void getAndAddShort(boolean taintValue, boolean taintUpdate, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        short original = location.getCategory().getShort(holder);
        VarHandle handle = location.getVarHandle(lookup, short.class);
        short update = taintUpdate ? manager.setLabel((short) 4, "label") : 4;
        short witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndAddInstanceFieldShort(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndAddStaticFieldShort(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndAddArrayElementShort(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        short actual = location.getCategory().getShort(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update + original, actual);
        checker.check(location.getExpectedLabels(taintValue, short.class), manager.getLabels(witness));
        checker.check(getAndApplyLabels(taintValue, taintUpdate, location, short.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndAddInt(taintValue={0}, taintUpdate={1}, location={2}, mode={3})")
    @MethodSource("getAndAddArguments")
    void getAndAddInt(boolean taintValue, boolean taintUpdate, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        int original = location.getCategory().getInt(holder);
        VarHandle handle = location.getVarHandle(lookup, int.class);
        int update = taintUpdate ? manager.setLabel((int) 4, "label") : 4;
        int witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndAddInstanceFieldInt(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndAddStaticFieldInt(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndAddArrayElementInt(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        int actual = location.getCategory().getInt(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update + original, actual);
        checker.check(location.getExpectedLabels(taintValue, int.class), manager.getLabels(witness));
        checker.check(getAndApplyLabels(taintValue, taintUpdate, location, int.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndAddLong(taintValue={0}, taintUpdate={1}, location={2}, mode={3})")
    @MethodSource("getAndAddArguments")
    void getAndAddLong(boolean taintValue, boolean taintUpdate, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        long original = location.getCategory().getLong(holder);
        VarHandle handle = location.getVarHandle(lookup, long.class);
        long update = taintUpdate ? manager.setLabel((long) 4, "label") : 4;
        long witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndAddInstanceFieldLong(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndAddStaticFieldLong(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndAddArrayElementLong(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        long actual = location.getCategory().getLong(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update + original, actual);
        checker.check(location.getExpectedLabels(taintValue, long.class), manager.getLabels(witness));
        checker.check(getAndApplyLabels(taintValue, taintUpdate, location, long.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndAddFloat(taintValue={0}, taintUpdate={1}, location={2}, mode={3})")
    @MethodSource("getAndAddArguments")
    void getAndAddFloat(boolean taintValue, boolean taintUpdate, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        float original = location.getCategory().getFloat(holder);
        VarHandle handle = location.getVarHandle(lookup, float.class);
        float update = taintUpdate ? manager.setLabel((float) 4, "label") : 4;
        float witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndAddInstanceFieldFloat(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndAddStaticFieldFloat(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndAddArrayElementFloat(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        float actual = location.getCategory().getFloat(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update + original, actual);
        checker.check(location.getExpectedLabels(taintValue, float.class), manager.getLabels(witness));
        checker.check(getAndApplyLabels(taintValue, taintUpdate, location, float.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndAddDouble(taintValue={0}, taintUpdate={1}, location={2}, mode={3})")
    @MethodSource("getAndAddArguments")
    void getAndAddDouble(boolean taintValue, boolean taintUpdate, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        double original = location.getCategory().getDouble(holder);
        VarHandle handle = location.getVarHandle(lookup, double.class);
        double update = taintUpdate ? manager.setLabel((double) 4, "label") : 4;
        double witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndAddInstanceFieldDouble(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndAddStaticFieldDouble(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndAddArrayElementDouble(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        double actual = location.getCategory().getDouble(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(update + original, actual);
        checker.check(location.getExpectedLabels(taintValue, double.class), manager.getLabels(witness));
        checker.check(getAndApplyLabels(taintValue, taintUpdate, location, double.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndBitwiseBoolean(taintValue={0}, taintUpdate={1}, location={2}, mode={3})")
    @MethodSource("getAndBitwiseArguments")
    void getAndBitwiseBoolean(boolean taintValue, boolean taintUpdate, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        boolean original = location.getCategory().getBoolean(holder);
        VarHandle handle = location.getVarHandle(lookup, boolean.class);
        //noinspection SimplifiableConditionalExpression
        boolean update = taintUpdate ? manager.setLabel(true, "label") : true;
        boolean witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndBitwiseInstanceFieldBoolean(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndBitwiseStaticFieldBoolean(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndBitwiseArrayElementBoolean(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        boolean actual = location.getCategory().getBoolean(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(VarHandleHelper.performBitwiseOperationBoolean(mode, original, update), actual);
        checker.check(location.getExpectedLabels(taintValue, boolean.class), manager.getLabels(witness));
        checker.check(getAndApplyLabels(taintValue, taintUpdate, location, boolean.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndBitwiseByte(taintValue={0}, taintUpdate={1}, location={2}, mode={3})")
    @MethodSource("getAndBitwiseArguments")
    void getAndBitwiseByte(boolean taintValue, boolean taintUpdate, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        byte original = location.getCategory().getByte(holder);
        VarHandle handle = location.getVarHandle(lookup, byte.class);
        byte update = taintUpdate ? manager.setLabel((byte) 4, "label") : 4;
        byte witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndBitwiseInstanceFieldByte(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndBitwiseStaticFieldByte(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndBitwiseArrayElementByte(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        byte actual = location.getCategory().getByte(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(VarHandleHelper.performBitwiseOperationByte(mode, original, update), actual);
        checker.check(location.getExpectedLabels(taintValue, byte.class), manager.getLabels(witness));
        checker.check(getAndApplyLabels(taintValue, taintUpdate, location, byte.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndBitwiseChar(taintValue={0}, taintUpdate={1}, location={2}, mode={3})")
    @MethodSource("getAndBitwiseArguments")
    void getAndBitwiseChar(boolean taintValue, boolean taintUpdate, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        char original = location.getCategory().getChar(holder);
        VarHandle handle = location.getVarHandle(lookup, char.class);
        char update = taintUpdate ? manager.setLabel((char) 4, "label") : 4;
        char witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndBitwiseInstanceFieldChar(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndBitwiseStaticFieldChar(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndBitwiseArrayElementChar(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        char actual = location.getCategory().getChar(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(VarHandleHelper.performBitwiseOperationChar(mode, original, update), actual);
        checker.check(location.getExpectedLabels(taintValue, char.class), manager.getLabels(witness));
        checker.check(getAndApplyLabels(taintValue, taintUpdate, location, char.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndBitwiseShort(taintValue={0}, taintUpdate={1}, location={2}, mode={3})")
    @MethodSource("getAndBitwiseArguments")
    void getAndBitwiseShort(boolean taintValue, boolean taintUpdate, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        short original = location.getCategory().getShort(holder);
        VarHandle handle = location.getVarHandle(lookup, short.class);
        short update = taintUpdate ? manager.setLabel((short) 4, "label") : 4;
        short witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndBitwiseInstanceFieldShort(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndBitwiseStaticFieldShort(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndBitwiseArrayElementShort(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        short actual = location.getCategory().getShort(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(VarHandleHelper.performBitwiseOperationShort(mode, original, update), actual);
        checker.check(location.getExpectedLabels(taintValue, short.class), manager.getLabels(witness));
        checker.check(getAndApplyLabels(taintValue, taintUpdate, location, short.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndBitwiseInt(taintValue={0}, taintUpdate={1}, location={2}, mode={3})")
    @MethodSource("getAndBitwiseArguments")
    void getAndBitwiseInt(boolean taintValue, boolean taintUpdate, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        int original = location.getCategory().getInt(holder);
        VarHandle handle = location.getVarHandle(lookup, int.class);
        int update = taintUpdate ? manager.setLabel((int) 4, "label") : 4;
        int witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndBitwiseInstanceFieldInt(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndBitwiseStaticFieldInt(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndBitwiseArrayElementInt(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        int actual = location.getCategory().getInt(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(VarHandleHelper.performBitwiseOperationInt(mode, original, update), actual);
        checker.check(location.getExpectedLabels(taintValue, int.class), manager.getLabels(witness));
        checker.check(getAndApplyLabels(taintValue, taintUpdate, location, int.class), manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getAndBitwiseLong(taintValue={0}, taintUpdate={1}, location={2}, mode={3})")
    @MethodSource("getAndBitwiseArguments")
    void getAndBitwiseLong(boolean taintValue, boolean taintUpdate, VariableLocation location, AccessMode mode)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        long original = location.getCategory().getLong(holder);
        VarHandle handle = location.getVarHandle(lookup, long.class);
        long update = taintUpdate ? manager.setLabel((long) 4, "label") : 4;
        long witness;
        switch (location) {
            case INSTANCE_FIELD:
                witness = getAndBitwiseInstanceFieldLong(handle, holder, mode, update);
                break;
            case STATIC_FIELD:
                witness = getAndBitwiseStaticFieldLong(handle, mode, update);
                break;
            case ARRAY_ELEMENT:
                witness = getAndBitwiseArrayElementLong(handle, holder, mode, update);
                break;
            default:
                throw new AssertionError();
        }
        long actual = location.getCategory().getLong(holder);
        Assertions.assertEquals(original, witness);
        Assertions.assertEquals(VarHandleHelper.performBitwiseOperationLong(mode, original, update), actual);
        checker.check(location.getExpectedLabels(taintValue, long.class), manager.getLabels(witness));
        checker.check(getAndApplyLabels(taintValue, taintUpdate, location, long.class), manager.getLabels(actual));
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

    static Stream<Arguments> getAndSetArguments() {
        return BenchUtil.cartesianProduct(new Boolean[] {true, false}, VariableLocation.values(), new AccessMode[] {
            AccessMode.GET_AND_SET, AccessMode.GET_AND_SET_ACQUIRE, AccessMode.GET_AND_SET_RELEASE,
        });
    }

    static Stream<Arguments> compareAndExchangeArguments() {
        return BenchUtil.cartesianProduct(new Boolean[] {true, false}, VariableLocation.values(), new AccessMode[] {
            AccessMode.COMPARE_AND_EXCHANGE,
            AccessMode.COMPARE_AND_EXCHANGE_ACQUIRE,
            AccessMode.COMPARE_AND_EXCHANGE_RELEASE,
        });
    }

    static Stream<Arguments> getAndAddArguments() {
        return BenchUtil.cartesianProduct(
                new Boolean[] {true, false}, new Boolean[] {true, false}, VariableLocation.values(), new AccessMode[] {
                    AccessMode.GET_AND_ADD, AccessMode.GET_AND_ADD_ACQUIRE, AccessMode.GET_AND_ADD_RELEASE,
                });
    }

    static Stream<Arguments> getAndBitwiseArguments() {
        return BenchUtil.cartesianProduct(
                new Boolean[] {true, false}, new Boolean[] {true, false}, VariableLocation.values(), new AccessMode[] {
                    AccessMode.GET_AND_BITWISE_OR,
                    AccessMode.GET_AND_BITWISE_OR_RELEASE,
                    AccessMode.GET_AND_BITWISE_OR_ACQUIRE,
                    AccessMode.GET_AND_BITWISE_AND,
                    AccessMode.GET_AND_BITWISE_AND_RELEASE,
                    AccessMode.GET_AND_BITWISE_AND_ACQUIRE,
                    AccessMode.GET_AND_BITWISE_XOR,
                    AccessMode.GET_AND_BITWISE_XOR_RELEASE,
                    AccessMode.GET_AND_BITWISE_XOR_ACQUIRE
                });
    }

    private static Object[] getAndApplyLabels(
            boolean taintValue, boolean taintUpdate, VariableLocation location, Class<?> baseType) {
        List<Object> labels = new LinkedList<>(Arrays.asList(location.getExpectedLabels(taintValue, baseType)));
        if (taintUpdate) {
            labels.add("label");
        }
        return labels.toArray();
    }
}
