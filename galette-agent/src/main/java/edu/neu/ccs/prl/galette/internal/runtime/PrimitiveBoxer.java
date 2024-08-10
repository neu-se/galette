package edu.neu.ccs.prl.galette.internal.runtime;

public final class PrimitiveBoxer {
    @InvokedViaHandle(handle = Handle.BOX_BOOLEAN)
    public static BoxedBoolean box(boolean value) {
        return new BoxedBoolean(value);
    }

    @InvokedViaHandle(handle = Handle.BOX_BYTE)
    public static BoxedByte box(byte value) {
        return new BoxedByte(value);
    }

    @InvokedViaHandle(handle = Handle.BOX_CHAR)
    public static BoxedChar box(char value) {
        return new BoxedChar(value);
    }

    @InvokedViaHandle(handle = Handle.BOX_SHORT)
    public static BoxedShort box(short value) {
        return new BoxedShort(value);
    }

    @InvokedViaHandle(handle = Handle.BOX_INT)
    public static BoxedInt box(int value) {
        return new BoxedInt(value);
    }

    @InvokedViaHandle(handle = Handle.BOX_LONG)
    public static BoxedLong box(long value) {
        return new BoxedLong(value);
    }

    @InvokedViaHandle(handle = Handle.BOX_FLOAT)
    public static BoxedFloat box(float value) {
        return new BoxedFloat(value);
    }

    @InvokedViaHandle(handle = Handle.BOX_DOUBLE)
    public static BoxedDouble box(double value) {
        return new BoxedDouble(value);
    }

    @InvokedViaHandle(handle = Handle.UNBOX_BOOLEAN)
    public static boolean unbox(BoxedBoolean value) {
        return value.getValue();
    }

    @InvokedViaHandle(handle = Handle.UNBOX_BYTE)
    public static byte unbox(BoxedByte value) {
        return value.getValue();
    }

    @InvokedViaHandle(handle = Handle.UNBOX_CHAR)
    public static char unbox(BoxedChar value) {
        return value.getValue();
    }

    @InvokedViaHandle(handle = Handle.UNBOX_SHORT)
    public static short unbox(BoxedShort value) {
        return value.getValue();
    }

    @InvokedViaHandle(handle = Handle.UNBOX_INT)
    public static int unbox(BoxedInt value) {
        return value.getValue();
    }

    @InvokedViaHandle(handle = Handle.UNBOX_LONG)
    public static long unbox(BoxedLong value) {
        return value.getValue();
    }

    @InvokedViaHandle(handle = Handle.UNBOX_FLOAT)
    public static float unbox(BoxedFloat value) {
        return value.getValue();
    }

    @InvokedViaHandle(handle = Handle.UNBOX_DOUBLE)
    public static double unbox(BoxedDouble value) {
        return value.getValue();
    }

    public static boolean isBoxed(Object o) {
        return o instanceof BoxedStackInt
                || o instanceof BoxedLong
                || o instanceof BoxedFloat
                || o instanceof BoxedDouble;
    }

    public interface BoxedStackInt {
        int getIntValue();
    }

    public static final class BoxedBoolean implements BoxedStackInt {
        private final boolean value;

        public BoxedBoolean(boolean value) {
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }

        @Override
        public int getIntValue() {
            return value ? 1 : 0;
        }
    }

    public static final class BoxedByte implements BoxedStackInt {
        private final byte value;

        public BoxedByte(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }

        @Override
        public int getIntValue() {
            return value;
        }
    }

    public static final class BoxedChar implements BoxedStackInt {
        private final char value;

        public BoxedChar(char value) {
            this.value = value;
        }

        public char getValue() {
            return value;
        }

        @Override
        public int getIntValue() {
            return value;
        }
    }

    public static final class BoxedShort implements BoxedStackInt {
        private final short value;

        public BoxedShort(short value) {
            this.value = value;
        }

        public short getValue() {
            return value;
        }

        @Override
        public int getIntValue() {
            return value;
        }
    }

    public static final class BoxedInt implements BoxedStackInt {
        private final int value;

        public BoxedInt(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public int getIntValue() {
            return value;
        }
    }

    public static final class BoxedLong {
        private final long value;

        public BoxedLong(long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }
    }

    public static final class BoxedFloat {
        private final float value;

        public BoxedFloat(float value) {
            this.value = value;
        }

        public float getValue() {
            return value;
        }
    }

    public static final class BoxedDouble {
        private final double value;

        public BoxedDouble(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }
    }
}
