import java.util.TreeSet;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Comparator;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    static String[] dictWords;
    static int m;
    static int n;
    static Trie dictTrie;
    static Set<String> dict;

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // YOUR CODE HERE
        if (k <= 0) {
            throw new IllegalArgumentException();
        }
        loadDict();
        dictTrie = new Trie();
        dictTrie.insert(dictWords);
        dict = new TreeSet<>(Arrays.asList(dictWords));

        char[][] board = loadBoard(boardFilePath);
        m = board.length;
        n = board[0].length;
        List<String> res = bfs(board, k);
        return res;
    }

    private static void loadDict() {
        In dicts = new In(dictPath);
        dictWords = dicts.readAllLines();
    }

    private static char[][] loadBoard(String boardFilePath) {
        In inb = new In(boardFilePath);
        String[] boardLines = inb.readAllLines();
        char[][] letterGrid = new char[boardLines.length][];
        for (int i = 0; i < boardLines.length; i++) {
            letterGrid[i] = boardLines[i].toCharArray();
        }
        return letterGrid;
    }

    private static List<String> bfs(char[][] board, int k) {

        TreeSet<String> uniqueWords = new TreeSet<>(new StrComparator());
        // initialize fringe
        LinkedList<BfsNode> fringe = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int pos = i * n + j;
                Set<Integer> visited = new TreeSet<>();
                visited.add(pos);
                String word = String.valueOf(board[i][j]);
                if (dictTrie.root.containChar(board[i][j])) {
                    Trie.TrieNode nextNode = dictTrie.root.nextNode(board[i][j]);
                    fringe.add(new BfsNode(pos, visited, word, nextNode));
                }
            }
        }
        // search
        while (!fringe.isEmpty()) {
            BfsNode node = fringe.poll();
            if (dict.contains(node.word)) {
                uniqueWords.add(node.word);
                if (uniqueWords.size() > k) {
                    uniqueWords.pollLast();
                }
            }
            List<Integer> nextPos = node.neighbors();
            for (int pos: nextPos) {
                int i = pos / n;
                int j = pos % n;
                char c = board[i][j];
                if (!node.visited.contains(pos)
                        && node.trieNode.containChar(c)) {
                    node.visited.add(pos);
                    Trie.TrieNode trieNode = node.trieNode.nextNode(c);
                    String word = node.word + c;
                    fringe.add(new BfsNode(pos, node.visited, word, trieNode));
                    node.visited.remove(pos);
                }
            }
        }

        List<String> result = new ArrayList<>(uniqueWords);
        return result;
    }

    static class BfsNode {
        private int pos;
        private Set<Integer> visited;
        private String word;
        private Trie.TrieNode trieNode;

        BfsNode(int pos, Set<Integer> visited, String word, Trie.TrieNode trieNode) {
            this.pos = pos;
            this.visited = new TreeSet<>(visited);
            this.word = word;
            this.trieNode = trieNode;
        }

        List<Integer> neighbors() {
            List<Integer> res = new ArrayList<>();
            int i = pos / n;
            int j = pos % n;
            if (i - 1 >= 0) {
                res.add((i - 1) * n + j);
                if (j - 1 >=  0) {
                    res.add((i - 1) * n + (j - 1));
                }
                if (j + 1 < n) {
                    res.add((i - 1) * n + (j + 1));
                }
            }
            if (i + 1 < m) {
                res.add((i + 1) * n + j);
                if (j - 1 >=  0) {
                    res.add((i + 1) * n + (j - 1));
                }
                if (j + 1 < n) {
                    res.add((i + 1) * n + (j + 1));
                }
            }
            if (j - 1 >= 0) {
                res.add(i * n + j - 1);
            }
            if (j + 1 < n) {
                res.add(i * n + j + 1);
            }
            return res;
        }

    }

    public static void main(String[] args) {
        int k = 7;
        String boardFilePath = "./exampleBoard.txt";
        List<String> res = solve(k, boardFilePath);
        System.out.println(Arrays.toString(res.toArray()));
    }

    static class StrComparator implements Comparator<String> {
        public int compare(String s1, String s2) {
            if (s1.length() != s2.length()) {
                return s2.length() - s1.length();
            } else {
                return s1.compareTo(s2);
            }
        }
    }
}
