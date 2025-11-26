package OA.Rubrik_OA;

import java.io.*;
import java.util.*;

public class NoOfPairsOrTripletsWhoseGCDisEqY {

    /*------------- FAST IO TEMPLATE -------------
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;
    
        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }
    
        String next() throws IOException {
            while (st == null || !st.hasMoreTokens()) {
                st = new StringTokenizer(br.readLine());
            }
            return st.nextToken();
        }
    
        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    
        long nextLong() throws IOException {
            return Long.parseLong(next());
        }
    
        String nextLine() throws IOException {
            return br.readLine();
        }
    
    }
    */
   
    static final int MAX = 100000;
    static final long MOD = 1000000007;
    
    /*Easy-version
        How to find the count of pairs (i,j) in the array whose GCD == y
        for all y from 1 to max(arr). 
        compute g[y] = no of pairs whose GCD is EXACTLY y    :- we can return g[y] in O(1) time for any y query.
    */
    public static void cntOfPairsWithGCDasY(int[] arr, int n) {
        // print for all the y's from 1 to max(arr)

        long[] u = new long[MAX + 1]; // u[i] = how many numbers divisible by i

        // step-1 : calculate u[i](divisor count)  for all i   ; (finding the factors/divisors of each num in arr : TC = O(n * sqrt(max(arr))) SC = O(max(arr)))  ; if  n & max(i) = 10^5 => TC: O(n * sqrt(n)) 
        for (int num : arr) {
            for (int d = 1; d * d <= num; d++) {
                if (num % d == 0) {
                    u[d]++;
                    if (d != (num / d)) {
                        u[num / d]++;
                    }
                }
            }
        }


        // step-2 : calculate mul[i] = no of pairs whose GCD is MULTIPLE of i ( divisible_by i)
        // mul[2] = g[2] i.e no.of pairs whose GCD is 2 + g[4] i.e no.of pairs whose GCD is 4 + ...
        long[] mul = new long[MAX + 1];
        for (int i = 1; i <= MAX; i++) {
            if (u[i] >= 2) {
                mul[i] = (u[i] * (u[i] - 1)) / 2; // nC2     ; where u[i] = how many numbers divisible by i
            }
        }

        // step-3 : calculate g[i] = no of pairs whose GCD is EXACTLY i ; (calculate exact gcd counts using inclusion-exclusion (reverse order))
        long[] g = new long[MAX + 1];
        for (int i = MAX; i >= 1; i--) {
            long temp = mul[i];
            for (int j = 2 * i; j <= MAX; j += i) {
                temp -= g[j];
            }
            g[i] = temp;
        }

        // Step 4: output all non-zero gcd counts
        StringBuilder sb = new StringBuilder();
        System.out.println("g[i] = no of pairs whose GCD is EXACTLY i");
        for (int i = 1; i <= MAX; i++) {
            if (g[i] > 0) {
                sb.append("g[").append(i).append("] = ").append(g[i]).append("\n");
            }
        }

        System.out.print(sb);

    }


    /* Factor Calculation using N log N Sieve Method (Optimized- Java Version)
       -------------------------------------------------------------
       Precompute factors for ALL numbers up to MAX in O(MAX log MAX)
       Then for each DISTINCT number in the array, directly fetch
       its precomputed factors in O(1) time.
    ------------------------------------------------------------- */
    public static void computeFactorsSieveOptimized(int[] arr, int n) {

        //step 1: find max-value in arr
        int max = 0;
        for (int x : arr)
            max = Math.max(max, x);

        // step-2: precompute factors for all numbers 1...max
        List<List<Integer>> fact = new ArrayList<>();
        for (int i = 0; i <= max; i++) {
            fact.add(new ArrayList<>());
        }

        for (int i = 1; i <= max; i++) {
            for (int j = i; j <= max; j += i) {
                fact.get(j).add(i); // i is a factor of j
            }
        }

        // Step 3: Convert array to distinct set to avoid duplicate work
        Set<Integer> distinct = new HashSet<>();
        for (int x : arr)
            distinct.add(x);

        // Step 4: Print factors ONLY for distinct elements
        for (int x : distinct) {
            System.out.print("f[" + x + "] = ");
            for (int f : fact.get(x)) {
                System.out.print(f + " ");
            }
            System.out.println();
        }

    }

    
    /*Harder-Version
            Find the no.of SubSequences of size "k" in the array whose GCD == "y"
            formula for it is : nCk
    */
    /*Real-Version 
            Given an array of size-"N" - answer Q-queries of the form (k,g)
                                       - does there exists a subset of size-"k" whose GCD == "g" ?
    */


