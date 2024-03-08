package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.FlowBench;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

@FlowBench
@EnabledForJreRange(min = JRE.JAVA_9)
public class VarHandleITCase {
    public static float f = 7.0f;

    @Test
    void getAndAdd() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        VarHandle vh = lookup.findStaticVarHandle(VarHandleITCase.class, "f", float.class);
        float old = (float) vh.getAndAdd(5.0f);
        Assertions.assertEquals(old, 7.0f);
        Assertions.assertEquals(f, 12.0f);
    }
}
