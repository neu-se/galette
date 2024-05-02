package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

class MismatchedFrameAdjuster extends EmptyFrameAdjuster {
    private final TagFrame original;
    private final Object[] arguments;

    MismatchedFrameAdjuster(TagFrame original, Object[] arguments) {
        this.original = original;
        this.arguments = arguments;
    }

    @Override
    public TagFrame createFrame() {
        return new AdjustedTagFrame(original, arguments);
    }
}
