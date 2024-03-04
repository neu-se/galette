package edu.neu.ccs.prl.galette;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReferenceITCase {
    @Test
    void weakReference() {
        Object expected = new Object();
        Reference<?> reference = new WeakReference<>(expected);
        Assertions.assertEquals(expected, reference.get());
    }

    @Test
    void softReference() {
        Object expected = new Object();
        Reference<?> reference = new SoftReference<>(expected);
        Assertions.assertEquals(expected, reference.get());
    }
}
