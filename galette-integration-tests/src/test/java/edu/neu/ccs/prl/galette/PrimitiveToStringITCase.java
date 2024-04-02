package edu.neu.ccs.prl.galette;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("WrapperTypeMayBePrimitive")
public class PrimitiveToStringITCase {
    @Test
    void primitiveBooleanToString() {
        boolean value = Tainter.setTag(false, Tag.of("label"));
        String s = Boolean.toString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void boxedBooleanToString() {
        Boolean value = Tainter.setTag(true, Tag.of("label"));
        String s = value.toString();
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void stringValueOfBoolean() {
        boolean value = Tainter.setTag(true, Tag.of("label"));
        String s = String.valueOf(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveByteToString() {
        byte value = Tainter.setTag(Byte.MAX_VALUE, Tag.of("label"));
        String s = Byte.toString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void boxedByteToString() {
        Byte value = Tainter.setTag(Byte.MIN_VALUE, Tag.of("label"));
        String s = value.toString();
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void stringValueOfByte() {
        byte value = Tainter.setTag(Byte.MAX_VALUE, Tag.of("label"));
        String s = String.valueOf(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveCharToString() {
        char value = Tainter.setTag(Character.MAX_VALUE, Tag.of("label"));
        String s = Character.toString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void boxedCharToString() {
        Character value = Tainter.setTag(Character.MIN_VALUE, Tag.of("label"));
        String s = value.toString();
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void stringValueOfChar() {
        char value = Tainter.setTag(Character.MAX_VALUE, Tag.of("label"));
        String s = String.valueOf(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveShortToString() {
        short value = Tainter.setTag(Short.MAX_VALUE, Tag.of("label"));
        String s = Short.toString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void boxedShortToString() {
        Short value = Tainter.setTag(Short.MIN_VALUE, Tag.of("label"));
        String s = value.toString();
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void stringValueOfShort() {
        short value = Tainter.setTag(Short.MAX_VALUE, Tag.of("label"));
        String s = String.valueOf(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveIntToString() {
        int value = Tainter.setTag(Integer.MAX_VALUE, Tag.of("label"));
        String s = Integer.toString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void boxedIntToString() {
        Integer value = Tainter.setTag(Integer.MIN_VALUE, Tag.of("label"));
        String s = value.toString();
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void stringValueOfInt() {
        int value = Tainter.setTag(Integer.MAX_VALUE, Tag.of("label"));
        String s = String.valueOf(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveIntToUnsignedString() {
        int value = Tainter.setTag(Integer.MAX_VALUE, Tag.of("label"));
        String s = Integer.toUnsignedString(value, 10);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveIntToOctalString() {
        int value = Tainter.setTag(Integer.MIN_VALUE, Tag.of("label"));
        String s = Integer.toOctalString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveIntToHexString() {
        int value = Tainter.setTag(Integer.MIN_VALUE, Tag.of("label"));
        String s = Integer.toHexString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveIntToBinaryString() {
        int value = Tainter.setTag(Integer.MIN_VALUE, Tag.of("label"));
        String s = Integer.toBinaryString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveLongToString() {
        long value = Tainter.setTag(Long.MAX_VALUE, Tag.of("label"));
        String s = Long.toString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void boxedLongToString() {
        Long value = Tainter.setTag(Long.MIN_VALUE, Tag.of("label"));
        String s = value.toString();
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void stringValueOfLong() {
        long value = Tainter.setTag(Long.MAX_VALUE, Tag.of("label"));
        String s = String.valueOf(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveLongToUnsignedString() {
        long value = Tainter.setTag(Long.MAX_VALUE, Tag.of("label"));
        String s = Long.toUnsignedString(value, 10);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveLongToOctalString() {
        long value = Tainter.setTag(Long.MIN_VALUE, Tag.of("label"));
        String s = Long.toOctalString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveLongToHexString() {
        long value = Tainter.setTag(Long.MIN_VALUE, Tag.of("label"));
        String s = Long.toHexString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveLongToBinaryString() {
        long value = Tainter.setTag(Long.MIN_VALUE, Tag.of("label"));
        String s = Long.toBinaryString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveFloatToString() {
        float value = Tainter.setTag(Float.MAX_VALUE, Tag.of("label"));
        String s = Float.toString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void boxedFloatToString() {
        Float value = Tainter.setTag(Float.MIN_VALUE, Tag.of("label"));
        String s = value.toString();
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void stringValueOfFloat() {
        float value = Tainter.setTag(Float.MAX_VALUE, Tag.of("label"));
        String s = String.valueOf(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void primitiveDoubleToString() {
        double value = Tainter.setTag(Double.MAX_VALUE, Tag.of("label"));
        String s = Double.toString(value);
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void boxedDoubleToString() {
        Double value = Tainter.setTag(Double.MIN_VALUE, Tag.of("label"));
        String s = value.toString();
        checkCharLabels(s, Tag.of("label"));
    }

    @Test
    void stringValueOfDouble() {
        double value = Tainter.setTag(Double.MAX_VALUE, Tag.of("label"));
        String s = String.valueOf(value);
        checkCharLabels(s, Tag.of("label"));
    }

    private static void checkCharLabels(String s, Tag expected) {
        for (int i = 0; i < s.length(); i++) {
            Tag actual = Tainter.getTag(s.charAt(i));
            Assertions.assertEquals(expected, actual);
        }
    }
}
