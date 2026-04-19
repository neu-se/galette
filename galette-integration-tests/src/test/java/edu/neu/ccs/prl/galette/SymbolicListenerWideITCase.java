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
 * Integration tests for the cat-2 (long/double), size-changing conversion,
 * and array-load/store {@link SymbolicExecutionListener} hooks.
 *
 * <p>Like {@link SymbolicListenerITCase}, the test bodies run instrumented,
 * so every tagged operation in setup/assertions fires hooks. The recording
 * listener therefore filters events by a unique label on at least one input
 * tag, and each test further filters on opcode to avoid counting noise from
 * unrelated events.
 */
public class SymbolicListenerWideITCase {
    private static final String LBL = "WIDE_LBL";
    private static final String LBL2 = "WIDE_LBL2";

    private RecordingListener listener;

    @BeforeEach
    void installListener() {
        listener = new RecordingListener(LBL);
        SymbolicListener.setListener(listener);
    }

    @AfterEach
    void uninstallListener() {
        SymbolicListener.setListener(null);
        listener = null;
    }

    // ---------- onLongArith ----------

    @Test
    void longArithFiresForLaddWithValuesAndTags() {
        long a = Tainter.setTag(100L, Tag.of(LBL));
        long b = Tainter.setTag(42L, Tag.of(LBL2));
        long c = a + b;
        if (c != 142L) {
            Assertions.fail("expected 142, got " + c);
        }
        List<Event> matched = filterByOpcode(listener.matching("longArith"), LADD);
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertEquals(100L, e.lvalue1);
        Assertions.assertEquals(42L, e.lvalue2);
        assertTagHasLabel(e.tag1, LBL);
        assertTagHasLabel(e.tag2, LBL2);
    }

    @Test
    void longArithDoesNotFireWhenBothUntagged() {
        long a = 100L;
        long b = 42L;
        long c = a + b;
        if (c != 142L) {
            Assertions.fail("expected 142, got " + c);
        }
        Assertions.assertEquals(0, listener.matching("longArith").size());
    }

