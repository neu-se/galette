package edu.neu.ccs.prl.phosphor.internal.transform;

import edu.neu.ccs.prl.phosphor.internal.runtime.Tainter;
import org.objectweb.asm.Type;

public final class Configuration {
    private Configuration() {
        throw new AssertionError();
    }

    private static final String TAINTER_INTERNAL_NAME = Type.getInternalName(Tainter.class);

    public static boolean isInternalTaintingClass(String className) {
        return TAINTER_INTERNAL_NAME.equals(className);
    }
}
