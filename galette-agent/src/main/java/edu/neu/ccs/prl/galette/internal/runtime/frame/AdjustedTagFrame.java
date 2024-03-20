package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public class AdjustedTagFrame extends TagFrame {
    private final IndirectTagFrame original;

    public AdjustedTagFrame(IndirectTagFrame original) {
        this.original = original;
        /// TODO: Handle set caller, set thrown tag, and set return tag
        //  Have the original frame delegate to the adjusted frame
    }

    public IndirectTagFrame getOriginal() {
        return original;
    }
}
