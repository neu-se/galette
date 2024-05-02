package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.InvokedViaHandle;

public final class GetCallerHelper {
    @InvokedViaHandle(handle = Handle.GET_CALLER_HELPER)
    public static Class<?> getCaller(Class<?> caller, Class<?> stored) {
        return stored == null ? caller : stored;
    }
}
