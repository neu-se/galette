package edu.neu.ccs.prl.galette.eval;

import com.shadow.taint.agent.model.TaintLabel;
import com.shadow.taint.agent.model.TypedTaint;
import com.shadow.taint.agent.runtime.TaintCollector;
import com.shadow.taint.agent.runtime.TaintHeap;
import com.shadow.taint.agent.runtime.TaintTagger;
import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MirrorTaintTagManager implements TagManager {
    @Override
    public void setUp() {
        TaintHeap.startup();
    }

    @Override
    public void tearDown() {
        TaintHeap.shutdown();
    }

    @Override
    public boolean setLabels(boolean value, Object[] labels) {
        // Clear old labels
        clearLabels(TaintCollector.collectManually(value));
        // Add new labels
        for (Object label : labels) {
            if (!(label instanceof String)) {
                throw new IllegalArgumentException("Only string labels are supported");
            }
            value = TaintTagger.tagManually(value, (String) label);
        }
        return value;
    }

    @Override
    public byte setLabels(byte value, Object[] labels) {
        // Clear old labels
        clearLabels(TaintCollector.collectManually(value));
        // Add new labels
        for (Object label : labels) {
            if (!(label instanceof String)) {
                throw new IllegalArgumentException("Only string labels are supported");
            }
            value = TaintTagger.tagManually(value, (String) label);
        }
        return value;
    }

    @Override
    public char setLabels(char value, Object[] labels) {
        // Clear old labels
        clearLabels(TaintCollector.collectManually(value));
        // Add new labels
        for (Object label : labels) {
            if (!(label instanceof String)) {
                throw new IllegalArgumentException("Only string labels are supported");
            }
            value = TaintTagger.tagManually(value, (String) label);
        }
        return value;
    }

    @Override
    public short setLabels(short value, Object[] labels) {
        // Clear old labels
        clearLabels(TaintCollector.collectManually(value));
        // Add new labels
        for (Object label : labels) {
            if (!(label instanceof String)) {
                throw new IllegalArgumentException("Only string labels are supported");
            }
            value = TaintTagger.tagManually(value, (String) label);
        }
        return value;
    }

    @Override
    public int setLabels(int value, Object[] labels) {
        // Clear old labels
        clearLabels(TaintCollector.collectManually(value));
        // Add new labels
        for (Object label : labels) {
            if (!(label instanceof String)) {
                throw new IllegalArgumentException("Only string labels are supported");
            }
            value = TaintTagger.tagManually(value, (String) label);
        }
        return value;
    }

    @Override
    public long setLabels(long value, Object[] labels) {
        // Clear old labels
        clearLabels(TaintCollector.collectManually(value));
        // Add new labels
        for (Object label : labels) {
            if (!(label instanceof String)) {
                throw new IllegalArgumentException("Only string labels are supported");
            }
            value = TaintTagger.tagManually(value, (String) label);
        }
        return value;
    }

    @Override
    public float setLabels(float value, Object[] labels) {
        // Clear old labels
        clearLabels(TaintCollector.collectManually(value));
        // Add new labels
        for (Object label : labels) {
            if (!(label instanceof String)) {
                throw new IllegalArgumentException("Only string labels are supported");
            }
            value = TaintTagger.tagManually(value, (String) label);
        }
        return value;
    }

    @Override
    public double setLabels(double value, Object[] labels) {
        // Clear old labels
        clearLabels(TaintCollector.collectManually(value));
        // Add new labels
        for (Object label : labels) {
            if (!(label instanceof String)) {
                throw new IllegalArgumentException("Only string labels are supported");
            }
            value = TaintTagger.tagManually(value, (String) label);
        }
        return value;
    }

    @Override
    public <T> T setLabels(T value, Object[] labels) {
        Set<TaintLabel> labelSet = new HashSet<>();
        for (Object label : labels) {
            if (!(label instanceof String)) {
                throw new IllegalArgumentException("Only string labels are supported");
            }
            labelSet.add(new TaintLabel((String) label, "", new StackTraceElement[0]));
        }
        TypedTaint taint = TaintHeap.queryTaint(value);
        if (taint == null) {
            taint = new TypedTaint(10);
            TaintHeap.recordTaint(value, taint);
        }
        taint.setLabels(labelSet);
        return value;
    }

    @Override
    public Object[] getLabels(boolean value) {
        return extractLabels(TaintCollector.collectManually(value)).toArray();
    }

    @Override
    public Object[] getLabels(byte value) {
        return extractLabels(TaintCollector.collectManually(value)).toArray();
    }

    @Override
    public Object[] getLabels(char value) {
        return extractLabels(TaintCollector.collectManually(value)).toArray();
    }

    @Override
    public Object[] getLabels(short value) {
        return extractLabels(TaintCollector.collectManually(value)).toArray();
    }

    @Override
    public Object[] getLabels(int value) {
        return extractLabels(TaintCollector.collectManually(value)).toArray();
    }

    @Override
    public Object[] getLabels(long value) {
        return extractLabels(TaintCollector.collectManually(value)).toArray();
    }

    @Override
    public Object[] getLabels(float value) {
        return extractLabels(TaintCollector.collectManually(value)).toArray();
    }

    @Override
    public Object[] getLabels(double value) {
        return extractLabels(TaintCollector.collectManually(value)).toArray();
    }

    @Override
    public Object[] getLabels(Object value) {
        return extractLabels(TaintHeap.queryTaint(value)).toArray();
    }

    private static void clearLabels(Map<String, TypedTaint> taints) {
        if (taints != null) {
            for (TypedTaint taint : taints.values()) {
                Set<TaintLabel> labelSet = taint.getLabels();
                if (labelSet != null) {
                    labelSet.clear();
                }
            }
        }
    }

    private static Set<Object> extractLabels(Map<String, TypedTaint> taints) {
        Set<Object> result = new HashSet<>();
        if (taints != null) {
            for (TypedTaint taint : taints.values()) {
                result.addAll(extractLabels(taint));
            }
        }
        return result;
    }

    private static Set<Object> extractLabels(TypedTaint taint) {
        return taint == null ? Collections.emptySet() : extractLabels(taint.getLabels());
    }

    private static Set<Object> extractLabels(Set<TaintLabel> labels) {
        Set<Object> result = new HashSet<>();
        if (labels != null) {
            for (TaintLabel label : labels) {
                String name = label.getName();
                String original = getOriginalLabel(name);
                result.add(original);
            }
        }
        return result;
    }

    private static String getOriginalLabel(String label) {
        String target = "TaintTagger#tagManually#";
        if (label.startsWith(target)) {
            label = label.substring(target.length());
            int index = label.lastIndexOf('(');
            if (index != -1) {
                label = label.substring(0, index);
            }
            index = label.lastIndexOf(':');
            if (index != -1) {
                label = label.substring(0, index);
            }
        }
        return label;
    }
}
