package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
@SuppressWarnings({"UnnecessaryBoxing", "UnnecessaryUnboxing", "ConstantValue"})
public class BoxedTypeITCase {
    @Test
    void boxedBoolean(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        boolean value = manager.setLabels(true, expected);
        Boolean boxed = Boolean.valueOf(value);
        boolean unboxed = boxed.booleanValue();
        Assertions.assertTrue(unboxed);
        Object[] actual = manager.getLabels(unboxed);
        checker.check(expected, actual);
        checker.check(new Object[0], manager.getLabels(Boolean.valueOf(true).booleanValue()));
    }

    @Test
    void boxedByte(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        byte value = manager.setLabels((byte) 8, expected);
        Byte boxed = Byte.valueOf(value);
        byte unboxed = boxed.byteValue();
        Assertions.assertEquals((byte) 8, unboxed);
        Object[] actual = manager.getLabels(unboxed);
        checker.check(expected, actual);
        checker.check(new Object[0], manager.getLabels(Byte.valueOf((byte) 8).byteValue()));
    }

    @Test
    void boxedCharacter(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        char value = manager.setLabels('x', expected);
        Character boxed = Character.valueOf(value);
        char unboxed = boxed.charValue();
        Assertions.assertEquals('x', unboxed);
        Object[] actual = manager.getLabels(unboxed);
        checker.check(expected, actual);
        checker.check(new Object[0], manager.getLabels(Character.valueOf('x').charValue()));
    }

    @Test
    void boxedShort(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        short value = manager.setLabels((short) 8, expected);
        Short boxed = Short.valueOf(value);
        short unboxed = boxed.shortValue();
        Assertions.assertEquals((short) 8, unboxed);
        Object[] actual = manager.getLabels(unboxed);
        checker.check(expected, actual);
        checker.check(new Object[0], manager.getLabels(Short.valueOf((short) 8).shortValue()));
    }

    @Test
    void boxedInteger(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int value = manager.setLabels(8, expected);
        Integer boxed = Integer.valueOf(value);
        int unboxed = boxed.intValue();
        Assertions.assertEquals(8, unboxed);
        Object[] actual = manager.getLabels(unboxed);
        checker.check(expected, actual);
        checker.check(new Object[0], manager.getLabels(Integer.valueOf(8).intValue()));
    }

    @Test
    void boxedLong(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        long value = manager.setLabels(8L, expected);
        Long boxed = Long.valueOf(value);
        long unboxed = boxed.longValue();
        Assertions.assertEquals(8L, unboxed);
        Object[] actual = manager.getLabels(unboxed);
        checker.check(expected, actual);
        checker.check(new Object[0], manager.getLabels(Long.valueOf(8L).longValue()));
    }

    @Test
    void boxedFloat(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        float value = manager.setLabels(8f, expected);
        Float boxed = Float.valueOf(value);
        float unboxed = boxed.floatValue();
        Assertions.assertEquals(8, unboxed);
        Object[] actual = manager.getLabels(unboxed);
        checker.check(expected, actual);
        checker.check(new Object[0], manager.getLabels(Float.valueOf(8f).floatValue()));
    }

    @Test
    void boxedDouble(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        double value = manager.setLabels(8.0, expected);
        Double boxed = Double.valueOf(value);
        double unboxed = boxed.doubleValue();
        Assertions.assertEquals(8, unboxed);
        Object[] actual = manager.getLabels(unboxed);
        checker.check(expected, actual);
        checker.check(new Object[0], manager.getLabels(Double.valueOf(8.0).doubleValue()));
    }
}
