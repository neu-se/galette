package edu.neu.ccs.prl.galette.bench.extension;

public interface TagManager {
    void setUp();

    void tearDown();

    boolean setLabels(boolean value, Object[] labels);

    byte setLabels(byte value, Object[] labels);

    char setLabels(char value, Object[] labels);

    short setLabels(short value, Object[] labels);

    int setLabels(int value, Object[] labels);

    long setLabels(long value, Object[] labels);

    float setLabels(float value, Object[] labels);

    double setLabels(double value, Object[] labels);

    <T> T setLabels(T value, Object[] labels);

    Object[] getLabels(boolean value);

    Object[] getLabels(byte value);

    Object[] getLabels(char value);

    Object[] getLabels(short value);

    Object[] getLabels(int value);

    Object[] getLabels(long value);

    Object[] getLabels(float value);

    Object[] getLabels(double value);

    Object[] getLabels(Object value);

    default boolean setLabel(boolean value, String label) {
        return setLabels(value, new Object[] {label});
    }

    default byte setLabel(byte value, String label) {
        return setLabels(value, new Object[] {label});
    }

    default char setLabel(char value, String label) {
        return setLabels(value, new Object[] {label});
    }

    default short setLabel(short value, String label) {
        return setLabels(value, new Object[] {label});
    }

    default int setLabel(int value, String label) {
        return setLabels(value, new Object[] {label});
    }

    default long setLabel(long value, String label) {
        return setLabels(value, new Object[] {label});
    }

    default float setLabel(float value, String label) {
        return setLabels(value, new Object[] {label});
    }

    default double setLabel(double value, String label) {
        return setLabels(value, new Object[] {label});
    }

    default <T> T setLabel(T value, String label) {
        return setLabels(value, new Object[] {label});
    }
}
