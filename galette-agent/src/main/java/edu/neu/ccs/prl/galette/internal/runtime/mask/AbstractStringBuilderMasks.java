package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class AbstractStringBuilderMasks {
    @Mask(owner = "java/lang/StringBuilder", name = "append", type = MaskType.REPLACE)
    public static StringBuilder append(StringBuilder builder, boolean b, TagFrame frame) {
        Tag receiverTag = frame.dequeue();
        String s = StringAccessor.setCharTags(b ? "true" : "false", frame.dequeue());
        StringAccessor.append(builder, s, TagFrame.emptyFrame());
        frame.setReturnTag(receiverTag);
        return builder;
    }

    @Mask(owner = "java/lang/StringBuffer", name = "append", type = MaskType.REPLACE)
    public static StringBuffer append(StringBuffer buffer, boolean b, TagFrame frame) {
        Tag receiverTag = frame.dequeue();
        String s = StringAccessor.setCharTags(b ? "true" : "false", frame.dequeue());
        StringAccessor.append(buffer, s, TagFrame.emptyFrame());
        frame.setReturnTag(receiverTag);
        return buffer;
    }

    @Mask(owner = "java/lang/StringBuilder", name = "append", type = MaskType.REPLACE)
    public static StringBuilder append(StringBuilder builder, int value, TagFrame frame) {
        Tag receiverTag = frame.dequeue();
        String s = BoxTypeAccessor.toString(value, TagFrame.emptyFrame());
        s = StringAccessor.setCharTags(s, frame.dequeue());
        StringAccessor.append(builder, s, TagFrame.emptyFrame());
        frame.setReturnTag(receiverTag);
        return builder;
    }

    @Mask(owner = "java/lang/StringBuffer", name = "append", type = MaskType.REPLACE)
    public static StringBuffer append(StringBuffer builder, int value, TagFrame frame) {
        Tag receiverTag = frame.dequeue();
        String s = BoxTypeAccessor.toString(value, TagFrame.emptyFrame());
        s = StringAccessor.setCharTags(s, frame.dequeue());
        StringAccessor.append(builder, s, TagFrame.emptyFrame());
        frame.setReturnTag(receiverTag);
        return builder;
    }

    @Mask(owner = "java/lang/StringBuilder", name = "append", type = MaskType.REPLACE)
    public static StringBuilder append(StringBuilder builder, long value, TagFrame frame) {
        Tag receiverTag = frame.dequeue();
        String s = BoxTypeAccessor.toString(value, TagFrame.emptyFrame());
        s = StringAccessor.setCharTags(s, frame.dequeue());
        StringAccessor.append(builder, s, TagFrame.emptyFrame());
        frame.setReturnTag(receiverTag);
        return builder;
    }

    @Mask(owner = "java/lang/StringBuffer", name = "append", type = MaskType.REPLACE)
    public static StringBuffer append(StringBuffer builder, long value, TagFrame frame) {
        Tag receiverTag = frame.dequeue();
        String s = BoxTypeAccessor.toString(value, TagFrame.emptyFrame());
        s = StringAccessor.setCharTags(s, frame.dequeue());
        StringAccessor.append(builder, s, TagFrame.emptyFrame());
        frame.setReturnTag(receiverTag);
        return builder;
    }
}
