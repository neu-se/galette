package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.extension.FlowBench;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class FieldITCase {
    public static int x = 8;
    public int y = 99;

    @Test
    void staticField() {
        Tag expected = Tag.of("label");
        FieldITCase.x = Tainter.setTag(7, expected);
        int value = FieldITCase.x;
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void instanceField() {
        Tag expected = Tag.of("label");
        this.y = Tainter.setTag(77, expected);
        int value = this.y;
        Tag actual = Tainter.getTag(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void inheritedField() {
        // TODO
    }
}
