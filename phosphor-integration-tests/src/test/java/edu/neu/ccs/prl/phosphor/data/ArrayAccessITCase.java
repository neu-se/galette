package edu.neu.ccs.prl.phosphor.data;

import edu.neu.ccs.prl.phosphor.internal.runtime.Tag;
import edu.neu.ccs.prl.phosphor.internal.runtime.Tainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
@SuppressWarnings("MismatchedReadAndWriteOfArray")
public class ArrayAccessITCase {
    @Test
    public void intArrayTaintedIndexLoad() {
        Tag expected = Tag.create("label");
        int i = Tainter.setTag(5, expected);
        int[] a = new int[10];
        int value = a[i];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void referenceArrayTaintedIndexLoad() {
        Tag expected = Tag.create("label");
        int i = Tainter.setTag(5, expected);
        String[] a = new String[10];
        String value = a[i];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void longArrayTaintedIndexLoad() {
        Tag expected = Tag.create("label");
        int i = Tainter.setTag(5, expected);
        long[] a = new long[10];
        long value = a[i];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void intArrayTaintedIndexStore() {
        Tag expected = Tag.create("label");
        int i = Tainter.setTag(5, expected);
        int[] a = new int[10];
        a[i] = 7;
        int value = a[5];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void referenceArrayTaintedIndexStore() {
        Tag expected = Tag.create("label");
        int i = Tainter.setTag(5, expected);
        String[] a = new String[10];
        a[i] = "hello";
        String value = a[5];
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void longArrayTaintedIndexStore() {
        Tag expected = Tag.create("label");
        int i = Tainter.setTag(5, expected);
        long[] a = new long[10];
        a[i] = 7;
        long value = a[5];
        Tag actual = Tainter.getTag(value);
        Assertions.assertTrue(actual.isEmpty());
    }
}
