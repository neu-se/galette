package edu.neu.ccs.prl.galette.data;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"UnnecessaryBoxing", "UnnecessaryUnboxing"})
public class BoxedTypeITCase {
    @Test
    @Disabled("cache")
    void boxedBoolean() {
        Tag expected = Tag.create("label");
        boolean value = Tainter.setTag(true, expected);
        Boolean boxed = Boolean.valueOf(value);
        boolean unboxed = boxed.booleanValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Disabled("cache")
    void boxedByte() {
        Tag expected = Tag.create("label");
        byte value = Tainter.setTag((byte) 8, expected);
        Byte boxed = Byte.valueOf(value);
        byte unboxed = boxed.byteValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Disabled("cache")
    void boxedCharacter() {
        Tag expected = Tag.create("label");
        char value = Tainter.setTag('x', expected);
        Character boxed = Character.valueOf(value);
        char unboxed = boxed.charValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Disabled("cache")
    void boxedShort() {
        Tag expected = Tag.create("label");
        short value = Tainter.setTag((short) 8, expected);
        Short boxed = Short.valueOf(value);
        short unboxed = boxed.shortValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Disabled("cache + hard-coded offsets")
    void boxedInteger() {
        Tag expected = Tag.create("label");
        int value = Tainter.setTag(8, expected);
        Integer boxed = Integer.valueOf(value);
        int unboxed = boxed.intValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Disabled("cache + hard-coded offsets")
    void boxedLong() {
        Tag expected = Tag.create("label");
        long value = Tainter.setTag(8L, expected);
        Long boxed = Long.valueOf(value);
        long unboxed = boxed.longValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void boxedFloat() {
        Tag expected = Tag.create("label");
        float value = Tainter.setTag(8f, expected);
        Float boxed = Float.valueOf(value);
        float unboxed = boxed.floatValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void boxedDouble() {
        Tag expected = Tag.create("label");
        double value = Tainter.setTag(8.0, expected);
        Double boxed = Double.valueOf(value);
        double unboxed = boxed.doubleValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
    }
}