    // Fast exponentiation
    static long modPow(long a, long b, long mod) {
        long res = 1;
        while (b > 0) {
            if ((b & 1) == 1) res = (res * a) % mod;
            a = (a * a) % mod;
            b >>= 1;
        }
        return res;
    }

    // Factorials & Inverse Factorials
    static long[] fact = new long[MAX + 1];
    static long[] invFact = new long[MAX + 1];
    
    // Precompute factorials & inverse factorials
    static void buildFactorials() {
        fact[0] = 1;
        for (int i = 1; i <= MAX; i++)
            fact[i] = (fact[i - 1] * i) % MOD;

        invFact[MAX] = modPow(fact[MAX], MOD - 2, MOD);

        for (int i = MAX - 1; i >= 0; i--)
            invFact[i] = (invFact[i + 1] * (i + 1)) % MOD;
    }

    // nCr mod M
    static long nCr(int n, int r) {
        if (r < 0 || r > n) return 0;
        return fact[n] * invFact[r] % MOD * invFact[n - r] % MOD;
    }

    public static boolean isSubSequenceOfSizeKWhoseGCDisG(int[] arr, int k, int g) {
        //does there exists a subset of size K in this array whose gcd == g ?

        int n = arr.length;

        // step-1: find max-value in arr
        int max = 0;
        for (int x : arr)
            max = Math.max(max, x);

        // step-2: precompute factors for all numbers 1...max
        List<List<Integer>> fact = new ArrayList<>();
        for (int i = 0; i <= max; i++) {
            fact.add(new ArrayList<>());
        }

        for (int i = 1; i <= max; i++) {
            for (int j = i; j <= max; j += i) {
                fact.get(j).add(i); // i is a factor of j
            }
        }

        // step-3 : freq[i] = how many numbers divisible by i
        int[] freq = new int[max + 1];
        for (int x : arr) {
            for (int f : fact.get(x)) {
                freq[f]++;
            }
        }

        // step-4 : Compute Factorials
        buildFactorials();

        // step-5 : mul[i][k] = no.of k-sized subsets divisible by i
        long[][] mul = new long[max + 1][6];
        for(int dv=1; dv<=max; dv++) {
            mul[dv][k] = nCr(freq[dv], k);
        }

        // Step 6: Inclusion–Exclusion to get gcd[i][k]
        long[][] gcd = new long[max + 1][6];

        for (int dv = max; dv >= 1; dv--) {
            long t = mul[dv][k];

            for (int j = dv * 2; j <= max; j += dv) {
                t -= gcd[j][k];
            }

            gcd[dv][k] = t;
        }

        // Step 7: Return YES/NO based on > 0
        return gcd[g][k] > 0;

    }



