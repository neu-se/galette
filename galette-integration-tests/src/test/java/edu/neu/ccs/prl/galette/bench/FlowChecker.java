package edu.neu.ccs.prl.galette.bench;

import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

public class FlowChecker {
    private int truePositives;
    private int falsePositives;
    private int falseNegatives;
    private final List<AssertionFailedError> failures = new LinkedList<>();

    public void check(Set<?> expected, Set<?> predicted) {
        for (Object label : expected) {
            if (predicted.contains(label)) {
                truePositives++;
            } else {
                falseNegatives++;
            }
        }
        for (Object label : predicted) {
            if (!expected.contains(label)) {
                falsePositives++;
            }
        }
        try {
            Assertions.assertEquals(expected, predicted);
        } catch (AssertionFailedError e) {
            failures.add(e);
        }
    }

    public void check(Object[] expected, Object[] predicted) {
        check(new HashSet<>(Arrays.asList(expected)), new HashSet<>(Arrays.asList(predicted)));
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
