package edu.neu.ccs.prl.galette;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import org.junit.jupiter.api.Assertions;

public final class TagAssertions {
    private TagAssertions() {
        throw new AssertionError(getClass() + " is a static utility class");
    }

    public static void assertTagEquals(Object value, Object[] labels) {
        Assertions.assertArrayEquals(labels, Tag.getLabels(Tainter.getTag(value)));
    }

    public static void assertTagEquals(boolean value, Object... labels) {
        Assertions.assertArrayEquals(labels, Tag.getLabels(Tainter.getTag(value)));
    }

    public static void assertTagEquals(byte value, Object... labels) {
        Assertions.assertArrayEquals(labels, Tag.getLabels(Tainter.getTag(value)));
    }

    public static void assertTagEquals(char value, Object... labels) {
        Assertions.assertArrayEquals(labels, Tag.getLabels(Tainter.getTag(value)));
    }

    public static void assertTagEquals(float value, Object... labels) {
        Assertions.assertArrayEquals(labels, Tag.getLabels(Tainter.getTag(value)));
    }

    public static void assertTagEquals(double value, Object... labels) {
        Assertions.assertArrayEquals(labels, Tag.getLabels(Tainter.getTag(value)));
    }

    public static void assertTagEquals(int value, Object... labels) {
        Assertions.assertArrayEquals(labels, Tag.getLabels(Tainter.getTag(value)));
    }

    public static void assertTagEquals(long value, Object... labels) {
        Assertions.assertArrayEquals(labels, Tag.getLabels(Tainter.getTag(value)));
    }

    public static void assertTagEquals(short value, Object... labels) {
        Assertions.assertArrayEquals(labels, Tag.getLabels(Tainter.getTag(value)));
    }
}
