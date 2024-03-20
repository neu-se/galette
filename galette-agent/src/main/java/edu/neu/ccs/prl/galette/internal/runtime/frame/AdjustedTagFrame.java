package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public class AdjustedTagFrame extends TagFrame {
    private final TagFrame original;

    public AdjustedTagFrame(TagFrame original) {
        this.original = original;
        /// TODO how to handle set caller, set thrown tag, and set return tag?
    }

    public TagFrame getOriginal() {
        return original;
    }
}
