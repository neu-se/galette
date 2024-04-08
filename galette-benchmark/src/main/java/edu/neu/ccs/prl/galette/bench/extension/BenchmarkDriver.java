package edu.neu.ccs.prl.galette.bench.extension;

import edu.neu.ccs.prl.galette.instrument.InstrumentUtil;
import edu.neu.ccs.prl.meringue.JvmLauncher;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import org.junit.platform.launcher.TestIdentifier;

public final class BenchmarkDriver {
    public static void main(String[] args) throws IOException {
        File testJavaHome = new File(args[0]);
        FileFlowReport report = new FileFlowReport(new File(args[1]));
        JvmLauncher testForkLauncher = JvmLauncher.fromMain(
                InstrumentUtil.javaHomeToJavaExec(testJavaHome),
                ForkTestMain.class.getName(),
                getForkJavaOptions(testJavaHome, args),
                true,
                new String[0],
                null,
                null);
        List<TestIdentifier> testIdentifiers = TestCollector.collectTests();
        testIdentifiers.sort(Comparator.comparing(TestIdentifier::getUniqueId));
        try (ForkedTestRunner runner = new ForkedTestRunner(testForkLauncher, Duration.ofSeconds(5))) {
            for (TestIdentifier testIdentifier : testIdentifiers) {
                FlowReport.FlowReportEntry entry = runner.run(testIdentifier.getUniqueId());
                report.record(entry);
            }
        }
    }

    private static String[] getForkJavaOptions(File javaHome, String[] args) {
        List<String> options = new ArrayList<>(Arrays.asList(args).subList(2, args.length));
        if (InstrumentUtil.isModularJvm(javaHome)) {
            options.add("--add-exports");
            options.add("java.base/jdk.internal.misc=ALL-UNNAMED");
        }
        return options.toArray(new String[0]);
    }
}
