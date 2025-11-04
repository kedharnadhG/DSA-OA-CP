package YT_VIDS.AMZ_OA_INTV;

import java.util.Stack;

// Doc: https://docs.google.com/document/d/1Wnpg0OSJBZLcRzGwXe72ErE8TQCBp1X-E8onA131kEg/edit?tab=t.0

public class PackingEfficiency {
    
    /*Brute-Force: TC: O(N^3) 
    */
    public static boolean isValid(int[] arr, int j, int end){
        for (int i = j; i <= end; i++) {
            if (arr[i] > arr[j]) {
                return false;
            }
        }
        return true;
    }

    public static int getMaxEfficiencyBF(int[] arr, int k) {
        int n = arr.length;

        int EfficiencyCnt = 0;

        for (int i = 0; i <= n - k; i++) {

            int start = i;
            int end = i + k - 1;

            for (int j = start; j <= end; j++) {
                if (isValid(arr, j, end)) {
                    EfficiencyCnt++;
                }
            }

        }

        return EfficiencyCnt;

    }

    

    /*Better:  TC- O(N^2)
     
    */
    public static int getMaxEfficiencyBetter(int[] arr, int k) {

        int n = arr.length;

        int EfficiencyCnt = 0;

        int[] suffixMax = new int[n + 1];

        for (int i = 0; i <= n - k; i++) {

            int start = i;
            int end = i + k - 1;

            //preprocessing suffix max-array for each window
            suffixMax[end] = arr[end];
            for (int j = end - 1; j >= start; j--) {
                suffixMax[j] = Math.max(suffixMax[j + 1], arr[j]);
            }

            for (int j = start; j <= end; j++) {
                if (arr[j] > suffixMax[j + 1]) {
                    EfficiencyCnt++;
                }
            }

        }

        return EfficiencyCnt;

    }
    


    /*Optmized: Tc- O(N)
    
        for each-i :- calculating it's Contribution to the answer  ( using Next Greater Element concept)
    
    */
    public static int getMaxEfficiencyOpt(int[] arr, int k) {
        int n = arr.length;
        int EfficiencyCnt = 0;

        int[] nextGreater = new int[n];

        //finding Next Greater Element for each index
        Stack<Integer> st = new Stack<>();

        for (int i = n - 1; i >= 0; i--) {
            while (!st.isEmpty() && arr[st.peek()] <= arr[i]) st.pop();
            nextGreater[i] = st.isEmpty() ? n : st.peek();
            st.push(i);
        }

        
        //calculating contribution of each index to the answer
        for (int i = 0; i < n; i++) {
            
            int rightLimit = nextGreater[i] - 1;
            int leftLimit = Math.max(0, i - k + 1);

            int right_most_start = Math.min(i, n - k);

            int possible_starts = Math.min(right_most_start, rightLimit - k + 1) - leftLimit + 1;

            if (possible_starts > 0) {
                EfficiencyCnt += possible_starts;
            }


        }

        return EfficiencyCnt;

    }




    public static void main(String[] args) {
        
        int[] arr = { 3, 6, 2, 9, 4, 1 };
        int k = 3;

        System.out.println("Max Efficiency: " + getMaxEfficiencyBF(arr, k));
        
        System.out.println("---------------------");

        System.out.println("Max Efficiency: " + getMaxEfficiencyBetter(arr, k));

        System.out.println("---------------------");
        
        System.out.println("Max Efficiency: " + getMaxEfficiencyOpt(arr, k));

        

    }



}
