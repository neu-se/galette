package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.ArrayTagStore;
import edu.neu.ccs.prl.galette.internal.runtime.ArrayWrapper;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class LongMasks {
    @Mask(owner = "java/lang/Long", name = "valueOf", isStatic = true)
    public static Long valueOf(long value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Long result;
        if (Tag.isEmpty(valueTag)) {
            result = BoxTypeAccessor.valueOf(value, frame.create(Tag.emptyTag()));
        } else {
            result = BoxTypeAccessor.newLong(value, frame.create(Tag.emptyTag(), valueTag));
        }
        frame.setReturnTag(valueTag);
        return result;
    }

    @Mask(owner = "java/lang/Long", name = "parseLong", isStatic = true, type = MaskType.POST_PROCESS)
    public static long parseLong(long returnValue, String s, int radix, TagFrame frame) {
        Tag sTag = frame.get(0);
        Tag radixTag = frame.get(1);
        Tag returnTag = frame.getReturnTag();
        Tag charTags = StringAccessor.getMergedTag(s, sTag);
        Tag tag = Tag.union(radixTag, charTags, returnTag);
        frame.setReturnTag(tag);
        return returnValue;
    }

    @Mask(owner = "java/lang/Long", name = "getChars", isStatic = true, type = MaskType.POST_PROCESS)
    @Mask(owner = "java/lang/StringUTF16", name = "getChars", isStatic = true, type = MaskType.POST_PROCESS)
    public static int getChars(int returnValue, long i, int index, byte[] buf, TagFrame frame) {
        Tag iTag = frame.get(0);
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
        Tag valueTag = frame.get(0);
        Tag tag = Tag.union(frame.getReturnTag(), valueTag);
        String result = StringAccessor.setCharTags(returnValue, tag);
        frame.setReturnTag(tag);
        return result;
    }

    @Mask(owner = "java/lang/Long", name = "toString", isStatic = true, type = MaskType.POST_PROCESS)
    public static String toString(String returnValue, long i, int radix, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Tag radixTag = frame.get(1);
        Tag tag = Tag.union(frame.getReturnTag(), valueTag, radixTag);
        String result = StringAccessor.setCharTags(returnValue, tag);
        frame.setReturnTag(tag);
        return result;
    }
}
