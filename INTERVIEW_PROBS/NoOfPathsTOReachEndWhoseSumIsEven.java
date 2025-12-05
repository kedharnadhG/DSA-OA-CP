package INTERVIEW_PROBS;


// prob : Problem :- Given a grid of size N*M filled with positive integers -> how many paths exist from (1,1) to (N,M) whose sum is even

//doc : https://docs.google.com/document/d/1GAAg03auzh8R804_ZVo-vsKF-0UPbpaXgtTKKfGyBjU/edit?tab=t.0
public class NoOfPathsTOReachEndWhoseSumIsEven {
    

    public static int countEvenSumPathsEasy(int[][] grid) {

        int n = grid.length;
        int m = grid[0].length;

        // dp[2][3][0] :- how many paths are there whose sum is even which are starting from [1,1] & end at [2,3]
        // dp[i][j][1] = how many paths exist which start from 1,1 and end at i,j whose sum is odd

        int[][][] dp = new int[n][m][2];

        // base case
        if(grid[0][0] % 2 == 0) {
            dp[0][0][0] = 1;
        } else {
            dp[0][0][1] = 1;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {

                if (i == 0 && j == 0)
                    continue; // already handled base case

                int val = grid[i][j];

                if (val % 2 == 0) {
                    // even
                    dp[i][j][0] += (i > 0 ? dp[i - 1][j][0] : 0) + (j > 0 ? dp[i][j - 1][0] : 0);    // even + even = even
                    dp[i][j][1] += (i > 0 ? dp[i - 1][j][1] : 0) + (j > 0 ? dp[i][j - 1][1] : 0);    // even + odd = odd
                } else {
                    // odd
                    dp[i][j][0] += (i > 0 ? dp[i - 1][j][1] : 0) + (j > 0 ? dp[i][j - 1][1] : 0);     // odd + odd = even
                    dp[i][j][1] += (i > 0 ? dp[i - 1][j][0] : 0) + (j > 0 ? dp[i][j - 1][0] : 0);       // odd + even = odd
                }

            }
        }
        
        return dp[n - 1][m - 1][0]; // number of paths whose sum is even ending at (N,M)

    }



    public static void main(String[] args) {
        int[][] grid = {
                { 2, 3, 4 },
                { 6, 5, 8 },
                { 1, 4, 3 }
        };

        System.out.println("Number of paths with even sum: " + countEvenSumPathsEasy(grid));
    }

}
