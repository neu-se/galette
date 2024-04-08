package edu.neu.ccs.prl.galette.bench.extension;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;

import edu.neu.ccs.prl.meringue.ForkConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

public final class ForkTestMain {
    private static final Launcher launcher = LauncherFactory.create();

    public static void main(String[] args) throws Throwable {
        int port = Integer.parseInt(args[0]);
        if (port == -1) {
            return;
        }
        try (ForkConnection connection = new ForkConnection(port)) {
            SimpleListener listener = new SimpleListener(new PrintWriter(System.out), connection);
            launcher.registerTestExecutionListeners(listener);
            String[] uniqueIds = connection.receive(String[].class);
            DiscoverySelector[] selectors = Arrays.stream(uniqueIds)
                    .map(DiscoverySelectors::selectUniqueId)
                    .toArray(DiscoverySelector[]::new);
            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectors)
                    .filters(includeClassNamePatterns(".*ITCase"))
                    .build();
            FlowCheckerResolver.setReport(connection::send);
            TestPlan plan = launcher.discover(request);
            launcher.execute(plan);
            // Notify the runner that we have finished
            connection.send(null);
        }
    }

    static class SimpleListener implements TestExecutionListener {
        private final PrintWriter out;
        private final ForkConnection connection;

        SimpleListener(PrintWriter out, ForkConnection connection) {
            this.out = out;
            this.connection = connection;
        }

        @Override
        public void executionStarted(TestIdentifier testIdentifier) {
            if (testIdentifier.isTest()) {
                try {
                    connection.send(testIdentifier.getUniqueId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            if (testIdentifier.isTest()) {
                TestExecutionResult.Status status = testExecutionResult.getStatus();
                out.println(String.format("%s :: %s", testIdentifier.getUniqueId(), status));
                out.flush();
            }
        }
    }
}
