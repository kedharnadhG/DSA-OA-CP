package INTERVIEW_PROBS;

// Doc: https://docs.google.com/document/d/1HJ9JFzTVNJM5U4eNeh7ofVxSyjfyETHH3Ryy-ak7T30/edit?tab=t.0

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;


public class KthLargestMaxSubArrSum {

    static class SumWithRanges {
    
        int Sum;
        int start;
        int end;
        public SumWithRanges(int sum, int start, int end) {
            this.Sum = sum;
            this.start = start;
            this.end = end;
        }
        
    }
    
    /*Brute-Force :-   TC: O(N^2 log(N)) SC: O(N^2)
    
        Generate all subarray sums, store them in an array, sort the array, and return the k-th largest sum.
     
    */
    public static int kthLargestSumBF(int[] arr, int k) {
        int n = arr.length;
        int[] sumArr = new int[n * (n + 1) / 2];
        int index = 0;
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = i; j < n; j++) {
                sum += arr[j];
                sumArr[index++] = sum;
            }
        }

        System.out.println(Arrays.toString(sumArr));
        Arrays.sort(sumArr);

        return sumArr[sumArr.length - k];

    }



    /* Optimization: - TC: O(k log(k)) SC: O(k)
        (let constraint: 10^5) & since all-are +-ve numbers :-> we can use a increasing-Monotonic-Sum-Func  using 2-pointers
        Use a max-heap (priority queue) of size k to keep track of the k largest sums while generating subarray sums.    
     
    */
    public static int kthLargestSumOptimized(int[] arr, int k) {
        int n = arr.length;

        int[] prefSum = new int[n + 1];

        for (int i = 0; i < n; i++) {
            prefSum[i + 1] = prefSum[i] + arr[i]; // sum(l, r) = prefix[r+1] - prefix[l]
        }

        PriorityQueue<SumWithRanges> pq = new PriorityQueue<>(
                (a, b) -> b.Sum - a.Sum);

        pq.add(new SumWithRanges(prefSum[n] - prefSum[0], 0, n - 1));

        Set<String> visited = new HashSet<>();
        visited.add(0 + "," + (n - 1));

        int ans = 0;

        // ----------------------------------------------
        //  Extract the top element (largest sum) one by one
        // After popping a range [start, end], push its two children:
        //   → Remove left element → [start+1, end]
        //   → Remove right element → [start, end-1]
        //
        // Repeat k times. The kth extracted value is the kth largest subarray sum.
        // ----------------------------------------------

        for (int rank = 1; rank <= k; rank++) {

            SumWithRanges top = pq.poll(); // largest available subArray sum
            ans = top.Sum;

            // Generate left child by removing the leftmost element
            if (top.start + 1 <= top.end) {
                int newStart = top.start + 1;
                int newEnd = top.end;
                String key = newStart + "," + newEnd;
                if (!visited.contains(key)) {
                    int newSum = prefSum[newEnd + 1] - prefSum[newStart];
                    pq.add(new SumWithRanges(newSum, newStart, newEnd));
                    visited.add(key);
                }
            }

            // Generate right child by removing the rightmost element
            if (top.start <= top.end - 1) {
                int newStart = top.start;
                int newEnd = top.end - 1;
                String key = newStart + "," + newEnd;
                if (!visited.contains(key)) {
                    int newSum = prefSum[newEnd + 1] - prefSum[newStart];
                    pq.add(new SumWithRanges(newSum, newStart, newEnd));
                    visited.add(key);
                }
            }

        }

        return ans;

    }

    
    // for the easy-version: TC: O(n * log(sum(arr)) )
    // refer the "  KthSmallestSubarraySum.java  " file
    

    public static void main(String[] args) {
        
        int arr[] = { 1, 1, 1, 2, 5 };
        int k = 2;

        System.out.println("BF: The " + k + "th largest max subarray sum is : " + kthLargestSumBF(arr, k));

        System.out.println("Optimized: The " + k + "th largest max subarray sum is : " + kthLargestSumOptimized(arr, k));

        


    }



}
