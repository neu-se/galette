package edu.neu.ccs.prl.galette.internal.runtime.symbolic;

/**
 * JVM opcode constants passed to {@link SymbolicExecutionListener} callbacks.
 *
 * <p>Exposed here so SPI consumers do not need to pull in {@code asm} just to
 * name opcodes. Values match the JVM specification exactly.
 */
public final class SymbolicOpcodes {
    private SymbolicOpcodes() {
        throw new AssertionError();
    }

    // Integer binary arithmetic
    public static final int IADD = 96;
    public static final int LADD = 97;
    public static final int FADD = 98;
    public static final int DADD = 99;
    public static final int ISUB = 100;
    public static final int LSUB = 101;
    public static final int FSUB = 102;
    public static final int DSUB = 103;
    public static final int IMUL = 104;
    public static final int LMUL = 105;
    public static final int FMUL = 106;
    public static final int DMUL = 107;
    public static final int IDIV = 108;
    public static final int LDIV = 109;
    public static final int FDIV = 110;
    public static final int DDIV = 111;
    public static final int IREM = 112;
    public static final int LREM = 113;
    public static final int FREM = 114;
    public static final int DREM = 115;

    // Unary
    public static final int INEG = 116;
    public static final int LNEG = 117;
    public static final int FNEG = 118;
    public static final int DNEG = 119;

    // Shifts
    public static final int ISHL = 120;
    public static final int LSHL = 121;
    public static final int ISHR = 122;
    public static final int LSHR = 123;
    public static final int IUSHR = 124;
    public static final int LUSHR = 125;

    // Bitwise
    public static final int IAND = 126;
    public static final int LAND = 127;
    public static final int IOR = 128;
    public static final int LOR = 129;
    public static final int IXOR = 130;
    public static final int LXOR = 131;

    // Conversions
    public static final int I2L = 133;
    public static final int I2F = 134;
    public static final int I2D = 135;
    public static final int L2I = 136;
    public static final int L2F = 137;
    public static final int L2D = 138;
    public static final int F2I = 139;
    public static final int F2L = 140;
    public static final int F2D = 141;
    public static final int D2I = 142;
    public static final int D2F = 143;
    public static final int D2L = 144;
    public static final int I2B = 145;
    public static final int I2C = 146;
    public static final int I2S = 147;

    // Comparisons (produce int result)
    public static final int LCMP = 148;
    public static final int FCMPL = 149;
    public static final int FCMPG = 150;
    public static final int DCMPL = 151;
    public static final int DCMPG = 152;

    // Conditional branches
    public static final int IFEQ = 153;
    public static final int IFNE = 154;
    public static final int IFLT = 155;
    public static final int IFGE = 156;
    public static final int IFGT = 157;
    public static final int IFLE = 158;
    public static final int IF_ICMPEQ = 159;
    public static final int IF_ICMPNE = 160;
    public static final int IF_ICMPLT = 161;
    public static final int IF_ICMPGE = 162;
    public static final int IF_ICMPGT = 163;
    public static final int IF_ICMPLE = 164;
    public static final int IF_ACMPEQ = 165;
    public static final int IF_ACMPNE = 166;
    public static final int IFNULL = 198;
    public static final int IFNONNULL = 199;

    // Switches
    public static final int TABLESWITCH = 170;
    public static final int LOOKUPSWITCH = 171;

    // Array loads/stores
    public static final int IALOAD = 46;
    public static final int LALOAD = 47;
    public static final int FALOAD = 48;
    public static final int DALOAD = 49;
    public static final int AALOAD = 50;
    public static final int BALOAD = 51;
    public static final int CALOAD = 52;
    public static final int SALOAD = 53;
    public static final int IASTORE = 79;
    public static final int LASTORE = 80;
    public static final int FASTORE = 81;
    public static final int DASTORE = 82;
    public static final int AASTORE = 83;
    public static final int BASTORE = 84;
    public static final int CASTORE = 85;
    public static final int SASTORE = 86;
}
