package edu.neu.ccs.prl.galette.internal.transform;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

import edu.neu.ccs.prl.galette.example.MethodHandleExamples;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IndirectFramePasserTest {
    private static final MethodHandles.Lookup lookup = lookup();

    @Test
    void invokeIntIntIoInt() throws ReflectiveOperationException {
        Class<?> instrumented =
                AsmTestUtil.instrumentAndLoad(MethodHandleExamples.class, GaletteTransformerTest::instrument);
        Method method = instrumented.getDeclaredMethod("invokeIntIntIoInt", MethodHandle.class, TagFrame.class);
        MethodType mt = methodType(int.class, int.class, int.class);
        MethodHandle mh = lookup.findStatic(Math.class, "max", mt);
        int result = (int) method.invoke(null, mh, TagFrame.emptyFrame());
        Assertions.assertEquals(98, result);
    }
}
