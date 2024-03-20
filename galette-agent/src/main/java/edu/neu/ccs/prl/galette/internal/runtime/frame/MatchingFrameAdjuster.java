package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

class MatchingFrameAdjuster extends AbstractFrameAdjuster {
    private final IndirectTagFrame original;
    private final int index = 0;

    public MatchingFrameAdjuster(IndirectTagFrame original) {
        this.original = original;
    }

    @Override
    protected void processInternal(Object value) {
        // TODO
    }

    @Override
    public TagFrame createFrame() {
        return new AdjustedTagFrame(original);
    }
}
