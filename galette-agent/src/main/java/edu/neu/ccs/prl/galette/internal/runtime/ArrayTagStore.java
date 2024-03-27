package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.WeakIdentityHashMap;
import edu.neu.ccs.prl.galette.internal.runtime.mask.UnsafeWrapper;
import java.lang.reflect.Array;

/**
 * Maintains a "mirror-space" for storing the taint tags associated with array lengths and elements.
 */
public final class ArrayTagStore {
    /**
     * Delay initialization to prevent circular class initialization.
     */
    private static volatile WeakIdentityHashMap<Object, ArrayWrapper> wrappers;

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

    public static Tag getTagVolatile(UnsafeWrapper unsafe, Object array, Tag indexTag, long offset) {
        ArrayWrapper wrapper = getWrapper(array);
        // Propagate the array index's tag
        if (wrapper == null) {
            return indexTag;
        } else {
            Tag elementTag = (Tag) unsafe.getObjectVolatile(wrapper.getElements(), offset);
            return Tag.union(indexTag, elementTag);
        }
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

    public static void setTagVolatile(UnsafeWrapper unsafe, Object array, Tag indexTag, Tag valueTag, long offset) {
        // Propagate the array index's tag
        Tag tag = Tag.union(indexTag, valueTag);
        ArrayWrapper wrapper = getWrapper(array, tag);
        if (wrapper != null) {
            unsafe.putObjectVolatile(wrapper.getElements(), offset, tag);
        }
    }

    public static synchronized void arraycopyTags(Object src, int srcPos, Object dest, int destPos, int length) {
        ArrayWrapper sourceWrapper = getWrapper(src);
        ArrayWrapper destWrapper = getWrapper(dest);
        if (sourceWrapper != null || destWrapper != null) {
            if (destWrapper == null) {
                destWrapper = new ArrayWrapper(dest);
                setWrapper(dest, destWrapper);
            }
            if (sourceWrapper == null) {
                sourceWrapper = new ArrayWrapper(src);
                setWrapper(src, sourceWrapper);
            }
            System.arraycopy(sourceWrapper.getElements(), srcPos, destWrapper.getElements(), destPos, length);
        }
    }

    private static synchronized ArrayWrapper getWrapper(Object array, Tag tag) {
        ArrayWrapper wrapper = getWrapper(array);
        if (wrapper == null) {
            if (array != null && !Tag.isEmpty(tag)) {
                wrapper = new ArrayWrapper(array);
                setWrapper(array, wrapper);
            }
        }
        return wrapper;
    }

    public static synchronized void clear() {
        if (wrappers != null && FlagAccessor.reserve()) {
            try {
                wrappers.clear();
            } finally {
                FlagAccessor.free();
            }
        }
    }

    public static synchronized void initialize() {
        if (wrappers == null) {
            wrappers = new WeakIdentityHashMap<>();
        }
    }

    public static synchronized ArrayWrapper getWrapper(Object array) {
        if (wrappers != null && array != null && FlagAccessor.reserve()) {
            try {
                return wrappers.get(array);
            } finally {
                FlagAccessor.free();
            }
        }
        return null;
    }

    public static synchronized void setWrapper(Object array, ArrayWrapper wrapper) {
        if (wrappers != null && array != null && FlagAccessor.reserve()) {
            try {
                wrappers.put(array, wrapper);
            } finally {
                FlagAccessor.free();
            }
        }
    }
}
