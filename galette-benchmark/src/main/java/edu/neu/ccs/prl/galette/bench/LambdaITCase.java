package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.function.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class LambdaITCase {
    @Test
    void constructorReference(TagManager manager, FlowChecker checker) {
        Object[] labels = {"lambda"};
        BiFunction<Integer, Short, Example> f = Example::new;
        int p = 8;
        short r = manager.setLabels((short) 4, labels);
        Example e = f.apply(p, r);
        Assertions.assertEquals(p, e.p);
        Assertions.assertEquals(r, e.r);
        checker.checkEmpty(manager.getLabels(p));
        checker.check(labels, manager.getLabels(r));
    }

    @Test
    void unboundInstanceMethodReference(TagManager manager, FlowChecker checker) {
        Object[] labels = {"lambda"};
        ObjLongConsumer<Example> f = Example::setR;
        Example e = new Example(0, (short) 0);
        long r = manager.setLabels(2, labels);
        f.accept(e, r);
        Assertions.assertEquals(r, e.r);
        checker.check(labels, manager.getLabels(r));
    }

    @Test
    void boundInstanceMethodReference(TagManager manager, FlowChecker checker) {
        Object[] labels1 = {"lambda1"};
        Object[] labels2 = {"lambda2"};
        int p = manager.setLabels(8, labels1);
        Example e = new Example(p, (short) 0);
        IntToDoubleFunction f = e::withP;
        int x = manager.setLabels(5, labels2);
        double result = f.applyAsDouble(x);
        Assertions.assertEquals(p + x, result);
        checker.check(new Object[] {"lambda1", "lambda2"}, manager.getLabels(result));
    }

    @Test
    void staticMethodReference(TagManager manager, FlowChecker checker) {
        Object[] labels = {"lambda"};
        DoubleSupplier f = Example::getQ;
        double expected = manager.setLabels(44.7, labels);
        Example.q = expected;
        double actual = f.getAsDouble();
        Assertions.assertEquals(expected, actual);
        checker.check(labels, manager.getLabels(actual));
    }

    @Test
    void methodReferenceWithCast(TagManager manager, FlowChecker checker) {
        Object[] labels = {"lambda"};
        float expected = manager.setLabels(3.3f, labels);
        Example.f = expected;
        DoubleSupplier supplier = Example::getF;
        double actual = supplier.getAsDouble();
        Assertions.assertEquals(expected, actual);
        checker.check(labels, manager.getLabels(actual));
    }

    @Test
    void capturedArray(TagManager manager, FlowChecker checker) {
        Object[] labels1 = {"lambda1"};
        Object[] labels2 = {"lambda2"};
        int[] i = new int[1];
        i[0] = manager.setLabels(0, labels1);
        Runnable r = () -> i[0] += manager.setLabels(10, labels2);
        r.run();
        int actual = i[0];
        Assertions.assertEquals(10, actual);
        checker.check(new Object[] {"lambda1", "lambda2"}, manager.getLabels(actual));
    }

    @Test
    void boxingLambda(TagManager manager, FlowChecker checker) {
        BinaryOperator<Integer> sum = Integer::sum;
        Object[] labels = {"lambda"};
        int actual = sum.apply(8, manager.setLabels(5, labels));
        Assertions.assertEquals(13, actual);
        checker.check(labels, manager.getLabels(actual));
    }

    static class Example {
        private final int p;
        private short r;
        private static double q;
        private static float f;

        Example(int p, short r) {
            this.p = p;
            this.r = r;
        }

        double withP(int x) {
            return x + p;
        }

        void setR(long a) {
            r = (short) a;
        }

        static double getQ() {
            return q;
        }

        static float getF() {
            return f;
        }
    }
}
