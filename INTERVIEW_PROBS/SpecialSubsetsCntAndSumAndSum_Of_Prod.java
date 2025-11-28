package INTERVIEW_PROBS;


// doc: https://docs.google.com/document/d/12mHkvzdWTgGMQyA-MCHhVj0_LabGHefjouM_1dxmARc/edit?tab=t.0
public class SpecialSubsetsCntAndSumAndSum_Of_Prod {
    

    // Easy Version: TC : O(n*maxSum) , SC : O(n*maxSum)   ( mastSum = 5000 as per constraints )
    public static long countSpecialSubsets_EasyVer(int n, int[] arr) {

        // 'y' is the largest element in the array
        long y = 0;
        for (int i = 0; i < n; i++) {
            y = Math.max(y, arr[i]);
        }

        long[][] dp = new long[n + 1][5001];
        // dp[i][j] = number of subsets from [0...i] with sum 'j'
        dp[0][0] = 1; // base case: i.e empty subset with sum 0

        for (int i = 1; i <= n; i++) {

            dp[i][0] = 1; // empty subset
            for (int j = 1; j <= 5000; j++) {
                dp[i][j] = dp[i - 1][j]; // not including arr[i-1]    " (i-1) since arr is 0-indexed and dp is 1-indexed " 
                if (j >= arr[i - 1]) {
                    dp[i][j] += dp[i - 1][j - arr[i - 1]]; // including arr[i-1]
                }
            }

        }

        long count = 0;

        // count of subSets with sum >= y  ( i.e largest element in the arr is y ; a subset is special if sum of its elements >= largest element in the arr )
        for (int i = (int) y; i <= 5000; i++) {
            count += dp[n][i];
        }

        return count;

    }

    
    // Follow-Up 1: Find the sum of all valid subsets
    public static long sumOfSpecialSubsets(int n, int[] arr) {

        // 'y' is the largest element in the array
        long y = 0;
        for (int i = 0; i < n; i++) {
            y = Math.max(y, arr[i]);
        }

        long[][] dp = new long[n + 1][5001];
        // dp[i][j] = number of subsets from [0...i] with sum 'j'
        dp[0][0] = 1; // base case: i.e empty subset with sum 0

        for (int i = 1; i <= n; i++) {

            dp[i][0] = 1; // empty subset
            for (int j = 1; j <= 5000; j++) {
                dp[i][j] = dp[i - 1][j]; // not including arr[i-1]    " (i-1) since arr is 0-indexed and dp is 1-indexed " 
                if (j >= arr[i - 1]) {
                    dp[i][j] += dp[i - 1][j - arr[i - 1]]; // including arr[i-1]
                }
            }

        }

        long sum = 0;
        for (int j = (int) y; j <= 5000; j++) {
            sum += (j * dp[n][j]); // sum of all valid subsets with sum >= y
        }

        return sum;

    }

    

    // Follow-Up 2: Find the sum of products of all valid subsets
    public static long sumOfProductsOfSpecialSubsets(int n, int[] arr) {

        // 'y' is the largest element in the array
        long y = 0;
        for (int i = 0; i < n; i++) {
            y = Math.max(y, arr[i]);
        }

        long MOD = 1000000007L;
        long[][] dp1 = new long[n + 1][5001];

        // dp1[i][j] = total sum of products of all subsets formed from first i elements whose subset-sum equals j.
        dp1[0][0] = 0; // base case: empty subset product

        for (int i = 1; i <= n; i++) {
            dp1[i][0] = 0; // empty subset product

            for (int j = 1; j <= 5000; j++) {

                dp1[i][j] = dp1[i - 1][j]; // not including arr[i-1]
                if (j >= arr[i - 1]) {
                    dp1[i][j] = (dp1[i][j] + (dp1[i - 1][j - arr[i - 1]] * arr[i - 1]) % MOD) % MOD; // including arr[i-1]
                }

                // special case: subset = {arr[i-1]} alone with sum = arr[i-1]
                if (j == arr[i - 1]) {
                    dp1[i][j] = (dp1[i][j] + arr[i - 1]) % MOD; // including arr[i-1]
                }
            }

        }
        
        // dp1[n][5] = total sum of products of all subsets whose sum is EXACTLY 5
        return dp1[n][5] % MOD; 
 
    }


