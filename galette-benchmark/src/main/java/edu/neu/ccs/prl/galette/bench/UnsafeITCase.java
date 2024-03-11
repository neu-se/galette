package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@FlowBench
@Disabled("Unimplemented")
// @EnabledForJreRange(min = JRE.JAVA_9)
public class UnsafeITCase {
    private final UnsafeWrapper unsafe =
            JRE.currentVersion() == JRE.JAVA_8 ? new SunUnsafeWrapper() : new JdkUnsafeWrapper();

    @SuppressWarnings("unused")
    private TagManager manager;

    @SuppressWarnings("unused")
    private FlowChecker checker;

    @ParameterizedTest(name = "compareSucceeds={0},taintedValue={1},locationType={2}")
    @MethodSource("compareAndSwapIntArguments")
    void compareAndSwapInt(boolean compareSucceeds, boolean taintedValue, UnsafeLocation locationType)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintedValue);
        int original = 7;
        int expected = compareSucceeds ? original : original + 50;
        int update = taintedValue ? manager.setLabels(9, new Object[] {"update"}) : 9;
        boolean result = unsafe.compareAndSwapInt(
                locationType.getBase(unsafe, holder, int.class),
                locationType.getOffset(unsafe, int.class),
                expected,
                update);
        Assertions.assertEquals(compareSucceeds, result);
        int actual = locationType.getIntValue(holder);
        Assertions.assertEquals(compareSucceeds ? update : original, actual);
        if (taintedValue) {
            checker.check(new Object[] {"update"}, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    static Stream<Arguments> compareAndSwapIntArguments() {
        return BenchUtil.cartesianProduct(
                new Boolean[] {true, false}, new Boolean[] {true, false}, UnsafeLocation.values());
    }
}
