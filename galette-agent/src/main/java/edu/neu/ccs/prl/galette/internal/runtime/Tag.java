package edu.neu.ccs.prl.galette.internal.runtime;

import edu.neu.ccs.prl.galette.internal.runtime.collection.Iterator;
import edu.neu.ccs.prl.galette.internal.runtime.collection.ObjectIntMap;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import java.io.*;

/**
 * An immutable set of labels.
 */
public final class Tag implements Serializable {
    private static final long serialVersionUID = -1353943194836946961L;
    private transient ObjectIntMap<Object> backingMap;

    private Tag(Object label) {
        this.backingMap = new ObjectIntMap<>();
        backingMap.put(label, 1);
    }

    private Tag(ObjectIntMap<Object> backingMap) {
        if (backingMap == null) {
            throw new NullPointerException();
        }
        this.backingMap = backingMap;
    }

    public int size() {
        return backingMap.size();
    }

    public boolean isEmpty() {
        return backingMap.isEmpty();
    }

    public boolean contains(Object element) {
        return backingMap.containsKey(element);
    }

    public Object[] getLabels() {
        return backingMap.getKeys().toArray(new Object[backingMap.size()]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Tag)) {
            return false;
        }
        Tag tag = (Tag) o;
        return backingMap.equals(tag.backingMap);
    }

    @Override
    public int hashCode() {
        return backingMap.hashCode();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(size() * 28).append('{');
        Iterator<ObjectIntMap.Entry<Object>> it = backingMap.entryIterator();
        while (it.hasNext()) {
            ObjectIntMap.Entry<Object> entry = it.next();
            buffer.append(entry.getKey() == this ? "(this Tag)" : entry.getKey());
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
        return buffer.append('}').toString();
    }

    @InvokedViaHandle(handle = Handle.TAG_GET_EMPTY)
    public static Tag getEmptyTag() {
        return null;
    }

    @InvokedViaHandle(handle = Handle.TAG_UNION)
    public static Tag union(Tag t1, Tag t2) {
        if (isEmpty(t1) || t1 == t2) {
            return t2;
        } else if (isEmpty(t2)) {
            return t1;
        } else {
            ObjectIntMap<Object> labels = new ObjectIntMap<>(t1.backingMap);
            SimpleList<Object> keys = t2.backingMap.getKeys();
            for (int i = 0; i < keys.size(); i++) {
                labels.put(keys.get(i), 1);
            }
            return new Tag(labels);
        }
    }

    public static Tag of(Object label) {
        return new Tag(label);
    }

    public static Tag of(Object... labels) {
        ObjectIntMap<Object> distinct = new ObjectIntMap<>();
        for (Object label : labels) {
            distinct.put(label, 1);
        }
        return new Tag(distinct);
    }

    public static boolean isEmpty(Tag tag) {
        return tag == null || tag.isEmpty();
    }

    public static Object[] getLabels(Tag tag) {
        return tag == null ? new Object[0] : tag.getLabels();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        Object[] labels = getLabels();
        out.writeInt(getLabels().length);
        for (Object label : labels) {
            out.writeObject(label);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        backingMap = new ObjectIntMap<>();
        int length = in.readInt();
        for (int i = 0; i < length; i++) {
            Object label = in.readObject();
            backingMap.put(label, 1);
        }
    }
}
