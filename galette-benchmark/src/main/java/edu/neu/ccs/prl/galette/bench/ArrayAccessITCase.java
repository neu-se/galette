package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class ArrayAccessITCase {
    @Test
    public void intArrayTaintedIndexLoad(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int i = manager.setLabels(2, expected);
        int[] a = new int[] {7, 8, 9};
        int value = a[i];
        Assertions.assertEquals(9, value);
        checker.check(expected, manager.getLabels(value));
        checker.checkEmpty(manager.getLabels(a[0]));
    }

    @Test
    public void referenceArrayTaintedIndexLoad(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int i = manager.setLabels(2, expected);
        String[] a = new String[] {"a", "b", "c"};
        String value = a[i];
        Assertions.assertEquals("c", value);
        checker.check(expected, manager.getLabels(value));
        checker.checkEmpty(manager.getLabels(a[0]));
    }

    @Test
    public void longArrayTaintedIndexLoad(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int i = manager.setLabels(2, expected);
        long[] a = new long[] {7, 8, 9};
        long value = a[i];
        Assertions.assertEquals(9L, value);
        checker.check(expected, manager.getLabels(value));
        checker.checkEmpty(manager.getLabels(a[0]));
    }

    @Test
    public void intArrayTaintedIndexStore(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int i = manager.setLabels(5, expected);
        int[] a = new int[10];
        a[i] = 7;
        int value = a[5];
        Assertions.assertEquals(7, value);
        checker.check(expected, manager.getLabels(value));
        checker.checkEmpty(manager.getLabels(a[0]));
    }

    @Test
    public void referenceArrayTaintedIndexStore(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int i = manager.setLabels(5, expected);
        String[] a = new String[10];
        a[i] = "hello";
        String value = a[5];
        Assertions.assertEquals("hello", value);
        checker.check(expected, manager.getLabels(value));
        checker.checkEmpty(manager.getLabels(a[0]));
    }

    @Test
    public void longArrayTaintedIndexStore(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int i = manager.setLabels(5, expected);
        long[] a = new long[10];
        a[i] = 7;
        long value = a[5];
        Assertions.assertEquals(7, value);
        checker.check(expected, manager.getLabels(value));
        checker.checkEmpty(manager.getLabels(a[0]));
    }

    @Test
    public void taintedIntArrayElement(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int[] a = new int[10];
        a[5] = manager.setLabels(5, expected);
        int value = a[5];
        Assertions.assertEquals(5, value);
        checker.check(expected, manager.getLabels(value));
        checker.checkEmpty(manager.getLabels(a[0]));
    }

    @Test
    public void taintedReferenceArrayElement(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        String[] a = new String[10];
        a[5] = manager.setLabels("hello", expected);
        String value = a[5];
        Assertions.assertEquals("hello", value);
        checker.check(expected, manager.getLabels(value));
        checker.checkEmpty(manager.getLabels(a[0]));
    }

    @Test
    public void taintedLongArrayElement(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        long[] a = new long[10];
        a[5] = manager.setLabels(5L, expected);
        long value = a[5];
        Assertions.assertEquals(5L, value);
        checker.check(expected, manager.getLabels(value));
        checker.checkEmpty(manager.getLabels(a[0]));
    }

    @Test
    public void taintedMultiDimensionalArrayElement(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int[][][] a = new int[3][5][7];
        a[2][3][4] = manager.setLabels(5, expected);
        int value = a[2][3][4];
        Assertions.assertEquals(5, value);
        checker.check(expected, manager.getLabels(value));
        checker.checkEmpty(manager.getLabels(a[0]));
    }

    @Test
    public void sortedIntArray(TagManager manager, FlowChecker checker) {
        int[] a = new int[] {44, 7, 26, 30, 51, 39, 34, 34, 19, 47};
        for (int i = 0; i < a.length; i++) {
            a[i] = manager.setLabels(a[i], new String[] {String.valueOf(i)});
        }
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
}
