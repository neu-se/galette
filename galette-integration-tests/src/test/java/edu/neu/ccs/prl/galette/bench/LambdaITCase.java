package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.extension.FlowBench;
import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class LambdaITCase {
    @Test
    void virtualMethodReference() {
        String s = "hello";
        Supplier<Integer> supplier = s::length;
        int actual = supplier.get();
        Assertions.assertEquals(s.length(), actual);
    }
}
