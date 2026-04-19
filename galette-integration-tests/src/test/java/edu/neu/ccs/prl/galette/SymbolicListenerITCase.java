package edu.neu.ccs.prl.galette;

import static edu.neu.ccs.prl.galette.internal.runtime.symbolic.SymbolicOpcodes.*;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.Tainter;
import edu.neu.ccs.prl.galette.internal.runtime.symbolic.SymbolicExecutionListener;
import edu.neu.ccs.prl.galette.internal.runtime.symbolic.SymbolicListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for the {@link SymbolicExecutionListener} SPI.
 *
 * <p>The test methods themselves run inside a Galette-instrumented JDK so
 * every branch, switch, iinc, and int-arith in the test body also fires
 * hooks. To avoid that noise, the recording listener filters events by a
 * unique label; each test produces a tagged value carrying that label and
 * asserts that exactly the expected filtered events were seen.
 */
public class SymbolicListenerITCase {
    private RecordingListener listener;

    @BeforeEach
    void installListener() {
        listener = new RecordingListener("LBL");
        SymbolicListener.setListener(listener);
    }

    @AfterEach
    void uninstallListener() {
        SymbolicListener.setListener(null);
        listener = null;
    }

    // ---------- onIntBranch ----------

    @Test
    void intBranchFiresWithOpcodeValueAndTag() {
        int x = Tainter.setTag(5, Tag.of("LBL"));
        // javac compiles `if (x == 0) body` as IFNE skip-body, so the listener
        // observes IFNE with the fall-through outcome (not taken).
        //noinspection ConstantValue
        if (x == 0) {
            Assertions.fail("unreachable");
        }
        List<Event> matched = listener.matching("intBranch");
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertEquals(IFNE, e.opcode);
        Assertions.assertEquals(5, e.value1);
        assertTagHasLabel(e.tag1, "LBL");
    }

    @Test
    void intBranchDoesNotFireWhenUntagged() {
        int x = 5;
        //noinspection ConstantValue
        if (x == 0) {
            Assertions.fail("unreachable");
        }
        Assertions.assertEquals(0, listener.matching("intBranch").size());
    }

    @Test
    void intBranchFiresForEachIfVariant() {
        int x = Tainter.setTag(3, Tag.of("LBL"));
        //noinspection ConstantValue
        if (x != 0) {
            /* IFEQ: branch-if-zero */
        }
        //noinspection ConstantValue
        if (x == 0) {
            /* IFNE */
        }
        if (x >= 0) {
            /* IFLT */
        }
        if (x < 0) {
            /* IFGE */
        }
        if (x <= 0) {
            /* IFGT */
        }
        //noinspection ConstantValue
        if (x > 0) {
            /* IFLE */
        }
        List<Event> matched = listener.matching("intBranch");
        // javac compiles "x != 0" as IFEQ-branch-to-else etc.; check that all
        // six single-operand opcodes appeared.
        boolean[] seen = new boolean[6];
        for (Event e : matched) {
            if (e.opcode == IFEQ) seen[0] = true;
            else if (e.opcode == IFNE) seen[1] = true;
            else if (e.opcode == IFLT) seen[2] = true;
            else if (e.opcode == IFGE) seen[3] = true;
            else if (e.opcode == IFGT) seen[4] = true;
            else if (e.opcode == IFLE) seen[5] = true;
        }
        for (int i = 0; i < 6; i++) {
            Assertions.assertTrue(seen[i], "missing IF* opcode index " + i + ", saw " + matched);
        }
    }

    // ---------- onIntCmpBranch ----------

    @Test
    void intCmpBranchFiresWithBothValuesAndTags() {
        int a = Tainter.setTag(7, Tag.of("LBL"));
        int b = Tainter.setTag(9, Tag.of("LBL2"));
        // javac compiles `if (a == b) body` as IF_ICMPNE skip-body.
        if (a == b) {
            Assertions.fail("unreachable");
        }
        List<Event> matched = listener.matching("intCmpBranch");
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertEquals(IF_ICMPNE, e.opcode);
        Assertions.assertEquals(7, e.value1);
        Assertions.assertEquals(9, e.value2);
        assertTagHasLabel(e.tag1, "LBL");
        assertTagHasLabel(e.tag2, "LBL2");
    }

