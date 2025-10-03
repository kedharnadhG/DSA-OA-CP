package OA;

import java.util.Arrays;
import java.util.Stack;

public class NoOfSubArraysWithMaxAsK1 {

    // DOC: https://docs.google.com/document/d/1CjdMnQDq-XgPaqoHvaNOUsu2FVy3lATMxYnwMs9ceY4/edit?tab=t.0
    // Git : https://github.com/kedharnadhG/DSA-OA-CP/blob/main/OA/NoOfSubArraysWithMaxAsK1.java

    /* Problem Description :-
        Given an array A of N integers and an integer K, find the number of subarrays in which the maximum element is equal to K.
     
     */


    /* Brute Force :-> 
        Time Complexity :-> O(N^2)
        Space Complexity :-> O(1)
    
        trying all the subarrays and checking if the max element is equal to K or not.
     
    */
    public static int countSubarraysWithMaxKBF(int[] arr, int k) {
        int n = arr.length;

        int count = 0;

        for (int i = 0; i < n; i++) {
            int max = Integer.MIN_VALUE;
            for (int j = i; j < n; j++) {
                int curr = arr[j];
                max = Math.max(max, curr);
                if (max == k) {
                    count++;
                }
            }
        }

        return count;

    }


    /*Better Approach :->
        (assuming "k" is repeating only once in the array) (no duplicates of k) ( array has distinct elements)
        Time Complexity :-> O(N)
        Space Complexity :-> O(N)
    */
    public static int countSubarraysWithMaxKBetter(int[] arr, int k) {
        int n = arr.length;

        // index of k
        int kIndex = -1;
        for (int i = 0; i < n; i++) {
            if (arr[i] == k) {
                kIndex = i;
                break;
            }
        }
        if (kIndex == -1) {
            return 0; // k not found in the array
        }

        // computing the PGE(Previous Greater Element) and NGE(Next Greater Element) arrays
        int[] pge = new int[n];
        int[] nge = new int[n];
        Arrays.fill(pge, -1);
        Arrays.fill(nge, -1);

        Stack<Integer> st = new Stack<>();

        // PGE
        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && arr[st.peek()] < arr[i]) {
                st.pop();
            }

            if (!st.isEmpty()) {
                pge[i] = st.peek();
            }
            st.push(i);
        }

        // clear the stack to be reused
        st.clear();

        // NGE
        for (int i = n - 1; i >= 0; i--) {

            while (!st.isEmpty() && arr[st.peek()] < arr[i]) {
                st.pop();
            }

            if (!st.isEmpty()) {
                nge[i] = st.peek();
            }

            st.push(i);

        }

        // calculating the count of subarrays with max element as k
        int x = kIndex - pge[kIndex]; // number of elements on the left side of k (including k)
        int y = (nge[kIndex] == -1 ? n : nge[kIndex]) - kIndex;

        return x * y;

    }
    

    /*  Harder-Version (when k can be repeated multiple times in the array) :-> 
        
     
    */



    public static void main(String[] args) {
        int[] arr = { 8, 2, 5, 1, 10 };
        int k = 5;


        int count = 0;

        count = countSubarraysWithMaxKBF(arr, k);

        System.out.println("(BF) The count of subarrays with max element as K is :-> " + count);

        System.out.println("-----------------------------------------------------------------");

        count = countSubarraysWithMaxKBetter(arr, k);

        System.out.println("(Better) The count of subarrays with max element as K is :-> " + count);
        
        System.out.println("-----------------------------------------------------------------");

        count = countSubarraysWithMaxKHarder(arr, k);

        System.out.println("(Harder) The count of subarrays with max element as K is :-> " + count);


    }

}
