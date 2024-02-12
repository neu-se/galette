package edu.neu.ccs.prl.galette.bench;

public interface TagManager {
    void reset();

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
}
