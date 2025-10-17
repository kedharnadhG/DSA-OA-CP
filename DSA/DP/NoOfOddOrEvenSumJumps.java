package DSA.DP;

import java.util.Scanner;

public class NoOfOddOrEvenSumJumps {
    


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();              // 5

        int[] a = new int[n+1];

        for (int i = 1; i <= n; i++) {
            a[i] = sc.nextInt();               // 2 3 5 8 10
        }

        int[][] dp = new int[10005][3];    // dp[i][2] :-> even sum , dp[i][1] :-> odd sum


        if (a[0] % 2 == 0) {
            dp[1][2] = 1;
        } 
        else {
            dp[1][1] = 1;
        }

        for (int i = 2; i <= n; i++) {
            
            if (a[i] % 2 == 0) {
                dp[i][1] = dp[i - 1][1] + dp[i-2][1];
                dp[i][2] = dp[i - 1][2] + dp[i-2][2];
            }
            else {
                dp[i][1] = dp[i - 1][2] + dp[i-2][2];
                dp[i][2] = dp[i - 1][1] + dp[i-2][1];
            }

        }


        System.out.println(" The number of odd or even sum jumps(1/2) are :-> " + dp[n][1] + " " + dp[n][2]);



        /*jumps (1/2)
                Evn sum journey 👍
                evn:
                    2...3...5...8...10
                    2...3...5.....10
        
                Odd sum journey : 
        
                    
                    odd:
                    2...5...10
                    2...3...8...10
                    2...5...8....10 
        
        */
        

        // Harder version :-> we can jump 1/2/3 steps at a time
         int[][] dp2 = new int[10005][3];    // dp[i][2] :-> even sum , dp[i][1] :-> odd sum


        if (a[0] % 2 == 0) {
            dp2[1][2] = 1;
        } 
        else {
            dp2[1][1] = 1;
        }

        if(a[2] % 2 == 0) {
            dp2[2][2] = 1;
        }
        else {
            dp2[2][1] = 1;
        }

        for (int i = 3; i <= n; i++) {
            
            if (a[i] % 2 == 0) {
                dp2[i][2] = dp2[i - 1][2] + dp2[i-2][2] + dp2[i-3][2];
                dp2[i][1] = dp2[i - 1][1] + dp2[i-2][1] + dp2[i-3][1];
            }
            else {
                dp2[i][1] = dp2[i - 1][2] + dp2[i-2][2] + dp2[i-3][2];
                dp2[i][2] = dp2[i - 1][1] + dp2[i-2][1] + dp2[i-3][1];
            }

        }


        System.out.println(" The number of odd or even sum jumps(1/2/3) are :-> " + dp2[n][1] + " " + dp2[n][2]);

    }
}
