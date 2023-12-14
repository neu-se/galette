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
public class HashMap<K, V> {
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
    private transient Entry<K, V>[] entries;
    /**
     * Track of structural modifications between the HashMap and the iterator.
     */
    private transient int modCount = 0;
    /**
     * The maximum number of elements that can be put in this map before having to rehash.
     */
    private int threshold;

    /**
     * Constructs a new, empty map.
     */
    public HashMap() {
        this(16);
    }

    /**
     * Constructs a new, empty map with the specified capacity.
     *
     * @param capacity the initial capacity of this hash map.
     * @throws IllegalArgumentException when the capacity is less than zero.
     */
    public HashMap(int capacity) {
        if (capacity >= 0) {
            capacity = ObjectIntMap.calculateCapacity(capacity);
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
    public HashMap(HashMap<? extends K, ? extends V> map) {
        this(ObjectIntMap.calculateCapacity(map.size()));
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
     * Returns {@code true} if this map contains an entry for the specified key.
     *
     * @param key the key to be searched for
     * @return {@code true} if this map contains an entry for the specified key
     */
    public boolean containsKey(Object key) {
        Entry<K, V> m = getEntry(key);
        return m != null;
    }

    public Iterator<Entry<K, V>> entryIterator() {
        return new EntryIterator<>(this);
    }

    /**
     * Returns the value of the entry in this map for the specified key.
     *
     * @param key the key to be searched for
     * @return the value of entry for the specified key or {@code null} if there is no entry for the specified key
     */
    public V get(Object key) {
        Entry<K, V> m = getEntry(key);
        if (m == null) {
            return null;
        }
        return m.getValue();
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V>[] newElementArray(int s) {
        return new Entry[s];
    }

    private void computeThreshold() {
        threshold = (int) (entries.length * LOAD_FACTOR);
    }

    private Entry<K, V> getEntry(Object key) {
        int hash = key == null ? 0 : key.hashCode();
        int index = hash & (entries.length - 1);
        Entry<K, V> m = entries[index];
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
    public V put(K key, V value) {
        Entry<K, V> entry = getEntry(key);
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
            return null;
        } else {
            V result = entry.value;
            entry.value = value;
            return result;
        }
    }

    public void putAll(HashMap<? extends K, ? extends V> map) {
        if (!map.isEmpty()) {
            int capacity = size + map.size();
            if (capacity > threshold) {
                rehash(capacity);
            }
            Iterator<? extends Entry<? extends K, ? extends V>> itr = map.entryIterator();
            while (itr.hasNext()) {
                Entry<? extends K, ? extends V> entry = itr.next();
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    private void rehash(int capacity) {
        int x = (capacity == 0 ? 1 : capacity << 1);
        int length = ObjectIntMap.calculateCapacity(x);
        Entry<K, V>[] newData = newElementArray(length);
        for (int i = 0; i < entries.length; i++) {
            Entry<K, V> entry = entries[i];
            entries[i] = null;
            while (entry != null) {
                int index = entry.hash & (length - 1);
                Entry<K, V> next = entry.next;
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
    public boolean remove(Object key) {
        return removeEntry(key) != null;
    }

    private Entry<K, V> removeEntry(Object key) {
        int index = 0;
        Entry<K, V> entry;
        Entry<K, V> last = null;
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
        Iterator<Entry<K, V>> it = entryIterator();
        while (it.hasNext()) {
            result += it.next().hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof HashMap)) {
            return false;
        }
        try {
            @SuppressWarnings("unchecked")
            HashMap<K, V> map = (HashMap<K, V>) object;
            if (size() != map.size()) {
                return false;
            }
            Iterator<Entry<K, V>> itr = entryIterator();
            while (itr.hasNext()) {
                Entry<K, V> entry = itr.next();
                K key = entry.getKey();
                if (!map.containsKey(key) || !objectEquals(entry.getValue(), map.get(key))) {
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
        Iterator<Entry<K, V>> it = entryIterator();
        while (it.hasNext()) {
            Entry<K, V> entry = it.next();
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

    public SimpleList<K> getKeys() {
        SimpleList<K> keys = new SimpleList<>();
        Iterator<Entry<K, V>> itr = entryIterator();
        while (itr.hasNext()) {
            keys.add(itr.next().getKey());
        }
        return keys;
    }

    public static class Entry<K, V> {
        private final int hash;
        private final K key;
        private Entry<K, V> next;
        private V value;

        private Entry(K key, int hash, V value) {
            this.key = key;
            this.hash = hash;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            return hash ^ value.hashCode();
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else if (object instanceof HashMap.Entry) {
                Entry<?, ?> entry = (Entry<?, ?>) object;
                return objectEquals(key, entry.getKey()) && objectEquals(value, entry.value);
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private static class EntryIterator<K, V> implements Iterator<Entry<K, V>> {
        final HashMap<K, V> associatedMap;
        int expectedModCount;
        Entry<K, V> futureEntry;
        Entry<K, V> currentEntry;
        Entry<K, V> prevEntry;
        private int position = 0;

        EntryIterator(HashMap<K, V> map) {
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

        public Entry<K, V> next() {
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
