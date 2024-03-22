package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
@SuppressWarnings("MismatchedReadAndWriteOfArray")
public class ArrayLengthITCase {
    @Test
    void primitiveArrayLength(TagManager manager, FlowChecker checker) {
        int i = manager.setLabel(5, "label");
        int[] a = new int[i];
        Assertions.assertEquals(5, a.length);
        Object[] actual = manager.getLabels(a.length);
        checker.check(new Object[] {"label"}, actual);
        a = new int[5];
        checker.checkEmpty(manager.getLabels(a.length));
    }

    @Test
    void referenceArrayLength(TagManager manager, FlowChecker checker) {
        int i = manager.setLabel(5, "label");
        String[] a = new String[i];
        Assertions.assertEquals(5, a.length);
        Object[] actual = manager.getLabels(a.length);
        checker.check(new Object[] {"label"}, actual);
        a = new String[5];
        checker.checkEmpty(manager.getLabels(a.length));
    }

    @Test
    void multiDimensionalPrimitiveArrayLength(TagManager manager, FlowChecker checker) {
        Object[] tag1 = new Object[] {"label1"};
        Object[] tag3 = new Object[] {"label3"};
        int[][][] a = new int[manager.setLabels(2, tag1)][3][manager.setLabels(1, tag3)];
        Assertions.assertEquals(2, a.length);
        Object[] actual = manager.getLabels(a.length);
        checker.check(tag1, actual);
        for (int[][] x : a) {
            Assertions.assertEquals(3, x.length);
            checker.checkEmpty(manager.getLabels(x.length));
            for (int[] y : x) {
                Assertions.assertEquals(1, y.length);
                actual = manager.getLabels(y.length);
                checker.check(tag3, actual);
            }
        }
    }

    @Test
    void multiDimensionalPrimitiveArrayLengthJagged(TagManager manager, FlowChecker checker) {
        Object[] tag1 = new Object[] {"label1"};
        Object[] tag2 = new Object[] {"label2"};
        int[][][] a = new int[manager.setLabels(5, tag1)][manager.setLabels(6, tag2)][];
        Assertions.assertEquals(5, a.length);
        Object[] actual = manager.getLabels(a.length);
        checker.check(tag1, actual);
        for (int[][] x : a) {
            Assertions.assertEquals(6, x.length);
            actual = manager.getLabels(x.length);
            checker.check(tag2, actual);
        }
    }

    @Test
    void multiDimensionReferenceArrayLength(TagManager manager, FlowChecker checker) {
        Object[] tag1 = new Object[] {"label1"};
        Object[] tag2 = new Object[] {"label2"};
        String[][] a = new String[manager.setLabels(5, tag1)][manager.setLabels(6, tag2)];
        Assertions.assertEquals(5, a.length);
        Object[] actual = manager.getLabels(a.length);
        checker.check(tag1, actual);
        for (String[] x : a) {
            Assertions.assertEquals(6, x.length);
            actual = manager.getLabels(x.length);
            checker.check(tag2, actual);
        }
    }
}
