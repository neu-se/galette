package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@FlowBench
public abstract class ArrayBaseITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    @ParameterizedTest(name = "getSetElement(type={0}, taintValue={1}, taintSetIndex={2}, taintGetIndex={3})")
    @MethodSource("arguments")
    void getSetElement(Class<?> type, boolean taintValue, boolean taintSetIndex, boolean taintGetIndex) {
        int setIndex = taintSetIndex ? manager.setLabel(1, "set") : 1;
        int getIndex = taintGetIndex ? manager.setLabel(1, "get") : 1;
        Object[] actualLabels = getSetElement(type, taintValue, setIndex, getIndex);
        checker.check(getExpectedLabels(taintValue, taintSetIndex, taintGetIndex), actualLabels);
    }

    private Object[] getSetElement(Class<?> type, boolean taintValue, int setIndex, int getIndex) {
        if (type == Integer.TYPE) {
            return getSetInt(taintValue, setIndex, getIndex);
        } else if (type == Boolean.TYPE) {
            return getSetBoolean(taintValue, setIndex, getIndex);
        } else if (type == Byte.TYPE) {
            return getSetByte(taintValue, setIndex, getIndex);
        } else if (type == Character.TYPE) {
            return getSetChar(taintValue, setIndex, getIndex);
        } else if (type == Short.TYPE) {
            return getSetShort(taintValue, setIndex, getIndex);
        } else if (type == Double.TYPE) {
            return getSetDouble(taintValue, setIndex, getIndex);
        } else if (type == Float.TYPE) {
            return getSetFloat(taintValue, setIndex, getIndex);
        } else if (type == Long.TYPE) {
            return getSetLong(taintValue, setIndex, getIndex);
        } else if (type == Object.class) {
            return getSetObject(taintValue, setIndex, getIndex);
        } else {
            throw new AssertionError();
        }
    }

    abstract Object[] getSetObject(boolean taintValue, int setIndex, int getIndex);

    abstract Object[] getSetBoolean(boolean taintValue, int setIndex, int getIndex);

    abstract Object[] getSetByte(boolean taintValue, int setIndex, int getIndex);

    abstract Object[] getSetChar(boolean taintValue, int setIndex, int getIndex);

    abstract Object[] getSetShort(boolean taintValue, int setIndex, int getIndex);

    abstract Object[] getSetInt(boolean taintValue, int setIndex, int getIndex);

    abstract Object[] getSetLong(boolean taintValue, int setIndex, int getIndex);

    abstract Object[] getSetFloat(boolean taintValue, int setIndex, int getIndex);

    abstract Object[] getSetDouble(boolean taintValue, int setIndex, int getIndex);

    private static Object[] getExpectedLabels(boolean taintValue, boolean taintSetIndex, boolean taintGetIndex) {
        int count = (taintValue ? 1 : 0) + (taintGetIndex ? 1 : 0) + (taintSetIndex ? 1 : 0);
        Object[] result = new Object[count];
        int index = 0;
        if (taintValue) {
            result[index++] = "value";
        }
        if (taintSetIndex) {
            result[index++] = "set";
        }
        if (taintGetIndex) {
            result[index] = "get";
        }
        return result;
    }

    static Stream<Arguments> arguments() {
        Class<?>[] types = new Class[] {
            Object.class,
            boolean.class,
            byte.class,
            char.class,
            short.class,
            int.class,
            long.class,
            float.class,
            double.class
        };
        Boolean[] booleans = new Boolean[] {true, false};
        return BenchUtil.cartesianProduct(types, booleans, booleans, booleans);
    }
}
