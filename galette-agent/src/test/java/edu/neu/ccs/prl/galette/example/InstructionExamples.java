package edu.neu.ccs.prl.galette.example;

@SuppressWarnings({"unused", "ConstantValue", "MismatchedReadAndWriteOfArray", "DataFlowIssue"})
public class InstructionExamples {
    public static Object loadNullConstant() {
        return null;
    }

    public static int loadIntConstant() {
        return -1;
    }

    public static long loadZeroLongConstant() {
        return 0L;
    }

    public static long loadLongConstant() {
        return -88L;
    }

    public static String loadStringConstant() {
        return "hello";
    }

    public static int intArrayLoad() {
        int[] x = new int[1];
        return x[0];
    }

    public static long longArrayLoad() {
        long[] x = new long[1];
        return x[0];
    }

    public static boolean intArrayStore() {
        int[] x = new int[1];
        x[0] = 9;
        return true;
    }

    public static boolean longArrayStore() {
        long[] x = new long[1];
        x[0] = 99;
        return true;
    }

    public static int intAdd() {
        return 7 + 99;
    }

    public static long longAdd() {
        return 7L + 99L;
    }

    public static long longShift() {
        return 98889L >> 2;
    }

    public static boolean longCompare() {
        return 98889L == -99L;
    }

    public static char intCast() {
        return (char) 99;
    }

    public static double longCast() {
        return (double) 8L;
    }

    public static int arrayLength() {
        int[] x = new int[1];
        return x.length;
    }

    public static int monitor() {
        synchronized (InstructionExamples.class) {
            return 9;
        }
    }

    public static void newIntArray() {
        int[] x = new int[1];
    }

    public static void newLongArray() {
        long[] x = new long[1];
    }

    public static int exceptionHandler() {
        try {
            Object x = null;
            return x.hashCode();
        } catch (NullPointerException e) {
            return 7;
        }
    }
}
