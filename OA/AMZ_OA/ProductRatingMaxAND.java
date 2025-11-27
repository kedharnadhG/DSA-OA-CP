/*Output:
    single number = maximum AND achievable for some chosen m elements with total increment cost <= k
*/

// doc : https://docs.google.com/document/d/1ay6vidljVb-nilRgHoWLfHj4AMNeRiuFIaETGQKp-8k/edit?tab=t.0


package OA.AMZ_OA;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class ProductRatingMaxAND {

    public static void main(String[] args) {

        int n = 4;                  // number of products
        int m = 2;                  // we must choose exactly m items
        long k = 8;                 // total allowed cost for increasing ratings

        long[] real = {1, 2, 4, 8}; // actual product ratings (will be upgraded!)
        long[] b    = {1, 2, 4, 8}; // logical masked ratings (used ONLY for bitwise logic)
        // -------------------------------------------------------------------

        // Initially, all indices can potentially be selected
        HashSet<Integer> candidates = new HashSet<>();
        for (int i = 0; i < n; i++) candidates.add(i);

        long answer = 0; // we build AND bit-by-bit from MSB → LSB

        // Process bits from MOST SIGNIFICANT (31) to LEAST SIGNIFICANT (0)
        for (int bit = 31; bit >= 0; bit--) {

            long mask = 1L << bit; // current bit value (e.g., bit=3 → mask=8)

            ArrayList<Integer> ones  = new ArrayList<>();
            ArrayList<Integer> zeros = new ArrayList<>();

            // Partition current candidate items by whether this bit is logically present
            for (int idx : candidates) {
                if ((b[idx] & mask) != 0)
                    ones.add(idx);     // bit-i already set
                else
                    zeros.add(idx);    // bit-i missing
            }

            // CASE 1: Already enough items have this bit set logically
            if (ones.size() >= m) {
                answer |= mask;                  // include this bit in final AND
                candidates = new HashSet<>(ones); // only keep those items
            }

            // CASE 2: Not enough → we need to upgrade zeros
            else {
                int need = m - ones.size();

                if (zeros.size() >= need) {

                    // IMPORTANT:
                    // To set bit-i, the *actual rating* must be >= (answer | mask)
                    long targetValue = answer | mask;

                    ArrayList<long[]> costs = new ArrayList<>();

                    for (int idx : zeros) {

                        // cost to raise REAL rating to targetValue
                        long cost = Math.max(0, targetValue - real[idx]);

                        costs.add(new long[]{cost, idx});
                    }

                    // Choose the cheapest 'need' zeros
                    costs.sort(Comparator.comparingLong(a -> a[0]));

                    long totalCost = 0;
                    for (int i = 0; i < need; i++)
                        totalCost += costs.get(i)[0];

                    // If upgrade fits within remaining budget
                    if (totalCost <= k) {

                        k -= totalCost;     // spend budget
                        answer |= mask;     // lock this bit in final AND

                        HashSet<Integer> newCandidates = new HashSet<>(ones);

                        for (int i = 0; i < need; i++) {
                            int idx = (int) costs.get(i)[1];
                            newCandidates.add(idx);

                            // ACTUAL rating upgraded
                            real[idx] = targetValue;
                        }

                        candidates = newCandidates;
                    }
                }
            }

            // -------------------------------------------------------------------
            // CLEANING LOGICAL b[] FOR NEXT LOWER BIT
            //
            // We keep real[] untouched (actual rating), but for bit-based reasoning
            // we keep only lower bits (< current bit) inside b[].
            //
            // Example:
            //    bit = 3 → mask = 8 → lowerMask = 7 (0111)
            //
            //    b[idx] = real[idx] & lowerMask
            // -------------------------------------------------------------------

            long lowerMask = mask - 1;

            for (int idx : candidates) {
                b[idx] = real[idx] & lowerMask; // keep only bits below 'bit'
            }
        }

        System.out.println("Output: " + answer);  // CORRECT OUTPUT = 10
    }

}






