package edu.neu.ccs.prl.galette.bench.extension;

import edu.neu.ccs.prl.galette.bench.extension.FlowReport.FlowReportEntry;
import edu.neu.ccs.prl.meringue.JvmLauncher;
import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.junit.platform.launcher.TestIdentifier;

final class ForkedTestRunner implements Closeable {
    /**
     * Maximum amount of time to execute a single test for.
     * <p>
     * Non-negative, non-null.
     */
    private final Duration timeout;

    private final JvmLauncher launcher;
    private final ServerSocket server;
    private ForkConnection connection;
    private Process process;

    public ForkedTestRunner(JvmLauncher launcher, Duration timeout) throws IOException {
        if (launcher == null || timeout == null) {
            throw new NullPointerException();
        }
        // Create a server socket bound to an automatically allocated port
        this.server = new ServerSocket(0);
        this.launcher = launcher.appendArguments(String.valueOf(server.getLocalPort()));
        this.timeout = timeout;
    }

    private boolean isConnected() {
        return connection != null && !connection.isClosed();
    }

    public void run(FileFlowReport report, List<TestIdentifier> testIdentifiers) throws IOException {
        Set<String> uniqueIds =
                testIdentifiers.stream().map(TestIdentifier::getUniqueId).collect(Collectors.toSet());
        while (!uniqueIds.isEmpty()) {
            uniqueIds.removeAll(run(report, uniqueIds));
        }
    }

    private Set<String> run(FileFlowReport report, Set<String> uniqueIds) throws IOException {
        Set<String> executed = new HashSet<>();
        if (!isConnected()) {
            closeConnection();
            // Launch the test JVM
            this.process =
                    launcher.appendArguments(uniqueIds.toArray(new String[0])).launch();
            // Connect to the JVM
            this.connection = new ForkConnection(server.accept());
        }
        while (isConnected()) {
            String nextId = connection.receiveString();
            if (nextId.isEmpty()) {
                return executed;
            }
            executed.add(nextId);
            ForkTimer timer = new ForkTimer();
            try {
                FlowReportEntry entry = new FlowReportEntry(
                        nextId,
                        connection.receiveInt(),
                        connection.receiveInt(),
                        connection.receiveInt(),
                        connection.receiveString());
                report.record(entry);
            } catch (Throwable t) {
                closeConnection();
                String status = timer.timedOut ? "timeout" : "crash";
                report.record(new FlowReportEntry(nextId, 0, 0, 0, status));
            } finally {
                timer.cancel();
            }
        }
        return executed;
    }

    private void closeConnection() {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
        }
        if (process != null && process.isAlive()) {
            try {
                stop(process);
            } catch (InterruptedException e) {
                //
            }
            process = null;
        }
    }

    @Override
    public void close() throws IOException {
        server.close();
        closeConnection();
    }

    static void stop(Process process) throws InterruptedException {
        if (process.isAlive()) {
            process.destroy();
            if (!process.waitFor(10, TimeUnit.SECONDS)) {
                process.destroyForcibly().waitFor();
            }
        }
    }

    private final class ForkTimer extends Timer {
        private boolean timedOut = false;

        ForkTimer() {
            schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            closeConnection();
                            timedOut = true;
                        }
                    },
                    timeout.toMillis());
        }
    }
}
