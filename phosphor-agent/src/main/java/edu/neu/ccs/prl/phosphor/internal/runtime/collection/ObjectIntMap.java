/**
 * Based on Apache Harmony's (https://harmony.apache.org/) HashMap.
 * <p>
 * Apache Harmony is licensed under the Apache License Version 2.0.
 * A copy of this license may be obtained at:
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package edu.neu.ccs.prl.phosphor.internal.runtime.collection;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Null key values are supported.
 */
public class ObjectIntMap<K> {
    /**
     * The maximum ratio of stored elements to storage size that does not lead to rehash.
     */
    private static final float LOAD_FACTOR = 0.75f;
    /**
     * The number of entries in this map.
     */
    private transient int size;
    /**
     * Holds the entries in this map.
     */
    private transient Entry<K>[] entries;
    /**
     * Track of structural modifications between the ObjectIntMap and the iterator.
     */
    private transient int modCount = 0;
    /**
     * The maximum number of elements that can be put in this map before having to rehash.
     */
    private int threshold;

    /**
     * Constructs a new, empty map.
     */
    public ObjectIntMap() {
        this(16);
    }

    /**
     * Constructs a new, empty map with the specified capacity.
     *
     * @param capacity the initial capacity of this hash map.
     * @throws IllegalArgumentException when the capacity is less than zero.
     */
    public ObjectIntMap(int capacity) {
        if (capacity >= 0) {
            capacity = calculateCapacity(capacity);
            size = 0;
            entries = newElementArray(capacity);
            computeThreshold();
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Constructs a new, empty map containing the entries from
     * the specified map.
     *
     * @param map the map whose entries are to be added to the constructed map
     * @throws NullPointerException if the specified map is null
     */
    public ObjectIntMap(ObjectIntMap<? extends K> map) {
        this(calculateCapacity(map.size()));
        putAll(map);
    }

    /**
     * Returns true if this map contains no entries.
     *
     * @return true if this map contains no entries
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all mappings from this map.
     */
    public void clear() {
        if (size > 0) {
            size = 0;
            for (int i = 0; i < entries.length; i++) {
                ((Object[]) entries)[i] = null;
            }
            modCount++;
        }
    }

    /**
     * Returns true if this map contains an entry for the specified key.
     *
     * @param key the key to be searched for
     * @return true if this map contains an entry for the specified key
     */
    public boolean containsKey(Object key) {
        Entry<K> m = getEntry(key);
        return m != null;
    }

    public Iterator<Entry<K>> entryIterator() {
        return new EntryIterator<>(this);
    }

    /**
     * Returns the value of the entry in this map for the specified key.
     *
     * @param key the key to be searched for
     * @return the value of entry for the specified key
     * @throws NoSuchElementException if there is no entry for the specified key
     */
    public int get(K key) {
        Entry<K> m = getEntry(key);
        if (m == null) {
            throw new NoSuchElementException();
        }
        return m.getValue();
    }

    /**
     * Returns the value of the entry in this map for the specified key or the specified default value if
     * there is no entry for the specified key.
     *
     * @param key the key to be searched for
     * @return the value of entry for the specified key or the specified default value if there is no entry for
     * the specified key
     */
    public int getOrDefault(Object key, int defaultValue) {
        Entry<K> m = getEntry(key);
        if (m == null) {
            return defaultValue;
        }
        return m.getValue();
    }

    @SuppressWarnings("unchecked")
    private Entry<K>[] newElementArray(int s) {
        return new Entry[s];
    }

    private void computeThreshold() {
        threshold = (int) (entries.length * LOAD_FACTOR);
    }

    private Entry<K> getEntry(Object key) {
        int hash = key == null ? 0 : key.hashCode();
        int index = hash & (entries.length - 1);
        Entry<K> m = entries[index];
        while (m != null && (m.hash != hash || !objectEquals(key, m.getKey()))) {
            m = m.next;
        }
        return m;
    }

    /**
     * Maps the specified key to the specified value.
     *
     * @param key   the key
     * @param value the value
     */
    public void put(K key, int value) {
        Entry<K> entry = getEntry(key);
        if (entry == null) {
            modCount++;
            int hash = key == null ? 0 : key.hashCode();
            int index = hash & (entries.length - 1);
            entry = new Entry<>(key, hash, value);
            entry.next = entries[index];
            entries[index] = entry;
            if (++size > threshold) {
                rehash(entries.length);
            }
        }
        entry.value = value;
    }

    public void putAll(ObjectIntMap<? extends K> map) {
        if (!map.isEmpty()) {
            int capacity = size + map.size();
            if (capacity > threshold) {
                rehash(capacity);
            }
            Iterator<? extends Entry<? extends K>> itr = map.entryIterator();
            while (itr.hasNext()) {
                Entry<? extends K> entry = itr.next();
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    private void rehash(int capacity) {
        int length = calculateCapacity((capacity == 0 ? 1 : capacity << 1));
        Entry<K>[] newData = newElementArray(length);
        for (int i = 0; i < entries.length; i++) {
            Entry<K> entry = entries[i];
            entries[i] = null;
            while (entry != null) {
                int index = entry.hash & (length - 1);
                Entry<K> next = entry.next;
                entry.next = newData[index];
                newData[index] = entry;
                entry = next;
            }
        }
        entries = newData;
        computeThreshold();
    }

    /**
     * Removes the entry with the specified key from this map.
     *
     * @param key the key of the entry to remove
     * @return true if an entry was removed
     */
    public boolean remove(K key) {
        return removeEntry(key) != null;
    }

    private Entry<K> removeEntry(K key) {
        int index = 0;
        Entry<K> entry;
        Entry<K> last = null;
        if (key != null) {
            int hash = key.hashCode();
            index = hash & (entries.length - 1);
            entry = entries[index];
            while (entry != null && !(entry.hash == hash && objectEquals(key, entry.key))) {
                last = entry;
                entry = entry.next;
            }
        } else {
            entry = entries[0];
            while (entry != null && entry.key != null) {
                last = entry;
                entry = entry.next;
            }
        }
        if (entry == null) {
            return null;
        }
        if (last == null) {
            entries[index] = entry.next;
        } else {
            last.next = entry.next;
        }
        modCount++;
        size--;
        return entry;
    }

    /**
     * Returns the number of entries in this map.
     *
     * @return the number of entries in this map
     */
    public int size() {
        return size;
    }

    @Override
    public int hashCode() {
        int result = 0;
        Iterator<Entry<K>> it = entryIterator();
        while (it.hasNext()) {
            result += it.next().hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ObjectIntMap)) {
            return false;
        }
        try {
            @SuppressWarnings("unchecked")
            ObjectIntMap<K> map = (ObjectIntMap<K>) object;
            if (size() != map.size()) {
                return false;
            }
            Iterator<Entry<K>> itr = entryIterator();
            while (itr.hasNext()) {
                Entry<K> entry = itr.next();
                K key = entry.getKey();
                if (!map.containsKey(key) || entry.getValue() != map.get(key)) {
                    return false;
                }
            }
        } catch (NullPointerException | ClassCastException e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(size() * 28).append('{');
        Iterator<Entry<K>> it = entryIterator();
        while (it.hasNext()) {
            Entry<K> entry = it.next();
            buffer.append(entry.getKey() == this ? "(this Map)" : entry.getKey())
                    .append('=')
                    .append(entry.getValue());
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
        return buffer.append('}').toString();
    }

    private static boolean objectEquals(Object o1, Object o2) {
        //noinspection EqualsReplaceableByObjectsCall
        return (o1 == o2) || (o1 != null && o1.equals(o2));
    }

    private static int calculateCapacity(int x) {
        if (x >= 1 << 30) {
            return 1 << 30;
        }
        if (x == 0) {
            return 16;
        }
        x = x - 1;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x + 1;
    }

    public static class Entry<K> {
        private final int hash;
        private final K key;
        private Entry<K> next;
        private int value;

        private Entry(K key, int hash, int value) {
            this.key = key;
            this.hash = hash;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public int getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            return hash ^ value;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else if (object instanceof ObjectIntMap.Entry) {
                Entry<?> entry = (Entry<?>) object;
                return objectEquals(key, entry.getKey()) && value == entry.value;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private static class EntryIterator<K> implements Iterator<Entry<K>> {
        final ObjectIntMap<K> associatedMap;
        int expectedModCount;
        Entry<K> futureEntry;
        Entry<K> currentEntry;
        Entry<K> prevEntry;
        private int position = 0;

        EntryIterator(ObjectIntMap<K> map) {
            this.associatedMap = map;
            this.expectedModCount = map.modCount;
            this.futureEntry = null;
        }

        public boolean hasNext() {
            if (futureEntry != null) {
                return true;
            }
            while (position < associatedMap.entries.length) {
                if (associatedMap.entries[position] == null) {
                    position++;
                } else {
                    return true;
                }
            }
            return false;
        }

        public Entry<K> next() {
            makeNext();
            return currentEntry;
        }

        public final void remove() {
            checkConcurrentMod();
            if (currentEntry == null) {
                throw new IllegalStateException();
            }
            if (prevEntry == null) {
                int index = currentEntry.hash & (associatedMap.entries.length - 1);
                associatedMap.entries[index] = associatedMap.entries[index].next;
            } else {
                prevEntry.next = currentEntry.next;
            }
            currentEntry = null;
            expectedModCount++;
            associatedMap.modCount++;
            associatedMap.size--;
        }

        final void checkConcurrentMod() throws ConcurrentModificationException {
            if (expectedModCount != associatedMap.modCount) {
                throw new ConcurrentModificationException();
            }
        }

        final void makeNext() {
            checkConcurrentMod();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (futureEntry == null) {
                currentEntry = associatedMap.entries[position++];
                futureEntry = currentEntry.next;
                prevEntry = null;
            } else {
                if (currentEntry != null) {
                    prevEntry = currentEntry;
                }
                currentEntry = futureEntry;
                futureEntry = futureEntry.next;
            }
        }
    }
}
