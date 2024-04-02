package edu.neu.ccs.prl.galette;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

public class ParsePrimitiveITCase {
    @Test
    void parseBoolean() {
        String s = TagAssertions.taintWithIndices("true");
        boolean value = Boolean.parseBoolean(s);
        TagAssertions.assertTagEquals(
                value, IntStream.range(0, s.length()).boxed().toArray());
    }

    @Test
    void valueOfBoolean() {
        String s = TagAssertions.taintWithIndices("true");
        Boolean value = Boolean.valueOf(s);
        TagAssertions.assertTagEquals(
                value.booleanValue(), IntStream.range(0, s.length()).boxed().toArray());
    }

    @Test
    void parseByte() {
        String s = TagAssertions.taintWithIndices("72");
        byte value = Byte.parseByte(s, 10);
        TagAssertions.assertTagEquals(
                value, IntStream.range(0, s.length()).boxed().toArray());
    }

    @Test
    void parseShort() {
        String s = TagAssertions.taintWithIndices("72");
        short value = Short.parseShort(s, 10);
        TagAssertions.assertTagEquals(
                value, IntStream.range(0, s.length()).boxed().toArray());
    }

    @Test
    void parseInt() {
        String s = TagAssertions.taintWithIndices("-0872");
        int value = Integer.parseInt(s, 10);
        TagAssertions.assertTagEquals(
                value, IntStream.range(0, s.length()).boxed().toArray());
    }

    @Test
    void parseUnsignedInt() {
        String s = TagAssertions.taintWithIndices("872");
        int value = Integer.parseUnsignedInt(s, 10);
        TagAssertions.assertTagEquals(
                value, IntStream.range(0, s.length()).boxed().toArray());
    }

    @Test
    void parseLong() {
        String s = TagAssertions.taintWithIndices("-999999972");
        long value = Long.parseLong(s, 10);
        TagAssertions.assertTagEquals(
                value, IntStream.range(0, s.length()).boxed().toArray());
    }

    @Test
    void parseUnsignedLong() {
        String s = TagAssertions.taintWithIndices("9223372036854775806");
        long value = Long.parseUnsignedLong(s, 10);
        TagAssertions.assertTagEquals(
                value, IntStream.range(0, s.length()).boxed().toArray());
    }

    @Test
    void parseDouble() {
        String s = TagAssertions.taintWithIndices("-1.999");
        double value = Double.parseDouble(s);
        TagAssertions.assertTagEquals(
                value, IntStream.range(0, s.length()).boxed().toArray());
    }

    @Test
    void parseFloat() {
        String s = TagAssertions.taintWithIndices("1999.5");
        float value = Float.parseFloat(s);
        TagAssertions.assertTagEquals(
                value, IntStream.range(0, s.length()).boxed().toArray());
    }
}
