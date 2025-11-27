package INTERVIEW_PROBS;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

// doc: https://docs.google.com/document/d/12VQ0FbaCEuFn9S1YVWIVy8uhJTocZ5oxET3YpkwkW5w/edit?tab=t.0

public class CountMediansInPrefix {

    static void insert(TreeMap<Integer, Integer> map, int val) {
        map.put(val, map.getOrDefault(val, 0) + 1);
    };

    static void remove(TreeMap<Integer, Integer> map, int val) {
        int freq = map.get(val);
        if (freq == 1) {
            map.remove(val);
        } else {
            map.put(val, freq - 1);
        }
    }

    static int last(TreeMap<Integer, Integer> map) {
        return map.lastKey();
    }

    static int first(TreeMap<Integer, Integer> map) {
        return map.firstKey();
    }

    static int size(TreeMap<Integer, Integer> map) {
        // map.size() gives distinct keys; we need total elements, so sum frequencies.
        int s = 0;
        for (int v : map.values()) {
            s += v;
        }

        return s;
    }
    


    /*Brute Force : O(N^3 * log(N))       
                     if we use sorting for the each subArray then TC will be O(N^3 * N log(N))
           
            You can use two heap techniques-also (instead of two sorted sets) as well to maintain the median of a running stream of numbers. 
    */
    public static void countMediansInPrefixBF(int[] arr, int n) {
        
        for (int i = 0; i < n; i++) {
            HashSet<Integer> medians = new HashSet<>();

            for (int l = 0; l <= i; l++) {
                TreeMap<Integer, Integer> left = new TreeMap<>();
                TreeMap<Integer, Integer> right = new TreeMap<>();

                int leftSize = 0, rightSize = 0;

                for (int r = l; r <= i; r++) {
                    int x = arr[r];

                    if (leftSize == 0 || x <= last(left)) {
                        // x belongs to left
                        insert(left, x);
                        leftSize++;
                    } else {
                        // x belongs to right
                        insert(right, x);
                        rightSize++;
                    }

                    //rebalance     // Segregating the median element while maintaining two sorted sets 
                    //Condition : s1==s2 or s1>s2
                    while (leftSize > rightSize + 1) {
                        int val = last(left);

                        remove(left, val);
                        insert(right, val);
                        leftSize--;
                        rightSize++;
                    }
                    while (rightSize > leftSize) {
                        int val = first(right);

                        remove(right, val);
                        insert(left, val);
                        leftSize++;
                        rightSize--;
                    }

                    int len = r - l + 1;
                    if (len >= 2) {
                        medians.add(last(left));
                    }

                }
            }

            System.out.println("Number of distinct medians in prefix of length " + (i + 1) + " is : " + medians.size());

        }


    }




    // Optimized :  Using "Median-Tricks" : O(N^2)    
    /* can 'g' be the median of some subarray, 
            G == 1 : is "1" can be the median of some subarray ...
            G==1 --> Find the smallest valid j1 :-  g==1 contributes F(j1), F(j1+1),.....F(n)            ( F(j1) means 1-distinct median is contributing F(j1) ;  means from 'j1' to "n" all subarrays will have g as median)
            G==2 --> Find the smallest valid j2 :-  g==2 contributes F(j2), F(j2+1),.....F(n)
    
            for updation of F array we can use concept  :  "Range-Update-Trick"
    */

    // ------------------------------------------------------
    // Optimized: Using Median-Trick + Prefix-Sum + Range-Update   : O(N^2)
    // ------------------------------------------------------

