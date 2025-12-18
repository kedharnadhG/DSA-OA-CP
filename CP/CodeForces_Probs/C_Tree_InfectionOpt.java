package CP.CodeForces_Probs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class C_Tree_InfectionOpt {
    static boolean canFinish(long[] rem, long x) {
        long need = 0;

        for (long r : rem) {
            if (r > x) {
                need += (r - x);
                if (need > x) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();

        while (t-- > 0) {

            int n = sc.nextInt();

            int[] childCnt = new int[n + 1];
            for (int i = 2; i <= n; i++) {
                int p = sc.nextInt();
                childCnt[p]++;
            }

            List<Integer> groups = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                if (childCnt[i] > 0) {
                    groups.add(childCnt[i]);
                }
            }

            groups.sort(Collections.reverseOrder());

            int g = groups.size();
            long[] rem = new long[g];

            for (int i = 0; i < g; i++) {
                rem[i] = groups.get(i) - (g - i + 1); // see video to understand again,; ( l-list gets over in g-secodns, after that how the r-list looks like :->  the max will get reduced by every-second right i.e g-seconds , like that 2nd-max will get reduced by (g-1) seconds and so on..)
                if (rem[i] < 0)
                    rem[i] = 0;
            }

            long low = 0, high = (long) 2e5, ans = -1;

            while (low <= high) {
                long mid = (low + high) / 2;

                if (canFinish(rem, mid)) {
                    ans = mid;
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }

            // Your( g + x ) counts seconds AFTER the first infection exists
            System.out.println(ans + g + 1);   // "+1" is because to statrt the infection initially.. ( (first injection) )

        }

        sc.close();

    }
}





/*
        C. TREE INFECTION — OPTIMIZED SOLUTION (DETAILED NOTES)
        ===========================================================

        PROBLEM SUMMARY
        ---------------
        We have a rooted tree.
        Initially all nodes are healthy.

        Each second:
        1) Spreading:
        - If a parent has ≥1 infected child,
            it can infect ONE more child.
        2) Injection:
        - We can infect ANY healthy node.

        Goal:
        Infect all nodes in minimum seconds.


        KEY OBSERVATION
        ---------------
        Children of the same parent form ONE independent group.

        If a parent has k children:
        - 1 injection starts the group
        - remaining (k - 1) are infected by spreading (1 per second)

        So each group has total work = k.


        STEP 1: BUILD GROUPS
        --------------------
        For each node:
        childCnt[node] = number of children

        Each childCnt[node] > 0 is ONE group.

        Let groups[] = all such group sizes.


        STEP 2: SORT GROUPS
        ------------------
        Sort groups in descending order.

        Reason:
        Starting larger groups earlier allows more parallel spreading.


        STEP 3: DEFINE g
        ----------------
        g = number of groups

        We will spend the FIRST g seconds
        using injections to start ALL groups
        (one group per second).


        STEP 4: WHEN DOES A GROUP START?
        --------------------------------
        Group i (0-based index) is injected at second (i + 1).

        Because spreading happens before injection in a second,
        a group injected at second s starts spreading from second (s + 1).

        To simplify math, we SHIFT the time model.


        STEP 5: SHIFTED TIME MODEL (IMPORTANT)
        -------------------------------------
        We intentionally use a shifted model where:
        - each group is assumed to have
        (g - i + 1) effective spreading opportunities
        before the second phase begins.

        This absorbs the real +1 delay into the formula.

        So remaining work for group i is:

        remaining_work[i] = groups[i] - (g - i + 1)

        If this is negative, clamp to 0.


        STEP 6: WHAT rem[] REPRESENTS
        -----------------------------
        rem[i] = work that still needs to be done
        AFTER all groups are started.


        STEP 7: FEASIBILITY FUNCTION f(x)
        --------------------------------
        Question:
        Can we finish all rem[] in x more seconds?

        In x seconds:
        - each group auto-spreads x times
        - we also get x injections

        If a group has rem[i] > x,
        it needs (rem[i] - x) extra injections.

        Let:
        need = sum of all extra injections

        Condition:
        need <= x  → possible
        need >  x  → impossible


        STEP 8: BINARY SEARCH
        --------------------
        f(x) is monotonic (false → true),
        so we binary search the minimum x.


        STEP 9: FINAL ANSWER
        -------------------
        Because we used the shifted model (g - i + 1),
        we must add back the shift.

        Total time = g + x + 1


        IMPORTANT WARNING
        -----------------
        There are TWO valid models:

        Model A:
        rem[i] = groups[i] - (g - i)
        answer = g + x

        Model B (this code):
        rem[i] = groups[i] - (g - i + 1)
        answer = g + x + 1

        NEVER mix these models.


        COMPLEXITY
        ----------
        Sorting: O(N log N)
        Binary search: O(log N)
        Check: O(N)

        Total: O(N log N)
        ===========================================================
*/
