package edu.neu.ccs.prl.phosphor.instrument;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.internal.ContentTypeDetector;

public class GenericInstrumenter extends Instrumenter {
    private final Instrumentation instrumentation;
    private final AtomicInteger count = new AtomicInteger(0);
    private final boolean verbose;

    private GenericInstrumenter(Instrumentation instrumentation, boolean verbose) {
        super(null);
        this.instrumentation = instrumentation;
        this.verbose = verbose;
    }

    @Override
    public byte[] instrument(byte[] classFileBuffer, String name) throws IOException {
        byte[] result = instrumentation.apply(classFileBuffer);
        int n;
        if ((n = count.incrementAndGet()) % 1000 == 0 && verbose) {
            System.out.println("Processed: " + n);
        }
        return result == null ? classFileBuffer : result;
    }

    private void processRegularFile(File input, File output) throws IOException {
        Files.createDirectories(output.getParentFile().toPath());
        try {
            try (InputStream in = Files.newInputStream(input.toPath());
                    OutputStream out = Files.newOutputStream(output.toPath())) {
                instrumentAll(in, out, input.getName());
            }
        } catch (IOException e) {
            Throwable cause = e.getMessage().contains("JaCoCo") ? e.getCause() : e;
            throw new IOException("Failed to instrument: " + input, cause);
        }
        if (input.canExecute() && !output.setExecutable(true)) {
            throw new IOException("Failed to set execute permission for: " + output);
        }
        if (input.canRead() && !output.setReadable(true)) {
            throw new IOException("Failed to set read permission for: " + output);
        }
        if (input.canWrite() && !output.setWritable(true)) {
            throw new IOException("Failed to set write permission for: " + output);
        }
    }

    private Callable<Void> createTask(Path source, Path destination, Path input) {
        return () -> {
            Path output = destination.resolve(source.relativize(input));
            if (Files.isDirectory(input)) {
                Files.createDirectories(output);
            } else if (Files.isRegularFile(source)) {
                processRegularFile(input.toFile(), output.toFile());
            }
            return null;
        };
    }

    public List<Callable<Void>> createTasks(Path source, Path destination) throws IOException {
        try (Stream<Path> walk = Files.walk(source)) {
            return walk.map((input) -> createTask(source, destination, input)).collect(Collectors.toList());
        }
    }

    private static int getType(File source) throws IOException {
        try (InputStream in = Files.newInputStream(source.toPath())) {
            return new ContentTypeDetector(in).getType();
        }
    }

    public static void process(File source, File destination, Instrumentation instrumentation, boolean verbose)
            throws IOException, InterruptedException {
        if (!source.isDirectory() && getType(source) == ContentTypeDetector.UNKNOWN) {
            throw new IllegalArgumentException("Unknown source file type: " + source);
        }
        GenericInstrumenter instrumenter = new GenericInstrumenter(instrumentation, verbose);
        ExecutorService executor =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            List<Future<Void>> futures = instrumenter.createTasks(source.toPath(), destination.toPath()).stream()
                    .map(executor::submit)
                    .collect(Collectors.toList());
            for (Future<Void> future : futures) {
                future.get();
            }
        } catch (ExecutionException e) {
            throw new IOException(e.getMessage(), e.getCause());
        } finally {
            executor.shutdown();
            while (!executor.isTerminated()) {
                if (executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                    break;
                }
            }
        }
    }
}
