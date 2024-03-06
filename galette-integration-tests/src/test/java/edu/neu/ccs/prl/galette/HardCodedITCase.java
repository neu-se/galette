package edu.neu.ccs.prl.galette;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Checks that classes that contain fields that may be accessed by the JVM using a
 * hard-coded offset work properly.
 */
@SuppressWarnings("WrapperTypeMayBePrimitive")
public class HardCodedITCase {
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

    @Test
    void stackTraceElement() {
        Throwable t = new IllegalStateException();
        StackTraceElement[] trace = t.getStackTrace();
        StackTraceElement element = trace[0];
        Assertions.assertNotNull(element.getClassName());
        Assertions.assertNotNull(element.toString());
    }

    @Test
    void taintedStackTraceElement() {
        String declaringClass = Tainter.setTag("X", Tag.of("X"));
        String methodName = Tainter.setTag("M", Tag.of("M"));
        String fileName = Tainter.setTag(null, Tag.of("F"));
        int lineNumber = Tainter.setTag(-1, Tag.of(-1));
        StackTraceElement e = new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
        TagAssertions.assertTagEquals(e.getClassName(), new Object[] {"X"});
        TagAssertions.assertTagEquals(e.getMethodName(), new Object[] {"M"});
        TagAssertions.assertTagEquals(e.getFileName(), new Object[] {"F"});
        TagAssertions.assertTagEquals(e.getLineNumber(), -1);
    }

    @Test
    void throwable() {
        Object o = null;
        try {
            //noinspection DataFlowIssue,ResultOfMethodCallIgnored
            o.hashCode();
        } catch (NullPointerException e) {
            Assertions.assertDoesNotThrow(e::getMessage);
            Assertions.assertNotNull(e.getSuppressed());
            Assertions.assertNotNull(e.getStackTrace());
            e.initCause(new RuntimeException());
            Assertions.assertNotNull(e.getCause());
        }
    }

    @Test
    void booleanClass() {
        Assertions.assertEquals(boolean.class, Boolean.TYPE);
        Boolean b = true;
        Assertions.assertTrue(b);
        Assertions.assertTrue(Boolean.TRUE);
        Assertions.assertFalse(Boolean.FALSE);
    }

    @Test
    void characterClass() {
        Assertions.assertEquals(2, Character.BYTES);
        Assertions.assertEquals('\uFFFF', Character.MAX_VALUE);
        Assertions.assertEquals('\u0000', Character.MIN_VALUE);
        Assertions.assertEquals(16, Character.SIZE);
        Assertions.assertEquals(char.class, Character.TYPE);
        Character x = 7;
        Assertions.assertEquals(7, x.charValue());
    }

    @Test
    void floatClass() {
        Assertions.assertEquals(4, Float.BYTES);
        Assertions.assertEquals(3.4028235E38f, Float.MAX_VALUE);
        Assertions.assertEquals(1.4E-45f, Float.MIN_VALUE);
        Assertions.assertEquals(32, Float.SIZE);
        Assertions.assertEquals(float.class, Float.TYPE);
        Float x = 7.0f;
        Assertions.assertEquals(7.0, x.floatValue());
    }

    @Test
    void doubleClass() {
        Assertions.assertEquals(8, Double.BYTES);
        Assertions.assertEquals(1.7976931348623157E308, Double.MAX_VALUE);
        Assertions.assertEquals(4.9E-324, Double.MIN_VALUE);
        Assertions.assertEquals(64, Double.SIZE);
        Assertions.assertEquals(double.class, Double.TYPE);
        Double x = 7.0;
        Assertions.assertEquals(7.0, x.doubleValue());
    }

    @Test
    void byteClass() {
        Assertions.assertEquals(1, Byte.BYTES);
        Assertions.assertEquals(127, Byte.MAX_VALUE);
        Assertions.assertEquals(-128, Byte.MIN_VALUE);
        Assertions.assertEquals(8, Byte.SIZE);
        Assertions.assertEquals(byte.class, Byte.TYPE);
        Byte x = 7;
        Assertions.assertEquals(7, x.byteValue());
    }

    @Test
    void shortClass() {
        Assertions.assertEquals(2, Short.BYTES);
        Assertions.assertEquals(32767, Short.MAX_VALUE);
        Assertions.assertEquals(-32768, Short.MIN_VALUE);
        Assertions.assertEquals(16, Short.SIZE);
        Assertions.assertEquals(short.class, Short.TYPE);
        Short x = 7;
        Assertions.assertEquals(7, x.shortValue());
    }

    @Test
    void integerClass() {
        Assertions.assertEquals(4, Integer.BYTES);
        Assertions.assertEquals(0x7fffffff, Integer.MAX_VALUE);
        Assertions.assertEquals(0x80000000, Integer.MIN_VALUE);
        Assertions.assertEquals(32, Integer.SIZE);
        Assertions.assertEquals(int.class, Integer.TYPE);
        Integer x = 7;
        Assertions.assertEquals(7, x.intValue());
    }

    @Test
    void longClass() {
        Assertions.assertEquals(8, Long.BYTES);
        Assertions.assertEquals(0x7fffffffffffffffL, Long.MAX_VALUE);
        Assertions.assertEquals(0x8000000000000000L, Long.MIN_VALUE);
        Assertions.assertEquals(64, Long.SIZE);
        Assertions.assertEquals(long.class, Long.TYPE);
        Long x = 7L;
        Assertions.assertEquals(7L, x.longValue());
    }
}
