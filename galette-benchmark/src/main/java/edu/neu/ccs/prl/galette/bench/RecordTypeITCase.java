package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

@FlowBench
@EnabledForJreRange(min = JRE.JAVA_16)
public class RecordTypeITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    void toStringInt() {}

    void hashCodeInt() {}

    void localRecord() {
        record IntLongPair(int x, long y) {}
        int x = 7;
        long y = 40L;
        IntLongPair pair = new IntLongPair(x, y);
        pair.x();
    }

    record BooleanRecord(boolean value) {}

    record ByteRecord(byte value) {}

    record CharRecord(char value) {}

    record FloatRecord(float value) {}

    record DoubleRecord(double value) {}

    record IntRecord(int value) {}

    record LongRecord(long value) {}

    record ShortRecord(short value) {}

    record ObjectRecord(Object value) {}

    record IntArrayRecord(int[] value) {}
}
