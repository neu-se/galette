package edu.neu.ccs.prl.galette.data;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssignmentITCase {
    @Test
    public void taintedObject() {
        Tag expected = Tag.of("label");
        HashMap<Object, Object> item = new HashMap<>();
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedArray() {
        Tag expected = Tag.of("label");
        int[] item = new int[2];
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedNull() {
        Tag expected = Tag.of("label");
        Object item = Tainter.setTag(null, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedBoolean() {
        Tag expected = Tag.of("label");
        boolean item = Tainter.setTag(true, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedByte() {
        Tag expected = Tag.of("label");
        byte item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedChar() {
        Tag expected = Tag.of("label");
        char item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedShort() {
        Tag expected = Tag.of("label");
        short item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedInt() {
        Tag expected = Tag.of("label");
        int item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedLong() {
        Tag expected = Tag.of("label");
        long item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedFloat() {
        Tag expected = Tag.of("label");
        float item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void taintedDouble() {
        Tag expected = Tag.of("label");
        double item = 7;
        item = Tainter.setTag(item, expected);
        Tag actual = Tainter.getTag(item);
        Assertions.assertEquals(expected, actual);
    }
}
