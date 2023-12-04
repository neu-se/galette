package edu.neu.ccs.prl.phosphor.instrument;

import edu.neu.ccs.prl.phosphor.internal.transform.FileUtil;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public final class JLinkInvoker {
    private JLinkInvoker() {
        throw new AssertionError();
    }

    public static void invoke(File javaHome, File outputDirectory, Instrumentation instrumentation, String modules)
            throws InterruptedException, IOException {
        String jlinkAgentJar =
                InstrumentUtil.getClassPathElement(JLinkRegistrationAgent.class).getAbsolutePath();
        List<String> command = new ArrayList<>();
        String classPath = buildClassPath(instrumentation);
        command.add(InstrumentUtil.javaHomeToJLinkExec(javaHome).getAbsolutePath());
        command.add("-J-javaagent:" + jlinkAgentJar);
        command.add("-J--class-path=" + classPath);
        command.add("-J--add-reads=" + JLinkRegistrationAgent.MODULE_NAME + "=ALL-UNNAMED");
        command.add("-J--module-path=" + jlinkAgentJar);
        command.add("-J--add-modules=" + JLinkRegistrationAgent.MODULE_NAME);
        command.add(getPluginOption(instrumentation));
        command.add("--output=" + outputDirectory.getAbsolutePath());
        command.add("--add-modules");
        command.add(processModules(instrumentation, modules));
        ProcessBuilder builder = new ProcessBuilder(command);
        System.out.println(String.join(" ", builder.command()));
        Process process = builder.inheritIO().start();
        if (process.waitFor() != 0) {
            throw new RuntimeException("Failed to create instrumented runtime image");
        }
    }

    private static File storeOptions(Properties options) throws IOException {
        // Write the options to a temporary file
        File file = FileUtil.createTemporaryFile("instrument-", ".properties");
        try (FileWriter writer = new FileWriter(file)) {
            options.store(writer, null);
        }
        return file;
    }

    private static String buildClassPath(Instrumentation instrumentation) {
        return instrumentation.getClassPathElements().stream()
                .map(File::getAbsolutePath)
                .collect(Collectors.joining(File.pathSeparator));
    }

    private static String getPluginOption(Instrumentation instrumentation) throws IOException {
        return String.format(
                "--instrument=x:type=%s:options=%s",
                instrumentation.getClass().getName(),
                storeOptions(instrumentation.getOptions()).getAbsolutePath());
    }

    private static String processModules(Instrumentation instrumentation, String moduleString) {
        // Ensure that all required modules are included
        Set<String> modules = new HashSet<>(instrumentation.getRequiredModules());
        if (!moduleString.isEmpty()) {
            modules.addAll(Arrays.asList(moduleString.split(",")));
        }
        return String.join(",", modules);
    }
}