    /*  Idea:
        For each value g = 1..n:
            Convert array into +1 / -1 / 0 relative to g
            Find smallest index r such that a subarray including g has:
                    sum == 0 OR sum == 1
                    AND length >= 2
            Then g contributes to all prefixes r..n-1
    */
    public static int[] CountMediansInPrefixOptimized(int[] arr, int n) {
        int[] F = new int[n]; // F[i] : number of distinct medians in prefix of length i+1
    
        // pos[g] : position of value g in arr (since p is a permutation of 1..n)
        int[] pos = new int[n + 1];
        for (int i = 0; i < n; i++) {
            pos[arr[i]] = i;
        }

        // Trying every possible median-value -g
        for (int g = 1; g <= n; g++) {

            int center = pos[g]; // index where g is located
            int[] a = new int[n]; // transformed array relative to g

            // Transform array into +1 / -1 / 0 relative to g
            for (int i = 0; i < n; i++) {
                if (arr[i] < g) {
                    a[i] = -1;
                } else if (arr[i] > g) {
                    a[i] = 1;
                } else {
                    a[i] = 0; // the position of g itself
                }
            }

            // prefix sum map: prefix sum value -> smallest index where it occurs
            HashMap<Integer, Integer> firstPos = new HashMap<>();
            firstPos.put(0, -1); // prefix sum 0 occurs at index -1 (before array starts)
            int prefixSum = 0;

            int bestEnd = Integer.MAX_VALUE; // smallest valid end index for subarray with median g

            // find smallest valid subarray [l..r]  including g 
            for (int r = 0; r < n; r++) {
                prefixSum += a[r];

                // We need earlier index L-1 such that:
                // prefix[r] - prefix[L-1] == 0 or 1
                // → prefix[L-1] == prefix[r] or prefix[r]-1

                int want1 = prefixSum;
                int want2 = prefixSum - 1;

                if (firstPos.containsKey(want1)) {
                    int Lminus1 = firstPos.get(want1);
                    int L = Lminus1 + 1; // starting index of subarray

                    // Check if subarray [L..r] includes g and has length >= 2
                    if (L <= center && center <= r && (r - L + 1) >= 2) {
                        bestEnd = r;
                        break;
                    }
                }

                if (firstPos.containsKey(want2)) {
                    int Lminus1 = firstPos.get(want2);
                    int L = Lminus1 + 1; // starting index of subarray

                    // Check if subarray [L..r] includes g and has length >= 2
                    if (L <= center && center <= r && (r - L + 1) >= 2) {
                        bestEnd = r;
                        break;
                    }
                }

                // Record first occurrence of this prefix sum
                firstPos.putIfAbsent(prefixSum, r);

            }

            // If we found a valid subarray, g contributes to all prefixes bestEnd..n-1
            if (bestEnd != Integer.MAX_VALUE) {
                F[bestEnd] += 1; // range update start

            }

        }
        
        // Prefix sum on F to get final counts
        for (int i = 1; i < n; i++) {
            F[i] += F[i - 1];
        }

        return F;
    }

    public static void main(String[] args) {
        int n = 5;
        int[] arr = { 3, 1, 4, 5, 2 };

        System.out.println("-------------------Brute Force------------------");
        // Counting distinct medians obtainable inside each prefix ( subarray of size>=2).
        countMediansInPrefixBF(arr, n);


        // ------------------------------------------------------
        // Optimized: Using Median-Trick + Prefix-Sum + Range-Update
        System.out.println("-------------------Optimized------------------");
        int[] res = CountMediansInPrefixOptimized(arr, n);
        for (int i = 0; i < n; i++) {
            System.out.println("Number of distinct medians in prefix of length " + (i + 1) + " is : " + res[i]);
        }

    }
}




/* QUICK NOTES : Median-Trick + O(N^2) Solution
    --------------------------------------------

    1) Fix a value g (1..n). Ask: can g be median of some subarray?

    2) Build array:
            >g → +1
            <g → -1
            g → 0  (only once, since permutation)

    3) A subarray has median g iff:
            sum == 0 OR sum == 1
            AND it includes the index where g occurs
            AND length >= 2

    4) Using prefix sums:
            sum(L..R) = prefix[R] - prefix[L-1]
            We need:
                prefix[L-1] == prefix[R]
                prefix[L-1] == prefix[R] - 1

    5) For each g, find smallest R such that [L..R] is valid.

    6) Once g becomes a valid median at R:
            g contributes to all prefixes R..n-1
    → Apply range-update:
            res[R]++

    7) Finally prefix-sum the res[] array to get f(i).

    --------------------------------------------
    Complexity: O(N^2)
    --------------------------------------------
*/
