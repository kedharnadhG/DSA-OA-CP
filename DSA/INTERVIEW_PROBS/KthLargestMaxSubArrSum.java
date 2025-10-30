package DSA.INTERVIEW_PROBS;

import java.util.Arrays;

public class KthLargestMaxSubArrSum {
    
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


    public static void main(String[] args) {
        
        int arr[] = { 1, 1, 1, 2, 5 };
        int k = 2;

        System.out.println("BF: The " + k + "th largest max subarray sum is : " + kthLargestSumBF(arr, k));



    }



}
