public class OffByOne implements CharacterComparator {
    @Override
    public boolean equalChars(char x, char y) {
        int i = x - y;
        if (i == 1 ||  i == -1) {
            return true;
        } else {
            return false;
        }
    }
}
