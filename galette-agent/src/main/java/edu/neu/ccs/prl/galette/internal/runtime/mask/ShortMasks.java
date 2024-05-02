package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class ShortMasks {
    @Mask(owner = "java/lang/Short", name = "valueOf", isStatic = true)
    public static Short valueOf(short value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        } else {
            TagFrame calleeFrame = frame.create(null, valueTag);
            return BoxTypeAccessor.newShort(value, calleeFrame);
        }
    }
}
