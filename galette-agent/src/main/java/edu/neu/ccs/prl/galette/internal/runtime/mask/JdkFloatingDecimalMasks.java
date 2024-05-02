package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import org.objectweb.asm.Opcodes;

public final class JdkFloatingDecimalMasks {
    @SuppressWarnings("unused")
    @MemberAccess(owner = "jdk/internal/math/FloatingDecimal", name = "appendTo", opcode = Opcodes.INVOKESTATIC)
    public static void appendToInternal(double d, Appendable buf, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "jdk/internal/math/FloatingDecimal", name = "appendTo", opcode = Opcodes.INVOKESTATIC)
    public static void appendToInternal(float f, Appendable buf, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @Mask(
            owner = "jdk/internal/math/FloatingDecimal",
            name = "toJavaFormatString",
            type = MaskType.POST_PROCESS,
            isStatic = true)
    public static String toJavaFormatString(String returnValue, double d, TagFrame frame) {
        return StringAccessor.setCharTags(returnValue, frame.dequeue());
    }

    @Mask(
            owner = "jdk/internal/math/FloatingDecimal",
            name = "toJavaFormatString",
            type = MaskType.POST_PROCESS,
            isStatic = true)
    public static String toJavaFormatString(String returnValue, float f, TagFrame frame) {
        return StringAccessor.setCharTags(returnValue, frame.dequeue());
    }

    @Mask(owner = "jdk/internal/math/FloatingDecimal", name = "appendTo", type = MaskType.REPLACE, isStatic = true)
    public static void appendTo(double d, Appendable buf, TagFrame frame) {
        StringBuffer buffer = StringAccessor.newStringBuilder(TagFrame.emptyFrame());
        appendToInternal(d, buffer, TagFrame.emptyFrame());
        appendToPost(buf, frame, buffer);
    }

    @Mask(owner = "jdk/internal/math/FloatingDecimal", name = "appendTo", type = MaskType.REPLACE, isStatic = true)
    public static void appendTo(float f, Appendable buf, TagFrame frame) {
        StringBuffer buffer = StringAccessor.newStringBuilder(TagFrame.emptyFrame());
        appendToInternal(f, buffer, TagFrame.emptyFrame());
        appendToPost(buf, frame, buffer);
    }

    @Mask(
            owner = "jdk/internal/math/FloatingDecimal",
            name = "parseDouble",
            type = MaskType.POST_PROCESS,
            isStatic = true)
    public static double parseDouble(double returnValue, String s, TagFrame frame) {
        Tag sTag = frame.dequeue();
        Tag returnTag = frame.getReturnTag();
        frame.setReturnTag(Tag.union(Tag.union(Tag.union(StringAccessor.getCharTags(s)), sTag), returnTag));
        return returnValue;
    }

    @Mask(
            owner = "jdk/internal/math/FloatingDecimal",
            name = "parseFloat",
            type = MaskType.POST_PROCESS,
            isStatic = true)
    public static float parseFloat(float returnValue, String s, TagFrame frame) {
        Tag sTag = frame.dequeue();
        Tag returnTag = frame.getReturnTag();
        frame.setReturnTag(Tag.union(Tag.union(Tag.union(StringAccessor.getCharTags(s)), sTag), returnTag));
        return returnValue;
    }

    static void appendToPost(Appendable buf, TagFrame frame, StringBuffer buffer) {
        Tag valueTag = frame.dequeue();
        Tag bufTag = frame.dequeue();
        String s = StringAccessor.toString(buffer, TagFrame.emptyFrame());
        s = StringAccessor.setCharTags(s, valueTag);
        StringAccessor.append(buf, s, frame.create(bufTag));
    }
}
