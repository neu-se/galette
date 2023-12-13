package edu.neu.ccs.prl.phosphor.data;

import edu.neu.ccs.prl.phosphor.internal.runtime.Tag;
import edu.neu.ccs.prl.phosphor.internal.runtime.Tainter;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssignmentITCase {
    @Test
    public void taintedObject() {
        Tag expected = Tag.create("label");
        HashMap<Object, Object> item = new HashMap<>();
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedArray() {
        Tag expected = Tag.create("label");
        int[] item = new int[2];
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedNull() {
        Tag expected = Tag.create("label");
        Object item = Tainter.setTag(null, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedBoolean() {
        Tag expected = Tag.create("label");
        boolean item = Tainter.setTag(true, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedByte() {
        Tag expected = Tag.create("label");
        byte item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedChar() {
        Tag expected = Tag.create("label");
        char item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedShort() {
        Tag expected = Tag.create("label");
        short item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedInt() {
        Tag expected = Tag.create("label");
        int item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedLong() {
        Tag expected = Tag.create("label");
        long item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedFloat() {
        Tag expected = Tag.create("label");
        float item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedDouble() {
        Tag expected = Tag.create("label");
        double item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }
}
