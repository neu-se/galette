package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.TaggedObject;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public final class ClassMasks {
    @Mask(
            owner = "java/lang/Class",
            name = "getFields",
            parametersDescriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)",
            type = MaskType.REPAIR_RETURN)
    @Mask(
            owner = "java/lang/Class",
            name = "getDeclaredFields",
            parametersDescriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)",
            type = MaskType.REPAIR_RETURN)
    public static Field[] getFields(Field[] fields) {
        SimpleList<Field> result = new SimpleList<>();
        for (Field f : fields) {
            if (!f.getName().startsWith(GaletteTransformer.ADDED_MEMBER_PREFIX)) {
                result.add(f);
            }
        }
        return result.toArray(new Field[result.size()]);
    }

    @Mask(
            owner = "java/lang/Class",
            name = "getMethods",
            parametersDescriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)",
            type = MaskType.REPAIR_RETURN)
    @Mask(
            owner = "java/lang/Class",
            name = "getDeclaredMethods",
            parametersDescriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)",
            type = MaskType.REPAIR_RETURN)
    public static Method[] getMethods(Method[] methods) {
        SimpleList<Method> result = new SimpleList<>();
        for (Method m : methods) {
            if (!isGaletteAdded(m.getParameterTypes())) {
                result.add(m);
            }
        }
        return result.toArray(new Method[result.size()]);
    }

    @Mask(
            owner = "java/lang/Class",
            name = "getConstructors",
            parametersDescriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)",
            type = MaskType.REPAIR_RETURN)
    @Mask(
            owner = "java/lang/Class",
            name = "getDeclaredConstructors",
            parametersDescriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)",
            type = MaskType.REPAIR_RETURN)
    public static Constructor<?>[] getConstructors(Constructor<?>[] constructors) {
        SimpleList<Constructor<?>> result = new SimpleList<>();
        for (Constructor<?> c : constructors) {
            if (!isGaletteAdded(c.getParameterTypes())) {
                result.add(c);
            }
        }
        return result.toArray(new Constructor[result.size()]);
    }

    @Mask(
            owner = "java/lang/Class",
            name = "getInterfaces",
            parametersDescriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)",
            type = MaskType.REPAIR_RETURN)
    public static Class<?>[] getInterfaces(Class<?>[] interfaces) {
        SimpleList<Class<?>> list = filter(interfaces);
        return list.toArray(new Class[list.size()]);
    }

    @Mask(
            owner = "sun/reflect/generics/repository/ClassRepository",
            name = "getSuperInterfaces",
            parametersDescriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)",
            type = MaskType.REPAIR_RETURN)
    @Mask(
            owner = "sun/reflect/generics/repository/ClassRepository",
            name = "computeSuperInterfaces",
            parametersDescriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)",
            type = MaskType.REPAIR_RETURN)
    public static Type[] getSuperInterfaces(Type[] interfaces) {
        SimpleList<Type> list = filter(interfaces);
        return list.toArray(new Type[list.size()]);
    }

    private static <T extends Type> SimpleList<T> filter(T[] types) {
        SimpleList<T> result = new SimpleList<>();
        for (T type : types) {
            if (!type.equals(TaggedObject.class)) {
                result.add(type);
            }
        }
        return result;
    }

    private static boolean isGaletteAdded(Class<?>[] parameterTypes) {
        return parameterTypes.length > 0 && parameterTypes[parameterTypes.length - 1].equals(TagFrame.class);
    }
}
