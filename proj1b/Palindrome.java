public class Palindrome {

    public Deque<Character> wordToDeque(String word) {
        Deque<Character> d = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i += 1) {
            Character c = word.charAt(i);
            d.addLast(c);
        }
        return d;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> d = wordToDeque(word);
        for (int i = 0; i < word.length() / 2; i += 1) {
            Character c1 = word.charAt(i);
            Character c2 = d.removeLast();
            if (c1 != c2) {
                return false;
            }
        }

        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> d = wordToDeque(word);
        for (int i = 0; i < word.length() / 2; i += 1) {
            Character c1 = word.charAt(i);
            Character c2 = d.removeLast();
            if (!cc.equalChars(c1, c2)) {
                return false;
            }
        }

        return true;
    }
}
