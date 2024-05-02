package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrameFactory;

public final class BooleanMasks {
    @Mask(owner = "java/lang/Boolean", name = "valueOf", isStatic = true)
    public static Boolean valueOf(boolean value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Boolean result;
        if (Tag.isEmpty(valueTag)) {
            result = BoxTypeAccessor.valueOf(value, TagFrameFactory.acquire(frame, Tag.emptyTag()));
        } else {
            result = BoxTypeAccessor.newBoolean(value, TagFrameFactory.acquire(frame, Tag.emptyTag(), valueTag));
        }
        frame.setReturnTag(valueTag);
        return result;
    }

    @Mask(owner = "java/lang/Boolean", name = "parseBoolean", isStatic = true)
    public static boolean parseBoolean(String value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        boolean parsed = BoxTypeAccessor.parseBoolean(value, TagFrameFactory.acquire(frame, Tag.emptyTag()));
        Tag parsedTag = StringAccessor.getMergedTag(value, valueTag);
        frame.setReturnTag(parsedTag);
        return parsed;
    }

    @Mask(owner = "java/lang/Boolean", name = "valueOf", isStatic = true)
    public static Boolean valueOfBoolean(String value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        boolean parsed = BoxTypeAccessor.parseBoolean(value, TagFrameFactory.acquire(frame, Tag.emptyTag()));
        Tag parsedTag = StringAccessor.getMergedTag(value, valueTag);
        return valueOf(parsed, TagFrameFactory.acquire(frame, parsedTag));
    }

    @Mask(owner = "java/lang/Boolean", name = "toString", isStatic = true, type = MaskType.POST_PROCESS)
    public static String toString(String returnValue, boolean value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Tag tag = Tag.union(frame.getReturnTag(), valueTag);
        String result = StringAccessor.setCharTags(returnValue, tag);
        frame.setReturnTag(tag);
        return result;
    }

    @Mask(owner = "java/lang/Boolean", name = "toString", type = MaskType.POST_PROCESS)
    public static String toString(String returnValue, Boolean receiver, TagFrame frame) {
        Tag returnTag = frame.getReturnTag();
        boolean value = BoxTypeAccessor.booleanValue(receiver, frame);
        Tag valueTag = frame.getReturnTag();
        Tag tag = Tag.union(returnTag, valueTag);
        String result = StringAccessor.setCharTags(returnValue, tag);
        frame.setReturnTag(tag);
        return result;
    }

    @Mask(owner = "java/lang/Boolean", name = "hashCode", isStatic = true, type = MaskType.POST_PROCESS)
    public static int hashCode(int returnValue, boolean value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Tag tag = Tag.union(frame.getReturnTag(), valueTag);
        frame.setReturnTag(tag);
        return returnValue;
    }

    @Mask(owner = "java/lang/String", name = "valueOf", isStatic = true, type = MaskType.POST_PROCESS)
    public static String stringValueOf(String returnValue, boolean value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Tag tag = Tag.union(frame.getReturnTag(), valueTag);
        String result = StringAccessor.setCharTags(returnValue, tag);
        frame.setReturnTag(tag);
        return result;
    }
}
