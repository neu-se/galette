package edu.neu.ccs.prl.galette.extension;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;

public class GaletteTagManager implements TagManager {
    @Override
    public void reset() {
        Tainter.clearTags();
    }

    @Override
    public boolean setLabels(boolean value, Object... labels) {
        return Tainter.setTag(value, Tag.of(labels));
    }

    @Override
    public byte setLabels(byte value, Object... labels) {
        return Tainter.setTag(value, Tag.of(labels));
    }

    @Override
    public char setLabels(char value, Object... labels) {
        return Tainter.setTag(value, Tag.of(labels));
    }

    @Override
    public short setLabels(short value, Object... labels) {
        return Tainter.setTag(value, Tag.of(labels));
    }

    @Override
    public int setLabels(int value, Object... labels) {
        return Tainter.setTag(value, Tag.of(labels));
    }

    @Override
    public long setLabels(long value, Object... labels) {
        return Tainter.setTag(value, Tag.of(labels));
    }

    @Override
    public float setLabels(float value, Object... labels) {
        return Tainter.setTag(value, Tag.of(labels));
    }

    @Override
    public double setLabels(double value, Object... labels) {
        return Tainter.setTag(value, Tag.of(labels));
    }

    @Override
    public <T> T setLabels(T value, Object... labels) {
        return Tainter.setTag(value, Tag.of(labels));
    }

    @Override
    public Object[] getLabels(boolean value) {
        return Tag.getLabels(Tainter.getTag(value));
    }

    @Override
    public Object[] getLabels(byte value) {
        return Tag.getLabels(Tainter.getTag(value));
    }

    @Override
    public Object[] getLabels(char value) {
        return Tag.getLabels(Tainter.getTag(value));
    }

    @Override
    public Object[] getLabels(short value) {
        return Tag.getLabels(Tainter.getTag(value));
    }

    @Override
    public Object[] getLabels(int value) {
        return Tag.getLabels(Tainter.getTag(value));
    }

    @Override
    public Object[] getLabels(long value) {
        return Tag.getLabels(Tainter.getTag(value));
    }

    @Override
    public Object[] getLabels(float value) {
        return Tag.getLabels(Tainter.getTag(value));
    }

    @Override
    public Object[] getLabels(double value) {
        return Tag.getLabels(Tainter.getTag(value));
    }

    @Override
    public Object[] getLabels(Object value) {
        return Tag.getLabels(Tainter.getTag(value));
    }
}
