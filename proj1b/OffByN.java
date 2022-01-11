public class OffByN implements CharacterComparator {
    private int N;
    public OffByN(int N) {
        this.N  = N;
    }
    public boolean equalChars(char x, char y) {
        int i = x - y;
        if (i == N ||  i == -1*N) {
            return true;
        } else {
            return false;
        }
    }
}
