public class OffByN implements CharacterComparator {
    private int N;
    public OffByN(int N) {
        this.N  = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        int i = x - y;
        return (i == N ||  i == -1 * N);
    }
}
