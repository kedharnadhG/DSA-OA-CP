package OA.AMZ_OA;

import java.util.*;

public class MinSwapsFileAffinity {

    public static void main(String[] args) {

        int n = 6;
        int[] fileSize =  {1, 1, 2, 2, 3, 3};
        int[] affinity =  {1, 2, 2, 3, 3, 1};
        // ------------------------------------------------------------

        // STEP 1: Count bad pairs
        int bad = 0;
        for (int i = 0; i < n; i++) {
            if (fileSize[i] == affinity[i]) {
                bad++;
            }
        }

        // STEP 2: If no bad pairs → 0 swaps
        if (bad == 0) {
            System.out.println("Minimum swaps to ensure fileSize[i] != affinity[i] for all i: 0");
            return;
        }

        // STEP 3: If at least one good pair exists → always solvable
        if (bad < n) {
            int answer = (bad + 1) / 2;   // ceil(bad/2)
            System.out.println("Minimum swaps to ensure fileSize[i] != affinity[i] for all i: " + answer);
            return;
        }

        // STEP 4: When bad == n → fileSize == affinity for all i → need derangement
        Map<Integer, Integer> freq = new HashMap<>();
        int maxFreq = 0;

        for (int x : fileSize) {
            freq.put(x, freq.getOrDefault(x, 0) + 1);
            maxFreq = Math.max(maxFreq, freq.get(x));
        }

        // STEP 5: Derangement is possible only if maxFreq ≤ n/2
        if (maxFreq > n / 2) {
            System.out.println("Minimum swaps to ensure fileSize[i] != affinity[i] for all i: -1");
        } else {
            int answer = (n + 1) / 2;    // ceil(n/2)
            System.out.println("Minimum swaps to ensure fileSize[i] != affinity[i] for all i: " + answer);
        }
        
    }
}






