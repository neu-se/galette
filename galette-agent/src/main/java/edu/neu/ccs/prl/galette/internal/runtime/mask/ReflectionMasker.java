package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public final class ReflectionMasker {

    @Mask(
            owner = "java/lang/Class",
            name = "getFields",
            descriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)[Ljava/lang/reflect/Field;",
            type = MaskType.REPAIR_RETURN)
    @Mask(
            owner = "java/lang/Class",
            name = "getDeclaredFields",
            descriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)[Ljava/lang/reflect/Field;",
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
            descriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)[Ljava/lang/reflect/Method;",
            type = MaskType.REPAIR_RETURN)
    @Mask(
            owner = "java/lang/Class",
            name = "getDeclaredMethods",
            descriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)[Ljava/lang/reflect/Method;",
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
            descriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)[Ljava/lang/reflect/Constructor;",
            type = MaskType.REPAIR_RETURN)
    @Mask(
            owner = "java/lang/Class",
            name = "getDeclaredConstructors",
            descriptor = "(Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)[Ljava/lang/reflect/Constructor;",
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

    private static boolean isGaletteAdded(Class<?>[] parameterTypes) {
        return parameterTypes.length > 0 && parameterTypes[parameterTypes.length - 1].equals(TagFrame.class);
    }
}
