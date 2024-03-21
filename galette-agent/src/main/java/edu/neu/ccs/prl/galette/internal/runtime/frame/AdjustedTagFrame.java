package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.collection.Queue;

class AdjustedTagFrame extends TagFrame {
    private final TagFrame original;
    private final Object[] arguments;

    public AdjustedTagFrame(TagFrame original, Object[] arguments, Queue<Tag> tags) {
        super(tags);
        this.original = original;
        this.arguments = arguments;
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

    @Override
    public void setThrownTag(Throwable t, Tag tag) {
        original.setThrownTag(t, tag);
        super.setThrownTag(t, tag);
    }

    public TagFrame getOriginal() {
        return original;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
