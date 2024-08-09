package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.mask.MemberAccess;
import org.objectweb.asm.Opcodes;

public final class IndirectTagFrameStore {
    private static volatile boolean INITIALIZED = false;
    private static final Object UNINITIALIZED_THIS = new Object();

    private IndirectTagFrameStore() {
        throw new AssertionError(getClass() + " is a static utility class");
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_$$LOCAL_frame", opcode = Opcodes.GETFIELD)
    private static AugmentedFrame getFrameInfo(Thread thread) {
        // Placeholder
        return null;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_$$LOCAL_frame", opcode = Opcodes.PUTFIELD)
    private static void setFrameInfo(Thread thread, AugmentedFrame value) {
        // Placeholder
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_GET_AND_CLEAR)
    public static AugmentedFrame getAndClear() {
        if (INITIALIZED) {
            AugmentedFrame aFrame = getFrameInfo(Thread.currentThread());
            setFrameInfo(Thread.currentThread(), null);
            return aFrame;
        }
        return null;
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_GET_ADJUSTER)
    public static FrameAdjuster getAdjuster(AugmentedFrame frame) {
        if (frame != null) {
            return new MatchingFrameAdjuster(frame.getFrame(), frame.getArguments());
        }
        return new EmptyFrameAdjuster();
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_CLEAR)
    public static void clear() {
        if (INITIALIZED) {
            setFrameInfo(Thread.currentThread(), null);
        }
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_SET_FRAME)
    public static void set(AugmentedFrame frame) {
        if (INITIALIZED) {
            setFrameInfo(Thread.currentThread(), frame);
        }
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_SET)
    public static void set(TagFrame frame, Object[] arguments) {
        if (INITIALIZED) {
            setFrameInfo(Thread.currentThread(), new AugmentedFrame(frame, arguments));
        }
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_GET_UNINITIALIZED_THIS)
    public static Object getUninitializedThisMarker() {
        return UNINITIALIZED_THIS;
    }

    public static synchronized void initialize() {
        // Ensure that the necessary classes are initialized to prevent circular class initialization
        // noinspection unused
        Object[] dependencies = new Object[] {AugmentedFrame.class, Thread.currentThread()};
        INITIALIZED = true;
    }
}
