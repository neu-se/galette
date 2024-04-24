package edu.neu.ccs.prl.galette.bench;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

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

    public static boolean getAndSetInstanceFieldBoolean(
            VarHandle handle, Holder holder, AccessMode mode, boolean update) {
        switch (mode) {
            case GET_AND_SET:
                return (boolean) handle.getAndSet(holder, update);
            case GET_AND_SET_ACQUIRE:
                return (boolean) handle.getAndSetAcquire(holder, update);
            case GET_AND_SET_RELEASE:
                return (boolean) handle.getAndSetRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean getAndSetStaticFieldBoolean(VarHandle handle, AccessMode mode, boolean update) {
        switch (mode) {
            case GET_AND_SET:
                return (boolean) handle.getAndSet(update);
            case GET_AND_SET_ACQUIRE:
                return (boolean) handle.getAndSetAcquire(update);
            case GET_AND_SET_RELEASE:
                return (boolean) handle.getAndSetRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean getAndSetArrayElementBoolean(
            VarHandle handle, Holder holder, AccessMode mode, boolean update) {
        boolean[] array = (boolean[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(boolean.class, holder);
        switch (mode) {
            case GET_AND_SET:
                return (boolean) handle.getAndSet(array, 0, update);
            case GET_AND_SET_ACQUIRE:
                return (boolean) handle.getAndSetAcquire(array, 0, update);
            case GET_AND_SET_RELEASE:
                return (boolean) handle.getAndSetRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte getAndSetInstanceFieldByte(VarHandle handle, Holder holder, AccessMode mode, byte update) {
        switch (mode) {
            case GET_AND_SET:
                return (byte) handle.getAndSet(holder, update);
            case GET_AND_SET_ACQUIRE:
                return (byte) handle.getAndSetAcquire(holder, update);
            case GET_AND_SET_RELEASE:
                return (byte) handle.getAndSetRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte getAndSetStaticFieldByte(VarHandle handle, AccessMode mode, byte update) {
        switch (mode) {
            case GET_AND_SET:
                return (byte) handle.getAndSet(update);
            case GET_AND_SET_ACQUIRE:
                return (byte) handle.getAndSetAcquire(update);
            case GET_AND_SET_RELEASE:
                return (byte) handle.getAndSetRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte getAndSetArrayElementByte(VarHandle handle, Holder holder, AccessMode mode, byte update) {
        byte[] array = (byte[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(byte.class, holder);
        switch (mode) {
            case GET_AND_SET:
                return (byte) handle.getAndSet(array, 0, update);
            case GET_AND_SET_ACQUIRE:
                return (byte) handle.getAndSetAcquire(array, 0, update);
            case GET_AND_SET_RELEASE:
                return (byte) handle.getAndSetRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char getAndSetInstanceFieldChar(VarHandle handle, Holder holder, AccessMode mode, char update) {
        switch (mode) {
            case GET_AND_SET:
                return (char) handle.getAndSet(holder, update);
            case GET_AND_SET_ACQUIRE:
                return (char) handle.getAndSetAcquire(holder, update);
            case GET_AND_SET_RELEASE:
                return (char) handle.getAndSetRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char getAndSetStaticFieldChar(VarHandle handle, AccessMode mode, char update) {
        switch (mode) {
            case GET_AND_SET:
                return (char) handle.getAndSet(update);
            case GET_AND_SET_ACQUIRE:
                return (char) handle.getAndSetAcquire(update);
            case GET_AND_SET_RELEASE:
                return (char) handle.getAndSetRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char getAndSetArrayElementChar(VarHandle handle, Holder holder, AccessMode mode, char update) {
        char[] array = (char[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(char.class, holder);
        switch (mode) {
            case GET_AND_SET:
                return (char) handle.getAndSet(array, 0, update);
            case GET_AND_SET_ACQUIRE:
                return (char) handle.getAndSetAcquire(array, 0, update);
            case GET_AND_SET_RELEASE:
                return (char) handle.getAndSetRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short getAndSetInstanceFieldShort(VarHandle handle, Holder holder, AccessMode mode, short update) {
        switch (mode) {
            case GET_AND_SET:
                return (short) handle.getAndSet(holder, update);
            case GET_AND_SET_ACQUIRE:
                return (short) handle.getAndSetAcquire(holder, update);
            case GET_AND_SET_RELEASE:
                return (short) handle.getAndSetRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short getAndSetStaticFieldShort(VarHandle handle, AccessMode mode, short update) {
        switch (mode) {
            case GET_AND_SET:
                return (short) handle.getAndSet(update);
            case GET_AND_SET_ACQUIRE:
                return (short) handle.getAndSetAcquire(update);
            case GET_AND_SET_RELEASE:
                return (short) handle.getAndSetRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short getAndSetArrayElementShort(VarHandle handle, Holder holder, AccessMode mode, short update) {
        short[] array = (short[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(short.class, holder);
        switch (mode) {
            case GET_AND_SET:
                return (short) handle.getAndSet(array, 0, update);
            case GET_AND_SET_ACQUIRE:
                return (short) handle.getAndSetAcquire(array, 0, update);
            case GET_AND_SET_RELEASE:
                return (short) handle.getAndSetRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getAndSetInstanceFieldInt(VarHandle handle, Holder holder, AccessMode mode, int update) {
        switch (mode) {
            case GET_AND_SET:
                return (int) handle.getAndSet(holder, update);
            case GET_AND_SET_ACQUIRE:
                return (int) handle.getAndSetAcquire(holder, update);
            case GET_AND_SET_RELEASE:
                return (int) handle.getAndSetRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getAndSetStaticFieldInt(VarHandle handle, AccessMode mode, int update) {
        switch (mode) {
            case GET_AND_SET:
                return (int) handle.getAndSet(update);
            case GET_AND_SET_ACQUIRE:
                return (int) handle.getAndSetAcquire(update);
            case GET_AND_SET_RELEASE:
                return (int) handle.getAndSetRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getAndSetArrayElementInt(VarHandle handle, Holder holder, AccessMode mode, int update) {
        int[] array = (int[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(int.class, holder);
        switch (mode) {
            case GET_AND_SET:
                return (int) handle.getAndSet(array, 0, update);
            case GET_AND_SET_ACQUIRE:
                return (int) handle.getAndSetAcquire(array, 0, update);
            case GET_AND_SET_RELEASE:
                return (int) handle.getAndSetRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long getAndSetInstanceFieldLong(VarHandle handle, Holder holder, AccessMode mode, long update) {
        switch (mode) {
            case GET_AND_SET:
                return (long) handle.getAndSet(holder, update);
            case GET_AND_SET_ACQUIRE:
                return (long) handle.getAndSetAcquire(holder, update);
            case GET_AND_SET_RELEASE:
                return (long) handle.getAndSetRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long getAndSetStaticFieldLong(VarHandle handle, AccessMode mode, long update) {
        switch (mode) {
            case GET_AND_SET:
                return (long) handle.getAndSet(update);
            case GET_AND_SET_ACQUIRE:
                return (long) handle.getAndSetAcquire(update);
            case GET_AND_SET_RELEASE:
                return (long) handle.getAndSetRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long getAndSetArrayElementLong(VarHandle handle, Holder holder, AccessMode mode, long update) {
        long[] array = (long[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(long.class, holder);
        switch (mode) {
            case GET_AND_SET:
                return (long) handle.getAndSet(array, 0, update);
            case GET_AND_SET_ACQUIRE:
                return (long) handle.getAndSetAcquire(array, 0, update);
            case GET_AND_SET_RELEASE:
                return (long) handle.getAndSetRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static float getAndSetInstanceFieldFloat(VarHandle handle, Holder holder, AccessMode mode, float update) {
        switch (mode) {
            case GET_AND_SET:
                return (float) handle.getAndSet(holder, update);
            case GET_AND_SET_ACQUIRE:
                return (float) handle.getAndSetAcquire(holder, update);
            case GET_AND_SET_RELEASE:
                return (float) handle.getAndSetRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static float getAndSetStaticFieldFloat(VarHandle handle, AccessMode mode, float update) {
        switch (mode) {
            case GET_AND_SET:
                return (float) handle.getAndSet(update);
            case GET_AND_SET_ACQUIRE:
                return (float) handle.getAndSetAcquire(update);
            case GET_AND_SET_RELEASE:
                return (float) handle.getAndSetRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static float getAndSetArrayElementFloat(VarHandle handle, Holder holder, AccessMode mode, float update) {
        float[] array = (float[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(float.class, holder);
        switch (mode) {
            case GET_AND_SET:
                return (float) handle.getAndSet(array, 0, update);
            case GET_AND_SET_ACQUIRE:
                return (float) handle.getAndSetAcquire(array, 0, update);
            case GET_AND_SET_RELEASE:
                return (float) handle.getAndSetRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double getAndSetInstanceFieldDouble(VarHandle handle, Holder holder, AccessMode mode, double update) {
        switch (mode) {
            case GET_AND_SET:
                return (double) handle.getAndSet(holder, update);
            case GET_AND_SET_ACQUIRE:
                return (double) handle.getAndSetAcquire(holder, update);
            case GET_AND_SET_RELEASE:
                return (double) handle.getAndSetRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double getAndSetStaticFieldDouble(VarHandle handle, AccessMode mode, double update) {
        switch (mode) {
            case GET_AND_SET:
                return (double) handle.getAndSet(update);
            case GET_AND_SET_ACQUIRE:
                return (double) handle.getAndSetAcquire(update);
            case GET_AND_SET_RELEASE:
                return (double) handle.getAndSetRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double getAndSetArrayElementDouble(VarHandle handle, Holder holder, AccessMode mode, double update) {
        double[] array = (double[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(double.class, holder);
        switch (mode) {
            case GET_AND_SET:
                return (double) handle.getAndSet(array, 0, update);
            case GET_AND_SET_ACQUIRE:
                return (double) handle.getAndSetAcquire(array, 0, update);
            case GET_AND_SET_RELEASE:
                return (double) handle.getAndSetRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Object getAndSetInstanceFieldObject(VarHandle handle, Holder holder, AccessMode mode, Object update) {
        switch (mode) {
            case GET_AND_SET:
                return (Object) handle.getAndSet(holder, update);
            case GET_AND_SET_ACQUIRE:
                return (Object) handle.getAndSetAcquire(holder, update);
            case GET_AND_SET_RELEASE:
                return (Object) handle.getAndSetRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Object getAndSetStaticFieldObject(VarHandle handle, AccessMode mode, Object update) {
        switch (mode) {
            case GET_AND_SET:
                return (Object) handle.getAndSet(update);
            case GET_AND_SET_ACQUIRE:
                return (Object) handle.getAndSetAcquire(update);
            case GET_AND_SET_RELEASE:
                return (Object) handle.getAndSetRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Object getAndSetArrayElementObject(VarHandle handle, Holder holder, AccessMode mode, Object update) {
        Object[] array = (Object[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(Object.class, holder);
        switch (mode) {
            case GET_AND_SET:
                return (Object) handle.getAndSet(array, 0, update);
            case GET_AND_SET_ACQUIRE:
                return (Object) handle.getAndSetAcquire(array, 0, update);
            case GET_AND_SET_RELEASE:
                return (Object) handle.getAndSetRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndExchangeInstanceFieldBoolean(
            VarHandle handle, Holder holder, AccessMode mode, boolean expected, boolean update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (boolean) handle.compareAndExchange(holder, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (boolean) handle.compareAndExchangeAcquire(holder, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (boolean) handle.compareAndExchangeRelease(holder, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndExchangeStaticFieldBoolean(
            VarHandle handle, AccessMode mode, boolean expected, boolean update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (boolean) handle.compareAndExchange(expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (boolean) handle.compareAndExchangeAcquire(expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (boolean) handle.compareAndExchangeRelease(expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean compareAndExchangeArrayElementBoolean(
            VarHandle handle, Holder holder, AccessMode mode, boolean expected, boolean update) {
        boolean[] array = (boolean[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(boolean.class, holder);
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (boolean) handle.compareAndExchange(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (boolean) handle.compareAndExchangeAcquire(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (boolean) handle.compareAndExchangeRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte compareAndExchangeInstanceFieldByte(
            VarHandle handle, Holder holder, AccessMode mode, byte expected, byte update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (byte) handle.compareAndExchange(holder, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (byte) handle.compareAndExchangeAcquire(holder, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (byte) handle.compareAndExchangeRelease(holder, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte compareAndExchangeStaticFieldByte(
            VarHandle handle, AccessMode mode, byte expected, byte update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (byte) handle.compareAndExchange(expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (byte) handle.compareAndExchangeAcquire(expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (byte) handle.compareAndExchangeRelease(expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte compareAndExchangeArrayElementByte(
            VarHandle handle, Holder holder, AccessMode mode, byte expected, byte update) {
        byte[] array = (byte[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(byte.class, holder);
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (byte) handle.compareAndExchange(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (byte) handle.compareAndExchangeAcquire(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (byte) handle.compareAndExchangeRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char compareAndExchangeInstanceFieldChar(
            VarHandle handle, Holder holder, AccessMode mode, char expected, char update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (char) handle.compareAndExchange(holder, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (char) handle.compareAndExchangeAcquire(holder, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (char) handle.compareAndExchangeRelease(holder, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char compareAndExchangeStaticFieldChar(
            VarHandle handle, AccessMode mode, char expected, char update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (char) handle.compareAndExchange(expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (char) handle.compareAndExchangeAcquire(expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (char) handle.compareAndExchangeRelease(expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char compareAndExchangeArrayElementChar(
            VarHandle handle, Holder holder, AccessMode mode, char expected, char update) {
        char[] array = (char[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(char.class, holder);
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (char) handle.compareAndExchange(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (char) handle.compareAndExchangeAcquire(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (char) handle.compareAndExchangeRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short compareAndExchangeInstanceFieldShort(
            VarHandle handle, Holder holder, AccessMode mode, short expected, short update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (short) handle.compareAndExchange(holder, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (short) handle.compareAndExchangeAcquire(holder, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (short) handle.compareAndExchangeRelease(holder, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short compareAndExchangeStaticFieldShort(
            VarHandle handle, AccessMode mode, short expected, short update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (short) handle.compareAndExchange(expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (short) handle.compareAndExchangeAcquire(expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (short) handle.compareAndExchangeRelease(expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short compareAndExchangeArrayElementShort(
            VarHandle handle, Holder holder, AccessMode mode, short expected, short update) {
        short[] array = (short[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(short.class, holder);
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (short) handle.compareAndExchange(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (short) handle.compareAndExchangeAcquire(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (short) handle.compareAndExchangeRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int compareAndExchangeInstanceFieldInt(
            VarHandle handle, Holder holder, AccessMode mode, int expected, int update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (int) handle.compareAndExchange(holder, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (int) handle.compareAndExchangeAcquire(holder, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (int) handle.compareAndExchangeRelease(holder, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int compareAndExchangeStaticFieldInt(VarHandle handle, AccessMode mode, int expected, int update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (int) handle.compareAndExchange(expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (int) handle.compareAndExchangeAcquire(expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (int) handle.compareAndExchangeRelease(expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int compareAndExchangeArrayElementInt(
            VarHandle handle, Holder holder, AccessMode mode, int expected, int update) {
        int[] array = (int[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(int.class, holder);
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (int) handle.compareAndExchange(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (int) handle.compareAndExchangeAcquire(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (int) handle.compareAndExchangeRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long compareAndExchangeInstanceFieldLong(
            VarHandle handle, Holder holder, AccessMode mode, long expected, long update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (long) handle.compareAndExchange(holder, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (long) handle.compareAndExchangeAcquire(holder, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (long) handle.compareAndExchangeRelease(holder, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long compareAndExchangeStaticFieldLong(
            VarHandle handle, AccessMode mode, long expected, long update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (long) handle.compareAndExchange(expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (long) handle.compareAndExchangeAcquire(expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (long) handle.compareAndExchangeRelease(expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long compareAndExchangeArrayElementLong(
            VarHandle handle, Holder holder, AccessMode mode, long expected, long update) {
        long[] array = (long[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(long.class, holder);
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (long) handle.compareAndExchange(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (long) handle.compareAndExchangeAcquire(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (long) handle.compareAndExchangeRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static float compareAndExchangeInstanceFieldFloat(
            VarHandle handle, Holder holder, AccessMode mode, float expected, float update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (float) handle.compareAndExchange(holder, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (float) handle.compareAndExchangeAcquire(holder, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (float) handle.compareAndExchangeRelease(holder, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static float compareAndExchangeStaticFieldFloat(
            VarHandle handle, AccessMode mode, float expected, float update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (float) handle.compareAndExchange(expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (float) handle.compareAndExchangeAcquire(expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (float) handle.compareAndExchangeRelease(expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static float compareAndExchangeArrayElementFloat(
            VarHandle handle, Holder holder, AccessMode mode, float expected, float update) {
        float[] array = (float[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(float.class, holder);
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (float) handle.compareAndExchange(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (float) handle.compareAndExchangeAcquire(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (float) handle.compareAndExchangeRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double compareAndExchangeInstanceFieldDouble(
            VarHandle handle, Holder holder, AccessMode mode, double expected, double update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (double) handle.compareAndExchange(holder, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (double) handle.compareAndExchangeAcquire(holder, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (double) handle.compareAndExchangeRelease(holder, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double compareAndExchangeStaticFieldDouble(
            VarHandle handle, AccessMode mode, double expected, double update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (double) handle.compareAndExchange(expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (double) handle.compareAndExchangeAcquire(expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (double) handle.compareAndExchangeRelease(expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double compareAndExchangeArrayElementDouble(
            VarHandle handle, Holder holder, AccessMode mode, double expected, double update) {
        double[] array = (double[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(double.class, holder);
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (double) handle.compareAndExchange(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (double) handle.compareAndExchangeAcquire(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (double) handle.compareAndExchangeRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Object compareAndExchangeInstanceFieldObject(
            VarHandle handle, Holder holder, AccessMode mode, Object expected, Object update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (Object) handle.compareAndExchange(holder, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (Object) handle.compareAndExchangeAcquire(holder, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (Object) handle.compareAndExchangeRelease(holder, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Object compareAndExchangeStaticFieldObject(
            VarHandle handle, AccessMode mode, Object expected, Object update) {
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (Object) handle.compareAndExchange(expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (Object) handle.compareAndExchangeAcquire(expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (Object) handle.compareAndExchangeRelease(expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Object compareAndExchangeArrayElementObject(
            VarHandle handle, Holder holder, AccessMode mode, Object expected, Object update) {
        Object[] array = (Object[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(Object.class, holder);
        switch (mode) {
            case COMPARE_AND_EXCHANGE:
                return (Object) handle.compareAndExchange(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_ACQUIRE:
                return (Object) handle.compareAndExchangeAcquire(array, 0, expected, update);
            case COMPARE_AND_EXCHANGE_RELEASE:
                return (Object) handle.compareAndExchangeRelease(array, 0, expected, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte getAndAddInstanceFieldByte(VarHandle handle, Holder holder, AccessMode mode, byte update) {
        switch (mode) {
            case GET_AND_ADD:
                return (byte) handle.getAndAdd(holder, update);
            case GET_AND_ADD_ACQUIRE:
                return (byte) handle.getAndAddAcquire(holder, update);
            case GET_AND_ADD_RELEASE:
                return (byte) handle.getAndAddRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte getAndAddStaticFieldByte(VarHandle handle, AccessMode mode, byte update) {
        switch (mode) {
            case GET_AND_ADD:
                return (byte) handle.getAndAdd(update);
            case GET_AND_ADD_ACQUIRE:
                return (byte) handle.getAndAddAcquire(update);
            case GET_AND_ADD_RELEASE:
                return (byte) handle.getAndAddRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte getAndAddArrayElementByte(VarHandle handle, Holder holder, AccessMode mode, byte update) {
        byte[] array = (byte[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(byte.class, holder);
        switch (mode) {
            case GET_AND_ADD:
                return (byte) handle.getAndAdd(array, 0, update);
            case GET_AND_ADD_ACQUIRE:
                return (byte) handle.getAndAddAcquire(array, 0, update);
            case GET_AND_ADD_RELEASE:
                return (byte) handle.getAndAddRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char getAndAddInstanceFieldChar(VarHandle handle, Holder holder, AccessMode mode, char update) {
        switch (mode) {
            case GET_AND_ADD:
                return (char) handle.getAndAdd(holder, update);
            case GET_AND_ADD_ACQUIRE:
                return (char) handle.getAndAddAcquire(holder, update);
            case GET_AND_ADD_RELEASE:
                return (char) handle.getAndAddRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char getAndAddStaticFieldChar(VarHandle handle, AccessMode mode, char update) {
        switch (mode) {
            case GET_AND_ADD:
                return (char) handle.getAndAdd(update);
            case GET_AND_ADD_ACQUIRE:
                return (char) handle.getAndAddAcquire(update);
            case GET_AND_ADD_RELEASE:
                return (char) handle.getAndAddRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char getAndAddArrayElementChar(VarHandle handle, Holder holder, AccessMode mode, char update) {
        char[] array = (char[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(char.class, holder);
        switch (mode) {
            case GET_AND_ADD:
                return (char) handle.getAndAdd(array, 0, update);
            case GET_AND_ADD_ACQUIRE:
                return (char) handle.getAndAddAcquire(array, 0, update);
            case GET_AND_ADD_RELEASE:
                return (char) handle.getAndAddRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short getAndAddInstanceFieldShort(VarHandle handle, Holder holder, AccessMode mode, short update) {
        switch (mode) {
            case GET_AND_ADD:
                return (short) handle.getAndAdd(holder, update);
            case GET_AND_ADD_ACQUIRE:
                return (short) handle.getAndAddAcquire(holder, update);
            case GET_AND_ADD_RELEASE:
                return (short) handle.getAndAddRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short getAndAddStaticFieldShort(VarHandle handle, AccessMode mode, short update) {
        switch (mode) {
            case GET_AND_ADD:
                return (short) handle.getAndAdd(update);
            case GET_AND_ADD_ACQUIRE:
                return (short) handle.getAndAddAcquire(update);
            case GET_AND_ADD_RELEASE:
                return (short) handle.getAndAddRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short getAndAddArrayElementShort(VarHandle handle, Holder holder, AccessMode mode, short update) {
        short[] array = (short[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(short.class, holder);
        switch (mode) {
            case GET_AND_ADD:
                return (short) handle.getAndAdd(array, 0, update);
            case GET_AND_ADD_ACQUIRE:
                return (short) handle.getAndAddAcquire(array, 0, update);
            case GET_AND_ADD_RELEASE:
                return (short) handle.getAndAddRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getAndAddInstanceFieldInt(VarHandle handle, Holder holder, AccessMode mode, int update) {
        switch (mode) {
            case GET_AND_ADD:
                return (int) handle.getAndAdd(holder, update);
            case GET_AND_ADD_ACQUIRE:
                return (int) handle.getAndAddAcquire(holder, update);
            case GET_AND_ADD_RELEASE:
                return (int) handle.getAndAddRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getAndAddStaticFieldInt(VarHandle handle, AccessMode mode, int update) {
        switch (mode) {
            case GET_AND_ADD:
                return (int) handle.getAndAdd(update);
            case GET_AND_ADD_ACQUIRE:
                return (int) handle.getAndAddAcquire(update);
            case GET_AND_ADD_RELEASE:
                return (int) handle.getAndAddRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getAndAddArrayElementInt(VarHandle handle, Holder holder, AccessMode mode, int update) {
        int[] array = (int[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(int.class, holder);
        switch (mode) {
            case GET_AND_ADD:
                return (int) handle.getAndAdd(array, 0, update);
            case GET_AND_ADD_ACQUIRE:
                return (int) handle.getAndAddAcquire(array, 0, update);
            case GET_AND_ADD_RELEASE:
                return (int) handle.getAndAddRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long getAndAddInstanceFieldLong(VarHandle handle, Holder holder, AccessMode mode, long update) {
        switch (mode) {
            case GET_AND_ADD:
                return (long) handle.getAndAdd(holder, update);
            case GET_AND_ADD_ACQUIRE:
                return (long) handle.getAndAddAcquire(holder, update);
            case GET_AND_ADD_RELEASE:
                return (long) handle.getAndAddRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long getAndAddStaticFieldLong(VarHandle handle, AccessMode mode, long update) {
        switch (mode) {
            case GET_AND_ADD:
                return (long) handle.getAndAdd(update);
            case GET_AND_ADD_ACQUIRE:
                return (long) handle.getAndAddAcquire(update);
            case GET_AND_ADD_RELEASE:
                return (long) handle.getAndAddRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long getAndAddArrayElementLong(VarHandle handle, Holder holder, AccessMode mode, long update) {
        long[] array = (long[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(long.class, holder);
        switch (mode) {
            case GET_AND_ADD:
                return (long) handle.getAndAdd(array, 0, update);
            case GET_AND_ADD_ACQUIRE:
                return (long) handle.getAndAddAcquire(array, 0, update);
            case GET_AND_ADD_RELEASE:
                return (long) handle.getAndAddRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static float getAndAddInstanceFieldFloat(VarHandle handle, Holder holder, AccessMode mode, float update) {
        switch (mode) {
            case GET_AND_ADD:
                return (float) handle.getAndAdd(holder, update);
            case GET_AND_ADD_ACQUIRE:
                return (float) handle.getAndAddAcquire(holder, update);
            case GET_AND_ADD_RELEASE:
                return (float) handle.getAndAddRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static float getAndAddStaticFieldFloat(VarHandle handle, AccessMode mode, float update) {
        switch (mode) {
            case GET_AND_ADD:
                return (float) handle.getAndAdd(update);
            case GET_AND_ADD_ACQUIRE:
                return (float) handle.getAndAddAcquire(update);
            case GET_AND_ADD_RELEASE:
                return (float) handle.getAndAddRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static float getAndAddArrayElementFloat(VarHandle handle, Holder holder, AccessMode mode, float update) {
        float[] array = (float[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(float.class, holder);
        switch (mode) {
            case GET_AND_ADD:
                return (float) handle.getAndAdd(array, 0, update);
            case GET_AND_ADD_ACQUIRE:
                return (float) handle.getAndAddAcquire(array, 0, update);
            case GET_AND_ADD_RELEASE:
                return (float) handle.getAndAddRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double getAndAddInstanceFieldDouble(VarHandle handle, Holder holder, AccessMode mode, double update) {
        switch (mode) {
            case GET_AND_ADD:
                return (double) handle.getAndAdd(holder, update);
            case GET_AND_ADD_ACQUIRE:
                return (double) handle.getAndAddAcquire(holder, update);
            case GET_AND_ADD_RELEASE:
                return (double) handle.getAndAddRelease(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double getAndAddStaticFieldDouble(VarHandle handle, AccessMode mode, double update) {
        switch (mode) {
            case GET_AND_ADD:
                return (double) handle.getAndAdd(update);
            case GET_AND_ADD_ACQUIRE:
                return (double) handle.getAndAddAcquire(update);
            case GET_AND_ADD_RELEASE:
                return (double) handle.getAndAddRelease(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double getAndAddArrayElementDouble(VarHandle handle, Holder holder, AccessMode mode, double update) {
        double[] array = (double[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(double.class, holder);
        switch (mode) {
            case GET_AND_ADD:
                return (double) handle.getAndAdd(array, 0, update);
            case GET_AND_ADD_ACQUIRE:
                return (double) handle.getAndAddAcquire(array, 0, update);
            case GET_AND_ADD_RELEASE:
                return (double) handle.getAndAddRelease(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean getAndBitwiseInstanceFieldBoolean(
            VarHandle handle, Holder holder, AccessMode mode, boolean update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (boolean) handle.getAndBitwiseOr(holder, update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (boolean) handle.getAndBitwiseOrRelease(holder, update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (boolean) handle.getAndBitwiseOrAcquire(holder, update);
            case GET_AND_BITWISE_AND:
                return (boolean) handle.getAndBitwiseAnd(holder, update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (boolean) handle.getAndBitwiseAndRelease(holder, update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (boolean) handle.getAndBitwiseAndAcquire(holder, update);
            case GET_AND_BITWISE_XOR:
                return (boolean) handle.getAndBitwiseXor(holder, update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (boolean) handle.getAndBitwiseXorRelease(holder, update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (boolean) handle.getAndBitwiseXorAcquire(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean getAndBitwiseStaticFieldBoolean(VarHandle handle, AccessMode mode, boolean update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (boolean) handle.getAndBitwiseOr(update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (boolean) handle.getAndBitwiseOrRelease(update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (boolean) handle.getAndBitwiseOrAcquire(update);
            case GET_AND_BITWISE_AND:
                return (boolean) handle.getAndBitwiseAnd(update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (boolean) handle.getAndBitwiseAndRelease(update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (boolean) handle.getAndBitwiseAndAcquire(update);
            case GET_AND_BITWISE_XOR:
                return (boolean) handle.getAndBitwiseXor(update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (boolean) handle.getAndBitwiseXorRelease(update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (boolean) handle.getAndBitwiseXorAcquire(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean getAndBitwiseArrayElementBoolean(
            VarHandle handle, Holder holder, AccessMode mode, boolean update) {
        boolean[] array = (boolean[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(boolean.class, holder);
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (boolean) handle.getAndBitwiseOr(array, 0, update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (boolean) handle.getAndBitwiseOrRelease(array, 0, update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (boolean) handle.getAndBitwiseOrAcquire(array, 0, update);
            case GET_AND_BITWISE_AND:
                return (boolean) handle.getAndBitwiseAnd(array, 0, update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (boolean) handle.getAndBitwiseAndRelease(array, 0, update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (boolean) handle.getAndBitwiseAndAcquire(array, 0, update);
            case GET_AND_BITWISE_XOR:
                return (boolean) handle.getAndBitwiseXor(array, 0, update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (boolean) handle.getAndBitwiseXorRelease(array, 0, update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (boolean) handle.getAndBitwiseXorAcquire(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static boolean performBitwiseOperationBoolean(AccessMode mode, boolean original, boolean update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
            case GET_AND_BITWISE_OR_RELEASE:
            case GET_AND_BITWISE_OR_ACQUIRE:
                return original | update;
            case GET_AND_BITWISE_AND:
            case GET_AND_BITWISE_AND_RELEASE:
            case GET_AND_BITWISE_AND_ACQUIRE:
                return original & update;
            case GET_AND_BITWISE_XOR:
            case GET_AND_BITWISE_XOR_RELEASE:
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return original ^ update;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte getAndBitwiseInstanceFieldByte(VarHandle handle, Holder holder, AccessMode mode, byte update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (byte) handle.getAndBitwiseOr(holder, update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (byte) handle.getAndBitwiseOrRelease(holder, update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (byte) handle.getAndBitwiseOrAcquire(holder, update);
            case GET_AND_BITWISE_AND:
                return (byte) handle.getAndBitwiseAnd(holder, update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (byte) handle.getAndBitwiseAndRelease(holder, update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (byte) handle.getAndBitwiseAndAcquire(holder, update);
            case GET_AND_BITWISE_XOR:
                return (byte) handle.getAndBitwiseXor(holder, update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (byte) handle.getAndBitwiseXorRelease(holder, update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (byte) handle.getAndBitwiseXorAcquire(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte getAndBitwiseStaticFieldByte(VarHandle handle, AccessMode mode, byte update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (byte) handle.getAndBitwiseOr(update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (byte) handle.getAndBitwiseOrRelease(update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (byte) handle.getAndBitwiseOrAcquire(update);
            case GET_AND_BITWISE_AND:
                return (byte) handle.getAndBitwiseAnd(update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (byte) handle.getAndBitwiseAndRelease(update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (byte) handle.getAndBitwiseAndAcquire(update);
            case GET_AND_BITWISE_XOR:
                return (byte) handle.getAndBitwiseXor(update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (byte) handle.getAndBitwiseXorRelease(update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (byte) handle.getAndBitwiseXorAcquire(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte getAndBitwiseArrayElementByte(VarHandle handle, Holder holder, AccessMode mode, byte update) {
        byte[] array = (byte[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(byte.class, holder);
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (byte) handle.getAndBitwiseOr(array, 0, update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (byte) handle.getAndBitwiseOrRelease(array, 0, update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (byte) handle.getAndBitwiseOrAcquire(array, 0, update);
            case GET_AND_BITWISE_AND:
                return (byte) handle.getAndBitwiseAnd(array, 0, update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (byte) handle.getAndBitwiseAndRelease(array, 0, update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (byte) handle.getAndBitwiseAndAcquire(array, 0, update);
            case GET_AND_BITWISE_XOR:
                return (byte) handle.getAndBitwiseXor(array, 0, update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (byte) handle.getAndBitwiseXorRelease(array, 0, update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (byte) handle.getAndBitwiseXorAcquire(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte performBitwiseOperationByte(AccessMode mode, byte original, byte update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
            case GET_AND_BITWISE_OR_RELEASE:
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (byte) (original | update);
            case GET_AND_BITWISE_AND:
            case GET_AND_BITWISE_AND_RELEASE:
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (byte) (original & update);
            case GET_AND_BITWISE_XOR:
            case GET_AND_BITWISE_XOR_RELEASE:
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (byte) (original ^ update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char getAndBitwiseInstanceFieldChar(VarHandle handle, Holder holder, AccessMode mode, char update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (char) handle.getAndBitwiseOr(holder, update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (char) handle.getAndBitwiseOrRelease(holder, update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (char) handle.getAndBitwiseOrAcquire(holder, update);
            case GET_AND_BITWISE_AND:
                return (char) handle.getAndBitwiseAnd(holder, update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (char) handle.getAndBitwiseAndRelease(holder, update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (char) handle.getAndBitwiseAndAcquire(holder, update);
            case GET_AND_BITWISE_XOR:
                return (char) handle.getAndBitwiseXor(holder, update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (char) handle.getAndBitwiseXorRelease(holder, update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (char) handle.getAndBitwiseXorAcquire(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char getAndBitwiseStaticFieldChar(VarHandle handle, AccessMode mode, char update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (char) handle.getAndBitwiseOr(update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (char) handle.getAndBitwiseOrRelease(update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (char) handle.getAndBitwiseOrAcquire(update);
            case GET_AND_BITWISE_AND:
                return (char) handle.getAndBitwiseAnd(update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (char) handle.getAndBitwiseAndRelease(update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (char) handle.getAndBitwiseAndAcquire(update);
            case GET_AND_BITWISE_XOR:
                return (char) handle.getAndBitwiseXor(update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (char) handle.getAndBitwiseXorRelease(update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (char) handle.getAndBitwiseXorAcquire(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char getAndBitwiseArrayElementChar(VarHandle handle, Holder holder, AccessMode mode, char update) {
        char[] array = (char[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(char.class, holder);
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (char) handle.getAndBitwiseOr(array, 0, update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (char) handle.getAndBitwiseOrRelease(array, 0, update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (char) handle.getAndBitwiseOrAcquire(array, 0, update);
            case GET_AND_BITWISE_AND:
                return (char) handle.getAndBitwiseAnd(array, 0, update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (char) handle.getAndBitwiseAndRelease(array, 0, update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (char) handle.getAndBitwiseAndAcquire(array, 0, update);
            case GET_AND_BITWISE_XOR:
                return (char) handle.getAndBitwiseXor(array, 0, update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (char) handle.getAndBitwiseXorRelease(array, 0, update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (char) handle.getAndBitwiseXorAcquire(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static char performBitwiseOperationChar(AccessMode mode, char original, char update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
            case GET_AND_BITWISE_OR_RELEASE:
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (char) (original | update);
            case GET_AND_BITWISE_AND:
            case GET_AND_BITWISE_AND_RELEASE:
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (char) (original & update);
            case GET_AND_BITWISE_XOR:
            case GET_AND_BITWISE_XOR_RELEASE:
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (char) (original ^ update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short getAndBitwiseInstanceFieldShort(
            VarHandle handle, Holder holder, AccessMode mode, short update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (short) handle.getAndBitwiseOr(holder, update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (short) handle.getAndBitwiseOrRelease(holder, update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (short) handle.getAndBitwiseOrAcquire(holder, update);
            case GET_AND_BITWISE_AND:
                return (short) handle.getAndBitwiseAnd(holder, update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (short) handle.getAndBitwiseAndRelease(holder, update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (short) handle.getAndBitwiseAndAcquire(holder, update);
            case GET_AND_BITWISE_XOR:
                return (short) handle.getAndBitwiseXor(holder, update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (short) handle.getAndBitwiseXorRelease(holder, update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (short) handle.getAndBitwiseXorAcquire(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short getAndBitwiseStaticFieldShort(VarHandle handle, AccessMode mode, short update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (short) handle.getAndBitwiseOr(update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (short) handle.getAndBitwiseOrRelease(update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (short) handle.getAndBitwiseOrAcquire(update);
            case GET_AND_BITWISE_AND:
                return (short) handle.getAndBitwiseAnd(update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (short) handle.getAndBitwiseAndRelease(update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (short) handle.getAndBitwiseAndAcquire(update);
            case GET_AND_BITWISE_XOR:
                return (short) handle.getAndBitwiseXor(update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (short) handle.getAndBitwiseXorRelease(update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (short) handle.getAndBitwiseXorAcquire(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short getAndBitwiseArrayElementShort(VarHandle handle, Holder holder, AccessMode mode, short update) {
        short[] array = (short[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(short.class, holder);
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (short) handle.getAndBitwiseOr(array, 0, update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (short) handle.getAndBitwiseOrRelease(array, 0, update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (short) handle.getAndBitwiseOrAcquire(array, 0, update);
            case GET_AND_BITWISE_AND:
                return (short) handle.getAndBitwiseAnd(array, 0, update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (short) handle.getAndBitwiseAndRelease(array, 0, update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (short) handle.getAndBitwiseAndAcquire(array, 0, update);
            case GET_AND_BITWISE_XOR:
                return (short) handle.getAndBitwiseXor(array, 0, update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (short) handle.getAndBitwiseXorRelease(array, 0, update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (short) handle.getAndBitwiseXorAcquire(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static short performBitwiseOperationShort(AccessMode mode, short original, short update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
            case GET_AND_BITWISE_OR_RELEASE:
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (short) (original | update);
            case GET_AND_BITWISE_AND:
            case GET_AND_BITWISE_AND_RELEASE:
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (short) (original & update);
            case GET_AND_BITWISE_XOR:
            case GET_AND_BITWISE_XOR_RELEASE:
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (short) (original ^ update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getAndBitwiseInstanceFieldInt(VarHandle handle, Holder holder, AccessMode mode, int update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (int) handle.getAndBitwiseOr(holder, update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (int) handle.getAndBitwiseOrRelease(holder, update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (int) handle.getAndBitwiseOrAcquire(holder, update);
            case GET_AND_BITWISE_AND:
                return (int) handle.getAndBitwiseAnd(holder, update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (int) handle.getAndBitwiseAndRelease(holder, update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (int) handle.getAndBitwiseAndAcquire(holder, update);
            case GET_AND_BITWISE_XOR:
                return (int) handle.getAndBitwiseXor(holder, update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (int) handle.getAndBitwiseXorRelease(holder, update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (int) handle.getAndBitwiseXorAcquire(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getAndBitwiseStaticFieldInt(VarHandle handle, AccessMode mode, int update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (int) handle.getAndBitwiseOr(update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (int) handle.getAndBitwiseOrRelease(update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (int) handle.getAndBitwiseOrAcquire(update);
            case GET_AND_BITWISE_AND:
                return (int) handle.getAndBitwiseAnd(update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (int) handle.getAndBitwiseAndRelease(update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (int) handle.getAndBitwiseAndAcquire(update);
            case GET_AND_BITWISE_XOR:
                return (int) handle.getAndBitwiseXor(update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (int) handle.getAndBitwiseXorRelease(update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (int) handle.getAndBitwiseXorAcquire(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getAndBitwiseArrayElementInt(VarHandle handle, Holder holder, AccessMode mode, int update) {
        int[] array = (int[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(int.class, holder);
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (int) handle.getAndBitwiseOr(array, 0, update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (int) handle.getAndBitwiseOrRelease(array, 0, update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (int) handle.getAndBitwiseOrAcquire(array, 0, update);
            case GET_AND_BITWISE_AND:
                return (int) handle.getAndBitwiseAnd(array, 0, update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (int) handle.getAndBitwiseAndRelease(array, 0, update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (int) handle.getAndBitwiseAndAcquire(array, 0, update);
            case GET_AND_BITWISE_XOR:
                return (int) handle.getAndBitwiseXor(array, 0, update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (int) handle.getAndBitwiseXorRelease(array, 0, update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (int) handle.getAndBitwiseXorAcquire(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int performBitwiseOperationInt(AccessMode mode, int original, int update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
            case GET_AND_BITWISE_OR_RELEASE:
            case GET_AND_BITWISE_OR_ACQUIRE:
                return original | update;
            case GET_AND_BITWISE_AND:
            case GET_AND_BITWISE_AND_RELEASE:
            case GET_AND_BITWISE_AND_ACQUIRE:
                return original & update;
            case GET_AND_BITWISE_XOR:
            case GET_AND_BITWISE_XOR_RELEASE:
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return original ^ update;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long getAndBitwiseInstanceFieldLong(VarHandle handle, Holder holder, AccessMode mode, long update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (long) handle.getAndBitwiseOr(holder, update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (long) handle.getAndBitwiseOrRelease(holder, update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (long) handle.getAndBitwiseOrAcquire(holder, update);
            case GET_AND_BITWISE_AND:
                return (long) handle.getAndBitwiseAnd(holder, update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (long) handle.getAndBitwiseAndRelease(holder, update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (long) handle.getAndBitwiseAndAcquire(holder, update);
            case GET_AND_BITWISE_XOR:
                return (long) handle.getAndBitwiseXor(holder, update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (long) handle.getAndBitwiseXorRelease(holder, update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (long) handle.getAndBitwiseXorAcquire(holder, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long getAndBitwiseStaticFieldLong(VarHandle handle, AccessMode mode, long update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (long) handle.getAndBitwiseOr(update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (long) handle.getAndBitwiseOrRelease(update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (long) handle.getAndBitwiseOrAcquire(update);
            case GET_AND_BITWISE_AND:
                return (long) handle.getAndBitwiseAnd(update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (long) handle.getAndBitwiseAndRelease(update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (long) handle.getAndBitwiseAndAcquire(update);
            case GET_AND_BITWISE_XOR:
                return (long) handle.getAndBitwiseXor(update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (long) handle.getAndBitwiseXorRelease(update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (long) handle.getAndBitwiseXorAcquire(update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long getAndBitwiseArrayElementLong(VarHandle handle, Holder holder, AccessMode mode, long update) {
        long[] array = (long[]) HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(long.class, holder);
        switch (mode) {
            case GET_AND_BITWISE_OR:
                return (long) handle.getAndBitwiseOr(array, 0, update);
            case GET_AND_BITWISE_OR_RELEASE:
                return (long) handle.getAndBitwiseOrRelease(array, 0, update);
            case GET_AND_BITWISE_OR_ACQUIRE:
                return (long) handle.getAndBitwiseOrAcquire(array, 0, update);
            case GET_AND_BITWISE_AND:
                return (long) handle.getAndBitwiseAnd(array, 0, update);
            case GET_AND_BITWISE_AND_RELEASE:
                return (long) handle.getAndBitwiseAndRelease(array, 0, update);
            case GET_AND_BITWISE_AND_ACQUIRE:
                return (long) handle.getAndBitwiseAndAcquire(array, 0, update);
            case GET_AND_BITWISE_XOR:
                return (long) handle.getAndBitwiseXor(array, 0, update);
            case GET_AND_BITWISE_XOR_RELEASE:
                return (long) handle.getAndBitwiseXorRelease(array, 0, update);
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return (long) handle.getAndBitwiseXorAcquire(array, 0, update);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static long performBitwiseOperationLong(AccessMode mode, long original, long update) {
        switch (mode) {
            case GET_AND_BITWISE_OR:
            case GET_AND_BITWISE_OR_RELEASE:
            case GET_AND_BITWISE_OR_ACQUIRE:
                return original | update;
            case GET_AND_BITWISE_AND:
            case GET_AND_BITWISE_AND_RELEASE:
            case GET_AND_BITWISE_AND_ACQUIRE:
                return original & update;
            case GET_AND_BITWISE_XOR:
            case GET_AND_BITWISE_XOR_RELEASE:
            case GET_AND_BITWISE_XOR_ACQUIRE:
                return original ^ update;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static VarHandle getVarHandle(VariableLocation location, MethodHandles.Lookup lookup, Class<?> type)
            throws ReflectiveOperationException {
        switch (location) {
            case STATIC_FIELD:
                return lookup.findStaticVarHandle(
                        Holder.class, location.getCategory().getFieldName(type), type);
            case INSTANCE_FIELD:
                return lookup.findVarHandle(Holder.class, location.getCategory().getFieldName(type), type);
            case ARRAY_ELEMENT:
                return MethodHandles.arrayElementVarHandle(
                        location.getCategory().getFieldType(type));
            default:
                throw new AssertionError();
        }
    }
}
