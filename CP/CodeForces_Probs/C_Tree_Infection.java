package CP.CodeForces_Probs;

import java.util.*;

public class C_Tree_Infection {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();

        while (t-- > 0) {
            int n = sc.nextInt();

            int[] childCnt = new int[n + 1];

            // Read parents and count children
            for (int i = 2; i <= n; i++) {
                int p = sc.nextInt();
                childCnt[p]++;
            }

            // Max-heaps
            PriorityQueue<Integer> left = new PriorityQueue<>(Collections.reverseOrder());
            PriorityQueue<Integer> right = new PriorityQueue<>(Collections.reverseOrder());

            // 🔥 IMPORTANT FIX:
            // Add a dummy group of size 1 to represent the FIRST infection
            left.add(1);

            // Build left list (groups formed by children)
            for (int i = 1; i <= n; i++) {
                if (childCnt[i] > 0) {
                    left.add(childCnt[i]);
                }
            }

            int time = 0;

            while (!left.isEmpty() || !right.isEmpty()) {
                time++;

                // 1) spreading
                List<Integer> temp = new ArrayList<>();
                while (!right.isEmpty()) {
                    int x = right.poll() - 1;
                    if (x > 0) temp.add(x);
                }
                for (int x : temp) right.add(x);

                // 2) injection
                if (!left.isEmpty()) {
                    int x = left.poll() - 1;
                    if (x > 0) right.add(x);
                } else if (!right.isEmpty()) {
                    int x = right.poll() - 1;
                    if (x > 0) right.add(x);
                }
            }

            System.out.println(time);
        }
        sc.close();
    }
}









/*  C. TREE INFECTION — PRIORITY QUEUE SIMULATION (CORRECTED)
    ===========================================================

    IMPORTANT CORRECTION
    --------------------
    In simulation-based solutions, we MUST explicitly model
    the VERY FIRST infection.

    Initially:
    - NO node is infected
    - The first infection itself costs ONE FULL SECOND

    To model this correctly, we add a DUMMY GROUP of size 1.


    WHY ADD `left.add(1)`?
    ----------------------
    This dummy group represents:
    - The first injection that starts the entire infection process

    Without it:
    - Simulation assumes infection already exists
    - Time becomes off by exactly 1
    - Output becomes smaller than expected


    PROBLEM RESTATEMENT
    -------------------
    We are given a rooted tree (root = 1).
    Initially, all nodes are healthy.

    Each second:
    1) Spreading:
    - If a parent has at least one infected child,
        it can infect ONE more child.
    2) Injection:
    - Infect ANY healthy node.

    Goal:
    Infect all nodes in minimum time.


    GROUP OBSERVATION
    -----------------
    Children of the same parent form ONE group.

    For a parent with k children:
    - 1 injection starts the group
    - (k - 1) spreading steps finish it


    LEFT / RIGHT MODEL
    ------------------
    LEFT  = groups not yet started
    RIGHT = groups already started and spreading

    We use max-heaps to always prioritize largest groups.


    SIMULATION LOGIC
    ----------------
    Each loop iteration = 1 second.

    Inside each second:
    1) Spreading:
    - All RIGHT groups reduce by 1
    2) Injection:
    - If LEFT not empty:
        start largest LEFT group
    - Else:
        help largest RIGHT group


    WHY THIS IS NOW CORRECT
    ----------------------
    - Dummy group ensures first infection costs time
    - time++ correctly matches problem seconds
    - Simulation exactly follows problem rules


    COMPLEXITY
    ----------
    Time:  O(N log N)
    Space: O(N)


    KEY TAKEAWAY
    ------------
    Simulation MUST pay for the first infection.
    Missing that is the most common bug in this problem.

    ===========================================================
*/
