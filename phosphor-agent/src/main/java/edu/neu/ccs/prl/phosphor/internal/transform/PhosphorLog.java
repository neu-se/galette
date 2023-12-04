package edu.neu.ccs.prl.phosphor.internal.transform;

import java.io.PrintStream;

public final class PhosphorLog {
    private static PrintStream out;

    private PhosphorLog() {
        throw new AssertionError();
    }

    public static synchronized void initialize(PrintStream out) {
        PhosphorLog.out = out;
    }

    public static synchronized void info(String message) {
        if (out != null) {
            out.println("[PHOSPHOR INFO]: " + message);
        }
    }

    public static synchronized void error(String message, Throwable t) {
        if (out != null) {
            out.println("[PHOSPHOR ERROR]: " + message);
            t.printStackTrace(out);
        }
    }
}
