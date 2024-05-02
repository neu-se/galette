package edu.neu.ccs.prl.galette.internal.runtime.mask;

import static edu.neu.ccs.prl.galette.internal.runtime.mask.JdkFloatingDecimalMasks.appendToPost;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import org.objectweb.asm.Opcodes;

public final class SunFloatingDecimalMasks {
    @SuppressWarnings("unused")
    @MemberAccess(owner = "sun/misc/FloatingDecimal", name = "appendTo", opcode = Opcodes.INVOKESTATIC)
    public static void appendToInternal(double d, Appendable buf, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "sun/misc/FloatingDecimal", name = "appendTo", opcode = Opcodes.INVOKESTATIC)
    public static void appendToInternal(float f, Appendable buf, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @Mask(
            owner = "sun/misc/FloatingDecimal",
            name = "toJavaFormatString",
            type = MaskType.POST_PROCESS,
            isStatic = true)
    public static String toJavaFormatString(String returnValue, double d, TagFrame frame) {
        return StringAccessor.setCharTags(returnValue, frame.dequeue());
    }

    @Mask(
            owner = "sun/misc/FloatingDecimal",
            name = "toJavaFormatString",
            type = MaskType.POST_PROCESS,
            isStatic = true)
    public static String toJavaFormatString(String returnValue, float f, TagFrame frame) {
        return StringAccessor.setCharTags(returnValue, frame.dequeue());
    }

    @Mask(owner = "sun/misc/FloatingDecimal", name = "appendTo", type = MaskType.REPLACE, isStatic = true)
    public static void appendTo(double d, Appendable buf, TagFrame frame) {
        StringBuffer buffer = StringAccessor.newStringBuilder(TagFrame.emptyFrame());
        appendToInternal(d, buffer, TagFrame.emptyFrame());
        appendToPost(buf, frame, buffer);
    }

    @Mask(owner = "sun/misc/FloatingDecimal", name = "appendTo", type = MaskType.REPLACE, isStatic = true)
    public static void appendTo(float f, Appendable buf, TagFrame frame) {
        StringBuffer buffer = StringAccessor.newStringBuilder(TagFrame.emptyFrame());
        appendToInternal(f, buffer, TagFrame.emptyFrame());
        appendToPost(buf, frame, buffer);
    }

    @Mask(owner = "sun/misc/FloatingDecimal", name = "parseDouble", type = MaskType.POST_PROCESS, isStatic = true)
    public static double parseDouble(float returnValue, String s, TagFrame frame) throws NumberFormatException {
        Tag sTag = frame.dequeue();
        Tag returnTag = frame.getReturnTag();
        frame.setReturnTag(Tag.union(Tag.union(Tag.union(StringAccessor.getCharTags(s)), sTag), returnTag));
        return returnValue;
    }

    @Mask(owner = "sun/misc/FloatingDecimal", name = "parseFloat", type = MaskType.POST_PROCESS, isStatic = true)
    public static float parseFloat(float returnValue, String s, TagFrame frame) throws NumberFormatException {
        Tag sTag = frame.dequeue();
        Tag returnTag = frame.getReturnTag();
        frame.setReturnTag(Tag.union(Tag.union(Tag.union(StringAccessor.getCharTags(s)), sTag), returnTag));
        return returnValue;
    }
}
