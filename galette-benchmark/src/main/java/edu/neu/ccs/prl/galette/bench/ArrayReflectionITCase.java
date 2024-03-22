package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import java.lang.reflect.Array;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class ArrayReflectionITCase extends ArrayBaseITCase {
    @Test
    void newInstance() {
        Object[] tag = new Object[] {"label1"};
        int length = manager.setLabels(5, tag);
        int[] a = (int[]) Array.newInstance(int.class, length);
        Assertions.assertEquals(5, a.length);
        checker.check(tag, manager.getLabels(a.length));
    }

    @Test
    void multiDimensionalNewInstance() {
        Object[] tag1 = new Object[] {"label1"};
        Object[] tag2 = new Object[] {"label2"};
        int length1 = manager.setLabels(3, tag1);
        int[] dimensions = new int[] {length1, 2, manager.setLabels(1, tag2)};
        int[][][] a = (int[][][]) Array.newInstance(int.class, dimensions);
        Assertions.assertEquals(2, a.length);
        Object[] actual = manager.getLabels(a.length);
        checker.check(tag1, actual);
        for (int[][] x : a) {
            Assertions.assertEquals(2, x.length);
            checker.checkEmpty(manager.getLabels(x.length));
            for (int[] y : x) {
                Assertions.assertEquals(1, y.length);
                actual = manager.getLabels(y.length);
                checker.check(tag2, actual);
            }
        }
    }

    @Test
    void getLength() {
        Object[] tag = new Object[] {"label1"};
        int[] a = new int[manager.setLabels(5, tag)];
        int length = Array.getLength(a);
        Assertions.assertEquals(5, length);
        checker.check(tag, manager.getLabels(length));
    }

    @Override
    Object[] getSetObject(boolean taintValue, int setIndex, int getIndex) {
        Object[] array = new Object[3];
        Object value = taintValue ? manager.setLabel(new Object(), "value") : 7;
        Array.set(array, setIndex, value);
        Object actual = Array.get(array, getIndex);
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetBoolean(boolean taintValue, int setIndex, int getIndex) {
        boolean[] array = new boolean[3];
        @SuppressWarnings("SimplifiableConditionalExpression")
        boolean value = taintValue ? manager.setLabel(true, "value") : true;
        Array.setBoolean(array, setIndex, value);
        boolean actual = Array.getBoolean(array, getIndex);
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetByte(boolean taintValue, int setIndex, int getIndex) {
        byte[] array = new byte[3];
        byte value = taintValue ? manager.setLabel((byte) 7, "value") : 7;
        Array.setByte(array, setIndex, value);
        byte actual = Array.getByte(array, getIndex);
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetChar(boolean taintValue, int setIndex, int getIndex) {
        char[] array = new char[3];
        char value = taintValue ? manager.setLabel((char) 7, "value") : 7;
        Array.setChar(array, setIndex, value);
        char actual = Array.getChar(array, getIndex);
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetShort(boolean taintValue, int setIndex, int getIndex) {
        short[] array = new short[3];
        short value = taintValue ? manager.setLabel((short) 7, "value") : 7;
        Array.setShort(array, setIndex, value);
        short actual = Array.getShort(array, getIndex);
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetInt(boolean taintValue, int setIndex, int getIndex) {
        int[] array = new int[3];
        int value = taintValue ? manager.setLabel(7, "value") : 7;
        Array.setInt(array, setIndex, value);
        int actual = Array.getInt(array, getIndex);
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetLong(boolean taintValue, int setIndex, int getIndex) {
        long[] array = new long[3];
        long value = taintValue ? manager.setLabel((long) 7, "value") : 7;
        Array.setLong(array, setIndex, value);
        long actual = Array.getLong(array, getIndex);
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetFloat(boolean taintValue, int setIndex, int getIndex) {
        float[] array = new float[3];
        float value = taintValue ? manager.setLabel((float) 7, "value") : 7;
        Array.setFloat(array, setIndex, value);
        float actual = Array.getFloat(array, getIndex);
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetDouble(boolean taintValue, int setIndex, int getIndex) {
        double[] array = new double[3];
        double value = taintValue ? manager.setLabel((double) 7, "value") : 7;
        Array.setDouble(array, setIndex, value);
        double actual = Array.getDouble(array, getIndex);
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }
}
