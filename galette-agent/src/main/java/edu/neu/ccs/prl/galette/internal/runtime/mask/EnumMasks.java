package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

@SuppressWarnings("unused")
public class EnumMasks {
    @Mask(owner = "java/lang/Enum", name = "valueOf", isStatic = true)
    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name, TagFrame frame) {
        Tag enumTypeTag = frame.pop();
        Tag nameTag = frame.pop();
        // TODO propagate from tainted characters in name to instance
        TagFrame calleeFrame = TagFrame.createForCall(frame).push(nameTag).push(enumTypeTag);
        T result = EnumAccessor.valueOf(enumType, name, calleeFrame);
        frame.setReturnTag(Tag.union(nameTag, calleeFrame.getReturnTag()));
        return result;
    }
}