    @Test
    void longArithReturnedTagBecomesResultTag() {
        long a = Tainter.setTag(3L, Tag.of(LBL));
        long b = Tainter.setTag(4L, Tag.of(LBL2));
        final Tag override = Tag.of("LRESULT");
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public Tag onLongArith(int opcode, long v1, long v2, Tag t1, Tag t2) {
                return override;
            }
        });
        long c = a + b;
        if (c != 7L) {
            Assertions.fail("expected 7, got " + c);
        }
        assertTagHasLabel(Tainter.getTag(c), "LRESULT");
    }

    @Test
    void longArithCoversAllLongBinaryOpcodes() {
        long a = Tainter.setTag(12L, Tag.of(LBL));
        long b = Tainter.setTag(3L, Tag.of(LBL));
        long sink = 0L;
        sink ^= a + b; // LADD
        sink ^= a - b; // LSUB
        sink ^= a * b; // LMUL
        sink ^= a / b; // LDIV
        sink ^= a % b; // LREM
        sink ^= a & b; // LAND
        sink ^= a | b; // LOR
        sink ^= a ^ b; // LXOR
        if (sink == Long.MIN_VALUE + 1) {
            Assertions.fail("sink should not equal sentinel");
        }
        int[] expected = {LADD, LSUB, LMUL, LDIV, LREM, LAND, LOR, LXOR};
        boolean[] seen = new boolean[expected.length];
        for (Event e : listener.matching("longArith")) {
            for (int i = 0; i < expected.length; i++) {
                if (e.opcode == expected[i]) {
                    seen[i] = true;
                }
            }
        }
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertTrue(seen[i], "missing long-arith opcode " + expected[i]);
        }
    }

    // ---------- onLongShift ----------

    @Test
    void longShiftFiresForLshlWithLongAndIntSignature() {
        long a = Tainter.setTag(1L, Tag.of(LBL));
        int s = Tainter.setTag(3, Tag.of(LBL2));
        long c = a << s;
        if (c != 8L) {
            Assertions.fail("expected 8, got " + c);
        }
        List<Event> matched = filterByOpcode(listener.matching("longShift"), LSHL);
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertEquals(1L, e.lvalue1);
        Assertions.assertEquals(3, e.value2);
        assertTagHasLabel(e.tag1, LBL);
        assertTagHasLabel(e.tag2, LBL2);
    }

    @Test
    void longShiftCoversAllShiftOpcodes() {
        long a = Tainter.setTag(0x7F00F0F0L, Tag.of(LBL));
        int s = 2;
        long sink = 0L;
        sink ^= a << s; // LSHL
        sink ^= a >> s; // LSHR
        sink ^= a >>> s; // LUSHR
        if (sink == Long.MIN_VALUE + 1) {
            Assertions.fail("sink");
        }
        int[] expected = {LSHL, LSHR, LUSHR};
        boolean[] seen = new boolean[expected.length];
        for (Event e : listener.matching("longShift")) {
            for (int i = 0; i < expected.length; i++) {
                if (e.opcode == expected[i]) {
                    seen[i] = true;
                }
            }
        }
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertTrue(seen[i], "missing long-shift opcode " + expected[i]);
        }
    }

    // ---------- onLongCmp ----------

    @Test
    void longCmpFiresForLessEqualGreater() {
        long a = Tainter.setTag(1L, Tag.of(LBL));
        long b = Tainter.setTag(2L, Tag.of(LBL2));
        // LCMP is emitted by javac when comparing longs in a branch.
        boolean lt = a < b;
        boolean eq = a == b;
        boolean gt = a > b;
        if (!lt || eq || gt) {
            Assertions.fail("lt=" + lt + " eq=" + eq + " gt=" + gt);
        }
        // Also exercise a == b' for the equal case:
        long bp = Tainter.setTag(1L, Tag.of(LBL2));
        boolean eq2 = a == bp;
        if (!eq2) {
            Assertions.fail("eq2 expected true");
        }
        // And a > c for the greater case:
        long c = Tainter.setTag(0L, Tag.of(LBL2));
        boolean gt2 = a > c;
        if (!gt2) {
            Assertions.fail("gt2 expected true");
        }
        List<Event> matched = listener.matching("longCmp");
        // At least three LCMP events — one per comparison pair that produced
        // an event (lt, eq2, gt2). eq above used a == b (untagged-match case
        // is skipped since both are tagged and the event should fire).
        Assertions.assertTrue(matched.size() >= 3, "expected >=3 longCmp events, got " + matched.size());
        // Verify all LCMP opcode:
        for (Event e : matched) {
            Assertions.assertEquals(LCMP, e.opcode);
        }
    }

    // ---------- onLongUnary ----------

    @Test
    void longUnaryFiresForLneg() {
        long a = Tainter.setTag(17L, Tag.of(LBL));
        long c = -a;
        if (c != -17L) {
            Assertions.fail("expected -17, got " + c);
        }
        List<Event> matched = listener.matching("longUnary");
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertEquals(LNEG, e.opcode);
        Assertions.assertEquals(17L, e.lvalue1);
        assertTagHasLabel(e.tag1, LBL);
    }

    @Test
    void longUnaryDoesNotFireWhenUntagged() {
        long a = 17L;
        long c = -a;
        if (c != -17L) {
            Assertions.fail("expected -17, got " + c);
        }
        Assertions.assertEquals(0, listener.matching("longUnary").size());
    }

    // ---------- onDoubleArith ----------

    @Test
    void doubleArithFiresForDaddWithValuesAndTags() {
        double a = Tainter.setTag(1.5, Tag.of(LBL));
        double b = Tainter.setTag(2.25, Tag.of(LBL2));
        double c = a + b;
        if (c != 3.75) {
            Assertions.fail("expected 3.75, got " + c);
        }
        List<Event> matched = filterByOpcode(listener.matching("doubleArith"), DADD);
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertEquals(1.5, e.dvalue1);
        Assertions.assertEquals(2.25, e.dvalue2);
        assertTagHasLabel(e.tag1, LBL);
        assertTagHasLabel(e.tag2, LBL2);
    }

    @Test
    void doubleArithDoesNotFireWhenBothUntagged() {
        double a = 1.5;
        double b = 2.25;
        double c = a + b;
        if (c != 3.75) {
            Assertions.fail("expected 3.75, got " + c);
        }
        Assertions.assertEquals(0, listener.matching("doubleArith").size());
    }

    @Test
    void doubleArithReturnedTagBecomesResultTag() {
        double a = Tainter.setTag(4.0, Tag.of(LBL));
        double b = Tainter.setTag(5.0, Tag.of(LBL2));
        final Tag override = Tag.of("DRESULT");
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public Tag onDoubleArith(int opcode, double v1, double v2, Tag t1, Tag t2) {
                return override;
            }
        });
        double c = a + b;
        if (c != 9.0) {
            Assertions.fail("expected 9.0, got " + c);
        }
        assertTagHasLabel(Tainter.getTag(c), "DRESULT");
    }

    @Test
    void doubleArithCoversAllDoubleBinaryOpcodes() {
        double a = Tainter.setTag(6.0, Tag.of(LBL));
        double b = Tainter.setTag(3.0, Tag.of(LBL));
        double sink = 0.0;
        sink += a + b; // DADD
        sink += a - b; // DSUB
        sink += a * b; // DMUL
        sink += a / b; // DDIV
        sink += a % b; // DREM
        if (sink == Double.MIN_VALUE) {
            Assertions.fail("sink");
        }
        int[] expected = {DADD, DSUB, DMUL, DDIV, DREM};
        boolean[] seen = new boolean[expected.length];
        for (Event e : listener.matching("doubleArith")) {
            for (int i = 0; i < expected.length; i++) {
                if (e.opcode == expected[i]) {
                    seen[i] = true;
                }
            }
        }
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertTrue(seen[i], "missing double-arith opcode " + expected[i]);
        }
    }

    // ---------- onDoubleCmp ----------

    @Test
    void doubleCmpFiresForDcmplAndDcmpg() {
        double a = Tainter.setTag(1.0, Tag.of(LBL));
        double b = Tainter.setTag(2.0, Tag.of(LBL2));
        // javac emits DCMPG for < and <=; DCMPL for > and >=.
        if (a < b) {
            /* DCMPG branch-if-ge-zero */
        }
        if (a > b) {
            Assertions.fail("unreachable");
        }
        List<Event> matched = listener.matching("doubleCmp");
        boolean sawDcmpl = false, sawDcmpg = false;
        for (Event e : matched) {
            if (e.opcode == DCMPL) sawDcmpl = true;
            if (e.opcode == DCMPG) sawDcmpg = true;
        }
        Assertions.assertTrue(sawDcmpl, "missing DCMPL");
        Assertions.assertTrue(sawDcmpg, "missing DCMPG");
    }

    // ---------- onDoubleUnary ----------

    @Test
    void doubleUnaryFiresForDneg() {
        double a = Tainter.setTag(3.5, Tag.of(LBL));
        double c = -a;
        if (c != -3.5) {
            Assertions.fail("expected -3.5, got " + c);
        }
        List<Event> matched = listener.matching("doubleUnary");
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertEquals(DNEG, e.opcode);
        Assertions.assertEquals(3.5, e.dvalue1);
        assertTagHasLabel(e.tag1, LBL);
    }

    @Test
    void doubleUnaryDoesNotFireWhenUntagged() {
        double a = 3.5;
        double c = -a;
        if (c != -3.5) {
            Assertions.fail("expected -3.5, got " + c);
        }
        Assertions.assertEquals(0, listener.matching("doubleUnary").size());
    }

    // ---------- onIntWiden ----------

    @Test
    void intWidenFiresForI2LWithValueAndOverrideTagFlows() {
        final Tag override = Tag.of("I2L_RESULT");
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public Tag onIntWiden(int opcode, int value, Tag tag) {
                return override;
            }
        });
        int i = Tainter.setTag(7, Tag.of(LBL));
        long l = (long) i;
        if (l != 7L) {
            Assertions.fail("expected 7, got " + l);
        }
        assertTagHasLabel(Tainter.getTag(l), "I2L_RESULT");
    }

    @Test
    void intWidenFiresForI2DWithValueAndOverrideTagFlows() {
        final Tag override = Tag.of("I2D_RESULT");
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public Tag onIntWiden(int opcode, int value, Tag tag) {
                return override;
            }
        });
        int i = Tainter.setTag(11, Tag.of(LBL));
        double d = (double) i;
        if (d != 11.0) {
            Assertions.fail("expected 11.0, got " + d);
        }
        assertTagHasLabel(Tainter.getTag(d), "I2D_RESULT");
    }

    @Test
    void intWidenRecordsBothOpcodes() {
        int i = Tainter.setTag(9, Tag.of(LBL));
        long l = (long) i;
        double d = (double) i;
        if (l != 9L || d != 9.0) {
            Assertions.fail("conversion results off: l=" + l + " d=" + d);
        }
        boolean sawI2L = false, sawI2D = false;
        for (Event e : listener.matching("intWiden")) {
            if (e.opcode == I2L && e.value1 == 9) sawI2L = true;
            if (e.opcode == I2D && e.value1 == 9) sawI2D = true;
        }
        Assertions.assertTrue(sawI2L, "missing I2L");
        Assertions.assertTrue(sawI2D, "missing I2D");
    }

    // ---------- onFloatWiden ----------

    @Test
    void floatWidenFiresForF2LWithOverrideTag() {
        final Tag override = Tag.of("F2L_RESULT");
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public Tag onFloatWiden(int opcode, float value, Tag tag) {
                return override;
            }
        });
        float f = Tainter.setTag(4.0f, Tag.of(LBL));
        long l = (long) f;
        if (l != 4L) {
            Assertions.fail("expected 4, got " + l);
        }
        assertTagHasLabel(Tainter.getTag(l), "F2L_RESULT");
    }

    @Test
    void floatWidenFiresForF2DWithOverrideTag() {
        final Tag override = Tag.of("F2D_RESULT");
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public Tag onFloatWiden(int opcode, float value, Tag tag) {
                return override;
            }
        });
        float f = Tainter.setTag(5.5f, Tag.of(LBL));
        double d = (double) f;
        if (d != 5.5) {
            Assertions.fail("expected 5.5, got " + d);
        }
        assertTagHasLabel(Tainter.getTag(d), "F2D_RESULT");
    }

    @Test
    void floatWidenRecordsBothOpcodes() {
        float f = Tainter.setTag(6.0f, Tag.of(LBL));
        long l = (long) f;
        double d = (double) f;
        if (l != 6L || d != 6.0) {
            Assertions.fail("results off l=" + l + " d=" + d);
        }
        boolean sawF2L = false, sawF2D = false;
        for (Event e : listener.matching("floatWiden")) {
            if (e.opcode == F2L && e.fvalue1 == 6.0f) sawF2L = true;
            if (e.opcode == F2D && e.fvalue1 == 6.0f) sawF2D = true;
        }
        Assertions.assertTrue(sawF2L, "missing F2L");
        Assertions.assertTrue(sawF2D, "missing F2D");
    }

    // ---------- onLongConvert ----------

    @Test
    void longConvertCoversL2I_L2F_L2D() {
        long l = Tainter.setTag(13L, Tag.of(LBL));
        int i = (int) l;
        float f = (float) l;
        double d = (double) l;
        if (i != 13 || f != 13.0f || d != 13.0) {
            Assertions.fail("results off i=" + i + " f=" + f + " d=" + d);
        }
        boolean sawL2I = false, sawL2F = false, sawL2D = false;
        for (Event e : listener.matching("longConvert")) {
            if (e.opcode == L2I && e.lvalue1 == 13L) sawL2I = true;
            if (e.opcode == L2F && e.lvalue1 == 13L) sawL2F = true;
            if (e.opcode == L2D && e.lvalue1 == 13L) sawL2D = true;
        }
        Assertions.assertTrue(sawL2I, "missing L2I");
        Assertions.assertTrue(sawL2F, "missing L2F");
        Assertions.assertTrue(sawL2D, "missing L2D");
    }

    @Test
    void longConvertReturnedTagFlows() {
        final Tag override = Tag.of("L2I_RESULT");
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public Tag onLongConvert(int opcode, long value, Tag tag) {
                return override;
            }
        });
        long l = Tainter.setTag(25L, Tag.of(LBL));
        int i = (int) l;
        if (i != 25) {
            Assertions.fail("expected 25, got " + i);
        }
        assertTagHasLabel(Tainter.getTag(i), "L2I_RESULT");
    }

    // ---------- onDoubleConvert ----------

    @Test
    void doubleConvertCoversD2I_D2F_D2L() {
        double d = Tainter.setTag(21.0, Tag.of(LBL));
        int i = (int) d;
        float f = (float) d;
        long l = (long) d;
        if (i != 21 || f != 21.0f || l != 21L) {
            Assertions.fail("results off i=" + i + " f=" + f + " l=" + l);
        }
        boolean sawD2I = false, sawD2F = false, sawD2L = false;
        for (Event e : listener.matching("doubleConvert")) {
            if (e.opcode == D2I && e.dvalue1 == 21.0) sawD2I = true;
            if (e.opcode == D2F && e.dvalue1 == 21.0) sawD2F = true;
            if (e.opcode == D2L && e.dvalue1 == 21.0) sawD2L = true;
        }
        Assertions.assertTrue(sawD2I, "missing D2I");
        Assertions.assertTrue(sawD2F, "missing D2F");
        Assertions.assertTrue(sawD2L, "missing D2L");
    }

    @Test
    void doubleConvertReturnedTagFlows() {
        final Tag override = Tag.of("D2L_RESULT");
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public Tag onDoubleConvert(int opcode, double value, Tag tag) {
                return override;
            }
        });
        double d = Tainter.setTag(8.0, Tag.of(LBL));
        long l = (long) d;
        if (l != 8L) {
            Assertions.fail("expected 8, got " + l);
        }
        assertTagHasLabel(Tainter.getTag(l), "D2L_RESULT");
    }

    // ---------- onArrayLoad ----------

    @Test
    void arrayLoadFiresForIaloadWithArrayTag() {
        int[] arr = Tainter.setTag(new int[] {10, 20, 30}, Tag.of(LBL));
        int v = arr[1];
        if (v != 20) {
            Assertions.fail("expected 20, got " + v);
        }
        List<Event> matched = filterByOpcode(listener.matching("arrayLoad"), IALOAD);
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertSame(arr, e.refValue1);
        Assertions.assertEquals(1, e.value1);
        assertTagHasLabel(e.tag1, LBL);
    }

    @Test
    void arrayLoadFiresWhenOnlyIndexIsTagged() {
        int[] arr = new int[] {10, 20, 30};
        int idx = Tainter.setTag(2, Tag.of(LBL));
        int v = arr[idx];
        if (v != 30) {
            Assertions.fail("expected 30, got " + v);
        }
        List<Event> matched = filterByOpcode(listener.matching("arrayLoad"), IALOAD);
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertSame(arr, e.refValue1);
        Assertions.assertEquals(2, e.value1);
        assertTagHasLabel(e.tag2, LBL);
    }

    @Test
    void arrayLoadDoesNotFireWhenAllThreeEmpty() {
        int[] arr = new int[] {10, 20, 30};
        int v = arr[1];
        if (v != 20) {
            Assertions.fail("expected 20, got " + v);
        }
        // Nothing in play is tagged with our label, so there can be no event
        // whose tag set contains LBL.
        Assertions.assertEquals(0, listener.matching("arrayLoad").size());
    }

    @Test
    void arrayLoadReturnedTagBecomesElementTag() {
        final Tag override = Tag.of("ELEM_RESULT");
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public Tag onArrayLoad(int opcode, Object array, int index, Tag aT, Tag iT, Tag eT) {
                return override;
            }
        });
        int[] arr = Tainter.setTag(new int[] {10, 20, 30}, Tag.of(LBL));
        int v = arr[1];
        if (v != 20) {
            Assertions.fail("expected 20, got " + v);
        }
        assertTagHasLabel(Tainter.getTag(v), "ELEM_RESULT");
    }

    @Test
    void arrayLoadCoversAllEightOpcodes() {
        // One tagged array per element type. Note: Tainter.setTag must be
        // assigned back — the returned reference carries the shadow tag.
        int[] ia = Tainter.setTag(new int[] {1, 2}, Tag.of(LBL));
        long[] la = Tainter.setTag(new long[] {1L, 2L}, Tag.of(LBL));
        float[] fa = Tainter.setTag(new float[] {1f, 2f}, Tag.of(LBL));
        double[] da = Tainter.setTag(new double[] {1.0, 2.0}, Tag.of(LBL));
        Object[] aa = Tainter.setTag(new Object[] {new Object(), new Object()}, Tag.of(LBL));
        byte[] ba = Tainter.setTag(new byte[] {1, 2}, Tag.of(LBL));
        char[] ca = Tainter.setTag(new char[] {'a', 'b'}, Tag.of(LBL));
        short[] sa = Tainter.setTag(new short[] {1, 2}, Tag.of(LBL));

        int sinkI = ia[0]; // IALOAD
        long sinkL = la[0]; // LALOAD
        float sinkF = fa[0]; // FALOAD
        double sinkD = da[0]; // DALOAD
        Object sinkA = aa[0]; // AALOAD
        byte sinkB = ba[0]; // BALOAD
        char sinkC = ca[0]; // CALOAD
        short sinkS = sa[0]; // SALOAD
        if (sinkI + sinkL + (long) sinkF + (long) sinkD + sinkB + sinkC + sinkS == Long.MIN_VALUE && sinkA == null) {
            Assertions.fail("sink");
        }
        int[] expected = {IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD, SALOAD};
        boolean[] seen = new boolean[expected.length];
        for (Event e : listener.matching("arrayLoad")) {
            for (int i = 0; i < expected.length; i++) {
                if (e.opcode == expected[i]) {
                    seen[i] = true;
                }
            }
        }
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertTrue(seen[i], "missing arrayLoad opcode " + expected[i]);
        }
    }

    // ---------- onArrayStore ----------

    @Test
    void arrayStoreFiresForIastoreWithArrayTag() {
        int[] arr = Tainter.setTag(new int[] {0, 0, 0}, Tag.of(LBL));
        arr[1] = 99;
        List<Event> matched = filterByOpcode(listener.matching("arrayStore"), IASTORE);
        Assertions.assertEquals(1, matched.size());
        Event e = matched.get(0);
        Assertions.assertSame(arr, e.refValue1);
        Assertions.assertEquals(1, e.value1);
        assertTagHasLabel(e.tag1, LBL);
    }

    @Test
    void arrayStoreReturnedTagIsMirroredIntoSlot() {
        final Tag override = Tag.of("STORED_RESULT");
        // Listener overrides store tag; default load just returns element tag.
        SymbolicListener.setListener(new SymbolicExecutionListener() {
            @Override
            public Tag onArrayStore(int opcode, Object array, int index, Tag aT, Tag iT, Tag vT) {
                return override;
            }
        });
        int[] arr = new int[] {0, 0, 0};
        int v = Tainter.setTag(99, Tag.of(LBL));
        arr[1] = v;
        // Now a later load should surface the overridden tag. We fetch the
        // element's tag directly from the shadow store, bypassing any load
        // hook by using Tainter.getTag on the loaded value. The load hook
        // runs but its default behavior returns elemTag unchanged.
        SymbolicListener.setListener(null); // don't let load hook interfere
        int loaded = arr[1];
        if (loaded != 99) {
            Assertions.fail("expected 99, got " + loaded);
        }
        assertTagHasLabel(Tainter.getTag(loaded), "STORED_RESULT");
    }

    @Test
    void arrayStoreCoversAllEightOpcodes() {
        int[] ia = Tainter.setTag(new int[] {0, 0}, Tag.of(LBL));
        long[] la = Tainter.setTag(new long[] {0L, 0L}, Tag.of(LBL));
        float[] fa = Tainter.setTag(new float[] {0f, 0f}, Tag.of(LBL));
        double[] da = Tainter.setTag(new double[] {0.0, 0.0}, Tag.of(LBL));
        Object[] aa = Tainter.setTag(new Object[] {null, null}, Tag.of(LBL));
        byte[] ba = Tainter.setTag(new byte[] {0, 0}, Tag.of(LBL));
        char[] ca = Tainter.setTag(new char[] {'\0', '\0'}, Tag.of(LBL));
        short[] sa = Tainter.setTag(new short[] {0, 0}, Tag.of(LBL));

        ia[0] = 1; // IASTORE
        la[0] = 1L; // LASTORE
        fa[0] = 1f; // FASTORE
        da[0] = 1.0; // DASTORE
        aa[0] = new Object(); // AASTORE
        ba[0] = 1; // BASTORE
        ca[0] = 'x'; // CASTORE
        sa[0] = 1; // SASTORE

        int[] expected = {IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE, CASTORE, SASTORE};
        boolean[] seen = new boolean[expected.length];
        for (Event e : listener.matching("arrayStore")) {
            for (int i = 0; i < expected.length; i++) {
                if (e.opcode == expected[i]) {
                    seen[i] = true;
                }
            }
        }
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertTrue(seen[i], "missing arrayStore opcode " + expected[i]);
        }
    }

    // ---------- Helpers ----------

    private static List<Event> filterByOpcode(List<Event> in, int opcode) {
        List<Event> out = new ArrayList<>();
        for (Event e : in) {
            if (e.opcode == opcode) {
                out.add(e);
            }
        }
        return out;
    }

    private static void assertTagHasLabel(Tag tag, String label) {
        Object[] labels = Tag.getLabels(tag);
        Assertions.assertNotNull(labels, "expected non-null labels for tag " + tag);
        Assertions.assertTrue(
                Arrays.asList(labels).contains(label), "expected label " + label + " in " + Arrays.toString(labels));
    }

    /**
     * Records every callback whose input tags contain {@code labelFilter}.
     * Filtering keeps noise from the instrumented test body out of the
     * event stream.
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
        public Tag onLongArith(int opcode, long v1, long v2, Tag t1, Tag t2) {
            if (tagMatches(t1) || tagMatches(t2)) {
                events.add(Event.longArith("longArith", opcode, v1, v2, t1, t2));
            }
            return Tag.union(t1, t2);
        }

        @Override
        public Tag onLongShift(int opcode, long v1, int v2, Tag t1, Tag t2) {
            if (tagMatches(t1) || tagMatches(t2)) {
                events.add(Event.longShift(opcode, v1, v2, t1, t2));
            }
            return Tag.union(t1, t2);
        }

        @Override
        public Tag onLongCmp(long v1, long v2, Tag t1, Tag t2) {
            if (tagMatches(t1) || tagMatches(t2)) {
                events.add(Event.longArith("longCmp", LCMP, v1, v2, t1, t2));
            }
            return Tag.union(t1, t2);
        }

        @Override
        public Tag onLongUnary(int opcode, long value, Tag tag) {
            if (tagMatches(tag)) {
                events.add(Event.longUnary("longUnary", opcode, value, tag));
            }
            return tag;
        }

        @Override
        public Tag onDoubleArith(int opcode, double v1, double v2, Tag t1, Tag t2) {
            if (tagMatches(t1) || tagMatches(t2)) {
                events.add(Event.doubleArith("doubleArith", opcode, v1, v2, t1, t2));
            }
            return Tag.union(t1, t2);
        }

        @Override
        public Tag onDoubleCmp(int opcode, double v1, double v2, Tag t1, Tag t2) {
            if (tagMatches(t1) || tagMatches(t2)) {
                events.add(Event.doubleArith("doubleCmp", opcode, v1, v2, t1, t2));
            }
            return Tag.union(t1, t2);
        }

        @Override
        public Tag onDoubleUnary(int opcode, double value, Tag tag) {
            if (tagMatches(tag)) {
                events.add(Event.doubleUnary("doubleUnary", opcode, value, tag));
            }
            return tag;
        }

        @Override
        public Tag onIntWiden(int opcode, int value, Tag tag) {
            if (tagMatches(tag)) {
                events.add(Event.intWiden(opcode, value, tag));
            }
            return tag;
        }

        @Override
        public Tag onFloatWiden(int opcode, float value, Tag tag) {
            if (tagMatches(tag)) {
                events.add(Event.floatWiden(opcode, value, tag));
            }
            return tag;
        }

        @Override
        public Tag onLongConvert(int opcode, long value, Tag tag) {
            if (tagMatches(tag)) {
                events.add(Event.longUnary("longConvert", opcode, value, tag));
            }
            return tag;
        }

        @Override
        public Tag onDoubleConvert(int opcode, double value, Tag tag) {
            if (tagMatches(tag)) {
                events.add(Event.doubleUnary("doubleConvert", opcode, value, tag));
            }
            return tag;
        }

        @Override
        public Tag onArrayLoad(int opcode, Object array, int index, Tag aT, Tag iT, Tag eT) {
            if (tagMatches(aT) || tagMatches(iT) || tagMatches(eT)) {
                events.add(Event.arrayOp("arrayLoad", opcode, array, index, aT, iT, eT));
            }
            return eT;
        }

        @Override
        public Tag onArrayStore(int opcode, Object array, int index, Tag aT, Tag iT, Tag vT) {
            if (tagMatches(aT) || tagMatches(iT) || tagMatches(vT)) {
                events.add(Event.arrayOp("arrayStore", opcode, array, index, aT, iT, vT));
            }
            return vT;
        }
    }

    static final class Event {
        final String kind;
        final int opcode;
        final int value1;
        final int value2;
        final long lvalue1;
        final long lvalue2;
        final float fvalue1;
        final double dvalue1;
        final double dvalue2;
        final Object refValue1;
        final Tag tag1;
        final Tag tag2;
        final Tag tag3;

        private Event(
                String kind,
                int opcode,
                int value1,
                int value2,
                long lvalue1,
                long lvalue2,
                float fvalue1,
                double dvalue1,
                double dvalue2,
                Object refValue1,
                Tag tag1,
                Tag tag2,
                Tag tag3) {
            this.kind = kind;
            this.opcode = opcode;
            this.value1 = value1;
            this.value2 = value2;
            this.lvalue1 = lvalue1;
            this.lvalue2 = lvalue2;
            this.fvalue1 = fvalue1;
            this.dvalue1 = dvalue1;
            this.dvalue2 = dvalue2;
            this.refValue1 = refValue1;
            this.tag1 = tag1;
            this.tag2 = tag2;
            this.tag3 = tag3;
        }

        static Event longArith(String kind, int opcode, long v1, long v2, Tag t1, Tag t2) {
            return new Event(kind, opcode, 0, 0, v1, v2, 0, 0, 0, null, t1, t2, null);
        }

        static Event longShift(int opcode, long v1, int v2, Tag t1, Tag t2) {
            return new Event("longShift", opcode, 0, v2, v1, 0, 0, 0, 0, null, t1, t2, null);
        }

        static Event longUnary(String kind, int opcode, long v, Tag t) {
            return new Event(kind, opcode, 0, 0, v, 0, 0, 0, 0, null, t, null, null);
        }

        static Event doubleArith(String kind, int opcode, double v1, double v2, Tag t1, Tag t2) {
            return new Event(kind, opcode, 0, 0, 0, 0, 0, v1, v2, null, t1, t2, null);
        }

        static Event doubleUnary(String kind, int opcode, double v, Tag t) {
            return new Event(kind, opcode, 0, 0, 0, 0, 0, v, 0, null, t, null, null);
        }

        static Event intWiden(int opcode, int v, Tag t) {
            return new Event("intWiden", opcode, v, 0, 0, 0, 0, 0, 0, null, t, null, null);
        }

        static Event floatWiden(int opcode, float v, Tag t) {
            return new Event("floatWiden", opcode, 0, 0, 0, 0, v, 0, 0, null, t, null, null);
        }

        static Event arrayOp(String kind, int opcode, Object array, int index, Tag aT, Tag iT, Tag eT) {
            return new Event(kind, opcode, index, 0, 0, 0, 0, 0, 0, array, aT, iT, eT);
        }

        @Override
        public String toString() {
            return "Event{" + kind + ",op=" + opcode + "}";
        }
    }
}
