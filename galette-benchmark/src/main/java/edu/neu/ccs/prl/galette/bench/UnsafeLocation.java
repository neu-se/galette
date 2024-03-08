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
            String name = Holder.getBasicName(type) + "s";
            Field f = Holder.class.getDeclaredField(name);
            return unsafe.staticFieldBase(f);
        }

        @Override
        int getIntValue(Holder holder) {
            return Holder.is;
        }
    },
    INSTANCE_FIELD {
        @Override
        long getOffset(UnsafeWrapper unsafe, Class<?> type) throws NoSuchFieldException {
            String name = Holder.getBasicName(type);
            Field f = Holder.class.getDeclaredField(name);
            return unsafe.objectFieldOffset(f);
        }

        @Override
        Object getBase(UnsafeWrapper unsafe, Holder holder, Class<?> type) {
            return holder;
        }

        @Override
        int getIntValue(Holder holder) {
            return holder.i;
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
        int getIntValue(Holder holder) {
            return holder.ia[0];
        }
    };

    abstract long getOffset(UnsafeWrapper unsafe, Class<?> type) throws ReflectiveOperationException;

    abstract Object getBase(UnsafeWrapper unsafe, Holder holder, Class<?> type) throws NoSuchFieldException;

    abstract int getIntValue(Holder holder);
}