    public static void main(String[] args) {
        


        // Easy Version
        int n = 7;
        int[] arr = { 1, 2, 3, 4, 5, 5, 5 };
        System.out.println("Count of Special Subsets (Easy Ver): " + countSpecialSubsets_EasyVer(n, arr));

        System.out.println("-----------------------------------------------------------------");


        // Follow-Up 1: Find the sum of all valid subsets
        System.out.println(" ------ Follow-Up 1: Sum of the Special Subsets ------ ");
        System.out.println(" Sum of all valid subsets: " + sumOfSpecialSubsets(n, arr));

        System.out.println("-----------------------------------------------------------------");

        // Follow-Up 2: Find the sum of products of all valid subsets
        System.out.println(" ------ Follow-Up 2: Sum of Products of the Special Subsets ------ ");
        int n2 = 5;
        int[] arr2 = { 1, 2, 3, 5, 5 };
        System.out.println(" Sum of products of all valid subsets: " + sumOfProductsOfSpecialSubsets(n2, arr2));
        
    }
}




/*⭐ SPECIAL SUBSETS — FULL INTUITION (Easy-Version Notes) ⭐
        ================================================================================

        Problem condition:
        A subset S is "special" if:
                sum(S) ≥ every element in T   (where T = elements NOT chosen)

        This can be written as:
                sum(S) ≥ max(T)

        So the ONLY thing we must beat is the largest unchosen element.

        -------------------------------------------------------------------------------
        1) Why we use y = largest element in the entire array?
        -------------------------------------------------------------------------------
        Let y be the maximum value anywhere in the array.

        For ANY subset S, the value of max(T) is always ≤ y.
        It might be:
            - equal to y (if we didn't choose y), or
            - smaller than y (if we chose y inside S).

        Therefore, the HARDEST possible requirement is:
                sum(S) ≥ y

        If we satisfy this strongest condition, we automatically satisfy ALL weaker ones.
        Thus the condition for S to be special becomes:
                sum(S) ≥ y

        -------------------------------------------------------------------------------
        2) What happens when the subset S excludes y?
        -------------------------------------------------------------------------------
        If S does NOT contain y:
                y ∈ T → max(T) = y
        Then the condition becomes:
                sum(S) ≥ y
        So these subsets are valid ONLY if their sum reaches y.

        -------------------------------------------------------------------------------
        3) What happens when the subset S includes y?
        -------------------------------------------------------------------------------
        If S DOES contain y:
                y ∉ T → max(T) < y
        But sum(S) ≥ y automatically because y ∈ S.

        So ANY subset containing y is valid because it already satisfies sum(S) ≥ y.

        In BOTH situations, the single unifying condition is:
                A subset S is special  ⇔  sum(S) ≥ y

        -------------------------------------------------------------------------------
        4) Why do we sum dp[n][y] + dp[n][y+1] + ... + dp[n][5000]?
        -------------------------------------------------------------------------------
        dp[n][j] = number of subsets whose sum is exactly j.

        Since a subset is special iff sum(S) ≥ y,
        we add the counts of ALL subset sums from y up to the maximum possible sum.

        Hence:
            answer = dp[n][y] + dp[n][y+1] + ... + dp[n][5000]

        This counts ALL subsets whose sum beats the strongest required value y.

        -------------------------------------------------------------------------------
        5) FINAL UNDERSTANDING — ONE GOLDEN SENTENCE
        -------------------------------------------------------------------------------
        To dominate all unchosen elements, a subset must dominate the LARGEST possible
        enemy, which is y. If a subset's sum is ≥ y, it dominates ANY possible T.

        Therefore:
                            special subsets = all subsets with sum(S) ≥ y.
        -------------------------------------------------------------------------------
*/


