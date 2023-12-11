package edu.neu.ccs.prl.phosphor.internal.runtime;

public final class ArrayTainter {
    private ArrayTainter() {
        throw new AssertionError();
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAINTER_GET_LENGTH_TAG)
    public static Tag getLengthTag(Object array, Tag arrayTag) {
        // TODO
        return Tag.getEmptyTag();
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAINTER_SET_LENGTH_TAG)
    public static void setLengthTag(Object array, Tag lengthTag) {
        // TODO
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAINTER_GET_TAG)
    public static Tag getTag(Object array, int index, Tag arrayTag, Tag indexTag) {
        // TODO
        return Tag.getEmptyTag();
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAINTER_SET_TAG)
    public static void setTag(Object array, int index, Tag arrayTag, Tag indexTag) {
        // TODO
    }
}
