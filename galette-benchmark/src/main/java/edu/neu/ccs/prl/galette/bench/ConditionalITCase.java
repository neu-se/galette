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
        int j = 2;
        if (i == 0) {
            k = i;
        } else {
            j = i;
        }
        Assertions.assertEquals(k, 0);
        Assertions.assertEquals(j, 2);
        checker.check(new Object[] {"i"}, manager.getLabels(k));
        checker.checkEmpty(manager.getLabels(j));
    }

    @Test
    void ifBranchNotTaken(TagManager manager, FlowChecker checker) {
        int i = manager.setLabels(0, new Object[] {"i"});
        int k = 1;
        int j = 2;
        if (i != 0) {
            k = i;
        } else {
            j = i;
        }
        Assertions.assertEquals(k, 1);
        Assertions.assertEquals(j, 0);
        checker.checkEmpty(manager.getLabels(k));
        checker.check(new Object[] {"i"}, manager.getLabels(j));
    }

    @Test
    void switchNonDefaultCase(TagManager manager, FlowChecker checker) {
        int i = manager.setLabels(0, new Object[] {"i"});
        int k;
        int j;
        switch (i) {
            case 0:
            case 1:
                k = i;
                j = 1;
                break;
            default:
                k = 7;
                j = i;
        }
        Assertions.assertEquals(k, 0);
        Assertions.assertEquals(j, 1);
        checker.check(new Object[] {"i"}, manager.getLabels(k));
        checker.checkEmpty(manager.getLabels(j));
    }

    @Test
    void switchDefaultCase(TagManager manager, FlowChecker checker) {
        int i = manager.setLabels(0, new Object[] {"i"});
        int k;
        int j;
        switch (i) {
            case 1:
            case 2:
                k = i;
                j = 1;
                break;
            default:
                k = 7;
                j = i;
        }
        Assertions.assertEquals(k, 7);
        Assertions.assertEquals(j, 0);
        checker.checkEmpty(manager.getLabels(k));
        checker.check(new Object[] {"i"}, manager.getLabels(j));
    }
}
