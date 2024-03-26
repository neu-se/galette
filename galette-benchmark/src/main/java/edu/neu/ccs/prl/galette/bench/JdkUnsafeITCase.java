package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@FlowBench
@EnabledForJreRange(min = JRE.JAVA_9)
public class JdkUnsafeITCase extends UnsafeBaseITCase {
    @ParameterizedTest(name = "compareAndExchangeInt(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndExchangeInt(boolean compareSucceeds, boolean taintValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        int original = location.getCategory().getInt(holder);
        int expected = compareSucceeds ? original : original + 50;
        int update = taintValue ? manager.setLabels(9, new Object[] {"update"}) : 9;
        int witness = getUnsafe()
                .compareAndExchangeInt(
                        location.getBase(getUnsafe(), holder, int.class),
                        location.getOffset(getUnsafe(), int.class),
                        expected,
                        update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        checkWitnessLabels(taintValue, location, int.class, manager.getLabels(witness));
        int actual = location.getCategory().getInt(holder);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, int.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndExchangeLong(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndExchangeLong(boolean compareSucceeds, boolean taintValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        long original = location.getCategory().getLong(holder);
        long expected = compareSucceeds ? original : original + 50;
        long update = taintValue ? manager.setLabels(9L, new Object[] {"update"}) : 9L;
        long witness = getUnsafe()
                .compareAndExchangeLong(
                        location.getBase(getUnsafe(), holder, long.class),
                        location.getOffset(getUnsafe(), long.class),
                        expected,
                        update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        checkWitnessLabels(taintValue, location, long.class, manager.getLabels(witness));
        long actual = location.getCategory().getLong(holder);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, long.class, manager.getLabels(actual));
    }

    @ParameterizedTest(name = "compareAndExchangeObject(compareSucceeds={0}, taintValue={1}, location={2})")
    @MethodSource("compareAndSwapArguments")
    void compareAndExchangeObject(boolean compareSucceeds, boolean taintValue, UnsafeLocation location)
            throws ReflectiveOperationException {
        Holder holder = new Holder(manager, !taintValue);
        Object original = location.getCategory().getObject(holder);
        Object expected = compareSucceeds ? original : new Object();
        Object update = taintValue ? manager.setLabels("hello", new Object[] {"update"}) : "hello";
        Object witness = getUnsafe()
                .compareAndExchangeObject(
                        location.getBase(getUnsafe(), holder, Object.class),
                        location.getOffset(getUnsafe(), Object.class),
                        expected,
                        update);
        Assertions.assertEquals(compareSucceeds, expected == witness);
        checkWitnessLabels(taintValue, location, Object.class, manager.getLabels(witness));
        Object actual = location.getCategory().getObject(holder);
        checkCompareAndSwapLabels(compareSucceeds, taintValue, location, Object.class, manager.getLabels(actual));
    }

    @Override
    UnsafeAdapter getUnsafe() {
        return new JdkUnsafeAdapter();
    }
}
