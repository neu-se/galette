package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.collection.Pair;
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
    private static Pair<TagFrame, Object[]> getFrame(Thread thread) {
        // Placeholder
        return null;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_$$LOCAL_frame", opcode = Opcodes.PUTFIELD)
    private static void setFrame(Thread thread, Pair<TagFrame, Object[]> value) {
        // Placeholder
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_GET_ADJUSTER)
    public static FrameAdjuster getAdjuster() {
        if (INITIALIZED) {
            Pair<TagFrame, Object[]> pair = getFrame(Thread.currentThread());
            if (pair != null) {
                // Consume the frame
                setFrame(Thread.currentThread(), null);
                return new MatchingFrameAdjuster(pair.getFirst(), pair.getSecond());
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

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_RESTORE)
    public static void restore(TagFrame frame) {
        if (INITIALIZED && frame instanceof AdjustedTagFrame) {
            AdjustedTagFrame aFrame = (AdjustedTagFrame) frame;
            setFrame(Thread.currentThread(), new Pair<>(aFrame.getOriginal(), aFrame.getArguments()));
        }
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_SET)
    public static void set(TagFrame frame, Object[] arguments) {
        if (INITIALIZED) {
            setFrame(Thread.currentThread(), new Pair<>(frame, arguments));
        }
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_GET_UNINITIALIZED_THIS)
    public static Object getUninitializedThisMarker() {
        return UNINITIALIZED_THIS;
    }

    public static synchronized void initialize() {
        // Ensure that needed classes are initialized to prevent circular class initialization
        Object[] dependencies = new Object[] {AdjustedTagFrame.class, Pair.class, Thread.currentThread()};
        INITIALIZED = true;
    }
}
