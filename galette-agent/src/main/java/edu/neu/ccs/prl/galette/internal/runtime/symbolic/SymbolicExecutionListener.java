package edu.neu.ccs.prl.galette.internal.runtime.symbolic;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;

/**
 * SPI for observing tagged control-flow and data-flow events during an
 * instrumented program's execution. Used by symbolic-execution front-ends
 * (e.g., Knarr) to collect path constraints over tagged values.
 *
 * <p>Implementations are installed via
 * {@link SymbolicListener#setListener(SymbolicExecutionListener)} and must be
 * safe to call from any thread that executes instrumented code. Callbacks
 * fire only when at least one of the relevant tags is non-empty.
 *
 * <p>All callbacks must not throw; thrown exceptions are swallowed by the
 * dispatcher. <em>Important:</em> when a callback that returns a {@link Tag}
 * (e.g., {@link #onIntArith}, {@link #onIntUnary}) throws, the dispatcher
 * falls back to the default tag-propagation behavior — a tag union for
 * binary ops, a pass-through for unary ops. A listener override that throws
 * is therefore silently replaced by the default; if a specific result tag
 * is required, catch and handle errors inside the override.
 *
 * <p>The listener is responsible for computing any derived information
 * (e.g., taken/not-taken at a branch, taken arm at a switch) from the
 * provided runtime values and opcodes. Opcode constants are exposed by
 * {@link SymbolicOpcodes} so consumers do not need to depend on asm.
 *
 * <p>Default implementations are no-ops, or, for binary/unary operations,
 * return a tag that unions the input tags. Implementations override only
 * the callbacks relevant to their analysis.
 */
public interface SymbolicExecutionListener {
    // ---------- Conditional branches ----------

    /**
     * Invoked before a single-operand integer conditional jump
     * ({@code IFEQ}, {@code IFNE}, {@code IFLT}, {@code IFGE}, {@code IFGT},
     * {@code IFLE}).
     */
    default void onIntBranch(int opcode, int value, Tag tag) {}

    /**
     * Invoked before a two-operand integer conditional jump
     * ({@code IF_ICMPEQ}..{@code IF_ICMPLE}).
     */
    default void onIntCmpBranch(int opcode, int value1, int value2, Tag tag1, Tag tag2) {}

    /**
     * Invoked before {@code IFNULL} or {@code IFNONNULL}.
     */
    default void onRefBranch(int opcode, Object value, Tag tag) {}

    /**
     * Invoked before {@code IF_ACMPEQ} or {@code IF_ACMPNE}.
     */
    default void onRefCmpBranch(int opcode, Object value1, Object value2, Tag tag1, Tag tag2) {}

    // ---------- Switches ----------

    /**
     * Invoked before a {@code TABLESWITCH}. {@code min} and {@code max} are
     * the low and high keys of the switch; the fallthrough arm is taken when
     * {@code value < min || value > max}.
     */
    default void onTableSwitch(int opcode, int value, Tag tag, int min, int max) {}

    /**
     * Invoked before a {@code LOOKUPSWITCH}. {@code keys} contains the
     * case keys in ascending order; the default arm is taken when no key
     * matches {@code value}.
     */
    default void onLookupSwitch(int opcode, int value, Tag tag, int[] keys) {}

    // ---------- Local-variable update ----------

    /**
     * Invoked at {@code IINC}. {@code tag} is the tag of the local variable
     * <em>before</em> the increment.
     */
    default void onIinc(int varIndex, int increment, Tag tag) {}

    // ---------- Arithmetic (result tags) ----------

    /**
     * Invoked for integer binary arithmetic that takes and returns int:
     * {@code IADD}, {@code ISUB}, {@code IMUL}, {@code IDIV}, {@code IREM},
     * {@code ISHL}, {@code ISHR}, {@code IUSHR}, {@code IAND}, {@code IOR},
     * {@code IXOR}. Returns the tag to attach to the result.
     */
    default Tag onIntArith(int opcode, int value1, int value2, Tag tag1, Tag tag2) {
        return Tag.union(tag1, tag2);
    }

    /**
     * Invoked for integer unary ops that take and return int-sized:
     * {@code INEG}, {@code I2B}, {@code I2C}, {@code I2S}. Returns the tag
     * to attach to the result.
     */
    default Tag onIntUnary(int opcode, int value, Tag tag) {
        return tag;
    }

    /**
     * Invoked for float binary arithmetic ({@code FADD}, {@code FSUB},
     * {@code FMUL}, {@code FDIV}, {@code FREM}) and float comparisons
     * ({@code FCMPL}, {@code FCMPG}). Result is float for arithmetic and
     * int for comparisons. Returns the tag to attach to the result.
     */
    default Tag onFloatArith(int opcode, float value1, float value2, Tag tag1, Tag tag2) {
        return Tag.union(tag1, tag2);
    }

