package edu.neu.ccs.prl.phosphor.all;

import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LambdaITCase {
    @Test
    void virtualMethodReference() {
        String s = "hello";
        Supplier<Integer> supplier = s::length;
        int actual = supplier.get();
        Assertions.assertEquals(s.length(), actual);
    }
}