    @Test
    void intCmpBranchDoesNotFireWhenBothUntagged() {
        int a = 7;
        int b = 9;
        if (a == b) {
            Assertions.fail("unreachable");
        }
        Assertions.assertEquals(0, listener.matching("intCmpBranch").size());
    }

    @Test
    void intCmpBranchFiresWhenOnlyOneSideIsTagged() {
        int a = Tainter.setTag(7, Tag.of("LBL"));
        int b = 9;
        if (a == b) {
            Assertions.fail("unreachable");
        }
        List<Event> matched = listener.matching("intCmpBranch");
        Assertions.assertEquals(1, matched.size());
        assertTagHasLabel(matched.get(0).tag1, "LBL");
    }

    @Test
    void intCmpBranchFiresForEachIfIcmpVariant() {
        int a = Tainter.setTag(4, Tag.of("LBL"));
        int b = 5;
        if (a == b) {}
        if (a != b) {}
        if (a < b) {}
        if (a >= b) {}
        if (a > b) {}
        if (a <= b) {}
        int[] seen = new int[6];
        for (Event e : listener.matching("intCmpBranch")) {
            if (e.opcode == IF_ICMPEQ) {
                seen[0]++;
            } else if (e.opcode == IF_ICMPNE) {
                seen[1]++;
            } else if (e.opcode == IF_ICMPLT) {
                seen[2]++;
            } else if (e.opcode == IF_ICMPGE) {
                seen[3]++;
            } else if (e.opcode == IF_ICMPGT) {
                seen[4]++;
            } else if (e.opcode == IF_ICMPLE) {
                seen[5]++;
            }
        }
        for (int i = 0; i < 6; i++) {
            Assertions.assertTrue(seen[i] > 0, "missing IF_ICMP* opcode index " + i);
        }
    }

    // ---------- onRefBranch ----------

    @Test
    void refBranchFiresForIfnullWithTag() {
        Object o = Tainter.setTag(new Object(), Tag.of("LBL"));
        if (o == null) {
            Assertions.fail("unreachable");
        }
        List<Event> matched = listener.matching("refBranch");
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertTrue(e.opcode == IFNULL || e.opcode == IFNONNULL, "opcode " + e.opcode);
        Assertions.assertSame(o, e.refValue1);
        assertTagHasLabel(e.tag1, "LBL");
    }

    @Test
    void refBranchFiresForIfnonnullWithNullValue() {
        Object o = Tainter.setTag((Object) null, Tag.of("LBL"));
        if (o != null) {
            Assertions.fail("unreachable");
        }
        List<Event> matched = listener.matching("refBranch");
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertNull(e.refValue1);
        assertTagHasLabel(e.tag1, "LBL");
    }

    @Test
    void refBranchDoesNotFireWhenUntagged() {
        Object o = new Object();
        if (o == null) {
            Assertions.fail("unreachable");
        }
        Assertions.assertEquals(0, listener.matching("refBranch").size());
    }

    // ---------- onRefCmpBranch ----------

    @Test
    void refCmpBranchFiresWithTags() {
        Object a = Tainter.setTag(new Object(), Tag.of("LBL"));
        Object b = Tainter.setTag(new Object(), Tag.of("LBL2"));
        if (a == b) {
            Assertions.fail("unreachable");
        }
        List<Event> matched = listener.matching("refCmpBranch");
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertTrue(e.opcode == IF_ACMPEQ || e.opcode == IF_ACMPNE);
        Assertions.assertSame(a, e.refValue1);
        Assertions.assertSame(b, e.refValue2);
        assertTagHasLabel(e.tag1, "LBL");
        assertTagHasLabel(e.tag2, "LBL2");
    }

