package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
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
        Object[] actual = manager.getLabels(value);
        checker.check(expected, actual);
    }

    @Test
    public void referenceArrayTaintedIndexLoad(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int i = manager.setLabels(2, expected);
        String[] a = new String[] {"a", "b", "c"};
        String value = a[i];
        Assertions.assertEquals("c", value);
        Object[] actual = manager.getLabels(value);
        checker.check(expected, actual);
    }

    @Test
    public void longArrayTaintedIndexLoad(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int i = manager.setLabels(2, expected);
        long[] a = new long[] {7, 8, 9};
        long value = a[i];
        Assertions.assertEquals(9L, value);
        Object[] actual = manager.getLabels(value);
        checker.check(expected, actual);
    }

    @Test
    public void intArrayTaintedIndexStore(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int i = manager.setLabels(5, expected);
        int[] a = new int[10];
        a[i] = 7;
        int value = a[5];
        Assertions.assertEquals(7, value);
        Object[] actual = manager.getLabels(value);
        checker.check(expected, actual);
    }

    @Test
    public void referenceArrayTaintedIndexStore(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int i = manager.setLabels(5, expected);
        String[] a = new String[10];
        a[i] = "hello";
        String value = a[5];
        Assertions.assertEquals("hello", value);
        Object[] actual = manager.getLabels(value);
        checker.check(expected, actual);
    }

    @Test
    public void longArrayTaintedIndexStore(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int i = manager.setLabels(5, expected);
        long[] a = new long[10];
        a[i] = 7;
        long value = a[5];
        Assertions.assertEquals(7, value);
        Object[] actual = manager.getLabels(value);
        checker.check(expected, actual);
    }

    @Test
    public void taintedIntArrayElement(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int[] a = new int[10];
        a[5] = manager.setLabels(5, expected);
        int value = a[5];
        Assertions.assertEquals(5, value);
        Object[] actual = manager.getLabels(value);
        checker.check(expected, actual);
    }

    @Test
    public void taintedReferenceArrayElement(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        String[] a = new String[10];
        a[5] = manager.setLabels("hello", expected);
        String value = a[5];
        Assertions.assertEquals("hello", value);
        Object[] actual = manager.getLabels(value);
        checker.check(expected, actual);
    }

    @Test
    public void taintedLongArrayElement(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        long[] a = new long[10];
        a[5] = manager.setLabels(5L, expected);
        long value = a[5];
        Assertions.assertEquals(5L, value);
        Object[] actual = manager.getLabels(value);
        checker.check(expected, actual);
    }
}
