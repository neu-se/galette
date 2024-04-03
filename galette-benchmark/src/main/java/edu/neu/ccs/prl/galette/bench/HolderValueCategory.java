package edu.neu.ccs.prl.galette.bench;

import edu.neu.ccs.prl.galette.bench.extension.TagManager;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

enum HolderValueCategory {
    BASIC_STATIC {
        @Override
        Class<?> getFieldType(Class<?> baseType) {
            return baseType;
        }

        @Override
        String suffix() {
            return "s";
        }

        @Override
        boolean getBoolean(Holder holder) {
            return Holder.zs;
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
        Object getValue(Class<?> baseType, Holder holder) {
            if (baseType == Integer.TYPE) {
                return Holder.is;
            } else if (baseType == Boolean.TYPE) {
                return Holder.zs;
            } else if (baseType == Byte.TYPE) {
                return Holder.bs;
            } else if (baseType == Character.TYPE) {
                return Holder.cs;
            } else if (baseType == Short.TYPE) {
                return Holder.ss;
            } else if (baseType == Double.TYPE) {
                return Holder.ds;
            } else if (baseType == Float.TYPE) {
                return Holder.fs;
            } else if (baseType == Long.TYPE) {
                return Holder.js;
            } else if (baseType == Object.class) {
                return Holder.ls;
            } else {
                throw new IllegalArgumentException();
            }
        }
    },
    BASIC {
        @Override
        Class<?> getFieldType(Class<?> baseType) {
            return baseType;
        }

        @Override
        String suffix() {
            return "";
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
        Object getValue(Class<?> baseType, Holder holder) {
            if (baseType == Integer.TYPE) {
                return holder.i;
            } else if (baseType == Boolean.TYPE) {
                return holder.z;
            } else if (baseType == Byte.TYPE) {
                return holder.b;
            } else if (baseType == Character.TYPE) {
                return holder.c;
            } else if (baseType == Short.TYPE) {
                return holder.s;
            } else if (baseType == Double.TYPE) {
                return holder.d;
            } else if (baseType == Float.TYPE) {
                return holder.f;
            } else if (baseType == Long.TYPE) {
                return holder.j;
            } else if (baseType == Object.class) {
                return holder.l;
            } else {
                throw new IllegalArgumentException();
            }
        }
    },
    BOXED {
        @Override
        Class<?> getFieldType(Class<?> baseType) {
            return MethodType.methodType(baseType).wrap().returnType();
        }

        @Override
        String suffix() {
            return "w";
        }

        @Override
        boolean getBoolean(Holder holder) {
            return holder.zw;
        }

        @Override
        byte getByte(Holder holder) {
            return holder.bw;
        }

        @Override
        char getChar(Holder holder) {
            return holder.cw;
        }

        @Override
        double getDouble(Holder holder) {
            return holder.dw;
        }

        @Override
        float getFloat(Holder holder) {
            return holder.fw;
        }

        @Override
        int getInt(Holder holder) {
            return holder.iw;
        }

        @Override
        long getLong(Holder holder) {
            return holder.jw;
        }

        @Override
        short getShort(Holder holder) {
            return holder.sw;
        }

        @Override
        Object getObject(Holder holder) {
            throw new UnsupportedOperationException();
        }

        @Override
        Object getValue(Class<?> baseType, Holder holder) {
            if (baseType == Integer.TYPE) {
                return holder.iw;
            } else if (baseType == Boolean.TYPE) {
                return holder.zw;
            } else if (baseType == Byte.TYPE) {
                return holder.bw;
            } else if (baseType == Character.TYPE) {
                return holder.cw;
            } else if (baseType == Short.TYPE) {
                return holder.sw;
            } else if (baseType == Double.TYPE) {
                return holder.dw;
            } else if (baseType == Float.TYPE) {
                return holder.fw;
            } else if (baseType == Long.TYPE) {
                return holder.jw;
            } else if (baseType == Object.class) {
                throw new UnsupportedOperationException();
            } else {
                throw new IllegalArgumentException();
            }
        }
    },
    ONE_DIMENSIONAL_ARRAY {
        @Override
        Class<?> getFieldType(Class<?> baseType) {
            return Array.newInstance(baseType, 0).getClass();
        }

        @Override
        String suffix() {
            return "a";
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
        Object getValue(Class<?> baseType, Holder holder) {
            if (baseType == Integer.TYPE) {
                return holder.ia;
            } else if (baseType == Boolean.TYPE) {
                return holder.za;
            } else if (baseType == Byte.TYPE) {
                return holder.ba;
            } else if (baseType == Character.TYPE) {
                return holder.ca;
            } else if (baseType == Short.TYPE) {
                return holder.sa;
            } else if (baseType == Double.TYPE) {
                return holder.da;
            } else if (baseType == Float.TYPE) {
                return holder.fa;
            } else if (baseType == Long.TYPE) {
                return holder.ja;
            } else if (baseType == Object.class) {
                return holder.la;
            } else {
                throw new IllegalArgumentException();
            }
        }
    },
    TWO_DIMENSIONAL_ARRAY {
        @Override
        Class<?> getFieldType(Class<?> baseType) {
            return Array.newInstance(ONE_DIMENSIONAL_ARRAY.getFieldType(baseType), 0)
                    .getClass();
        }

        @Override
        String suffix() {
            return "aa";
        }

        @Override
        boolean getBoolean(Holder holder) {
            return holder.zaa[0][0];
        }

        @Override
        byte getByte(Holder holder) {
            return holder.baa[0][0];
        }

        @Override
        char getChar(Holder holder) {
            return holder.caa[0][0];
        }

        @Override
        double getDouble(Holder holder) {
            return holder.daa[0][0];
        }

        @Override
        float getFloat(Holder holder) {
            return holder.faa[0][0];
        }

        @Override
        int getInt(Holder holder) {
            return holder.iaa[0][0];
        }

        @Override
        long getLong(Holder holder) {
            return holder.jaa[0][0];
        }

        @Override
        short getShort(Holder holder) {
            return holder.saa[0][0];
        }

        @Override
        Object getObject(Holder holder) {
            return holder.laa[0][0];
        }

        @Override
        Object getValue(Class<?> baseType, Holder holder) {
            if (baseType == Integer.TYPE) {
                return holder.iaa;
            } else if (baseType == Boolean.TYPE) {
                return holder.zaa;
            } else if (baseType == Byte.TYPE) {
                return holder.baa;
            } else if (baseType == Character.TYPE) {
                return holder.caa;
            } else if (baseType == Short.TYPE) {
                return holder.saa;
            } else if (baseType == Double.TYPE) {
                return holder.daa;
            } else if (baseType == Float.TYPE) {
                return holder.faa;
            } else if (baseType == Long.TYPE) {
                return holder.jaa;
            } else if (baseType == Object.class) {
                return holder.laa;
            } else {
                throw new IllegalArgumentException();
            }
        }
    },
    OBJECT_HOLDING_ONE_DIMENSIONAL_ARRAY {
        @Override
        Class<?> getFieldType(Class<?> baseType) {
            return Object.class;
        }

        @Override
        String suffix() {
            return "ao";
        }

        @Override
        boolean getBoolean(Holder holder) {
            return ((boolean[]) holder.zao)[0];
        }

        @Override
        byte getByte(Holder holder) {
            return ((byte[]) holder.bao)[0];
        }

        @Override
        char getChar(Holder holder) {
            return ((char[]) holder.cao)[0];
        }

        @Override
        double getDouble(Holder holder) {
            return ((double[]) holder.dao)[0];
        }

        @Override
        float getFloat(Holder holder) {
            return ((float[]) holder.fao)[0];
        }

        @Override
        int getInt(Holder holder) {
            return ((int[]) holder.iao)[0];
        }

        @Override
        long getLong(Holder holder) {
            return ((long[]) holder.jao)[0];
        }

        @Override
        short getShort(Holder holder) {
            return ((short[]) holder.sao)[0];
        }

        @Override
        Object getObject(Holder holder) {
            return ((Object[]) holder.lao)[0];
        }

        @Override
        Object getValue(Class<?> baseType, Holder holder) {
            if (baseType == Integer.TYPE) {
                return holder.iao;
            } else if (baseType == Boolean.TYPE) {
                return holder.zao;
            } else if (baseType == Byte.TYPE) {
                return holder.bao;
            } else if (baseType == Character.TYPE) {
                return holder.cao;
            } else if (baseType == Short.TYPE) {
                return holder.sao;
            } else if (baseType == Double.TYPE) {
                return holder.dao;
            } else if (baseType == Float.TYPE) {
                return holder.fao;
            } else if (baseType == Long.TYPE) {
                return holder.jao;
            } else if (baseType == Object.class) {
                return holder.lao;
            } else {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public Constructor<Holder> getConstructor(Class<?> baseType) throws NoSuchMethodException {
            return Holder.class.getDeclaredConstructor(baseType, Object.class);
        }
    };

    public Method getter(Class<?> baseType) throws NoSuchMethodException {
        return Holder.class.getDeclaredMethod(
                "get" + Holder.getBasicName(baseType).toUpperCase() + suffix());
    }

    public Method setter(Class<?> baseType) throws NoSuchMethodException {
        return Holder.class.getDeclaredMethod(
                "set" + Holder.getBasicName(baseType).toUpperCase() + suffix(), getFieldType(baseType));
    }

    abstract Class<?> getFieldType(Class<?> baseType);

    abstract String suffix();

    abstract boolean getBoolean(Holder holder);

    abstract byte getByte(Holder holder);

    abstract char getChar(Holder holder);

    abstract double getDouble(Holder holder);

    abstract float getFloat(Holder holder);

    abstract int getInt(Holder holder);

    abstract long getLong(Holder holder);

    abstract short getShort(Holder holder);

    abstract Object getObject(Holder holder);

    abstract Object getValue(Class<?> baseType, Holder holder);

    public Constructor<Holder> getConstructor(Class<?> baseType) throws NoSuchMethodException {
        Class<?> fieldType = getFieldType(baseType);
        return Holder.class.getDeclaredConstructor(fieldType);
    }

    public Object[] getLabels(Class<?> baseType) {
        return new Object[] {Holder.getBasicName(baseType) + suffix()};
    }

    public Object[] getElementLabels(Class<?> baseType, Holder holder, TagManager manager) {
        if (baseType == Integer.TYPE) {
            return manager.getLabels(getInt(holder));
        } else if (baseType == Boolean.TYPE) {
            return manager.getLabels(getBoolean(holder));
        } else if (baseType == Byte.TYPE) {
            return manager.getLabels(getByte(holder));
        } else if (baseType == Character.TYPE) {
            return manager.getLabels(getChar(holder));
        } else if (baseType == Short.TYPE) {
            return manager.getLabels(getShort(holder));
        } else if (baseType == Double.TYPE) {
            return manager.getLabels(getDouble(holder));
        } else if (baseType == Float.TYPE) {
            return manager.getLabels(getFloat(holder));
        } else if (baseType == Long.TYPE) {
            return manager.getLabels(getLong(holder));
        } else if (baseType == Object.class) {
            return manager.getLabels(getObject(holder));
        } else {
            throw new AssertionError();
        }
    }

    public String getFieldName(Class<?> baseType) {
        return Holder.getBasicName(baseType) + suffix();
    }

    public Field getField(Class<?> baseType) throws NoSuchFieldException {
        return Holder.class.getDeclaredField(getFieldName(baseType));
    }
}
