package edu.neu.ccs.prl.phosphor.internal.runtime;

@SuppressWarnings("unused")
public final class Tainter {
    private Tainter() {
        throw new AssertionError();
    }

    public static boolean setTag(boolean value, Tag tag, PhosphorFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static byte setTag(byte value, Tag tag, PhosphorFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static char setTag(char value, Tag tag, PhosphorFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static short setTag(short value, Tag tag, PhosphorFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static int setTag(int value, Tag tag, PhosphorFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static long setTag(long value, Tag tag, PhosphorFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static float setTag(float value, Tag tag, PhosphorFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static double setTag(double value, Tag tag, PhosphorFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static <T> T setTag(T value, Tag tag, PhosphorFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static boolean setTag(boolean value, Tag tag) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static byte setTag(byte value, Tag tag) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static char setTag(char value, Tag tag) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static short setTag(short value, Tag tag) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static int setTag(int value, Tag tag) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static long setTag(long value, Tag tag) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static float setTag(float value, Tag tag) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static double setTag(double value, Tag tag) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static <T> T setTag(T value, Tag tag) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static Tag getTag(boolean value, PhosphorFrame frame) {
        return frame.pop();
    }

    public static Tag getTag(byte value, PhosphorFrame frame) {
        return frame.pop();
    }

    public static Tag getTag(char value, PhosphorFrame frame) {
        return frame.pop();
    }

    public static Tag getTag(short value, PhosphorFrame frame) {
        return frame.pop();
    }

    public static Tag getTag(int value, PhosphorFrame frame) {
        return frame.pop();
    }

    public static Tag getTag(long value, PhosphorFrame frame) {
        return frame.pop();
    }

    public static Tag getTag(float value, PhosphorFrame frame) {
        return frame.pop();
    }

    public static Tag getTag(double value, PhosphorFrame frame) {
        return frame.pop();
    }

    public static Tag getTag(Object value, PhosphorFrame frame) {
        return frame.pop();
    }

    public static Tag getTag(boolean value) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static Tag getTag(byte value) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static Tag getTag(char value) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static Tag getTag(short value) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static Tag getTag(int value) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static Tag getTag(long value) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static Tag getTag(float value) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static Tag getTag(double value) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }

    public static Tag getTag(Object value) {
        throw new AssertionError("Uninstrumented placeholder method was called");
    }
}
