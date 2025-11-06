package INTERVIEW_PROBS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class SmallestSubArrFrqOfABCisEq {

    // prob-stmnt: find the smallest subarray of the given string s where the frequency of a, b and c are equal

    public static void main(String[] args) throws Exception  {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine().trim());       // 8    
        String s = br.readLine().trim();        // abbcabbc

        long a = 0, b = 0, c = 0;

        long[] p1 = new long[n];
        long[] p2 = new long[n];

        for (int i = 0; i < n; i++) {

            if (s.charAt(i) == 'a') {
                a++;
            } else if (s.charAt(i) == 'b') {
                b++;
            } else {
                c++;
            }

            p1[i] = a - b;
            p2[i] = b - c;
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("0#0", -1);        // if you used 1-based-indexing, then this can be  => map.put("0#0", 0);
        long len = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            String key = p1[i] + "#" + p2[i];

            if (map.containsKey(key)) {
                int prevIdx = map.get(key);

                len = Math.min(len, i - prevIdx);
            }

            map.put(key, i);
        }

        long ans = len == Integer.MAX_VALUE ? -1 : len;

        System.out.println(" The length of the smallest subarray is where the frequency of a, b and c are equal is: " + ans);

    }
    
}
