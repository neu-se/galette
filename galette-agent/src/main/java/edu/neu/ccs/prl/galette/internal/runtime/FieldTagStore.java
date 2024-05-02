package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.HashMap;
import edu.neu.ccs.prl.galette.internal.runtime.collection.ObjectIntMap;
import edu.neu.ccs.prl.galette.internal.runtime.collection.WeakDataStore;

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
    private static WeakDataStore<Object, HashMap<String, Tag>> instanceFieldTags = null;
    private static HashMap<String, Tag> staticFieldTags = null;

    private FieldTagStore() {
        throw new AssertionError(getClass() + " is a static utility class");
    }

    @InvokedViaHandle(handle = Handle.FIELD_TAG_STORE_PUT_STATIC)
    public static synchronized void putStatic(Tag tag, String fieldReference) {
        if (staticFieldTags != null) {
            staticFieldTags.put(fieldReference, tag);
        }
    }

    @InvokedViaHandle(handle = Handle.FIELD_TAG_STORE_GET_STATIC)
    public static synchronized Tag getStatic(String fieldReference) {
        if (staticFieldTags != null) {
            return staticFieldTags.get(fieldReference);
        }
        return Tag.emptyTag();
    }

    @InvokedViaHandle(handle = Handle.FIELD_TAG_STORE_PUT_FIELD)
    public static synchronized void putField(Object receiver, Tag tag, String fieldReference) {
        HashMap<String, Tag> tags = getInstanceTagsInternal(receiver, tag);
        if (tags != null) {
            synchronized (tags) {
                tags.put(fieldReference, tag);
            }
        }
    }

    @InvokedViaHandle(handle = Handle.FIELD_TAG_STORE_GET_FIELD)
    public static Tag getField(Object receiver, String fieldReference) {
        HashMap<String, Tag> tags = getInstanceTagsInternal(receiver, null);
        if (tags != null) {
            synchronized (tags) {
                return tags.get(fieldReference);
            }
        }
        return null;
    }

    private static HashMap<String, Tag> getInstanceTagsInternal(Object receiver, Tag tag) {
        if (staticFieldTags != null) {
            HashMap<String, Tag> tags = instanceFieldTags.get(receiver);
            if (tags == null && !Tag.isEmpty(tag)) {
                tags = instanceFieldTags.computeIfAbsent(receiver);
            }
            return tags;
        }
        return null;
    }

    public static HashMap<String, Tag> getInstanceTags(Object receiver) {
        HashMap<String, Tag> tags = getInstanceTagsInternal(receiver, null);
        if (tags == null) {
            return null;
        }
        synchronized (tags) {
            return new HashMap<>(tags);
        }
    }

    public static void setInstanceTags(Object receiver, HashMap<String, Tag> tags) {
        if (staticFieldTags != null) {
            HashMap<String, Tag> dest = instanceFieldTags.computeIfAbsent(receiver);
            synchronized (dest) {
                dest.clear();
                dest.putAll(tags);
            }
        }
    }

    public static synchronized void clear() {
        if (staticFieldTags != null) {
            instanceFieldTags.clear();
            staticFieldTags.clear();
        }
    }

    public static void initialize() {
        if (staticFieldTags == null) {
            // Ensure that needed classes are initialized to prevent circular class initialization
            Object[] dependencies = new Object[] {
                ObjectIntMap.class, System.class, HashMap.class, HashMap.Entry.class,
            };
            instanceFieldTags = new WeakDataStore<>(k -> new HashMap<>());
            staticFieldTags = new HashMap<>();
        }
    }
}
