package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrameFactory;
import org.objectweb.asm.Opcodes;

public class EnumMasks {
    @Mask(owner = "java/lang/Enum", name = "valueOf", isStatic = true)
    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name, TagFrame frame) {
        Tag enumTypeTag = frame.get(0);
        Tag nameTag = frame.get(1);
        TagFrame calleeFrame = TagFrameFactory.acquire(frame, enumTypeTag, nameTag);
        T result = valueOfInternal(enumType, name, calleeFrame);
        Tag merged = Tag.union(calleeFrame.getReturnTag(), StringAccessor.getMergedTag(name, nameTag));
        frame.setReturnTag(merged);
        return result;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Enum", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    private static <T extends Enum<T>> T valueOfInternal(Class<T> enumType, String name, TagFrame frame) {
        // Placeholder
        return Enum.valueOf(enumType, name);
    }
}
