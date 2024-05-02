package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.*;
import edu.neu.ccs.prl.galette.internal.runtime.collection.HashMap;
import edu.neu.ccs.prl.galette.internal.transform.Configuration;
import java.io.*;
import java.lang.ref.SoftReference;
import org.objectweb.asm.Opcodes;

/**
 * Note: does not handle accesses through subtypes
 */
public final class SerializationMasks {
    @Mask(owner = "java/io/ObjectOutputStream", name = "writeObject0", type = MaskType.POST_PROCESS)
    public static void writeObject0(ObjectOutputStream out, Object obj, boolean unshared, TagFrame frame)
            throws IOException {
        if (Configuration.isPropagateThroughSerialization()) {
            if (obj != null && obj.getClass().isArray()) {
                ArrayWrapper wrapper = ArrayTagStore.getWrapper(obj);
                out.writeObject(wrapper);
            } else if (isMirroredType(obj)) {
                out.writeObject(FieldTagStore.getInstanceTags(obj));
            }
        }
    }

    @Mask(owner = "java/io/ObjectInputStream", name = "readObject0", type = MaskType.POST_PROCESS)
    public static Object readObject0(
            Object retValue, ObjectInputStream in, Class<?> type, boolean unshared, TagFrame frame) {
        if (Configuration.isPropagateThroughSerialization()) {
            if (retValue != null && retValue.getClass().isArray()) {
                ArrayWrapper wrapper =
                        (ArrayWrapper) readObject0(in, ArrayWrapper.class, unshared, TagFrame.emptyFrame());
                if (wrapper != null) {
                    ArrayTagStore.updateWrapper(retValue, wrapper);
                }
            } else if (isMirroredType(retValue)) {
                @SuppressWarnings("unchecked")
                HashMap<String, Tag> tags =
                        (HashMap<String, Tag>) readObject0(in, HashMap.class, unshared, TagFrame.emptyFrame());
                if (tags != null) {
                    FieldTagStore.setInstanceTags(retValue, tags);
                }
            }
        }
        return retValue;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/io/ObjectInputStream", name = "readObject0", opcode = Opcodes.INVOKEVIRTUAL)
    public static Object readObject0(ObjectInputStream stream, Class<?> type, boolean unshared, TagFrame frame) {
        // Placeholder
        return null;
    }

    private static boolean isMirroredType(Object o) {
        return o instanceof Integer
                || o instanceof Boolean
                || o instanceof Byte
                || o instanceof Character
                || o instanceof Double
                || o instanceof Float
                || o instanceof Long
                || o instanceof Short
                || o instanceof StackTraceElement
                || o instanceof SoftReference;
    }
}
