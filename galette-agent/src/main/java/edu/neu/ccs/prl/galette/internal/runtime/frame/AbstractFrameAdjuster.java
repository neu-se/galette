package edu.neu.ccs.prl.galette.internal.runtime.frame;

abstract class AbstractFrameAdjuster implements FrameAdjuster {
    @Override
    public FrameAdjuster process(boolean value) {
        processInternal(value);
        return this;
    }

    @Override
    public FrameAdjuster process(byte value) {
        processInternal(value);
        return this;
    }

    @Override
    public FrameAdjuster process(char value) {
        processInternal(value);
        return this;
    }

    @Override
    public FrameAdjuster process(short value) {
        processInternal(value);
        return this;
    }

    @Override
    public FrameAdjuster process(int value) {
        processInternal(value);
        return this;
    }

    @Override
    public FrameAdjuster process(long value) {
        processInternal(value);
        return this;
    }

    @Override
    public FrameAdjuster process(float value) {
        processInternal(value);
        return this;
    }

    @Override
    public FrameAdjuster process(double value) {
        processInternal(value);
        return this;
    }

    @Override
    public FrameAdjuster process(Object value) {
        processInternal(value);
        return this;
    }

    protected abstract void processInternal(Object value);
}
