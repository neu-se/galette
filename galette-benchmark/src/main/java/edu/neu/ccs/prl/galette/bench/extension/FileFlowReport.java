package edu.neu.ccs.prl.galette.bench.extension;

import java.io.*;
import java.nio.file.Files;

public class FileFlowReport implements FlowReport {
    private final File report;

    public FileFlowReport(File report) throws IOException {
        Files.createDirectories(report.getParentFile().toPath());
        this.report = report;
        boolean append = Boolean.getBoolean("flow.report.append");
        if (!report.isFile() || !append) {
            try (PrintWriter out = new PrintWriter(new FileOutputStream(report, false))) {
                out.println("test,tp,fp,fn,status");
            }
        }
    }

    public void record(String testIdentifier, int truePositives, int falsePositives, int falseNegatives, String status)
            throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(new FileOutputStream(report, true))) {
            out.printf(
                    "\"%s\",%d,%d,%d,%s%n",
                    testIdentifier.replace('"', '\''),
                    truePositives,
                    falsePositives,
                    falseNegatives,
                    status
            );
        }
    }

    @Override
    public void record(FlowReportEntry entry) throws IOException {
        record(
                entry.getTestIdentifier(),
                entry.getTruePositives(),
                entry.getFalsePositives(),
                entry.getFalseNegatives(),
                entry.getStatus());
    }
}
