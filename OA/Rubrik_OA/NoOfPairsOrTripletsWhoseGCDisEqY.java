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
   
    static final int MAX = 1000000;
    
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



    public static void main(String[] args) {
        
        // FastReader sc = new FastReader();

        int n = 5;
        int[] arr = { 5, 4, 2, 6, 8 };
        // now print for all the gcd values from 1 to max(arr)  (i.e y=1 to P(max(arr)))
        System.out.println("---------- Easy-Version  ( No.Of Pairs Whose GCD is Y )---------- ");
        cntOfPairsWithGCDasY(arr, n);


        System.out.println("---------- Factors Optimised Version ---------- ");
        int[] arr2 = { 1, 12, 3, 4, 13, 12, 4, 1 };
        computeFactorsSieveOptimized(arr2, arr2.length);


        System.out.println("---------- Harder-Version  ( No.Of subSequences of size k whose GCD is Y )---------- ");
        

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

    -> Same Concept ->  (as no.of pairs whose gcd == y)
            but here (nC3) instead of (nC2)   (as we have to find triplets) 
                ( formula- nC3 = n*(n-1)*(n-2)/6 )    <== ( nC3 = n!/(3!(n-3)! )  ==> nC3 = n*(n-1)*(n-2)/3*2*1 ) )
    mul[2] ⇒ g[2] + g[4] + g[6] + g[8] + ………. 
 */