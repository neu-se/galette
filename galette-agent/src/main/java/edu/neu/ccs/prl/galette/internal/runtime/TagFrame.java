package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.Queue;

public class TagFrame {
    private Class<?> caller;
    private Tag returnTag = Tag.emptyTag();
    private final Queue<Tag> tags;
    private int i = 0;

    public TagFrame(Queue<Tag> tags) {
        this.tags = new Queue<>(tags);
    }

    public TagFrame() {
        this.tags = new Queue<>();
    }

    public Tag get(int index) {
        while (index > i) {
            dequeue();
        }
        return dequeue();
    }

    @InvokedViaHandle(handle = Handle.FRAME_DEQUEUE)
    public Tag dequeue() {
        i++;
        return tags.isEmpty() ? Tag.emptyTag() : tags.dequeue();
    }

    @InvokedViaHandle(handle = Handle.FRAME_ENQUEUE)
    public TagFrame enqueue(Tag tag) {
        tags.enqueue(tag);
        return this;
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

    public Queue<Tag> copyTags() {
        return new Queue<>(tags);
    }

    @Override
    public String toString() {
        return tags.toString();
    }

    @InvokedViaHandle(handle = Handle.FRAME_CREATE)
    public static TagFrame create(TagFrame parent) {
        return new TagFrame();
    }

    public TagFrame create(Tag... tags) {
        return new TagFrame().enqueue(tags);
    }

    TagFrame enqueue(Tag... tags) {
        for (Tag tag : tags) {
            enqueue(tag);
        }
        return this;
    }

    public static TagFrame newReflectiveFrame(TagFrame invokerFrame, Tag... tags) {
        return new ReflectionTagFrame(invokerFrame).enqueue(tags);
    }

    public static TagFrame emptyFrame() {
        return new TagFrame();
    }

    private static final class ReflectionTagFrame extends TagFrame {
        private final TagFrame invokerFrame;

        ReflectionTagFrame(TagFrame invokerFrame) {
            this.invokerFrame = invokerFrame;
        }

        public Tag getReturnTag() {
            return invokerFrame.getReturnTag();
        }

        public void setReturnTag(Tag returnTag) {
            invokerFrame.setReturnTag(returnTag);
        }
    }
}
