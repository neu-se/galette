package edu.neu.ccs.prl.phosphor.internal.agent;

import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import edu.neu.ccs.prl.phosphor.internal.transform.PhosphorLog;
import edu.neu.ccs.prl.phosphor.internal.transform.PhosphorTransformer;
import edu.neu.ccs.prl.phosphor.internal.transform.TransformationCache;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public final class PhosphorAgent {
    private PhosphorAgent() {
        throw new AssertionError();
    }

    @SuppressWarnings("unused")
    public static void premain(String agentArgs, Instrumentation inst, PhosphorFrame frame) throws IOException {
        premain(agentArgs, inst);
    }

    public static void premain(String agentArgs, Instrumentation inst) throws IOException {
        PhosphorLog.initialize(System.err);
        String cachePath = System.getProperty("phosphor.cache");
        TransformationCache cache = cachePath == null ? null : new TransformationCache(new File(cachePath));
        PhosphorTransformer.setCache(cache);
        inst.addTransformer(new TransformerWrapper());
    }

    private static final class TransformerWrapper implements ClassFileTransformer {
        private final PhosphorTransformer transformer = new PhosphorTransformer();

        @SuppressWarnings("unused")
        public byte[] transform(
                ClassLoader loader,
                String className,
                Class<?> classBeingRedefined,
                ProtectionDomain protectionDomain,
                byte[] classFileBuffer,
                PhosphorFrame frame) {
            return transform(loader, className, classBeingRedefined, protectionDomain, classFileBuffer);
        }

        @Override
        public byte[] transform(
                ClassLoader loader,
                String className,
                Class<?> classBeingRedefined,
                ProtectionDomain protectionDomain,
                byte[] classFileBuffer) {
            if (classBeingRedefined != null) {
                // The class is being redefined or retransformed
                return null;
            }
            return transformer.transform(classFileBuffer, false);
        }
    }
}
