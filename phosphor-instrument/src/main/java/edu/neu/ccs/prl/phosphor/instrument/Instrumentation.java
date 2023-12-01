package edu.neu.ccs.prl.phosphor.instrument;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Implementors are expected to have a zero-argument, public constructor.
 */
public interface Instrumentation {
    /**
     * Configures this instance using the specified options.
     *
     * @param options the key-value pairs that should be used to configure this instance
     * @throws IOException if an I/O error occurs
     */
    void configure(Properties options) throws IOException, ReflectiveOperationException;

    /**
     * Returns key-value pairs that produce the current configuration of this instance.
     * Must be non-null.
     *
     * @return  key-value pairs that produce the current configuration of this instance.
     */
    Properties getOptions();

    /**
     * Returns the class path elements needed to use this class.
     * The returned set should be non-null.
     * All elements of the return set should be non-null.
     *
     * @return class path elements needed to use this class
     */
    Set<File> getClassPathElements();

    byte[] apply(byte[] classFileBuffer);

    boolean shouldPack(String classFileName);

    Set<File> getElementsToPack();

    BiFunction<String, byte[], byte[]> createPatcher(Function<String, byte[]> entryLocator);

    Set<String> getRequiredModules();

    static Instrumentation create(String className, Properties options)
            throws ReflectiveOperationException, IOException {
        Class<?> clazz = Class.forName(className, true, Instrumentation.class.getClassLoader());
        Instrumentation instance = (Instrumentation) clazz.getConstructor().newInstance();
        instance.configure(options);
        return instance;
    }
}
