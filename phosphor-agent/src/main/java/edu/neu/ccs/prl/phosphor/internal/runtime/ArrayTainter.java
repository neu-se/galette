package edu.neu.ccs.prl.phosphor.internal.runtime;

import edu.neu.ccs.prl.phosphor.internal.runtime.collection.ObjectIntMap;
import edu.neu.ccs.prl.phosphor.internal.runtime.collection.WeakIdentityHashMap;
import java.lang.reflect.Array;

public final class ArrayTainter {
    /**
     * Delay initialization to prevent circular class initialization
     */
    private static volatile WeakIdentityHashMap<Object, ArrayShadow> shadows;

    private ArrayTainter() {
        throw new AssertionError();
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAINTER_GET_LENGTH_TAG)
    public static synchronized Tag getLengthTag(Object array, Tag arrayTag) {
        // A lack of a shadow means that the length and element tags for the array are empty
        if (array == null || !hasShadow(array)) {
            return Tag.getEmptyTag();
        }
        return getShadow(array).getLength();
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAINTER_SET_LENGTH_TAG)
    public static synchronized void setLengthTag(Object array, Tag lengthTag) {
        // A lack of a shadow means that the length and element tags for the array are empty
        if (array != null && (hasShadow(array) || !Tag.isEmpty(lengthTag))) {
            getShadow(array).setLength(lengthTag);
        }
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAINTER_SET_LENGTH_TAGS)
    public static synchronized void setLengthTags(Object array, Tag[] lengthTags) {
        // If the length tag for all arrays is considered to be empty, and all passed tags are empty, return.
        // The check helps delay use of reflection
        if (shadows != null || containsNonEmpty(lengthTags)) {
            if (lengthTags.length > 0) {
                setLengthTagsInternal(array, lengthTags, 0);
            }
        }
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

    private static boolean containsNonEmpty(Tag[] tags) {
        for (Tag tag : tags) {
            if (!Tag.isEmpty(tag)) {
                return true;
            }
        }
        return false;
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAINTER_GET_TAG)
    public static synchronized Tag getTag(Object array, int index, Tag arrayTag, Tag indexTag) {
        Tag elementTag = Tag.getEmptyTag();
        // A lack of a shadow means that the length and element tags for the array are empty
        if (array != null && hasShadow(array)) {
            elementTag = getShadow(array).getElement(index);
        }
        return Tag.union(elementTag, indexTag);
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAINTER_SET_TAG)
    public static synchronized void setTag(Object array, int index, Tag arrayTag, Tag indexTag, Tag valueTag) {
        Tag tag = Tag.union(indexTag, valueTag);
        // A lack of a shadow means that the length and element tags for the array are empty
        if (array != null && (hasShadow(array) || !Tag.isEmpty(tag))) {
            getShadow(array).setElement(tag, index);
        }
    }

    private static synchronized boolean hasShadow(Object array) {
        return shadows != null && shadows.containsKey(array);
    }

    private static synchronized ArrayShadow getShadow(Object array) {
        if (shadows == null) {
            initialize();
        }
        ArrayShadow shadow = shadows.get(array);
        if (shadow == null) {
            shadow = new ArrayShadow(array);
            shadows.put(array, shadow);
        }
        return shadow;
    }

    private static synchronized void initialize() {
        // Create a temporary map and add and entry to it to force the HashMap$Entry class to be loaded
        WeakIdentityHashMap<Object, Object> temp = new WeakIdentityHashMap<>();
        // Ensure that ArrayShadow and ObjectIntMap classes are initialized
        temp.put(ArrayShadow.class, ObjectIntMap.class);
        shadows = new WeakIdentityHashMap<>();
    }
}
