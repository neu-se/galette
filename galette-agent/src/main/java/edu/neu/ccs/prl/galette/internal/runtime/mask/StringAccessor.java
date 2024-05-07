package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.*;
import org.objectweb.asm.Opcodes;

public final class StringAccessor {
    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/StringBuilder", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static StringBuilder newStringBuilder(TagFrame frame) {
        // Placeholder
        return new StringBuilder();
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Appendable", name = "append", opcode = Opcodes.INVOKEINTERFACE, isInterface = true)
    public static Appendable append(Appendable appendable, CharSequence csq, TagFrame frame) {
        // Placeholder
        return appendable;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/String", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static String newString(char[] value, TagFrame frame) {
        // Placeholder
        return new String(value);
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/String", name = "toCharArray", opcode = Opcodes.INVOKEVIRTUAL)
    public static char[] toCharArray(String s, TagFrame frame) {
        // Placeholder
        return s.toCharArray();
    }

    public static String toString(Object o, TagFrame frame) {
        if (o instanceof TaggedObject) {
            return ((TaggedObject) o).toString(frame);
        } else {
            return o.toString();
        }
    }

    public static Tag[] getCharTags(String s, TagFrame frame) {
        if (s != null) {
            char[] values = toCharArray(s, frame.acquire(0));
            ArrayWrapper wrapper = ArrayTagStore.getWrapper(values);
            if (wrapper != null) {
                return wrapper.getElements();
            }
        }
        return null;
    }

    public static Tag getMergedTag(String value, Tag valueTag, TagFrame frame) {
        return Tag.union(Tag.union(StringAccessor.getCharTags(value, frame)), valueTag);
    }

    public static String setCharTags(char[] values, Tag tag, TagFrame frame) {
        if (values != null && !Tag.isEmpty(tag)) {
            ArrayWrapper wrapper = new ArrayWrapper(values);
            for (int i = 0; i < values.length; i++) {
                wrapper.setElement(tag, i);
            }
            ArrayTagStore.updateWrapper(values, wrapper);
        }
        return newString(values, frame.acquire(0));
    }

    public static String setCharTags(String s, Tag tag, TagFrame frame) {
        if (s != null && !Tag.isEmpty(tag)) {
            return setCharTags(toCharArray(s, frame.acquire(0)), tag, frame);
        }
        return s;
    }
}
