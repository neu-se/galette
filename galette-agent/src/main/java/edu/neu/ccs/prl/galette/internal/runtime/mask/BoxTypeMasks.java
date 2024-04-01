package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class BoxTypeMasks {
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

    @Mask(owner = "java/lang/Byte", name = "valueOf", isStatic = true)
    public static Byte valueOf(byte value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        }
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        return BoxTypeAccessor.newByte(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Character", name = "valueOf", isStatic = true)
    public static Character valueOf(char value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        }
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        return BoxTypeAccessor.newCharacter(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Integer", name = "valueOf", isStatic = true)
    public static Integer valueOf(int value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        }
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        return BoxTypeAccessor.newInteger(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Long", name = "valueOf", isStatic = true)
    public static Long valueOf(long value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        }
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        return BoxTypeAccessor.newLong(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Short", name = "valueOf", isStatic = true)
    public static Short valueOf(short value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        }
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        return BoxTypeAccessor.newShort(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Float", name = "valueOf", isStatic = true)
    public static Float valueOf(float value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        }
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        return BoxTypeAccessor.newFloat(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Double", name = "valueOf", isStatic = true)
    public static Double valueOf(double value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        }
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        return BoxTypeAccessor.newDouble(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Boolean", name = "parseBoolean", isStatic = true)
    public static boolean parseBoolean(String value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        boolean parsed = BoxTypeAccessor.parseBoolean(value, TagFrame.create(frame));
        Tag parsedTag = Tag.union(Tag.union(StringAccessor.getCharTags(value)), valueTag);
        frame.setReturnTag(parsedTag);
        return parsed;
    }

    @Mask(owner = "java/lang/Integer", name = "parseInt", isStatic = true)
    public static int parseInt(String value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        int parsed = BoxTypeAccessor.parseInt(value, TagFrame.create(frame));
        Tag parsedTag = Tag.union(Tag.union(StringAccessor.getCharTags(value)), valueTag);
        frame.setReturnTag(parsedTag);
        return parsed;
    }

    @Mask(owner = "java/lang/Long", name = "parseLong", isStatic = true)
    public static long parseLong(String value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        long parsed = BoxTypeAccessor.parseLong(value, TagFrame.create(frame));
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
}
