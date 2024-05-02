package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import org.objectweb.asm.Opcodes;

@SuppressWarnings({
    "unused",
    "UnusedReturnValue",
    "UnnecessaryBoxing",
    "BooleanConstructorCall",
    "CachedNumberConstructorCall",
    "UnnecessaryUnboxing"
})
public final class BoxTypeAccessor {
    @MemberAccess(owner = "java/lang/Boolean", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Boolean valueOf(boolean value, TagFrame frame) {
        // Placeholder
        return Boolean.valueOf(value);
    }

    @MemberAccess(owner = "java/lang/Boolean", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Boolean newBoolean(boolean value, TagFrame frame) {
        // Placeholder
        return new Boolean(value);
    }

    @MemberAccess(owner = "java/lang/Byte", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Byte valueOf(byte value, TagFrame frame) {
        // Placeholder
        return Byte.valueOf(value);
    }

    @MemberAccess(owner = "java/lang/Byte", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Byte newByte(byte value, TagFrame frame) {
        // Placeholder
        return new Byte(value);
    }

    @MemberAccess(owner = "java/lang/Character", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Character valueOf(char value, TagFrame frame) {
        // Placeholder
        return Character.valueOf(value);
    }

    @MemberAccess(owner = "java/lang/Character", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Character newCharacter(char value, TagFrame frame) {
        // Placeholder
        return new Character(value);
    }

    @MemberAccess(owner = "java/lang/Integer", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Integer valueOf(int value, TagFrame frame) {
        // Placeholder
        return Integer.valueOf(value);
    }

    @MemberAccess(owner = "java/lang/Integer", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Integer newInteger(int value, TagFrame frame) {
        // Placeholder
        return new Integer(value);
    }

    @MemberAccess(owner = "java/lang/Long", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Long valueOf(long value, TagFrame frame) {
        // Placeholder
        return Long.valueOf(value);
    }

    @MemberAccess(owner = "java/lang/Long", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Long newLong(long value, TagFrame frame) {
        // Placeholder
        return new Long(value);
    }

    @MemberAccess(owner = "java/lang/Short", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Short valueOf(short value, TagFrame frame) {
        // Placeholder
        return Short.valueOf(value);
    }

    @MemberAccess(owner = "java/lang/Short", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Short newShort(short value, TagFrame frame) {
        // Placeholder
        return new Short(value);
    }

    @MemberAccess(owner = "java/lang/Float", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Float valueOf(float value, TagFrame frame) {
        // Placeholder
        return Float.valueOf(value);
    }

    @MemberAccess(owner = "java/lang/Float", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Float newFloat(float value, TagFrame frame) {
        // Placeholder
        return new Float(value);
    }

    @MemberAccess(owner = "java/lang/Double", name = "valueOf", opcode = Opcodes.INVOKESTATIC)
    public static Double valueOf(double value, TagFrame frame) {
        // Placeholder
        return Double.valueOf(value);
    }

    @MemberAccess(owner = "java/lang/Double", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static Double newDouble(double value, TagFrame frame) {
        // Placeholder
        return new Double(value);
    }

    @MemberAccess(owner = "java/lang/Boolean", name = "parseBoolean", opcode = Opcodes.INVOKESTATIC)
    public static boolean parseBoolean(String value, TagFrame frame) {
        // Placeholder
        return Boolean.parseBoolean(value);
    }

    @MemberAccess(owner = "java/lang/Boolean", name = "booleanValue", opcode = Opcodes.INVOKEVIRTUAL)
    static boolean booleanValue(Boolean receiver, TagFrame frame) {
        // Placeholder
        return receiver.booleanValue();
    }

    @MemberAccess(owner = "java/lang/Integer", name = "toString", opcode = Opcodes.INVOKESTATIC)
    public static String toString(int value, TagFrame frame) {
        // Placeholder
        return Integer.toString(value);
    }

    @MemberAccess(owner = "java/lang/Long", name = "toString", opcode = Opcodes.INVOKESTATIC)
    public static String toString(long value, TagFrame frame) {
        // Placeholder
        return Long.toString(value);
    }
}
