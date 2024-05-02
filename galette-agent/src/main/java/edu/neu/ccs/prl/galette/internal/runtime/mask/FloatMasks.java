package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrameFactory;

public final class FloatMasks {
    @Mask(owner = "java/lang/Float", name = "valueOf", isStatic = true)
    public static Float valueOf(float value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Float result;
        if (Tag.isEmpty(valueTag)) {
            result = BoxTypeAccessor.valueOf(value, TagFrameFactory.acquire(frame, Tag.emptyTag()));
        } else {
            result = BoxTypeAccessor.newFloat(value, TagFrameFactory.acquire(frame, Tag.emptyTag(), valueTag));
        }
        frame.setReturnTag(valueTag);
        return result;
    }

    @Mask(owner = "java/lang/Float", name = "floatToRawIntBits", isStatic = true, type = MaskType.POST_PROCESS)
    @Mask(owner = "java/lang/Float", name = "floatToIntBits", isStatic = true, type = MaskType.POST_PROCESS)
    public static int floatToIntBits(int returnValue, float value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        frame.setReturnTag(valueTag);
        return returnValue;
    }

    @Mask(owner = "java/lang/Float", name = "intBitsToFloat", isStatic = true, type = MaskType.POST_PROCESS)
    public static float intBitsToFloat(float returnValue, int bits, TagFrame frame) {
        Tag valueTag = frame.get(0);
        frame.setReturnTag(valueTag);
        return returnValue;
    }
}
