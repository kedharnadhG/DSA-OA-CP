package OA.AMZ_OA;

import java.util.*;

//doc-link :  https://docs.google.com/document/d/1Rm6F4lrDZPCw114DLJPXg7igouXElWINVu8EXTZpQhs/edit?tab=t.0
// see notes at the end of this file

public class NoOfSubArrsWhoseAvgGTEqK {

    
    /*Brute force :- O(N^2) 
     
    */
    public static int countSubarraysWithAvgGTEqKBF(int[] arr, int n, int k) {
        int cnt = 0;

        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = i; j < n; j++) {
                sum += arr[j];
                int length = j - i + 1;
                double avg = (double) sum / length;
                if (avg >= k) {
                    cnt++;
                }
            }
        }

        return cnt;
    }

    
    // Easy-Version :-  using prefix-sums
    /*( prefix[j] - prefix[i-1] ) / (j-i+1) >= k
        prefix[j] - prefix[i-1] >= kj - ki + k
        prefix[j] - kj - k >= prefix[i-1] - ki
    
        a subarray [i…j] is valid if and only if :-> p[i-1] - k*i (LHS) == p[j] - k*j - k(RHS) 
    
        Find the number of pairs (i,j) such that LHS == RHS if this is satisfied it means [i….j] is a valid subarray whose average is “K” 
        -> For each index “j” → you need to know how many “i” can it form a valid bonding with in the range [1………j-1] 
    
        
        | Concept              | Correct Formula                                            | Wrong (shifted) Version               | Why wrong                                      |
        | -------------------- | ---------------------------------------------------------- | ------------------------------------- | ---------------------------------------------- |
        | Algebraic derivation | `pref[j] - k*j = pref[i-1] - k*i + k`                      | `pref[j] - k*j - k = pref[i-1] - k*i` | loses the `+K` compensation from `(j - i + 1)` |
        | Code (1-based)       | `LHS = pref[j - 1] - K*j + K`, `RHS = pref[j] - K*j`       | —                                     | matches exactly                                |
        | Code (0-based)       | `LHS = pref[j] - K*(j+1) + K`, `RHS = pref[j+1] - K*(j+1)` | —                                     | equivalent 0-based form                        |
    
        // see notes at the end of this file for detailed explanation
    
    */
    public static long countSubarraysWithAvgEqKEasy(int[] arr, int n, int k) {

        long[] pref = new long[n + 1];
        pref[0] = 0;
        for (int i = 1; i <= n; i++) {
            pref[i] = pref[i - 1] + arr[i - 1];
        }

        Map<Long, Long> freqMap = new HashMap<>();

        long count = 0;

        for (int j = 0; j < n; j++) {
            // LHS(i = j) → this will be used for future j’s
            long lhs = pref[j] - (long) k * (j + 1) + k;        // (j+1) because pref is 1-indexed, & "+k" is because of the derivation
            freqMap.put(lhs, freqMap.getOrDefault(lhs, 0L) + 1);

            // RHS(j) → current j
            long rhs = pref[j + 1] - (long) k * (j + 1);

            // If any LHS matches RHS, it means we found valid (i, j) pair
            count += freqMap.getOrDefault(rhs, 0L);
        }

        return count;

    }

    
    /*Real Version
         RHS >= LHS   ( avg >= k  )
    
         rhs : is fixed for each j
    */
    /*PBDS (Policy-Based Data Structures) — C++ only
        -----------------------------------------------
        Gives order-statistics features:
            find_by_order(k)  → k-th smallest element
            order_of_key(x)   → count of elements < x
        All in O(log n) time.

        Java equivalents:
        -----------------
        - No direct built-in structure.
        - Options:
        1️⃣ TreeSet / TreeMap (O(n) scanning)
        2️⃣ Fenwick Tree or Segment Tree (O(log n) with compression)
        3️⃣ Google Guava’s TreeMultiset (frequency tracking)
        4️⃣ Custom Order-Statistic Tree (augment size per node)

        For competitive programming:
        → Fenwick Tree is the go-to substitute for PBDS in Java.
    */



    public static void main(String[] args) {
        
        int[] arr = { 3, 4, 5, 6, 2 };
        int n = arr.length;
        int k = 4;

        // brute force
        System.out.println("------------------ Brute Force ------------------");
        int result = countSubarraysWithAvgGTEqKBF(arr, n, k);
        System.out.println("Number of subarrays with average >= " + k + " is: " + result);

        System.out.println("------------------ Easy Version ------------------");

        // Easy-version :->      #pairs(i,j) : ( lhs == rhs ) i.e  [i….j] is a valid subarray whose average is “K” 
        int[] arr1 = { 2,2,2,3,1};
        int k1 = 2;
        int n1 = arr1.length;
        long res = countSubarraysWithAvgEqKEasy(arr1, n1, k1);
        System.out.println("Number of pairs | subarrays with average >= " + k1 + " is: " + res);


        // real-version
        System.out.println("------------------ Real Version ------------------");



    }



}



