package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.mask.MemberAccess;
import org.objectweb.asm.Opcodes;

public final class ExceptionStore {
    private static volatile boolean INITIALIZED = false;

    private ExceptionStore() {
        throw new AssertionError(getClass() + " is a static utility class");
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_$$LOCAL_exceptionInfo", opcode = Opcodes.GETFIELD)
    private static Object getExceptionInfo(Thread thread) {
        // Placeholder
        return null;
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Thread", name = "$$GALETTE_$$LOCAL_exceptionInfo", opcode = Opcodes.PUTFIELD)
    private static void setExceptionInfo(Thread thread, Object info) {
        // Placeholder
    }

    @InvokedViaHandle(handle = Handle.EXCEPTION_STORE_SET)
    public static void setThrownTag(Throwable t, Tag tag) {
        if (INITIALIZED) {
            if (Tag.isEmpty(tag)) {
                setExceptionInfo(Thread.currentThread(), null);
            } else {
                setExceptionInfo(Thread.currentThread(), new ExceptionInfo(t, tag));
            }
        }
    }

    @InvokedViaHandle(handle = Handle.EXCEPTION_STORE_GET)
    public static Tag getThrownTag(Throwable t) {
        if (INITIALIZED) {
            Object info = getExceptionInfo(Thread.currentThread());
            // Clear exception info
            setExceptionInfo(Thread.currentThread(), null);
            if (info instanceof ExceptionInfo) {
                ExceptionInfo ei = (ExceptionInfo) info;
                if (ei.throwable == t) {
                    return ei.tag;
                }
            }
        }
        return Tag.emptyTag();
    }

    public static synchronized void initialize() {
        // Ensure that needed classes are initialized to prevent circular class initialization
        Object[] dependencies = new Object[] {Thread.currentThread()};
        INITIALIZED = true;
    }

    private static final class ExceptionInfo {
        private final Throwable throwable;
        private final Tag tag;

        private ExceptionInfo(Throwable throwable, Tag tag) {
            this.throwable = throwable;
            this.tag = tag;
        }
    }
}
