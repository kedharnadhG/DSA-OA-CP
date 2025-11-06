package INTERVIEW_PROBS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ValidShortSubStrToRemoveToGetFreqOfABCisEq {
    
    // Given String consists of A, B and C, find the shortest substring to remove to make frequency of A, B and C equal.

    public static void main(String[] args) throws Exception {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());  // 5

        String s = br.readLine().trim();  // abccb

        long p1[] = new long[n];
        long p2[] = new long[n];

        long a = 0, b = 0, c = 0;

        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == 'a')
                a++;
            else if (s.charAt(i) == 'b')
                b++;
            else
                c++;

            p1[i] = a - b;
            p2[i] = b - c;
        }
        
        long x = p1[n - 1];
        long y = p2[n - 1];

        Map<String, Integer> map = new HashMap<>();
        map.put("0#0", -1);

        long len = Long.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            long key1 = p1[i] - x;
            long key2 = p2[i] - y;

            String shiftedKey = key1 + "#" + key2;

            if (map.containsKey(shiftedKey)) {
                len = Math.min(len, i - map.get(shiftedKey));
            }

            String originalKey = p1[i] + "#" + p2[i];
            map.put(originalKey, i);

        }

        System.out.println("The shortest substring to remove to make frequency of A, B and C equal is :-> " + len);

    }
}