/************************************************************************************
    🧠 FINAL TAKEAWAY NOTES — Subarray Average == K (Prefix-Sum Based Formula)
-------------------------------------------------------------------------------------

🔹 Goal:
    Count the number of subarrays [i..j] whose average equals K.

    Mathematically:
        (prefix[j] - prefix[i-1]) / (j - i + 1) = K

-------------------------------------------------------------------------------------
🔹 Step 1: Derivation
    Multiply both sides by (j - i + 1):
        prefix[j] - prefix[i-1] = K * (j - i + 1)

    Expand the right-hand side:
        prefix[j] - prefix[i-1] = K*j - K*i + K

    Rearrange (group j and i terms separately):
        prefix[j] - K*j = prefix[i-1] - K*i + K

    ✅ This is the 1-based form of our identity.

-------------------------------------------------------------------------------------
🔹 Step 2: Convert to 0-based indexing (using prefix array of size n+1)

    Define:
        prefix[0] = 0
        prefix[x] = arr[0] + arr[1] + ... + arr[x-1]   // sum of first x elements

    Substitute i → (i+1), j → (j+1):
        prefix[j+1] - K*(j+1) = prefix[i] - K*(i+1) + K

    ✅ 0-based usable form:
        RHS(j) = prefix[j+1] - K*(j+1)
        LHS(i) = prefix[i]   - K*(i+1) + K

-------------------------------------------------------------------------------------
🔹 Step 3: Why "+K" stays on LHS (and NOT moved as "-K")
    • The "+K" arises from expanding K*(j - i + 1).
      That "+1" represents the *inclusive length* of the subarray.
    • If you moved "+K" as "-K" to RHS:
          prefix[j] - K*j - K = prefix[i-1] - K*i
      ⇒ it becomes K*(j - i - 1), meaning subarrays of length (j - i - 1)
         → ❌ misses all single-element subarrays.

    ✅ Keep "+K" on the LHS side to correctly handle all subarrays (length ≥ 1).

-------------------------------------------------------------------------------------
🔹 Step 4: Implementation logic (0-based)

    for (int j = 0; j < n; j++) {
        long lhs = prefix[j] - K*(j+1) + K;   // prepare LHS(i=j)
        freq[lhs]++;                          // insert first (so i==j also valid)
        long rhs = prefix[j+1] - K*(j+1);     // current RHS(j)
        count += freq[rhs];                   // count all matches (i ≤ j)
    }

-------------------------------------------------------------------------------------
🔹 Step 5: Intuition summary
    • Each (i, j) pair corresponds to a unique subarray [i..j].
    • Equation ensures avg(arr[i..j]) == K.
    • Adding LHS first includes single-element subarrays (i == j).
    • Final count = total subarrays whose average equals K.

-------------------------------------------------------------------------------------
    ✅ FINAL FORMULA (forever remember):
        prefix[j+1] - K*(j+1) = prefix[i] - K*(i+1) + K

    Count pairs (i, j) satisfying this ⇒ number of valid subarrays.

************************************************************************************/


/*************************************************************************************
    🧩 Concept Notes — Understanding "pairs" & "subarrays" and the LHS–RHS order
--------------------------------------------------------------------------------------

🔹 1. Why (i, j) pairs represent subarrays:
    • A subarray is defined by its start and end indices — i.e. [i … j].
    • Each unique pair (i, j) corresponds to exactly one subarray.
        → Example: (0,2) ⇒ subarray arr[0..2].
    • So, counting valid (i, j) pairs is exactly the same as counting subarrays
      that satisfy the given condition (like average == K).

🔹 2. What our formula checks:
       prefix[j+1] - K*(j+1) = prefix[i] - K*(i+1) + K
    • This is a rearranged form of:
          average(arr[i..j]) == K
      meaning — for every j, we need to find all i ≤ j
      that make the above equation true.

🔹 3. Why we insert LHS first and check RHS later:
    • LHS(i) = prefix[i] - K*(i+1) + K   → depends on the starting index i
    • RHS(j) = prefix[j+1] - K*(j+1)     → depends on the ending index j
    • When we are at index j in the loop:
         - The map (freq) already stores all previous LHS(i) for i < j.
         - We FIRST insert LHS for i = j, so that current index is also included.  ,  i=1,j=1 : [1..1] ,  to check the subarray of length 1 (i == j) is also valid or not
         - Then we check RHS(j):
               count += freq[RHS(j)]
           This ensures subarrays of length 1 (i == j) are also counted.

    ✅ Inserting LHS first ⇒ counts all valid subarrays [i..j] including i == j.
    ❌ Checking RHS first ⇒ would skip single-element subarrays.

🔹 4. At the end:
    • Each time LHS(i) == RHS(j), it means subarray [i..j] has average = K.
    • The final count = total number of subarrays satisfying the condition.

--------------------------------------------------------------------------------------
    TL;DR:
        → Each (i, j) pair ↔ one subarray [i..j].
        → Insert LHS first, then check RHS — to include current j’s subarray too.
*************************************************************************************/
