package INTERVIEW_PROBS;

// Doc: https://docs.google.com/document/d/1HJ9JFzTVNJM5U4eNeh7ofVxSyjfyETHH3Ryy-ak7T30/edit?tab=t.0


/*
⚙️ 5️⃣ TL;DR Summary
----------------------------------------
| Goal                       | Binary Search Condition      | Relation       |
|-----------------------------|------------------------------|----------------|
| k-th smallest subarray sum  | Find smallest u where F(u) ≥ k           | Direct         |
| k-th largest subarray sum   | Find smallest u where F(u) ≥ (total - k + 1) | Mirror version |
----------------------------------------
*/



public class KthSmallestSubarraySum {

    // TC: O(n * log(sum(arr)) ) SC: O(1)
   public static long kthSmallestSubarraySum(int[] arr, long k) {
        int n = arr.length;

        long low = 0, high = 0;
        for (int num : arr) high += num; // max possible sum

        long ans = -1;
        while (low <= high) {
            long mid = low + (high - low) / 2;
            long cnt = countSubarraysWithSumAtMost(arr, mid);

            if (cnt >= k) {
                ans = mid; // possible answer, but try smaller
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }

    private static long countSubarraysWithSumAtMost(int[] arr, long limit) {
        long count = 0;
        long sum = 0;
        int i = 0;

        for (int j = 0; j < arr.length; j++) {
            sum += arr[j];
            while (sum > limit) {
                sum -= arr[i++];
            }
            count += (j - i + 1);
        }
        return count;
    }




    public static void main(String[] args) {
        int[] arr = { 1,2,3,4 };
        int n = arr.length;
        long k = 6;     // kth smallest subarray sum
        System.out.println(k + "'th Smallest Subarray Sum: " + kthSmallestSubarraySum(arr, k)); // output = 4


        //  for kth largest subarray sum
        long totalSubarrays = (long) n * (n + 1) / 2;
        long kthSmallestIndex = totalSubarrays - k + 1;
        System.out.println(k +  "'th Largest Subarray Sum: " + kthSmallestSubarraySum(arr, kthSmallestIndex));

    } 
}





/* Brute Force: TC: O(sum * n) SC: O(1)

    // for the easy-version: TC: O(n * log(sum(arr)) )
    // refer the "  KthSmallestSubarraySum.java  " file/*
    * 🧠 Brute Force (O(sum * n))
    * Problem: Find k-th smallest subarray sum
    * Conditions:
    *  - Array elements are non-negative
    *  - n ≤ 10^4
    * 
    * Approach:
    *  1️⃣ Compute F(u): number of subarrays with sum ≤ u
    *  2️⃣ Loop u = 0 → totalSum
    *  3️⃣ First u where F(u) ≥ k ⇒ is the k-th smallest subarray sum
    

        import java.util.*;

        public class KthSmallestSubarraySumBruteForce {
            public static void main(String[] args) {
                int[] arr = {2, 1, 3};
                int n = arr.length;
                long k = 4;  // Example: 4th smallest

                long totalSum = 0;
                for (int x : arr) totalSum += x;

                long ans = -1;
                for (long u = 0; u <= totalSum; u++) {
                    long count = countSubarrays(arr, n, u); // F(u)
                    if (count >= k) {
                        ans = u;
                        break;
                    }
                }

                System.out.println("K-th smallest subarray sum = " + ans);
            }

            // 🧮 F(u): counts number of subarrays with sum ≤ u
            static long countSubarrays(int[] arr, int n, long u) {
                long count = 0;
                long sum = 0;
                int i = 0;
                for (int j = 0; j < n; j++) {
                    sum += arr[j];
                    while (sum > u) {
                        sum -= arr[i];
                        i++;
                    }
                    count += (j - i + 1);
                }
                return count;
            }
        }

*/