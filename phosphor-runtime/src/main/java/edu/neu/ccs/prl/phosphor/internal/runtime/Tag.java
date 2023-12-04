package edu.neu.ccs.prl.phosphor.internal.runtime;

public final class Tag {
    @InvokedViaHandle(handle = Handle.TAG_GET_EMPTY)
    public static Tag getEmptyTag() {
        return null;
    }
}
