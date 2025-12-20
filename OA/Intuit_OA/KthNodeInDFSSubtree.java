package OA.Intuit_OA;

import java.util.*;

public class KthNodeInDFSSubtree {
    static List<Integer>[] tree;
    static List<Integer> dfsOrder;
    
    // dfs-traversal to get preorder of subtree
    static void dfs(int node) {
        dfsOrder.add(node);
        for (int child : tree[node]) {
            dfs(child);
        }
    }

    // Brute-force approach: DFS for each query : TC: O( NlogN + Q*N ) , SC: O(N)
    static List<Integer> kThNodeUsingDFSBForce(int n, int[] parent, int q, int[][] queries) {

        List<Integer> results = new ArrayList<>();

        // adjacency list
        tree = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            tree[i] = new ArrayList<>();
        }

        // build tree from parent array
        for (int i = 1; i <= n; i++) {
            if (parent[i] != -1) {
                tree[parent[i]].add(i); // 'i' is child of 'parent[i]'
            }
        }

        // sort children for smallest-first DFS
        for (int i = 1; i <= n; i++) {
            Collections.sort(tree[i]);
        }

        for (int[] query : queries) {
            int node = query[0];
            int k = query[1];

            dfsOrder = new ArrayList<>();
            dfs(node); // DFS from 'node'

            if (dfsOrder.size() >= k) {
                results.add(dfsOrder.get(k - 1));
            } else {
                results.add(-1);
            }

        }

        return results;

    }

    




    /*           Optimized Approach:   Eulet-Tour + Flattening of Tree
    */
    
    static List<Integer> flat; // flattened DFS order of entire tree
    static int[] start; // start index of each node's subtree in 'flat'
    static int[] subtreeSize; // size of each node's subtree
    static int timer;

    static void EulerDFS(int node) {
        start[node] = timer++;
        flat.add(node);

        // int size = 1; // count self

        for (int child : tree[node]) {
            EulerDFS(child);

            // size += subtreeSize[child];    // add child's subtree size
        }
        subtreeSize[node] = timer - start[node]; // subtree size of 'node'
        
        // subtreeSize[node] = size; // alternatively,
    }
    
    static List<Integer> kThNodeUsingDFSOptimized(int n, int[] parent, int q, int[][] queries) {
        
        List<Integer> results = new ArrayList<>();

        // adjacency list
        tree = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            tree[i] = new ArrayList<>();
        }

        // build tree from parent array
        for (int i = 1; i <= n; i++) {
            if (parent[i] != -1) {
                tree[parent[i]].add(i); // 'i' is child of 'parent[i]'
            }
        }

        // sort children for smallest-first DFS
        for (int i = 1; i <= n; i++) {
            Collections.sort(tree[i]);
        }

        // Euler Tour DFS to flatten tree
        flat = new ArrayList<>();
        start = new int[n + 1];
        subtreeSize = new int[n + 1];
        timer = 0;

        EulerDFS(1); // DFS from root

        for (int[] query : queries) {
            int node = query[0];
            int k = query[1];

            if (subtreeSize[node] >= k) {
                results.add(flat.get(start[node] + k - 1));
            } else {
                results.add(-1);
            }

        }

        return results;

    }


    public static void main(String[] args) {
        int n = 9;
        int[] parent = { -1, -1, 1, 1, 1, 3, 5, 3, 5, 7 }; // 1-indexed     ; parent[0] unused ; parent[1] = -1 (root); parent[i] = parent of node i
        
        int q = 4;
        int[][] queries = {
                { 1, 5 }, { 7, 2 }, { 9, 2 }, { 3, 6 }
        };
    
        System.out.println("K-th Node in DFS Subtree Queries Results:");
        kThNodeUsingDFSBForce(n, parent, q, queries).forEach(res -> System.out.println(res));
    
        

        System.out.println(" Optimized Approach Results: ");
        kThNodeUsingDFSOptimized(n, parent, q, queries).forEach(res -> System.out.println(res));

    
    }
    



}







