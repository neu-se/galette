package edu.neu.ccs.prl.galette.internal.runtime.mask;

import static edu.neu.ccs.prl.galette.internal.runtime.mask.JdkFloatingDecimalMasks.append;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrameFactory;
import org.objectweb.asm.Opcodes;

public final class SunFloatingDecimalMasks {
    @SuppressWarnings("unused")
    @MemberAccess(owner = "sun/misc/FloatingDecimal", name = "appendTo", opcode = Opcodes.INVOKESTATIC)
    private static void appendToInternal(double d, Appendable buf, TagFrame frame) {
        // Placeholder
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "sun/misc/FloatingDecimal", name = "appendTo", opcode = Opcodes.INVOKESTATIC)
    private static void appendToInternal(float f, Appendable buf, TagFrame frame) {
        // Placeholder
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "sun/misc/FloatingDecimal", name = "parseDouble", opcode = Opcodes.INVOKESTATIC)
    private static double parseDoubleInternal(String value, TagFrame frame) {
        // Placeholder
        return -1;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "sun/misc/FloatingDecimal", name = "parseFloat", opcode = Opcodes.INVOKESTATIC)
    private static float parseFloatInternal(String value, TagFrame frame) {
        // Placeholder
        return -1;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "sun/misc/FloatingDecimal", name = "toJavaFormatString", opcode = Opcodes.INVOKESTATIC)
    private static String toJavaFormatStringInternal(double d, TagFrame frame) {
        // Placeholder
        return "-1";
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "sun/misc/FloatingDecimal", name = "toJavaFormatString", opcode = Opcodes.INVOKESTATIC)
    private static String toJavaFormatStringInternal(float f, TagFrame frame) {
        // Placeholder
        return "-1";
    }

    @Mask(owner = "sun/misc/FloatingDecimal", name = "toJavaFormatString", type = MaskType.REPLACE, isStatic = true)
    public static String toJavaFormatString(double value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        String result = toJavaFormatStringInternal(value, TagFrameFactory.acquire(frame, Tag.emptyTag()));
        result = StringAccessor.setCharTags(result, valueTag);
        frame.setReturnTag(valueTag);
        return result;
    }

    @Mask(owner = "sun/misc/FloatingDecimal", name = "toJavaFormatString", type = MaskType.REPLACE, isStatic = true)
    public static String toJavaFormatString(float value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        String result = toJavaFormatStringInternal(value, TagFrameFactory.acquire(frame, Tag.emptyTag()));
        result = StringAccessor.setCharTags(result, valueTag);
        frame.setReturnTag(valueTag);
        return result;
    }

    @Mask(owner = "sun/misc/FloatingDecimal", name = "appendTo", type = MaskType.REPLACE, isStatic = true)
    public static void appendTo(double d, Appendable buf, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Tag bufTag = frame.get(1);
        StringBuilder builder = StringAccessor.newStringBuilder(TagFrameFactory.acquire(frame, Tag.emptyTag()));
        appendToInternal(d, builder, TagFrameFactory.acquire(frame, Tag.emptyTag(), Tag.emptyTag()));
        append(buf, builder, frame, valueTag, bufTag);
    }

    @Mask(owner = "sun/misc/FloatingDecimal", name = "appendTo", type = MaskType.REPLACE, isStatic = true)
    public static void appendTo(float f, Appendable buf, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Tag bufTag = frame.get(1);
        StringBuilder builder = StringAccessor.newStringBuilder(TagFrame.emptyFrame());
        appendToInternal(f, builder, TagFrameFactory.acquire(frame, Tag.emptyTag(), Tag.emptyTag()));
        append(buf, builder, frame, valueTag, bufTag);
    }

    @Mask(owner = "sun/misc/FloatingDecimal", name = "parseDouble", type = MaskType.REPLACE, isStatic = true)
    public static double parseDouble(String value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        double parsed = parseDoubleInternal(value, TagFrameFactory.acquire(frame, Tag.emptyTag()));
        Tag parsedTag = StringAccessor.getMergedTag(value, valueTag);
        frame.setReturnTag(parsedTag);
        return parsed;
    }

    @Mask(owner = "sun/misc/FloatingDecimal", name = "parseFloat", type = MaskType.REPLACE, isStatic = true)
    public static float parseFloat(String value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        float parsed = parseFloatInternal(value, TagFrameFactory.acquire(frame, Tag.emptyTag()));
        Tag parsedTag = StringAccessor.getMergedTag(value, valueTag);
        frame.setReturnTag(parsedTag);
        return parsed;
    }
}