/*  AMAZON OA — PRODUCT RATING SYSTEM (MASTER NOTES)
=====================================================================================

        PROBLEM (IN SIMPLE WORDS)
        -------------------------
        You are given 'n' product ratings.  
        You must choose EXACTLY 'm' products.  
        You are allowed to INCREASE ratings (each +1 costs 1 operation).  
        Total cost used must be <= k.  

        Goal:
            After possible upgrades, choose m items such that
                    their FINAL BITWISE AND is MAXIMUM.

        The AND becomes larger when numbers share HIGH bits (like 16, 8, 4...),
        so we try to force as many high bits as possible.


        WHY BITWISE GREEDY? WHY GO FROM HIGH BIT → LOW BIT?
        ----------------------------------------------------
        Bitwise AND is dominated by the MOST SIGNIFICANT BIT.

        Example:
            Setting bit-3 adds +8 to the AND,
            Setting bit-1 adds only +2.

        So first decide:
            "Can we make bit-31 = 1 in all m chosen elements?"
        If yes → do it.
        If not → skip it.
        Then check bit-30,
        then bit-29,
        ...
        down to bit-0.

        This greedy is OPTIMAL because higher bits give MUCH larger AND value
        than combining many lower bits.


        THE 'CANDIDATES' SET — WHAT DOES IT MEAN?
        -----------------------------------------
        candidates = the set of indices that CAN STILL be part of the final m items.

        Whenever we decide a bit MUST be 1 in the final AND,
        any item that CANNOT get that bit is removed from candidates.

        Thus candidates SHRINK as we lock more bits into the final AND.


        TWO ARRAYS: real[] AND b[] — WHY TWO ARRAYS?
        --------------------------------------------
        real[i] = the ACTUAL rating of item i.
                This is the true value on which we pay upgrades.

        b[i]    = a TEMPORARY, LOGICAL version used ONLY for bit-checking.

        We modify b[i] after each bit so that higher bits are removed.
        This ensures future bit-costs are computed CORRECTLY.


        WHY WE CLEAN USING:   b[i] = real[i] & ((1 << bit) - 1) ?
        ----------------------------------------------------------
        After we finish processing bit 'bit', that bit and all higher bits are ALREADY DECIDED.
        We do NOT want those bits to influence lower-bit checks.

        (1 << bit)   gives something like      1000
        ((1<<bit)-1) gives something like      0111

        So:
            b[i] = real[i] & 0111   → this REMOVES bit-3 and all higher bits,
                                    keeping ONLY lower bits (bit 0,1,2).

        This keeps the future cost logic correct and prevents double-counting bits.


        HOW DO WE DECIDE WHETHER BIT 'i' CAN BE SET?
        --------------------------------------------
        mask = (1 << i)

        Split candidates into:
            ones  = items whose b[j] ALREADY has bit-i = 1
            zeros = items that DON'T have bit-i

        Case A: ones.size() >= m
                → Already enough items have the bit → FREE
                → answer |= mask
                → candidates = ones only

        Case B: ones.size() < m
                → We need to UPGRADE some zeros.
                → need = m - ones.size()

                Now we must upgrade 'need' zeros so that *they also have bit-i*.


        THE MOST IMPORTANT FORMULA:
        ----------------------------
        To force bit-i = 1 AND preserve previously set bits,
        each item must become at least:

                targetValue = answer | mask

        Example:
            If answer already has bit-3 (1000), and we consider bit-1 (0010):
                targetValue = 1000 | 0010 = 1010 (decimal 10)

        So to give an item bit-3 AND bit-1, raise it to 10.

        This formula ensures:
            - we DO NOT break higher bits,
            - we set the new bit,
            - we pay the MINIMUM possible cost.


        COST OF UPGRADING AN ITEM:
        --------------------------
        If real[j] is the current rating,
        then cost = max(0, targetValue - real[j])

        We compute this cost for all zeros,
        sort by cost,
        take the cheapest 'need' zeros.

        If totalCost <= k:
                - spend the cost
                - upgrade:
                    real[j] = targetValue
                - lock this bit: answer |= mask
                - candidates = ones U chosenZeros
        Else:
                - too expensive → skip this bit.


        WHY SORT AND PICK CHEAPEST ZEROS?
        ---------------------------------
        We want to use minimum possible budget.
        Choosing more expensive zeros could consume budget needed for higher-value bits.
        So we always pick the cheapest feasible zeros.


        AFTER EACH BIT:
        ----------------
        We update b[i] using:
                b[i] = real[i] & ((1<<bit)-1)
        This keeps only lower bits for the next iteration.


        COMPLETE FLOW PER BIT:
        ----------------------
        1. Split candidates into ones and zeros
        2. If enough ones:
                answer |= mask
                candidates = ones
        3. Else:
                compute costs to raise zeros to targetValue = answer | mask
                pick cheapest
                if affordable:
                    upgrade real[]
                    reduce k
                    lock bit in answer
                    update candidates
        4. Clean b[i] using (1<<bit)-1


        WHY THIS ALGORITHM IS 100% CORRECT?
        -----------------------------------
        - Higher bits matter more → must be taken first → greedy is correct.
        - Forcing bit-i is only possible if m items can reach targetValue.
        - targetValue = minimal valid number that satisfies all locked bits.
        - Picking cheapest zeros keeps budget usage optimal.
        - Cleaning b[] prevents double-counting higher bits.
        - real[] preserves actual upgrades so cost is accurate.
        - Works for ANY m (1 to n).
        - Works for ANY k (0 to 1e9).
        - Works for ANY rating values (1 to 1e9).


        WHY IT FITS WITHIN CONSTRAINTS?
        -------------------------------
        n <= 1e5
        Bits = 32
        Each bit: O(n log n) in worst case
        Total ≈ 32 * (1e5 log n) ≈ 50 million operations → FAST ENOUGH.

        Memory usage ~ few MB → SAFE.


        EXAMPLE FROM OA IMAGE:
        ----------------------
        ratings = [1,2,4,8], m=2, k=8

        Bit-3:
            need 1 zero
            4→8 costs 4 → OK
            real becomes [1,2,8,8], k=4
            answer = 8
            candidates = {8,8}

        Clean b → both become b=0

        Bit-1:
            targetValue = 8 | 2 = 10
            upgrade both 8→10 (cost=2 each = 4) → OK
            real becomes [10,10]
            answer = 10

        Final answer = 10


        FINAL TAKEAWAY (REMEMBER THIS):
        -------------------------------
        To maximize AND:
            - Try to set high bits first.
            - To set bit-i, every chosen item must become >= (answer | (1<<i)).
            - Cost = how much we need to raise real[].
            - Pick cheapest needed upgrades.
            - Clean b[] to ignore processed bits.
            - Repeat for lower bits.

        This procedure guarantees the maximum possible AND with budget k.


        =====================================================================================
                                    END OF MASTER NOTES
        =====================================================================================
*/
