package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

@FlowBench
@EnabledForJreRange(min = JRE.JAVA_9)
@Disabled("Unimplemented")
public class JdkUnsafeITCase {
    private final UnsafeWrapper unsafe = new JdkUnsafeWrapper();

    @Test
    void compareAndSetInt(TagManager manager, FlowChecker checker) throws ReflectiveOperationException {
        for (boolean successful : new boolean[] {true, false}) {
            for (boolean taint : new boolean[] {true, false}) {
                for (UnsafeLocation location : UnsafeLocation.values()) {
                    compareAndSetInt(manager, checker, successful, taint, location);
                }
            }
        }
    }

    private void compareAndSetInt(
            TagManager manager, FlowChecker checker, boolean successful, boolean taint, UnsafeLocation location)
            throws ReflectiveOperationException {
        // JDK 11, 17, 21
        Holder holder = new Holder(manager, !taint);
        int original = 7;
        int expected = successful ? original : original + 50;
        int update = taint ? manager.setLabels(9, new Object[] {"update"}) : 9;
        boolean result = unsafe.compareAndSwapInt(
                location.getBase(unsafe, holder, int.class), location.getOffset(unsafe, int.class), expected, update);
        Assertions.assertEquals(successful, result);
        int actual = location.getIntValue(holder);
        Assertions.assertEquals(successful ? update : original, actual);
        if (taint) {
            checker.check(new Object[] {"update"}, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }
}