    public static void main(String[] args) {
        
        // FastReader sc = new FastReader();

        int n = 5;
        int[] arr = { 5, 4, 2, 6, 8 };
        /*// now print for all the gcd values from 1 to max(arr)  (i.e y=1 to P(max(arr)))
            System.out.println("---------- Easy-Version  ( No.Of Pairs Whose GCD is Y )---------- ");
            cntOfPairsWithGCDasY(arr, n);
        */

        /*     System.out.println("---------- Factors Optimised Version ---------- ");
        int[] arr2 = { 1, 12, 3, 4, 13, 12, 4, 1 };
        computeFactorsSieveOptimized(arr2, arr2.length);
        */

        System.out.println("---------- Harder-Version  ( No.Of subSequences of size k whose GCD is Y )---------- ");
        int[] arr3 = {3,6,12,5,13,11,22,5,18,52,49,34};
        int Q = 10;  // no.of queries
        int[][] queries = {{4,2}, {3,5}, {5,8}, {5,3}, {2,2}, {3,2}, {1,3}, {3,2}, {2,5}, {2,7}};   // {k,g}  -> subSequences of size k whose GCD == g
        
         for(int[] q : queries) {
            boolean ans = isSubSequenceOfSizeKWhoseGCDisG(arr3, q[0], q[1]);
            System.out.println(
                "Is there a SubSequence of size "+q[0]+" whose GCD is "+q[1]+" ? => " + (ans ? "YES" : "NO")
            );
        }
    }
}



/*------------------------------NOTES-----------------------------------*/

/*Easy-Version - Notes
        📌 GCD PAIRS — QUICK NOTES

        ------------------------------------------------------------
        1) u[i] — Divisibility Count
        ------------------------------------------------------------
        u[i] = number of elements in the array divisible by i.

        We compute divisors efficiently:
        for d = 1 to sqrt(num):
            if d divides num:
                u[d]++
                if (d != num/d):
                    u[num/d]++

        Why the second condition?
        - Because every divisor d has a partner num/d.
        - But when d == num/d (perfect square), we must NOT count twice.

        ------------------------------------------------------------
        2) mul[i] — Pairs whose GCD is MULTIPLE of i
        ------------------------------------------------------------
        mul[i] = C(u[i], 2)
            = u[i] * (u[i] - 1) / 2

        mul[i] includes all pairs where gcd ∈ {i, 2i, 3i, 4i, ...}

        ------------------------------------------------------------
        3) g[i] — EXACT GCD Count using Inclusion–Exclusion
        ------------------------------------------------------------
        g[i] = mul[i]
            - (g[2i] + g[3i] + g[4i] + ...)

        We subtract ALL counts of pairs whose GCD is a multiple of i 
        (except i itself), because mul[i] “overcounts” them.

        ------------------------------------------------------------
        4) Why process from MAX → 1 (reverse order)?
        ------------------------------------------------------------
        Because:
        mul[i] = g[i] + g[2i] + g[3i] + ...

        To compute g[i], we must already know:
        g[2i], g[3i], g[4i], ...

        Hence compute in reverse so larger gcd values are ready first.

        ------------------------------------------------------------
        5) Time Complexity — O(P log P)
        ------------------------------------------------------------
        The inclusion–exclusion loop:
        for i = 1..P:
            for (j = 2i; j <= P; j += i)

        Total operations:
        P/1 + P/2 + P/3 + ... + P/P
        = P * (1 + 1/2 + 1/3 + ... )
        ≈ P log P  (harmonic series)

        ------------------------------------------------------------
        6) Core Intuition Recap
        ------------------------------------------------------------
        - u[i] counts how many array numbers are divisible by i.
        - mul[i] counts all pairs whose GCD is a multiple of i.
        - g[i] subtracts contributions of larger gcds → exact count.
        - Always compute g[] from large → small for correctness.
        ------------------------------------------------------------
        7) Important Implementation Notes

        - MAX must be >= max element of array.
        - Step-1 is based on array values only.
        - Step-2 and Step-3 are computed from 1..MAX.
        - mul[i] collects pairs whose gcd is multiple of i.
        - g[i] uses inclusion-exclusion from big → small.
        - Using a larger MAX is safe but slower; never smaller.

*/

