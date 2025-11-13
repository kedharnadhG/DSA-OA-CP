package CP.CodeForces_Probs;


// link: https://codeforces.com/contest/2155/problem/C
// submission : https://codeforces.com/contest/2155/submission/342380926

// doc: https://docs.google.com/document/d/1KZfeVh3M8Jrg0XXCjsejCYYc5_GJRKdgMqP-04LZzQs/edit?tab=t.0

import java.util.Scanner;

public class C_The_Ancient_Wizards_Capes {

    public static final long MOD = 676767677L;

    public static long checkWithLandR(char first, long[] arr) {
        int n = arr.length;

        char[] cape = new char[n];
        cape[0] = first;

        char prev = first;

        // cape-direction
        for (int i = 1; i < n; i++) {
            if (arr[i] == arr[i - 1]) {
                //equal => alternative cape
                prev = (prev == 'L') ? 'R' : 'L';
            } else if (arr[i - 1] + 1 == arr[i]) {
                // both are 'L'
                if (prev != 'L')
                    return 0;
                prev = 'L';
            } else if (arr[i - 1] == arr[i] + 1) {
                // decreasing by 1  => both are 'R'
                if (prev != 'R')
                    return 0;
                prev = 'R';
            } else {
                // difference > 1 => invalid
            }

            cape[i] = prev;
        }

        // calculating l-count
        long[] leftLCount = new long[n];
        long leftLCnt = 0;
        for (int i = 0; i < n; i++) {
            leftLCount[i] = leftLCnt;
            if (cape[i] == 'L') {
                leftLCnt++;
            }
        }

        // calculating r-count
        long[] rightRCount = new long[n];
        long rightRCnt = 0;
        for (int i = n - 1; i >= 0; i--) {
            rightRCount[i] = rightRCnt;
            if (cape[i] == 'R') {
                rightRCnt++;
            }
        }

        // calculating answer
        for (int i = 0; i < n; i++) {

            // Each wizard can see:
            // - all 'L' capes to their left (leftLCount[i])
            // - all 'R' capes to their right (rightRCount[i])
            // - and themselves (+1)   :  "+1" to include the current wizard itself in the total visible count
            if (arr[i] != leftLCount[i] + rightRCount[i] + 1) {
                return 0;
            }
        }

        return 1;

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();

        while (t-- > 0) {
            int n = sc.nextInt();
            long[] arr = new long[n];

            for (int i = 0; i < n; i++)
                arr[i] = sc.nextInt();

            long ans = checkWithLandR('L', arr) + checkWithLandR('R', arr);

            System.out.println(ans % MOD);
        }
        
        sc.close();
    }
    
}
