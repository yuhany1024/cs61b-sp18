/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int numDigits = 0;
        for (String s: asciis) {
            numDigits = Math.max(numDigits, s.length());
        }
        String[] sorted = new String[asciis.length];
        System.arraycopy(asciis, 0, sorted, 0, asciis.length);
        for (int d = 0; d < numDigits; d++) {
            sorted = sortHelperLSD(sorted, d);
        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static String[] sortHelperLSD(String[] asciis, int index) {
        int[] counts = new int[256];
        for (String s : asciis) {
            if (s.length() - 1 < index) {
                counts[0] += 1;
            } else {
                counts[s.charAt(index)] += 1;
            }
        }
        int[] startIndex = new int[256];
        startIndex[0] = 0;
        for (int i = 1; i <256; i++) {
            startIndex[i] = startIndex[i-1] + counts[i-1];
        }

        String[] sorted = new String[asciis.length];
        for (String s : asciis) {
            int place;
            if (s.length() - 1 < index) {
                place = startIndex[0];
                startIndex[0] += 1;
            } else {
                place = startIndex[s.charAt(index)];
                startIndex[s.charAt(index)] += 1;
            }
            sorted[place] = s;
        }
        return sorted;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] arr = new String[] {"hello", "bba", "word", "apple"};
        String[] sorted = sort(arr);
        for (String s: sorted) {
            System.out.println(s);
        }
    }
}
