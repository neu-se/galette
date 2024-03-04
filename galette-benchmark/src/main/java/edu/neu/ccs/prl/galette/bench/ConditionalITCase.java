package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class ConditionalITCase {
    @Test
    void ifBranchTaken(TagManager manager, FlowChecker checker) {
        int i = manager.setLabels(0, new Object[] {"i"});
        int k = 1;
        if (i == 0) {
            k = i;
        }
        Assertions.assertEquals(k, 0);
        checker.check(new Object[] {"i"}, manager.getLabels(k));
    }

    @Test
    void ifBranchNotTaken(TagManager manager, FlowChecker checker) {
        int i = manager.setLabels(0, new Object[] {"i"});
        int k = 1;
        if (i != 0) {
            k = i;
        }
        Assertions.assertEquals(k, 1);
        checker.check(new Object[] {}, manager.getLabels(k));
    }

    @Test
    void switchNonDefaultCase(TagManager manager, FlowChecker checker) {
        int i = manager.setLabels(0, new Object[] {"i"});
        int k;
        switch (i) {
            case 0:
            case 1:
                k = i;
                break;
            default:
                k = 7;
        }
        Assertions.assertEquals(k, 0);
        checker.check(new Object[] {"i"}, manager.getLabels(k));
    }

    @Test
    void switchDefaultCase(TagManager manager, FlowChecker checker) {
        int i = manager.setLabels(0, new Object[] {"i"});
        int k;
        switch (i) {
            case 1:
            case 2:
                k = i;
                break;
            default:
                k = 7;
        }
        Assertions.assertEquals(k, 7);
        checker.check(new Object[] {}, manager.getLabels(k));
    }
}
