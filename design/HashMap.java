/**
 * Basic HashMap implementation with fundamental operations
 */
public class HashMap<K, V> {
    // Entry class for key-value pairs
    private class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
    
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    private Entry<K, V>[] buckets;
    private int size;
    private float loadFactor;
    
    // Constructor
    @SuppressWarnings("unchecked")
    public HashMap() {
        this.buckets = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY];
        this.size = 0;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }
    
    // Get size
    public int size() {
        return size;
    }
    
    // Check if empty
    public boolean isEmpty() {
        return size == 0;
    }
    
    // Get the index in the bucket array
    private int getIndex(K key) {
        int hashCode = key.hashCode();
        return (hashCode & 0x7FFFFFFF) % buckets.length;
    }
    
    // Put a key-value pair
    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        // Check if we need to resize
        if (size >= buckets.length * loadFactor) {
            resize();
        }
        
        int index = getIndex(key);
        Entry<K, V> entry = buckets[index];
        
        // Check if key already exists
        while (entry != null) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = value; // Update value
                return oldValue;
            }
            entry = entry.next;
        }
        
        // Insert new entry at the beginning of the bucket
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = buckets[index];
        buckets[index] = newEntry;
        size++;
        
        return null;
    }
    
    // Get value for a key
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        int index = getIndex(key);
        Entry<K, V> entry = buckets[index];
        
        while (entry != null) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        
        return null; // Key not found
    }
    
    // Remove a key-value pair
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        int index = getIndex(key);
        Entry<K, V> current = buckets[index];
        Entry<K, V> prev = null;
        
        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = current.next; // Removing head of the bucket
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        
        return null; // Key not found
    }
    
    // Check if the hash map contains a key
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        int index = getIndex(key);
        Entry<K, V> entry = buckets[index];
        
        while (entry != null) {
            if (entry.key.equals(key)) {
                return true;
            }
            entry = entry.next;
        }
        
        return false;
    }
    
    // Clear all entries
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
        size = 0;
    }
    
    // Resize the bucket array
    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = (Entry<K, V>[]) new Entry[oldBuckets.length * 2];
        size = 0;
        
        // Rehash all existing entries
        for (Entry<K, V> entry : oldBuckets) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }
}
