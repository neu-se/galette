package edu.neu.ccs.prl.phosphor.internal.runtime;

public final class Tag {
    @InvokedViaHandle(handle = Handle.TAG_GET_EMPTY)
    public static Tag getEmptyTag() {
        return null;
    }

    @InvokedViaHandle(handle = Handle.TAG_UNION)
    public static Tag union(Tag t1, Tag t2) {
        return null;
    }
}
