package edu.neu.ccs.prl.galette.example;

import java.lang.invoke.MethodHandle;

@SuppressWarnings("unused")
public class MethodHandleExamples {
    public static int invokeIntIntIoInt(MethodHandle mh) throws Throwable {
        return (int) mh.invokeExact(7, 98);
    }
}
