package edu.neu.ccs.prl.galette.internal.runtime.mask;

import static edu.neu.ccs.prl.galette.internal.runtime.mask.JdkFloatingDecimalMasks.appendToPost;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import org.objectweb.asm.Opcodes;

public final class ToDecimalMasks {
    @SuppressWarnings({"unused", "UnusedReturnValue"})
    @MemberAccess(owner = "jdk/internal/math/DoubleToDecimal", name = "appendDecimalTo", opcode = Opcodes.INVOKEVIRTUAL)
    static Appendable appendDecimalToInternal(Object receiver, double d, Appendable buf, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @SuppressWarnings({"unused", "UnusedReturnValue"})
    @MemberAccess(owner = "jdk/internal/math/FloatToDecimal", name = "appendDecimalTo", opcode = Opcodes.INVOKEVIRTUAL)
    static Appendable appendDecimalToInternal(Object receiver, float f, Appendable buf, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @Mask(owner = "jdk/internal/math/DoubleToDecimal", name = "toDecimalString", type = MaskType.POST_PROCESS)
    public static String toDecimalString(String returnValue, Object receiver, double v, TagFrame frame) {
        frame.dequeue();
        return StringAccessor.setCharTags(returnValue, frame.dequeue());
    }

    @Mask(owner = "jdk/internal/math/DoubleToDecimal", name = "appendDecimalTo", type = MaskType.REPLACE)
    public static Appendable appendDecimalTo(Object receiver, double v, Appendable app, TagFrame frame) {
        frame.dequeue();
        StringBuffer buffer = StringAccessor.newStringBuilder(new TagFrame(null));
        appendDecimalToInternal(receiver, v, buffer, new TagFrame(null));
        appendToPost(app, frame, buffer);
        return app;
    }

    @Mask(owner = "jdk/internal/math/FloatToDecimal", name = "toDecimalString", type = MaskType.POST_PROCESS)
    public static String toDecimalString(String returnValue, Object receiver, float v, TagFrame frame) {
        frame.dequeue();
        return StringAccessor.setCharTags(returnValue, frame.dequeue());
    }

    @Mask(owner = "jdk/internal/math/FloatToDecimal", name = "appendDecimalTo", type = MaskType.REPLACE)
    public static Appendable appendDecimalTo(Object receiver, float v, Appendable app, TagFrame frame) {
        frame.dequeue();
        StringBuffer buffer = StringAccessor.newStringBuilder(new TagFrame(null));
        appendDecimalToInternal(receiver, v, buffer, new TagFrame(null));
        appendToPost(app, frame, buffer);
        return app;
    }
}
