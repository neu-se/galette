package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.ArrayTagStore;
import edu.neu.ccs.prl.galette.internal.runtime.ArrayWrapper;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import edu.neu.ccs.prl.galette.internal.transform.ShadowFieldAdder;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.objectweb.asm.Opcodes;

public final class UnsafeTagLocator {
    private static final UnsafeWrapper UNSAFE = UnsafeWrapper.createInstance();
    private static volatile boolean initialized = false;

    /**
     *  Used to disambiguate between a static field of a given type and an instance field of java.lang.Class.
     */
    private static long LAST_INSTANCE_OFFSET_JAVA_LANG_CLASS = UNSAFE.getInvalidFieldOffset();

    public static void initialize() {
        initialized = true;
    }

    static void putTag(Object o, long offset, Tag offsetTag, Tag tag, Class<?> arrayType) {
        if (initialized && o != null) {
            if (o.getClass().isArray()) {
                putArrayTag(o, offset, offsetTag, tag, arrayType, false);
            } else {
                long shadowOffset = getShadowOffset(o, offset);
                if (shadowOffset != UNSAFE.getInvalidFieldOffset()) {
                    UNSAFE.putObject(o, shadowOffset, tag);
                }
            }
        }
    }

    static void putTagVolatile(Object o, long offset, Tag offsetTag, Tag tag, Class<?> arrayType) {
        if (initialized && o != null) {
            if (o.getClass().isArray()) {
                putArrayTag(o, offset, offsetTag, tag, arrayType, true);
            } else {
                long shadowOffset = getShadowOffset(o, offset);
                if (shadowOffset != UNSAFE.getInvalidFieldOffset()) {
                    UNSAFE.putObjectVolatile(o, shadowOffset, tag);
                }
            }
        }
    }

    private static void putArrayTag(
            Object array, long offset, Tag offsetTag, Tag tag, Class<?> arrayType, boolean volatileAccess) {
        // Propagate the array index's tag
        tag = Tag.union(offsetTag, tag);
        ArrayWrapper wrapper = ArrayTagStore.getWrapper(array, tag);
        if (wrapper != null) {
            int index = computeArrayIndex(array, offset);
            int scale = UNSAFE.arrayIndexScale(array.getClass());
            int typeScale = UNSAFE.arrayIndexScale(arrayType);
            if (scale >= typeScale) {
                // Aligned access, or partial value unaligned
                putArrayTag(wrapper, index, tag, volatileAccess);
            } else {
                // Unaligned access
                int count = typeScale / scale;
                for (int i = 0; i < count && index + i < wrapper.size(); i++) {
                    putArrayTag(wrapper, index + i, tag, volatileAccess);
                }
            }
        }
    }

    private static void putArrayTag(ArrayWrapper wrapper, int index, Tag tag, boolean volatileAccess) {
        if (volatileAccess) {
            long shadowOffset =
                    UNSAFE.arrayBaseOffset(Tag[].class) + (long) UNSAFE.arrayIndexScale(Tag[].class) * index;
            UNSAFE.putObjectVolatile(wrapper.getElements(), shadowOffset, tag);
        } else {
            wrapper.setElement(tag, index);
        }
    }

    static Tag getTag(Object o, long offset, Tag offsetTag, Class<?> arrayType) {
        if (initialized && o != null) {
            if (o.getClass().isArray()) {
                return getArrayTag(o, offset, offsetTag, arrayType, false);
            } else {
                long shadowOffset = getShadowOffset(o, offset);
                if (shadowOffset != UNSAFE.getInvalidFieldOffset()) {
                    Object result = UNSAFE.getObject(o, shadowOffset);
                    if (result instanceof Tag) {
                        return (Tag) result;
                    }
                }
            }
        }
        return Tag.getEmptyTag();
    }

    static Tag getTagVolatile(Object o, long offset, Tag offsetTag, Class<?> arrayType) {
        if (initialized && o != null) {
            if (o.getClass().isArray()) {
                return getArrayTag(o, offset, offsetTag, arrayType, true);
            } else {
                long shadowOffset = getShadowOffset(o, offset);
                if (shadowOffset != UNSAFE.getInvalidFieldOffset()) {
                    Object result = UNSAFE.getObjectVolatile(o, shadowOffset);
                    if (result instanceof Tag) {
                        return (Tag) result;
                    }
                }
            }
        }
        return Tag.getEmptyTag();
    }

    private static Tag getArrayTag(
            Object array, long offset, Tag offsetTag, Class<?> arrayType, boolean volatileAccess) {
        ArrayWrapper wrapper = ArrayTagStore.getWrapper(array);
        // Propagate the array index's tag
        Tag result = offsetTag;
        if (wrapper != null) {
            int index = computeArrayIndex(array, offset);
            int scale = UNSAFE.arrayIndexScale(array.getClass());
            int typeScale = UNSAFE.arrayIndexScale(arrayType);
            if (scale >= typeScale) {
                // Aligned access, or partial value unaligned
                result = Tag.union(result, getArrayTag(wrapper, index, volatileAccess));
            } else {
                // Unaligned access
                int count = typeScale / scale;
                for (int i = 0; i < count && index + i < wrapper.size(); i++) {
                    result = Tag.union(result, getArrayTag(wrapper, index + i, volatileAccess));
                }
            }
        }
        return result;
    }

