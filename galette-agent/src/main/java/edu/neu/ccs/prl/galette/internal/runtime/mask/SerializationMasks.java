package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.*;
import edu.neu.ccs.prl.galette.internal.transform.Configuration;
import java.io.*;
import org.objectweb.asm.Opcodes;

/**
 * TODO
 * ObjectOutputStream#writeString
 * ObjectOutputStream#writeObject0(Object obj, boolean unshared
 */
public final class SerializationMasks {
    @Mask(
            owner = "java/io/ObjectOutputStream",
            name = "writeObject0",
            descriptor = "(Ljava/lang/Object;ZLedu/neu/ccs/prl/galette/internal/runtime/TagFrame;)V",
            type = MaskType.POST_PROCESS)
    public static void writeObject0(ObjectOutputStream stream, Object obj, boolean unshared, TagFrame frame)
            throws IOException {
        if (Configuration.isPropagateThroughSerialization()) {
            if (obj != null && obj.getClass().isArray()) {
                ArrayWrapper wrapper = ArrayTagStore.getWrapper(obj);
                stream.writeObject(wrapper);
            }
        }
    }

    @Mask(
            owner = "java/io/ObjectInputStream",
            name = "readObject0",
            descriptor = "(Ljava/lang/Class;ZLedu/neu/ccs/prl/galette/internal/runtime/TagFrame;)Ljava/lang/Object;",
            type = MaskType.POST_PROCESS)
    public static Object readObject0(
            Object retValue, ObjectInputStream stream, Class<?> type, boolean unshared, TagFrame frame) {
        if (Configuration.isPropagateThroughSerialization()) {
            if (retValue != null && retValue.getClass().isArray()) {
                ArrayWrapper wrapper = (ArrayWrapper) readObject0(stream, ArrayWrapper.class, unshared, new TagFrame());
                if (wrapper != null) {
                    ArrayTagStore.setWrapper(retValue, wrapper);
                }
            }
        }
        return retValue;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/io/ObjectInputStream", name = "readObject0", opcode = Opcodes.INVOKEVIRTUAL)
    public static Object readObject0(ObjectInputStream stream, Class<?> type, boolean unshared, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }
}
