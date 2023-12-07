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
    public static void $$PHOSPHOR_premain(String agentArgs, Instrumentation inst, PhosphorFrame frame)
            throws IOException {
        premain(agentArgs, inst);
    }

    public static void premain(String agentArgs, Instrumentation inst) throws IOException {
        PhosphorLog.initialize(System.err);
        String cachePath = System.getProperty("phosphor.cache");
        TransformationCache cache = cachePath == null ? null : new TransformationCache(new File(cachePath));
        inst.addTransformer(new TransformerWrapper(cache));
    }

    private static final class TransformerWrapper implements ClassFileTransformer {
        private final TransformationCache cache;
        private final PhosphorTransformer transformer = new PhosphorTransformer();

        private TransformerWrapper(TransformationCache cache) {
            this.cache = cache;
        }

        @SuppressWarnings("unused")
        public byte[] $$PHOSPHOR_transform(
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
            try {
                // Only cache dynamically instrumented files that are not VM anonymous
                if (className != null && cache != null && cache.hasEntry(className, classFileBuffer)) {
                    return cache.loadEntry(className);
                }
                byte[] result = transformer.transform(classFileBuffer, false);
                if (className != null && cache != null && result != null) {
                    cache.storeEntry(className, classFileBuffer, result);
                }
                return result;
            } catch (IOException e) {
                throw new RuntimeException("Failed to load or store cache entry", e);
            } catch (Throwable t) {
                // Log the error to prevent it from being silently swallowed by the JVM
                PhosphorLog.error("Failed to instrument class: " + className, t);
                throw t;
            }
        }
    }
}
