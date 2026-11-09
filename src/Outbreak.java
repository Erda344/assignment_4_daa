import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        int[] parent = new int[n];
        Arrays.fill(parent, -2);
        int[] order = new int[n];
        int orderSize = 1;
        order[0] = 0;
        parent[0] = -1;

        for (int i = 0; i < orderSize; i++) {
            int person = order[i];
            for (int next : nb[person]) {
                if (next == parent[person]) continue;
                parent[next] = person;
                order[orderSize++] = next;
            }
        }

        long[] testedCost = new long[n];
        long[] notTestedCost = new long[n];

        for (int i = n - 1; i >= 0; i--) {
            int person = order[i];
            testedCost[person] = cost[person];

            for (int child : nb[person]) {
                if (parent[child] != person) continue;
                testedCost[person] += Math.min(testedCost[child], notTestedCost[child]);
                notTestedCost[person] += testedCost[child];
            }
        }

        boolean[] tested = new boolean[n];
        tested[0] = testedCost[0] <= notTestedCost[0];

        for (int i = 1; i < n; i++) {
            int person = order[i];
            int p = parent[person];
            if (!tested[p]) {
                tested[person] = true;
            } else {
                tested[person] = testedCost[person] <= notTestedCost[person];
            }
        }

        return tested;
    }

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        int[] parent = new int[n];
        Arrays.fill(parent, -2);
        int[] order = new int[n];
        int orderSize = 1;
        order[0] = 0;
        parent[0] = -1;

        for (int i = 0; i < orderSize; i++) {
            int person = order[i];
            for (int next : nb[person]) {
                if (next == parent[person]) continue;
                parent[next] = person;
                order[orderSize++] = next;
            }
        }

        int[] depth = new int[n];
        int longest = 0;

        for (int i = n - 1; i >= 0; i--) {
            int person = order[i];
            int first = 0;
            int second = 0;

            for (int child : nb[person]) {
                if (parent[child] != person) continue;
                int branch = depth[child] + 1;
                if (branch > first) {
                    second = first;
                    first = branch;
                } else if (branch > second) {
                    second = branch;
                }
            }

            depth[person] = first;
            longest = Math.max(longest, first + second);
        }

        return longest;
    }
}
