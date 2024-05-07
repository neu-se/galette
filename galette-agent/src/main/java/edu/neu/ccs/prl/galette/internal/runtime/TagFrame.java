package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.Arrays;

public class TagFrame {
    private static final TagFrame DISABLED = new DisabledTagFrame();
    private Class<?> caller;
    private Tag returnTag = Tag.emptyTag();
    private Tag[] tags = new Tag[256];
    private int size;

    private TagFrame(int size) {
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
        // TODO resize/ensure size
        // Clear the remainder of the tag array
        for (int i = size; i < this.size; i++) {
            tags[i] = Tag.emptyTag();
        }
        this.returnTag = Tag.emptyTag();
        this.size = size;
        return this;
    }

    @Override
    public String toString() {
        return Arrays.toString(tags, size);
    }

    @InvokedViaHandle(handle = Handle.FRAME_SET_TAGS)
    public void setTags(Tag[] tags) {
        this.tags = tags;
        this.size = tags.length;
    }

    @InvokedViaHandle(handle = Handle.FRAME_GET_TAGS)
    public Tag[] getTags() {
        return tags.clone();
    }

    public static TagFrame emptyFrame() {
        return new TagFrame(0);
    }

    public static TagFrame disabled() {
        return DISABLED;
    }

    private static final class DisabledTagFrame extends TagFrame {
        DisabledTagFrame() {
            super(0);
        }

        @Override
        public Tag get(int index) {
            return Tag.emptyTag();
        }

        @Override
        public TagFrame set(int index, Tag tag) {
            return this;
        }

        @Override
        public Tag getReturnTag() {
            return Tag.emptyTag();
        }

        @Override
        public void setReturnTag(Tag returnTag) {}

        @Override
        public TagFrame acquire(int size) {
            return this;
        }
    }
}
