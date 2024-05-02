package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrameFactory;

public final class ByteMasks {
    @Mask(owner = "java/lang/Byte", name = "valueOf", isStatic = true)
    public static Byte valueOf(byte value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Byte result;
        if (Tag.isEmpty(valueTag)) {
            result = BoxTypeAccessor.valueOf(value, TagFrameFactory.acquire(frame, Tag.emptyTag()));
        } else {
            result = BoxTypeAccessor.newByte(value, TagFrameFactory.acquire(frame, Tag.emptyTag(), valueTag));
        }
        frame.setReturnTag(valueTag);
        return result;
    }
}
