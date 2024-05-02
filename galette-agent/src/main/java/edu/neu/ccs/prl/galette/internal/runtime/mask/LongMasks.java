package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.ArrayTagStore;
import edu.neu.ccs.prl.galette.internal.runtime.ArrayWrapper;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class LongMasks {
    @Mask(owner = "java/lang/Long", name = "valueOf", isStatic = true)
    public static Long valueOf(long value, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        if (Tag.isEmpty(valueTag)) {
            return BoxTypeAccessor.valueOf(value, TagFrame.create(frame));
        }
        TagFrame calleeFrame = frame.create(null, valueTag);
        return BoxTypeAccessor.newLong(value, calleeFrame);
    }

    @Mask(owner = "java/lang/Long", name = "parseLong", isStatic = true, type = MaskType.POST_PROCESS)
    public static long parseLong(long returnValue, String s, int radix, TagFrame frame) {
        Tag sTag = frame.dequeue();
        Tag radixTag = frame.dequeue();
        Tag charTags = Tag.union(StringAccessor.getCharTags(s));
        Tag returnTag = frame.getReturnTag();
        Tag tag = Tag.union(sTag, radixTag, charTags, returnTag);
        frame.setReturnTag(tag);
        return returnValue;
    }

    @Mask(owner = "java/lang/Long", name = "getChars", isStatic = true, type = MaskType.POST_PROCESS)
    @Mask(owner = "java/lang/StringUTF16", name = "getChars", isStatic = true, type = MaskType.POST_PROCESS)
    public static int getChars(int returnValue, long i, int index, byte[] buf, TagFrame frame) {
        Tag iTag = frame.dequeue();
        ArrayWrapper wrapper = ArrayTagStore.getWrapper(buf);
        if (wrapper != null) {
            for (int j = returnValue; j < index; j++) {
                wrapper.setElement(Tag.union(iTag, wrapper.getElement(j)), j);
            }
        }
        return returnValue;
    }

    @Mask(owner = "java/lang/Long", name = "toString", isStatic = true, type = MaskType.POST_PROCESS)
    public static String toString(String returnValue, long i, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        Tag tag = Tag.union(frame.getReturnTag(), valueTag);
        frame.setReturnTag(tag);
        return StringAccessor.setCharTags(returnValue, tag);
    }

    @Mask(owner = "java/lang/Long", name = "toString", isStatic = true, type = MaskType.POST_PROCESS)
    public static String toString(String returnValue, long i, int radix, TagFrame frame) {
        Tag valueTag = frame.dequeue();
        Tag radixTag = frame.dequeue();
        Tag tag = Tag.union(frame.getReturnTag(), valueTag, radixTag);
        frame.setReturnTag(tag);
        return StringAccessor.setCharTags(returnValue, tag);
    }
}
