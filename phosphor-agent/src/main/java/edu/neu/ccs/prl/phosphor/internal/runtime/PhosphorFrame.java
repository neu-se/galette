package edu.neu.ccs.prl.phosphor.internal.runtime;

public class PhosphorFrame {
    private Class<?> caller;

    @InvokedViaHandle(handle = Handle.FRAME_GET_CALLER)
    public Class<?> getCaller(Class<?> ret) {
        return caller == null ? ret : caller;
    }

    @InvokedViaHandle(handle = Handle.FRAME_SET_CALLER)
    public PhosphorFrame setCaller(Class<?> caller) {
        this.caller = caller;
        return this;
    }

    @InvokedViaHandle(handle = Handle.FRAME_CREATE_FOR_CALL)
    public static PhosphorFrame createForCall(PhosphorFrame callerFrame) {
        // TODO
        return new PhosphorFrame();
    }

    @InvokedViaHandle(handle = Handle.FRAME_GET_INSTANCE)
    public static PhosphorFrame getInstance() {
        return new PhosphorFrame();
    }
}
