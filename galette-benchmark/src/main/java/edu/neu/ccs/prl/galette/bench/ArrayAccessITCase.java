package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class ArrayAccessITCase extends ArrayBaseITCase {
    @Test
    void taintedMultiDimensionalArrayElement() {
        Object[] expected = new Object[] {"label"};
        int[][][] a = new int[3][5][7];
        a[2][3][4] = manager.setLabel(5, "label");
        int value = a[2][3][4];
        Assertions.assertEquals(5, value);
        checker.check(expected, manager.getLabels(value));
        checker.checkEmpty(manager.getLabels(a[0]));
    }

    @Test
    void sortedIntArray() {
        int[] a = new int[] {44, 7, 26, 30, 51, 39, 34, 34, 19, 47};
        BenchUtil.taintWithIndices(manager, a);
        Arrays.sort(a);
        int[] expectedValues = new int[] {7, 19, 26, 30, 34, 34, 39, 44, 47, 51};
        for (int i = 0; i < expectedValues.length; i++) {
            Assertions.assertEquals(expectedValues[i], a[i]);
        }
        String[] expectedLabels = new String[] {"1", "8", "2", "3", "6", "7", "5", "0", "9", "4"};
        for (int i = 0; i < expectedLabels.length; i++) {
            checker.check(new Object[] {expectedLabels[i]}, manager.getLabels(a[i]));
        }
    }

    @Override
    Object[] getSetObject(boolean taintValue, int setIndex, int getIndex) {
        Object[] array = new Object[3];
        Object value = taintValue ? manager.setLabel(new Object(), "value") : 7;
        array[setIndex] = value;
        Object actual = array[getIndex];
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetBoolean(boolean taintValue, int setIndex, int getIndex) {
        boolean[] array = new boolean[3];
        @SuppressWarnings("SimplifiableConditionalExpression")
        boolean value = taintValue ? manager.setLabel(true, "value") : true;
        array[setIndex] = value;
        boolean actual = array[getIndex];
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetByte(boolean taintValue, int setIndex, int getIndex) {
        byte[] array = new byte[3];
        byte value = taintValue ? manager.setLabel((byte) 7, "value") : 7;
        array[setIndex] = value;
        byte actual = array[getIndex];
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetChar(boolean taintValue, int setIndex, int getIndex) {
        char[] array = new char[3];
        char value = taintValue ? manager.setLabel((char) 7, "value") : 7;
        array[setIndex] = value;
        char actual = array[getIndex];
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetShort(boolean taintValue, int setIndex, int getIndex) {
        short[] array = new short[3];
        short value = taintValue ? manager.setLabel((short) 7, "value") : 7;
        array[setIndex] = value;
        short actual = array[getIndex];
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetInt(boolean taintValue, int setIndex, int getIndex) {
        int[] array = new int[3];
        int value = taintValue ? manager.setLabel(7, "value") : 7;
        array[setIndex] = value;
        int actual = array[getIndex];
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetLong(boolean taintValue, int setIndex, int getIndex) {
        long[] array = new long[3];
        long value = taintValue ? manager.setLabel((long) 7, "value") : 7;
        array[setIndex] = value;
        long actual = array[getIndex];
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetFloat(boolean taintValue, int setIndex, int getIndex) {
        float[] array = new float[3];
        float value = taintValue ? manager.setLabel((float) 7, "value") : 7;
        array[setIndex] = value;
        float actual = array[getIndex];
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }

    @Override
    Object[] getSetDouble(boolean taintValue, int setIndex, int getIndex) {
        double[] array = new double[3];
        double value = taintValue ? manager.setLabel((double) 7, "value") : 7;
        array[setIndex] = value;
        double actual = array[getIndex];
        Assertions.assertEquals(value, actual);
        return manager.getLabels(actual);
    }
}
