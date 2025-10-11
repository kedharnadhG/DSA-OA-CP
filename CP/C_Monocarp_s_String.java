package CP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class C_Monocarp_s_String {

    // submission-link : https://codeforces.com/contest/2145/submission/342799126

     static class FastScanner {
        BufferedReader br;
        StringTokenizer st;
        FastScanner(InputStream in) { br = new BufferedReader(new InputStreamReader(in)); }
        String next() throws IOException {
            while (st == null || !st.hasMoreElements()) st = new StringTokenizer(br.readLine());
            return st.nextToken();
        }
        int nextInt() throws IOException { return Integer.parseInt(next()); }
    }

    public static void main(String[] args) throws IOException {
        FastScanner sc = new FastScanner(System.in);
        int t = sc.nextInt();

        while (t-- > 0) {
            int n = sc.nextInt();
            String s = sc.next();

            int ta = 0, tb = 0;
            for (int i = 0; i < n; i++) {
                if (s.charAt(i) == 'a')
                    ta++;
                else
                    tb++;
            }

            if (ta == tb) {
                System.out.println(0);
                continue;
            }

            int diff = ta - tb;
            Map<Integer, Integer> lastSeen = new HashMap<>();
            lastSeen.put(0, -1);
            int pr = 0;
            int ans = n;

            for (int i = 0; i < n; i++) {
                pr += (s.charAt(i) == 'a') ? 1 : -1;
                if (lastSeen.containsKey(pr - diff)) {
                    ans = Math.min(ans, i - lastSeen.get(pr - diff));
                }
                lastSeen.put(pr, i);
            }

            System.out.println(ans == n ? -1 : ans);

        }
    }
    
}
