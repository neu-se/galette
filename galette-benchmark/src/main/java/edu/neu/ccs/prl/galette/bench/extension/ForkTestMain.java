package edu.neu.ccs.prl.galette.bench.extension;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;

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
        String[] uniqueIds = new String[args.length - 1];
        System.arraycopy(args, 1, uniqueIds, 0, uniqueIds.length);
        try (ForkConnection connection = new ForkConnection(port)) {
            SimpleListener listener = new SimpleListener(new PrintWriter(System.out), connection);
            launcher.registerTestExecutionListeners(listener);
            DiscoverySelector[] selectors = Arrays.stream(uniqueIds)
                    .map(DiscoverySelectors::selectUniqueId)
                    .toArray(DiscoverySelector[]::new);
            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectors)
                    .filters(includeClassNamePatterns(".*ITCase"))
                    .build();
            FlowCheckerResolver.setReport(o -> {
                connection.send(o.getTruePositives());
                connection.send(o.getFalsePositives());
                connection.send(o.getFalseNegatives());
                connection.send(o.getStatus());
            });
            TestPlan plan = launcher.discover(request);
            launcher.execute(plan);
            // Notify the runner that we have finished
            connection.send("");
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
