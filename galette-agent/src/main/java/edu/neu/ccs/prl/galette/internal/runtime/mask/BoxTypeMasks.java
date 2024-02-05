package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

@SuppressWarnings("unused")
public final class BoxTypeMasks {
    @Mask(owner = "java/lang/Boolean", name = "valueOf", isStatic = true)
    public static Boolean valueOf(boolean value, TagFrame frame) {
        Tag valueTag = frame.pop();
        TagFrame calleeFrame = TagFrame.createForCall(frame).push(valueTag).push(Tag.getEmptyTag());
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newBoolean(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Byte", name = "valueOf", isStatic = true)
    public static Byte valueOf(byte value, TagFrame frame) {
        Tag valueTag = frame.pop();
        TagFrame calleeFrame = TagFrame.createForCall(frame).push(valueTag).push(Tag.getEmptyTag());
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newByte(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Character", name = "valueOf", isStatic = true)
    public static Character valueOf(char value, TagFrame frame) {
        Tag valueTag = frame.pop();
        TagFrame calleeFrame = TagFrame.createForCall(frame).push(valueTag).push(Tag.getEmptyTag());
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newCharacter(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Integer", name = "valueOf", isStatic = true)
    public static Integer valueOf(int value, TagFrame frame) {
        Tag valueTag = frame.pop();
        TagFrame calleeFrame = TagFrame.createForCall(frame).push(valueTag).push(Tag.getEmptyTag());
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newInteger(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Long", name = "valueOf", isStatic = true)
    public static Long valueOf(long value, TagFrame frame) {
        Tag valueTag = frame.pop();
        TagFrame calleeFrame = TagFrame.createForCall(frame).push(valueTag).push(Tag.getEmptyTag());
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newLong(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Short", name = "valueOf", isStatic = true)
    public static Short valueOf(short value, TagFrame frame) {
        Tag valueTag = frame.pop();
        TagFrame calleeFrame = TagFrame.createForCall(frame).push(valueTag).push(Tag.getEmptyTag());
        return Tag.isEmpty(valueTag)
                ? BoxTypeAccessor.valueOf(value, calleeFrame)
                : BoxTypeAccessor.newShort(value, calleeFrame);
    }
}
