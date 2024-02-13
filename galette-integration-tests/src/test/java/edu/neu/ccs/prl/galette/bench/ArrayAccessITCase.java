package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.extension.FlowBench;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
@SuppressWarnings("MismatchedReadAndWriteOfArray")
public class ArrayAccessITCase {
    @Test
    public void intArrayTaintedIndexLoad() {
        Tag expected = Tag.of("label");
        int i = Tainter.setTag(5, expected);
        int[] a = new int[10];
        int value = a[i];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void referenceArrayTaintedIndexLoad() {
        Tag expected = Tag.of("label");
        int i = Tainter.setTag(5, expected);
        String[] a = new String[10];
        String value = a[i];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void longArrayTaintedIndexLoad() {
        Tag expected = Tag.of("label");
        int i = Tainter.setTag(5, expected);
        long[] a = new long[10];
        long value = a[i];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void intArrayTaintedIndexStore() {
        Tag expected = Tag.of("label");
        int i = Tainter.setTag(5, expected);
        int[] a = new int[10];
        a[i] = 7;
        int value = a[5];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void referenceArrayTaintedIndexStore() {
        Tag expected = Tag.of("label");
        int i = Tainter.setTag(5, expected);
        String[] a = new String[10];
        a[i] = "hello";
        String value = a[5];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void longArrayTaintedIndexStore() {
        Tag expected = Tag.of("label");
        int i = Tainter.setTag(5, expected);
        long[] a = new long[10];
        a[i] = 7;
        long value = a[5];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedIntArrayElement() {
        Tag expected = Tag.of("label");
        int[] a = new int[10];
        a[5] = Tainter.setTag(5, expected);
        int value = a[5];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedReferenceArrayElement() {
        Tag expected = Tag.of("label");
        String[] a = new String[10];
        a[5] = Tainter.setTag("hello", expected);
        String value = a[5];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedLongArrayElement() {
        Tag expected = Tag.of("label");
        long[] a = new long[10];
        a[5] = Tainter.setTag(5L, expected);
        long value = a[5];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }
}
