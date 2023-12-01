package edu.neu.ccs.prl.phosphor.instrument;

import edu.neu.ccs.prl.phosphor.internal.agent.PhosphorAgent;
import edu.neu.ccs.prl.phosphor.internal.agent.PhosphorTransformer;
import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Instances of this class are created via reflection.
 */
@SuppressWarnings("unused")
public class PhosphorInstrumentation implements Instrumentation {
    private ClassFileTransformer transformer;
    private Set<File> classPathElements;

    @Override
    public void configure(Properties options) {
        transformer = new PhosphorTransformer();
        classPathElements = new HashSet<>();
        classPathElements.add(InstrumentUtil.getClassPathElement(PhosphorAgent.class));
    }

    @Override
    public Properties getOptions() {
        return new Properties();
    }

    @Override
    public java.util.Set<File> getClassPathElements() {
        return classPathElements;
    }

    @Override
    public byte[] apply(byte[] classFileBuffer) {
        try {
            return transformer.transform(null, null, null, null, classFileBuffer);
        } catch (IllegalClassFormatException e) {
            return classFileBuffer;
        }
    }

    @Override
    public BiFunction<String, byte[], byte[]> createPatcher(Function<String, byte[]> entryLocator) {
        return (resourceName, buffer) -> buffer;
    }

    @Override
    public Set<String> getRequiredModules() {
        return new HashSet<>(Arrays.asList("java.base", "jdk.jdwp.agent", "java.instrument", "jdk.unsupported"));
    }

    @Override
    public boolean shouldPack(String resourceName) {
        return resourceName.startsWith(PhosphorAgent.RUNTIME_PACKAGE_PREFIX);
    }

    @Override
    public Set<File> getElementsToPack() {
        return classPathElements;
    }
}
