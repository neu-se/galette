package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.mask.MemberAccess;
import org.objectweb.asm.Opcodes;

/**
 * Before accessing mirrored tags, tag stores must check whether the access was triggered from
 * within a tag store's handling of a different access.
 * This "inner" access should be suppressed to prevent the risk of triggering an infinite loop.
 * This suppression is done using {@link TagStoreFlagAccessor#reserve()}.
 */
public final class TagStoreFlagAccessor {
    private static volatile boolean INITIALIZED = false;

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_$$LOCAL_tagStoreFlag", opcode = Opcodes.GETFIELD)
    private static boolean getFlag(Thread thread) {
        // Placeholder
        return false;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_$$LOCAL_tagStoreFlag", opcode = Opcodes.PUTFIELD)
    private static void setFlag(Thread thread, boolean value) {
        // Placeholder
    }

    public static void free() {
        if (INITIALIZED) {
            setFlag(Thread.currentThread(), false);
        }
    }

    public static boolean reserve() {
        if (INITIALIZED && !getFlag(Thread.currentThread())) {
            // Prevent re-entry on the same Thread
            setFlag(Thread.currentThread(), true);
            return true;
        }
        return false;
    }

    public static synchronized void initialize() {
        // Ensure that needed classes are initialized to prevent circular class initialization
        Object[] dependencies = new Object[] {Thread.currentThread()};
        INITIALIZED = true;
    }
}
