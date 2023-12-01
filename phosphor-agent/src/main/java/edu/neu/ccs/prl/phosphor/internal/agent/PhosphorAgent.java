package edu.neu.ccs.prl.phosphor.internal.agent;

import java.lang.instrument.Instrumentation;
import org.objectweb.asm.Opcodes;

public final class PhosphorAgent {
    /**
     * Prefix of packages that should not be instrumented.
     */
    public static final String INTERNAL_PACKAGE_PREFIX = "edu/neu/ccs/prl/phosphor/internal/";
    /**
     * ASM API version implemented by the class and method visitors used by transformers.
     */
    public static final int ASM_VERSION = Opcodes.ASM9;
    /**
     * Prefix of class members added via instrumentation.
     */
    public static final String ADDED_MEMBER_PREFIX = "$$PHOSPHOR_";
    /**
     * Prefix of packages that need to be packed into the base module or added to the boot class path.
     */
    public static final String RUNTIME_PACKAGE_PREFIX = "edu/neu/ccs/prl/phosphor/internal/runtime/";

    private PhosphorAgent() {
        throw new AssertionError();
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        PhosphorLog.initialize(System.err);
        inst.addTransformer(new PhosphorTransformer());
    }
}
