package edu.neu.ccs.prl.phosphor.internal.agent;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public final class AsmUtil {
    private AsmUtil() {
        throw new AssertionError();
    }

    public static boolean isSet(int access, int flag) {
        return ((access & flag) != 0);
    }

    public static int makePublic(int access) {
        access &= ~Opcodes.ACC_PRIVATE;
        access &= ~Opcodes.ACC_PROTECTED;
        return access | Opcodes.ACC_PUBLIC;
    }

    public static byte[] toBytes(ClassNode cn) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }
}
