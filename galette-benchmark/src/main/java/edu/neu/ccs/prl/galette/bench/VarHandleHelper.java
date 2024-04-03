package edu.neu.ccs.prl.galette.bench;

import java.lang.invoke.VarHandle;
import java.lang.invoke.VarHandle.AccessMode;

@SuppressWarnings("DuplicatedCode")
public final class VarHandleHelper {
    public static boolean getInstanceFieldBoolean(VarHandle handle, Holder holder, AccessMode mode) {
        switch (mode) {
            case GET:
                return (boolean) handle.get(holder);
            case GET_VOLATILE:
                return (boolean) handle.getVolatile(holder);
            case GET_ACQUIRE:
                return (boolean) handle.getAcquire(holder);
            case GET_OPAQUE:
                return (boolean) handle.getOpaque(holder);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean getStaticFieldBoolean(VarHandle handle, AccessMode mode) {
        switch (mode) {
            case GET:
                return (boolean) handle.get();
            case GET_VOLATILE:
                return (boolean) handle.getVolatile();
            case GET_ACQUIRE:
                return (boolean) handle.getAcquire();
            case GET_OPAQUE:
                return (boolean) handle.getOpaque();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean getArrayElementBoolean(VarHandle handle, Holder holder, AccessMode mode) {
        boolean[] array = (boolean[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(boolean.class, holder);
        switch (mode) {
            case GET:
                return (boolean) handle.get(array, 0);
            case GET_VOLATILE:
                return (boolean) handle.getVolatile(array, 0);
            case GET_ACQUIRE:
                return (boolean) handle.getAcquire(array, 0);
            case GET_OPAQUE:
                return (boolean) handle.getOpaque(array, 0);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte getInstanceFieldByte(VarHandle handle, Holder holder, AccessMode mode) {
        switch (mode) {
            case GET:
                return (byte) handle.get(holder);
            case GET_VOLATILE:
                return (byte) handle.getVolatile(holder);
            case GET_ACQUIRE:
                return (byte) handle.getAcquire(holder);
            case GET_OPAQUE:
                return (byte) handle.getOpaque(holder);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte getStaticFieldByte(VarHandle handle, AccessMode mode) {
        switch (mode) {
            case GET:
                return (byte) handle.get();
            case GET_VOLATILE:
                return (byte) handle.getVolatile();
            case GET_ACQUIRE:
                return (byte) handle.getAcquire();
            case GET_OPAQUE:
                return (byte) handle.getOpaque();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte getArrayElementByte(VarHandle handle, Holder holder, AccessMode mode) {
        byte[] array = (byte[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(byte.class, holder);
        switch (mode) {
            case GET:
                return (byte) handle.get(array, 0);
            case GET_VOLATILE:
                return (byte) handle.getVolatile(array, 0);
            case GET_ACQUIRE:
                return (byte) handle.getAcquire(array, 0);
            case GET_OPAQUE:
                return (byte) handle.getOpaque(array, 0);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char getInstanceFieldChar(VarHandle handle, Holder holder, AccessMode mode) {
        switch (mode) {
            case GET:
                return (char) handle.get(holder);
            case GET_VOLATILE:
                return (char) handle.getVolatile(holder);
            case GET_ACQUIRE:
                return (char) handle.getAcquire(holder);
            case GET_OPAQUE:
                return (char) handle.getOpaque(holder);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char getStaticFieldChar(VarHandle handle, AccessMode mode) {
        switch (mode) {
            case GET:
                return (char) handle.get();
            case GET_VOLATILE:
                return (char) handle.getVolatile();
            case GET_ACQUIRE:
                return (char) handle.getAcquire();
            case GET_OPAQUE:
                return (char) handle.getOpaque();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char getArrayElementChar(VarHandle handle, Holder holder, AccessMode mode) {
        char[] array = (char[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(char.class, holder);
        switch (mode) {
            case GET:
                return (char) handle.get(array, 0);
            case GET_VOLATILE:
                return (char) handle.getVolatile(array, 0);
            case GET_ACQUIRE:
                return (char) handle.getAcquire(array, 0);
            case GET_OPAQUE:
                return (char) handle.getOpaque(array, 0);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short getInstanceFieldShort(VarHandle handle, Holder holder, AccessMode mode) {
        switch (mode) {
            case GET:
                return (short) handle.get(holder);
            case GET_VOLATILE:
                return (short) handle.getVolatile(holder);
            case GET_ACQUIRE:
                return (short) handle.getAcquire(holder);
            case GET_OPAQUE:
                return (short) handle.getOpaque(holder);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short getStaticFieldShort(VarHandle handle, AccessMode mode) {
        switch (mode) {
            case GET:
                return (short) handle.get();
            case GET_VOLATILE:
                return (short) handle.getVolatile();
            case GET_ACQUIRE:
                return (short) handle.getAcquire();
            case GET_OPAQUE:
                return (short) handle.getOpaque();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short getArrayElementShort(VarHandle handle, Holder holder, AccessMode mode) {
        short[] array = (short[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(short.class, holder);
        switch (mode) {
            case GET:
                return (short) handle.get(array, 0);
            case GET_VOLATILE:
                return (short) handle.getVolatile(array, 0);
            case GET_ACQUIRE:
                return (short) handle.getAcquire(array, 0);
            case GET_OPAQUE:
                return (short) handle.getOpaque(array, 0);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getInstanceFieldInt(VarHandle handle, Holder holder, AccessMode mode) {
        switch (mode) {
            case GET:
                return (int) handle.get(holder);
            case GET_VOLATILE:
                return (int) handle.getVolatile(holder);
            case GET_ACQUIRE:
                return (int) handle.getAcquire(holder);
            case GET_OPAQUE:
                return (int) handle.getOpaque(holder);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getStaticFieldInt(VarHandle handle, AccessMode mode) {
        switch (mode) {
            case GET:
                return (int) handle.get();
            case GET_VOLATILE:
                return (int) handle.getVolatile();
            case GET_ACQUIRE:
                return (int) handle.getAcquire();
            case GET_OPAQUE:
                return (int) handle.getOpaque();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getArrayElementInt(VarHandle handle, Holder holder, AccessMode mode) {
        int[] array = (int[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(int.class, holder);
        switch (mode) {
            case GET:
                return (int) handle.get(array, 0);
            case GET_VOLATILE:
                return (int) handle.getVolatile(array, 0);
            case GET_ACQUIRE:
                return (int) handle.getAcquire(array, 0);
            case GET_OPAQUE:
                return (int) handle.getOpaque(array, 0);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long getInstanceFieldLong(VarHandle handle, Holder holder, AccessMode mode) {
        switch (mode) {
            case GET:
                return (long) handle.get(holder);
            case GET_VOLATILE:
                return (long) handle.getVolatile(holder);
            case GET_ACQUIRE:
                return (long) handle.getAcquire(holder);
            case GET_OPAQUE:
                return (long) handle.getOpaque(holder);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long getStaticFieldLong(VarHandle handle, AccessMode mode) {
        switch (mode) {
            case GET:
                return (long) handle.get();
            case GET_VOLATILE:
                return (long) handle.getVolatile();
            case GET_ACQUIRE:
                return (long) handle.getAcquire();
            case GET_OPAQUE:
                return (long) handle.getOpaque();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long getArrayElementLong(VarHandle handle, Holder holder, AccessMode mode) {
        long[] array = (long[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(long.class, holder);
        switch (mode) {
            case GET:
                return (long) handle.get(array, 0);
            case GET_VOLATILE:
                return (long) handle.getVolatile(array, 0);
            case GET_ACQUIRE:
                return (long) handle.getAcquire(array, 0);
            case GET_OPAQUE:
                return (long) handle.getOpaque(array, 0);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static float getInstanceFieldFloat(VarHandle handle, Holder holder, AccessMode mode) {
        switch (mode) {
            case GET:
                return (float) handle.get(holder);
            case GET_VOLATILE:
                return (float) handle.getVolatile(holder);
            case GET_ACQUIRE:
                return (float) handle.getAcquire(holder);
            case GET_OPAQUE:
                return (float) handle.getOpaque(holder);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static float getStaticFieldFloat(VarHandle handle, AccessMode mode) {
        switch (mode) {
            case GET:
                return (float) handle.get();
            case GET_VOLATILE:
                return (float) handle.getVolatile();
            case GET_ACQUIRE:
                return (float) handle.getAcquire();
            case GET_OPAQUE:
                return (float) handle.getOpaque();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static float getArrayElementFloat(VarHandle handle, Holder holder, AccessMode mode) {
        float[] array = (float[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(float.class, holder);
        switch (mode) {
            case GET:
                return (float) handle.get(array, 0);
            case GET_VOLATILE:
                return (float) handle.getVolatile(array, 0);
            case GET_ACQUIRE:
                return (float) handle.getAcquire(array, 0);
            case GET_OPAQUE:
                return (float) handle.getOpaque(array, 0);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double getInstanceFieldDouble(VarHandle handle, Holder holder, AccessMode mode) {
        switch (mode) {
            case GET:
                return (double) handle.get(holder);
            case GET_VOLATILE:
                return (double) handle.getVolatile(holder);
            case GET_ACQUIRE:
                return (double) handle.getAcquire(holder);
            case GET_OPAQUE:
                return (double) handle.getOpaque(holder);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double getStaticFieldDouble(VarHandle handle, AccessMode mode) {
        switch (mode) {
            case GET:
                return (double) handle.get();
            case GET_VOLATILE:
                return (double) handle.getVolatile();
            case GET_ACQUIRE:
                return (double) handle.getAcquire();
            case GET_OPAQUE:
                return (double) handle.getOpaque();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double getArrayElementDouble(VarHandle handle, Holder holder, AccessMode mode) {
        double[] array = (double[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(double.class, holder);
        switch (mode) {
            case GET:
                return (double) handle.get(array, 0);
            case GET_VOLATILE:
                return (double) handle.getVolatile(array, 0);
            case GET_ACQUIRE:
                return (double) handle.getAcquire(array, 0);
            case GET_OPAQUE:
                return (double) handle.getOpaque(array, 0);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Object getInstanceFieldObject(VarHandle handle, Holder holder, AccessMode mode) {
        switch (mode) {
            case GET:
                return (Object) handle.get(holder);
            case GET_VOLATILE:
                return (Object) handle.getVolatile(holder);
            case GET_ACQUIRE:
                return (Object) handle.getAcquire(holder);
            case GET_OPAQUE:
                return (Object) handle.getOpaque(holder);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Object getStaticFieldObject(VarHandle handle, AccessMode mode) {
        switch (mode) {
            case GET:
                return (Object) handle.get();
            case GET_VOLATILE:
                return (Object) handle.getVolatile();
            case GET_ACQUIRE:
                return (Object) handle.getAcquire();
            case GET_OPAQUE:
                return (Object) handle.getOpaque();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Object getArrayElementObject(VarHandle handle, Holder holder, AccessMode mode) {
        Object[] array = (Object[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(Object.class, holder);
        switch (mode) {
            case GET:
                return (Object) handle.get(array, 0);
            case GET_VOLATILE:
                return (Object) handle.getVolatile(array, 0);
            case GET_ACQUIRE:
                return (Object) handle.getAcquire(array, 0);
            case GET_OPAQUE:
                return (Object) handle.getOpaque(array, 0);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setInstanceFieldBoolean(VarHandle handle, Holder holder, AccessMode mode, boolean value) {
        switch (mode) {
            case SET:
                handle.set(holder, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(holder, value);
                return;
            case SET_RELEASE:
                handle.setRelease(holder, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(holder, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setStaticFieldBoolean(VarHandle handle, AccessMode mode, boolean value) {
        switch (mode) {
            case SET:
                handle.set(value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(value);
                return;
            case SET_RELEASE:
                handle.setRelease(value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setArrayElementBoolean(VarHandle handle, Holder holder, AccessMode mode, boolean value) {
        boolean[] array = (boolean[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(boolean.class, holder);
        switch (mode) {
            case SET:
                handle.set(array, 0, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(array, 0, value);
                return;
            case SET_RELEASE:
                handle.setRelease(array, 0, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(array, 0, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setInstanceFieldByte(VarHandle handle, Holder holder, AccessMode mode, byte value) {
        switch (mode) {
            case SET:
                handle.set(holder, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(holder, value);
                return;
            case SET_RELEASE:
                handle.setRelease(holder, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(holder, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setStaticFieldByte(VarHandle handle, AccessMode mode, byte value) {
        switch (mode) {
            case SET:
                handle.set(value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(value);
                return;
            case SET_RELEASE:
                handle.setRelease(value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setArrayElementByte(VarHandle handle, Holder holder, AccessMode mode, byte value) {
        byte[] array = (byte[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(byte.class, holder);
        switch (mode) {
            case SET:
                handle.set(array, 0, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(array, 0, value);
                return;
            case SET_RELEASE:
                handle.setRelease(array, 0, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(array, 0, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setInstanceFieldChar(VarHandle handle, Holder holder, AccessMode mode, char value) {
        switch (mode) {
            case SET:
                handle.set(holder, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(holder, value);
                return;
            case SET_RELEASE:
                handle.setRelease(holder, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(holder, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setStaticFieldChar(VarHandle handle, AccessMode mode, char value) {
        switch (mode) {
            case SET:
                handle.set(value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(value);
                return;
            case SET_RELEASE:
                handle.setRelease(value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setArrayElementChar(VarHandle handle, Holder holder, AccessMode mode, char value) {
        char[] array = (char[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(char.class, holder);
        switch (mode) {
            case SET:
                handle.set(array, 0, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(array, 0, value);
                return;
            case SET_RELEASE:
                handle.setRelease(array, 0, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(array, 0, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setInstanceFieldShort(VarHandle handle, Holder holder, AccessMode mode, short value) {
        switch (mode) {
            case SET:
                handle.set(holder, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(holder, value);
                return;
            case SET_RELEASE:
                handle.setRelease(holder, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(holder, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setStaticFieldShort(VarHandle handle, AccessMode mode, short value) {
        switch (mode) {
            case SET:
                handle.set(value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(value);
                return;
            case SET_RELEASE:
                handle.setRelease(value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setArrayElementShort(VarHandle handle, Holder holder, AccessMode mode, short value) {
        short[] array = (short[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(short.class, holder);
        switch (mode) {
            case SET:
                handle.set(array, 0, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(array, 0, value);
                return;
            case SET_RELEASE:
                handle.setRelease(array, 0, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(array, 0, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setInstanceFieldInt(VarHandle handle, Holder holder, AccessMode mode, int value) {
        switch (mode) {
            case SET:
                handle.set(holder, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(holder, value);
                return;
            case SET_RELEASE:
                handle.setRelease(holder, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(holder, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setStaticFieldInt(VarHandle handle, AccessMode mode, int value) {
        switch (mode) {
            case SET:
                handle.set(value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(value);
                return;
            case SET_RELEASE:
                handle.setRelease(value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setArrayElementInt(VarHandle handle, Holder holder, AccessMode mode, int value) {
        int[] array = (int[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(int.class, holder);
        switch (mode) {
            case SET:
                handle.set(array, 0, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(array, 0, value);
                return;
            case SET_RELEASE:
                handle.setRelease(array, 0, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(array, 0, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setInstanceFieldLong(VarHandle handle, Holder holder, AccessMode mode, long value) {
        switch (mode) {
            case SET:
                handle.set(holder, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(holder, value);
                return;
            case SET_RELEASE:
                handle.setRelease(holder, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(holder, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setStaticFieldLong(VarHandle handle, AccessMode mode, long value) {
        switch (mode) {
            case SET:
                handle.set(value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(value);
                return;
            case SET_RELEASE:
                handle.setRelease(value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setArrayElementLong(VarHandle handle, Holder holder, AccessMode mode, long value) {
        long[] array = (long[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(long.class, holder);
        switch (mode) {
            case SET:
                handle.set(array, 0, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(array, 0, value);
                return;
            case SET_RELEASE:
                handle.setRelease(array, 0, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(array, 0, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setInstanceFieldFloat(VarHandle handle, Holder holder, AccessMode mode, float value) {
        switch (mode) {
            case SET:
                handle.set(holder, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(holder, value);
                return;
            case SET_RELEASE:
                handle.setRelease(holder, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(holder, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setStaticFieldFloat(VarHandle handle, AccessMode mode, float value) {
        switch (mode) {
            case SET:
                handle.set(value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(value);
                return;
            case SET_RELEASE:
                handle.setRelease(value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setArrayElementFloat(VarHandle handle, Holder holder, AccessMode mode, float value) {
        float[] array = (float[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(float.class, holder);
        switch (mode) {
            case SET:
                handle.set(array, 0, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(array, 0, value);
                return;
            case SET_RELEASE:
                handle.setRelease(array, 0, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(array, 0, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setInstanceFieldDouble(VarHandle handle, Holder holder, AccessMode mode, double value) {
        switch (mode) {
            case SET:
                handle.set(holder, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(holder, value);
                return;
            case SET_RELEASE:
                handle.setRelease(holder, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(holder, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setStaticFieldDouble(VarHandle handle, AccessMode mode, double value) {
        switch (mode) {
            case SET:
                handle.set(value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(value);
                return;
            case SET_RELEASE:
                handle.setRelease(value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setArrayElementDouble(VarHandle handle, Holder holder, AccessMode mode, double value) {
        double[] array = (double[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(double.class, holder);
        switch (mode) {
            case SET:
                handle.set(array, 0, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(array, 0, value);
                return;
            case SET_RELEASE:
                handle.setRelease(array, 0, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(array, 0, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setInstanceFieldObject(VarHandle handle, Holder holder, AccessMode mode, Object value) {
        switch (mode) {
            case SET:
                handle.set(holder, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(holder, value);
                return;
            case SET_RELEASE:
                handle.setRelease(holder, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(holder, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setStaticFieldObject(VarHandle handle, AccessMode mode, Object value) {
        switch (mode) {
            case SET:
                handle.set(value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(value);
                return;
            case SET_RELEASE:
                handle.setRelease(value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void setArrayElementObject(VarHandle handle, Holder holder, AccessMode mode, Object value) {
        Object[] array = (Object[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(Object.class, holder);
        switch (mode) {
            case SET:
                handle.set(array, 0, value);
                return;
            case SET_VOLATILE:
                handle.setVolatile(array, 0, value);
                return;
            case SET_RELEASE:
                handle.setRelease(array, 0, value);
                return;
            case SET_OPAQUE:
                handle.setOpaque(array, 0, value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetInstanceFieldBoolean(
            VarHandle handle, Holder holder, AccessMode mode, boolean original, boolean update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(holder, original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(holder, original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(holder, original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetStaticFieldBoolean(
            VarHandle handle, AccessMode mode, boolean original, boolean update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetArrayElementBoolean(
            VarHandle handle, Holder holder, AccessMode mode, boolean expected, boolean update) {
        boolean[] array = (boolean[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(boolean.class, holder);
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetInstanceFieldByte(
            VarHandle handle, Holder holder, AccessMode mode, byte original, byte update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(holder, original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(holder, original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(holder, original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetStaticFieldByte(VarHandle handle, AccessMode mode, byte original, byte update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetArrayElementByte(
            VarHandle handle, Holder holder, AccessMode mode, byte expected, byte update) {
        byte[] array = (byte[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(byte.class, holder);
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetInstanceFieldChar(
            VarHandle handle, Holder holder, AccessMode mode, char original, char update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(holder, original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(holder, original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(holder, original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetStaticFieldChar(VarHandle handle, AccessMode mode, char original, char update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetArrayElementChar(
            VarHandle handle, Holder holder, AccessMode mode, char expected, char update) {
        char[] array = (char[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(char.class, holder);
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetInstanceFieldShort(
            VarHandle handle, Holder holder, AccessMode mode, short original, short update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(holder, original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(holder, original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(holder, original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetStaticFieldShort(
            VarHandle handle, AccessMode mode, short original, short update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetArrayElementShort(
            VarHandle handle, Holder holder, AccessMode mode, short expected, short update) {
        short[] array = (short[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(short.class, holder);
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetInstanceFieldInt(
            VarHandle handle, Holder holder, AccessMode mode, int original, int update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(holder, original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(holder, original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(holder, original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetStaticFieldInt(VarHandle handle, AccessMode mode, int original, int update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetArrayElementInt(
            VarHandle handle, Holder holder, AccessMode mode, int expected, int update) {
        int[] array = (int[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(int.class, holder);
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetInstanceFieldLong(
            VarHandle handle, Holder holder, AccessMode mode, long original, long update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(holder, original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(holder, original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(holder, original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetStaticFieldLong(VarHandle handle, AccessMode mode, long original, long update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetArrayElementLong(
            VarHandle handle, Holder holder, AccessMode mode, long expected, long update) {
        long[] array = (long[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(long.class, holder);
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetInstanceFieldFloat(
            VarHandle handle, Holder holder, AccessMode mode, float original, float update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(holder, original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(holder, original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(holder, original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetStaticFieldFloat(
            VarHandle handle, AccessMode mode, float original, float update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetArrayElementFloat(
            VarHandle handle, Holder holder, AccessMode mode, float expected, float update) {
        float[] array = (float[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(float.class, holder);
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetInstanceFieldDouble(
            VarHandle handle, Holder holder, AccessMode mode, double original, double update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(holder, original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(holder, original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(holder, original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetStaticFieldDouble(
            VarHandle handle, AccessMode mode, double original, double update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetArrayElementDouble(
            VarHandle handle, Holder holder, AccessMode mode, double expected, double update) {
        double[] array = (double[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(double.class, holder);
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetInstanceFieldObject(
            VarHandle handle, Holder holder, AccessMode mode, Object original, Object update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(holder, original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(holder, original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(holder, original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(holder, original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetStaticFieldObject(
            VarHandle handle, AccessMode mode, Object original, Object update) {
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(original, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(original, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(original, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(original, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(original, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndSetArrayElementObject(
            VarHandle handle, Holder holder, AccessMode mode, Object expected, Object update) {
        Object[] array = (Object[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(Object.class, holder);
        switch (mode) {
            case COMPARE_AND_SET:
                return (boolean) handle.compareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET:
                return (boolean) handle.weakCompareAndSet(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_PLAIN:
                return (boolean) handle.weakCompareAndSetPlain(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_ACQUIRE:
                return (boolean) handle.weakCompareAndSetAcquire(array, 0, expected, update);
            case WEAK_COMPARE_AND_SET_RELEASE:
                return (boolean) handle.weakCompareAndSetRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }
}
