package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class LoopITCase {
    @Test
    void forLoop(TagManager manager, FlowChecker checker) {
        // Find the index of the minimum value in an array using a standard for loop
        int[] a = new int[] {8, 10, 16, 32, -9, 14, 0, 1, 6};
        BenchUtil.taintWithIndices(manager, a);
        int index = 0;
        for (int i = 1; i < a.length; i++) {
            if (a[index] > a[i]) {
                index = i;
            }
        }
        int min = a[index];
        Assertions.assertEquals(-9, min);
        checker.check(new Object[] {"4"}, manager.getLabels(min));
    }

    @Test
    void forEachLoop(TagManager manager, FlowChecker checker) {
        // Find the maximum value in an array using a for-each loop
        int[] a = new int[] {8, 10, 16, 32, -9, 14, 0, 1, 6};
        BenchUtil.taintWithIndices(manager, a);
        int max = Integer.MIN_VALUE;
        for (int i : a) {
            max = Math.max(max, i);
        }
        Assertions.assertEquals(32, max);
        checker.check(new Object[] {"3"}, manager.getLabels(max));
    }

    @Test
    void whileLoop(TagManager manager, FlowChecker checker) {
        // Find the sum of the values in an array using a while loop
        int[] a = new int[] {8, 10, 16, 32, -9, 14, 0, 1, 6};
        BenchUtil.taintWithIndices(manager, a);
        int sum = 0;
        int i = 0;
        while (i < a.length) {
            sum += a[i++];
        }
        Assertions.assertEquals(78, sum);
        Object[] labels = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        checker.check(labels, manager.getLabels(sum));
    }

    @Test
    void doWhileLoop(TagManager manager, FlowChecker checker) {
        int[] a = new int[] {8, 10, 16, 32, -9, 14, 0, 1, 6};
        BenchUtil.taintWithIndices(manager, a);
        int x;
        int i = 0;
        do {
            x = a[i++];
        } while (x != 0);
        Assertions.assertEquals(0, x);
        checker.check(new Object[] {"6"}, manager.getLabels(x));
    }
}
