package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.HashMap;
import edu.neu.ccs.prl.galette.internal.runtime.collection.ObjectIntMap;
import edu.neu.ccs.prl.galette.internal.runtime.collection.WeakIdentityHashMap;
import java.lang.reflect.Array;

public final class ArrayTagStore {
    /**
     * Delay initialization to prevent circular class initialization
     */
    private static volatile WeakIdentityHashMap<Object, ArrayShadow> shadows;

    private ArrayTagStore() {
        throw new AssertionError();
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_GET_LENGTH_TAG)
    public static synchronized Tag getLengthTag(Object array, Tag arrayTag) {
        if (shadows == null || array == null) {
            return Tag.getEmptyTag();
        }
        ArrayShadow shadow = shadows.get(array);
        return shadow == null ? Tag.getEmptyTag() : shadow.getLength();
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_SET_LENGTH_TAG)
    public static synchronized void setLengthTag(Object array, Tag lengthTag) {
        if (shadows == null || array == null) {
            return;
        }
        ArrayShadow shadow = shadows.get(array);
        if (shadow == null) {
            if (Tag.isEmpty(lengthTag)) {
                return;
            }
            shadow = new ArrayShadow(array);
            shadows.put(array, shadow);
        }
        shadow.setLength(lengthTag);
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_SET_LENGTH_TAGS)
    public static synchronized void setLengthTags(Object array, Tag[] lengthTags) {
        if (shadows == null || array == null || lengthTags.length == 0) {
            return;
        }
        setLengthTagsInternal(array, lengthTags, 0);
    }

    private static synchronized void setLengthTagsInternal(Object array, Tag[] lengthTags, int dimension) {
        setLengthTag(array, lengthTags[dimension]);
        if (dimension < lengthTags.length - 1) {
            // Not the last dimension
            int length = Array.getLength(array);
            for (int i = 0; i < length; i++) {
                Object next = Array.get(array, i);
                setLengthTagsInternal(next, lengthTags, dimension + 1);
            }
        }
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_GET_TAG)
    public static synchronized Tag getTag(Object array, int index, Tag arrayTag, Tag indexTag) {
        if (shadows == null || array == null) {
            return Tag.getEmptyTag();
        }
        ArrayShadow shadow = shadows.get(array);
        // Propagate the array index's tag
        return shadow == null ? indexTag : Tag.union(indexTag, shadow.getElement(index));
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_SET_TAG)
    public static synchronized void setTag(Object array, int index, Tag arrayTag, Tag indexTag, Tag valueTag) {
        if (shadows == null || array == null) {
            return;
        }
        // Propagate the array index's tag
        Tag tag = Tag.union(indexTag, valueTag);
        ArrayShadow shadow = shadows.get(array);
        if (shadow == null) {
            if (Tag.isEmpty(tag)) {
                return;
            }
            shadow = new ArrayShadow(array);
            shadows.put(array, shadow);
        }
        shadow.setElement(tag, index);
    }

    public static synchronized void arraycopyTags(Object src, int srcPos, Object dest, int destPos, int length) {
        if (shadows == null || src == null || dest == null) {
            return;
        }
        ArrayShadow sourceShadow = shadows.get(src);
        ArrayShadow destShadow = shadows.get(dest);
        if (sourceShadow != null || destShadow != null) {
            if (destShadow == null) {
                destShadow = new ArrayShadow(dest);
                shadows.put(dest, destShadow);
            }
            if (sourceShadow == null) {
                sourceShadow = new ArrayShadow(src);
            }
            System.arraycopy(sourceShadow.elements, srcPos, destShadow.elements, destPos, length);
        }
    }

    public static synchronized void clear() {
        if (shadows != null) {
            shadows.clear();
        }
    }

    public static synchronized void initialize() {
        if (shadows == null) {
            // Ensure that needed classes are initialized to prevent circular class initialization
            Object[] dependencies = new Object[] {
                WeakIdentityHashMap.class,
                ObjectIntMap.class,
                System.class,
                HashMap.class,
                HashMap.Entry.class,
                ArrayShadow.class
            };
            // Create a temporary map and add an element to force IdentityWeakReference to be initialized
            new WeakIdentityHashMap<>().put(dependencies, dependencies);
            shadows = new WeakIdentityHashMap<>();
        }
    }

    private static final class ArrayShadow {
        private Tag length = Tag.getEmptyTag();
        private final Tag[] elements;

        ArrayShadow(Object array) {
            int length = Array.getLength(array);
            elements = new Tag[length];
        }

        void setElement(Tag element, int index) {
            elements[index] = element;
        }

        Tag getElement(int index) {
            return elements[index];
        }

        void setLength(Tag length) {
            this.length = length;
        }

        Tag getLength() {
            return length;
        }
    }
}
