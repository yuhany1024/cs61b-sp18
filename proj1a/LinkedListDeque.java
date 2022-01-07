/** Linked list deque.
 * @author Yuhan.
 * */
public class LinkedListDeque<T> {
    /** The size of the list. */
    private int size;
    /** The front sentinel node of the list. */
    private Node sentFront;
    /** The last sentinel node of the list. */
    private Node sentLast;

    /** Define the class of node. */
    private class Node {
        /** The item of the node. */
        private T item;
        /** The previous node. */
        private Node prev;
        /** The next node. */
        private Node next;

        /** create a node.
         * @param i : item
         * @param p : the previous node
         *  @param n : the next node. */
        Node(T i, Node p, Node n) {
            this.item = i;
            this.prev = p;
            this.next = n;
        }
    }

    /** create a linkedListDeque. */
    public LinkedListDeque() {
        size = 0;
        sentFront = new Node(null, null, null);
        sentLast = new Node(null, sentFront, null);
        sentFront.next = sentLast;
    }

    /** Adds an @param item of type T to the front of the deque. */
    public void addFirst(T item) {
        Node newNode = new Node(item, sentFront, sentFront.next);
        sentFront.next.prev = newNode;
        sentFront.next = newNode;
        size += 1;
    }

    /** Adds an @param item of type T to the back of the deque. */
    public void addLast(T item) {
        Node newNode = new Node(item, sentLast.prev, sentLast);
        sentLast.prev.next = newNode;
        sentLast.prev = newNode;
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
        Node currNode = sentFront.next;
        while (currNode != null) {
            System.out.print(currNode.item + " ");
            currNode = currNode.next;
        }
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node nodeToRemove = sentFront.next;
        T removedVal = nodeToRemove.item;
        nodeToRemove.item = null;
        sentFront.next = sentFront.next.next;
        sentFront.next.prev = sentFront;
        size -= 1;
        return removedVal;


    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. */
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        Node nodeToRemove = sentLast.prev;
        T removedVal = nodeToRemove.item;
        nodeToRemove.item = null;
        sentLast.prev = nodeToRemove.prev;
        nodeToRemove.prev.next = sentLast;
        size -= 1;
        return removedVal;

    }

    /** Gets the item at the given @param index , where 0 is the front,
     * 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!*/
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node p = sentFront.next;
        while (index > 0) {
            p = p.next;
            index -= 1;
        }
        return p.item;
    }

    /** Same as get, but uses recursion.
     * return the item at @param index .
     * */
    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursive(sentFront.next, index);
    }
    /** A helper function for getRecursive
     * return the item starting from @param head and
     * at @param index .
     * */
    private T getRecursive(Node head, int index) {
        if (index == 0) {
            return head.item;
        }
        return getRecursive(head.next, index - 1);
    }


}