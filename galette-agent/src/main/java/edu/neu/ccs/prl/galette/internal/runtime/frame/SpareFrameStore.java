package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.mask.MemberAccess;
import org.objectweb.asm.Opcodes;

public final class SpareFrameStore {
    private static volatile boolean INITIALIZED = false;

    private SpareFrameStore() {
        throw new AssertionError(getClass() + " is a static utility class");
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_$$LOCAL_spare_frame", opcode = Opcodes.GETFIELD)
    private static TagFrame getSpare(Thread thread) {
        // Placeholder
        return null;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_$$LOCAL_spare_frame", opcode = Opcodes.PUTFIELD)
    private static void setSpare(Thread thread, TagFrame frame) {
        // Placeholder
    }

    public static TagFrame getAndClear() {
        if (INITIALIZED) {
            Thread current = Thread.currentThread();
            TagFrame spare = getSpare(current);
            if (spare != null) {
                setSpare(current, null);
                return spare.acquire(0);
            }
        }
        return new TagFrame();
    }

    @InvokedViaHandle(handle = Handle.SPARE_FRAME_SET)
    public static void set(TagFrame frame, Tag[] tags) {
        if (INITIALIZED && tags == null) {
            // Set as spare if this frame was not from a matched signature polymorphic call
            setSpare(Thread.currentThread(), frame);
        }
    }

    public static synchronized void initialize() {
        // Ensure that needed classes are initialized to prevent circular class initialization
        // noinspection unused
        Object[] dependencies = new Object[] {Thread.currentThread()};
        INITIALIZED = true;
    }
}
