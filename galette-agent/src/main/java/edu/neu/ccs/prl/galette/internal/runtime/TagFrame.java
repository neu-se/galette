package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.Queue;

public class TagFrame {
    private Class<?> caller;
    private Tag returnTag = Tag.getEmptyTag();
    private final Queue<Tag> tags = new Queue<>();

    @InvokedViaHandle(handle = Handle.FRAME_DEQUEUE)
    public Tag dequeue() {
        return tags.isEmpty() ? Tag.getEmptyTag() : tags.dequeue();
    }

    @InvokedViaHandle(handle = Handle.FRAME_ENQUEUE)
    public TagFrame enqueue(Tag tag) {
        tags.enqueue(tag);
        return this;
    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }

    @InvokedViaHandle(handle = Handle.FRAME_GET_RETURN_TAG)
    public Tag getReturnTag() {
        return returnTag;
    }

    @InvokedViaHandle(handle = Handle.FRAME_SET_RETURN_TAG)
    public void setReturnTag(Tag returnTag) {
        this.returnTag = returnTag;
    }

    @InvokedViaHandle(handle = Handle.FRAME_GET_CALLER)
    public Class<?> getCaller(Class<?> ret) {
        return caller == null ? ret : caller;
    }

    @InvokedViaHandle(handle = Handle.FRAME_SET_CALLER)
    public TagFrame setCaller(Class<?> caller) {
        this.caller = caller;
        return this;
    }

    @InvokedViaHandle(handle = Handle.FRAME_SET_THROWN_TAG)
    public void setThrownTag(Throwable t, Tag tag) {
        // TODO
    }

    @InvokedViaHandle(handle = Handle.FRAME_CREATE_FOR_CALL)
    public static TagFrame createForCall(TagFrame callerFrame) {
        // TODO
        return new TagFrame();
    }

    @InvokedViaHandle(handle = Handle.FRAME_CREATE_EMPTY)
    public static TagFrame createEmpty() {
        return new TagFrame();
    }
}
