package edu.neu.ccs.prl.galette.bench;

import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

@EnabledForJreRange(min = JRE.JAVA_11)
public class StringIndyConcatITCase extends StringConcatITCase {
    @Override
    ConcatAdapter getAdapter() {
        return new IndyConcatAdapter();
    }
}
