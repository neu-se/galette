package edu.neu.ccs.prl.phosphor.data;

import edu.neu.ccs.prl.phosphor.internal.runtime.Tag;
import edu.neu.ccs.prl.phosphor.internal.runtime.Tainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ArrayLengthITCase {
    @Test
    public void primitiveArrayLength() {
        Tag expected = Tag.create("label");
        int i = Tainter.setTag(5, expected);
        int[] a = new int[i];
        Tag actual = Tainter.getTag(a.length);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void referenceArrayLength() {
        Tag expected = Tag.create("label");
        int i = Tainter.setTag(5, expected);
        String[] a = new String[i];
        Tag actual = Tainter.getTag(a.length);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void multiPrimitiveArrayLength() {
        Tag expected = Tag.create("label");
        int i = Tainter.setTag(5, expected);
        int[][] a = new int[i][i];
        Tag actual = Tainter.getTag(a.length);
        Assertions.assertEquals(expected, actual);
        actual = Tainter.getTag(a[0].length);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void multiReferenceArrayLength() {
        Tag expected = Tag.create("label");
        int i = Tainter.setTag(5, expected);
        String[][] ar = new String[i][i];
        Tag actual = Tainter.getTag(ar.length);
        Assertions.assertEquals(expected, actual);
        actual = Tainter.getTag(ar[0].length);
        Assertions.assertEquals(expected, actual);
    }
}
