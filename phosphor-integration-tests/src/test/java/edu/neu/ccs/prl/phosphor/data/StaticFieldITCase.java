package edu.neu.ccs.prl.phosphor.data;

import org.junit.jupiter.api.Test;

public class StaticFieldITCase {
    public static int x = 8;

    @Test
    void staticField() {
        // TODO
        x = 7;
    }
}
