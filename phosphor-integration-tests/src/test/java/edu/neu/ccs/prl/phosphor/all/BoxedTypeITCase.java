package edu.neu.ccs.prl.phosphor.all;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("UnnecessaryBoxing")
public class BoxedTypeITCase {
    @Test
    void boxedBoolean() {
        boolean expected = false;
        Boolean boxed = Boolean.valueOf(expected);
        //noinspection ConstantValue
        Assertions.assertEquals(expected, boxed);
    }

    @Test
    void boxedByte() {
        byte expected = 8;
        Byte boxed = Byte.valueOf(expected);
        Assertions.assertEquals(expected, boxed.byteValue());
    }

    @Test
    void boxedCharacter() {
        char expected = 8;
        Character boxed = Character.valueOf(expected);
        Assertions.assertEquals(expected, boxed.charValue());
    }

    @Test
    void boxedShort() {
        short expected = 8;
        Short boxed = Short.valueOf(expected);
        Assertions.assertEquals(expected, boxed.shortValue());
    }
}
