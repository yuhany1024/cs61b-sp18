package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }

        if (p.key.compareTo(key) == 0) {
            return p.value;
        } else if (p.key.compareTo(key) > 0)  {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }

    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size += 1;
            return new Node(key, value);
        }

        if (p.key.compareTo(key) == 0) {
            p.value = value;
        } else if (p.key.compareTo(key) > 0) {
            p.left = putHelper(key, value, p.left);
        } else if (p.key.compareTo(key) < 0) {
            p.right = putHelper(key, value, p.right);
        }

        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        if (root == null) {
            root = new Node(key, value);
            size += 1;
        } else {
            putHelper(key, value, root);
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    private void keySetHelper(Node p, Set<K> keys) {
        if (p != null) {
            keys.add(p.key);
            keySetHelper(p.left, keys);
            keySetHelper(p.right, keys);
        }
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<K>();
        keySetHelper(root, keys);
        return keys;
    }

    private Node removeHelper(Node p, K key) {
        if (p == null) {
            return null;
        }
        if (p.key.compareTo(key) == 0) {
            if (p.left == null && p.right == null) {
                return null;
            } else if (p.left == null)  {
                return p.right;
            } else if (p.right == null) {
                return p.left;
            } else {
                // have two children
                Node inorderSuccessor = minNode(p.right);
                p.key = inorderSuccessor.key;
                p.value = inorderSuccessor.value;
                p.right = removeHelper(p.right, key);
            }
            size -= 1;
        } else if (p.key.compareTo(key) > 0) {
            p.left = removeHelper(p.left, key);
        } else if (p.key.compareTo(key) < 0) {
            p.right = removeHelper(p.right, key);
        }
        return p;
    }

    private Node minNode(Node p) {
        // find the inorder successor
        if (p.left == null) {
            return p;
        } else {
            return minNode(p.left);
        }

    }
    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        V value = get(key);
        if (value != null) {
            root = removeHelper(root, key);
        }
        return value;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        V value4key = get(key);
        if (value4key == value) {
            root = removeHelper(root, key);
            return value;
        } else {
            return null;
        }
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
