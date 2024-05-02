package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import org.objectweb.asm.Opcodes;

public final class StringConcatHelperMasks {
    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/StringConcatHelper", name = "prepend", opcode = Opcodes.INVOKESTATIC)
    private static int prepend(int index, byte[] buf, byte coder, String value, TagFrame frame) {
        // Placeholder
        return -1;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/StringConcatHelper", name = "prepend", opcode = Opcodes.INVOKESTATIC)
    public static long prepend(long indexCoder, byte[] buf, String value, TagFrame frame) {
        // Placeholder
        return -1L;
    }

    @Mask(owner = "java/lang/StringConcatHelper", name = "prepend", type = MaskType.REPLACE, isStatic = true)
    public static int prepend(int index, byte[] buf, byte coder, boolean value, TagFrame frame) {
        Tag indexTag = frame.dequeue();
        Tag bufTag = frame.dequeue();
        Tag coderTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        String s = StringAccessor.setCharTags(value ? "true" : "false", valueTag);
        TagFrame childFrame = frame.create(indexTag, bufTag, coderTag, valueTag);
        int result = prepend(index, buf, coder, s, childFrame);
        frame.setReturnTag(childFrame.getReturnTag());
        return result;
    }

    @Mask(owner = "java/lang/StringConcatHelper", name = "prepend", type = MaskType.REPLACE, isStatic = true)
    public static long prepend(long indexCoder, byte[] buf, boolean value, TagFrame frame) {
        Tag indexCoderTag = frame.dequeue();
        Tag bufTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        String s = StringAccessor.setCharTags(value ? "true" : "false", valueTag);
        TagFrame childFrame = frame.create(indexCoderTag, bufTag, valueTag);
        long result = prepend(indexCoder, buf, s, childFrame);
        frame.setReturnTag(childFrame.getReturnTag());
        return result;
    }
}
