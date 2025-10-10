package DSA.HASHING;

import java.util.Arrays;

public class PrefixCommonArrOfTwoArrs {

    public static int[] prefixCommon(int[] A, int[] B) {
        
        int n = A.length;
        int cnt = 0;
        int[] ans  = new int[n];
        boolean[] visited = new boolean[n + 1];

        for (int i = 0; i < n; i++) {

            // mark A[i] as visited
            if (visited[A[i]])
                cnt++;
            visited[A[i]] = true;

            // mark B[i] as visited
            if (visited[B[i]])
                cnt++;
            visited[B[i]] = true;

            ans[i] = cnt;
        }

        return ans;

    }
    

    public static void main(String[] args) {
        // int[] A = { 2, 3, 1 };
        // int[] B = { 3, 1, 2 };
        int[] A = { 1, 3, 2, 4 };
        int[] B = { 3, 1, 2, 4 };

        int[] ans = prefixCommon(A, B);

        System.out.println(Arrays.toString(ans));
    }
}
