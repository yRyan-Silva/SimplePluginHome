package me.ryan.simplepluginhome.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public abstract class Registry<K, V> {

    private ConcurrentHashMap<K, V> elements;

    public Registry() {
        this.elements = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<K, V> getElements() {
        if (this.elements == null) this.elements = new ConcurrentHashMap<>();
        return this.elements;
    }

    public void setElements(ConcurrentHashMap<K, V> elements) {
        this.elements = elements;
    }

    public boolean containsKey(K key) {
        return this.elements.containsKey(key);
    }

    public boolean containsValue(V value) {
        return this.elements.containsValue(value);
    }

    public void register(K key, V value) {
        if (value == null) return;
        this.elements.put(key, value);
    }

    public void register(Map<K, V> elements) {
        this.elements.putAll(elements);
    }

    public V unregister(K key) {
        return this.elements.remove(key);
    }

    @SafeVarargs
    public final void unregister(K... keyArg) {
        for (K k : keyArg) this.elements.remove(k);
    }

    public V getByKey(Predicate<K> predicate) {
        if (this.elements == null) this.elements = new ConcurrentHashMap<>();

        for (Map.Entry<K, V> entry : this.elements.entrySet())
            if (predicate.test(entry.getKey())) return entry.getValue();
        return null;
    }

    public V getByValue(Predicate<V> predicate) {
        if (this.elements == null) this.elements = new ConcurrentHashMap<>();
        for (V v : this.elements.values())
            if (predicate.test(v)) return v;
        return null;
    }

    public V getByKey(K k) {
        if (k == null) return null;
        return this.elements.get(k);
    }

    public V getByKey(K k, V def) {
        if (k == null) return def;
        return this.elements.getOrDefault(k, def);
    }

    public List<V> getByKeyElements(Predicate<K> predicate) {
        final List<V> array = new ArrayList<>();
        if (this.elements == null) this.elements = new ConcurrentHashMap<>();

        for (Map.Entry<K, V> entry : this.elements.entrySet())
            if (predicate.test(entry.getKey())) array.add(entry.getValue());
        return array;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return new ConcurrentHashMap<>(this.elements).entrySet();
    }

    public int size() {
        return this.elements.size();
    }

    public void removeIf(Predicate<K> predicate) {
        if (predicate == null) return;
        for (K key : new ConcurrentHashMap<>(this.elements).keySet())
            if (predicate.test(key)) this.elements.remove(key);
    }

    public List<V> getValues() {
        return new ArrayList<>(this.elements.values());
    }

    public List<K> getKeys() {
        return new ArrayList<>(this.elements.keySet());
    }

}