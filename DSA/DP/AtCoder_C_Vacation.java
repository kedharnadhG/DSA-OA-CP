package DSA.DP;

public class AtCoder_C_Vacation {
    
    // prob-link : https://atcoder.jp/contests/dp/tasks/dp_c
    // doc: https://docs.google.com/document/d/155E_2S7FabNMwPyQT4qcPJYug8gLOJsu37UsUNMrOCE/edit?tab=t.0

    /*Easy-Version
     link : https://ideone.com/kPNLiz
    */


    public static long maxSum(long[] a, long[] b, long[] c) {

        int n = a.length;

        long[] dpa = new long[n];
        long[] dpb = new long[n];
        long[] dpc = new long[n];

        dpa[0] = a[0];
        dpb[0] = b[0];
        dpc[0] = c[0];

        for (int i = 1; i < n; i++) {
            dpa[i] = a[i] + Math.max(dpb[i - 1], dpc[i - 1]);
            dpb[i] = b[i] + Math.max(dpa[i - 1], dpc[i - 1]);
            dpc[i] = c[i] + Math.max(dpa[i - 1], dpb[i - 1]);
        }

        return Math.max(dpa[n - 1], Math.max(dpb[n - 1], dpc[n - 1]));
    }


    public static long maxSumIf2ConsecutiveAllowed(long[] a, long[] b, long[] c) {
        int n = a.length;

        long[] dpa = new long[n];
        long[] dpb = new long[n];
        long[] dpc = new long[n];

        dpa[0] = a[0];
        dpb[0] = b[0];
        dpc[0] = c[0];

        if (n > 1) {

            dpa[1] = a[1] + Math.max(a[0], Math.max(b[0], c[0]));
            dpb[1] = b[1] + Math.max(b[0], Math.max(a[0], c[0]));
            dpc[1] = c[1] + Math.max(c[0], Math.max(a[0], b[0]));
        }

        for (int i = 2; i < n; i++) {
            dpa[i] = Math.max(
                    a[i] + Math.max(dpb[i - 1], dpc[i - 1]),
                    a[i] + a[i - 1] + Math.max(dpb[i - 2], dpc[i - 2]) // since, same-activity is allowed for 2-consecutive days
            );

            dpb[i] = Math.max(
                    b[i] + Math.max(dpa[i - 1], dpc[i - 1]),
                    b[i] + b[i - 1] + Math.max(dpa[i - 2], dpc[i - 2]) // since, same-activity is allowed for 2-consecutive days
            );

            dpc[i] = Math.max(
                    c[i] + Math.max(dpa[i - 1], dpb[i - 1]),
                    c[i] + c[i - 1] + Math.max(dpa[i - 2], dpb[i - 2]) // since, same-activity is allowed for 2-consecutive days
            );

        }

        return Math.max(dpa[n - 1], Math.max(dpb[n - 1], dpc[n - 1]));

    }
    
    // Harder-version is pending : refer-doc once

    public static void main(String[] args) {
        long[] a = { 10, 20, 30 };
        long[] b = { 40, 50, 60 };
        long[] c = { 70, 80, 90 };

        // can't do same-activity for 2-or-more consecutive days (i.e : can't do same activity on 2-consecutive days)
        System.out.println("The maximum sum is :-> " + maxSum(a, b, c));

        System.out.println("-----------------------------------------------------------------");

        // New-Q  : 2 consecutive are allowed;  3 consecutive are not allowed; 
        System.out.println("The maximum sum if 2 consecutive are allowed is :-> " + maxSumIf2ConsecutiveAllowed(a, b, c));
    }

}
