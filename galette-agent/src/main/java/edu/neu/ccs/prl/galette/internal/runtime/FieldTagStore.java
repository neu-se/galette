package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.HashMap;
import edu.neu.ccs.prl.galette.internal.runtime.collection.ObjectIntMap;
import edu.neu.ccs.prl.galette.internal.runtime.collection.WeakIdentityHashMap;

/**
 * Maintains a "mirror-space" for storing the taint tags associated with the fields of classes that do not have
 * shadow fields added to them.
 * <br>
 * This store does not consider the class hierarchy when "resolving" symbolic field references.
 * Instead, it acts as if symbolic field references uniquely identify a specific field.
 * This is not broadly the case because fields can also be referenced using a reference to the subtype.
 * However, this is the case if the class that declares the field is final or the field is private.
 * In these cases, it is also safe for the shadow fields to be missing.
 * Otherwise, we may mistakenly generate a reference to the missing shadow field in response to a reference to
 * the original field through the subtype.
 */
public final class FieldTagStore {
    private static WeakIdentityHashMap<Object, HashMap<String, Tag>> instanceFieldTags = null;
    private static HashMap<String, Tag> staticFieldTags = null;

    private FieldTagStore() {
        throw new AssertionError(getClass() + " is a static utility class");
    }

    @InvokedViaHandle(handle = Handle.FIELD_TAG_STORE_PUT_STATIC)
    public static synchronized void putStatic(Tag tag, String fieldReference) {
        if (!isInitialized()) {
            return;
        }
        staticFieldTags.put(fieldReference, tag);
    }

    @InvokedViaHandle(handle = Handle.FIELD_TAG_STORE_GET_STATIC)
    public static synchronized Tag getStatic(String fieldReference) {
        if (!isInitialized()) {
            return Tag.getEmptyTag();
        }
        return staticFieldTags.get(fieldReference);
    }

    @InvokedViaHandle(handle = Handle.FIELD_TAG_STORE_PUT_FIELD)
    public static synchronized void putField(Object receiver, Tag tag, String fieldReference) {
        if (!isInitialized()) {
            return;
        }
        HashMap<String, Tag> tags = instanceFieldTags.get(receiver);
        if (tags != null || !Tag.isEmpty(tag)) {
            if (tags == null) {
                tags = new HashMap<>();
                instanceFieldTags.put(receiver, tags);
            }
            tags.put(fieldReference, tag);
        }
    }

    @InvokedViaHandle(handle = Handle.FIELD_TAG_STORE_GET_FIELD)
    public static synchronized Tag getField(Object receiver, String fieldReference) {
        if (!isInitialized()) {
            return Tag.getEmptyTag();
        }
        HashMap<String, Tag> tags = instanceFieldTags.get(receiver);
        return tags == null ? Tag.getEmptyTag() : tags.get(fieldReference);
    }

    public static synchronized HashMap<String, Tag> getTags(Object receiver) {
        if (!isInitialized()) {
            return null;
        }
        HashMap<String, Tag> tags = instanceFieldTags.get(receiver);
        return tags == null ? null : new HashMap<>(tags);
    }

    public static synchronized void setTags(Object receiver, HashMap<String, Tag> tags) {
        if (isInitialized()) {
            instanceFieldTags.put(receiver, new HashMap<>(tags));
        }
    }

    public static synchronized void clear() {
        if (isInitialized()) {
            instanceFieldTags.clear();
            staticFieldTags.clear();
        }
    }

    public static synchronized boolean isInitialized() {
        return instanceFieldTags != null;
    }

    public static synchronized void initialize() {
        if (!isInitialized()) {
            // Ensure that needed classes are initialized to prevent circular class initialization
            Object[] dependencies = new Object[] {
                WeakIdentityHashMap.class, ObjectIntMap.class, System.class, HashMap.class, HashMap.Entry.class,
            };
            // Create a temporary map and add an element to force IdentityWeakReference to be initialized
            new WeakIdentityHashMap<>().put(dependencies, dependencies);
            instanceFieldTags = new WeakIdentityHashMap<>();
            staticFieldTags = new HashMap<>();
        }
    }
}
