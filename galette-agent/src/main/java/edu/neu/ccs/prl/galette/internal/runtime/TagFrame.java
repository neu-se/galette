package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.Arrays;

public class TagFrame {
    private Class<?> caller;
    private Tag returnTag = Tag.emptyTag();
    private Tag[] tags = new Tag[256];
    private int size;

    protected TagFrame(TagFrame other) {
        this.tags = other.tags.clone();
        this.size = other.size;
    }

    TagFrame(int size) {
        this.size = size;
    }

    @InvokedViaHandle(handle = Handle.FRAME_GET_TAG)
    public Tag get(int index) {
        return index >= size ? Tag.emptyTag() : tags[index];
    }

    @InvokedViaHandle(handle = Handle.FRAME_SET_TAG)
    public TagFrame set(int index, Tag tag) {
        tags[index] = tag;
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
    public Class<?> getCaller() {
        return caller;
    }

    @InvokedViaHandle(handle = Handle.FRAME_SET_CALLER)
    public TagFrame setCaller(Class<?> caller) {
        this.caller = caller;
        return this;
    }

    @InvokedViaHandle(handle = Handle.FRAME_ACQUIRE)
    public TagFrame acquire(int size) {
        // TODO resize
        return new TagFrame(size);
    }

    @Override
    public String toString() {
        return Arrays.toString(tags, size);
    }

    public static TagFrame newReflectiveFrame(TagFrame invokerFrame, Tag... tags) {
        TagFrame frame = new ReflectionTagFrame(invokerFrame, tags.length);
        System.arraycopy(tags, 0, frame.tags, 0, tags.length);
        return frame;
    }

    public static TagFrame emptyFrame() {
        return new TagFrame(0);
    }

    private static final class ReflectionTagFrame extends TagFrame {
        private final TagFrame invokerFrame;

        ReflectionTagFrame(TagFrame invokerFrame, int size) {
            super(size);
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