/*      AMAZON OA — MINIMUM SWAPS TO AVOID INFECTION (COMPLETE MASTER NOTES)
        ======================================================================================

        PROBLEM:
        --------
        We have two arrays of equal length n:

            fileSize[i]  → can be swapped with any fileSize[j]
            affinity[i]  → fixed array, cannot be changed

        A file i gets infected if:

            fileSize[i] == affinity[i]

        We want:

            For EVERY index i:  fileSize[i] != affinity[i]

        We may only use this operation:

            swap(fileSize[i], fileSize[j])

        Goal:
        -----
        Find the MINIMUM number of swaps required to make ALL indices safe.
        If impossible → print -1.


        CONCEPT 1 — BAD AND GOOD PAIRS:
        -------------------------------
        A BAD pair is an index i where:

            fileSize[i] == affinity[i]

        Everything else is GOOD.

        Let bad = number of bad pairs.

        We must fix ALL of them.


        CONCEPT 2 — HOW SWAPS FIX BAD PAIRS:
        ------------------------------------
        Suppose:

        BAD at i → fileSize[i] = X, affinity[i] = X
        BAD at j → fileSize[j] = Y, affinity[j] = Y

        If X != Y, then swapping i and j yields:

        fileSize[i] = Y != X    → FIXED
        fileSize[j] = X != Y    → FIXED

        ONE SWAP fixes TWO bad pairs.

        This is why pairing bads reduces them two at a time.


        ======================================================================================
        CASE 1 — bad == 0
        ======================================================================================
        Already no infection. No swaps needed.

        ANSWER = 0.


        ======================================================================================
        CASE 2 — bad < n   (AT LEAST ONE GOOD INDEX EXISTS)
        ======================================================================================

        If at least ONE good index exists, we always have a flexible place to fix leftover bads.

        Each swap of two bad positions fixes 2 bad pairs.

        If bad is EVEN:
            swaps = bad / 2

        If bad is ODD:
            we fix (bad-1) bads in pairs, leaving 1 bad.
            we swap that last bad with ANY good index.
        So:
            swaps = bad/2 + 1
                    = ceil(bad/2)
                    = (bad + 1) / 2

        This ALWAYS works because the existence of a good pair guarantees
        that we can resolve the leftover odd bad safely.

        Thus:

            If bad < n:
                ANSWER = (bad + 1) / 2


        ======================================================================================
        CASE 3 — bad == n   (EVERY position is BAD)
        ======================================================================================

        This means:

            fileSize[i] == affinity[i] for ALL i

        So fileSize[] and affinity[] are IDENTICAL arrays.

        To fix infection everywhere, we must rearrange fileSize such that:

            fileSize[i] != affinity[i]   for ALL i

        This is called a DERANGEMENT (a permutation where no element stays at its original position).


        DERANGEMENT is NOT always possible.

        We must check if we CAN rearrange fileSize so that no value remains in any forbidden position.


        ======================================================================================
        CRITICAL THEORY — WHEN IS DERANGEMENT POSSIBLE?
        ======================================================================================

        Let a value X appear f times in fileSize.
        Because arrays are identical in positions (bad == n),
        affinity has X in EXACTLY f positions.

        At each of these f positions, fileSize MUST place a value NOT EQUAL to X.

        How many values are available that are NOT X?
            (n - f)

        To avoid placing X in any forbidden spot, we need:

            (n - f) >= f

        Solving inequality:

            n - f ≥ f
            n ≥ 2f
            f ≤ n/2

        This gives us the MOST IMPORTANT rule:

        ======================================================================================
                DERANGEMENT IS POSSIBLE ⇔ max_frequency_of_any_value ≤ n/2
        ======================================================================================

        If this condition fails → some value occurs too many times → it MUST collide somewhere.

        Thus:

                if maxFreq > n/2:
                    ANSWER = -1   (impossible)
                else
                    derangement is possible


        ======================================================================================
        WHY THE ANSWER IS ceil(n/2) FOR DERANGEMENTS?
        ======================================================================================

        When all positions are bad (bad = n), and derangement is possible,
        we must completely "rotate" or "shift" values so that
        no value stays in its original affinity position.

        This requires creating cycles.

        FACT:
        -----
        To derange n positions with swaps, the minimum swaps needed is:

            ceil(n / 2)

        WHY?

        Because each swap at best solves TWO positions:

        • A swap of index i and j removes fileSize[i] and fileSize[j]
            from being equal to affinity[i] and affinity[j].

        There are n positions to derange.

        Each swap fixes 2 positions → at minimum:

            number of swaps ≥ n/2

        If n is even:
            n positions need fixing → n/2 swaps

        If n is odd:
            one position is left after fixing pairs → needs 1 more swap
            → total = (n//2) + 1 = ceil(n/2)

        Thus:

        ======================================================================================
                IF bad == n AND derangement possible:
                        ANSWER = ceil(n/2) = (n+1)/2
        ======================================================================================


        ======================================================================================
        FINAL ALGORITHM SUMMARY
        ======================================================================================

        Let bad = count of fileSize[i] == affinity[i].

        1) If bad == 0:
                print 0

        2) Else if bad < n:
                print (bad + 1) / 2

        3) Else (bad == n):
                Compute maxFreq in fileSize
                If maxFreq > n/2:
                    print -1
                Else
                    print (n + 1) / 2


        ======================================================================================
        WORKED EXAMPLE:
        ---------------
        Input:
        n = 6
        fileSize  = [1,1,2,2,3,3]
        affinity  = [1,2,2,3,3,1]

        bad positions = 0,2,4 → bad = 3

        Since bad < n:
            swaps = ceil(3/2) = 2

        Answer = 2


        ======================================================================================
        This note covers:
            ✔ Full problem explanation
            ✔ Why swaps fix two bads
            ✔ Why (bad+1)/2 works
            ✔ Why derangement is needed when bad==n
            ✔ Mathematical proof of maxFreq ≤ n/2
            ✔ Why answer is ceil(n/2)
            ✔ All edge cases and reasoning
            ✔ NO anagram check required

        This is the complete, final, interview-ready explanation.
        ======================================================================================
*/
