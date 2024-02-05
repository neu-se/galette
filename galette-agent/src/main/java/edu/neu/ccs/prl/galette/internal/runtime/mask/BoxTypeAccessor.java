package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import org.objectweb.asm.Opcodes;

@SuppressWarnings("unused")
public final class BoxTypeAccessor {
    @MemberAccess(owner = "java/lang/Boolean", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Boolean valueOf(boolean value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/Boolean", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Boolean newBoolean(boolean value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/Byte", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Byte valueOf(byte value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/Byte", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Byte newByte(byte value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/Character", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Character valueOf(char value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/Character", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Character newCharacter(char value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/Integer", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Integer valueOf(int value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/Integer", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Integer newInteger(int value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/Long", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Long valueOf(long value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/Long", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Long newLong(long value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/Short", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Short valueOf(short value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/Short", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Short newShort(short value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }
}
