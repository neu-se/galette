package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.symbolic.SymbolicExecutionListener;
import edu.neu.ccs.prl.galette.internal.runtime.symbolic.SymbolicListener;

/**
 * Post-process masks for {@link String} methods that fire
 * {@link SymbolicListener} callbacks, letting symbolic-execution front-ends
 * attach or replace the tag carried by the method's return value.
 *
 * <p><em>Important:</em> when no listener is installed, these masks are
 * intentional no-ops — they do not touch {@link TagFrame#setReturnTag} and
 * thereby preserve Galette's default data-flow propagation (e.g., char-tag
 * → length-result). Only a live listener may override the default tag.
 */
public final class StringSymbolicMasks {

    private StringSymbolicMasks() {
        throw new AssertionError();
    }

    @Mask(owner = "java/lang/String", name = "equals", type = MaskType.POST_PROCESS)
    public static boolean equalsMask(boolean returnValue, String receiver, Object other, TagFrame frame) {
        SymbolicExecutionListener l = SymbolicListener.getListener();
        if (l == null) {
            return returnValue;
        }
        Tag receiverTag = frame.get(0);
        Tag otherTag = frame.get(1);
        Tag[] receiverCharTags = receiver == null ? null : StringAccessor.getCharTags(receiver, frame);
        Tag[] otherCharTags = (other instanceof String) ? StringAccessor.getCharTags((String) other, frame) : null;
        Tag result = SymbolicListener.onStringEquals(
                returnValue, receiver, other, receiverTag, otherTag, receiverCharTags, otherCharTags);
        frame.setReturnTag(result);
        return returnValue;
    }

    @Mask(owner = "java/lang/String", name = "startsWith", type = MaskType.POST_PROCESS)
    public static boolean startsWithMask(boolean returnValue, String receiver, String prefix, TagFrame frame) {
        SymbolicExecutionListener l = SymbolicListener.getListener();
        if (l == null) {
            return returnValue;
        }
        Tag receiverTag = frame.get(0);
        Tag prefixTag = frame.get(1);
        Tag[] receiverCharTags = receiver == null ? null : StringAccessor.getCharTags(receiver, frame);
        Tag[] prefixCharTags = prefix == null ? null : StringAccessor.getCharTags(prefix, frame);
        Tag result = SymbolicListener.onStringStartsWith(
                returnValue,
                receiver,
                prefix,
                0,
                receiverTag,
                prefixTag,
                Tag.emptyTag(),
                receiverCharTags,
                prefixCharTags);
        frame.setReturnTag(result);
        return returnValue;
    }

    @Mask(owner = "java/lang/String", name = "startsWith", type = MaskType.POST_PROCESS)
    public static boolean startsWithOffsetMask(
            boolean returnValue, String receiver, String prefix, int offset, TagFrame frame) {
        SymbolicExecutionListener l = SymbolicListener.getListener();
        if (l == null) {
            return returnValue;
        }
        Tag receiverTag = frame.get(0);
        Tag prefixTag = frame.get(1);
        Tag offsetTag = frame.get(2);
        Tag[] receiverCharTags = receiver == null ? null : StringAccessor.getCharTags(receiver, frame);
        Tag[] prefixCharTags = prefix == null ? null : StringAccessor.getCharTags(prefix, frame);
        Tag result = SymbolicListener.onStringStartsWith(
                returnValue,
                receiver,
                prefix,
                offset,
                receiverTag,
                prefixTag,
                offsetTag,
                receiverCharTags,
                prefixCharTags);
        frame.setReturnTag(result);
        return returnValue;
    }

    @Mask(owner = "java/lang/String", name = "endsWith", type = MaskType.POST_PROCESS)
    public static boolean endsWithMask(boolean returnValue, String receiver, String suffix, TagFrame frame) {
        SymbolicExecutionListener l = SymbolicListener.getListener();
        if (l == null) {
            return returnValue;
        }
        Tag receiverTag = frame.get(0);
        Tag suffixTag = frame.get(1);
        Tag[] receiverCharTags = receiver == null ? null : StringAccessor.getCharTags(receiver, frame);
        Tag[] suffixCharTags = suffix == null ? null : StringAccessor.getCharTags(suffix, frame);
        Tag result = SymbolicListener.onStringEndsWith(
                returnValue, receiver, suffix, receiverTag, suffixTag, receiverCharTags, suffixCharTags);
        frame.setReturnTag(result);
        return returnValue;
    }

    @Mask(owner = "java/lang/String", name = "contains", type = MaskType.POST_PROCESS)
    public static boolean containsMask(boolean returnValue, String receiver, CharSequence seq, TagFrame frame) {
        SymbolicExecutionListener l = SymbolicListener.getListener();
        if (l == null) {
            return returnValue;
        }
        Tag receiverTag = frame.get(0);
        Tag seqTag = frame.get(1);
        Tag[] receiverCharTags = receiver == null ? null : StringAccessor.getCharTags(receiver, frame);
        Tag result =
                SymbolicListener.onStringContains(returnValue, receiver, seq, receiverTag, seqTag, receiverCharTags);
        frame.setReturnTag(result);
        return returnValue;
    }

    @Mask(owner = "java/lang/String", name = "indexOf", type = MaskType.POST_PROCESS)
    public static int indexOfStringMask(int returnValue, String receiver, String needle, TagFrame frame) {
        SymbolicExecutionListener l = SymbolicListener.getListener();
        if (l == null) {
            return returnValue;
        }
        Tag receiverTag = frame.get(0);
        Tag needleTag = frame.get(1);
        Tag[] receiverCharTags = receiver == null ? null : StringAccessor.getCharTags(receiver, frame);
        Tag[] needleCharTags = needle == null ? null : StringAccessor.getCharTags(needle, frame);
        Tag result = SymbolicListener.onStringIndexOf(
                returnValue, receiver, needle, receiverTag, needleTag, receiverCharTags, needleCharTags);
        frame.setReturnTag(result);
        return returnValue;
    }

    @Mask(owner = "java/lang/String", name = "length", type = MaskType.POST_PROCESS)
    public static int lengthMask(int returnValue, String receiver, TagFrame frame) {
        SymbolicExecutionListener l = SymbolicListener.getListener();
        if (l == null) {
            return returnValue;
        }
        Tag receiverTag = frame.get(0);
        Tag[] receiverCharTags = receiver == null ? null : StringAccessor.getCharTags(receiver, frame);
        Tag result = SymbolicListener.onStringLength(returnValue, receiver, receiverTag, receiverCharTags);
        frame.setReturnTag(result);
        return returnValue;
    }

    @Mask(owner = "java/lang/String", name = "charAt", type = MaskType.POST_PROCESS)
    public static char charAtMask(char returnValue, String receiver, int index, TagFrame frame) {
        SymbolicExecutionListener l = SymbolicListener.getListener();
        if (l == null) {
            return returnValue;
        }
        Tag receiverTag = frame.get(0);
        Tag indexTag = frame.get(1);
        Tag[] receiverCharTags = receiver == null ? null : StringAccessor.getCharTags(receiver, frame);
        Tag result =
                SymbolicListener.onStringCharAt(returnValue, receiver, index, receiverTag, indexTag, receiverCharTags);
        frame.setReturnTag(result);
        return returnValue;
    }
}
