package edu.neu.ccs.prl.galette.bench;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

enum VariableLocation {
    STATIC_FIELD(HolderValueCategory.BASIC_STATIC) {
        @Override
        long getOffset(UnsafeAdapter unsafe, Class<?> type) throws NoSuchFieldException {
            String name = Holder.getBasicName(type) + "s";
            Field f = Holder.class.getDeclaredField(name);
            return unsafe.staticFieldOffset(f);
        }

        @Override
        Object getBase(UnsafeAdapter unsafe, Holder holder, Class<?> type) throws NoSuchFieldException {
            return unsafe.staticFieldBase(
                    Holder.class.getDeclaredField(getCategory().getFieldName(type)));
        }

        @Override
        public VarHandle getVarHandle(MethodHandles.Lookup lookup, Class<?> type) throws ReflectiveOperationException {
            String fieldName = getCategory().getFieldName(type);
            return lookup.findStaticVarHandle(Holder.class, fieldName, type);
        }
    },
    INSTANCE_FIELD(HolderValueCategory.BASIC) {
        @Override
        long getOffset(UnsafeAdapter unsafe, Class<?> type) throws NoSuchFieldException {
            return unsafe.objectFieldOffset(
                    Holder.class.getDeclaredField(getCategory().getFieldName(type)));
        }

        @Override
        Object getBase(UnsafeAdapter unsafe, Holder holder, Class<?> type) {
            return holder;
        }

        @Override
        public VarHandle getVarHandle(MethodHandles.Lookup lookup, Class<?> type) throws ReflectiveOperationException {
            String fieldName = getCategory().getFieldName(type);
            return lookup.findVarHandle(Holder.class, fieldName, type);
        }
    },
    ARRAY_ELEMENT(HolderValueCategory.ONE_DIMENSIONAL_ARRAY) {
        @Override
        long getOffset(UnsafeAdapter unsafe, Class<?> type) {
            Class<?> arrayType = Array.newInstance(type, 0).getClass();
            return unsafe.arrayBaseOffset(arrayType);
        }

        @Override
        Object getBase(UnsafeAdapter unsafe, Holder holder, Class<?> type) {
            return HolderValueCategory.ONE_DIMENSIONAL_ARRAY.getValue(type, holder);
        }

        @Override
        public VarHandle getVarHandle(MethodHandles.Lookup lookup, Class<?> type) {
            return MethodHandles.arrayElementVarHandle(getCategory().getFieldType(type));
        }
    };

    private final HolderValueCategory category;

    VariableLocation(HolderValueCategory category) {
        this.category = category;
    }

    abstract long getOffset(UnsafeAdapter unsafe, Class<?> type) throws ReflectiveOperationException;

    abstract Object getBase(UnsafeAdapter unsafe, Holder holder, Class<?> type) throws NoSuchFieldException;

    public abstract VarHandle getVarHandle(MethodHandles.Lookup lookup, Class<?> type)
            throws ReflectiveOperationException;

    public HolderValueCategory getCategory() {
        return category;
    }

    public Object[] getExpectedLabels(boolean taintedValue, Class<?> type) {
        return taintedValue ? category.getLabels(type) : new Object[0];
    }
}
