package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class DoubleMasks {
    @Mask(owner = "java/lang/Double", name = "valueOf", isStatic = true)
    public static Double valueOf(double value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        }
        TagFrame calleeFrame = frame.create(null, valueTag);
        return BoxTypeAccessor.newDouble(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Double", name = "doubleToRawLongBits", isStatic = true, type = MaskType.POST_PROCESS)
    @Mask(owner = "java/lang/Double", name = "doubleToLongBits", isStatic = true, type = MaskType.POST_PROCESS)
    public static long doubleToIntBits(long returnValue, double value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        return returnValue;
    }

    @Mask(owner = "java/lang/Double", name = "longBitsToDouble", isStatic = true, type = MaskType.POST_PROCESS)
    public static double longBitsToDouble(double returnValue, long bits, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        return returnValue;
    }
}
