package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class BooleanMasks {
    @Mask(owner = "java/lang/Boolean", name = "valueOf", isStatic = true)
    public static Boolean valueOf(boolean value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        }
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        return BoxTypeAccessor.newBoolean(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Boolean", name = "parseBoolean", isStatic = true)
    public static boolean parseBoolean(String value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        boolean parsed = BoxTypeAccessor.parseBoolean(value, TagFrame.create(frame));
        Tag parsedTag = Tag.union(Tag.union(StringAccessor.getCharTags(value)), valueTag);
        frame.setReturnTag(parsedTag);
        return parsed;
    }

    @Mask(owner = "java/lang/Boolean", name = "valueOf", isStatic = true)
    public static Boolean valueOfBoolean(String value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        boolean parsed = BoxTypeAccessor.parseBoolean(value, TagFrame.create(frame));
        Tag parsedTag = Tag.union(Tag.union(StringAccessor.getCharTags(value)), valueTag);
        frame.setReturnTag(parsedTag);
        if (Tag.isEmpty(parsedTag)) {
            return BoxTypeAccessor.valueOf(parsed, TagFrame.create(frame));
        }
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(parsedTag);
        return BoxTypeAccessor.newBoolean(parsed, calleeFrame);
    }

    @Mask(owner = "java/lang/Boolean", name = "toString", isStatic = true, type = MaskType.POST_PROCESS)
    public static String toString(String returnValue, boolean value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        Tag tag = Tag.union(frame.getReturnTag(), valueTag);
        frame.setReturnTag(tag);
        return StringAccessor.setCharTags(returnValue, tag);
    }

    @Mask(owner = "java/lang/Boolean", name = "toString", type = MaskType.POST_PROCESS)
    public static String toString(String returnValue, Boolean receiver, TagFrame frame) {
        BoxTypeAccessor.booleanValue(receiver, frame);
        return StringAccessor.setCharTags(returnValue, frame.getReturnTag());
    }

    @Mask(owner = "java/lang/String", name = "valueOf", isStatic = true, type = MaskType.POST_PROCESS)
    public static String stringValueOf(String returnValue, boolean value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        Tag tag = Tag.union(frame.getReturnTag(), valueTag);
        frame.setReturnTag(tag);
        return StringAccessor.setCharTags(returnValue, tag);
    }

    @Mask(owner = "java/lang/Boolean", name = "hashCode", isStatic = true, type = MaskType.POST_PROCESS)
    public static int hashCode(int returnValue, boolean value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        Tag tag = Tag.union(frame.getReturnTag(), valueTag);
        frame.setReturnTag(tag);
        return returnValue;
    }
}
