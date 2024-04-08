package edu.neu.ccs.prl.galette.bench.extension;

import java.util.LinkedList;
import java.util.List;

public class ListFlowReport implements FlowReport {
    private final List<FlowReportEntry> entries = new LinkedList<>();

    @Override
    public void record(FlowReportEntry entry) {
        entries.add(entry);
    }

    public List<FlowReportEntry> getEntries() {
        return entries;
    }
}
