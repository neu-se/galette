package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@FlowBench
public class StringITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    @ParameterizedTest(name = "length(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void length(boolean taintValue) {
        int expected = taintValue ? manager.setLabel(3, "label") : 3;
        char[] values = new char[expected];
        values[0] = 'r';
        values[1] = 'e';
        values[2] = 'd';
        String s = new String(values);
        int actual = s.length();
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(new Object[] {"label"}, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "charAt(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void charAt(boolean taintValue) {
        char expected = taintValue ? manager.setLabel('a', "label") : 'a';
        char[] values = new char[] {'b', expected};
        String s = new String(values);
        char actual = s.charAt(1);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(new Object[] {"label"}, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "codePointAt(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void codePointAt(boolean taintValue) {
        char expected = taintValue ? manager.setLabel('a', "label") : 'a';
        char[] values = new char[] {'b', expected};
        String s = new String(values);
        int actual = s.codePointAt(1);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(new Object[] {"label"}, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "concat(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void concat(boolean taintValue) {
        String s1 = "hello ";
        String s2 = "world";
        if (taintValue) {
            s1 = BenchUtil.taintCharacters(manager, s1, "label1");
            s2 = BenchUtil.taintCharacters(manager, s2, "label2");
        }
        String actual = s1.concat(s2);
        Assertions.assertEquals("hello world", actual);
        checkLabels(actual, taintValue, 0, s1.length(), new Object[] {"label1"});
        checkLabels(actual, taintValue, s1.length(), actual.length(), new Object[] {"label2"});
    }

    @ParameterizedTest(name = "substring(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void substring(boolean taintValue) {
        String s = "hello world";
        if (taintValue) {
            s = BenchUtil.taintWithIndices(manager, s);
        }
        String actual = s.substring(2, 5);
        Assertions.assertEquals("llo", actual);
        for (int i = 0; i < actual.length(); i++) {
            Object[] labels = manager.getLabels(actual.charAt(i));
            if (taintValue) {
                checker.check(new Object[] {String.valueOf(i + 2)}, labels);
            } else {
                checker.checkEmpty(labels);
            }
        }
    }

    @ParameterizedTest(name = "replaceChar(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void replaceChar(boolean taintValue) {
        char expected = taintValue ? manager.setLabel('-', "label") : '-';
        String s = "hello world";
        char actual = s.replace(' ', expected).charAt(5);
        Assertions.assertEquals(expected, actual);
        if (taintValue) {
            checker.check(new Object[] {"label"}, manager.getLabels(actual));
        } else {
            checker.checkEmpty(manager.getLabels(actual));
        }
    }

    @ParameterizedTest(name = "replace(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void replace(boolean taintValue) {
        String s1 = "hello";
        String s2 = "world";
        if (taintValue) {
            s1 = BenchUtil.taintCharacters(manager, s1, "label1");
            s2 = BenchUtil.taintCharacters(manager, s2, "label2");
        }
        String actual = s1.replace("ll", s2);
        Assertions.assertEquals("heworldo", actual);
        checkLabels(actual, taintValue, 0, 2, new Object[] {"label1"});
        checkLabels(actual, taintValue, 2, 7, new Object[] {"label2"});
        checkLabels(actual, taintValue, 7, actual.length(), new Object[] {"label1"});
    }

    @ParameterizedTest(name = "split(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void split(boolean taintValue) {
        String s = "hello world";
        if (taintValue) {
            s = BenchUtil.taintWithIndices(manager, s);
        }
        String[] actual = s.split(" ");
        Assertions.assertArrayEquals(new String[] {"hello", "world"}, actual);
        String a1 = actual[0];
        for (int i = 0; i < a1.length(); i++) {
            Object[] labels = manager.getLabels(a1.charAt(i));
            if (taintValue) {
                checker.check(new Object[] {String.valueOf(i)}, labels);
            } else {
                checker.checkEmpty(labels);
            }
        }
        String a2 = actual[1];
        for (int i = 0; i < a2.length(); i++) {
            Object[] labels = manager.getLabels(a2.charAt(i));
            if (taintValue) {
                checker.check(new Object[] {String.valueOf(i + "hello ".length())}, labels);
            } else {
                checker.checkEmpty(labels);
            }
        }
    }

    @ParameterizedTest(name = "join(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void join(boolean taintValue) {
        String s1 = "hello";
        String s2 = "world";
        String delimiter = " ";
        if (taintValue) {
            s1 = BenchUtil.taintCharacters(manager, s1, "label1");
            s2 = BenchUtil.taintCharacters(manager, s2, "label3");
            delimiter = BenchUtil.taintCharacters(manager, delimiter, "label2");
        }
        String actual = String.join(delimiter, s1, s2);
        Assertions.assertEquals("hello world", actual);
        checkLabels(actual, taintValue, 0, s1.length(), new Object[] {"label1"});
        checkLabels(actual, taintValue, s1.length(), s1.length() + 1, new Object[] {"label2"});
        checkLabels(actual, taintValue, s1.length() + 1, actual.length(), new Object[] {"label3"});
    }

    @ParameterizedTest(name = "repeat(taintValue={0})")
    @ValueSource(booleans = {true, false})
    @EnabledForJreRange(min = JRE.JAVA_11)
    void repeat(boolean taintValue) {
        String s1 = "hello";
        if (taintValue) {
            s1 = BenchUtil.taintCharacters(manager, s1, "label1");
        }
        String actual = s1.repeat(2);
        Assertions.assertEquals("hellohello", actual);
        checkLabels(actual, taintValue, 0, actual.length(), new Object[] {"label1"});
    }

    @ParameterizedTest(name = "chars(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void chars(boolean taintValue) {
        String s = "hello";
        if (taintValue) {
            s = BenchUtil.taintWithIndices(manager, s);
        }
        int[] actual = s.chars().toArray();
        int[] expected = new int[] {'h', 'e', 'l', 'l', 'o'};
        Assertions.assertArrayEquals(expected, actual);
        for (int i = 0; i < actual.length; i++) {
            Object[] labels = manager.getLabels(actual[i]);
            if (taintValue) {
                checker.check(new Object[] {String.valueOf(i)}, labels);
            } else {
                checker.checkEmpty(labels);
            }
        }
    }

    @ParameterizedTest(name = "codePoints(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void codePoints(boolean taintValue) {
        String s = "hello";
        if (taintValue) {
            s = BenchUtil.taintWithIndices(manager, s);
        }
        int[] actual = s.codePoints().toArray();
        int[] expected = new int[] {'h', 'e', 'l', 'l', 'o'};
        Assertions.assertArrayEquals(expected, actual);
        for (int i = 0; i < actual.length; i++) {
            Object[] labels = manager.getLabels(actual[i]);
            if (taintValue) {
                checker.check(new Object[] {String.valueOf(i)}, labels);
            } else {
                checker.checkEmpty(labels);
            }
        }
    }

    @ParameterizedTest(name = "toUpperCase(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void toUpperCase(boolean taintValue) {
        String s = "hello";
        if (taintValue) {
            s = BenchUtil.taintWithIndices(manager, s);
        }
        String actual = s.toUpperCase();
        Assertions.assertEquals("HELLO", actual);
        for (int i = 0; i < actual.length(); i++) {
            Object[] labels = manager.getLabels(actual.charAt(i));
            if (taintValue) {
                checker.check(new Object[] {String.valueOf(i)}, labels);
            } else {
                checker.checkEmpty(labels);
            }
        }
    }

    @ParameterizedTest(name = "toLowerCase(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void toLowerCase(boolean taintValue) {
        String s = "HELLO";
        if (taintValue) {
            s = BenchUtil.taintWithIndices(manager, s);
        }
        String actual = s.toLowerCase();
        Assertions.assertEquals("hello", actual);
        for (int i = 0; i < actual.length(); i++) {
            Object[] labels = manager.getLabels(actual.charAt(i));
            if (taintValue) {
                checker.check(new Object[] {String.valueOf(i)}, labels);
            } else {
                checker.checkEmpty(labels);
            }
        }
    }

    private void checkLabels(String s, boolean taintValue, int start, int end, Object[] labels) {
        for (int i = start; i < end; i++) {
            char c = s.charAt(i);
            if (taintValue) {
                checker.check(labels, manager.getLabels(c));
            } else {
                checker.checkEmpty(manager.getLabels(c));
            }
        }
    }
}
