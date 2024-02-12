package edu.neu.ccs.prl.galette.data;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("MismatchedReadAndWriteOfArray")
public class ArrayLengthITCase {
    @Test
    public void primitiveArrayLength() {
        Tag expected = Tag.of("label");
        int i = Tainter.setTag(5, expected);
        int[] a = new int[i];
        Tag actual = Tainter.getTag(a.length);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void referenceArrayLength() {
        Tag expected = Tag.of("label");
        int i = Tainter.setTag(5, expected);
        String[] a = new String[i];
        Tag actual = Tainter.getTag(a.length);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void multiPrimitiveArrayLength() {
        Tag label1 = Tag.of("label1");
        Tag label2 = Tag.of("label2");
        Tag label3 = Tag.of("label3");
        int[][][] a = new int[Tainter.setTag(2, label1)][Tainter.setTag(3, label2)][Tainter.setTag(1, label3)];
        Tag actual = Tainter.getTag(a.length);
        Assertions.assertEquals(label1, actual);
        for (int[][] x : a) {
            actual = Tainter.getTag(x.length);
            Assertions.assertEquals(label2, actual);
            for (int[] y : x) {
                actual = Tainter.getTag(y.length);
                Assertions.assertEquals(label3, actual);
            }
        }
    }

    @Test
    public void multiPrimitiveArrayLengthJagged() {
        Tag expected = Tag.of("label");
        int i = Tainter.setTag(5, expected);
        int[][][] a = new int[i][i][];
        Tag actual = Tainter.getTag(a.length);
        Assertions.assertEquals(expected, actual);
        actual = Tainter.getTag(a[0].length);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void multiReferenceArrayLength() {
        Tag expected = Tag.of("label");
        int i = Tainter.setTag(5, expected);
        String[][] ar = new String[i][i];
        Tag actual = Tainter.getTag(ar.length);
        Assertions.assertEquals(expected, actual);
        actual = Tainter.getTag(ar[0].length);
        Assertions.assertEquals(expected, actual);
    }
}
