package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class FieldITCase {
    public static int x = 8;
    public int y = 99;

    @Test
    void staticField(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        FieldITCase.x = manager.setLabels(7, expected);
        int value = FieldITCase.x;
        Assertions.assertEquals(7, value);
        Object[] actual = manager.getLabels(value);
        checker.check(expected, actual);
    }

    @Test
    void instanceField(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        this.y = manager.setLabels(77, expected);
        int value = this.y;
        Assertions.assertEquals(77, value);
        Object[] actual = manager.getLabels(value);
        checker.check(expected, actual);
    }

    @Test
    void inheritedField(TagManager manager, FlowChecker checker) {
        // TODO
    }
}
