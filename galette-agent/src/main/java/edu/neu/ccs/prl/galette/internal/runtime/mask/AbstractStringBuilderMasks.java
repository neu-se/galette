package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrameFactory;

public final class AbstractStringBuilderMasks {
    @Mask(
            owner = "java/lang/AbstractStringBuilder",
            name = "append",
            type = MaskType.REPLACE,
            returnDescriptor = "Ljava/lang/AbstractStringBuilder;")
    public static Appendable append(Appendable receiver, boolean b, TagFrame frame) {
        Tag receiverTag = frame.get(0);
        Tag valueTag = frame.get(1);
        return append(receiver, b ? "true" : "false", frame, receiverTag, valueTag);
    }

    @Mask(
            owner = "java/lang/AbstractStringBuilder",
            name = "append",
            type = MaskType.REPLACE,
            returnDescriptor = "Ljava/lang/AbstractStringBuilder;")
    public static Appendable append(StringBuilder receiver, int value, TagFrame frame) {
        Tag receiverTag = frame.get(0);
        Tag valueTag = frame.get(1);
        String toAppend = BoxTypeAccessor.toString(value, TagFrameFactory.acquire(frame, Tag.emptyTag()));
        return append(receiver, toAppend, frame, receiverTag, valueTag);
    }

    @Mask(
            owner = "java/lang/AbstractStringBuilder",
            name = "append",
            type = MaskType.REPLACE,
            returnDescriptor = "Ljava/lang/AbstractStringBuilder;")
    public static Appendable append(StringBuilder receiver, long value, TagFrame frame) {
        Tag receiverTag = frame.get(0);
        Tag valueTag = frame.get(1);
        String toAppend = BoxTypeAccessor.toString(value, TagFrameFactory.acquire(frame, Tag.emptyTag()));
        return append(receiver, toAppend, frame, receiverTag, valueTag);
    }

    private static Appendable append(
            Appendable receiver, String toAppend, TagFrame frame, Tag receiverTag, Tag valueTag) {
        toAppend = StringAccessor.setCharTags(toAppend, valueTag);
        StringAccessor.append(receiver, toAppend, TagFrameFactory.acquire(frame, receiverTag, Tag.emptyTag()));
        frame.setReturnTag(receiverTag);
        return receiver;
    }
}
