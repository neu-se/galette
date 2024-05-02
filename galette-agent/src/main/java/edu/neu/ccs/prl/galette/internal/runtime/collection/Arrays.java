package edu.neu.ccs.prl.galette.internal.runtime.collection;

public final class Arrays {
    private Arrays() {
        throw new AssertionError();
    }

    public static boolean equals(byte[] a1, byte[] a2) {
        if (a1.length != a2.length) {
            return false;
        }
        for (int i = 0; i < a1.length; i++) {
            if (a1[i] != a2[i]) {
                return false;
            }
        }
        return true;
    }

    public static String toString(Object[] array, int size) {
        String[] elements = new String[size];
        for (int i = 0; i < size; i++) {
            elements[i] = array[i] == null ? "null" : array[i].toString();
        }
        return "[" + String.join(", ", elements) + "]";
    }
}
