package edu.neu.ccs.prl.galette.internal.runtime.mask;

import org.objectweb.asm.Opcodes;

@SuppressWarnings("unused")
public class ArrayAccessor {
    @MemberAccess(owner = "java/lang/reflect/Array", name = "newArray", opcode = Opcodes.INVOKESTATIC)
    public static Object newArray(Class<?> componentType, int length) {
        throw new AssertionError("Placeholder method was called");
    }

    @MemberAccess(owner = "java/lang/reflect/Array", name = "multiNewArray", opcode = Opcodes.INVOKESTATIC)
    public static Object multiNewArray(Class<?> componentType, int[] dimensions) {
        throw new AssertionError("Placeholder method was called");
    }
}
