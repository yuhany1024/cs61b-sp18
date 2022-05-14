import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {

    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (char ch: inputSymbols) {
            if (!frequencyTable.containsKey(ch)) {
                frequencyTable.put(ch, 1);
            } else {
                int freq = frequencyTable.get(ch);
                frequencyTable.put(ch, freq + 1);
            }
        }
        return frequencyTable;
    }

    public static void main(String[] args) {
        char[] input = FileUtils.readFile(args[0]);
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        Map<Character, Integer> frequencyTable = buildFrequencyTable(input);

        BinaryTrie trie = new BinaryTrie(frequencyTable);
        ow.writeObject(trie);

        Map<Character, BitSequence> lookupTable = trie.buildLookupTable();
        List<BitSequence> sequences = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            sequences.add(lookupTable.get(input[i]));
        }

        BitSequence massiveSeq = BitSequence.assemble(sequences);
        ow.writeObject(massiveSeq);

    }

}
