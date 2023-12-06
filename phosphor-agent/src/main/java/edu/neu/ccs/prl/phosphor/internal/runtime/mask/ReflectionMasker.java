package edu.neu.ccs.prl.phosphor.internal.runtime.mask;

import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;

@SuppressWarnings("unused")
public final class ReflectionMasker {
    @Mask(
            owner = "jdk/internal/reflect/Reflection",
            name = "getCallerClass",
            descriptor = "()Ljava/lang/Class;",
            isStatic = true,
            type = MaskType.REPAIR_RETURN)
    @Mask(
            owner = "sun/reflect/Reflection",
            name = "getCallerClass",
            descriptor = "()Ljava/lang/Class;",
            isStatic = true,
            type = MaskType.REPAIR_RETURN)
    public static Class<?> getCallerClass(Class<?> ret, PhosphorFrame frame) {
        Class<?> caller = frame.getCallerClass();
        return caller == null ? ret : caller;
    }
}
