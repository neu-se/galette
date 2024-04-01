package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public class ProcessedTagFrame extends TagFrame {
    private final TagFrame original;

    private ProcessedTagFrame(TagFrame original) {
        super(original.copyTags(), original.getParent());
        this.original = original;
    }

    @Override
    public void setReturnTag(Tag returnTag) {
        original.setReturnTag(returnTag);
        super.setReturnTag(returnTag);
    }

    @Override
    public TagFrame setCaller(Class<?> caller) {
        original.setCaller(caller);
        return super.setCaller(caller);
    }

    @InvokedViaHandle(handle = Handle.PROCESSED_FRAME_CREATE)
    public static TagFrame create(TagFrame other) {
        return new ProcessedTagFrame(other);
    }
}
