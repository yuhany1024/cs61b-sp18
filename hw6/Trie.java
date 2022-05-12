import java.util.HashMap;

public class Trie {
    TrieNode root;

    static class TrieNode {
        HashMap<Character, TrieNode> children;
        boolean isWord;

        TrieNode() {
            children = new HashMap<>();
            isWord = false;
        }

        boolean containChar(char c) {
            return children.containsKey(c);
        }

        TrieNode nextNode(char c) {
            return children.get(c);
        }

        void putChar(char c, TrieNode n) {
            children.put(c, n);
        }

    }

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode currtNode = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!currtNode.containChar(c)) {
                TrieNode n = new TrieNode();
                currtNode.putChar(c, n);
            }
            if (i == word.length() - 1) {
                currtNode.isWord = true;
            }
            currtNode = currtNode.nextNode(c);
        }
    }

    public void insert(String[] sList) {
        for (String s: sList) {
            insert(s);
        }
    }

    public boolean contain(String word) {
        TrieNode currtNode = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!currtNode.containChar(c)) {
                return false;
            }
            if (i == word.length() - 1) {
                return currtNode.isWord;
            }
            currtNode = currtNode.nextNode(c);
        }
        return false;
    }

    public static void main(String[] args) {
        String[] sList = new String[] {"app", "apple"};
        Trie trie = new Trie();
        trie.insert(sList);
        System.out.println(trie.contain("pp"));
    }

}
