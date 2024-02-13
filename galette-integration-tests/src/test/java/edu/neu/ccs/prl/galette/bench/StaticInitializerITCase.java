package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.extension.FlowBench;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class StaticInitializerITCase {
    @Test
    public void staticInitializer() {
        Tag expected = Tag.of("label");
        int i = Tainter.setTag(5, expected);
        int j = Example.addX(i);
        Tag actual = Tainter.getTag(j);
        Assertions.assertEquals(12, j);
        Assertions.assertEquals(expected, actual);
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