    @Test
    void refCmpBranchDoesNotFireWhenBothUntagged() {
        Object a = new Object();
        Object b = new Object();
        if (a == b) {
            Assertions.fail("unreachable");
        }
        Assertions.assertEquals(0, listener.matching("refCmpBranch").size());
    }

    // ---------- onTableSwitch ----------

    @Test
    void tableSwitchFiresWithValueMinMaxAndTag() {
        int x = Tainter.setTag(2, Tag.of("LBL"));
        int result;
        switch (x) {
            case 0:
                result = 0;
                break;
            case 1:
                result = 1;
                break;
            case 2:
                result = 2;
                break;
            case 3:
                result = 3;
                break;
            default:
                result = -1;
        }
        Assertions.assertEquals(2, result);
        List<Event> matched = listener.matching("tableSwitch");
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertEquals(TABLESWITCH, e.opcode);
        Assertions.assertEquals(2, e.value1);
        Assertions.assertEquals(0, e.switchMin);
        Assertions.assertEquals(3, e.switchMax);
        assertTagHasLabel(e.tag1, "LBL");
    }

    @Test
    void tableSwitchDoesNotFireWhenUntagged() {
        int x = 2;
        int result;
        switch (x) {
            case 0:
                result = 0;
                break;
            case 1:
                result = 1;
                break;
            case 2:
                result = 2;
                break;
            default:
                result = -1;
        }
        Assertions.assertEquals(2, result);
        Assertions.assertEquals(0, listener.matching("tableSwitch").size());
    }

    // ---------- onLookupSwitch ----------

    @Test
    void lookupSwitchFiresWithValueKeysAndTag() {
        int x = Tainter.setTag(100, Tag.of("LBL"));
        int result;
        // Sparse keys compile to LOOKUPSWITCH.
        switch (x) {
            case 1:
                result = 1;
                break;
            case 100:
                result = 2;
                break;
            case 10000:
                result = 3;
                break;
            default:
                result = -1;
        }
        Assertions.assertEquals(2, result);
        List<Event> matched = listener.matching("lookupSwitch");
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertEquals(LOOKUPSWITCH, e.opcode);
        Assertions.assertEquals(100, e.value1);
        Assertions.assertNotNull(e.switchKeys);
        Assertions.assertArrayEquals(new int[] {1, 100, 10000}, e.switchKeys);
        assertTagHasLabel(e.tag1, "LBL");
    }

    @Test
    void lookupSwitchDoesNotFireWhenUntagged() {
        int x = 100;
        int result;
        switch (x) {
            case 1:
                result = 1;
                break;
            case 100:
                result = 2;
                break;
            case 10000:
                result = 3;
                break;
            default:
                result = -1;
        }
        Assertions.assertEquals(2, result);
        Assertions.assertEquals(0, listener.matching("lookupSwitch").size());
    }

    // ---------- onIinc ----------

    @Test
    void iincFiresWithIncrementAndPreTag() {
        int i = Tainter.setTag(10, Tag.of("LBL"));
        i += 3; // IINC when i is a local and increment fits in a byte/short.
        Assertions.assertEquals(13, i);
        List<Event> matched = listener.matching("iinc");
        Assertions.assertTrue(matched.size() >= 1, "expected at least one iinc event, got " + matched);
        Event e = matched.get(0);
        Assertions.assertEquals(3, e.iincIncrement);
        assertTagHasLabel(e.tag1, "LBL");
    }

    @Test
    void iincDoesNotFireWhenUntagged() {
        int i = 10;
        i += 3;
        Assertions.assertEquals(13, i);
        Assertions.assertEquals(0, listener.matching("iinc").size());
    }

    // ---------- onIntArith ----------

