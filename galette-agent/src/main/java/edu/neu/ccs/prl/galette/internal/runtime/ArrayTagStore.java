package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.HashMap;
import edu.neu.ccs.prl.galette.internal.runtime.collection.ObjectIntMap;
import edu.neu.ccs.prl.galette.internal.runtime.collection.WeakIdentityHashMap;
import edu.neu.ccs.prl.galette.internal.runtime.mask.UnsafeWrapper;
import java.lang.reflect.Array;

/**
 * Maintains a "mirror-space" for storing the taint tags associated with array lengths and elements.
 */
public final class ArrayTagStore {
    /**
     * Delay initialization to prevent circular class initialization
     */
    private static volatile WeakIdentityHashMap<Object, ArrayWrapper> wrappers;

    private ArrayTagStore() {
        throw new AssertionError();
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_GET_LENGTH_TAG)
    public static synchronized Tag getLengthTag(Object array, Tag arrayTag) {
        if (wrappers == null || array == null) {
            return Tag.getEmptyTag();
        }
        ArrayWrapper wrapper = wrappers.get(array);
        return wrapper == null ? Tag.getEmptyTag() : wrapper.getLength();
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_SET_LENGTH_TAG)
    public static synchronized void setLengthTag(Object array, Tag lengthTag) {
        if (wrappers == null || array == null) {
            return;
        }
        ArrayWrapper wrapper = wrappers.get(array);
        if (wrapper == null) {
            if (Tag.isEmpty(lengthTag)) {
                return;
            }
            wrapper = new ArrayWrapper(array);
            wrappers.put(array, wrapper);
        }
        wrapper.setLength(lengthTag);
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_SET_LENGTH_TAGS)
    public static synchronized void setLengthTags(Object array, Tag[] lengthTags) {
        if (wrappers == null || array == null || lengthTags.length == 0) {
            return;
        }
        setLengthTagsInternal(array, lengthTags, 0);
    }

    public static synchronized void setLengthTags(Object array, int[] dimensions) {
        if (wrappers == null || array == null || dimensions.length == 0) {
            return;
        }
        ArrayWrapper wrapper = wrappers.get(dimensions);
        if (wrapper != null) {
            setLengthTagsInternal(array, wrapper.elements, 0);
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

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_GET_TAG)
    public static synchronized Tag getTag(Object array, int index, Tag arrayTag, Tag indexTag) {
        if (wrappers == null || array == null) {
            return Tag.getEmptyTag();
        }
        ArrayWrapper wrapper = wrappers.get(array);
        // Propagate the array index's tag
        return wrapper == null ? indexTag : Tag.union(indexTag, wrapper.getElement(index));
    }

    public static synchronized Tag getTagVolatile(UnsafeWrapper unsafe, Object array, Tag indexTag, long offset) {
        if (wrappers == null || array == null) {
            return Tag.getEmptyTag();
        }
        ArrayWrapper wrapper = wrappers.get(array);
        // Propagate the array index's tag
        if (wrapper == null) {
            return indexTag;
        } else {
            Tag elementTag = (Tag) unsafe.getObjectVolatile(wrapper.elements, offset);
            return Tag.union(indexTag, elementTag);
        }
    }

    @InvokedViaHandle(handle = Handle.ARRAY_TAG_STORE_SET_TAG)
    public static synchronized void setTag(Object array, int index, Tag arrayTag, Tag indexTag, Tag valueTag) {
        // Propagate the array index's tag
        Tag tag = Tag.union(indexTag, valueTag);
        ArrayWrapper wrapper = getWrapper(array, tag);
        if (wrapper != null) {
            wrapper.setElement(tag, index);
        }
    }

    public static synchronized void setTagVolatile(
            UnsafeWrapper unsafe, Object array, Tag indexTag, Tag valueTag, long offset) {
        // Propagate the array index's tag
        Tag tag = Tag.union(indexTag, valueTag);
        ArrayWrapper wrapper = getWrapper(array, tag);
        if (wrapper != null) {
            unsafe.putObjectVolatile(wrapper.elements, offset, tag);
        }
    }

    private static synchronized ArrayWrapper getWrapper(Object array, Tag tag) {
        if (wrappers == null || array == null) {
            return null;
        }
        // Propagate the array index's tag
        ArrayWrapper wrapper = wrappers.get(array);
        if (wrapper == null) {
            if (Tag.isEmpty(tag)) {
                return null;
            }
            wrapper = new ArrayWrapper(array);
            wrappers.put(array, wrapper);
        }
        return wrapper;
    }

    public static synchronized void arraycopyTags(Object src, int srcPos, Object dest, int destPos, int length) {
        if (wrappers == null || src == null || dest == null) {
            return;
        }
        ArrayWrapper sourceWrapper = wrappers.get(src);
        ArrayWrapper destWrapper = wrappers.get(dest);
        if (sourceWrapper != null || destWrapper != null) {
            if (destWrapper == null) {
                destWrapper = new ArrayWrapper(dest);
                wrappers.put(dest, destWrapper);
            }
            if (sourceWrapper == null) {
                sourceWrapper = new ArrayWrapper(src);
            }
            System.arraycopy(sourceWrapper.elements, srcPos, destWrapper.elements, destPos, length);
        }
    }

    public static synchronized void clear() {
        if (wrappers != null) {
            wrappers.clear();
        }
    }

    public static synchronized void initialize() {
        if (wrappers == null) {
            // Ensure that needed classes are initialized to prevent circular class initialization
            Object[] dependencies = new Object[] {
                WeakIdentityHashMap.class,
                ObjectIntMap.class,
                System.class,
                HashMap.class,
                HashMap.Entry.class,
                ArrayWrapper.class
            };
            // Create a temporary map and add an element to force IdentityWeakReference to be initialized
            new WeakIdentityHashMap<>().put(dependencies, dependencies);
            wrappers = new WeakIdentityHashMap<>();
        }
    }

    private static final class ArrayWrapper {
        private Tag length = Tag.getEmptyTag();
        private final Tag[] elements;

        ArrayWrapper(Object array) {
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
