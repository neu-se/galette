package edu.neu.ccs.prl.galette.eval;

import edu.columbia.cs.psl.phosphor.runtime.MultiTainter;
import edu.columbia.cs.psl.phosphor.runtime.Taint;
import edu.columbia.cs.psl.phosphor.struct.PowerSetTree;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;

public class PhosphorTagManager implements TagManager {
    @Override
    public void setUp() {}

    @Override
    public void tearDown() {
        PowerSetTree.getInstance().reset();
    }

    @Override
    public boolean setLabels(boolean value, Object[] labels) {
        if (labels.length != 1) {
            throw new IllegalArgumentException("Only a single label is supported");
        }
        return MultiTainter.taintedBoolean(value, labels[0]);
    }

    @Override
    public byte setLabels(byte value, Object[] labels) {
        if (labels.length != 1) {
            throw new IllegalArgumentException("Only a single label is supported");
        }
        return MultiTainter.taintedByte(value, labels[0]);
    }

    @Override
    public char setLabels(char value, Object[] labels) {
        if (labels.length != 1) {
            throw new IllegalArgumentException("Only a single label is supported");
        }
        return MultiTainter.taintedChar(value, labels[0]);
    }

    @Override
    public short setLabels(short value, Object[] labels) {
        if (labels.length != 1) {
            throw new IllegalArgumentException("Only a single label is supported");
        }
        return MultiTainter.taintedShort(value, labels[0]);
    }

    @Override
    public int setLabels(int value, Object[] labels) {
        if (labels.length != 1) {
            throw new IllegalArgumentException("Only a single label is supported");
        }
        return MultiTainter.taintedInt(value, labels[0]);
    }

    @Override
    public long setLabels(long value, Object[] labels) {
        if (labels.length != 1) {
            throw new IllegalArgumentException("Only a single label is supported");
        }
        return MultiTainter.taintedLong(value, labels[0]);
    }

    @Override
    public float setLabels(float value, Object[] labels) {
        if (labels.length != 1) {
            throw new IllegalArgumentException("Only a single label is supported");
        }
        return MultiTainter.taintedFloat(value, labels[0]);
    }

    @Override
    public double setLabels(double value, Object[] labels) {
        if (labels.length != 1) {
            throw new IllegalArgumentException("Only a single label is supported");
        }
        return MultiTainter.taintedDouble(value, labels[0]);
    }

    @Override
    public <T> T setLabels(T value, Object[] labels) {
        if (labels.length != 1) {
            throw new IllegalArgumentException("Only a single label is supported");
        }
        return MultiTainter.taintedReference(value, labels[0]);
    }

    @Override
    public Object[] getLabels(boolean value) {
        Taint<?> tag = MultiTainter.getTaint(value);
        return tag == null ? new Object[0] : tag.getLabels();
    }

    @Override
    public Object[] getLabels(byte value) {
        Taint<?> tag = MultiTainter.getTaint(value);
        return tag == null ? new Object[0] : tag.getLabels();
    }

    @Override
    public Object[] getLabels(char value) {
        Taint<?> tag = MultiTainter.getTaint(value);
        return tag == null ? new Object[0] : tag.getLabels();
    }

    @Override
    public Object[] getLabels(short value) {
        Taint<?> tag = MultiTainter.getTaint(value);
        return tag == null ? new Object[0] : tag.getLabels();
    }

    @Override
    public Object[] getLabels(int value) {
        Taint<?> tag = MultiTainter.getTaint(value);
        return tag == null ? new Object[0] : tag.getLabels();
    }

    @Override
    public Object[] getLabels(long value) {
        Taint<?> tag = MultiTainter.getTaint(value);
        return tag == null ? new Object[0] : tag.getLabels();
    }

    @Override
    public Object[] getLabels(float value) {
        Taint<?> tag = MultiTainter.getTaint(value);
        return tag == null ? new Object[0] : tag.getLabels();
    }

    @Override
    public Object[] getLabels(double value) {
        Taint<?> tag = MultiTainter.getTaint(value);
        return tag == null ? new Object[0] : tag.getLabels();
    }

    @Override
    public Object[] getLabels(Object value) {
        Taint<?> tag = MultiTainter.getTaint(value);
        return tag == null ? new Object[0] : tag.getLabels();
    }
}
