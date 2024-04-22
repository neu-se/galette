package edu.neu.ccs.prl.galette.internal.runtime.collection;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeakIdentityHashMap<K, V> {
    private final ReferenceQueue<Object> queue = new ReferenceQueue<>();
    private final HashMap<IdentityWeakReference<K>, V> map = new HashMap<>();

    public boolean containsKey(Object key) {
        expungeStaleEntries();
        return map.containsKey(new IdentityWeakReference<>(key, queue));
    }

    public V get(Object key) {
        expungeStaleEntries();
        return map.get(new IdentityWeakReference<>(key, queue));
    }

    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("WeakIdentityHashMap cannot have null keys");
        }
        expungeStaleEntries();
        return map.put(new IdentityWeakReference<>(key, queue), value);
    }

    public void clear() {
        expungeStaleEntries();
        map.clear();
    }

    private void expungeStaleEntries() {
        for (Reference<?> ref; (ref = queue.poll()) != null; ) {
            map.remove(ref);
        }
    }

    private static class IdentityWeakReference<T> extends WeakReference<T> {
        private final int hashCode;

        IdentityWeakReference(T referent, ReferenceQueue<? super T> queue) {
            super(referent, queue);
            hashCode = System.identityHashCode(referent);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof IdentityWeakReference)) {
                return false;
            }
            IdentityWeakReference<?> that = (IdentityWeakReference<?>) o;
            T referent = this.get();
            return (hashCode == that.hashCode) && referent != null && referent == that.get();
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }

    public static void ensureDependenciesLoaded() {
        Object[] dependencies = new Object[] {
            WeakIdentityHashMap.class,
            ObjectIntMap.class,
            System.class,
            HashMap.class,
            HashMap.Entry.class,
            IdentityWeakReference.class,
            ReferenceQueue.class
        };
    }
}
