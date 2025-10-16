package DSA.DP;

public class MaxSumByJumpsOneTwoOrThree {

    // we can jump from : i --to-> ( i + 1 ) & (i + 3) & (i + 5) only
    
    public static int maxSum(int[] b) {

        int n = b.length;

        int[] dp = new int[n];

        dp[0] = b[0];
        dp[1] = b[0] + b[1];
        dp[2] = b[0] + b[1] + b[2];
        dp[3] = Math.max(dp[0] + b[3], dp[2] + b[3]);
        dp[4] = Math.max(dp[1] + b[4], dp[3] + b[4]);

        for (int i = 5; i < n; i++) {
            dp[i] = Math.max(dp[i - 1] + b[i], Math.max(dp[i - 3] + b[i], dp[i - 5] + b[i]));
        }
        
        return dp[n - 1];
        
    }

    public static void main(String[] args) {
        int[] b = { 5, 8, 3, 100, -5, -5, 5, 10 };

        System.out.println("The maximum sum is :-> " + maxSum(b));
    }
}
