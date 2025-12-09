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
        if (grid[0][0] % 2 == 0) {
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
                    dp[i][j][0] += (i > 0 ? dp[i - 1][j][0] : 0) + (j > 0 ? dp[i][j - 1][0] : 0); // even + even = even
                    dp[i][j][1] += (i > 0 ? dp[i - 1][j][1] : 0) + (j > 0 ? dp[i][j - 1][1] : 0); // even + odd = odd
                } else {
                    // odd
                    dp[i][j][0] += (i > 0 ? dp[i - 1][j][1] : 0) + (j > 0 ? dp[i][j - 1][1] : 0); // odd + odd = even
                    dp[i][j][1] += (i > 0 ? dp[i - 1][j][0] : 0) + (j > 0 ? dp[i][j - 1][0] : 0); // odd + even = odd
                }

            }
        }

        return dp[n - 1][m - 1][0]; // number of paths whose sum is even ending at (N,M)

    }

    
    // Follow-Up 1 : print the no.Of paths whose sum is "K"   
    /* here even if k is  :    1=<K<=10^6     ( we have a catch here)
            it won't possible if sum is greater than 2000: bcoz
                    1<=N*M<=100
                    1<=B[i][j]<=10
                    
            to reach [N,M] from [1,1] we need to take (N-1)+(M-1) steps ( let say ( N+M)-steps ; (1-based))
                    in worst case if we take all steps in down direction or right direction
                    then max sum we can get is : 10 * (N+M) <= 10 * (100+100) = 1990 <= 2000
    
            Hence we can create dp of size dp[N][M][2001] to store no.Of paths whose sum is "K"
    
     */
    public static int countPathsWithSumK(int[][] grid, int K) {

        int n = grid.length;
        int m = grid[0].length;

        int[][][] dp = new int[n][m][2001];

        //base-case
        // dp[i][j][k] = how many paths exist which start from 1,1 and end at i,j whose sum is “k” 
        dp[0][0][grid[0][0]] = 1; // only one path to reach (0,0) with sum = grid[0][0]

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == 0 && j == 0)
                    continue; // case is already handled
                
                for (int k = 0; k <= K; k++) {
                    int weNeed = k - grid[i][j]; //   'k' is the sum we want at dp[i][j] , so to reach here we must have come from either top or left cell whose sum should be (k - grid[i][j])
                    
                    if (weNeed < 0)
                        continue; // because we can't take negative sum
                    //dp[i][j][k] = dp[i-1][j][K-b[i][j]] + dp[i][j-1][K-b[i][j]] 
                    dp[i][j][k] = (i>0 ? dp[i-1][j][weNeed] : 0) + (j>0 ? dp[i][j-1][weNeed] : 0);
                }
            }
        }
        
        return dp[n-1][m-1][K];
    }

    public static void main(String[] args) {
        int[][] grid = {
                { 2, 3, 4 },
                { 6, 5, 8 },
                { 1, 4, 3 }
        };

        System.out.println("Number of paths with even sum: " + countEvenSumPathsEasy(grid));


        System.out.println("-----------------------------------");

        int[][] grid2 = {
                { 1, 2, 3 },
                { 1, 1, 0 },
                { 2, 2, 9 }
        };
        int k = 15;
        System.out.println("Number of paths with sum " + k + ": " + countPathsWithSumK(grid2, k));


        System.out.println("-----------------------------------");

        

    }

}
