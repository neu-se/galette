package edu.neu.ccs.prl.galette.bench.extension;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.extension.*;
import org.opentest4j.MultipleFailuresError;

class FlowCheckerResolver implements ParameterResolver, TestWatcher, AfterTestExecutionCallback, BeforeEachCallback {
    private static final FlowReport report;

    static {
        String path = System.getProperty("flow.report");
        try {
            report = path == null ? null : new FlowReport(new File(path));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(FlowChecker.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return getFlowChecker(extensionContext);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        TagManagerResolver.initializeInstanceFields(context, FlowChecker.class, getFlowChecker(context));
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        updateReport(context, "disabled");
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        updateReport(context, "abort");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        if (getStore(context).get(MultipleFailuresError.class) == cause) {
            // If the failure was caused by a failed flow, the test would have otherwise succeeded
            updateReport(context, "success");
        } else {
            updateReport(context, "fail");
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        updateReport(context, "success");
    }

    private void updateReport(ExtensionContext context, String status) {
        try {
            if (report != null) {
                FlowChecker checker = getFlowChecker(context);
                report.recordEntry(
                        context.getRequiredTestClass(),
                        context.getRequiredTestMethod(),
                        context.getDisplayName(),
                        checker,
                        status);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FlowChecker getFlowChecker(ExtensionContext context) {
        return getStore(context).getOrComputeIfAbsent(FlowChecker.class);
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        ExtensionContext.Namespace namespace =
                ExtensionContext.Namespace.create(getClass(), context.getRequiredTestMethod());
        return context.getStore(namespace);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        List<? extends Throwable> failures = getFlowChecker(context).getFailures();
        if (!failures.isEmpty()) {
            MultipleFailuresError e = new MultipleFailuresError("Propagated labels did not match expected", failures);
            e.setStackTrace(Arrays.stream(e.getStackTrace()).limit(1).toArray(StackTraceElement[]::new));
            getStore(context).put(MultipleFailuresError.class, e);
            throw e;
        }
    }
}
