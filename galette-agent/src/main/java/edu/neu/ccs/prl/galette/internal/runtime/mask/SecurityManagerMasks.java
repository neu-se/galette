package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public final class SecurityManagerMasks {
    @Mask(owner = "java/lang/SecurityManager", name = "getClassContext", type = MaskType.POST_PROCESS)
    public static Class<?>[] getClassContext(Class<?>[] returnValue, Object receiver, TagFrame frame) {
        // Remove the first element which will for the shadow wrapper around the native getClassContext method
        Class<?>[] result = new Class[returnValue.length - 1];
        System.arraycopy(returnValue, 1, result, 0, result.length);
        return result;
    }
}