/*FACTOR CALCULATION — OPTIMAL (notes)
 
*/
/*Factors-Calculation-Optimal (NlogN method) - Notes     ( instead of N*sqrt(N) )   ( NlogN < N*sqrt(N) )
    
        More Efficient -> (NlogN method to store factors of all numbers of the array in hashmap) 

                NlogN - code to precompute factors/divisors of all numbers from 1 to 2*10^5        (we precompute this, before doing anything with the given-array )

                g[i] = list of all numbers which are multiple of i     ( G[i] -> table of number “i” till max_limit ⇒ 100000+1 )

                ( M + M/2 + M/3 + ... + M/M )  = M * (1 + 1/2 + 1/3 + ... + 1/M)  = MlogM    ( harmonic series )

                where M = max_limit = 10^5

                --------------------------
                instead of storing the table(multiples) in hashmap, we store the multiples in reverse order
                    ex:  1=> 1,2,3....     (what we store in map is , U[1->1, 2->1, 3->1, 4->1....]  )
                         2=> 2,4,6....     (what we store in map is , U[2->2, 4->(1,2), ....]  )      (4->1,2   i.e we have 1 alredy previously stored when we processed 1=>1,2,3.... & now we add 2 also)
                         3=> 3,6,9....

                    U[i] :- is nothing but which is storing the factors of i
*/
/*FACTOR CALCULATION — OPTIMAL (N log N METHOD)
        ---------------------------------------------------------------------------

        Goal:
            Efficiently compute factors of ALL numbers up to MAX using
            a sieve-style algorithm instead of sqrt(n) for each element.

        ---------------------------------------------------------------------------
        WHY NOT USE N * sqrt(N)?
        ---------------------------------------------------------------------------
        - For each array element x, checking all divisors up to sqrt(x)
        takes O(sqrt(x)).
        - Doing this for n numbers → O(n * sqrt(maxValue)).
        - Becomes slow for large inputs (1e5, 1e6 constraints).

        ---------------------------------------------------------------------------
        N LOG N SIEVE METHOD (Faster & Cleaner)
        ---------------------------------------------------------------------------
        We build a global table `fact[]` where:

            fact[j] contains all numbers i such that i divides j.

        Algorithm:
            for (int i = 1; i <= MAX; i++)
                for (int j = i; j <= MAX; j += i)
                    fact[j].add(i);

        Why is this N log N?
        - j takes values i, 2i, 3i, ...
        - Number of iterations = MAX/i
        - Total = MAX * (1 + 1/2 + 1/3 + ...) ≈ MAX log MAX

        This is ALWAYS faster than N * sqrt(N).

        ---------------------------------------------------------------------------
        WHY WE MUST CONVERT ARRAY TO DISTINCT BEFORE USING fact[]
        ---------------------------------------------------------------------------
        - Precomputation already gives factors of *every* number 1..MAX.
        - If array has duplicates (e.g., {12,12,12}) we DO NOT want to:
                • print same factors many times
                • do map insertions repeatedly
                • waste time and memory

        - Using a HashSet removes duplicates:
                arr → distinctArr

        This reduces work from n elements → m distinct elements (m ≤ n).
        Huge optimization in real problems.

        ---------------------------------------------------------------------------
        FINAL WORKFLOW
        ---------------------------------------------------------------------------
        1. Compute MAX = max element of array.
        2. Precompute factors for all numbers 1..MAX using sieve (N log N).
        3. Convert array to DISTINCT using HashSet.
        4. For each distinct number x, print fact[x].

        ---------------------------------------------------------------------------
        QUICK TAKEAWAY NOTES
        ---------------------------------------------------------------------------
        • Traditional: O(n * sqrt(n)) — divisor checking per element.
        • Optimal: O(MAX log MAX) — sieve of factors.
        • Always convert array → DISTINCT before factor processing.
        • After sieve, each number's factors are ready in O(1).
        • Perfect for problems involving divisors, gcd tables, freq tables.
        ---------------------------------------------------------------------------
*/


/*Find the number of triplets (i,j,k) in the array whose gcd == y 


/*------------------------------ FINAL- Overall NOTES-----------------------------------*/


