/*
 * Based on Apache Harmony's (https://harmony.apache.org/) java.util.concurrent.ConcurrentHashMap implementation.
 *
 * Apache Harmony is licensed under the Apache License Version 2.0.
 * A copy of this license may be obtained at:
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package edu.neu.ccs.prl.galette.internal.runtime.collection;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * A weak, identity, concurrent hash table.
 * Uses reference-equality instead of object-equality when comparing keys.
 * Uses "weak" keys, that is, the presence of a key in this map will not prevent that key from being discarded by the
 * garbage collector.
 * When a key is discarded by the garbage collector, its entry in this map is lazily removed.
 * Thread-safe.
 * Supports full concurrency of reads.
 * Does not allow {@code null} keys.
 * Allows {@code null} values.
 *
 * @param <K> the type of keys stored in this map
 * @param <V> the type of values associated with the keys in this map
 */
public final class WeakDataStore<K, V> {
    /**
     * The maximum capacity, used if a higher value is implicitly
     * specified by either of the constructors with arguments.  MUST
     * be a power of two <= 1<<30 to ensure that entries are indexable
     * using ints.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;
    /**
     * The number of entries in this map.
     */
    private volatile int size = 0;

    private final float loadFactor = 0.75f;
    private volatile ReferenceEntry<K, V>[] table = createTable(16);
    /**
     * The maximum number of entries that can be put in this map before having to rehash.
     */
    private int threshold = (int) (table.length * loadFactor);

    private final ReferenceQueue<Object> queue = new ReferenceQueue<>();
    private final Function<K, V> initializer;

    public WeakDataStore(Function<K, V> initializer) {
        ensureDependenciesLoaded();
        this.initializer = initializer;
    }

    public V get(Object key) {
        expungeStaleEntries();
        // Read volatile
        if (size != 0) {
            ReferenceEntry<K, V>[] tab = table;
            int hash = hash(key);
            ReferenceEntry<K, V> e = find(key, hash, tab);
            return e == null ? null : e.value;
        }
        return null;
    }

    public V computeIfAbsent(K key) {
        // Attempt to find an entry without locking
        V value = get(key);
        return value != null ? value : computeIfAbsentInternal(key);
    }

    private synchronized V computeIfAbsentInternal(K key) {
        V value;
        synchronized (this) {
            int c = size;
            if (c++ > threshold) {
                // Ensure capacity
                rehash();
            }
            int hash = hash(key);
            ReferenceEntry<K, V>[] tab = table;
            int index = hash & (tab.length - 1);
            ReferenceEntry<K, V> e = find(key, hash, tab);
            if (e != null) {
                // An existing value was found
                value = e.value;
            } else {
                // Update the map
                value = initializer.apply(key);
                tab[index] = new ReferenceEntry<>(new HashWeakReference<>(key, queue, hash), value, tab[index]);
                // Write volatile
                size = c;
            }
        }
        return value;
    }

    public void clear() {
        if (size != 0) {
            synchronized (this) {
                ReferenceEntry<K, V>[] tab = table;
                for (int i = 0; i < tab.length; i++) {
                    tab[i] = null;
                }
                // Write volatile
                size = 0;
            }
        }
    }

