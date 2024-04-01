package edu.neu.ccs.prl.galette.bench;

import static edu.neu.ccs.prl.galette.bench.ConcatAdapter.OPERAND;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@FlowBench
@SuppressWarnings({"ConstantValue", "DuplicatedCode"})
public abstract class StringConcatITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    abstract ConcatAdapter getAdapter();

    @ParameterizedTest(name = "concatWithBoolean(taintValue={0}, prefix={1})")
    @MethodSource("arguments")
    void concatWithBoolean(boolean taintValue, boolean prefix) {
        boolean x = true;
        if (taintValue) {
            x = manager.setLabel(x, "value");
        }
        String actual = getAdapter().concat(x, prefix);
        String expected = prefix ? "<OPERAND>true" : "true<OPERAND>";
        Assertions.assertEquals(expected, actual);
        checkSuffixLabels(actual, taintValue, prefix, new Object[] {"value"});
    }

    @ParameterizedTest(name = "concatWithByte(taintValue={0}, prefix={1})")
    @MethodSource("arguments")
    void concatWithByte(boolean taintValue, boolean prefix) {
        byte x = 7;
        if (taintValue) {
            x = manager.setLabel(x, "value");
        }
        String actual = getAdapter().concat(x, prefix);
        String expected = prefix ? "<OPERAND>7" : "7<OPERAND>";
        Assertions.assertEquals(expected, actual);
        checkSuffixLabels(actual, taintValue, prefix, new Object[] {"value"});
    }

    @ParameterizedTest(name = "concatWithChar(taintValue={0}, prefix={1})")
    @MethodSource("arguments")
    void concatWithChar(boolean taintValue, boolean prefix) {
        char x = 'a';
        if (taintValue) {
            x = manager.setLabel(x, "value");
        }
        String actual = getAdapter().concat(x, prefix);
        String expected = prefix ? "<OPERAND>a" : "a<OPERAND>";
        Assertions.assertEquals(expected, actual);
        checkSuffixLabels(actual, taintValue, prefix, new Object[] {"value"});
    }

    @ParameterizedTest(name = "concatWithShort(taintValue={0}, prefix={1})")
    @MethodSource("arguments")
    void concatWithShort(boolean taintValue, boolean prefix) {
        short x = 7;
        if (taintValue) {
            x = manager.setLabel(x, "value");
        }
        String actual = getAdapter().concat(x, prefix);
        String expected = prefix ? "<OPERAND>7" : "7<OPERAND>";
        Assertions.assertEquals(expected, actual);
        checkSuffixLabels(actual, taintValue, prefix, new Object[] {"value"});
    }

    @ParameterizedTest(name = "concatWithInt(taintValue={0}, prefix={1})")
    @MethodSource("arguments")
    void concatWithInt(boolean taintValue, boolean prefix) {
        int x = 7;
        if (taintValue) {
            x = manager.setLabel(x, "value");
        }
        String actual = getAdapter().concat(x, prefix);
        String expected = prefix ? "<OPERAND>7" : "7<OPERAND>";
        Assertions.assertEquals(expected, actual);
        checkSuffixLabels(actual, taintValue, prefix, new Object[] {"value"});
    }

    @ParameterizedTest(name = "concatWithLong(taintValue={0}, prefix={1})")
    @MethodSource("arguments")
    void concatWithLong(boolean taintValue, boolean prefix) {
        long x = 7;
        if (taintValue) {
            x = manager.setLabel(x, "value");
        }
        String actual = getAdapter().concat(x, prefix);
        String expected = prefix ? "<OPERAND>7" : "7<OPERAND>";
        Assertions.assertEquals(expected, actual);
        checkSuffixLabels(actual, taintValue, prefix, new Object[] {"value"});
    }

    @ParameterizedTest(name = "concatWithFloat(taintValue={0}, prefix={1})")
    @MethodSource("arguments")
    void concatWithFloat(boolean taintValue, boolean prefix) {
        float x = 7;
        if (taintValue) {
            x = manager.setLabel(x, "value");
        }
        String actual = getAdapter().concat(x, prefix);
        String expected = prefix ? "<OPERAND>7.0" : "7.0<OPERAND>";
        Assertions.assertEquals(expected, actual);
        checkSuffixLabels(actual, taintValue, prefix, new Object[] {"value"});
    }

    @ParameterizedTest(name = "concatWithDouble(taintValue={0}, prefix={1})")
    @MethodSource("arguments")
    void concatWithDouble(boolean taintValue, boolean prefix) {
        double x = 7;
        if (taintValue) {
            x = manager.setLabel(x, "value");
        }
        String actual = getAdapter().concat(x, prefix);
        String expected = prefix ? "<OPERAND>7.0" : "7.0<OPERAND>";
        Assertions.assertEquals(expected, actual);
        checkSuffixLabels(actual, taintValue, prefix, new Object[] {"value"});
    }

    @ParameterizedTest(name = "concatWithObject(taintValue={0}, prefix={1})")
    @MethodSource("arguments")
    void concatWithObject(boolean taintValue, boolean prefix) {
        String x = "hello";
        if (taintValue) {
            x = BenchUtil.taintCharacters(manager, x, "value");
        }
        String actual = getAdapter().concat(x, prefix);
        String expected = prefix ? "<OPERAND>hello" : "hello<OPERAND>";
        Assertions.assertEquals(expected, actual);
        checkSuffixLabels(actual, taintValue, prefix, new Object[] {"value"});
    }

    @Test
    void concatMany() {
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] values = expected.toCharArray();
        BenchUtil.taintWithIndices(manager, values);
        String actual = getAdapter().concatMany(values);
        Assertions.assertEquals(expected, actual);
        for (int i = 0; i < actual.length(); i++) {
            char c = actual.charAt(i);
            checker.check(new Object[] {String.valueOf(i)}, manager.getLabels(c));
        }
    }

    private void checkSuffixLabels(String s, boolean taintValue, boolean prefix, Object[] labels) {
        int start;
        int end;
        if (prefix) {
            start = OPERAND.length();
            end = s.length();
        } else {
            start = 0;
            end = s.length() - OPERAND.length();
        }
        for (int i = start; i < end; i++) {
            char c = s.charAt(i);
            if (taintValue) {
                checker.check(labels, manager.getLabels(c));
            } else {
                checker.checkEmpty(manager.getLabels(c));
            }
        }
    }

    static Stream<Arguments> arguments() {
        return BenchUtil.cartesianProduct(new Boolean[] {true, false}, new Boolean[] {true, false});
    }
}
