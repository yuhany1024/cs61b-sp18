public class OffByOne implements CharacterComparator {
    @Override
    public boolean equalChars(char x, char y) {
        int i = x - y;
        return (i == 1 ||  i == -1);
    }
}
