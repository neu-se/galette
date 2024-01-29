package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.Stack;

public class TagFrame {
    private Class<?> caller;
    private final Stack<Tag> tags = new Stack<>();
    private Tag returnTag = Tag.getEmptyTag();

    @InvokedViaHandle(handle = Handle.FRAME_PUSH)
    public TagFrame push(Tag tag) {
        tags.push(tag);
        return this;
    }

    @InvokedViaHandle(handle = Handle.FRAME_POP)
    public Tag pop() {
        return tags.isEmpty() ? Tag.getEmptyTag() : tags.pop();
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

    @InvokedViaHandle(handle = Handle.FRAME_GET_INSTANCE)
    public static TagFrame getInstance() {
        return new TagFrame();
    }
}