    private void rehash() {
        ReferenceEntry<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity >= MAXIMUM_CAPACITY) {
            return;
        }
        ReferenceEntry<K, V>[] newTable = createTable(oldCapacity << 1);
        threshold = (int) (newTable.length * loadFactor);
        for (ReferenceEntry<K, V> e : oldTable) {
            if (e != null) {
                rehash(e, newTable);
            }
        }
        table = newTable;
    }

    private static <K, V> ReferenceEntry<K, V> find(Object key, int hash, ReferenceEntry<K, V>[] tab) {
        int index = hash & (tab.length - 1);
        ReferenceEntry<K, V> e = tab[index];
        while (e != null) {
            if (e.matches(key, hash)) {
                return e;
            }
            e = e.next;
        }
        return null;
    }

    private static <K, V> void rehash(ReferenceEntry<K, V> oldEntry, ReferenceEntry<K, V>[] newTable) {
        int sizeMask = newTable.length - 1;
        ReferenceEntry<K, V> next = oldEntry.next;
        int index = oldEntry.hash & sizeMask;
        if (next == null) {
            // Singleton
            newTable[index] = oldEntry;
        } else {
            // Reuse trailing consecutive sequence at same slot
            ReferenceEntry<K, V> lastRun = oldEntry;
            int lastIndex = index;
            for (ReferenceEntry<K, V> last = next; last != null; last = last.next) {
                int k = last.hash & sizeMask;
                if (k != lastIndex) {
                    lastIndex = k;
                    lastRun = last;
                }
            }
            newTable[lastIndex] = lastRun;
            // Copy all remaining nodes
            for (ReferenceEntry<K, V> p = oldEntry; p != lastRun; p = p.next) {
                int k = p.hash & sizeMask;
                newTable[k] = new ReferenceEntry<>(p.reference, p.value, newTable[k]);
            }
        }
    }

    private void expungeStaleEntries() {
        for (Reference<?> ref; (ref = queue.poll()) != null; ) {
            expunge((HashWeakReference<?>) ref);
        }
    }

    private synchronized void expunge(HashWeakReference<?> ref) {
        int c = size - 1;
        ReferenceEntry<K, V>[] tab = table;
        int index = ref.hash & (tab.length - 1);
        ReferenceEntry<K, V> head = tab[index];
        ReferenceEntry<K, V> e = head;
        while (e != null && e.reference != ref) {
            e = e.next;
        }
        if (e != null) {
            tab[index] = remove(e, head);
            // Write volatile
            size = c;
        }
    }

    private ReferenceEntry<K, V> remove(ReferenceEntry<K, V> entry, ReferenceEntry<K, V> head) {
        // Remove the specified entry; create new entries for the elements before the specified entry to
        // change their next fields
        ReferenceEntry<K, V> newHead = entry.next;
        for (ReferenceEntry<K, V> p = head; p != entry; p = p.next) {
            newHead = new ReferenceEntry<>(p.reference, p.value, newHead);
        }
        return newHead;
    }

    @SuppressWarnings("unchecked")
    private ReferenceEntry<K, V>[] createTable(int i) {
        return new ReferenceEntry[i];
    }

    private static class ReferenceEntry<K, V> {
        private final HashWeakReference<K> reference;
        private final V value;
        private final int hash;
        private final ReferenceEntry<K, V> next;

        private ReferenceEntry(HashWeakReference<K> reference, V value, ReferenceEntry<K, V> next) {
            this.reference = reference;
            this.value = value;
            this.hash = reference.hash;
            this.next = next;
        }

        private boolean matches(Object key, int hash) {
            return (hash == this.hash) && (key != null && reference.get() == key);
        }
    }

    private static class HashWeakReference<K> extends WeakReference<K> {
        private final int hash;

        HashWeakReference(K key, ReferenceQueue<? super K> queue, int hash) {
            super(key, queue);
            this.hash = hash;
        }
    }

    static int hash(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
        int h = System.identityHashCode(o);
        // Spread bits to regularize both segment and index locations,
        // using variant of single-word Wang/Jenkins hash.
        // Necessary because power-of-two length hash tables encounter collisions for hashes that do not
        // differ in lower or upper bits.
        h += (h << 15) ^ 0xffffcd7d;
        h ^= (h >>> 10);
        h += (h << 3);
        h ^= (h >>> 6);
        h += (h << 2) + (h << 14);
        return h ^ (h >>> 16);
    }

    public static void ensureDependenciesLoaded() {
        Object[] dependencies = new Object[] {
            System.class, Function.class, ReferenceEntry.class, HashWeakReference.class, ReferenceQueue.class
        };
    }
}
