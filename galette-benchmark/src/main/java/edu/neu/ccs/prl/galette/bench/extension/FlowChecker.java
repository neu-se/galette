package edu.neu.ccs.prl.galette.bench.extension;

import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

public class FlowChecker {
    private int truePositives;
    private int falsePositives;
    private int falseNegatives;
    private final List<AssertionFailedError> failures = new LinkedList<>();

    public void check(Set<?> expected, Set<?> actual) {
        for (Object label : expected) {
            if (actual.contains(label)) {
                truePositives++;
            } else {
                falseNegatives++;
            }
        }
        for (Object label : actual) {
            if (!expected.contains(label)) {
                falsePositives++;
            }
        }
        try {
            Assertions.assertEquals(expected, actual);
        } catch (AssertionFailedError e) {
            failures.add(e);
        }
    }

    public void check(Object[] expected, Object[] actual) {
        check(new HashSet<>(Arrays.asList(expected)), new HashSet<>(Arrays.asList(actual)));
    }

    int getTruePositives() {
        return truePositives;
    }

    int getFalsePositives() {
        return falsePositives;
    }

    int getFalseNegatives() {
        return falseNegatives;
    }

    List<AssertionFailedError> getFailures() {
        return failures;
    }
}