    @Test
    void intArithFiresAndResultTagIsReturnedTag() {
        int a = Tainter.setTag(6, Tag.of("LBL"));
        int b = Tainter.setTag(7, Tag.of("LBL2"));
        // Replace the default union with a distinctive override tag, so we
        // can verify the listener's returned tag is what the result carries.
        final Tag override = Tag.of("RESULT");
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public Tag onIntArith(int opcode, int value1, int value2, Tag tag1, Tag tag2) {
                listener.recordArith(opcode, value1, value2, tag1, tag2);
                return override;
            }
        });
        int c = a + b;
        Assertions.assertEquals(13, c);
        assertTagHasLabel(Tainter.getTag(c), "RESULT");

        List<Event> matched = listener.matching("intArith");
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertEquals(IADD, e.opcode);
        Assertions.assertEquals(6, e.value1);
        Assertions.assertEquals(7, e.value2);
        assertTagHasLabel(e.tag1, "LBL");
        assertTagHasLabel(e.tag2, "LBL2");
    }

    @Test
    void intArithDefaultUnionsInputTags() {
        int a = Tainter.setTag(6, Tag.of("LBL"));
        int b = Tainter.setTag(7, Tag.of("OTHER"));
        int c = a + b;
        Assertions.assertEquals(13, c);
        Object[] labels = Tag.getLabels(Tainter.getTag(c));
        Assertions.assertNotNull(labels);
        List<Object> list = Arrays.asList(labels);
        Assertions.assertTrue(list.contains("LBL"), "expected LBL in " + list);
        Assertions.assertTrue(list.contains("OTHER"), "expected OTHER in " + list);
    }

    @Test
    void intArithDoesNotFireWhenBothUntagged() {
        int a = 6;
        int b = 7;
        int c = a + b;
        Assertions.assertEquals(13, c);
        Assertions.assertEquals(0, listener.matching("intArith").size());
        // Result should carry no tag.
        Assertions.assertTrue(Tag.isEmpty(Tainter.getTag(c)));
    }

    @Test
    void intArithCoversAllIntBinaryOpcodes() {
        int a = Tainter.setTag(12, Tag.of("LBL"));
        int b = Tainter.setTag(3, Tag.of("LBL"));
        int sink = 0;
        sink ^= a + b; // IADD
        sink ^= a - b; // ISUB
        sink ^= a * b; // IMUL
        sink ^= a / b; // IDIV
        sink ^= a % b; // IREM
        sink ^= a << b; // ISHL
        sink ^= a >> b; // ISHR
        sink ^= a >>> b; // IUSHR
        sink ^= a & b; // IAND
        sink ^= a | b; // IOR
        sink ^= a ^ b; // IXOR
        // consume sink so javac doesn't dead-code-eliminate it
        Assertions.assertNotEquals(Integer.MIN_VALUE + 1, sink);
        boolean[] seen = new boolean[11];
        int[] expected = {IADD, ISUB, IMUL, IDIV, IREM, ISHL, ISHR, IUSHR, IAND, IOR, IXOR};
        for (Event e : listener.matching("intArith")) {
            for (int i = 0; i < expected.length; i++) {
                if (e.opcode == expected[i]) {
                    seen[i] = true;
                }
            }
        }
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertTrue(seen[i], "missing int-arith opcode " + expected[i]);
        }
    }

    // ---------- onIntUnary ----------

    @Test
    void intUnaryFiresAndResultTagIsReturnedTag() {
        int a = Tainter.setTag(5, Tag.of("LBL"));
        final Tag override = Tag.of("NEG");
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public Tag onIntUnary(int opcode, int value, Tag tag) {
                listener.recordUnary(opcode, value, tag);
                return override;
            }
        });
        int c = -a;
        Assertions.assertEquals(-5, c);
        assertTagHasLabel(Tainter.getTag(c), "NEG");

        List<Event> matched = listener.matching("intUnary");
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertEquals(INEG, e.opcode);
        Assertions.assertEquals(5, e.value1);
        assertTagHasLabel(e.tag1, "LBL");
    }

    @Test
    void intUnaryDefaultForwardsInputTag() {
        int a = Tainter.setTag(5, Tag.of("LBL"));
        int c = -a;
        Assertions.assertEquals(-5, c);
        assertTagHasLabel(Tainter.getTag(c), "LBL");
    }

    @Test
    void intUnaryDoesNotFireWhenUntagged() {
        int a = 5;
        int c = -a;
        Assertions.assertEquals(-5, c);
        Assertions.assertEquals(0, listener.matching("intUnary").size());
    }

    @Test
    void intUnaryCoversAllUnaryOpcodes() {
        int a = Tainter.setTag(0x41424344, Tag.of("LBL"));
        int sink = 0;
        sink ^= -a; // INEG
        sink ^= (byte) a; // I2B
        sink ^= (char) a; // I2C
        sink ^= (short) a; // I2S
        Assertions.assertNotEquals(Integer.MIN_VALUE + 1, sink);
        boolean[] seen = new boolean[4];
        int[] expected = {INEG, I2B, I2C, I2S};
        for (Event e : listener.matching("intUnary")) {
            for (int i = 0; i < expected.length; i++) {
                if (e.opcode == expected[i]) {
                    seen[i] = true;
                }
            }
        }
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertTrue(seen[i], "missing int-unary opcode " + expected[i]);
        }
    }

    // ---------- onFloatArith ----------

    @Test
    void floatArithFiresForFaddWithValuesAndTags() {
        float a = Tainter.setTag(1.5f, Tag.of("LBL"));
        float b = Tainter.setTag(2.25f, Tag.of("LBL2"));
        float c = a + b;
        // Don't call Assertions.assertEquals(float, c) here — its internals
        // perform tagged float arithmetic and would contribute extra events.
        if (c != 3.75f) {
            Assertions.fail("expected 3.75f, got " + c);
        }
        // Exactly one event with both LBL and LBL2 present: the intended FADD.
        List<Event> faddEvents = new java.util.ArrayList<>();
        for (Event ev : listener.matching("floatArith")) {
            if (ev.opcode == FADD
                    && labels(ev.tag1).contains("LBL")
                    && labels(ev.tag2).contains("LBL2")) {
                faddEvents.add(ev);
            }
        }
        Assertions.assertEquals(1, faddEvents.size());
        Event e = faddEvents.get(0);
        Assertions.assertEquals(1.5f, e.fvalue1);
        Assertions.assertEquals(2.25f, e.fvalue2);
    }

    private static List<Object> labels(Tag t) {
        Object[] a = Tag.getLabels(t);
        return a == null ? Collections.emptyList() : Arrays.asList(a);
    }

    @Test
    void floatArithDoesNotFireWhenBothUntagged() {
        float a = 1.5f;
        float b = 2.25f;
        float c = a + b;
        Assertions.assertEquals(3.75f, c);
        Assertions.assertEquals(0, listener.matching("floatArith").size());
    }

    @Test
    void floatArithCoversAllFloatBinaryOpcodes() {
        float a = Tainter.setTag(3.0f, Tag.of("LBL"));
        float b = Tainter.setTag(2.0f, Tag.of("LBL"));
        float sink = 0f;
        sink += a + b; // FADD
        sink += a - b; // FSUB
        sink += a * b; // FMUL
        sink += a / b; // FDIV
        sink += a % b; // FREM
        // FCMPL / FCMPG: javac emits these for float comparisons in branches
        if (a < b) {
            sink += 1f;
        } // FCMPG
        if (a > b) {
            sink += 2f;
        } // FCMPL
        Assertions.assertNotEquals(Float.MIN_VALUE, sink);
        int[] expected = {FADD, FSUB, FMUL, FDIV, FREM, FCMPL, FCMPG};
        boolean[] seen = new boolean[expected.length];
        for (Event e : listener.matching("floatArith")) {
            for (int i = 0; i < expected.length; i++) {
                if (e.opcode == expected[i]) {
                    seen[i] = true;
                }
            }
        }
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertTrue(seen[i], "missing float-arith opcode " + expected[i]);
        }
    }

    @Test
    void floatArithReturnedTagBecomesResultTag() {
        float a = Tainter.setTag(4.0f, Tag.of("LBL"));
        float b = Tainter.setTag(5.0f, Tag.of("LBL2"));
        final Tag override = Tag.of("FRESULT");
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public Tag onFloatArith(int opcode, float v1, float v2, Tag t1, Tag t2) {
                return override;
            }
        });
        float c = a + b;
        Assertions.assertEquals(9.0f, c);
        assertTagHasLabel(Tainter.getTag(c), "FRESULT");
    }

    // ---------- onFloatUnary ----------

    @Test
    void floatUnaryFiresForFneg() {
        float a = Tainter.setTag(7.5f, Tag.of("LBL"));
        float c = -a;
        Assertions.assertEquals(-7.5f, c);
        List<Event> matched = listener.matching("floatUnary");
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertEquals(FNEG, e.opcode);
        Assertions.assertEquals(7.5f, e.fvalue1);
    }

    @Test
    void floatUnaryDoesNotFireWhenUntagged() {
        float a = 7.5f;
        float c = -a;
        Assertions.assertEquals(-7.5f, c);
        Assertions.assertEquals(0, listener.matching("floatUnary").size());
    }

    // ---------- onCat1Convert ----------

    @Test
    void cat1ConvertFiresForI2F() {
        int i = Tainter.setTag(42, Tag.of("LBL"));
        float f = (float) i;
        Assertions.assertEquals(42.0f, f);
        List<Event> matched = listener.matching("cat1Convert");
        Assertions.assertEquals(1, matched.size());
        Assertions.assertEquals(I2F, matched.get(0).opcode);
    }

    @Test
    void cat1ConvertFiresForF2I() {
        float f = Tainter.setTag(42.5f, Tag.of("LBL"));
        int i = (int) f;
        Assertions.assertEquals(42, i);
        List<Event> matched = listener.matching("cat1Convert");
        Assertions.assertEquals(1, matched.size());
        Assertions.assertEquals(F2I, matched.get(0).opcode);
    }

    @Test
    void cat1ConvertPreservesTagByDefault() {
        int i = Tainter.setTag(42, Tag.of("LBL"));
        float f = (float) i;
        Assertions.assertEquals(42.0f, f);
        assertTagHasLabel(Tainter.getTag(f), "LBL");
    }

    @Test
    void cat1ConvertDoesNotFireWhenUntagged() {
        int i = 42;
        float f = (float) i;
        Assertions.assertEquals(42.0f, f);
        Assertions.assertEquals(0, listener.matching("cat1Convert").size());
    }

    // ---------- Exception swallowing ----------

    @Test
    void listenerExceptionsAreSwallowed() {
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public void onIntBranch(int opcode, int value, Tag tag) {
                throw new RuntimeException("boom");
            }

            @Override
            public Tag onIntArith(int opcode, int value1, int value2, Tag tag1, Tag tag2) {
                throw new RuntimeException("boom-arith");
            }

            @Override
            public Tag onIntUnary(int opcode, int value, Tag tag) {
                throw new RuntimeException("boom-unary");
            }
        });
        int x = Tainter.setTag(5, Tag.of("LBL"));
        int y = Tainter.setTag(7, Tag.of("LBL"));
        Assertions.assertDoesNotThrow(() -> {
            //noinspection ConstantValue
            if (x == 0) {
                Assertions.fail("unreachable");
            }
            int sum = x + y;
            int neg = -sum;
            Assertions.assertEquals(-12, neg);
            // When onIntArith throws, dispatcher falls back to Tag.union of inputs.
            Object[] labels = Tag.getLabels(Tainter.getTag(sum));
            Assertions.assertNotNull(labels);
            Assertions.assertTrue(Arrays.asList(labels).contains("LBL"));
        });
    }

    // ---------- Helpers ----------

    private static void assertTagHasLabel(Tag tag, String label) {
        Object[] labels = Tag.getLabels(tag);
        Assertions.assertNotNull(labels, "expected non-null labels for tag");
        Assertions.assertTrue(
                Arrays.asList(labels).contains(label), "expected label " + label + " in " + Arrays.toString(labels));
    }

    /**
     * Listener that records every callback whose input tags contain
     * {@code labelFilter}. Filtering is essential because the test body
     * itself runs instrumented and would otherwise flood the recorder.
     */
    static final class RecordingListener implements SymbolicExecutionListener {
        final List<Event> events = Collections.synchronizedList(new ArrayList<>());
        final String labelFilter;

        RecordingListener(String labelFilter) {
            this.labelFilter = labelFilter;
        }

        private boolean tagMatches(Tag t) {
            if (Tag.isEmpty(t)) {
                return false;
            }
            Object[] labels = Tag.getLabels(t);
            if (labels == null) {
                return false;
            }
            for (Object o : labels) {
                if (labelFilter.equals(o)) {
                    return true;
                }
            }
            return false;
        }

        List<Event> matching(String kind) {
            List<Event> out = new ArrayList<>();
            synchronized (events) {
                for (Event e : events) {
                    if (e.kind.equals(kind)) {
                        out.add(e);
                    }
                }
            }
            return out;
        }

        @Override
        public void onIntBranch(int opcode, int value, Tag tag) {
            if (tagMatches(tag)) {
                events.add(Event.branch("intBranch", opcode, value, tag));
            }
        }

        @Override
        public void onIntCmpBranch(int opcode, int value1, int value2, Tag tag1, Tag tag2) {
            if (tagMatches(tag1) || tagMatches(tag2)) {
                events.add(Event.cmpBranch("intCmpBranch", opcode, value1, value2, tag1, tag2));
            }
        }

        @Override
        public void onRefBranch(int opcode, Object value, Tag tag) {
            if (tagMatches(tag)) {
                events.add(Event.refBranch("refBranch", opcode, value, tag));
            }
        }

        @Override
        public void onRefCmpBranch(int opcode, Object value1, Object value2, Tag tag1, Tag tag2) {
            if (tagMatches(tag1) || tagMatches(tag2)) {
                events.add(Event.refCmpBranch("refCmpBranch", opcode, value1, value2, tag1, tag2));
            }
        }

        @Override
        public void onTableSwitch(int opcode, int value, Tag tag, int min, int max) {
            if (tagMatches(tag)) {
                events.add(Event.tableSwitch(opcode, value, tag, min, max));
            }
        }

        @Override
        public void onLookupSwitch(int opcode, int value, Tag tag, int[] keys) {
            if (tagMatches(tag)) {
                events.add(Event.lookupSwitch(opcode, value, tag, keys));
            }
        }

        @Override
        public Tag onFloatArith(int opcode, float v1, float v2, Tag t1, Tag t2) {
            if (tagMatches(t1) || tagMatches(t2)) {
                events.add(Event.floatArith(opcode, v1, v2, t1, t2));
            }
            return Tag.union(t1, t2);
        }

        @Override
        public Tag onFloatUnary(int opcode, float value, Tag tag) {
            if (tagMatches(tag)) {
                events.add(Event.floatUnary(opcode, value, tag));
            }
            return tag;
        }

        @Override
        public Tag onCat1Convert(int opcode, Tag tag) {
            if (tagMatches(tag)) {
                events.add(Event.cat1Convert(opcode, tag));
            }
            return tag;
        }

        @Override
        public void onIinc(int varIndex, int increment, Tag tag) {
            if (tagMatches(tag)) {
                events.add(Event.iinc(varIndex, increment, tag));
            }
        }

        @Override
        public Tag onIntArith(int opcode, int value1, int value2, Tag tag1, Tag tag2) {
            recordArith(opcode, value1, value2, tag1, tag2);
            return Tag.union(tag1, tag2);
        }

        void recordArith(int opcode, int value1, int value2, Tag tag1, Tag tag2) {
            if (tagMatches(tag1) || tagMatches(tag2)) {
                events.add(Event.cmpBranch("intArith", opcode, value1, value2, tag1, tag2));
            }
        }

        @Override
        public Tag onIntUnary(int opcode, int value, Tag tag) {
            recordUnary(opcode, value, tag);
            return tag;
        }

        void recordUnary(int opcode, int value, Tag tag) {
            if (tagMatches(tag)) {
                events.add(Event.branch("intUnary", opcode, value, tag));
            }
        }
    }

    static final class Event {
        final String kind;
        final int opcode;
        final int value1;
        final int value2;
        final float fvalue1;
        final float fvalue2;
        final Object refValue1;
        final Object refValue2;
        final Tag tag1;
        final Tag tag2;
        final int switchMin;
        final int switchMax;
        final int[] switchKeys;
        final int iincIncrement;

        private Event(
                String kind,
                int opcode,
                int value1,
                int value2,
                float fvalue1,
                float fvalue2,
                Object refValue1,
                Object refValue2,
                Tag tag1,
                Tag tag2,
                int switchMin,
                int switchMax,
                int[] switchKeys,
                int iincIncrement) {
            this.kind = kind;
            this.opcode = opcode;
            this.value1 = value1;
            this.value2 = value2;
            this.fvalue1 = fvalue1;
            this.fvalue2 = fvalue2;
            this.refValue1 = refValue1;
            this.refValue2 = refValue2;
            this.tag1 = tag1;
            this.tag2 = tag2;
            this.switchMin = switchMin;
            this.switchMax = switchMax;
            this.switchKeys = switchKeys;
            this.iincIncrement = iincIncrement;
        }

        static Event branch(String kind, int opcode, int value, Tag tag) {
            return new Event(kind, opcode, value, 0, 0, 0, null, null, tag, null, 0, 0, null, 0);
        }

        static Event cmpBranch(String kind, int opcode, int v1, int v2, Tag t1, Tag t2) {
            return new Event(kind, opcode, v1, v2, 0, 0, null, null, t1, t2, 0, 0, null, 0);
        }

        static Event refBranch(String kind, int opcode, Object v, Tag t) {
            return new Event(kind, opcode, 0, 0, 0, 0, v, null, t, null, 0, 0, null, 0);
        }

        static Event refCmpBranch(String kind, int opcode, Object v1, Object v2, Tag t1, Tag t2) {
            return new Event(kind, opcode, 0, 0, 0, 0, v1, v2, t1, t2, 0, 0, null, 0);
        }

        static Event tableSwitch(int opcode, int v, Tag t, int min, int max) {
            return new Event("tableSwitch", opcode, v, 0, 0, 0, null, null, t, null, min, max, null, 0);
        }

        static Event lookupSwitch(int opcode, int v, Tag t, int[] keys) {
            return new Event("lookupSwitch", opcode, v, 0, 0, 0, null, null, t, null, 0, 0, keys, 0);
        }

        static Event iinc(int varIndex, int increment, Tag t) {
            return new Event("iinc", 0, varIndex, 0, 0, 0, null, null, t, null, 0, 0, null, increment);
        }

        static Event floatArith(int opcode, float v1, float v2, Tag t1, Tag t2) {
            return new Event("floatArith", opcode, 0, 0, v1, v2, null, null, t1, t2, 0, 0, null, 0);
        }

        static Event floatUnary(int opcode, float value, Tag tag) {
            return new Event("floatUnary", opcode, 0, 0, value, 0, null, null, tag, null, 0, 0, null, 0);
        }

        static Event cat1Convert(int opcode, Tag tag) {
            return new Event("cat1Convert", opcode, 0, 0, 0, 0, null, null, tag, null, 0, 0, null, 0);
        }

        @Override
        public String toString() {
            return "Event{" + kind + ",op=" + opcode + ",v1=" + value1 + ",v2=" + value2 + "}";
        }
    }
}
