package DSA.DP;

public class LargestValidSubStrPayPalOA {

    /* Valid SubString is a String :-> where adjacent-pairs of chars have diff <= k
      
    */
    
    public static void largestValidSubStr(String s, int k) {
        int n = s.length();

        int[] dp = new int[n];

        dp[0] = 1;

        int maxLen = 1;
        int maxIdx = 0;

        for (int i = 1; i < n; i++) {
            if (Math.abs(s.charAt(i) - s.charAt(i - 1)) <= k) {
                dp[i] = dp[i - 1] + 1;

            } else {
                dp[i] = 1;
            }

            if (dp[i] > maxLen) {
                maxLen = dp[i];
                maxIdx = i;
            }
        }

        int startIndex = maxIdx - maxLen + 1;
        System.out.println("The largest valid substring is :-> " + s.substring(startIndex, startIndex + maxLen));
        

    }

    public static void main(String[] args) {
        
        String s1 = "zebraa";
        int k1 = 1;

        largestValidSubStr(s1, k1);

        System.out.println("-----------------------------------------------------------------");
        
        String s2 = "ababbaca";
        int k2 = 1;
        largestValidSubStr(s2, k2);

    }
}
