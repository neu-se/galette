package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@FlowBench
public class FieldReflectionITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    @ParameterizedTest(name = "getField(baseType={0}, taintValue={1}, category={2})")
    @MethodSource("arguments")
    void getField(Class<?> baseType, boolean taintValue, HolderValueCategory category)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, taintValue, 120, false, "hello");
        Field f = category.getField(baseType);
        Object[] labels = checkGet(holder, category, f, baseType);
        if (taintValue) {
            checker.check(category.getLabels(baseType), labels);
        } else {
            checker.checkEmpty(labels);
        }
    }

    private Object[] checkGet(Holder holder, HolderValueCategory category, Field f, Class<?> baseType)
            throws ReflectiveOperationException {
        Class<?> type = f.getType();
        if (type == Integer.TYPE) {
            return checkGetInt(holder, category, f);
        } else if (type == Boolean.TYPE) {
            return checkGetBoolean(holder, category, f);
        } else if (type == Byte.TYPE) {
            return checkGetByte(holder, category, f);
        } else if (type == Character.TYPE) {
            return checkGetChar(holder, category, f);
        } else if (type == Short.TYPE) {
            return checkGetShort(holder, category, f);
        } else if (type == Double.TYPE) {
            return checkGetDouble(holder, category, f);
        } else if (type == Float.TYPE) {
            return checkGetFloat(holder, category, f);
        } else if (type == Long.TYPE) {
            return checkGetLong(holder, category, f);
        } else {
            return checkGetObject(holder, category, f, baseType);
        }
    }

    private Object[] checkGetBoolean(Holder holder, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        boolean expected = category.getBoolean(holder);
        boolean actual = f.getBoolean(Modifier.isStatic(f.getModifiers()) ? null : holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkGetByte(Holder holder, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        byte expected = category.getByte(holder);
        byte actual = f.getByte(Modifier.isStatic(f.getModifiers()) ? null : holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkGetChar(Holder holder, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        char expected = category.getChar(holder);
        char actual = f.getChar(Modifier.isStatic(f.getModifiers()) ? null : holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkGetShort(Holder holder, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        short expected = category.getShort(holder);
        short actual = f.getShort(Modifier.isStatic(f.getModifiers()) ? null : holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkGetInt(Holder holder, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        int expected = category.getInt(holder);
        int actual = f.getInt(Modifier.isStatic(f.getModifiers()) ? null : holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkGetLong(Holder holder, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        long expected = category.getLong(holder);
        long actual = f.getLong(Modifier.isStatic(f.getModifiers()) ? null : holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkGetFloat(Holder holder, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        float expected = category.getFloat(holder);
        float actual = f.getFloat(Modifier.isStatic(f.getModifiers()) ? null : holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkGetDouble(Holder holder, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        double expected = category.getDouble(holder);
        double actual = f.getDouble(Modifier.isStatic(f.getModifiers()) ? null : holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkGetObject(Holder holder, HolderValueCategory category, Field f, Class<?> baseType)
            throws ReflectiveOperationException {
        Object expected = category.getValue(baseType, holder);
        Object actual = f.get(Modifier.isStatic(f.getModifiers()) ? null : holder);
        Assertions.assertEquals(expected, actual);
        return getElementLabels(actual);
    }

    @ParameterizedTest(name = "setField(baseType={0}, taintValue={1}, category={2})")
    @MethodSource("arguments")
    void setField(Class<?> baseType, boolean taintValue, HolderValueCategory category)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue, 120, false, "hello");
        Holder source = new Holder(manager, taintValue, 4, true, new Object());
        Field f = category.getField(baseType);
        Object[] labels = checkSet(holder, source, category, f, baseType);
        if (taintValue) {
            checker.check(category.getLabels(baseType), labels);
        } else {
            checker.checkEmpty(labels);
        }
    }

    private Object[] checkSet(Holder holder, Holder source, HolderValueCategory category, Field f, Class<?> baseType)
            throws ReflectiveOperationException {
        Class<?> type = f.getType();
        if (type == Integer.TYPE) {
            return checkSetInt(holder, source, category, f);
        } else if (type == Boolean.TYPE) {
            return checkSetBoolean(holder, source, category, f);
        } else if (type == Byte.TYPE) {
            return checkSetByte(holder, source, category, f);
        } else if (type == Character.TYPE) {
            return checkSetChar(holder, source, category, f);
        } else if (type == Short.TYPE) {
            return checkSetShort(holder, source, category, f);
        } else if (type == Double.TYPE) {
            return checkSetDouble(holder, source, category, f);
        } else if (type == Float.TYPE) {
            return checkSetFloat(holder, source, category, f);
        } else if (type == Long.TYPE) {
            return checkSetLong(holder, source, category, f);
        } else {
            return checkSetObject(holder, source, category, f, baseType);
        }
    }

    private Object[] checkSetBoolean(Holder holder, Holder source, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        boolean expected = category.getBoolean(source);
        f.setBoolean(Modifier.isStatic(f.getModifiers()) ? null : holder, expected);
        boolean actual = category.getBoolean(holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkSetByte(Holder holder, Holder source, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        byte expected = category.getByte(source);
        f.setByte(Modifier.isStatic(f.getModifiers()) ? null : holder, expected);
        byte actual = category.getByte(holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkSetChar(Holder holder, Holder source, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        char expected = category.getChar(source);
        f.setChar(Modifier.isStatic(f.getModifiers()) ? null : holder, expected);
        char actual = category.getChar(holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkSetShort(Holder holder, Holder source, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        short expected = category.getShort(source);
        f.setShort(Modifier.isStatic(f.getModifiers()) ? null : holder, expected);
        short actual = category.getShort(holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkSetInt(Holder holder, Holder source, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        int expected = category.getInt(source);
        f.setInt(Modifier.isStatic(f.getModifiers()) ? null : holder, expected);
        int actual = category.getInt(holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkSetLong(Holder holder, Holder source, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        long expected = category.getLong(source);
        f.setLong(Modifier.isStatic(f.getModifiers()) ? null : holder, expected);
        long actual = category.getLong(holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkSetFloat(Holder holder, Holder source, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        float expected = category.getFloat(source);
        f.setFloat(Modifier.isStatic(f.getModifiers()) ? null : holder, expected);
        float actual = category.getFloat(holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkSetDouble(Holder holder, Holder source, HolderValueCategory category, Field f)
            throws ReflectiveOperationException {
        double expected = category.getDouble(source);
        f.setDouble(Modifier.isStatic(f.getModifiers()) ? null : holder, expected);
        double actual = category.getDouble(holder);
        Assertions.assertEquals(expected, actual);
        return manager.getLabels(actual);
    }

    private Object[] checkSetObject(
            Holder holder, Holder source, HolderValueCategory category, Field f, Class<?> baseType)
            throws ReflectiveOperationException {
        Object expected = category.getValue(baseType, source);
        f.set(Modifier.isStatic(f.getModifiers()) ? null : holder, expected);
        Object actual = category.getValue(baseType, source);
        Assertions.assertEquals(expected, actual);
        return getElementLabels(actual);
    }

    private Object[] getElementLabels(Object o) {
        if (o instanceof Object[]) {
            return getElementLabels(((Object[]) o)[0]);
        } else if (o instanceof boolean[]) {
            return manager.getLabels(((boolean[]) o)[0]);
        } else if (o instanceof byte[]) {
            return manager.getLabels(((byte[]) o)[0]);
        } else if (o instanceof char[]) {
            return manager.getLabels(((char[]) o)[0]);
        } else if (o instanceof short[]) {
            return manager.getLabels(((short[]) o)[0]);
        } else if (o instanceof int[]) {
            return manager.getLabels(((int[]) o)[0]);
        } else if (o instanceof long[]) {
            return manager.getLabels(((long[]) o)[0]);
        } else if (o instanceof float[]) {
            return manager.getLabels(((float[]) o)[0]);
        } else if (o instanceof double[]) {
            return manager.getLabels(((double[]) o)[0]);
        } else if (o instanceof Boolean) {
            return manager.getLabels(((Boolean) o).booleanValue());
        } else if (o instanceof Byte) {
            return manager.getLabels(((Byte) o).byteValue());
        } else if (o instanceof Character) {
            return manager.getLabels(((Character) o).charValue());
        } else if (o instanceof Short) {
            return manager.getLabels(((Short) o).shortValue());
        } else if (o instanceof Integer) {
            return manager.getLabels(((Integer) o).intValue());
        } else if (o instanceof Long) {
            return manager.getLabels(((Long) o).longValue());
        } else if (o instanceof Float) {
            return manager.getLabels(((Float) o).floatValue());
        } else if (o instanceof Double) {
            return manager.getLabels(((Double) o).doubleValue());
        } else {
            return manager.getLabels(o);
        }
    }

    static Stream<Arguments> arguments() {
        Class<?>[] types = new Class[] {
            Integer.TYPE,
            Boolean.TYPE,
            Byte.TYPE,
            Character.TYPE,
            Short.TYPE,
            Double.TYPE,
            Float.TYPE,
            Long.TYPE,
            Object.class
        };
        return BenchUtil.cartesianProduct(types, new Boolean[] {true, false}, HolderValueCategory.values())
                .filter(ConstructorReflectionITCase::isValid);
    }
}
