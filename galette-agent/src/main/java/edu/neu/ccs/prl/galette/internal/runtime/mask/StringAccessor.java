package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.*;
import org.objectweb.asm.Opcodes;

public final class StringAccessor {
    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/StringBuilder", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static StringBuffer newStringBuilder(TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Appendable", name = "append", opcode = Opcodes.INVOKEINTERFACE, isInterface = true)
    public static Appendable append(Appendable appendable, CharSequence csq, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/String", name = "<init>", opcode = Opcodes.INVOKESPECIAL)
    public static String newString(char[] value, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/String", name = "toCharArray", opcode = Opcodes.INVOKEVIRTUAL)
    public static char[] toCharArray(String s, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
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
            char[] values = toCharArray(s, new TagFrame(null));
            ArrayWrapper wrapper = ArrayTagStore.getWrapper(values);
            if (wrapper != null) {
                return wrapper.getElements();
            }
        }
        return null;
    }

    public static String setCharTags(char[] values, Tag tag) {
        if (values != null && !Tag.isEmpty(tag)) {
            ArrayWrapper wrapper = new ArrayWrapper(values);
            for (int i = 0; i < values.length; i++) {
                wrapper.setElement(tag, i);
            }
            ArrayTagStore.setWrapper(values, wrapper);
        }
        return newString(values, new TagFrame(null));
    }

    public static String setCharTags(String s, Tag tag) {
        if (s != null && !Tag.isEmpty(tag)) {
            return setCharTags(toCharArray(s, new TagFrame(null)), tag);
        }
        return s;
    }
}
