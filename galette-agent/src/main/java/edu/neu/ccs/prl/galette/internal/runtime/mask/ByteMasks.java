package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class ByteMasks {
    @Mask(owner = "java/lang/Byte", name = "valueOf", isStatic = true)
    public static Byte valueOf(byte value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        }
        TagFrame calleeFrame = frame.create(null, valueTag);
        return BoxTypeAccessor.newByte(value, calleeFrame);
    }
}
