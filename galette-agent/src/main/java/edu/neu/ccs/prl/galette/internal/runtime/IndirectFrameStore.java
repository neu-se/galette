package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.mask.MemberAccess;
import org.objectweb.asm.Opcodes;

public final class IndirectFrameStore {
    private static volatile boolean INITIALIZED = false;

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_frame", opcode = Opcodes.GETFIELD)
    private static TagFrame getFrame(Thread thread) {
        throw new AssertionError("Placeholder method was called");
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_frame", opcode = Opcodes.PUTFIELD)
    private static void setFrame(Thread thread, TagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_GET_AND_CLEAR)
    public static TagFrame getAndClear(String descriptor) {
        if (INITIALIZED) {
            TagFrame frame = getFrame(Thread.currentThread());
            if (frame != null) {
                setFrame(Thread.currentThread(), null);
                String callerDescriptor = frame.getCallerDescriptor();
                // TODO attempt to match using descriptor
                // if matches fails need way to ensure original is set back not the empty frame
            }
        }
        return new TagFrame();
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_CLEAR)
    public static void clear() {
        if (INITIALIZED) {
            setFrame(Thread.currentThread(), null);
        }
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_SET)
    public static void set(TagFrame frame) {
        if (INITIALIZED) {
            setFrame(Thread.currentThread(), frame);
        }
    }

    public static void initialize() {
        INITIALIZED = true;
    }
}
