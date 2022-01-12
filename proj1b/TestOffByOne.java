import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testComparator() {
        assertFalse(palindrome.isPalindrome("cat", offByOne));
        assertFalse(palindrome.isPalindrome("tac", offByOne));
        assertFalse(palindrome.isPalindrome("%)", offByOne));
        assertTrue(palindrome.isPalindrome("a", offByOne));
        assertTrue(palindrome.isPalindrome("flake", offByOne));
        assertTrue(palindrome.isPalindrome("ekalf", offByOne));
        assertTrue(palindrome.isPalindrome("FlakE", offByOne));
        assertTrue(palindrome.isPalindrome("%lak&", offByOne));
    }

}
