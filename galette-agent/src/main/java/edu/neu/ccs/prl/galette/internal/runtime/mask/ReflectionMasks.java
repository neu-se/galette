package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.*;
import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ReflectionMasks {
    private static volatile boolean initialized = false;

    public static void initialize() {
        initialized = true;
    }

    @Mask(
            owner = "sun/reflect/NativeMethodAccessorImpl",
            name = "invoke0",
            descriptor = "(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;"
                    + "Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)Ljava/lang/Object;",
            isStatic = true,
            type = MaskType.FIX_ARGUMENTS)
    @Mask(
            owner = "jdk/internal/reflect/NativeMethodAccessorImpl",
            name = "invoke0",
            descriptor = "(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;"
                    + "Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)Ljava/lang/Object;",
            isStatic = true,
            type = MaskType.FIX_ARGUMENTS)
    @Mask(
            owner = "jdk/internal/reflect/DirectMethodHandleAccessor$NativeAccessor",
            name = "invoke0",
            descriptor = "(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;"
                    + "Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)Ljava/lang/Object;",
            isStatic = true,
            type = MaskType.FIX_ARGUMENTS)
    public static Object[] invoke0(Method m, Object obj, Object[] args, TagFrame frame) {
        if (hasShadow(m, obj)) {
            Method shadow = getShadowMethod(m);
            if (shadow != null) {
                // Remove the tag for m
                frame.dequeue();
                fixFrame(m, args, frame, frame.dequeue());
                return new Object[] {shadow, obj, append(args, frame), frame};
            }
        }
        return new Object[] {m, obj, args, frame};
    }

    @Mask(
            owner = "sun/reflect/NativeConstructorAccessorImpl",
            name = "newInstance0",
            descriptor = "(Ljava/lang/reflect/Constructor;[Ljava/lang/Object;"
                    + "Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)Ljava/lang/Object;",
            isStatic = true,
            type = MaskType.FIX_ARGUMENTS)
    @Mask(
            owner = "jdk/internal/reflect/NativeConstructorAccessorImpl",
            name = "newInstance0",
            descriptor = "(Ljava/lang/reflect/Constructor;[Ljava/lang/Object;"
                    + "Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)Ljava/lang/Object;",
            isStatic = true,
            type = MaskType.FIX_ARGUMENTS)
    @Mask(
            owner = "jdk/internal/reflect/DirectConstructorHandleAccessor$NativeAccessor",
            name = "newInstance0",
            descriptor = "(Ljava/lang/reflect/Constructor;[Ljava/lang/Object;"
                    + "Ledu/neu/ccs/prl/galette/internal/runtime/TagFrame;)Ljava/lang/Object;",
            isStatic = true,
            type = MaskType.FIX_ARGUMENTS)
    public static Object[] newInstance0(Constructor<?> c, Object[] args, TagFrame frame) {
        if (hasShadow(c, null)) {
            Constructor<?> shadow = getShadowConstructor(c);
            if (shadow != null) {
                // Add tag for uninitialized this
                fixFrame(c, args, frame, Tag.getEmptyTag());
                return new Object[] {shadow, append(args, frame), frame};
            }
        }
        return new Object[] {c, args, frame};
    }

    private static void fixFrame(Executable e, Object[] args, TagFrame frame, Tag receiverTag) {
        frame.clearTags();
        if (!Modifier.isStatic(e.getModifiers())) {
            frame.enqueue(receiverTag);
        }
        if (args == null) {
            // Empty arguments
            return;
        }
        Class<?>[] parameters = e.getParameterTypes();
        if (parameters.length != args.length) {
            // Invalid call
            return;
        }
        ArrayWrapper wrapper = ArrayTagStore.getWrapper(args);
        for (int i = 0; i < args.length; i++) {
            Tag tag = wrapper == null ? Tag.getEmptyTag() : wrapper.getElement(i);
            if (parameters[i].isPrimitive()) {
                tag = Tag.union(tag, getValueTag(args[i]));
            }
            frame.enqueue(tag);
        }
    }

    private static Tag getValueTag(Object argument) {
        // Get the value tag for boxed types
        if (argument instanceof Boolean) {
            return FieldTagStore.getField(argument, "java/lang/Boolean#value#Z");
        } else if (argument instanceof Byte) {
            return FieldTagStore.getField(argument, "java/lang/Byte#value#B");
        } else if (argument instanceof Short) {
            return FieldTagStore.getField(argument, "java/lang/Short#value#S");
        } else if (argument instanceof Character) {
            return FieldTagStore.getField(argument, "java/lang/Character#value#C");
        } else if (argument instanceof Integer) {
            return FieldTagStore.getField(argument, "java/lang/Integer#value#I");
        } else if (argument instanceof Long) {
            return FieldTagStore.getField(argument, "java/lang/Long#value#J");
        } else if (argument instanceof Float) {
            return FieldTagStore.getField(argument, "java/lang/Float#value#F");
        } else if (argument instanceof Double) {
            return FieldTagStore.getField(argument, "java/lang/Double#value#D");
        }
        return Tag.getEmptyTag();
    }

    private static Constructor<?> getShadowConstructor(Constructor<?> c) {
        Class<?>[] shadowParameters = getShadowParameters(c);
        try {
            return c.getDeclaringClass().getDeclaredConstructor(shadowParameters);
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
    }

    private static Method getShadowMethod(Method m) {
        Class<?>[] shadowParameters = getShadowParameters(m);
        try {
            if (m.getDeclaringClass().equals(Object.class)) {
                return TaggedObject.class.getDeclaredMethod(m.getName(), shadowParameters);
            }
            return m.getDeclaringClass().getDeclaredMethod(m.getName(), shadowParameters);
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
    }

    private static boolean hasShadow(Executable exec, Object obj) {
        if (!initialized) {
            return false;
        }
        Class<?> clazz = exec.getDeclaringClass();
        if (clazz.isArray()) {
            return false;
        } else if (exec.getDeclaringClass().equals(Object.class)) {
            return !Modifier.isStatic(exec.getModifiers())
                    && !Modifier.isFinal(exec.getModifiers())
                    && obj instanceof TaggedObject;
        }
        return !GaletteTransformer.isExcluded(exec.getName().replace('.', '/'));
    }

    private static Class<?>[] getShadowParameters(Executable exec) {
        Class<?>[] parameters = exec.getParameterTypes();
        Class<?>[] shadowParameters = new Class[parameters.length + 1];
        System.arraycopy(parameters, 0, shadowParameters, 0, parameters.length);
        shadowParameters[parameters.length] = TagFrame.class;
        return shadowParameters;
    }

    private static Object[] append(Object[] args, Object arg) {
        if (args == null) {
            return new Object[] {arg};
        }
        Object[] result = new Object[args.length + 1];
        System.arraycopy(args, 0, result, 0, args.length);
        result[args.length] = arg;
        return result;
    }
}
