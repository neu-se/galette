package edu.neu.ccs.prl.galette.bench.extension;

import edu.neu.ccs.prl.galette.bench.extension.FlowReport.FlowReportEntry;
import edu.neu.ccs.prl.meringue.ForkConnection;
import edu.neu.ccs.prl.meringue.JvmLauncher;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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

    private void restartConnection() throws IOException {
        closeConnection();
        // Launch the analysis JVM
        this.process = launcher.launch();
        // Connection to the JVM
        this.connection = new ForkConnection(server.accept());
    }

    private boolean isConnected() {
        return connection != null && !connection.isClosed();
    }

    FlowReportEntry run(String testIdentifier) throws IOException {
        if (!isConnected()) {
            restartConnection();
        }
        ForkTimer timer = timeout == null ? null : new ForkTimer();
        try {
            connection.send(testIdentifier);
            FlowReportEntry entry = connection.receive(FlowReportEntry.class);
            if (timer != null) {
                timer.cancel();
            }
            return entry;
        } catch (Throwable t) {
            // Input caused fork to fail
            closeConnection();
            restartConnection();
            String status = "crash";
            if (timer != null && timer.timedOut) {
                status = "timeout";
            }
            return new FlowReportEntry(testIdentifier, 0, 0, 0, status);
        } finally {
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    private void closeConnection() {
        if (connection != null && !connection.isClosed()) {
            try {
                connection.send(null);
            } catch (IOException e) {
                // Failed to send shutdown signal
            }
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

    private static void stop(Process process) throws InterruptedException {
        if (process.isAlive()) {
            process.destroy();
            if (!process.waitFor(5, TimeUnit.SECONDS)) {
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
