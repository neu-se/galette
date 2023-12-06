package edu.neu.ccs.prl.phosphor.internal.runtime;

public class PhosphorFrame {
    private Class<?> callerClass;

    public Class<?> getCallerClass() {
        return callerClass;
    }

    @InvokedViaHandle(handle = Handle.FRAME_SET_CALLER_CLASS)
    public PhosphorFrame setCallerClass(Class<?> callerClass) {
        this.callerClass = callerClass;
        return this;
    }

    @InvokedViaHandle(handle = Handle.FRAME_GET_INSTANCE)
    public static PhosphorFrame getInstance() {
        return new PhosphorFrame();
    }
}
