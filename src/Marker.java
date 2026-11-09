import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    private static final long BASE = 257;
    private static final long MOD = 1_000_000_007;

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border length" line.
     */
    public static int[] prefixFunction(String p) {
        int[] pi = new int[p.length()];

        for (int i = 1; i < p.length(); i++) {
            int k = pi[i - 1];
            while (k > 0 && p.charAt(i) != p.charAt(k)) {
                k = pi[k - 1];
            }
            if (p.charAt(i) == p.charAt(k)) k++;
            pi[i] = k;
        }

        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA" in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */
    public static int[] findAll(String text, String p) {
        if (p.isEmpty()) return new int[0];

        int[] pi = prefixFunction(p);
        int[] positions = new int[text.length()];
        int count = 0;
        int k = 0;

        for (int i = 0; i < text.length(); i++) {
            while (k > 0 && text.charAt(i) != p.charAt(k)) {
                k = pi[k - 1];
            }
            if (text.charAt(i) == p.charAt(k)) k++;

            if (k == p.length()) {
                positions[count++] = i - p.length() + 1;
                k = pi[k - 1];
            }
        }

        return Arrays.copyOf(positions, count);
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA, GTAC; only ACGT is in b, so the answer is 1.
     */
    public static int sharedWords(String a, String b, int k) {
        if (k <= 0 || a.length() < k || b.length() < k) return 0;

        long highestPower = 1;
        for (int i = 1; i < k; i++) highestPower = highestPower * BASE % MOD;

        Map<Long, List<Integer>> positionsInB = new HashMap<>();
        long hashB = firstHash(b, k);
        positionsInB.computeIfAbsent(hashB, key -> new ArrayList<>()).add(0);

        for (int i = 1; i + k <= b.length(); i++) {
            hashB = nextHash(hashB, b.charAt(i - 1), b.charAt(i + k - 1), highestPower);
            positionsInB.computeIfAbsent(hashB, key -> new ArrayList<>()).add(i);
        }

        int shared = 0;
        long hashA = firstHash(a, k);
        for (int i = 0; i + k <= a.length(); i++) {
            if (i > 0) {
                hashA = nextHash(hashA, a.charAt(i - 1), a.charAt(i + k - 1), highestPower);
            }

            List<Integer> candidates = positionsInB.get(hashA);
            if (candidates == null) continue;

            for (int position : candidates) {
                if (sameWindow(a, i, b, position, k)) {
                    shared++;
                    break;
                }
            }
        }

        return shared;
    }

    private static long firstHash(String text, int k) {
        long hash = 0;
        for (int i = 0; i < k; i++) {
            hash = (hash * BASE + text.charAt(i)) % MOD;
        }
        return hash;
    }

    private static long nextHash(long hash, char removed, char added, long highestPower) {
        hash = (hash - removed * highestPower % MOD + MOD) % MOD;
        return (hash * BASE + added) % MOD;
    }

    private static boolean sameWindow(String a, int startA, String b, int startB, int k) {
        for (int i = 0; i < k; i++) {
            if (a.charAt(startA + i) != b.charAt(startB + i)) return false;
        }
        return true;
    }
}
