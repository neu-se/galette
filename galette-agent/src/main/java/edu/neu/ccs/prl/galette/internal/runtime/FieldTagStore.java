package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.HashMap;
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
        if (staticFieldTags != null && TagStoreFlagAccessor.reserve()) {
            try {
                staticFieldTags.put(fieldReference, tag);
            } finally {
                TagStoreFlagAccessor.free();
            }
        }
    }

    @InvokedViaHandle(handle = Handle.FIELD_TAG_STORE_GET_STATIC)
    public static synchronized Tag getStatic(String fieldReference) {
        if (staticFieldTags != null && TagStoreFlagAccessor.reserve()) {
            try {
                return staticFieldTags.get(fieldReference);
            } finally {
                TagStoreFlagAccessor.free();
            }
        }
        return Tag.getEmptyTag();
    }

    @InvokedViaHandle(handle = Handle.FIELD_TAG_STORE_PUT_FIELD)
    public static synchronized void putField(Object receiver, Tag tag, String fieldReference) {
        HashMap<String, Tag> tags = getInstanceTagsInternal(receiver, tag);
        if (tags != null) {
            tags.put(fieldReference, tag);
        }
    }

    @InvokedViaHandle(handle = Handle.FIELD_TAG_STORE_GET_FIELD)
    public static synchronized Tag getField(Object receiver, String fieldReference) {
        HashMap<String, Tag> tags = getInstanceTagsInternal(receiver, null);
        return tags == null ? Tag.getEmptyTag() : tags.get(fieldReference);
    }

    private static synchronized HashMap<String, Tag> getInstanceTagsInternal(Object receiver, Tag tag) {
        if (staticFieldTags != null && TagStoreFlagAccessor.reserve()) {
            try {
                HashMap<String, Tag> tags = instanceFieldTags.get(receiver);
                if (tags == null && !Tag.isEmpty(tag)) {
                    tags = new HashMap<>();
                    instanceFieldTags.put(receiver, tags);
                }
                return tags;
            } finally {
                TagStoreFlagAccessor.free();
            }
        }
        return null;
    }

    public static synchronized HashMap<String, Tag> getInstanceTags(Object receiver) {
        HashMap<String, Tag> tags = getInstanceTagsInternal(receiver, null);
        return tags == null ? null : new HashMap<>(tags);
    }

    public static synchronized void setInstanceTags(Object receiver, HashMap<String, Tag> tags) {
        if (staticFieldTags != null && TagStoreFlagAccessor.reserve()) {
            try {
                instanceFieldTags.put(receiver, new HashMap<>(tags));
            } finally {
                TagStoreFlagAccessor.free();
            }
        }
    }

    public static synchronized void clear() {
        if (staticFieldTags != null && TagStoreFlagAccessor.reserve()) {
            try {
                instanceFieldTags.clear();
                staticFieldTags.clear();
            } finally {
                TagStoreFlagAccessor.free();
            }
        }
    }

    public static synchronized void initialize() {
        if (staticFieldTags == null) {
            WeakIdentityHashMap.ensureDependenciesLoaded();
            instanceFieldTags = new WeakIdentityHashMap<>();
            staticFieldTags = new HashMap<>();
        }
    }
}
