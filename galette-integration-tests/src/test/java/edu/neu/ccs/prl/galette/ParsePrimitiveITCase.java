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
        byte value = Byte.parseByte(s);
        TagAssertions.assertTagEquals(
                value, IntStream.range(0, s.length()).boxed().toArray());
    }

    @Test
    void parseShort() {
        String s = TagAssertions.taintWithIndices("72");
        short value = Short.parseShort(s);
        TagAssertions.assertTagEquals(
                value, IntStream.range(0, s.length()).boxed().toArray());
    }

    @Test
    void parseInteger() {
        String s = TagAssertions.taintWithIndices("-0872");
        int value = Integer.parseInt(s);
        TagAssertions.assertTagEquals(
                value, IntStream.range(0, s.length()).boxed().toArray());
    }

    @Test
    void parseLong() {
        String s = TagAssertions.taintWithIndices("999999972");
        long value = Long.parseLong(s);
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
