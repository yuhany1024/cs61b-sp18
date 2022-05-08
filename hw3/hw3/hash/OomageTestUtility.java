package hw3.hash;

import java.util.Arrays;
import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int N = oomages.size();
        int[] buckets = new int[M];
        Arrays.fill(buckets, 0);
        for (Oomage o: oomages) {
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            buckets[bucketNum] += 1;
        }
        for (int n: buckets) {
            if (n < N / 50. || n > N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
