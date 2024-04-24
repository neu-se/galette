package edu.neu.ccs.prl.galette.bench;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public enum VariableLocation {
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
    };

    private final HolderValueCategory category;

    VariableLocation(HolderValueCategory category) {
        this.category = category;
    }

    abstract long getOffset(UnsafeAdapter unsafe, Class<?> type) throws ReflectiveOperationException;

    abstract Object getBase(UnsafeAdapter unsafe, Holder holder, Class<?> type) throws NoSuchFieldException;

    public HolderValueCategory getCategory() {
        return category;
    }

    public Object[] getExpectedLabels(boolean taintedValue, Class<?> type) {
        return taintedValue ? category.getLabels(type) : new Object[0];
    }
}
