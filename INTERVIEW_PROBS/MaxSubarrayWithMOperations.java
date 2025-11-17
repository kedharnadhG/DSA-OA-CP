package INTERVIEW_PROBS;

import java.util.*;

// doc :-  https://docs.google.com/document/d/17u_U0mT-lI6j3vNgC8faZws-jPdgXIjK2L0oVgOAHUk/edit?tab=t.0

public class MaxSubarrayWithMOperations {
    
    // kadane's Algorithm to find max subarray sum
    static long kadane(int[] arr) {
        long maxSoFar = Long.MIN_VALUE;
        long currSum = 0;

        for (int i = 0; i < arr.length; i++) {
            currSum = Math.max(arr[i], currSum + arr[i]);
            maxSoFar = Math.max(maxSoFar, currSum);
        }
        return maxSoFar;
    }


    /*Easy-Version:-
            Array “u” is given you are allowed to remove elements from the start or back of this array 1 at a time – and add those elements to the start or back of the main array 
            – you can do any number of operations you want – tell the maximum subarray sum you can get in the main array 
     
    */
    public static long maxSubarraySumWithUnlimitedOperations(int[] arr, int[] u) {
        int n = arr.length;
        int m = u.length;
        // Step 1: Calculate maximum subarray sum of the original array using Kadane's algorithm
        long maxSubarraySum = kadane(arr);

        // Sum of +ve elements in u
        long sumPositiveU = 0;
        for (int value : u) {
            if (value > 0) {
                sumPositiveU += value;
            }
        }

        long finalAns = Math.max(maxSubarraySum, sumPositiveU);

        // Calculate prefix sums of main array
        long maxPrefixSum = Long.MIN_VALUE;
        long currentPrefixSum = 0;
        for (int x : arr) {
            currentPrefixSum += x;
            maxPrefixSum = Math.max(maxPrefixSum, currentPrefixSum);
        }

        // Calculate suffix sums of main array
        long maxSuffixSum = Long.MIN_VALUE;
        long currentSuffixSum = 0;
        for (int i = n - 1; i >= 0; i--) {
            currentSuffixSum += arr[i];
            maxSuffixSum = Math.max(maxSuffixSum, currentSuffixSum);
        }

        // placing +ve_ele's_of_u at start or end of main array
        finalAns = Math.max(finalAns, sumPositiveU + maxPrefixSum);
        finalAns = Math.max(finalAns, sumPositiveU + maxSuffixSum);

        return finalAns;

    }

    
    /*Real-Version:- you can perform at most M operations on the array, tell the maximum subarray sum you can get in the main array 
     
        so, Try each possible combination of operations from 0 to M    :- 
        if m =4;
    
            0 from start , 4 from end
            1 from start , 3 from end
            2 from start , 2 from end
            3 from start , 1 from end
            4 from start , 0 from end
    */
    
    static long maxPositiveSumFromU(int[] u, int m) {
        int n = u.length;
        
        long[] left = new long[m + 1];
        long[] right = new long[m + 1];

        // left prefix-Positive sum
        for (int i = 1; i <= m; i++) {
            left[i] = left[i - 1] + Math.max(0, u[i - 1]);
        }
        
        // right suffix-Positive sum
        for (int i = 1; i <= m; i++) {
            right[i] = right[i - 1] + Math.max(0, u[n - i]);
        }

        long best = 0;

        // trying all splits :-    i-from Left , (m-i)-from Right
        for (int i = 0; i <= m; i++) {
            int j = m - i;

            if (i <= n && j <= n && (i + j) <= n) {
                best = Math.max(best, left[i] + right[j]);
            }
        }

        return best;
    }
    
    public static long maxSubarraySumWithMOperations(int[] mainArr, int[] u, int M) {
        
        int n = mainArr.length;
        int m = Math.min(u.length, M); // we can't take more than u.length elements


        long posSum = maxPositiveSumFromU(u, m);

        long ans = kadane(mainArr);


        // prefix 
        long prefix = 0, curr = 0;
        for (int x : mainArr) {
            curr += x;
            prefix = Math.max(prefix, curr);
        }
        
        // suffix
        long suffix = 0, curr2 = 0;
        for (int i = n - 1; i >= 0; i--) {
            curr2 += mainArr[i];
            suffix = Math.max(suffix, curr2);
        }

        ans = Math.max(ans, posSum);
        ans = Math.max(ans, posSum + prefix);
        ans = Math.max(ans, posSum + suffix);

        return ans;

    }


    public static void main(String[] args) {
        
        int[] arr = { 1, 3, -5, 2, 2 };
        int u[] = {  2, 3 };

        System.out.println(" ---------- Easy-Version ---------- ");
        System.out.println("Max Subarray Sum with unlimited Operations: " + maxSubarraySumWithUnlimitedOperations(arr, u));

        
        System.out.println(" ---------- Real-Version ---------- ");
        int m = 1; // max operations allowed
        System.out.println("Max SubArray Sum with M Operations: " + maxSubarraySumWithMOperations(arr, u, m));
    }
}
