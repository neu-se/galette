package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.RecordTypes.*;
import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import edu.neu.ccs.prl.galette.bench.extension.FlowChecker;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@FlowBench
@EnabledForJreRange(min = JRE.JAVA_16)
public class RecordTypeITCase {
    @SuppressWarnings("unused")
    TagManager manager;

    @SuppressWarnings("unused")
    FlowChecker checker;

    @ParameterizedTest(name = "getComponentBoolean(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentBoolean(boolean taintValue) {
        //noinspection SimplifiableConditionalExpression
        boolean value = taintValue ? manager.setLabel(true, "label") : true;
        BooleanRecord record = new BooleanRecord(value);
        boolean actual = record.value();
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "toStringBoolean(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void toStringBoolean(boolean taintValue) {
        //noinspection SimplifiableConditionalExpression
        boolean value = taintValue ? manager.setLabel(true, "label") : true;
        BooleanRecord record = new BooleanRecord(value);
        String actual = record.toString();
        Assertions.assertEquals("BooleanRecord[value=" + Objects.toString(value) + "]", actual);
        checkToStringLabels(actual, taintValue);
    }

    @ParameterizedTest(name = "hashCodeBoolean(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void hashCodeBoolean(boolean taintValue) {
        //noinspection SimplifiableConditionalExpression
        boolean value = taintValue ? manager.setLabel(true, "label") : true;
        BooleanRecord record = new BooleanRecord(value);
        int actual = record.hashCode();
        Assertions.assertEquals(Objects.hashCode(value), actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentBooleanArray(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentBooleanArray(boolean taintValue) {
        //noinspection SimplifiableConditionalExpression
        boolean value = taintValue ? manager.setLabel(true, "label") : true;
        BooleanArrayRecord record = new BooleanArrayRecord(new boolean[] {value});
        boolean actual = record.value()[0];
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentByte(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentByte(boolean taintValue) {
        byte value = taintValue ? manager.setLabel((byte) 5, "label") : 5;
        ByteRecord record = new ByteRecord(value);
        byte actual = record.value();
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "toStringByte(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void toStringByte(boolean taintValue) {
        byte value = taintValue ? manager.setLabel((byte) 5, "label") : 5;
        ByteRecord record = new ByteRecord(value);
        String actual = record.toString();
        Assertions.assertEquals("ByteRecord[value=" + Objects.toString(value) + "]", actual);
        checkToStringLabels(actual, taintValue);
    }

    @ParameterizedTest(name = "hashCodeByte(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void hashCodeByte(boolean taintValue) {
        byte value = taintValue ? manager.setLabel((byte) 5, "label") : 5;
        ByteRecord record = new ByteRecord(value);
        int actual = record.hashCode();
        Assertions.assertEquals(Objects.hashCode(value), actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentByteArray(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentByteArray(boolean taintValue) {
        byte value = taintValue ? manager.setLabel((byte) 5, "label") : 5;
        ByteArrayRecord record = new ByteArrayRecord(new byte[] {value});
        byte actual = record.value()[0];
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentChar(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentChar(boolean taintValue) {
        char value = taintValue ? manager.setLabel((char) 5, "label") : 5;
        CharRecord record = new CharRecord(value);
        char actual = record.value();
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "toStringChar(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void toStringChar(boolean taintValue) {
        char value = taintValue ? manager.setLabel((char) 5, "label") : 5;
        CharRecord record = new CharRecord(value);
        String actual = record.toString();
        Assertions.assertEquals("CharRecord[value=" + Objects.toString(value) + "]", actual);
        checkToStringLabels(actual, taintValue);
    }

    @ParameterizedTest(name = "hashCodeChar(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void hashCodeChar(boolean taintValue) {
        char value = taintValue ? manager.setLabel((char) 5, "label") : 5;
        CharRecord record = new CharRecord(value);
        int actual = record.hashCode();
        Assertions.assertEquals(Objects.hashCode(value), actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentCharArray(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentCharArray(boolean taintValue) {
        char value = taintValue ? manager.setLabel((char) 5, "label") : 5;
        CharArrayRecord record = new CharArrayRecord(new char[] {value});
        char actual = record.value()[0];
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentFloat(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentFloat(boolean taintValue) {
        float value = taintValue ? manager.setLabel((float) 5, "label") : 5;
        FloatRecord record = new FloatRecord(value);
        float actual = record.value();
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "toStringFloat(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void toStringFloat(boolean taintValue) {
        float value = taintValue ? manager.setLabel((float) 5, "label") : 5;
        FloatRecord record = new FloatRecord(value);
        String actual = record.toString();
        Assertions.assertEquals("FloatRecord[value=" + Objects.toString(value) + "]", actual);
        checkToStringLabels(actual, taintValue);
    }

    @ParameterizedTest(name = "hashCodeFloat(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void hashCodeFloat(boolean taintValue) {
        float value = taintValue ? manager.setLabel((float) 5, "label") : 5;
        FloatRecord record = new FloatRecord(value);
        int actual = record.hashCode();
        Assertions.assertEquals(Objects.hashCode(value), actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentFloatArray(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentFloatArray(boolean taintValue) {
        float value = taintValue ? manager.setLabel((float) 5, "label") : 5;
        FloatArrayRecord record = new FloatArrayRecord(new float[] {value});
        float actual = record.value()[0];
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentDouble(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentDouble(boolean taintValue) {
        double value = taintValue ? manager.setLabel((double) 5, "label") : 5;
        DoubleRecord record = new DoubleRecord(value);
        double actual = record.value();
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "toStringDouble(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void toStringDouble(boolean taintValue) {
        double value = taintValue ? manager.setLabel((double) 5, "label") : 5;
        DoubleRecord record = new DoubleRecord(value);
        String actual = record.toString();
        Assertions.assertEquals("DoubleRecord[value=" + Objects.toString(value) + "]", actual);
        checkToStringLabels(actual, taintValue);
    }

    @ParameterizedTest(name = "hashCodeDouble(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void hashCodeDouble(boolean taintValue) {
        double value = taintValue ? manager.setLabel((double) 5, "label") : 5;
        DoubleRecord record = new DoubleRecord(value);
        int actual = record.hashCode();
        Assertions.assertEquals(Objects.hashCode(value), actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentDoubleArray(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentDoubleArray(boolean taintValue) {
        double value = taintValue ? manager.setLabel((double) 5, "label") : 5;
        DoubleArrayRecord record = new DoubleArrayRecord(new double[] {value});
        double actual = record.value()[0];
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentShort(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentShort(boolean taintValue) {
        short value = taintValue ? manager.setLabel((short) 5, "label") : 5;
        ShortRecord record = new ShortRecord(value);
        short actual = record.value();
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "toStringShort(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void toStringShort(boolean taintValue) {
        short value = taintValue ? manager.setLabel((short) 5, "label") : 5;
        ShortRecord record = new ShortRecord(value);
        String actual = record.toString();
        Assertions.assertEquals("ShortRecord[value=" + Objects.toString(value) + "]", actual);
        checkToStringLabels(actual, taintValue);
    }

    @ParameterizedTest(name = "hashCodeShort(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void hashCodeShort(boolean taintValue) {
        short value = taintValue ? manager.setLabel((short) 5, "label") : 5;
        ShortRecord record = new ShortRecord(value);
        int actual = record.hashCode();
        Assertions.assertEquals(Objects.hashCode(value), actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentShortArray(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentShortArray(boolean taintValue) {
        short value = taintValue ? manager.setLabel((short) 5, "label") : 5;
        ShortArrayRecord record = new ShortArrayRecord(new short[] {value});
        short actual = record.value()[0];
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentInt(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentInt(boolean taintValue) {
        int value = taintValue ? manager.setLabel(5, "label") : 5;
        IntRecord record = new IntRecord(value);
        int actual = record.value();
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "toStringInt(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void toStringInt(boolean taintValue) {
        int value = taintValue ? manager.setLabel(5, "label") : 5;
        IntRecord record = new IntRecord(value);
        String actual = record.toString();
        Assertions.assertEquals("IntRecord[value=" + Objects.toString(value) + "]", actual);
        checkToStringLabels(actual, taintValue);
    }

    @ParameterizedTest(name = "hashCodeInt(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void hashCodeInt(boolean taintValue) {
        int value = taintValue ? manager.setLabel(5, "label") : 5;
        IntRecord record = new IntRecord(value);
        int actual = record.hashCode();
        Assertions.assertEquals(Objects.hashCode(value), actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentIntArray(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentIntArray(boolean taintValue) {
        int value = taintValue ? manager.setLabel(5, "label") : 5;
        IntArrayRecord record = new IntArrayRecord(new int[] {value});
        int actual = record.value()[0];
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentLong(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentLong(boolean taintValue) {
        long value = taintValue ? manager.setLabel((long) 5, "label") : 5;
        LongRecord record = new LongRecord(value);
        long actual = record.value();
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "toStringLong(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void toStringLong(boolean taintValue) {
        long value = taintValue ? manager.setLabel((long) 5, "label") : 5;
        LongRecord record = new LongRecord(value);
        String actual = record.toString();
        Assertions.assertEquals("LongRecord[value=" + Objects.toString(value) + "]", actual);
        checkToStringLabels(actual, taintValue);
    }

    @ParameterizedTest(name = "hashCodeLong(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void hashCodeLong(boolean taintValue) {
        long value = taintValue ? manager.setLabel((long) 5, "label") : 5;
        LongRecord record = new LongRecord(value);
        int actual = record.hashCode();
        Assertions.assertEquals(Objects.hashCode(value), actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentLongArray(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentLongArray(boolean taintValue) {
        long value = taintValue ? manager.setLabel((long) 5, "label") : 5;
        LongArrayRecord record = new LongArrayRecord(new long[] {value});
        long actual = record.value()[0];
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentObject(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentObject(boolean taintValue) {
        Object value = taintValue ? manager.setLabel("hello", "label") : "hello";
        ObjectRecord record = new ObjectRecord(value);
        Object actual = record.value();
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    @ParameterizedTest(name = "getComponentObjectArray(taintValue={0})")
    @ValueSource(booleans = {true, false})
    void getComponentObjectArray(boolean taintValue) {
        Object value = taintValue ? manager.setLabel("hello", "label") : "hello";
        ObjectArrayRecord record = new ObjectArrayRecord(new Object[] {value});
        Object actual = record.value()[0];
        Assertions.assertEquals(value, actual);
        checker.check(taintValue ? new Object[] {"label"} : new Object[0], manager.getLabels(actual));
    }

    private void checkToStringLabels(String s, boolean taintValue) {
        Object[] labels = new Object[] {"label"};
        s = s.substring(s.indexOf('=') + 1, s.length() - 1);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (taintValue) {
                checker.check(labels, manager.getLabels(c));
            } else {
                checker.checkEmpty(manager.getLabels(c));
            }
        }
    }
}
