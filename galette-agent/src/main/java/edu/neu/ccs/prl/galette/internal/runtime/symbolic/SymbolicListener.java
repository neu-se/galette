package edu.neu.ccs.prl.galette.internal.runtime.symbolic;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;

/**
 * Holder for the currently-installed {@link SymbolicExecutionListener} and
 * target of the static callbacks emitted by Galette's instrumentation.
 *
 * <p>The static {@code on*} methods are the ones invoked from instrumented
 * bytecode via {@link Handle}. They bounce to the installed listener; if no
 * listener is installed, or if every provided tag is empty, the call is a
 * no-op. Exceptions thrown by listener implementations are swallowed to
 * keep instrumented code running.
 */
public final class SymbolicListener {
    private static volatile SymbolicExecutionListener listener = null;

    private SymbolicListener() {
        throw new AssertionError();
    }

    public static void setListener(SymbolicExecutionListener l) {
        listener = l;
    }

    public static SymbolicExecutionListener getListener() {
        return listener;
    }

    // ---------- Branches ----------

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_INT_BRANCH)
    public static void onIntBranch(int opcode, int value, Tag tag) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                l.onIntBranch(opcode, value, tag);
            } catch (Throwable ignored) {
            }
        }
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_INT_CMP_BRANCH)
    public static void onIntCmpBranch(int opcode, int value1, int value2, Tag tag1, Tag tag2) {
        SymbolicExecutionListener l = listener;
        if (l != null && (!Tag.isEmpty(tag1) || !Tag.isEmpty(tag2))) {
            try {
                l.onIntCmpBranch(opcode, value1, value2, tag1, tag2);
            } catch (Throwable ignored) {
            }
        }
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_REF_BRANCH)
    public static void onRefBranch(int opcode, Object value, Tag tag) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                l.onRefBranch(opcode, value, tag);
            } catch (Throwable ignored) {
            }
        }
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_REF_CMP_BRANCH)
    public static void onRefCmpBranch(int opcode, Object value1, Object value2, Tag tag1, Tag tag2) {
        SymbolicExecutionListener l = listener;
        if (l != null && (!Tag.isEmpty(tag1) || !Tag.isEmpty(tag2))) {
            try {
                l.onRefCmpBranch(opcode, value1, value2, tag1, tag2);
            } catch (Throwable ignored) {
            }
        }
    }

    // ---------- Switches ----------

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_TABLE_SWITCH)
    public static void onTableSwitch(int opcode, int value, Tag tag, int min, int max) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                l.onTableSwitch(opcode, value, tag, min, max);
            } catch (Throwable ignored) {
            }
        }
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_LOOKUP_SWITCH)
    public static void onLookupSwitch(int opcode, int value, Tag tag, int[] keys) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                l.onLookupSwitch(opcode, value, tag, keys);
            } catch (Throwable ignored) {
            }
        }
    }

    // ---------- Iinc ----------

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_IINC)
    public static void onIinc(int varIndex, int increment, Tag tag) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                l.onIinc(varIndex, increment, tag);
            } catch (Throwable ignored) {
            }
        }
    }

    // ---------- Arithmetic ----------

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_INT_ARITH)
    public static Tag onIntArith(int opcode, int value1, int value2, Tag tag1, Tag tag2) {
        SymbolicExecutionListener l = listener;
        boolean tainted = !Tag.isEmpty(tag1) || !Tag.isEmpty(tag2);
        if (l != null && tainted) {
            try {
                return l.onIntArith(opcode, value1, value2, tag1, tag2);
            } catch (Throwable ignored) {
            }
        }
        return tainted ? Tag.union(tag1, tag2) : null;
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_INT_UNARY)
    public static Tag onIntUnary(int opcode, int value, Tag tag) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                return l.onIntUnary(opcode, value, tag);
            } catch (Throwable ignored) {
            }
        }
        return tag;
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_FLOAT_ARITH)
    public static Tag onFloatArith(int opcode, float value1, float value2, Tag tag1, Tag tag2) {
        SymbolicExecutionListener l = listener;
        boolean tainted = !Tag.isEmpty(tag1) || !Tag.isEmpty(tag2);
        if (l != null && tainted) {
            try {
                return l.onFloatArith(opcode, value1, value2, tag1, tag2);
            } catch (Throwable ignored) {
            }
        }
        return tainted ? Tag.union(tag1, tag2) : null;
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_FLOAT_UNARY)
    public static Tag onFloatUnary(int opcode, float value, Tag tag) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                return l.onFloatUnary(opcode, value, tag);
            } catch (Throwable ignored) {
            }
        }
        return tag;
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_CAT1_CONVERT)
    public static Tag onCat1Convert(int opcode, Tag tag) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                return l.onCat1Convert(opcode, tag);
            } catch (Throwable ignored) {
            }
        }
        return tag;
    }

    // ---------- Long ----------

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_LONG_ARITH)
    public static Tag onLongArith(int opcode, long value1, long value2, Tag tag1, Tag tag2) {
        SymbolicExecutionListener l = listener;
        boolean tainted = !Tag.isEmpty(tag1) || !Tag.isEmpty(tag2);
        if (l != null && tainted) {
            try {
                return l.onLongArith(opcode, value1, value2, tag1, tag2);
            } catch (Throwable ignored) {
            }
        }
        return tainted ? Tag.union(tag1, tag2) : null;
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_LONG_SHIFT)
    public static Tag onLongShift(int opcode, long value1, int value2, Tag tag1, Tag tag2) {
        SymbolicExecutionListener l = listener;
        boolean tainted = !Tag.isEmpty(tag1) || !Tag.isEmpty(tag2);
        if (l != null && tainted) {
            try {
                return l.onLongShift(opcode, value1, value2, tag1, tag2);
            } catch (Throwable ignored) {
            }
        }
        return tainted ? Tag.union(tag1, tag2) : null;
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_LONG_CMP)
    public static Tag onLongCmp(long value1, long value2, Tag tag1, Tag tag2) {
        SymbolicExecutionListener l = listener;
        boolean tainted = !Tag.isEmpty(tag1) || !Tag.isEmpty(tag2);
        if (l != null && tainted) {
            try {
                return l.onLongCmp(value1, value2, tag1, tag2);
            } catch (Throwable ignored) {
            }
        }
        return tainted ? Tag.union(tag1, tag2) : null;
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_LONG_UNARY)
    public static Tag onLongUnary(int opcode, long value, Tag tag) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                return l.onLongUnary(opcode, value, tag);
            } catch (Throwable ignored) {
            }
        }
        return tag;
    }

    // ---------- Double ----------

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_DOUBLE_ARITH)
    public static Tag onDoubleArith(int opcode, double value1, double value2, Tag tag1, Tag tag2) {
        SymbolicExecutionListener l = listener;
        boolean tainted = !Tag.isEmpty(tag1) || !Tag.isEmpty(tag2);
        if (l != null && tainted) {
            try {
                return l.onDoubleArith(opcode, value1, value2, tag1, tag2);
            } catch (Throwable ignored) {
            }
        }
        return tainted ? Tag.union(tag1, tag2) : null;
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_DOUBLE_CMP)
    public static Tag onDoubleCmp(int opcode, double value1, double value2, Tag tag1, Tag tag2) {
        SymbolicExecutionListener l = listener;
        boolean tainted = !Tag.isEmpty(tag1) || !Tag.isEmpty(tag2);
        if (l != null && tainted) {
            try {
                return l.onDoubleCmp(opcode, value1, value2, tag1, tag2);
            } catch (Throwable ignored) {
            }
        }
        return tainted ? Tag.union(tag1, tag2) : null;
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_DOUBLE_UNARY)
    public static Tag onDoubleUnary(int opcode, double value, Tag tag) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                return l.onDoubleUnary(opcode, value, tag);
            } catch (Throwable ignored) {
            }
        }
        return tag;
    }

    // ---------- Size-changing conversions ----------

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_INT_WIDEN)
    public static Tag onIntWiden(int opcode, int value, Tag tag) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                return l.onIntWiden(opcode, value, tag);
            } catch (Throwable ignored) {
            }
        }
        return tag;
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_FLOAT_WIDEN)
    public static Tag onFloatWiden(int opcode, float value, Tag tag) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                return l.onFloatWiden(opcode, value, tag);
            } catch (Throwable ignored) {
            }
        }
        return tag;
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_LONG_CONVERT)
    public static Tag onLongConvert(int opcode, long value, Tag tag) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                return l.onLongConvert(opcode, value, tag);
            } catch (Throwable ignored) {
            }
        }
        return tag;
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_DOUBLE_CONVERT)
    public static Tag onDoubleConvert(int opcode, double value, Tag tag) {
        SymbolicExecutionListener l = listener;
        if (l != null && !Tag.isEmpty(tag)) {
            try {
                return l.onDoubleConvert(opcode, value, tag);
            } catch (Throwable ignored) {
            }
        }
        return tag;
    }

    // ---------- Arrays ----------

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_ARRAY_LOAD)
    public static Tag onArrayLoad(int opcode, Object array, int index, Tag arrayTag, Tag indexTag, Tag elemTag) {
        SymbolicExecutionListener l = listener;
        if (l != null && (!Tag.isEmpty(arrayTag) || !Tag.isEmpty(indexTag) || !Tag.isEmpty(elemTag))) {
            try {
                return l.onArrayLoad(opcode, array, index, arrayTag, indexTag, elemTag);
            } catch (Throwable ignored) {
            }
        }
        return elemTag;
    }

    @InvokedViaHandle(handle = Handle.SYMBOLIC_ON_ARRAY_STORE)
    public static Tag onArrayStore(int opcode, Object array, int index, Tag arrayTag, Tag indexTag, Tag valueTag) {
        SymbolicExecutionListener l = listener;
        if (l != null && (!Tag.isEmpty(arrayTag) || !Tag.isEmpty(indexTag) || !Tag.isEmpty(valueTag))) {
            try {
                return l.onArrayStore(opcode, array, index, arrayTag, indexTag, valueTag);
            } catch (Throwable ignored) {
            }
        }
        return valueTag;
    }
}