/**             PROBLEM: K-th Node in DFS Traversal ( Visit smallest first ) of a Subtree 
        ============================================================

        PROBLEM STATEMENT
        ------------------------------------------------------------
        You are given a tree with N nodes.

        - Node 1 is the root.
        - For each node i (1 ≤ i ≤ N), the parent of i is given.
        Parent of root is -1.
        - Children of each node must be visited in increasing order.

        You are given Q queries.
        Each query contains:
            (node, k)

        For each query:
        - Perform a DFS traversal starting from 'node'
        - Return the k-th node visited in DFS order
        - If fewer than k nodes exist, return -1


        ============================================================
        KEY OBSERVATIONS
        ------------------------------------------------------------
        1. DFS traversal of a node visits ONLY its subtree.
        2. Preorder DFS naturally gives the visit order.
        3. Child order matters → children must be sorted.
        4. Each query is independent.


        ============================================================
        APPROACH
        ------------------------------------------------------------
        1. Build adjacency list from the parent array.
        2. Sort children of every node (to ensure smallest-first DFS).
        3. For each query:
        a. Start DFS from the given node.
        b. Store traversal order in a list.
        c. If list size ≥ k → answer = list[k-1]
            else → answer = -1


        ============================================================
        DFS LOGIC
        ------------------------------------------------------------
        DFS(node):
            add node to traversal list
            for each child of node (in sorted order):
                DFS(child)

        This is PREORDER DFS.


        ============================================================
        TIME & SPACE COMPLEXITY
        ------------------------------------------------------------
        - Building tree        : O(N)
        - Sorting children     : O(N log N)
        - DFS per query        : O(size of subtree), worst O(N)
        - Total complexity     : O(N log N + Q × N)

        Space:
        - Adjacency list       : O(N)
        - DFS recursion + list : O(N)


        ============================================================
        IMPORTANT JAVA CONFUSION:
        List<Integer>[]  vs  List<List<Integer>>
        ============================================================

        Both are used to represent an ADJACENCY LIST.
        They are LOGICALLY the same, but STRUCTURALLY different.

        ------------------------------------------------------------
        1) List<Integer>[] tree
        ------------------------------------------------------------

        Example:
            List<Integer>[] tree = new ArrayList[n + 1];

        What it is:
        - An ARRAY where each index stores a List<Integer>
        - Array size is fixed
        - Very fast access using tree[i]

        Pros:
        - Slightly faster (array indexing)
        - Common in competitive programming

        Cons:
        - Not type-safe (unchecked warnings)
        - Java does NOT allow new List<Integer>[n]
        - Less clean for interviews / production

        Use this when:
        - Competitive programming
        - LeetCode / Codeforces
        - Performance matters more than style


        ------------------------------------------------------------
        2) List<List<Integer>> tree
        ------------------------------------------------------------

        Example:
            List<List<Integer>> tree = new ArrayList<>();

        What it is:
        - A List that stores other Lists
        - Fully dynamic
        - Fully type-safe

        Pros:
        - Clean
        - Type-safe
        - Preferred in interviews and production code

        Cons:
        - Slightly slower due to method calls (negligible)

        Use this when:
        - Interviews (Google / Intuit / Amazon)
        - Clean Java code
        - Real-world applications


        ------------------------------------------------------------
        IS EITHER A "2D ARRAY"?
        ------------------------------------------------------------

        NO.

        Neither is a real 2D array like int[][].

        They are:
        - Adjacency lists
        - A collection of lists indexed by node number

        People say "2D" only informally.


        ============================================================
        WHEN TO USE WHAT (IMPORTANT)
        ------------------------------------------------------------

        Context                  →  Use
        ---------------------------------------------
        Competitive Programming  →  List<Integer>[]
        Interviews               →  List<List<Integer>>
        Production Java          →  List<List<Integer>>
        Learning / Practice      →  List<List<Integer>>


        ============================================================
        INTERVIEW ONE-LINERS (VERY IMPORTANT)
        ------------------------------------------------------------

        - "DFS preorder traversal gives subtree visit order."
        - "Sorting children ensures deterministic traversal."
        - "Adjacency list is implemented using a list of lists."
        - "Array of lists is faster but less type-safe."


        ============================================================
        FINAL TAKEAWAY
        ------------------------------------------------------------
        Both structures do the SAME JOB.
        Difference is not algorithmic, but STRUCTURAL and STYLISTIC.

        Rule of Thumb:
        - Want speed → Array of Lists
        - Want clarity → List of Lists

        ============================================================
*/



