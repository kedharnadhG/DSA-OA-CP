package INTERVIEW_PROBS;

import java.util.*;

public class MinMovesToVisitSet {

    // for follow-up
    static List<List<Integer>> graph;
    static int[] parent;
    static int[] depth;
    static boolean[] visited;

    static void dfs(int node, int par, int d) {
        visited[node] = true;
        parent[node] = par;
        depth[node] = d;

        for (int nei : graph.get(node)) {
            if (!visited[nei]) {
                dfs(nei, node, d + 1);
            }
        }
    }


    static int getMinMoves(int startNode, int[] targets, int[][] edges, int n) {

        // STEP 1: Build adjacency list
        // -----------------------------
        graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int[] e : edges) {
            graph.get(e[0]).add(e[1]);
            graph.get(e[1]).add(e[0]);
        }

        // -----------------------------
        // STEP 2: DFS from root = 1
        // to build parent[] and depth[]
        // -----------------------------
        parent = new int[n + 1];
        depth = new int[n + 1];
        visited = new boolean[n + 1];

        dfs(1, 0, 0);

        // -----------------------------
        // STEP 3: Collect minimal subtree
        // -----------------------------
        Set<Integer> usedNodes = new HashSet<>();
        usedNodes.add(1);

        for (int t : targets) {
            int curr = t;
            while (curr != 0) {
                usedNodes.add(curr);
                curr = parent[curr];
            }
        }

        // -----------------------------
        // STEP 4: Include startNode
        // -----------------------------
        if (startNode != 1) {
            int curr = startNode;
            while (curr != 0) {
                usedNodes.add(curr);
                curr = parent[curr];
            }
        }

        // -----------------------------
        // STEP 5: Base cost
        // -----------------------------
        int edgesInSubtree = usedNodes.size() - 1;
        int cost = 2 * edgesInSubtree;

        // -----------------------------
        // STEP 6: Save return path
        // -----------------------------
        if (startNode != 1) {
            cost -= depth[startNode];
        }

        return cost;


    }


    
    public static void main(String[] args) {
        
        int n = 8;

        int[] targets = { 5, 6 };

        int[][] edges = {
                { 1, 2 }, { 2, 5 }, { 2, 3 }, { 2, 6 },
                    {1,4}, {4,7},{7,8}
        };


        // Adjacency-list
        List<List<Integer>> graph = new ArrayList<>();
        for(int i = 0; i<=n; i++)
        {
            graph.add(new ArrayList<>());
        }

        for(int[] e:edges)
        {
            graph.get(e[0]).add(e[1]);
            graph.get(e[1]).add(e[0]);
        }
        
        // parent[x]   - parent array
        int[] parent = new int[n + 1];
        Arrays.fill(parent,-1);

        Queue<Integer> q = new LinkedList<>();
        q.add(1);

        parent[1]=0; // root parent is '0'
        
        while(!q.isEmpty())
        {
            int u = q.poll();

            for (int v : graph.get(u)) {
                if (parent[v] == -1) {
                    parent[v] = u;
                    q.add(v);
                }
            }
        }

        

        // getting the path for each target ( bottom-up)  ; ( no.of edges is nothing but ( no.of nodes participated in path -1 ) )
        Set<Integer> usedNodes = new HashSet<>();
        usedNodes.add(1);

        for (int t : targets) {
            int curr = t;
            while (curr != 0) {
                usedNodes.add(curr);
                curr = parent[curr];
            }
        }

        // Result = 2 * (edges in minimal subtree)
        // edges = nodes - 1

        int result = 2 * (usedNodes.size() - 1);
        System.out.println(" Min.No.Of moves to reach every node in the set from 1 is : " + result);

        System.out.println("");
        


        // FollowUp- we can start from ( 1, N)
        System.out.println("----------  FollowUp- we can start from ( 1, N)  ---------");
        int N = 8;
        System.out.println(" Min.No.Of moves to reach every node in the set from 1 or " + N + " is : " + Math.min(getMinMoves(1, targets, edges, n), getMinMoves(N, targets, edges, n)) );


    }


}





/*    PROBLEM SUMMARY:
    - Tree with N nodes (root = 1)
    - Must visit all nodes in set S at least once
    - Must return back to node 1
    - Each move is to an adjacent node
    - Find minimum number of moves

    KEY OBSERVATIONS:
    1. Tree ⇒ only ONE unique path between any two nodes
    2. We only need to traverse the UNION of paths:
        (1 → S1), (1 → S2), ..., (1 → Sk)
    3. This forms the minimal subtree required for traversal

    IMPORTANT LOGIC:
    - Let K = number of nodes in this minimal subtree
    - Number of edges = K - 1
    - Each edge must be traversed twice (down + up)

    FINAL ANSWER:
        moves = 2 × (K - 1)

    IMPLEMENTATION STRATEGY:
    1. Build adjacency list
    2. BFS from root (1) to compute parent[]
    3. For each target in S:
        - Walk up to root using parent[]
        - Mark all visited nodes in a Set
    4. Return:
        2 × (size_of_set - 1)

    TIME COMPLEXITY:
    - BFS: O(N)
    - Path marking: O(N) worst case
    - Overall: O(N)

    SPACE COMPLEXITY:
    - O(N) for graph, parent[], and Set

    INTERVIEW INSIGHT:
    This is a classic "prune the tree + double edges" problem.
    Used frequently by Google to test tree reasoning + optimization.
    ==========================================================
*/



/* ================== FOLLOWUP - EXPLANATION ==================

        1) If you start AND end at the same node:
        - Every edge is walked twice
        - cost = 2 × edges

        2) If you start at node N:
        - You do NOT need to return to N
        - The path from N → rest of the tree
            is walked only once

        3) That path length = distance(N → root)

        4) Hence we subtract it:
        cost = 2 × edges − distance(N → root)

        WHY THIS WORKS:
        - Tree has no cycles
        - Unique path between nodes
        - Removing a return path saves exactly its length

        IMPORTANT:
        - We include N in targets to ensure
        the subtree includes that branch
        - Ending node can be ANYWHERE
        - Subtraction represents the path we never walk back

        ONE LINE MEMORY:
        "Start at the farthest node → don't walk back from it"
        ========================================================
*/
