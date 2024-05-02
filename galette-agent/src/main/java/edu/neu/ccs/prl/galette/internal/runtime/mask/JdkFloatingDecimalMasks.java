package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import org.objectweb.asm.Opcodes;

public final class JdkFloatingDecimalMasks {
    @SuppressWarnings("unused")
    @MemberAccess(owner = "jdk/internal/math/FloatingDecimal", name = "appendTo", opcode = Opcodes.INVOKESTATIC)
    private static void appendToInternal(double d, Appendable buf, TagFrame frame) {
        // Placeholder
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "jdk/internal/math/FloatingDecimal", name = "appendTo", opcode = Opcodes.INVOKESTATIC)
    private static void appendToInternal(float f, Appendable buf, TagFrame frame) {
        // Placeholder
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "jdk/internal/math/FloatingDecimal", name = "parseDouble", opcode = Opcodes.INVOKESTATIC)
    private static double parseDoubleInternal(String value, TagFrame frame) {
        // Placeholder
        return -1;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "jdk/internal/math/FloatingDecimal", name = "parseFloat", opcode = Opcodes.INVOKESTATIC)
    private static float parseFloatInternal(String value, TagFrame frame) {
        // Placeholder
        return -1;
    }

    @SuppressWarnings("unused")
    @MemberAccess(
            owner = "jdk/internal/math/FloatingDecimal",
            name = "toJavaFormatString",
            opcode = Opcodes.INVOKESTATIC)
    private static String toJavaFormatStringInternal(double d, TagFrame frame) {
        // Placeholder
        return "-1";
    }

    @SuppressWarnings("unused")
    @MemberAccess(
            owner = "jdk/internal/math/FloatingDecimal",
            name = "toJavaFormatString",
            opcode = Opcodes.INVOKESTATIC)
    private static String toJavaFormatStringInternal(float f, TagFrame frame) {
        // Placeholder
        return "-1";
    }

    @Mask(
            owner = "jdk/internal/math/FloatingDecimal",
            name = "toJavaFormatString",
            type = MaskType.REPLACE,
            isStatic = true)
    public static String toJavaFormatString(double value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        String result = toJavaFormatStringInternal(value, frame.create(Tag.emptyTag()));
        result = StringAccessor.setCharTags(result, valueTag);
        frame.setReturnTag(valueTag);
        return result;
    }

    @Mask(
            owner = "jdk/internal/math/FloatingDecimal",
            name = "toJavaFormatString",
            type = MaskType.REPLACE,
            isStatic = true)
    public static String toJavaFormatString(float value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        String result = toJavaFormatStringInternal(value, frame.create(Tag.emptyTag()));
        result = StringAccessor.setCharTags(result, valueTag);
        frame.setReturnTag(valueTag);
        return result;
    }

    @Mask(owner = "jdk/internal/math/FloatingDecimal", name = "appendTo", type = MaskType.REPLACE, isStatic = true)
    public static void appendTo(double d, Appendable buf, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Tag bufTag = frame.get(1);
        StringBuilder builder = StringAccessor.newStringBuilder(frame.create(Tag.emptyTag()));
        appendToInternal(d, builder, frame.create(Tag.emptyTag(), Tag.emptyTag()));
        append(buf, builder, frame, valueTag, bufTag);
    }

    @Mask(owner = "jdk/internal/math/FloatingDecimal", name = "appendTo", type = MaskType.REPLACE, isStatic = true)
    public static void appendTo(float f, Appendable buf, TagFrame frame) {
        Tag valueTag = frame.get(0);
        Tag bufTag = frame.get(1);
        StringBuilder builder = StringAccessor.newStringBuilder(TagFrame.emptyFrame());
        appendToInternal(f, builder, frame.create(Tag.emptyTag(), Tag.emptyTag()));
        append(buf, builder, frame, valueTag, bufTag);
    }

    @Mask(owner = "jdk/internal/math/FloatingDecimal", name = "parseDouble", type = MaskType.REPLACE, isStatic = true)
    public static double parseDouble(String value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        double parsed = parseDoubleInternal(value, frame.create(Tag.emptyTag()));
        Tag parsedTag = StringAccessor.getMergedTag(value, valueTag);
        frame.setReturnTag(parsedTag);
        return parsed;
    }

    @Mask(owner = "jdk/internal/math/FloatingDecimal", name = "parseFloat", type = MaskType.REPLACE, isStatic = true)
    public static float parseFloat(String value, TagFrame frame) {
        Tag valueTag = frame.get(0);
        float parsed = parseFloatInternal(value, frame.create(Tag.emptyTag()));
        Tag parsedTag = StringAccessor.getMergedTag(value, valueTag);
        frame.setReturnTag(parsedTag);
        return parsed;
    }

    static void append(Appendable buf, StringBuilder builder, TagFrame frame, Tag valueTag, Tag bufTag) {
        String toAppend = StringAccessor.toString(builder, frame.create(Tag.emptyTag()));
        toAppend = StringAccessor.setCharTags(toAppend, valueTag);
        StringAccessor.append(buf, toAppend, frame.create(bufTag, Tag.emptyTag()));
    }
}
