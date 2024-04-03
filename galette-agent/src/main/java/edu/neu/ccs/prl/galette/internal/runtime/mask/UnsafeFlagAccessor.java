package edu.neu.ccs.prl.galette.internal.runtime.mask;

import org.objectweb.asm.Opcodes;

/**
 * Before propagating tags through unsafe accesses, {@link UnsafeMasks} must check whether the access was triggered from
 * within the handling of a different access.
 * Propagating for the "inner" access should be suppressed.
 */
@SuppressWarnings("ConstantValue")
public final class UnsafeFlagAccessor {
    private static volatile boolean INITIALIZED = false;

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_$$LOCAL_unsafeFlag", opcode = Opcodes.GETFIELD)
    private static boolean getFlag(Thread thread) {
        throw new AssertionError("Placeholder method was called");
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_$$LOCAL_unsafeFlag", opcode = Opcodes.PUTFIELD)
    private static void setFlag(Thread thread, boolean value) {
        throw new AssertionError("Placeholder method was called");
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

    public static void initialize() {
        INITIALIZED = true;
    }
}
