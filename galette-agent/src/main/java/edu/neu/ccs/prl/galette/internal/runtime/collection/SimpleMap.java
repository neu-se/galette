package edu.neu.ccs.prl.galette.internal.runtime.collection;

public class SimpleMap<K, V> {
    private final ObjectIntMap<K> backingMap = new ObjectIntMap<>();
    private final SimpleList<V> values = new SimpleList<>();

    public ObjectIntMap<K> getBackingMap() {
        return backingMap;
    }

    public SimpleList<V> getValues() {
        return values;
    }

    public boolean containsKey(Object key) {
        return backingMap.containsKey(key);
    }

    public V get(Object key) {
        int index = backingMap.getOrDefault(key, -1);
        return index == -1 ? null : values.get(index);
    }

    public boolean isEmpty() {
        return backingMap.isEmpty();
    }

    public V put(K key, V value) {
        if (!backingMap.containsKey(key)) {
            backingMap.put(key, values.size());
            values.add(value);
            return null;
        } else {
            int index = backingMap.get(key);
            V result = values.get(index);
            values.set(index, value);
            return result;
        }
    }

    public int size() {
        return backingMap.size();
    }

    public SimpleList<K> getKeys() {
        return backingMap.getKeys();
    }
}