    private static Tag getArrayTag(ArrayWrapper wrapper, int index, boolean volatileAccess) {
        if (volatileAccess) {
            long shadowOffset =
                    UNSAFE.arrayBaseOffset(Tag[].class) + (long) UNSAFE.arrayIndexScale(Tag[].class) * index;
            return (Tag) UNSAFE.getObjectVolatile(wrapper.getElements(), shadowOffset);
        } else {
            return wrapper.getElement(index);
        }
    }

    private static long getShadowOffset(Object o, long offset) {
        if (o == null) {
            return UNSAFE.getInvalidFieldOffset();
        }
        // Class instance are used as the "base" object when accessing static fields,
        // but they are also used to access instance fields of java.lang.Class
        boolean isStatic = o instanceof Class && offset > getLastInstanceOffsetInClass();
        Class<?> owner = isStatic ? (Class<?>) o : o.getClass();
        SimpleList<OffsetEntry> shadowOffsets = getShadowOffsets(owner);
        if (shadowOffsets == null) {
            shadowOffsets = computeShadowOffsets(owner);
            putShadowOffsets(owner, shadowOffsets);
        }
        for (int i = 0; i < shadowOffsets.size(); i++) {
            OffsetEntry entry = shadowOffsets.get(i);
            if (entry.originalOffset == offset && entry.isStatic == isStatic) {
                return entry.shadowOffset;
            }
        }
        return UNSAFE.getInvalidFieldOffset();
    }

    private static long getLastInstanceOffsetInClass() {
        if (LAST_INSTANCE_OFFSET_JAVA_LANG_CLASS == UNSAFE.getInvalidFieldOffset()) {
            for (Field field : Class.class.getDeclaredFields()) {
                try {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    long fieldOffset = UNSAFE.objectFieldOffset(field);
                    if (fieldOffset > LAST_INSTANCE_OFFSET_JAVA_LANG_CLASS) {
                        LAST_INSTANCE_OFFSET_JAVA_LANG_CLASS = fieldOffset;
                    }
                } catch (Exception e) {
                    //
                }
            }
        }
        return LAST_INSTANCE_OFFSET_JAVA_LANG_CLASS;
    }

    static int computeArrayIndex(Object array, long offset) {
        Class<?> clazz = array.getClass();
        long baseOffset = UNSAFE.arrayBaseOffset(clazz);
        long scale = UNSAFE.arrayIndexScale(clazz);
        return (int) ((offset - baseOffset) / scale);
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Class", name = "$$GALETTE_OFFSET_CACHE", opcode = Opcodes.GETFIELD)
    private static SimpleList<OffsetEntry> getShadowOffsets(Class<?> clazz) {
        throw new AssertionError("Placeholder method was called");
    }

    @SuppressWarnings("unused")
    @MemberAccess(owner = "java/lang/Class", name = "$$GALETTE_OFFSET_CACHE", opcode = Opcodes.PUTFIELD)
    private static void putShadowOffsets(Class<?> clazz, SimpleList<OffsetEntry> shadowOffsets) {
        throw new AssertionError("Placeholder method was called");
    }

    private static SimpleList<OffsetEntry> computeShadowOffsets(Class<?> targetClazz) {
        SimpleList<OffsetEntry> list = new SimpleList<>();
        for (Class<?> clazz = targetClazz;
                clazz != null && !Object.class.equals(clazz);
                clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                try {
                    boolean isStatic = Modifier.isStatic(field.getModifiers());
                    long fieldOffset = (isStatic ? UNSAFE.staticFieldOffset(field) : UNSAFE.objectFieldOffset(field));
                    long tagOffset = getTagOffset(clazz, field, isStatic);
                    list.add(new OffsetEntry(isStatic, fieldOffset, tagOffset));
                } catch (Exception e) {
                    //
                }
            }
        }
        return list;
    }

    private static long getTagOffset(Class<?> clazz, Field field, boolean isStatic) {
        try {
            Field shadowField = clazz.getField(ShadowFieldAdder.getShadowFieldName(field.getName()));
            if (Tag.class.equals(shadowField.getType())) {
                return (isStatic ? UNSAFE.staticFieldOffset(shadowField) : UNSAFE.objectFieldOffset(shadowField));
            }
        } catch (Exception e) {
            //
        }
        return UNSAFE.getInvalidFieldOffset();
    }

    private static final class OffsetEntry {
        private final boolean isStatic;
        private final long originalOffset;
        private final long shadowOffset;

        OffsetEntry(boolean isStatic, long originalOffset, long shadowOffset) {
            this.isStatic = isStatic;
            this.originalOffset = originalOffset;
            this.shadowOffset = shadowOffset;
        }
    }
}
