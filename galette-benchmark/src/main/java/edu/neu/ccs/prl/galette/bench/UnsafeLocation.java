package edu.neu.ccs.prl.galette.bench;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

enum UnsafeLocation {
    STATIC_FIELD {
        @Override
        long getOffset(UnsafeWrapper unsafe, Class<?> type) throws NoSuchFieldException {
            String name = Holder.getBasicName(type) + "s";
            Field f = Holder.class.getDeclaredField(name);
            return unsafe.staticFieldOffset(f);
        }

        @Override
        Object getBase(UnsafeWrapper unsafe, Holder holder, Class<?> type) throws NoSuchFieldException {
            return unsafe.staticFieldBase(Holder.class.getDeclaredField(getFieldName(type)));
        }

        @Override
        boolean getBoolean(Holder holder) {
            return holder.za[0];
        }

        @Override
        byte getByte(Holder holder) {
            return Holder.bs;
        }

        @Override
        char getChar(Holder holder) {
            return Holder.cs;
        }

        @Override
        double getDouble(Holder holder) {
            return Holder.ds;
        }

        @Override
        float getFloat(Holder holder) {
            return Holder.fs;
        }

        @Override
        int getInt(Holder holder) {
            return Holder.is;
        }

        @Override
        long getLong(Holder holder) {
            return Holder.js;
        }

        @Override
        short getShort(Holder holder) {
            return Holder.ss;
        }

        @Override
        Object getObject(Holder holder) {
            return Holder.ls;
        }

        @Override
        String getFieldName(Class<?> type) {
            return Holder.getBasicName(type) + "s";
        }
    },
    INSTANCE_FIELD {
        @Override
        long getOffset(UnsafeWrapper unsafe, Class<?> type) throws NoSuchFieldException {
            return unsafe.objectFieldOffset(Holder.class.getDeclaredField(getFieldName(type)));
        }

        @Override
        Object getBase(UnsafeWrapper unsafe, Holder holder, Class<?> type) {
            return holder;
        }

        @Override
        boolean getBoolean(Holder holder) {
            return holder.z;
        }

        @Override
        byte getByte(Holder holder) {
            return holder.b;
        }

        @Override
        char getChar(Holder holder) {
            return holder.c;
        }

        @Override
        double getDouble(Holder holder) {
            return holder.d;
        }

        @Override
        float getFloat(Holder holder) {
            return holder.f;
        }

        @Override
        int getInt(Holder holder) {
            return holder.i;
        }

        @Override
        long getLong(Holder holder) {
            return holder.j;
        }

        @Override
        short getShort(Holder holder) {
            return holder.s;
        }

        @Override
        Object getObject(Holder holder) {
            return holder.l;
        }

        @Override
        String getFieldName(Class<?> type) {
            return Holder.getBasicName(type);
        }
    },
    ARRAY_ELEMENT {
        @Override
        long getOffset(UnsafeWrapper unsafe, Class<?> type) {
            Class<?> arrayType = Array.newInstance(type, 0).getClass();
            return unsafe.arrayBaseOffset(arrayType);
        }

        @Override
        Object getBase(UnsafeWrapper unsafe, Holder holder, Class<?> type) {
            if (type == Integer.TYPE) {
                return holder.ia;
            } else if (type == Boolean.TYPE) {
                return holder.za;
            } else if (type == Byte.TYPE) {
                return holder.ba;
            } else if (type == Character.TYPE) {
                return holder.ca;
            } else if (type == Short.TYPE) {
                return holder.sa;
            } else if (type == Double.TYPE) {
                return holder.da;
            } else if (type == Float.TYPE) {
                return holder.fa;
            } else if (type == Long.TYPE) {
                return holder.ja;
            } else if (type == Object.class) {
                return holder.la;
            } else {
                throw new AssertionError();
            }
        }

        @Override
        boolean getBoolean(Holder holder) {
            return holder.za[0];
        }

        @Override
        byte getByte(Holder holder) {
            return holder.ba[0];
        }

        @Override
        char getChar(Holder holder) {
            return holder.ca[0];
        }

        @Override
        double getDouble(Holder holder) {
            return holder.da[0];
        }

        @Override
        float getFloat(Holder holder) {
            return holder.fa[0];
        }

        @Override
        int getInt(Holder holder) {
            return holder.ia[0];
        }

        @Override
        long getLong(Holder holder) {
            return holder.ja[0];
        }

        @Override
        short getShort(Holder holder) {
            return holder.sa[0];
        }

        @Override
        Object getObject(Holder holder) {
            return holder.la[0];
        }

        @Override
        String getFieldName(Class<?> type) {
            return Holder.getBasicName(type) + "a";
        }
    };

    abstract long getOffset(UnsafeWrapper unsafe, Class<?> type) throws ReflectiveOperationException;

    abstract Object getBase(UnsafeWrapper unsafe, Holder holder, Class<?> type) throws NoSuchFieldException;

    abstract boolean getBoolean(Holder holder);

    abstract byte getByte(Holder holder);

    abstract char getChar(Holder holder);

    abstract double getDouble(Holder holder);

    abstract float getFloat(Holder holder);

    abstract int getInt(Holder holder);

    abstract long getLong(Holder holder);

    abstract short getShort(Holder holder);

    abstract Object getObject(Holder holder);

    abstract String getFieldName(Class<?> type);

    public Object[] getExpectedLabels(boolean taintedValue, Class<?> type) {
        return taintedValue ? new Object[] {getFieldName(type)} : new Object[0];
    }
}
