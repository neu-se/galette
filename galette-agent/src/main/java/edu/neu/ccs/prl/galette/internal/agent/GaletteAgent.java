package edu.neu.ccs.prl.galette.internal.agent;

import edu.neu.ccs.prl.galette.internal.runtime.*;
import edu.neu.ccs.prl.galette.internal.runtime.frame.IndirectTagFrameStore;
import edu.neu.ccs.prl.galette.internal.runtime.mask.ReflectionMasks;
import edu.neu.ccs.prl.galette.internal.runtime.mask.UnsafeFlagAccessor;
import edu.neu.ccs.prl.galette.internal.transform.GaletteLog;
import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
import edu.neu.ccs.prl.galette.internal.transform.TransformationCache;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public final class GaletteAgent {
    static {
        // Enable mirrored tag stores
        // Note: must initialize TagStoreFlagAccessor first to prevent circularity
        TagStoreFlagAccessor.initialize();
        ArrayTagStore.initialize();
        FieldTagStore.initialize();
        // Enable propagation through Unsafe accesses
        UnsafeFlagAccessor.initialize();
        // Enable indirect frame passing
        IndirectTagFrameStore.initialize();
        // Enable propagation through reflective method and constructor calls
        ReflectionMasks.initialize();
        // Enable propagation through exceptions
        ExceptionStore.initialize();
    }

    private GaletteAgent() {
        throw new AssertionError();
    }

    @SuppressWarnings("unused")
    public static void premain(String agentArgs, Instrumentation inst, TagFrame frame) throws IOException {
        premain(agentArgs, inst);
    }

    public static void premain(String agentArgs, Instrumentation inst) throws IOException {
        GaletteLog.initialize(System.err);
        String cachePath = System.getProperty("galette.cache");
        TransformationCache cache = cachePath == null ? null : new TransformationCache(new File(cachePath));
        GaletteTransformer.setCache(cache);
        inst.addTransformer(new TransformerWrapper());
    }

    private static final class TransformerWrapper implements ClassFileTransformer {
        private final GaletteTransformer transformer = new GaletteTransformer();

        @SuppressWarnings("unused")
        public byte[] transform(
                ClassLoader loader,
                String className,
                Class<?> classBeingRedefined,
                ProtectionDomain protectionDomain,
                byte[] classFileBuffer,
                TagFrame frame) {
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
