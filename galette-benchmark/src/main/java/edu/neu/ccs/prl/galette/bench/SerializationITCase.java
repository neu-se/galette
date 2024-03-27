package edu.neu.ccs.prl.galette.bench;

import static edu.neu.ccs.prl.galette.bench.HolderValueCategory.*;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.io.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@FlowBench
public class SerializationITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    @ParameterizedTest(name = "roundTrip(baseType={0}, taintValue={1}, category={2})")
    @MethodSource("arguments")
    void roundTrip(Class<?> baseType, boolean taintValue, HolderValueCategory category)
            throws ReflectiveOperationException, IOException {
        Holder original = new Holder(manager, taintValue, 120, false, "hello");
        Holder result = roundTripSerialize(original);
        Object expected = category.getValue(baseType, original);
        Object actual = category.getValue(baseType, result);
        checkCopyEquality(expected, actual);
        Object[] labels = category.getElementLabels(baseType, result, manager);
        if (taintValue) {
            checker.check(category.getLabels(baseType), labels);
        } else {
            checker.checkEmpty(labels);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T roundTripSerialize(T input) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteStream);
        outputStream.writeObject(input);
        outputStream.close();
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        T result = (T) inputStream.readObject();
        inputStream.close();
        return result;
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
        HolderValueCategory[] categories = new HolderValueCategory[] {
            BASIC, BOXED, ONE_DIMENSIONAL_ARRAY, TWO_DIMENSIONAL_ARRAY, OBJECT_HOLDING_ONE_DIMENSIONAL_ARRAY
        };
        return BenchUtil.cartesianProduct(types, new Boolean[] {true, false}, categories)
                .filter(MethodReflectionITCase::isValid);
    }

    private void checkCopyEquality(Object expected, Object actual) {
        if (expected instanceof Object[] && actual instanceof Object[]) {
            Assertions.assertArrayEquals((Object[]) expected, (Object[]) actual);
        } else if (expected instanceof boolean[] && actual instanceof boolean[]) {
            Assertions.assertArrayEquals((boolean[]) expected, (boolean[]) actual);
        } else if (expected instanceof byte[] && actual instanceof byte[]) {
            Assertions.assertArrayEquals((byte[]) expected, (byte[]) actual);
        } else if (expected instanceof char[] && actual instanceof char[]) {
            Assertions.assertArrayEquals((char[]) expected, (char[]) actual);
        } else if (expected instanceof short[] && actual instanceof short[]) {
            Assertions.assertArrayEquals((short[]) expected, (short[]) actual);
        } else if (expected instanceof int[] && actual instanceof int[]) {
            Assertions.assertArrayEquals((int[]) expected, (int[]) actual);
        } else if (expected instanceof long[] && actual instanceof long[]) {
            Assertions.assertArrayEquals((long[]) expected, (long[]) actual);
        } else if (expected instanceof float[] && actual instanceof float[]) {
            Assertions.assertArrayEquals((float[]) expected, (float[]) actual);
        } else if (expected instanceof double[] && actual instanceof double[]) {
            Assertions.assertArrayEquals((double[]) expected, (double[]) actual);
        } else {
            Assertions.assertEquals(expected, actual);
        }
    }
}
