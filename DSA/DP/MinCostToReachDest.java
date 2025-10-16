package DSA.DP;

public class MinCostToReachDest {

    // we can jump from : i --to-> ( i + 1 ) & (i + 3) only
    public static int minCost(int[] A) {

        int n = A.length;

        int[] dp = new int[n];

        dp[0] = 0;
        dp[1] = Math.abs(A[1] - A[0]);
        dp[2] = dp[1] + Math.abs(A[2] - A[1]);

        for (int i = 3; i < n; i++) {
            dp[i] = Math.min(
                    dp[i - 1] + Math.abs(A[i] - A[i - 1]),
                    dp[i - 3] + Math.abs(A[i] - A[i - 3]));
        }

        return dp[n - 1];

        /*
                dp[0] = 0;
                for (int i = 1; i < n; i++) {
                    int oneJump = dp[i - 1] + Math.abs(A[i] - A[i - 1]);
                    int threeJump = Integer.MAX_VALUE;
                    if (i - 3 >= 0) {
                        threeJump = dp[i - 3] + Math.abs(A[i] - A[i - 3]);
                    }
                    dp[i] = Math.min(oneJump, threeJump);
                }
                return dp[n - 1];
        
        */

    }


    public static void main(String[] args) {
        int[] A = { 4, 12, 13, 18, 10, 12 };

        System.out.println("The minimum cost to reach destination is :-> " + minCost(A));
    }
}