/*  ✨ MASTER END-TO-END GCD + nCr NOTES ✨
        =====================================================================

        This document summarizes ALL concepts used across:

        ✔ Easy Version (count pairs whose GCD == y)  
        ✔ Factor Sieve (N log N)  
        ✔ Harder Version (count subsequences of size k)  
        ✔ Real Version (Q queries: does subset of size k exist with GCD = g?)  
        ✔ Inclusion–Exclusion  
        ✔ nCr (modulo + exact)  
        ✔ Modular inverse, modPow, factorial & inverse factorial  
        ✔ Why distinct array is needed  

        Keep this block at the EOF of your file for future revision.
        =====================================================================

        =====================================================================
        1) EASY VERSION — COUNT PAIRS WHOSE GCD = y
        =====================================================================
        Goal:
            For every y from 1..max(arr), compute:
                g[y] = number of pairs (i,j) in arr where GCD = y.

        Steps:
        -----------------------------------------------------------
        (1) Build u[i] = count of numbers divisible by i
            For each num in arr:
                for d up to sqrt(num):
                    if d divides num:
                        u[d]++
                        if d != num/d:
                            u[num/d]++

        (2) mul[i] = number of PAIRS whose gcd is MULTIPLE of i
                    = C(u[i], 2)

        (3) Inclusion–Exclusion (reverse from max → 1)
            mul[i] = g[i] + g[2i] + g[3i] + ...
            So:
                g[i] = mul[i] - (g[2i] + g[3i] + ...)

        (4) Print all g[i] > 0.


        =====================================================================
        2) FACTOR SIEVE — O(N log N) PRECOMPUTATION
        =====================================================================
        Goal:
            Precompute ALL factors for all numbers up to MAX.

        Why?
            √n factorization is slow for repeated queries.
            Sieve builds:
                factors[j] = all i where i divides j

        Algorithm:
            for i in 1..MAX:
                for j = i; j <= MAX; j += i:
                    factors[j].add(i)

        Time:
            MAX * (1 + 1/2 + 1/3 + ...) = MAX log MAX.

        WHY DISTINCT ARRAY?
        -------------------
        After factor sieve:
            factors[x] already known for all x.

        If array = {12,12,12}:
            Using distinct {12} avoids repeating work 3 times.
            Saves time, avoids repeated freq updates, avoids overcount.


        =====================================================================
        3) REAL VERSION — SUBSET GCD QUERIES
        =====================================================================
        Query:
            (k, g): Is there a subset of size k whose GCD = g?

        Constraints:
            1 <= k <= 5
            array values <= 100000

        Global idea:
        -----------------------------------------------------------
        1) freq[i] = how many numbers in arr divisible by i.

        2) mul[i][k] = number of k-sized subsets whose all elements 
                    are divisible by i
                    = C(freq[i], k)

        3) Inclusion–exclusion:
            mul[i][k] = gcd[i][k] + gcd[2i][k] + gcd[3i][k] + ...
        So:
            gcd[i][k] = mul[i][k]
                        - (gcd[2i][k] + gcd[3i][k] + ...)

        4) Answer:
            if gcd[g][k] > 0 → YES
            else → NO

        NOTE:
            For YES/NO correctness, nCr MUST be exact if values can overflow.
            (modulo can hide zeros).


        =====================================================================
        4) INCLUSION–EXCLUSION — WHY REVERSE ORDER?
        =====================================================================
        Because:
            g[i] depends on g[2i], g[3i], ...
        So we process i from MAX DOWN TO 1.

        Reverse ensures that when computing g[i],
        all g[multiples_of_i] are already known.


        =====================================================================
        5) nCk (COMBINATORICS) — FULL EXPLANATION
        =====================================================================
        We know:
            C(n,k) = n! / (k! * (n-k)!)

        Problem:
            Division does NOT work under modulo arithmetic.

        Solution:
            Replace division with multiplication by modular inverses.

            C(n,k) % M =
                fact[n] 
                * inverse(fact[k]) 
                * inverse(fact[n-k])
                % M


        --------------------------
        5.1) WHAT IS MODULAR INVERSE?
        --------------------------
        inverse(x) is a number y such that:
            (x * y) % MOD = 1

        Example (mod 7):
            3 * 5 = 15 ≡ 1 (mod 7)
        So inverse(3) = 5.


        --------------------------------------------
        5.2) HOW DO WE COMPUTE INVERSE(x) FAST?
        --------------------------------------------
        Using Fermat’s Little Theorem (MOD prime):

            x^(MOD-1) ≡ 1 (mod MOD)
        →  x^(MOD-2) ≡ inverse(x)

        So:
            inverse(x) = modPow(x, MOD - 2)

        This is why modPow is used.


        -----------------------------------------------------
        5.3) WHY invFact[] ARRAY EXISTS (INVERSE FACTORIALS)
        -----------------------------------------------------
        We want:
            inverse(k!)
            inverse((n-k)!)

        Using Fermat:
            invFact[MAX] = inverse(fact[MAX])
                        = fact[MAX]^(MOD-2)

        To find invFact[i-1], observe:
            n! = n * (n-1)!
        This identity rearranged gives:
            (n-1)! = n! / n

        Now divide both sides by n!(n-1)! (algebraic manipulation to isolate inverses):
            1 / (n-1)! = n / n!

        Convert to modulo world where division becomes multiplication by inverse:
            inverse((n-1)!) = n × inverse(n!)

        So the modular identity becomes:
            invFact[i-1] = (invFact[i] * i) % MOD

        This is the key relation used to fill invFact[] in O(MAX) time by going backwards.


        ------------------------------------------
        5.4) FINAL nCr FORMULA (MOD VERSION)
        ------------------------------------------
        C(n,k) % MOD = fact[n] * invFact[k] % MOD * invFact[n-k] % MOD


        ------------------------------------------
        5.5) EXACT nCr (WITHOUT MOD)
        ------------------------------------------
        For k ≤ 5, safe & fast multiplicative formula:

        C(n, k) =
            (n * (n-1) * ... * (n-k+1)) / (k!)

        Use BigInteger to avoid overflow:

        E.g., C(n,3) = n*(n-1)*(n-2) / 6


        =====================================================================
        6) MINI EXAMPLE — C(5,2)
        =====================================================================
        fact: [1,1,2,6,24,120]
        invFact[5] = inverse(120)
        invFact[4] = invFact[5] * 5
        invFact[3] = invFact[4] * 4
        ...

        C(5,2) = fact[5] * invFact[2] * invFact[3] = 10


        =====================================================================
        7) SUMMARY OF EVERYTHING IN 15 SECONDS
        =====================================================================
        • u[i] = how many array numbers divisible by i  
        • mul[i] = pairs/subsets with gcd multiple of i  
        • g[i] = exact-GCD count computed via inclusion–exclusion  
        • Factor sieve computes all divisors for all numbers in O(N log N)  
        • Use DISTINCT array to avoid repeated factor processing  
        • nCk under modulo = fact[n] * invFact[k] * invFact[n-k] % MOD  
        • inverse(x) = x^(MOD-2) via Fermat  
        • invFact built backwards since inverse((n-1)!) = inverse(n!) * n  
        • For YES/NO subset-GCD queries, exact nCr (BigInteger) is safest  
        • Process GCD values from MAX → 1  

        =====================================================================
                                END OF NOTES
        =====================================================================


            -> Same Concept ->  (as no.of pairs whose gcd == y)
                    but here (nC3) instead of (nC2)   (as we have to find triplets) 
                        ( formula- nC3 = n*(n-1)*(n-2)/6 )    <== ( nC3 = n!/(3!(n-3)! )  ==> nC3 = n*(n-1)*(n-2)/3*2*1 ) )
            mul[2] ⇒ g[2] + g[4] + g[6] + g[8] + ………. 
 */