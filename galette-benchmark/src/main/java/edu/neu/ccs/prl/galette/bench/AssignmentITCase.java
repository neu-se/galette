package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
@SuppressWarnings("ConstantValue")
public class AssignmentITCase {
    @Test
    public void taintedObject(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        HashMap<Object, Object> item = new HashMap<>();
        item = manager.setLabels(item, expected);
        Assertions.assertEquals(new HashMap<>(), item);
        Object[] actual = manager.getLabels(item);
        checker.check(expected, actual);
    }

    @Test
    public void taintedArray(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int[] item = new int[2];
        item = manager.setLabels(item, expected);
        Assertions.assertArrayEquals(new int[2], item);
        Object[] actual = manager.getLabels(item);
        checker.check(expected, actual);
    }

    @Test
    public void taintedNull(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        Object item = manager.setLabels(null, expected);
        Assertions.assertNull(item);
        Object[] actual = manager.getLabels(item);
        checker.check(expected, actual);
    }

    @Test
    public void taintedBoolean(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        boolean item = manager.setLabels(true, expected);
        Assertions.assertTrue(item);
        Object[] actual = manager.getLabels(item);
        checker.check(expected, actual);
    }

    @Test
    public void taintedByte(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        byte item = 7;
        item = manager.setLabels(item, expected);
        Assertions.assertEquals(7, item);
        Object[] actual = manager.getLabels(item);
        checker.check(expected, actual);
    }

    @Test
    public void taintedChar(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        char item = 7;
        item = manager.setLabels(item, expected);
        Assertions.assertEquals(7, item);
        Object[] actual = manager.getLabels(item);
        checker.check(expected, actual);
    }

    @Test
    public void taintedShort(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        short item = 7;
        item = manager.setLabels(item, expected);
        Assertions.assertEquals(7, item);
        Object[] actual = manager.getLabels(item);
        checker.check(expected, actual);
    }

    @Test
    public void taintedInt(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int item = 7;
        item = manager.setLabels(item, expected);
        Assertions.assertEquals(7, item);
        Object[] actual = manager.getLabels(item);
        checker.check(expected, actual);
    }

    @Test
    public void taintedLong(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        long item = 7;
        item = manager.setLabels(item, expected);
        Assertions.assertEquals(7, item);
        Object[] actual = manager.getLabels(item);
        checker.check(expected, actual);
    }

    @Test
    public void taintedFloat(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        float item = 7;
        item = manager.setLabels(item, expected);
        Assertions.assertEquals(7, item);
        Object[] actual = manager.getLabels(item);
        checker.check(expected, actual);
    }

    @Test
    public void taintedDouble(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        double item = 7;
        item = manager.setLabels(item, expected);
        Assertions.assertEquals(7, item);
        Object[] actual = manager.getLabels(item);
        checker.check(expected, actual);
    }

    @Test
    public void duplicateValues(TagManager manager, FlowChecker checker) {
        Object[] tag1 = new Object[] {"label1"};
        Object[] tag2 = new Object[] {"label2"};
        int x = 5;
        int y = 5;
        int z = 5;
        x = manager.setLabels(x, tag1);
        z = manager.setLabels(z, tag2);
        Assertions.assertEquals(5, x);
        Assertions.assertEquals(5, y);
        Assertions.assertEquals(5, z);
        checker.check(tag1, manager.getLabels(x));
        checker.check(new Object[0], manager.getLabels(y));
        checker.check(tag2, manager.getLabels(z));
    }
}
