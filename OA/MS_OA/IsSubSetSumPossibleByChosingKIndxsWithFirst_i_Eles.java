package OA.MS_OA;

import java.util.HashMap;
import java.util.Map;

public class IsSubSetSumPossibleByChosingKIndxsWithFirst_i_Eles {
    

    // easy
    public static boolean isSubSetSumPossible(int[] arr, int sum) {
        int n = arr.length;
        int[][] dp = new int[arr.length + 1][sum + 1];

        // base-case
        // dp[0][0] = 1; // sum = 0 is possible with empty-subset


        // initialization
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1; // sum = 0 is possible with empty-subset
        }
        for (int j = 1; j <= sum; j++) {
            dp[0][j] = 0; // sum > 0 is not possible with empty-subset
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= sum; j++) {
                if (arr[i - 1] <= j) {
                    dp[i][j] = dp[i - 1][j - arr[i - 1]] == 1 || dp[i - 1][j] == 1 ? 1 : 0;
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        if(dp[n][sum] == 1){
            return true;
        } else {
            return false;
        }

    }


    // Follow-Up:   How many ways are there to achieve a subSet-Sum "y" in the given array
    public static int countWaysToGetSubSetSum(int[] arr, int sum) {
        int n = arr.length;

        int[][] dp = new int[n + 1][sum + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1;
        }
        for (int j = 1; j <= sum; j++) {
            dp[0][j] = 0; // sum > 0 is not possible with empty-subset
        }
        

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= sum; j++) {
                if (arr[i - 1] <= j) {
                    dp[i][j] = dp[i - 1][j - arr[i - 1]] + dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[n][sum];
    
    }


    /*Real version :- Given two arrays “A” and “B” --> consider all subsets of size “K” → for each subset calculate g == min(A[i1] + A[i2] + …. + A[ik], B[i1] + B[i2] + B[i3] + …….. + B[ik]) 
            → Find maximum possible ‘g’ over all the subsets
    
    */

    public static long getMaxGByChoosingKIndxs(long[] A, long[] B, int givenK) {
        int n = A.length;

        long maxG = Long.MIN_VALUE;

        long[] c = new long[n];     // c[i] = A[i] - B[i]

        for (int i = 0; i < n; i++) {
            c[i] = A[i] - B[i];
        }

        // using 3D-map, since we can't have negative-indices for the 3rd-dim (k) which is from ( -2000 to 2000 )
       // 3D Map: dp[i][k][diff] → max sum(B) achievable
       Map<Integer, Map<Integer, Map<Long, Long>>> dp = new HashMap<>();

       // base-case
         dp.put(0, new HashMap<>());
         dp.get(0).put(0, new HashMap<>());
         dp.get(0).get(0).put(0L, 0L); // since, when no elements are chosen, diff = 0 and sum(B) = 0
         
         //forwaed-dp
         for (int i = 1; i <= n; i++) {

             dp.putIfAbsent(i, new HashMap<>());

             for (int k = 1; k <= givenK; k++) {

                 dp.get(i).putIfAbsent(k, new HashMap<>());

                 // pick    ( knapsack-DP )
                 if (dp.containsKey(i - 1) && dp.get(i - 1).containsKey(k - 1)) { // here we check for(i-1, k-1) exists or not, since we are picking the i-th element
                     for (Map.Entry<Long, Long> entry : dp.get(i - 1).get(k - 1).entrySet()) {
                         long prevSumB = entry.getKey();
                         long sumB = entry.getValue();

                         long newDiff = prevSumB + c[i - 1];
                         long newSumB = sumB + B[i - 1];
                         dp.get(i).get(k).merge(newDiff, newSumB, Math::max); // .merger( key, new-value, merger-fn ) :- if key exists, apply mergerFn(oldValue, newValue) to compute updated value;       else : insert (key, newValue) into the map.

                     }

                 }

                 // not-pick
                 if (dp.containsKey(i - 1) && dp.get(i - 1).containsKey(k)) { // here we check for(i-1, k) exists or not, since we are not picking the i-th element

                     for (Map.Entry<Long, Long> entry : dp.get(i - 1).get(k).entrySet()) {
                         long prevSumB = entry.getKey();
                         long sumB = entry.getValue();

                         dp.get(i).get(k).merge(prevSumB, sumB, Math::max);
                     }

                 }

             }

         }
         
        // calculating the maxG from dp[n][givenK][diff >= 0] , since we need to maximize min(sumA, sumB)  --> which is equivalent to maximizing sumB when diff >= 0 (i.e., sumA >= sumB)
        if(dp.containsKey(n) && dp.get(n).containsKey(givenK)) {
            for(Map.Entry<Long, Long> entry : dp.get(n).get(givenK).entrySet()) {
                if(entry.getKey() >=0) maxG = Math.max(maxG, entry.getValue());
            }
        }
        
       
        // reverse-dp :-  sum(B) - sum(A)    , now we store the value of sum(A) achievable for each diff
        
        for (int i = 0; i < n; i++) {

            c[i] = -c[i]; // c[i] = B[i] - A[i]  ; just reversing the sign of c[i]

        }

        Map<Integer, Map<Integer, Map<Long, Long>>> dp2Rev = new HashMap<>();
        // base-case
        dp2Rev.put(0, new HashMap<>());
        dp2Rev.get(0).put(0, new HashMap<>());
        dp2Rev.get(0).get(0).put(0L, 0L);
        

        for (int i = 1; i <= n; i++) {
        
            dp2Rev.putIfAbsent(i, new HashMap<>());

            for (int k = 1; k <= givenK; k++) {
            
                dp2Rev.get(i).putIfAbsent(k, new HashMap<>());

                // not-pick
                if(dp2Rev.containsKey(i - 1) && dp2Rev.get(i - 1).containsKey(k)) { 

                    for (Map.Entry<Long, Long> entry : dp2Rev.get(i - 1).get(k).entrySet()) {
                        long prevSumA = entry.getKey();
                        long sumA = entry.getValue();

                        dp2Rev.get(i).get(k).merge(prevSumA, sumA, Math::max);
                    }

                }


                // pick
                if(dp2Rev.containsKey(i - 1) && dp2Rev.get(i - 1).containsKey(k - 1)) { 

                    for (Map.Entry<Long, Long> entry : dp2Rev.get(i - 1).get(k - 1).entrySet()) {
                        long prevSumA = entry.getKey();
                        long sumA = entry.getValue();

                        long newDiff = prevSumA + c[i - 1];
                        long newSumA = sumA + A[i - 1];
                        dp2Rev.get(i).get(k).merge(newDiff, newSumA, Math::max);

                    }

                }
            
            
            }
        
        
        }
       
       
       if(dp2Rev.containsKey(n) && dp2Rev.get(n).containsKey(givenK)) {
            for(Map.Entry<Long, Long> entry : dp2Rev.get(n).get(givenK).entrySet()) {
                if(entry.getKey() >=0) maxG = Math.max(maxG, entry.getValue());
            }
        }
       
        return maxG;

    }
    


    public static void main(String[] args) {
        int arrTC1[] = { 2, 5, 1, 8, 10 };
        int yTC1 = 26;    // sum

        // easy
        System.out.println("Is Subset Sum of 12 Possible With First i Eles ? :-> " + isSubSetSumPossible(arrTC1, yTC1));


        // follow-up
        int yTC2 = 10;    // sum
        System.out.println(" Number of ways to get Subset-Sum of " + yTC2 + " is: " + countWaysToGetSubSetSum(arrTC1, yTC2));

        
        // real-version
        long A[] = { 6, 3, 6, 5, 1 };
        long B[] = { 1, 4, 5, 9, 2 };
        
        int k = 3;

        System.out.println("Max subset-sum-g by choosing k indxs is : " + getMaxGByChoosingKIndxs(A, B, k));


        

    }



}
