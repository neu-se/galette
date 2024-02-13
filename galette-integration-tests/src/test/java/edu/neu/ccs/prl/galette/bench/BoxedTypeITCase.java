package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.extension.FlowBench;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
@SuppressWarnings({"UnnecessaryBoxing", "UnnecessaryUnboxing"})
public class BoxedTypeITCase {
    @Test
    void boxedBoolean() {
        Tag expected = Tag.of("label");
        boolean value = Tainter.setTag(true, expected);
        Boolean boxed = Boolean.valueOf(value);
        boolean unboxed = boxed.booleanValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(
                Tag.getEmptyTag(), Tainter.getTag(Boolean.valueOf(true).booleanValue()));
    }

    @Test
    void boxedByte() {
        Tag expected = Tag.of("label");
        byte value = Tainter.setTag((byte) 8, expected);
        Byte boxed = Byte.valueOf(value);
        byte unboxed = boxed.byteValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(
                Tag.getEmptyTag(), Tainter.getTag(Byte.valueOf((byte) 8).byteValue()));
    }

    @Test
    void boxedCharacter() {
        Tag expected = Tag.of("label");
        char value = Tainter.setTag('x', expected);
        Character boxed = Character.valueOf(value);
        char unboxed = boxed.charValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(
                Tag.getEmptyTag(), Tainter.getTag(Character.valueOf('x').charValue()));
    }

    @Test
    void boxedShort() {
        Tag expected = Tag.of("label");
        short value = Tainter.setTag((short) 8, expected);
        Short boxed = Short.valueOf(value);
        short unboxed = boxed.shortValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(
                Tag.getEmptyTag(), Tainter.getTag(Short.valueOf((short) 8).shortValue()));
    }

    @Test
    void boxedInteger() {
        Tag expected = Tag.of("label");
        int value = Tainter.setTag(8, expected);
        Integer boxed = Integer.valueOf(value);
        int unboxed = boxed.intValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(
                Tag.getEmptyTag(), Tainter.getTag(Integer.valueOf(8).intValue()));
    }

    @Test
    void boxedLong() {
        Tag expected = Tag.of("label");
        long value = Tainter.setTag(8L, expected);
        Long boxed = Long.valueOf(value);
        long unboxed = boxed.longValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(
                Tag.getEmptyTag(), Tainter.getTag(Long.valueOf(8L).longValue()));
    }

    @Test
    void boxedFloat() {
        Tag expected = Tag.of("label");
        float value = Tainter.setTag(8f, expected);
        Float boxed = Float.valueOf(value);
        float unboxed = boxed.floatValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(
                Tag.getEmptyTag(), Tainter.getTag(Float.valueOf(8f).floatValue()));
    }

    @Test
    void boxedDouble() {
        Tag expected = Tag.of("label");
        double value = Tainter.setTag(8.0, expected);
        Double boxed = Double.valueOf(value);
        double unboxed = boxed.doubleValue();
        Tag actual = Tainter.getTag(unboxed);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(
                Tag.getEmptyTag(), Tainter.getTag(Double.valueOf(8.0).doubleValue()));
    }
}
