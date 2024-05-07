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
    private static Pair<TagFrame, Object[]> getFrameInfo(Thread thread) {
        // Placeholder
        return null;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_$$LOCAL_frame", opcode = Opcodes.PUTFIELD)
    private static void setFrameInfo(Thread thread, Pair<TagFrame, Object[]> value) {
        // Placeholder
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_GET_AND_CLEAR)
    public static Pair<TagFrame, Object[]> getAndClear() {
        if (INITIALIZED) {
            Pair<TagFrame, Object[]> pair = getFrameInfo(Thread.currentThread());
            setFrameInfo(Thread.currentThread(), null);
            return pair;
        }
        return null;
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_GET_ADJUSTER)
    public static FrameAdjuster getAdjuster(Pair<TagFrame, Object[]> pair) {
        if (pair != null) {
            return new MatchingFrameAdjuster(pair.getFirst(), pair.getSecond());
        }
        return new EmptyFrameAdjuster();
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_CLEAR)
    public static void clear() {
        if (INITIALIZED) {
            setFrameInfo(Thread.currentThread(), null);
        }
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_SET_PAIR)
    public static void set(Pair<TagFrame, Object[]> pair) {
        if (INITIALIZED) {
            setFrameInfo(Thread.currentThread(), pair);
        }
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_SET)
    public static void set(TagFrame frame, Object[] arguments) {
        if (INITIALIZED) {
            setFrameInfo(Thread.currentThread(), new Pair<>(frame, arguments));
        }
    }

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_GET_UNINITIALIZED_THIS)
    public static Object getUninitializedThisMarker() {
        return UNINITIALIZED_THIS;
    }

    public static synchronized void initialize() {
        // Ensure that needed classes are initialized to prevent circular class initialization
        // noinspection unused
        Object[] dependencies = new Object[] {Pair.class, Thread.currentThread()};
        INITIALIZED = true;
    }
}
