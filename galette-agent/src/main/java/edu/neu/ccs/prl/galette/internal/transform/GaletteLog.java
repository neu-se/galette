package edu.neu.ccs.prl.galette.internal.transform;

import java.io.PrintStream;

public final class GaletteLog {
    private static PrintStream out;

    private GaletteLog() {
        throw new AssertionError();
    }

    public static synchronized void initialize(PrintStream out) {
        GaletteLog.out = out;
    }

    public static synchronized void info(String message) {
        if (out != null) {
            out.println("[GALETTE INFO]: " + message);
        }
    }

    public static synchronized void error(String message, Throwable t) {
        if (out != null) {
            out.println("[GALETTE ERROR]: " + message);
            t.printStackTrace(out);
        }
    }
}