/*                      FINAL TAKE-AWAY NOTES ( OPTIMIZED APPROACH )
        Topic: K-th Node in DFS Subtree (Optimized using Euler Tour)
        ====================================================================

        PROBLEM STATEMENT
        --------------------------------------------------------------------
        You are given a TREE with N nodes.
        - Node 1 is the root.
        - For each node i, parent[i] is given.
        - Children must be visited in increasing order.

        You are given Q queries.
        Each query is of the form:
            (node, k)

        For each query:
        - Perform a DFS traversal starting from 'node'
        - Return the k-th node visited in this DFS order
        - If the subtree has fewer than k nodes, return -1


        ====================================================================
        BRUTE FORCE APPROACH (WHY IT FAILS)
        --------------------------------------------------------------------
        For every query:
        1. Run DFS from the given node
        2. Store DFS traversal order
        3. Return k-th element

        Time Complexity:
        - DFS per query = O(N)
        - Total = O(N × Q)

        This is too slow for large inputs.


        ====================================================================
        KEY OBSERVATION (MOST IMPORTANT)
        --------------------------------------------------------------------
        In a PREORDER DFS traversal of a tree:

        👉 All nodes belonging to a subtree appear CONTIGUOUSLY
        in the DFS order.

        This single observation enables the optimization.


        ====================================================================
        OPTIMIZATION TECHNIQUE
        Euler Tour + Tree Flattening
        --------------------------------------------------------------------
        Instead of running DFS for every query:

        1. Run DFS ONCE from the root (node 1)
        2. While doing DFS:
        - Store the traversal in a list (flattened tree)
        - Record where each node's subtree starts
        - Record how many nodes are in each subtree

        This converts subtree queries into simple array indexing.


        ====================================================================
        DATA STRUCTURES USED
        --------------------------------------------------------------------
        flat[] :
        - Stores DFS preorder traversal of the entire tree

        startIndex[node] :
        - Index in flat[] where node's subtree starts

        subtreeSize[node] :
        - Number of nodes in subtree rooted at node

        timer :
        - Just an INDEX counter for flat[]
        - NOT actual time, NOT clock
        - Equivalent to flat.size() before insertion


        ====================================================================
        DFS LOGIC (STEP-BY-STEP)
        --------------------------------------------------------------------
        dfs(node):

        1. startIndex[node] = timer
        (Remember where this node appears in DFS order)

        2. flat.add(node)
        (Store node in flattened array)

        3. timer++
        (Move to next index)

        4. size = 1
        (Count the node itself)

        5. For each child of node:
            dfs(child)
            size += subtreeSize[child]

        6. subtreeSize[node] = size
        (Total nodes in this subtree)


        ====================================================================
        WHY SUBTREE IS CONTIGUOUS IN flat[]
        --------------------------------------------------------------------
        DFS preorder always does:
        - Visit node
        - Fully visit first child subtree
        - Fully visit next child subtree
        - Then return

        So DFS order looks like:
        [node, child1_subtree..., child2_subtree..., ...]

        Hence:
        Subtree(node) =
        flat[startIndex[node] ... startIndex[node] + subtreeSize[node] - 1]


        ====================================================================
        ANSWERING A QUERY (node, k)
        --------------------------------------------------------------------
        If k > subtreeSize[node]:
            return -1
        Else:
            answer index = startIndex[node] + k - 1
            return flat[answer index]

        Each query is answered in O(1) time.


        ====================================================================
        TIME COMPLEXITY
        --------------------------------------------------------------------
        Build adjacency list      : O(N)
        Sort children             : O(N log N)
        DFS (Euler Tour)           : O(N)
        Each query                 : O(1)

        TOTAL:
        O(N log N + Q)


        ====================================================================
        SPACE COMPLEXITY
        --------------------------------------------------------------------
        Adjacency list             : O(N)
        flat[]                     : O(N)
        startIndex[], subtreeSize[]: O(N)

        TOTAL:
        O(N)


        ====================================================================
        MENTOR CODE vs ARRAY-BASED CODE (IMPORTANT CLARITY)
        --------------------------------------------------------------------
        Mentor used:
        - HashMap pos        → subtree start index
        - ArrayList st       → flattened DFS order
        - int[] dp           → subtree size

        Converted version uses:
        - int[] startIndex
        - List<Integer> flat
        - int[] subtreeSize

        ALGORITHM IS IDENTICAL.
        Only data structures differ.

        Use HashMap when:
        - Node labels are arbitrary

        Use arrays when:
        - Nodes are numbered 1..N (faster, cleaner)


        ====================================================================
        WHAT timer REALLY IS (COMMON CONFUSION)
        --------------------------------------------------------------------
        timer is NOT time.
        timer is NOT clock.

        timer is simply:
        👉 the index at which the next node is placed in flat[]

        Equivalent code:
        startIndex[node] = flat.size();
        flat.add(node);


        ====================================================================
        INTERVIEW ONE-LINER (MEMORIZE THIS)
        --------------------------------------------------------------------
        "I flatten the tree using DFS preorder so each subtree becomes
        a contiguous segment, allowing O(1) k-th node queries."


        ====================================================================
        FINAL GOLDEN RULES
        --------------------------------------------------------------------
        1) DFS preorder makes subtree nodes contiguous
        2) Euler Tour = flattening the tree
        3) timer is just an array index
        4) subtree queries → range queries
        5) One DFS + O(1) per query is optimal

        ====================================================================
        END OF NOTES
        ====================================================================
*/
