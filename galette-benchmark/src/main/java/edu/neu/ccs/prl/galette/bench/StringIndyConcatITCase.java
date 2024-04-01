package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Copy of {@link StringBuilderConcatITCase}.
 * This class should be compiled with target version of Java 9+ to ensure that invoke dynamic instructions
 * are used for string concatenation.
 */
@FlowBench
@EnabledForJreRange(min = JRE.JAVA_11)
@SuppressWarnings({"ConstantValue", "DuplicatedCode"})
public class StringIndyConcatITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    private static final String OPERAND = "<OPERAND>";

    @ParameterizedTest(name = "concatWithBoolean(taintValue={0}, prefix={1})")
    @MethodSource("arguments")
    void concatWithBoolean(boolean taintValue, boolean prefix) {
        boolean x = true;
        if (taintValue) {
            x = manager.setLabel(x, "value");
        }
        String actual = prefix ? OPERAND + x : x + OPERAND;
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
        String actual = prefix ? OPERAND + x : x + OPERAND;
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
        String actual = prefix ? OPERAND + x : x + OPERAND;
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
        String actual = prefix ? OPERAND + x : x + OPERAND;
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
        String actual = prefix ? OPERAND + x : x + OPERAND;
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
        String actual = prefix ? OPERAND + x : x + OPERAND;
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
        String actual = prefix ? OPERAND + x : x + OPERAND;
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
        String actual = prefix ? OPERAND + x : x + OPERAND;
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
        String actual = prefix ? OPERAND + x : x + OPERAND;
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
        String actual = "" + values[0] + values[1] + values[2] + values[3] + values[4] + values[5] + values[6]
                + values[7] + values[8] + values[9] + values[10] + values[11] + values[12] + values[13] + values[14]
                + values[15] + values[16] + values[17] + values[18] + values[19] + values[20] + values[21] + values[22]
                + values[23] + values[24] + values[25] + values[26] + values[27] + values[28] + values[29] + values[30]
                + values[31] + values[32] + values[33] + values[34] + values[35] + values[36] + values[37] + values[38]
                + values[39] + values[40] + values[41] + values[42] + values[43] + values[44] + values[45] + values[46]
                + values[47] + values[48] + values[49] + values[50] + values[51] + values[52] + values[53] + values[54]
                + values[55] + values[56] + values[57] + values[58] + values[59] + values[60] + values[61] + values[62]
                + values[63] + values[64] + values[65] + values[66] + values[67] + values[68] + values[69] + values[70]
                + values[71] + values[72] + values[73] + values[74] + values[75] + values[76] + values[77] + values[78]
                + values[79] + values[80] + values[81] + values[82] + values[83] + values[84] + values[85] + values[86]
                + values[87] + values[88] + values[89] + values[90] + values[91] + values[92] + values[93] + values[94]
                + values[95] + values[96] + values[97] + values[98] + values[99] + values[100] + values[101]
                + values[102] + values[103] + values[104] + values[105] + values[106] + values[107] + values[108]
                + values[109] + values[110] + values[111] + values[112] + values[113] + values[114] + values[115]
                + values[116] + values[117] + values[118] + values[119] + values[120] + values[121] + values[122]
                + values[123] + values[124] + values[125] + values[126] + values[127] + values[128] + values[129];
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
