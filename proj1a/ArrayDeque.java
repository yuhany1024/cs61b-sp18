public class ArrayDeque<T> {

    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;

    public ArrayDeque() {
        items = (T []) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int first = (nextFirst + 1 + items.length) % items.length;
        int last = (nextLast - 1 + items.length) % items.length;
        if (first <= last) {
            System.arraycopy(items, first, a, 0, size);
        } else {
            System.arraycopy(items, first, a, 0, items.length - first);
            System.arraycopy(items, 0, a, items.length - first, size - items.length + first);
        }

        nextFirst = a.length - 1;
        nextLast = size;
        items = a;
    }

    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[nextFirst] = item;
        nextFirst = (nextFirst - 1 + items.length) % items.length;
        size += 1;
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[nextLast] = item;
        nextLast = (nextLast + 1 + items.length) % items.length;
        size += 1;
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return (size == 0);
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, s
     * eparated by a space. */
    public void printDeque() {
        int index = 0;
        while (index < size) {
            System.out.print(get(index));
            index += 1;
        }
    }

    private void shrink(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int first = (nextFirst + 1 + items.length) % items.length;
        int last = (nextLast - 1 + items.length) % items.length;

        if (first <= last) {
            System.arraycopy(items, first, a, 0, size);
        } else {
            System.arraycopy(items, first, a, 0, items.length - first);
            System.arraycopy(items, 0, a, items.length - first, size - items.length + first);
        }
        nextFirst = capacity - 1;
        nextLast = size;
        items = a;

    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. */
    public T removeFirst() {
        T valueToRemove;
        if (size == 0) {
            return null;
        }
        int first = (nextFirst + 1 + items.length) % items.length;
        valueToRemove = items[first];
        items[first] = null;
        nextFirst = first;
        size -= 1;

        if (items.length >= 16 && size < items.length * 0.25) {
            int capacity = (int) (items.length * 0.5);
            shrink(capacity);
        }

        return valueToRemove;
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. */
    public T removeLast() {
        T valueToRemove;
        if (size == 0) {
            return null;
        }
        int last = (nextLast - 1 + items.length) % items.length;

        valueToRemove = items[last];
        items[last] = null;
        nextLast = last;
        size -= 1;

        if (items.length >= 16 && size < items.length * 0.25) {
            int capacity = (int) (items.length * 0.5);
            shrink(capacity);
        }

        return valueToRemove;
    }

    /** Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!*/
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return items[(nextFirst + index + 1) % items.length];
    }

}

