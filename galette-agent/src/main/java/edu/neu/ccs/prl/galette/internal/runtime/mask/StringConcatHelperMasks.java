package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import org.objectweb.asm.Opcodes;

public final class StringConcatHelperMasks {
    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/StringConcatHelper", name = "prepend", opcode = Opcodes.INVOKESTATIC)
    private static int prepend(int index, byte[] buf, byte coder, String value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @Mask(owner = "java/lang/StringConcatHelper", name = "prepend", type = MaskType.REPLACE, isStatic = true)
    public static int prepend(int index, byte[] buf, byte coder, boolean value, TagFrame frame) {
        Tag indexTag = frame.dequeue();
        Tag bufTag = frame.dequeue();
        Tag coderTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        String s = StringAccessor.setCharTags(value ? "true" : "false", valueTag);
        Tag sTag = Tag.getEmptyTag();
        TagFrame childFrame = new TagFrame(frame)
                .enqueue(indexTag)
                .enqueue(bufTag)
                .enqueue(coderTag)
                .enqueue(sTag);
        int result = prepend(index, buf, coder, s, childFrame);
        frame.setReturnTag(childFrame.getReturnTag());
        return result;
    }
}
