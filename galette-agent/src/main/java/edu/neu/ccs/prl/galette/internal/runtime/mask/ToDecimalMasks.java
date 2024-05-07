package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import org.objectweb.asm.Opcodes;

public final class ToDecimalMasks {
    @SuppressWarnings({"unused", "UnusedReturnValue"})
    @MemberAccess(owner = "jdk/internal/math/DoubleToDecimal", name = "appendDecimalTo", opcode = Opcodes.INVOKEVIRTUAL)
    static Appendable appendDecimalToInternal(Object receiver, double d, Appendable buf, TagFrame frame) {
        // Placeholder
        return buf;
    }

    @SuppressWarnings({"unused", "UnusedReturnValue"})
    @MemberAccess(owner = "jdk/internal/math/FloatToDecimal", name = "appendDecimalTo", opcode = Opcodes.INVOKEVIRTUAL)
    static Appendable appendDecimalToInternal(Object receiver, float f, Appendable buf, TagFrame frame) {
        // Placeholder
        return buf;
    }

    @Mask(owner = "jdk/internal/math/DoubleToDecimal", name = "toDecimalString", type = MaskType.POST_PROCESS)
    public static String toDecimalString(String returnValue, Object receiver, double v, TagFrame frame) {
        Tag tag = Tag.union(frame.getReturnTag(), frame.get(1));
        returnValue = StringAccessor.setCharTags(returnValue, tag);
        frame.setReturnTag(tag);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/math/DoubleToDecimal", name = "appendDecimalTo", type = MaskType.REPLACE)
    public static Appendable appendDecimalTo(Object receiver, double v, Appendable app, TagFrame frame) {
        Tag receiverTag = frame.get(0);
        Tag valueTag = frame.get(1);
        Tag bufTag = frame.get(2);
        StringBuilder builder = StringAccessor.newStringBuilder(TagFrame.disabled());
        appendDecimalToInternal(receiver, v, builder, TagFrame.disabled());
        JdkFloatingDecimalMasks.append(app, builder, frame, valueTag, bufTag);
        frame.setReturnTag(receiverTag);
        return app;
    }

    @Mask(owner = "jdk/internal/math/FloatToDecimal", name = "toDecimalString", type = MaskType.POST_PROCESS)
    public static String toDecimalString(String returnValue, Object receiver, float v, TagFrame frame) {
        Tag tag = Tag.union(frame.getReturnTag(), frame.get(1));
        returnValue = StringAccessor.setCharTags(returnValue, tag);
        frame.setReturnTag(tag);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/math/FloatToDecimal", name = "appendDecimalTo", type = MaskType.REPLACE)
    public static Appendable appendDecimalTo(Object receiver, float v, Appendable app, TagFrame frame) {
        Tag receiverTag = frame.get(0);
        Tag valueTag = frame.get(1);
        Tag bufTag = frame.get(2);
        StringBuilder builder = StringAccessor.newStringBuilder(TagFrame.disabled());
        appendDecimalToInternal(receiver, v, builder, TagFrame.disabled());
        JdkFloatingDecimalMasks.append(app, builder, frame, valueTag, bufTag);
        frame.setReturnTag(receiverTag);
        return app;
    }
}
