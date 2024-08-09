package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.collection.Arrays;

public final class AugmentedFrame {
    private final TagFrame frame;
    private final Object[] arguments;

    public AugmentedFrame(TagFrame frame, Object[] arguments) {
        this.frame = frame;
        this.arguments = arguments;
    }

    public TagFrame getFrame() {
        return frame;
    }

    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "(" + frame + ", " + Arrays.toString(arguments) + ")";
    }
}
