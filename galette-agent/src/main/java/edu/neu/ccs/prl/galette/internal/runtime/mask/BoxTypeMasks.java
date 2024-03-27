package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class BoxTypeMasks {
    @Mask(owner = "java/lang/Boolean", name = "valueOf", isStatic = true)
    public static Boolean valueOf(boolean value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        frame.setReturnTag(valueTag);
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newBoolean(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Byte", name = "valueOf", isStatic = true)
    public static Byte valueOf(byte value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        frame.setReturnTag(valueTag);
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newByte(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Character", name = "valueOf", isStatic = true)
    public static Character valueOf(char value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        frame.setReturnTag(valueTag);
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newCharacter(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Integer", name = "valueOf", isStatic = true)
    public static Integer valueOf(int value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        frame.setReturnTag(valueTag);
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newInteger(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Long", name = "valueOf", isStatic = true)
    public static Long valueOf(long value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        frame.setReturnTag(valueTag);
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newLong(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Short", name = "valueOf", isStatic = true)
    public static Short valueOf(short value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        frame.setReturnTag(valueTag);
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newShort(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Float", name = "valueOf", isStatic = true)
    public static Float valueOf(float value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        frame.setReturnTag(valueTag);
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newFloat(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Double", name = "valueOf", isStatic = true)
    public static Double valueOf(double value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        TagFrame calleeFrame = TagFrame.create(frame).enqueue(Tag.getEmptyTag()).enqueue(valueTag);
        frame.setReturnTag(valueTag);
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newDouble(value, calleeFrame);
    }
}
