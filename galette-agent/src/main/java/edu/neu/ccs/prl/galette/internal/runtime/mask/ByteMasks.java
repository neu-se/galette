package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class ByteMasks {
    @Mask(owner = "java/lang/Byte", name = "valueOf", isStatic = true)
    public static Byte valueOf(byte value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Byte result;
        if (Tag.isEmpty(valueTag)) {
            result = BoxTypeAccessor.valueOf(value, frame.create(Tag.emptyTag()));
        } else {
            result = BoxTypeAccessor.newByte(value, frame.create(Tag.emptyTag(), valueTag));
        }
        frame.setReturnTag(valueTag);
        return result;
    }
}
