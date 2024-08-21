package edu.neu.ccs.prl.galette.bench.extension;

import java.io.IOException;
import java.io.Serializable;

public interface FlowReport {
    void record(FlowReportEntry entry) throws IOException;

    final class FlowReportEntry implements Serializable {
        private static final long serialVersionUID = 1484508293276140432L;

        private final String testIdentifier;
        private final int truePositives;
        private final int falsePositives;
        private final int falseNegatives;
        private final String status;

        FlowReportEntry(
                String testIdentifier, int truePositives, int falsePositives, int falseNegatives, String status) {
            this.testIdentifier = testIdentifier;
            this.truePositives = truePositives;
            this.falsePositives = falsePositives;
            this.falseNegatives = falseNegatives;
            this.status = status;
        }

        public String getTestIdentifier() {
            return testIdentifier;
        }

        public int getTruePositives() {
            return truePositives;
        }

        public int getFalsePositives() {
            return falsePositives;
        }

        public int getFalseNegatives() {
            return falseNegatives;
        }

        public String getStatus() {
            return status;
        }
    }
}
