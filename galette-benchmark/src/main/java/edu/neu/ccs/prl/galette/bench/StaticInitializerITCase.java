package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class StaticInitializerITCase {
    @Test
    void staticInitializer(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"label"};
        int i = manager.setLabels(5, expected);
        int j = Example.addX(i);
        Assertions.assertEquals(12, j);
        Object[] actual = manager.getLabels(j);
        checker.check(expected, actual);
    }

    public static class Example {
        private static final int x;

        static {
            x = 7;
        }

        public static int addX(int y) {
            return x + y;
        }
    }
}
