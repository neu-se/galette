package edu.neu.ccs.prl.galette.bench.extension;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;

final class FlowReport {
    private final File report;

    FlowReport(File report) throws IOException {
        Files.createDirectories(report.getParentFile().toPath());
        this.report = report;
        boolean append = Boolean.getBoolean("flow.report.append");
        if (!report.isFile() || !append) {
            try (PrintWriter out = new PrintWriter(new FileOutputStream(report, false))) {
                out.println("class,method,tp,fp,fn,status");
            }
        }
    }

    void recordEntry(Class<?> testClass, Method testMethod, FlowChecker checker, String status)
            throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(new FileOutputStream(report, true))) {
            out.printf(
                    "%s,%s,%d,%d,%d,%s%n",
                    testClass.getName(),
                    testMethod.getName(),
                    checker.getTruePositives(),
                    checker.getFalsePositives(),
                    checker.getFalseNegatives(),
                    status);
        }
    }
}
