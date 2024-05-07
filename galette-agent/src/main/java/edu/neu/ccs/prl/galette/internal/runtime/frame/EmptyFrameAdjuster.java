package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

class EmptyFrameAdjuster implements FrameAdjuster {
    @Override
    public FrameAdjuster process(boolean value) {
        return this;
    }

    @Override
    public FrameAdjuster process(byte value) {
        return this;
    }

    @Override
    public FrameAdjuster process(char value) {
        return this;
    }

    @Override
    public FrameAdjuster process(short value) {
        return this;
    }

    @Override
    public FrameAdjuster process(int value) {
        return this;
    }

    @Override
    public FrameAdjuster process(long value) {
        return this;
    }

    @Override
    public FrameAdjuster process(float value) {
        return this;
    }

    @Override
    public FrameAdjuster process(double value) {
        return this;
    }

    @Override
    public FrameAdjuster process(Object value) {
        return this;
    }

    @Override
    public TagFrame createFrame() {
        // TODO attempt to use a "spare" frame
        return TagFrame.emptyFrame();
    }
}
