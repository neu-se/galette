package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class FloatMasks {
    @Mask(owner = "java/lang/Float", name = "valueOf", isStatic = true)
    public static Float valueOf(float value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        }
        TagFrame calleeFrame = frame.create(null, valueTag);
        return BoxTypeAccessor.newFloat(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Float", name = "floatToRawIntBits", isStatic = true, type = MaskType.POST_PROCESS)
    @Mask(owner = "java/lang/Float", name = "floatToIntBits", isStatic = true, type = MaskType.POST_PROCESS)
    public static int floatToIntBits(int returnValue, float value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        return returnValue;
    }

    @Mask(owner = "java/lang/Float", name = "intBitsToFloat", isStatic = true, type = MaskType.POST_PROCESS)
    public static float intBitsToFloat(float returnValue, int bits, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        return returnValue;
    }
}
