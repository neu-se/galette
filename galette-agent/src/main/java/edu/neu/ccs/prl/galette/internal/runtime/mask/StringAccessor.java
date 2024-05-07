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

    public static Tag[] getCharTags(String s) {
        if (s != null) {
            char[] values = toCharArray(s, TagFrame.disabled());
            ArrayWrapper wrapper = ArrayTagStore.getWrapper(values);
            if (wrapper != null) {
                return wrapper.getElements();
            }
        }
        return null;
    }

    public static Tag getMergedTag(String value, Tag valueTag) {
        return Tag.union(Tag.union(StringAccessor.getCharTags(value)), valueTag);
    }

    public static String setCharTags(char[] values, Tag tag) {
        if (values != null && !Tag.isEmpty(tag)) {
            ArrayWrapper wrapper = new ArrayWrapper(values);
            for (int i = 0; i < values.length; i++) {
                wrapper.setElement(tag, i);
            }
            ArrayTagStore.updateWrapper(values, wrapper);
        }
        return newString(values, TagFrame.disabled());
    }

    public static String setCharTags(String s, Tag tag) {
        if (s != null && !Tag.isEmpty(tag)) {
            return setCharTags(toCharArray(s, TagFrame.disabled()), tag);
        }
        return s;
    }
}
