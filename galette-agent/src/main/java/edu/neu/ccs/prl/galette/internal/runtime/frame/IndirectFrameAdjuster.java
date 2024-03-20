package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

class IndirectFrameAdjuster implements FrameAdjuster {
    private final TagFrame original;
    private final int index = 0;

    public IndirectFrameAdjuster(TagFrame original) {
        this.original = original;
    }

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

    private void processInternal(Object value) {
        // TODO
    }

    @Override
    public TagFrame createFrame() {
        return new AdjustedTagFrame(original);
    }
}