/*⭐ FOLLOW-UP 1 — SUM OF ALL VALID SPECIAL SUBSETS ⭐
        ================================================================================

        Goal:
        Instead of counting how many special subsets exist, we now want:
                the SUM of the sums of all valid subsets.

        Recall from the base problem:
        A subset S is special iff:
                sum(S) ≥ y
        where y = largest element of the array.

        We already computed:
                dp[i][j] = number of subsets from first i elements whose sum = j

        -------------------------------------------------------------------------------
        1) Which subset sums are valid?
        -------------------------------------------------------------------------------
        Same condition as before:
                Only sums j where j ≥ y are valid.
        Any subset with sum < y cannot dominate y when y ∈ T.

        Therefore, valid sums = all j in [y … 5000].

        -------------------------------------------------------------------------------
        2) How to compute "sum of all subset sums" for these valid subsets?
        -------------------------------------------------------------------------------
        If:
                dp[n][j] = number of subsets with sum = j
        Then:
                dp[n][j] subsets each contribute j to the total sum.

        So total contribution from all subsets with sum = j is:
                j * dp[n][j]

        -------------------------------------------------------------------------------
        3) Final answer
        -------------------------------------------------------------------------------
        We simply add contributions for all valid sums:

                answer = Σ ( j * dp[n][j] )   for j = y to 5000

        This gives:
        - j = the sum of a subset
        - dp[n][j] = number of subsets with that sum
        - j * dp[n][j] = total sum contributed by those subsets

        Add all of them and we get the required total.

        -------------------------------------------------------------------------------
        4) Why this works?
        -------------------------------------------------------------------------------
        We never need to enumerate subsets.
        DP already computes how many subsets achieve a certain sum.
        We multiply:
                (subset sum) × (number of subsets with that sum)
        to accumulate the total contribution of all special subsets.

        Efficient, clean, and uses the same DP table as the main problem.

        -------------------------------------------------------------------------------
        Final Formula:
                answer = Σ  j * dp[n][j]    for j ≥ y.
        -------------------------------------------------------------------------------
*/



/*⭐ FOLLOW-UP 2 — SUM OF PRODUCTS OF ALL VALID SPECIAL SUBSETS ⭐
        ================================================================================

        Goal:
        We want the SUM of the PRODUCTS of all special subsets S.

        A subset S is special iff:
                sum(S) ≥ y     (where y = largest element in the array)

        We compute this via DP on subset sums, but instead of counting subsets,
        we accumulate TOTAL PRODUCT for each sum.

        -------------------------------------------------------------------------------
        1) Definition of DP:
        -------------------------------------------------------------------------------
        dp1[i][j] =
            total sum of products of all subsets formed from first i elements
            whose subset-sum equals j.

        Example:
        If subsets giving sum j are:
            {3,5} → product = 15
            {2,7} → product = 14

        Then:
            dp1[i][j] = 15 + 14

        -------------------------------------------------------------------------------
        2) Transition:
        -------------------------------------------------------------------------------
        Two choices for each element b[i]:

        A) Do NOT include b[i]
        ----------------------------------
        Those subsets already exist in dp1[i-1][j].
        Hence:
                dp1[i][j] += dp1[i-1][j]

        B) Include b[i]
        ----------------------------------
        If we include b[i], the remaining elements must form a subset with:
                sum = j - b[i]

        The total product-sum of those subsets is:
                dp1[i-1][j - b[i]]

        Including b[i] multiplies every one of those products by b[i], so:
                dp1[i][j] += dp1[i-1][j - b[i]] * b[i]

        C) Special case: subset = {b[i]} alone
        ---------------------------------------
        This subset has sum = b[i] and product = b[i].

        Since dp1 uses 0 for empty-subset product accumulation,
        we must add this manually:

                if (j == b[i])
                    dp1[i][j] += b[i];

        -------------------------------------------------------------------------------
        3) Valid subsets:
        -------------------------------------------------------------------------------
        A subset is special iff its sum ≥ y.

        Thus final answer is:
                Σ dp1[n][j]  for all j = y to 5000

        Since product sums can be huge, take modulo 1e9+7 at every step.

        -------------------------------------------------------------------------------
        4) Summary:
        -------------------------------------------------------------------------------
        dp1 accumulates TOTAL PRODUCT of subsets per sum.
        We extend dp logic by multiplying b[i] when including it.
        Answer is sum over all dp1[n][j] for j ≥ y.
        -------------------------------------------------------------------------------
*/
