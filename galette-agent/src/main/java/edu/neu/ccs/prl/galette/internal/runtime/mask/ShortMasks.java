package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class ShortMasks {
    @Mask(owner = "java/lang/Short", name = "valueOf", isStatic = true)
    public static Short valueOf(short value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Short result;
        if (Tag.isEmpty(valueTag)) {
            result = BoxTypeAccessor.valueOf(value, frame.create(Tag.emptyTag()));
        } else {
            result = BoxTypeAccessor.newShort(value, frame.create(Tag.emptyTag(), valueTag));
        }
        frame.setReturnTag(valueTag);
        return result;
    }
}
