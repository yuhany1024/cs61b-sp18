package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        this.capacity = capacity;
        this.first = 0;
        this.last = 0;
        this.fillCount =  0;
        rb = (T[]) new Object[capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (isFull())  {
            throw new RuntimeException("Ring buffer overflow");
        } else {
            rb[last] = x;
            fillCount += 1;
            last += 1;
            last %= capacity;
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        T firstItem = rb[first];
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        } else {
            rb[first] = null;
            first += 1;
            first %= capacity;
            fillCount -= 1;
        }
        return firstItem;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        return rb[first];
    }

    @Override
    public Iterator<T> iterator() {
        return new BufferIterator();
    }

    private class BufferIterator implements Iterator<T> {
        private int magicWizard = first;

        @Override
        public boolean hasNext() {
            if (first == last) {
                return false;
            } else if (first < last) {
                return magicWizard < last;
            } else {
                return magicWizard >= first || magicWizard < last;
            }
        }

        @Override
        public T next() {
            T next =  rb[magicWizard];
            magicWizard += 1;
            magicWizard %= capacity;
            return next;
        }
    }
}
