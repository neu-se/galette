package edu.neu.ccs.prl.galette.bench.extension;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectUniqueId;

import edu.neu.ccs.prl.galette.bench.extension.FlowReport.FlowReportEntry;
import edu.neu.ccs.prl.meringue.ForkConnection;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

public final class ForkTestMain {
    private static final Launcher launcher = LauncherFactory.create();

    public static void main(String[] args) throws Throwable {
        int port = Integer.parseInt(args[0]);
        try (ForkConnection connection = new ForkConnection(port)) {
            while (!connection.isClosed()) {
                String uniqueId = connection.receive(String.class);
                FlowReportEntry entry = run(uniqueId);
                connection.send(entry);
            }
        }
    }

    private static FlowReportEntry run(String testIdentifier) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectUniqueId(testIdentifier))
                .filters(includeClassNamePatterns(".*ITCase"))
                .build();
        launcher.registerTestExecutionListeners(new SimpleListener(new PrintWriter(System.out)));
        ListFlowReport report = new ListFlowReport();
        FlowCheckerResolver.setReport(report);
        launcher.execute(launcher.discover(request));
        if (report.getEntries().size() != 1) {
            throw new IllegalStateException("Expected exactly one entry");
        }
        return report.getEntries().get(0);
    }

    static class SimpleListener implements TestExecutionListener {
        private final PrintWriter out;
        private TestPlan plan;

        SimpleListener(PrintWriter out) {
            this.out = out;
        }

        public void testPlanExecutionStarted(TestPlan testPlan) {
            this.plan = testPlan;
        }

        public void testPlanExecutionFinished(TestPlan testPlan) {
            this.plan = null;
        }

        public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            if (testIdentifier.isTest()) {
                TestExecutionResult.Status status = testExecutionResult.getStatus();
                out.println(String.format("%s :: %s", formatTestIdentifier(testIdentifier), status));
                out.flush();
            }
        }

        private String formatTestIdentifier(TestIdentifier testIdentifier) {
            return String.join(" > ", this.collectDisplayNames(testIdentifier.getUniqueIdObject()));
        }

        private List<String> collectDisplayNames(UniqueId uniqueId) {
            LinkedList<String> displayNames = new LinkedList<>();
            int size = uniqueId.getSegments().size();
            for (int i = 0; i < size; i++) {
                displayNames.addFirst(plan.getTestIdentifier(uniqueId).getDisplayName());
                if (i < size - 1) {
                    uniqueId = uniqueId.removeLastSegment();
                }
            }
            return displayNames;
        }
    }
}
