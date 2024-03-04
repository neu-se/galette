package edu.neu.ccs.prl.galette.eval;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import edu.neu.ccs.prl.galette.bench.ArrayAccessITCase;
import edu.neu.ccs.prl.galette.bench.ArrayLengthITCase;
import edu.neu.ccs.prl.galette.bench.AssignmentITCase;
import java.io.PrintWriter;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

public class BenchmarkRunner {
    public static void main(String[] args) {
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        try (LauncherSession session = LauncherFactory.openSession()) {
            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectClass(ArrayAccessITCase.class))
                    .selectors(selectClass(ArrayLengthITCase.class))
                    .selectors(selectClass(AssignmentITCase.class))
                    .build();
            Launcher launcher = session.getLauncher();
            launcher.registerTestExecutionListeners(listener);
            TestPlan testPlan = launcher.discover(request);
            launcher.execute(testPlan);
        }
        TestExecutionSummary summary = listener.getSummary();
        summary.printTo(new PrintWriter(System.out));
    }
}
