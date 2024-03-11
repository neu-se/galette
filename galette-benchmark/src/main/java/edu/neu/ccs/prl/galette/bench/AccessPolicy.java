package edu.neu.ccs.prl.galette.bench;

public enum AccessPolicy {
    VOLATILE {
        @Override
        void putBoolean(Object o, long offset, boolean x, UnsafeWrapper unsafe) {
            unsafe.putBooleanVolatile(o, offset, x);
        }

        @Override
        void putByte(Object o, long offset, byte x, UnsafeWrapper unsafe) {
            unsafe.putByteVolatile(o, offset, x);
        }

        @Override
        void putChar(Object o, long offset, char x, UnsafeWrapper unsafe) {
            unsafe.putCharVolatile(o, offset, x);
        }

        @Override
        void putDouble(Object o, long offset, double x, UnsafeWrapper unsafe) {
            unsafe.putDoubleVolatile(o, offset, x);
        }

        @Override
        void putFloat(Object o, long offset, float x, UnsafeWrapper unsafe) {
            unsafe.putFloatVolatile(o, offset, x);
        }

        @Override
        void putInt(Object o, long offset, int x, UnsafeWrapper unsafe) {
            unsafe.putIntVolatile(o, offset, x);
        }

        @Override
        void putLong(Object o, long offset, long x, UnsafeWrapper unsafe) {
            unsafe.putLongVolatile(o, offset, x);
        }

        @Override
        void putShort(Object o, long offset, short x, UnsafeWrapper unsafe) {
            unsafe.putShortVolatile(o, offset, x);
        }

        @Override
        void putObject(Object o, long offset, Object x, UnsafeWrapper unsafe) {
            unsafe.putObjectVolatile(o, offset, x);
        }

        @Override
        boolean getBoolean(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getBooleanVolatile(o, offset);
        }

        @Override
        byte getByte(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getByteVolatile(o, offset);
        }

        @Override
        char getChar(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getCharVolatile(o, offset);
        }

        @Override
        double getDouble(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getDoubleVolatile(o, offset);
        }

        @Override
        float getFloat(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getFloatVolatile(o, offset);
        }

        @Override
        int getInt(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getIntVolatile(o, offset);
        }

        @Override
        long getLong(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getLongVolatile(o, offset);
        }

        @Override
        short getShort(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getShortVolatile(o, offset);
        }

        @Override
        Object getObject(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getObjectVolatile(o, offset);
        }
    },
    NORMAL {
        @Override
        void putBoolean(Object o, long offset, boolean x, UnsafeWrapper unsafe) {
            unsafe.putBoolean(o, offset, x);
        }

        @Override
        void putByte(Object o, long offset, byte x, UnsafeWrapper unsafe) {
            unsafe.putByte(o, offset, x);
        }

        @Override
        void putChar(Object o, long offset, char x, UnsafeWrapper unsafe) {
            unsafe.putChar(o, offset, x);
        }

        @Override
        void putDouble(Object o, long offset, double x, UnsafeWrapper unsafe) {
            unsafe.putDouble(o, offset, x);
        }

        @Override
        void putFloat(Object o, long offset, float x, UnsafeWrapper unsafe) {
            unsafe.putFloat(o, offset, x);
        }

        @Override
        void putInt(Object o, long offset, int x, UnsafeWrapper unsafe) {
            unsafe.putInt(o, offset, x);
        }

        @Override
        void putLong(Object o, long offset, long x, UnsafeWrapper unsafe) {
            unsafe.putLong(o, offset, x);
        }

        @Override
        void putShort(Object o, long offset, short x, UnsafeWrapper unsafe) {
            unsafe.putShort(o, offset, x);
        }

        @Override
        void putObject(Object o, long offset, Object x, UnsafeWrapper unsafe) {
            unsafe.putObject(o, offset, x);
        }

        @Override
        boolean getBoolean(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getBoolean(o, offset);
        }

        @Override
        byte getByte(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getByte(o, offset);
        }

        @Override
        char getChar(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getChar(o, offset);
        }

        @Override
        double getDouble(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getDouble(o, offset);
        }

        @Override
        float getFloat(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getFloat(o, offset);
        }

        @Override
        int getInt(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getInt(o, offset);
        }

        @Override
        long getLong(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getLong(o, offset);
        }

        @Override
        short getShort(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getShort(o, offset);
        }

        @Override
        Object getObject(Object o, long offset, UnsafeWrapper unsafe) {
            return unsafe.getObject(o, offset);
        }
    },
    ORDERED {
        @Override
        void putBoolean(Object o, long offset, boolean x, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        void putByte(Object o, long offset, byte x, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        void putChar(Object o, long offset, char x, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        void putDouble(Object o, long offset, double x, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        void putFloat(Object o, long offset, float x, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        void putInt(Object o, long offset, int x, UnsafeWrapper unsafe) {
            unsafe.putOrderedInt(o, offset, x);
        }

        @Override
        void putLong(Object o, long offset, long x, UnsafeWrapper unsafe) {
            unsafe.putOrderedLong(o, offset, x);
        }

        @Override
        void putShort(Object o, long offset, short x, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        void putObject(Object o, long offset, Object x, UnsafeWrapper unsafe) {
            unsafe.putOrderedObject(o, offset, x);
        }

        @Override
        boolean getBoolean(Object o, long offset, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        byte getByte(Object o, long offset, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        char getChar(Object o, long offset, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        double getDouble(Object o, long offset, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        float getFloat(Object o, long offset, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        int getInt(Object o, long offset, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        long getLong(Object o, long offset, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        short getShort(Object o, long offset, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }

        @Override
        Object getObject(Object o, long offset, UnsafeWrapper unsafe) {
            throw new UnsupportedOperationException();
        }
    };

    abstract void putBoolean(Object o, long offset, boolean x, UnsafeWrapper unsafe);

    abstract void putByte(Object o, long offset, byte x, UnsafeWrapper unsafe);

    abstract void putChar(Object o, long offset, char x, UnsafeWrapper unsafe);

    abstract void putDouble(Object o, long offset, double x, UnsafeWrapper unsafe);

    abstract void putFloat(Object o, long offset, float x, UnsafeWrapper unsafe);

    abstract void putInt(Object o, long offset, int x, UnsafeWrapper unsafe);

    abstract void putLong(Object o, long offset, long x, UnsafeWrapper unsafe);

    abstract void putShort(Object o, long offset, short x, UnsafeWrapper unsafe);

    abstract void putObject(Object o, long offset, Object x, UnsafeWrapper unsafe);

    abstract boolean getBoolean(Object o, long offset, UnsafeWrapper unsafe);

    abstract byte getByte(Object o, long offset, UnsafeWrapper unsafe);

    abstract char getChar(Object o, long offset, UnsafeWrapper unsafe);

    abstract double getDouble(Object o, long offset, UnsafeWrapper unsafe);

    abstract float getFloat(Object o, long offset, UnsafeWrapper unsafe);

    abstract int getInt(Object o, long offset, UnsafeWrapper unsafe);

    abstract long getLong(Object o, long offset, UnsafeWrapper unsafe);

    abstract short getShort(Object o, long offset, UnsafeWrapper unsafe);

    abstract Object getObject(Object o, long offset, UnsafeWrapper unsafe);
}
