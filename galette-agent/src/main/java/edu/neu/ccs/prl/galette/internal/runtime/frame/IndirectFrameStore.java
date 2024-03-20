package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.mask.MemberAccess;
import org.objectweb.asm.Opcodes;

public final class IndirectFrameStore extends TagFrame {
    private static volatile boolean INITIALIZED = false;

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_frame", opcode = Opcodes.GETFIELD)
    private static IndirectTagFrame getFrame(Thread thread) {
        throw new AssertionError("Placeholder method was called");
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_frame", opcode = Opcodes.PUTFIELD)
    private static void setFrame(Thread thread, IndirectTagFrame frame) {
        throw new AssertionError("Placeholder method was called");
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_GET_ADJUSTER)
    public static FrameAdjuster getAdjuster() {
        if (INITIALIZED) {
            IndirectTagFrame frame = getFrame(Thread.currentThread());
            if (frame != null) {
                // Consume the frame
                setFrame(Thread.currentThread(), null);
                return new MatchingFrameAdjuster(frame);
            }
        }
        return new EmptyFrameAdjuster();
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
            if (frame instanceof AdjustedTagFrame) {
                // If this frame is an adjusted frame, use the original frame
                setFrame(Thread.currentThread(), ((AdjustedTagFrame) frame).getOriginal());
            } else if (frame instanceof IndirectTagFrame) {
                setFrame(Thread.currentThread(), (IndirectTagFrame) frame);
            }
        }
    }

    public static void initialize() {
        INITIALIZED = true;
    }
}
