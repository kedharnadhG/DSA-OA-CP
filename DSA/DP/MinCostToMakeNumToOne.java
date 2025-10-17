package DSA.DP;

public class MinCostToMakeNumToOne {
    

    public static void main(String[] args) {
        int n = 15;

        int y = 100;    // "-1" -> y
        int x = 1;   // "/7" -> x
        int z = 50;  // "/3" -> z
        int b = 85;    // "/5" -> b


        /* 0-based indexing
         
            // dp[i] corresponds to value (i + 1).
            int[] dp = new int[n];
            dp[0] = 0; // value 1 -> cost 0
        
            for (int i = 1; i < n; i++) {
                int value = i + 1;            // the actual number
                int best = dp[i - 1] + y;     // do -1 : value -> value-1 (index i-1)
                if (value % 3 == 0) best = Math.min(best, dp[(value / 3) - 1] + z);
                if (value % 5 == 0) best = Math.min(best, dp[(value / 5) - 1] + b);
                if (value % 7 == 0) best = Math.min(best, dp[(value / 7) - 1] + x);
                dp[i] = best;
            }
        
            System.out.println("Min cost to make " + n + " -> 1 is: " + dp[n - 1]); // 135
        */

        // 1-based indexing ( since the value is starting from 1 : Numbers in the problem start at 1, not 0 )
        int[] dp = new int[n + 1];
        dp[1] = 0; // cost to make 1 -> 1 is 0

        for (int v = 2; v <= n; v++) {
            int best = dp[v - 1] + y;         // move v -> v-1
            if (v % 3 == 0) best = Math.min(best, dp[v / 3] + z);
            if (v % 5 == 0) best = Math.min(best, dp[v / 5] + b);
            if (v % 7 == 0) best = Math.min(best, dp[v / 7] + x);
            dp[v] = best;
        }

        System.out.println("Min cost to make " + n + " -> 1 is: " + dp[n]); // 135


    }
}
