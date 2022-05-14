import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    private Node trieRoot;
    private Map<Character, BitSequence> lookupTable = new HashMap<>();

    private static class Node implements Comparable<Node>, Serializable {
        private char ch;
        private int freq;
        private Node left;
        private Node right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        private boolean isLeaf() {
            return left == null && right == null;
        }

        public int compareTo(Node that) {
            return this.freq - that.freq;
        }

    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> pq = new MinPQ<>();
        for (char c: frequencyTable.keySet()) {
            int freq = frequencyTable.get(c);
            if (freq > 0) {
                Node node = new Node(c, freq, null, null);
                pq.insert(node);
            }
        }

        while (pq.size() > 1) {
            Node node1 = pq.delMin();
            Node node2 = pq.delMin();
            Node parent = new Node('\0', node1.freq + node2.freq, node1, node2);
            pq.insert(parent);
        }

        trieRoot = pq.delMin();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node node = trieRoot;
        int i = 0;
        BitSequence bs = new BitSequence();
        while (!node.isLeaf()) {
            int bit = querySequence.bitAt(i);
            if (bit == 0) {
                node = node.left;
            } else {
                node = node.right;
            }
            bs = bs.appended(bit);
            i++;
        }
        return new Match(bs, node.ch);
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Node node = trieRoot;
        BitSequence bs = new BitSequence();
        backTracking(node, bs);
        return lookupTable;
    }

    private void backTracking(Node node, BitSequence bs) {
        if (node.isLeaf()) {
            lookupTable.put(node.ch, bs);
        } else {
            BitSequence leftBs = new BitSequence(bs);
            leftBs = leftBs.appended(0);
            backTracking(node.left, leftBs);

            BitSequence rightBs = new BitSequence(bs);
            rightBs = rightBs.appended(1);
            backTracking(node.right, rightBs);
        }

    }


}
