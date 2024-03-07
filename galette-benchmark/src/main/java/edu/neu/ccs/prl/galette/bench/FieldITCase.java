package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@FlowBench
public class FieldITCase {
    @AfterEach
    void reset() {
        A.x = 0;
        A.y = 0;
        B.y = 0;
    }

    @Test
    void staticField(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"field"};
        A.x = manager.setLabels(8, expected);
        int value = A.x;
        Assertions.assertEquals(8, value);
        checker.check(expected, manager.getLabels(value));
    }

    @Test
    void instanceField(TagManager manager, FlowChecker checker) {
        Object[] expected = new Object[] {"field"};
        A a = new A();
        a.a = manager.setLabels(8, expected);
        int value = a.a;
        Assertions.assertEquals(8, value);
        checker.check(expected, manager.getLabels(value));
    }

    @Test
    void inheritedStaticField(TagManager manager, FlowChecker checker) {
        // Since the field "x" is not declared in B,
        // B.x and A.x refer to the same value
        Object[] expected = new Object[] {"field"};
        B.x = manager.setLabels(8, expected);
        int value1 = A.x;
        int value2 = B.x;
        Assertions.assertEquals(8, value1);
        checker.check(expected, manager.getLabels(value1));
        Assertions.assertEquals(8, value2);
        checker.check(expected, manager.getLabels(value2));
    }

    @Test
    void inheritedInstanceField(TagManager manager, FlowChecker checker) {
        // Since the field "a" is not declared in B,
        // B.a and A.a refer to the same value
        Object[] expected = new Object[] {"field"};
        B b = new B();
        b.a = manager.setLabels(8, expected);
        @SuppressWarnings("UnnecessaryLocalVariable")
        A a = b;
        int value1 = a.a;
        int value2 = b.a;
        Assertions.assertEquals(8, value1);
        checker.check(expected, manager.getLabels(value1));
        Assertions.assertEquals(8, value2);
        checker.check(expected, manager.getLabels(value2));
    }

    @Test
    void shadowedStaticField(TagManager manager, FlowChecker checker) {
        // Since the field "y" is declared in B,
        // A.y and B.y refer to different values
        Object[] expected1 = new Object[] {"field1"};
        Object[] expected2 = new Object[] {"field2"};
        A.y = manager.setLabels(8, expected1);
        B.y = manager.setLabels(99, expected2);
        int value1 = A.y;
        int value2 = B.y;
        Assertions.assertEquals(8, value1);
        checker.check(expected1, manager.getLabels(value1));
        Assertions.assertEquals(99, value2);
        checker.check(expected2, manager.getLabels(value2));
    }

    @Test
    void shadowedInstanceField(TagManager manager, FlowChecker checker) {
        // Since the field "b" is declared in B,
        // A.b and B.b refer to different values
        Object[] expected1 = new Object[] {"field"};
        Object[] expected2 = new Object[] {"field2"};
        B b = new B();
        ((A) b).b = manager.setLabels(8, expected1);
        b.b = manager.setLabels(99, expected2);
        int value1 = ((A) b).b;
        int value2 = b.b;
        Assertions.assertEquals(8, value1);
        checker.check(expected1, manager.getLabels(value1));
        Assertions.assertEquals(99, value2);
        checker.check(expected2, manager.getLabels(value2));
    }

    static class A {
        static int x;
        static int y;
        int a;
        int b;
    }

    static class B extends A {
        static int y;
        int b;
    }
}