    /**
     * Invoked for {@code FNEG}. Returns the tag to attach to the negated
     * result.
     */
    default Tag onFloatUnary(int opcode, float value, Tag tag) {
        return tag;
    }

    /**
     * Invoked for cat-1 numeric conversions whose input and output both fit
     * in one JVM stack slot: {@code I2F}, {@code F2I}. Returns the tag to
     * attach to the converted result.
     */
    default Tag onCat1Convert(int opcode, Tag tag) {
        return tag;
    }

    // ---------- Long (cat-2) ----------

    /**
     * Invoked for long binary arithmetic ({@code LADD}, {@code LSUB},
     * {@code LMUL}, {@code LDIV}, {@code LREM}, {@code LAND}, {@code LOR},
     * {@code LXOR}).
     */
    default Tag onLongArith(int opcode, long value1, long value2, Tag tag1, Tag tag2) {
        return Tag.union(tag1, tag2);
    }

    /**
     * Invoked for long shifts: {@code LSHL}, {@code LSHR}, {@code LUSHR}.
     * The right operand is an int (cat-1); the left is a long (cat-2).
     */
    default Tag onLongShift(int opcode, long value1, int value2, Tag tag1, Tag tag2) {
        return Tag.union(tag1, tag2);
    }

    /**
     * Invoked for {@code LCMP}. Produces an int.
     */
    default Tag onLongCmp(long value1, long value2, Tag tag1, Tag tag2) {
        return Tag.union(tag1, tag2);
    }

    /**
     * Invoked for {@code LNEG}.
     */
    default Tag onLongUnary(int opcode, long value, Tag tag) {
        return tag;
    }

    // ---------- Double (cat-2) ----------

    /**
     * Invoked for double binary arithmetic ({@code DADD}, {@code DSUB},
     * {@code DMUL}, {@code DDIV}, {@code DREM}).
     */
    default Tag onDoubleArith(int opcode, double value1, double value2, Tag tag1, Tag tag2) {
        return Tag.union(tag1, tag2);
    }

    /**
     * Invoked for double comparisons {@code DCMPL}, {@code DCMPG}. Produces
     * an int.
     */
    default Tag onDoubleCmp(int opcode, double value1, double value2, Tag tag1, Tag tag2) {
        return Tag.union(tag1, tag2);
    }

    /**
     * Invoked for {@code DNEG}.
     */
    default Tag onDoubleUnary(int opcode, double value, Tag tag) {
        return tag;
    }

    // ---------- Size-changing conversions ----------

    /**
     * Invoked for {@code I2L} and {@code I2D}. The opcode distinguishes the
     * target type.
     */
    default Tag onIntWiden(int opcode, int value, Tag tag) {
        return tag;
    }

    /**
     * Invoked for {@code F2L} and {@code F2D}. The opcode distinguishes the
     * target type.
     */
    default Tag onFloatWiden(int opcode, float value, Tag tag) {
        return tag;
    }

    /**
     * Invoked for {@code L2I}, {@code L2F}, {@code L2D}. The opcode
     * distinguishes the target type.
     */
    default Tag onLongConvert(int opcode, long value, Tag tag) {
        return tag;
    }

    /**
     * Invoked for {@code D2I}, {@code D2F}, {@code D2L}. The opcode
     * distinguishes the target type.
     */
    default Tag onDoubleConvert(int opcode, double value, Tag tag) {
        return tag;
    }

    // ---------- Arrays ----------

    /**
     * Invoked for array load opcodes: {@code IALOAD}, {@code LALOAD},
     * {@code FALOAD}, {@code DALOAD}, {@code AALOAD}, {@code BALOAD},
     * {@code CALOAD}, {@code SALOAD}. Returns the tag to attach to the
     * loaded element. {@code elemTag} is the tag retrieved from Galette's
     * shadow array-tag store; the listener may return it unchanged or
     * substitute a derived tag.
     */
    default Tag onArrayLoad(int opcode, Object array, int index, Tag arrayTag, Tag indexTag, Tag elemTag) {
        return elemTag;
    }

    /**
     * Invoked for array store opcodes: {@code IASTORE}, {@code LASTORE},
     * {@code FASTORE}, {@code DASTORE}, {@code AASTORE}, {@code BASTORE},
     * {@code CASTORE}, {@code SASTORE}. Returns the tag that will be
     * mirrored into Galette's array-tag store for this slot. By default
     * returns {@code valueTag} unchanged.
     */
    default Tag onArrayStore(int opcode, Object array, int index, Tag arrayTag, Tag indexTag, Tag valueTag) {
        return valueTag;
    }
}
