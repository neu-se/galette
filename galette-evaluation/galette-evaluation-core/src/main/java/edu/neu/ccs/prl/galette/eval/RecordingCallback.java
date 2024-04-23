package edu.neu.ccs.prl.galette.eval;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.*;
import org.dacapo.harness.Callback;
import org.dacapo.harness.CommandLineArgs;

@SuppressWarnings("unused")
public class RecordingCallback extends Callback {
    private final File report;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ResidentSetSizeSampler sampler;
    ScheduledFuture<?> future;

    public RecordingCallback(CommandLineArgs commandLineArgs) throws FileNotFoundException {
        super(commandLineArgs);
        String path = System.getProperty("galette.dacapo.report");
        if (path == null) {
            throw new IllegalStateException("Missing Java property galette.dacapo.report");
        }
        report = new File(path);
        try (PrintWriter out = new PrintWriter(new FileOutputStream(report, false))) {
            out.println("iteration,rss,elapsed_time");
        }
    }

    @Override
    public synchronized void start(String benchmark) {
        super.start(benchmark);
        future = scheduler.scheduleAtFixedRate(sampler = new ResidentSetSizeSampler(), 0, 1, TimeUnit.MILLISECONDS);
    }

    @Override
    public synchronized void stop(long duration) {
        future.cancel(true);
        try {
            future.get();
        } catch (CancellationException e) {
            // Expected
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        super.stop(duration);
        record(sampler.maxSize, duration);
        future = null;
        sampler = null;
    }

    @Override
    public void complete(String benchmark, boolean valid) {
        super.complete(benchmark, valid);
        if (!valid) {
            // Terminate early if validation fails
            System.err.println("Exiting: DaCapo validation failed.");
            System.exit(1);
        }
    }

    public void record(long residentSetSize, long elapsedTime) {
        try (PrintWriter out = new PrintWriter(new FileOutputStream(report, true))) {
            out.printf("%d,%d,%d%n", iterations, residentSetSize, elapsedTime);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static class ResidentSetSizeSampler implements Runnable {
        private long maxSize = 0;

        @Override
        public void run() {
            maxSize = Math.max(maxSize, getResidentSetSize());
        }

        private static long getResidentSetSize() {
            try (Scanner scanner = new Scanner(Files.newInputStream(Paths.get("/proc/self/smaps_rollup")))) {
                String _line;
                scanner.nextLine();
                String line = scanner.nextLine();
                String[] parts = line.split("\\s+");
                if (parts.length != 3) {
                    throw new IOException("Expected three parts");
                } else if (!"Rss:".equals(parts[0])) {
                    throw new IOException("Expected RSS entry");
                } else if (!"kB".equals(parts[2])) {
                    throw new IOException("Expected unit kB");
                }
                return Long.parseLong(parts[1]);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
