package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.HashMap;
import edu.neu.ccs.prl.galette.internal.runtime.collection.ObjectIntMap;
import edu.neu.ccs.prl.galette.internal.runtime.collection.WeakIdentityHashMap;

public final class TagStore {
    private static WeakIdentityHashMap<Object, Tag> tags = null;

    private TagStore() {
        throw new AssertionError();
    }

    @InvokedViaHandle(handle = Handle.TAG_STORE_SET_TAG)
    public static synchronized void setTag(Object key, Tag tag) {
        if (tags != null && key != null && (tags.containsKey(key) || !Tag.isEmpty(tag))) {
            tags.put(key, tag);
        }
    }

    @InvokedViaHandle(handle = Handle.TAG_STORE_GET_TAG)
    public static synchronized Tag getTag(Object key) {
        return tags == null ? Tag.getEmptyTag() : tags.get(key);
    }

    public static synchronized void clear() {
        if (tags != null) {
            tags.clear();
        }
    }

    public static synchronized void initialize() {
        if (tags == null) {
            // Ensure that needed classes are initialized to prevent circular class initialization
            Object[] dependencies = new Object[] {
                WeakIdentityHashMap.class, ObjectIntMap.class, System.class, HashMap.class, HashMap.Entry.class,
            };
            // Create a temporary map and add an element to force IdentityWeakReference to be initialized
            new WeakIdentityHashMap<>().put(dependencies, dependencies);
            tags = new WeakIdentityHashMap<>();
        }
    }
}
