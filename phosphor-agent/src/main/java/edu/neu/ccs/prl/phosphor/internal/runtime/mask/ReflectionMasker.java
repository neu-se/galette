package edu.neu.ccs.prl.phosphor.internal.runtime.mask;

import edu.neu.ccs.prl.phosphor.internal.runtime.PhosphorFrame;
import edu.neu.ccs.prl.phosphor.internal.runtime.collection.SimpleList;
import edu.neu.ccs.prl.phosphor.internal.transform.PhosphorTransformer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public final class ReflectionMasker {

    @Mask(
            owner = "java/lang/Class",
            name = "$$PHOSPHOR_getFields",
            descriptor = "(Ledu/neu/ccs/prl/phosphor/internal/runtime/PhosphorFrame;)[Ljava/lang/reflect/Field;",
            type = MaskType.REPAIR_RETURN)
    @Mask(
            owner = "java/lang/Class",
            name = "$$PHOSPHOR_getDeclaredFields",
            descriptor = "(Ledu/neu/ccs/prl/phosphor/internal/runtime/PhosphorFrame;)[Ljava/lang/reflect/Field;",
            type = MaskType.REPAIR_RETURN)
    public static Field[] getFields(Field[] fields) {
        SimpleList<Field> result = new SimpleList<>();
        for (Field f : fields) {
            if (!f.getName().startsWith(PhosphorTransformer.ADDED_MEMBER_PREFIX)) {
                result.add(f);
            }
        }
        return result.toArray(new Field[result.size()]);
    }

    @Mask(
            owner = "java/lang/Class",
            name = "$$PHOSPHOR_getMethods",
            descriptor = "(Ledu/neu/ccs/prl/phosphor/internal/runtime/PhosphorFrame;)[Ljava/lang/reflect/Method;",
            type = MaskType.REPAIR_RETURN)
    @Mask(
            owner = "java/lang/Class",
            name = "$$PHOSPHOR_getDeclaredMethods",
            descriptor = "(Ledu/neu/ccs/prl/phosphor/internal/runtime/PhosphorFrame;)[Ljava/lang/reflect/Method;",
            type = MaskType.REPAIR_RETURN)
    public static Method[] getMethods(Method[] methods) {
        SimpleList<Method> result = new SimpleList<>();
        for (Method m : methods) {
            if (!isPhosphorAdded(m.getParameterTypes())) {
                result.add(m);
            }
        }
        return result.toArray(new Method[result.size()]);
    }

    @Mask(
            owner = "java/lang/Class",
            name = "$$PHOSPHOR_getConstructors",
            descriptor = "(Ledu/neu/ccs/prl/phosphor/internal/runtime/PhosphorFrame;)[Ljava/lang/reflect/Constructor;",
            type = MaskType.REPAIR_RETURN)
    @Mask(
            owner = "java/lang/Class",
            name = "$$PHOSPHOR_getDeclaredConstructors",
            descriptor = "(Ledu/neu/ccs/prl/phosphor/internal/runtime/PhosphorFrame;)[Ljava/lang/reflect/Constructor;",
            type = MaskType.REPAIR_RETURN)
    public static Constructor<?>[] getConstructors(Constructor<?>[] constructors) {
        SimpleList<Constructor<?>> result = new SimpleList<>();
        for (Constructor<?> c : constructors) {
            if (!isPhosphorAdded(c.getParameterTypes())) {
                result.add(c);
            }
        }
        return result.toArray(new Constructor[result.size()]);
    }

    private static boolean isPhosphorAdded(Class<?>[] parameterTypes) {
        return parameterTypes.length > 0 && parameterTypes[parameterTypes.length - 1].equals(PhosphorFrame.class);
    }
}
