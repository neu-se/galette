package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import org.objectweb.asm.Opcodes;

@SuppressWarnings("unused")
public class EnumAccessor {
    @MemberAccess(owner = "java/lang/Enum", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }
}
