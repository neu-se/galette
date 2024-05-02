package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.Queue;

public class TagFrame {
    private Class<?> caller;
    private Tag returnTag = Tag.getEmptyTag();
    private final Queue<Tag> tags;

    public TagFrame(Queue<Tag> tags) {
        this.tags = new Queue<>(tags);
    }

    public TagFrame() {
        this.tags = new Queue<>();
    }

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

    public Queue<Tag> copyTags() {
        return new Queue<>(tags);
    }

    public void clearTags() {
        tags.clear();
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
        TagFrame frame = new TagFrame();
        for (Tag tag : tags) {
            frame.enqueue(tag);
        }
        return frame;
    }

    public static TagFrame emptyFrame() {
        return new TagFrame();
    }

    private static final TagFrame EMPTY = new TagFrame() {
        @Override
        public Tag dequeue() {
            return null;
        }

        @Override
        public TagFrame enqueue(Tag tag) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public Tag getReturnTag() {
            return super.getReturnTag();
        }

        @Override
        public void setReturnTag(Tag returnTag) {
            super.setReturnTag(returnTag);
        }

        @Override
        public Class<?> getCaller(Class<?> ret) {
            return null;
        }

        @Override
        public TagFrame setCaller(Class<?> caller) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Queue<Tag> copyTags() {
            return new Queue<>();
        }

        @Override
        public void clearTags() {
            throw new UnsupportedOperationException();
        }
    };
}
