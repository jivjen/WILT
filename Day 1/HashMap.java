

public class HashMap<K, V> {

    Entry table[];

    int threshold;

    int capacity = 16;

    float loadFactor = 0.75f;

    int size = 0;

    public HashMap(int capacity) {
        this.capacity = capacity;
    }

    public HashMap(int loadFactor, int capacity) {
        this.loadFactor = loadFactor;
        this.capacity = capacity;
    }

    public HashMap() {
        threshold = (int) (capacity * loadFactor);
        table = new Entry[capacity];
    }

    public V get(K key) {
        // Generation of the index
        int hash = hash(key.hashCode());
        int index = getIndex(hash, capacity);
        // If no element is present at the index return null
        if (table[index] == null) {
            return null;
        } else {
            // Iterate over the linked list and return if the key matches
            Entry current = table[index];
            while (current != null) {
                if (current.getKey() == key) {
                    return (V) current.getValue();
                }
                current = current.getNext();
            }
            return null;
        }
    }

    public void put(K key, V value) {
        int hash = hash(key.hashCode());
        int index = getIndex(hash, capacity);
        Entry entry = new Entry(key, value, null);
        // If there is no value already present at that index, then just insert the new entry there
        if (table[index] == null) {
            table[index] = entry;
        }
        // In case of a value already being present at the particular index
        else {
            Entry currentEntry = table[index];
            Entry previousEntry = null;
            // Iterate through the Linked List
            while (currentEntry != null) {
                // If there exists a node with the key already present, then replace its value
                if (currentEntry.getKey().equals(hash)) {
                    currentEntry.setValue(value);
                    size++;
                    if (size > threshold) {
                        resize(capacity * 2);
                    }
                    return;
                }
                previousEntry = currentEntry;
                currentEntry = currentEntry.getNext();
            }
            // If key wasn't present then this would be the last occupied node so set the new entry as its next node
            if (previousEntry != null) {
                previousEntry.setNext(entry);
                size++;
                if (size > threshold) {
                    resize(capacity * 2);
                }
            }
        }
    }

    public V remove(K key) {
        int hash = hash(key.hashCode());
        int index = getIndex(hash, capacity);
        if (table[index] != null) {
            size--;
            Entry current = table[index];
            Entry previous = table[index];
            while (current != null) {
                Entry next = current.getNext();
                if (current.getKey().equals(key)) {
                    if (previous == current) {
                        table[index] = next;
                    } else {
                        previous.setNext(next);
                    }
                }
                previous = current;
                current = current.getNext();
            }
            return (V) current.getValue();
        } else {
            return null;
        }
    }

    private void resize(int newCapacity) {
        Entry[] old = table;
        int oldCapacity = old.length;
        Entry[] newTable = new Entry[newCapacity];
        for (int i = 0; i < old.length; i++) {
            Entry e = old[i];
            if (e != null) {
                old[i] = null;
                do {
                    Entry next = e.getNext();
                    int index = getIndex(next.hashCode(), newCapacity);
                    e.setNext(newTable[index]);
                    newTable[index] = e;
                    e = next;
                } while (e != null);
            }
        }
    }

    //Straight from the JDK implementation
    private int hash(int hashcode) {
        hashcode ^= (hashcode >>> 20) ^ (hashcode >>> 12);
        return hashcode ^ (hashcode >>> 7) ^ (hashcode >>> 4);
    }

    private int getIndex(int hash, int capacity) {
        return hash & (capacity - 1);
    }


}

