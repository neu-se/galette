package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.WeakDataStore;
import java.lang.reflect.Array;

/**
 * Maintains a "mirror-space" for storing the taint tags associated with array lengths and elements.
 */
public final class ArrayTagStore {
    /**
     * Delay initialization to prevent circular class initialization.
     */
    private static volatile WeakDataStore<Object, ArrayWrapper> wrappers;

    private ArrayTagStore() {
        throw new AssertionError();
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_GET_LENGTH_TAG)
    public static Tag getLengthTag(Object array, Tag arrayTag) {
        ArrayWrapper wrapper = getWrapper(array);
        return wrapper == null ? Tag.getEmptyTag() : wrapper.getLength();
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_SET_LENGTH_TAG)
    public static void setLengthTag(Object array, Tag lengthTag) {
        ArrayWrapper wrapper = getWrapper(array, lengthTag);
        if (wrapper != null) {
            wrapper.setLength(lengthTag);
        }
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_SET_LENGTH_TAGS)
    public static void setLengthTags(Object array, Tag[] lengthTags) {
        if (containsNonEmpty(lengthTags)) {
            setLengthTagsInternal(array, lengthTags, 0);
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

    public static void setLengthTags(Object array, int[] dimensions) {
        ArrayWrapper wrapper = getWrapper(dimensions);
        if (wrapper != null) {
            setLengthTags(array, wrapper.getElements());
        }
    }

    private static void setLengthTagsInternal(Object array, Tag[] lengthTags, int dimension) {
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
    public static Tag getTag(Object array, int index, Tag arrayTag, Tag indexTag) {
        ArrayWrapper wrapper = getWrapper(array);
        // Propagate the array index's tag
        return wrapper == null ? indexTag : Tag.union(indexTag, wrapper.getElement(index));
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_SET_TAG)
    public static void setTag(Object array, int index, Tag arrayTag, Tag indexTag, Tag valueTag) {
        // Propagate the array index's tag
        Tag tag = Tag.union(indexTag, valueTag);
        ArrayWrapper wrapper = getWrapper(array, tag);
        if (wrapper != null) {
            wrapper.setElement(tag, index);
        }
    }

    public static void arraycopyTags(Object src, int srcPos, Object dest, int destPos, int length) {
        ArrayWrapper sourceWrapper = getWrapper(src);
        ArrayWrapper destWrapper = getWrapper(dest);
        if (sourceWrapper != null || destWrapper != null) {
            if (destWrapper == null) {
                destWrapper = wrappers.computeIfAbsent(dest);
            }
            if (sourceWrapper == null) {
                sourceWrapper = wrappers.computeIfAbsent(src);
            }
            System.arraycopy(sourceWrapper.getElements(), srcPos, destWrapper.getElements(), destPos, length);
        }
    }

    public static ArrayWrapper getWrapper(Object array, Tag tag) {
        if (wrappers != null && array != null) {
            ArrayWrapper wrapper = wrappers.get(array);
            if (wrapper == null && !Tag.isEmpty(tag)) {
                return wrappers.computeIfAbsent(array);
            }
            return wrapper;
        }
        return null;
    }

    public static void clear() {
        if (wrappers != null) {
            wrappers.clear();
        }
    }

    public static void initialize() {
        if (wrappers == null) {
            // Ensure that needed classes are initialized to prevent circular class initialization
            Object[] dependencies = new Object[] {ArrayWrapper.class};
            wrappers = new WeakDataStore<>(ArrayWrapper::new);
        }
    }

    public static ArrayWrapper getWrapper(Object array) {
        if (wrappers != null && array != null) {
            return wrappers.get(array);
        }
        return null;
    }

    public static void updateWrapper(Object array, ArrayWrapper sourceWrapper) {
        if (wrappers != null && array != null) {
            ArrayWrapper destWrapper = wrappers.computeIfAbsent(array);
            destWrapper.setLength(sourceWrapper.getLength());
            System.arraycopy(sourceWrapper.getElements(), 0, destWrapper.getElements(), 0, destWrapper.size());
        }
    }
}
