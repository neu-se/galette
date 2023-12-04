package edu.neu.ccs.prl.phosphor.internal.runtime;

public class PhosphorFrame {
    @InvokedViaHandle(handle = Handle.FRAME_GET_INSTANCE)
    public static PhosphorFrame getInstance() {
        return new PhosphorFrame();
    }
}
