package edu.neu.ccs.prl.phosphor.internal.transform;

public final class ExclusionList {
    private final String[] exclusions;

    public ExclusionList(String... exclusions) {
        this.exclusions = exclusions.clone();
    }

    public ExclusionList(ExclusionList other, String... rest) {
        String[] values = other.exclusions;
        exclusions = new String[values.length + rest.length];
        System.arraycopy(values, 0, exclusions, 0, values.length);
        System.arraycopy(rest, 0, exclusions, values.length, rest.length);
    }

    public boolean isExcluded(String className) {
        for (String e : exclusions) {
            if (startsWith(className, e)) {
                return true;
            }
        }
        return false;
    }

    public static boolean startsWith(String receiver, String prefix) {
        char[] values = receiver.toCharArray();
        char[] prefixValues = prefix.toCharArray();
        if (values.length < prefixValues.length) {
            return false;
        } else {
            for (int i = 0; i < prefixValues.length; i++) {
                if (values[i] != prefixValues[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    public static ExclusionList createFromProperty(String key) {
        String s = System.getProperty(key);
        String[] values = s == null ? new String[0] : s.split(",");
        return new ExclusionList(values);
    }
}
