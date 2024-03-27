package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@FlowBench
public class ThrowableITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    @ParameterizedTest(name = "thrownException(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void caughtThrownException(boolean taintValue) {
        RuntimeException e = new RuntimeException();
        if (taintValue) {
            e = manager.setLabel(e, "exception");
        }
        try {
            throw e;
        } catch (Exception e2) {
            Assertions.assertEquals(e, e2);
            if (taintValue) {
                checker.check(new Object[] {"exception"}, manager.getLabels(e2));
            } else {
                checker.checkEmpty(manager.getLabels(e2));
            }
        }
    }

    @SuppressWarnings("CaughtExceptionImmediatelyRethrown")
    private void rethrow(RuntimeException e) {
        try {
            throw e;
        } catch (Exception e2) {
            throw e2;
        }
    }

    @ParameterizedTest(name = "rethrownException(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void rethrownException(boolean taintValue) {
        RuntimeException e = new RuntimeException();
        if (taintValue) {
            e = manager.setLabel(e, "exception");
        }
        try {
            rethrow(e);
        } catch (Exception e2) {
            Assertions.assertEquals(e, e2);
            if (taintValue) {
                checker.check(new Object[] {"exception"}, manager.getLabels(e2));
            } else {
                checker.checkEmpty(manager.getLabels(e2));
            }
        }
    }

    @Test
    void throwCausesNullPointerException() {
        RuntimeException e = manager.setLabel(null, "exception");
        try {
            throw e;
        } catch (Exception e2) {
            Assertions.assertNotEquals(e, e2);
            checker.checkEmpty(manager.getLabels(e2));
        }
    }
}
