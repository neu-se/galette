package edu.neu.ccs.prl.galette.internal.runtime;

@SuppressWarnings("unused")
public final class Tainter {
    private Tainter() {
        throw new AssertionError();
    }

    public static boolean setTag(boolean value, Tag tag, TagFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static byte setTag(byte value, Tag tag, TagFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static char setTag(char value, Tag tag, TagFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static short setTag(short value, Tag tag, TagFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static int setTag(int value, Tag tag, TagFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static long setTag(long value, Tag tag, TagFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static float setTag(float value, Tag tag, TagFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static double setTag(double value, Tag tag, TagFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static <T> T setTag(T value, Tag tag, TagFrame frame) {
        frame.setReturnTag(tag);
        return value;
    }

    public static boolean setTag(boolean value, Tag tag) {
        // Placeholder
        return value;
    }

    public static byte setTag(byte value, Tag tag) {
        // Placeholder
        return value;
    }

    public static char setTag(char value, Tag tag) {
        // Placeholder
        return value;
    }

    public static short setTag(short value, Tag tag) {
        // Placeholder
        return value;
    }

    public static int setTag(int value, Tag tag) {
        // Placeholder
        return value;
    }

    public static long setTag(long value, Tag tag) {
        // Placeholder
        return value;
    }

    public static float setTag(float value, Tag tag) {
        // Placeholder
        return value;
    }

    public static double setTag(double value, Tag tag) {
        // Placeholder
        return value;
    }

    public static <T> T setTag(T value, Tag tag) {
        // Placeholder
        return value;
    }

    public static Tag getTag(boolean value, TagFrame frame) {
        return frame.dequeue();
    }

    public static Tag getTag(byte value, TagFrame frame) {
        return frame.dequeue();
    }

    public static Tag getTag(char value, TagFrame frame) {
        return frame.dequeue();
    }

    public static Tag getTag(short value, TagFrame frame) {
        return frame.dequeue();
    }

    public static Tag getTag(int value, TagFrame frame) {
        return frame.dequeue();
    }

    public static Tag getTag(long value, TagFrame frame) {
        return frame.dequeue();
    }

    public static Tag getTag(float value, TagFrame frame) {
        return frame.dequeue();
    }

    public static Tag getTag(double value, TagFrame frame) {
        return frame.dequeue();
    }

    public static Tag getTag(Object value, TagFrame frame) {
        return frame.dequeue();
    }

    public static Tag getTag(boolean value) {
        // Placeholder
        return null;
    }

    public static Tag getTag(byte value) {
        // Placeholder
        return null;
    }

    public static Tag getTag(char value) {
        // Placeholder
        return null;
    }

    public static Tag getTag(short value) {
        // Placeholder
        return null;
    }

    public static Tag getTag(int value) {
        // Placeholder
        return null;
    }

    public static Tag getTag(long value) {
        // Placeholder
        return null;
    }

    public static Tag getTag(float value) {
        // Placeholder
        return null;
    }

    public static Tag getTag(double value) {
        // Placeholder
        return null;
    }

    public static Tag getTag(Object value) {
        // Placeholder
        return null;
    }

    public static void clearTags() {
        // Clear mirrored tag stores
        FieldTagStore.clear();
        ArrayTagStore.clear();
    }

    @SuppressWarnings("unused")
    public static void clearTags(TagFrame frame) {
        clearTags();
    }
}
