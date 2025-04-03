import java.util.*;
import java.util.LinkedList; // Explicit import (though covered by java.util.*)

/**
 * NeetCodeStandardUtils (v9 - Strict Reusability): A utility class providing
 * fundamental, highly reusable data structures, algorithms, and core patterns
 * frequently needed for competitive programming and interview preparation
 * (e.g., NeetCode 150).
 *
 * Version 9 applies stricter reusability criteria: Methods primarily solving only
 * one specific problem (or very close variants) in common lists like NeetCode 150
 * have been removed (e.g., specific serialization, KMP, isSubtree). The focus is
 * on utilities applicable across multiple *different* problem types or representing
 * fundamental algorithmic/data structure patterns.
 * Adheres to standard Java syntax, avoiding Streams and Lambda Expressions.
 */
public class NeetCodeProUtils {

    // =========================================================================
    // 0. Core Data Structure Definitions & Helpers
    // =========================================================================

    /** Node for Binary Trees. */
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode() {}
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /** Node for Singly or Doubly Linked Lists. */
    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode prev; // Optional: For Doubly Linked List needs

        public ListNode() {}
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
        public ListNode(int val, ListNode prev, ListNode next) {
             this.val = val;
             this.prev = prev;
             this.next = next;
        }
    }

    /** Basic Generic Pair class, useful for graph algorithms, heaps etc. */
    public static class Pair<K, V> {
        public K key;
        public V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        public K getKey() { return key; }
        public V getValue() { return value; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(key, pair.key) && Objects.equals(value, pair.value);
        }
        @Override
        public int hashCode() { return Objects.hash(key, value); }
    }

    /** Edge class for weighted graphs, useful for MST algorithms. */
    public static class Edge implements Comparable<Edge> {
        public int u, v, weight;

        public Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
        @Override
        public String toString() { return "(" + u + "-" + v + ", w=" + weight + ")"; }
    }


    // --- Static Comparators ---
    /** Comparator for int[] intervals by start time. */
    public static final Comparator<int[]> INTERVAL_START_COMPARATOR_ARRAY = new Comparator<int[]>() {
         @Override
         public int compare(int[] a, int[] b) {
             // Handle null checks if necessary, depending on expected input guarantees
             return Integer.compare(a[0], b[0]);
         }
    };

    /** Comparator for Pairs where Key is Integer (used in Dijkstra/Prim). */
     public static final Comparator<Pair<Integer, Integer>> PAIR_INTEGER_KEY_COMPARATOR = new Comparator<Pair<Integer, Integer>>() {
         @Override
         public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
             // Handle null checks if necessary
             return Integer.compare(p1.getKey(), p2.getKey()); // Compares by the Integer key (distance/weight)
         }
     };


    // =========================================================================
    // 1. Fundamental Data Structure Implementations
    // =========================================================================

    /**
     * Union-Find (Disjoint Set Union) data structure implementation.
     * Highly reusable for connectivity, cycle detection (undirected), and grouping problems.
     * Supports path compression and union by rank/size for efficiency.
     */
    public static class UnionFind {
        private int[] parent;
        private int[] rank; // Could also use size
        private int count; // Number of disjoint sets

        /** Initializes UnionFind for 'size' elements (0 to size-1). */
        public UnionFind(int size) {
            if (size < 0) throw new IllegalArgumentException("Size cannot be negative");
            parent = new int[size];
            rank = new int[size];
            count = size;
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0; // Initialize rank to 0
            }
        }

        /** Finds the representative (root) of the set containing x with path compression. */
        public int find(int x) {
            if (x < 0 || x >= parent.length) throw new IllegalArgumentException("Element out of bounds");
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        /** Merges the sets containing x and y using union by rank. Returns true if merged, false if already connected. */
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                // Union by rank
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++; // Increment rank only when ranks are equal
                }
                count--; // Decrement the number of disjoint sets
                return true; // Union performed
            }
            return false; // Already in the same set
        }

        /** Returns the number of disjoint sets. */
        public int getCount() { return count; }
        /** Checks if x and y are in the same set. */
        public boolean isConnected(int x, int y) { return find(x) == find(y); }
    }

    /**
     * Basic Trie (Prefix Tree) implementation.
     * Reusable for prefix searching, autocomplete, and related string/dictionary problems.
     */
    public static class Trie {
        private static class TrieNode {
            Map<Character, TrieNode> children = new HashMap<>();
            boolean isWord = false;
            // Could add word count, frequency, etc. if needed by multiple problems
        }
        private final TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        /** Inserts a word into the Trie. */
        public void insert(String word) {
            if (word == null) return;
            TrieNode node = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                // node.children.computeIfAbsent(c, k -> new TrieNode()); // Java 8 way
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
            }
            node.isWord = true;
        }

        /** Searches for a complete word in the Trie. */
        public boolean search(String word) {
            TrieNode node = findNode(word);
            return node != null && node.isWord;
        }

        /** Checks if any word in the Trie starts with the given prefix. */
        public boolean startsWith(String prefix) {
            return findNode(prefix) != null;
        }

        /** Helper to find the node corresponding to the end of a string (word or prefix). */
        private TrieNode findNode(String str) {
             if (str == null) return null;
            TrieNode node = root;
            for (int i = 0; i < str.length(); i++) {
                 char c = str.charAt(i);
                 if (!node.children.containsKey(c)) {
                     return null;
                 }
                 node = node.children.get(c);
            }
            return node;
        }

        /** Searches for a word, allowing '.' as a wildcard character. */
        public boolean searchWithWildcard(String word) {
            if (word == null) return false;
            return searchRecursiveWildcardHelper(word, 0, root);
        }
        private boolean searchRecursiveWildcardHelper(String word, int index, TrieNode node) {
            if (node == null) return false;
            if (index == word.length()) return node.isWord; // Match end of word

            char c = word.charAt(index);

            if (c == '.') { // Wildcard
                // Try all possible children
                for (TrieNode child : node.children.values()) {
                    if (searchRecursiveWildcardHelper(word, index + 1, child)) {
                        return true; // Found a match down one path
                    }
                }
                return false; // Wildcard path failed
            } else { // Specific character
                TrieNode nextNode = node.children.get(c);
                if (nextNode == null) return false; // Character not found
                return searchRecursiveWildcardHelper(word, index + 1, nextNode);
            }
        }
    }

    // =========================================================================
    // 2. Fundamental Algorithms & Operations
    // =========================================================================

    // --- Array & Math Utilities ---

    /** Creates a frequency map for integers in an array. Highly reusable helper. */
    public static Map<Integer, Integer> getFrequencyMapInt(int[] nums) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        if (nums == null) return freqMap;
        for (int num : nums) { // Enhanced for loop
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        return freqMap;
    }

    /** Creates a frequency map for characters in a string. Highly reusable helper. */
    public static Map<Character, Integer> getFrequencyMapChar(String str) {
        Map<Character, Integer> freqMap = new HashMap<>();
        if (str == null) return freqMap;
        for (int i = 0; i < str.length(); i++) {
             char c = str.charAt(i);
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        return freqMap;
    }

    /** Calculates Greatest Common Divisor (GCD) using Euclidean algorithm. Reusable in math problems. */
    public static int gcd(int a, int b) {
        // Ensure inputs are handled if potentially large (use long if necessary)
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a; // a holds the GCD
    }


    // --- Two Pointer Patterns ---

     /** Helper to check if a substring is a palindrome (case-sensitive). Reusable for palindrome problems. */
     public static boolean isPalindromeHelper(String s, int left, int right) {
         if (s == null) return false; // Define behavior for null string
         // Consider handling non-alphanumeric characters based on problem requirements
         while (left < right) {
             if (s.charAt(left) != s.charAt(right)) {
                 return false;
             }
             left++;
             right--;
         }
         return true;
     }

    // --- Binary Search ---
    /** Standard Binary Search for target in a sorted array. Returns index or -1 if not found. */
    public static int binarySearch(int[] sortedNums, int target) {
        if (sortedNums == null) return -1;
        int left = 0;
        int right = sortedNums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2; // Avoid potential overflow
            if (sortedNums[mid] == target) {
                return mid;
            } else if (sortedNums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // Target not found
    }

    /** Functional interface for the check method used in Binary Search on Answer patterns. */
    public interface CanAchieveChecker {
        /** Checks if a given 'value' satisfies the problem's condition. Params allow passing extra context. */
        boolean check(int value, Object... params);
    }
    /**
     * Template for Binary Search on the Answer Space. Finds the minimum value >= low
     * that satisfies the checker function. Highly reusable pattern for optimization problems.
     * Returns -1 or an indicator value if no value satisfies the condition.
     */
    public static int binarySearchOnAnswerMin(int low, int high, CanAchieveChecker checker, Object... params) {
         int ans = -1; // Or potentially high + 1 depending on problem needs
         while (low <= high) {
             int mid = low + (high - low) / 2;
             if (checker.check(mid, params)) { // If mid satisfies the condition...
                 ans = mid;      // Store it as a potential best answer
                 high = mid - 1; // Try to find an even smaller valid answer
             } else {
                 low = mid + 1;   // Mid doesn't work, need a larger value
             }
         }
         return ans; // Return the best (minimum) valid answer found
    }
    /**
     * Template for Binary Search on the Answer Space. Finds the maximum value <= high
     * that satisfies the checker function. Highly reusable pattern for optimization problems.
     * Returns -1 or an indicator value if no value satisfies the condition.
     */
    public static int binarySearchOnAnswerMax(int low, int high, CanAchieveChecker checker, Object... params) {
         int ans = -1; // Or potentially low - 1 depending on problem needs
         while (low <= high) {
             int mid = low + (high - low) / 2;
             if (checker.check(mid, params)) { // If mid satisfies the condition...
                 ans = mid;     // Store it as a potential best answer
                 low = mid + 1; // Try to find an even larger valid answer
             } else {
                 high = mid - 1; // Mid doesn't work, need a smaller value
             }
         }
         return ans; // Return the best (maximum) valid answer found
    }


    // --- Linked List Operations --- Core reusable functions for LL problems
    /** Reverses a singly linked list iteratively. Returns the new head. */
    public static ListNode reverseListIterative(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        while (current != null) {
            ListNode nextTemp = current.next; // Store next node
            current.next = prev; // Reverse pointer
            // Move pointers forward
            prev = current;
            current = nextTemp;
        }
        return prev; // prev is the new head
    }

    /** Merges two sorted linked lists iteratively. Returns the head of the merged list. */
    public static ListNode mergeTwoSortedLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1); // Dummy head to simplify insertion
        ListNode current = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                current.next = l1;
                l1 = l1.next;
            } else {
                current.next = l2;
                l2 = l2.next;
            }
            current = current.next;
        }
        // Append remaining nodes from either list
        current.next = (l1 != null) ? l1 : l2;
        return dummy.next; // Return the actual head of the merged list
    }

    /** Detects if a cycle exists in a linked list using Floyd's Tortoise and Hare algorithm. */
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;
        ListNode slow = head;
        ListNode fast = head; // Start both at head initially is fine too
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true; // Cycle detected
        }
        return false; // No cycle found
    }

    /** Finds the starting node of a cycle in a linked list using Floyd's algorithm, or null if no cycle. */
    public static ListNode detectCycleStart(ListNode head) {
        if (head == null || head.next == null) return null;
        ListNode slow = head, fast = head, intersection = null;
        // 1. Find intersection point
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) { intersection = slow; break; }
        }
        // 2. If no intersection, no cycle
        if (intersection == null) return null;
        // 3. Find cycle start: move one pointer to head, keep other at intersection. Move both one step until they meet.
        slow = head;
        while (slow != intersection) {
            slow = slow.next;
            intersection = intersection.next;
        }
        return slow; // Meeting point is the cycle start
    }

    /** Finds the intersection node of two linked lists (or null if no intersection). Assumes lists are cycle-free. */
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;
        ListNode pA = headA;
        ListNode pB = headB;
        // Traverse both lists. If a pointer reaches the end, switch it to the other list's head.
        // This compensates for length differences. Pointers will meet at the intersection or both become null.
        while (pA != pB) {
            pA = (pA == null) ? headB : pA.next;
            pB = (pB == null) ? headA : pB.next;
        }
        return pA; // Returns intersection node or null
    }


    // --- Tree Operations --- Core reusable functions and properties for Tree problems
    /** Calculates the maximum depth (height) of a binary tree recursively. */
    public static int maxDepth(TreeNode root) {
        if (root == null) return 0;
        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);
        return 1 + Math.max(leftDepth, rightDepth);
    }

    /** Checks if two binary trees are structurally and value-wise identical recursively. */
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        // Base cases
        if (p == null && q == null) return true; // Both null, identical
        if (p == null || q == null) return false; // One null, not identical
        if (p.val != q.val) return false; // Values differ, not identical
        // Recursive step: check left and right subtrees
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    /** Finds the Lowest Common Ancestor in a general Binary Tree recursively. Assumes p and q exist in the tree. */
     public static TreeNode lowestCommonAncestorBT(TreeNode root, TreeNode p, TreeNode q) {
         if (root == null || root == p || root == q) {
            // If root is null, or root is one of the nodes, return root
            return root;
         }
         // Recurse on left and right subtrees
         TreeNode leftLCA = lowestCommonAncestorBT(root.left, p, q);
         TreeNode rightLCA = lowestCommonAncestorBT(root.right, p, q);

         // If p and q were found in different subtrees (left and right are non-null), then root is the LCA
         if (leftLCA != null && rightLCA != null) return root;

         // Otherwise, the LCA must be in the subtree where one node was found
         // Return the non-null result (or null if neither p nor q was found in either subtree)
         return (leftLCA != null) ? leftLCA : rightLCA;
     }
     /** Finds the Lowest Common Ancestor in a Binary Search Tree. Assumes p and q exist in the tree. */
     public static TreeNode lowestCommonAncestorBST(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        int currVal = root.val;
        int pVal = p.val;
        int qVal = q.val;

        if (pVal > currVal && qVal > currVal) {
            // Both nodes are in the right subtree
            return lowestCommonAncestorBST(root.right, p, q);
        } else if (pVal < currVal && qVal < currVal) {
            // Both nodes are in the left subtree
            return lowestCommonAncestorBST(root.left, p, q);
        } else {
            // Nodes are on different sides, or one node is the root. Root is the LCA.
            return root;
        }
    }


     /** Helper for checking if a tree is height-balanced. Returns height or -1 if unbalanced. */
     private static int checkHeightBalanced(TreeNode node) {
         if (node == null) return 0; // Height of null tree is 0

         int leftHeight = checkHeightBalanced(node.left);
         if (leftHeight == -1) return -1; // Left subtree is unbalanced

         int rightHeight = checkHeightBalanced(node.right);
         if (rightHeight == -1) return -1; // Right subtree is unbalanced

         // Check if the current node is balanced
         if (Math.abs(leftHeight - rightHeight) > 1) {
             return -1; // Current node is unbalanced
         }

         // Return height if balanced
         return 1 + Math.max(leftHeight, rightHeight);
     }
     /** Main function to check if tree is height-balanced using the helper. */
     public static boolean isTreeBalanced(TreeNode root) {
         return checkHeightBalanced(root) != -1;
     }

    // --- Iterative Tree Traversals --- Foundational for many tree algorithms
    public static List<Integer> preorderTraversalIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val); // Visit root
            // Push right child first, then left (so left is processed first - LIFO)
            if (node.right != null) stack.push(node.right);
            if (node.left != null) stack.push(node.left);
        }
        return result;
    }

     public static List<Integer> inorderTraversalIterative(TreeNode root) {
         List<Integer> result = new ArrayList<>();
         if (root == null) return result;
         Stack<TreeNode> stack = new Stack<>();
         TreeNode current = root;
         while (current != null || !stack.isEmpty()) {
             // Traverse left as far as possible
             while (current != null) {
                 stack.push(current);
                 current = current.left;
             }
             // Visit the node at the top of the stack (leftmost unvisited)
             current = stack.pop();
             result.add(current.val);
             // Move to the right subtree
             current = current.right;
         }
         return result;
     }

    public static List<Integer> postorderTraversalIterative(TreeNode root) {
        LinkedList<Integer> result = new LinkedList<>(); // Use LinkedList for efficient addFirst
        if (root == null) return result;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.addFirst(node.val); // Add root first (effectively reversing postorder)
            // Push left child first, then right (so right is processed before left by pop, leading to correct reversed order)
            if (node.left != null) stack.push(node.left);
            if (node.right != null) stack.push(node.right);
        }
        // result list now holds nodes in Root-Right-Left order, which is reversed Postorder (Left-Right-Root)
        return result;
    }

    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>(levelSize); // Optimize list capacity
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevel.add(node.val);
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            result.add(currentLevel);
        }
        return result;
    }

    // --- Graph Operations --- Core algorithms for graph problems
    // Assumes standard adjacency list representations (Map<Integer, List<Integer>> or Map<Integer, List<Pair<Integer, Integer>>>)
    // Requires a set of all valid node IDs (`allNodeIds`) for robustness against disconnected components or invalid edges.

    /** Iterative BFS traversal starting from `startNodeId`. Requires graph definition (`adj`, `allNodeIds`). */
     public static List<Integer> bfsTraversalOrderById(int startNodeId, Map<Integer, List<Integer>> adj, Set<Integer> allNodeIds) {
         List<Integer> order = new ArrayList<>();
         Queue<Integer> queue = new LinkedList<>();
         Set<Integer> visited = new HashSet<>();

         if (!allNodeIds.contains(startNodeId)) return order; // Start node must be valid

         queue.offer(startNodeId);
         visited.add(startNodeId);

         while (!queue.isEmpty()) {
             int u = queue.poll();
             order.add(u);

             if (adj.containsKey(u)) {
                 for (int v : adj.getOrDefault(u, Collections.emptyList())) {
                     if (allNodeIds.contains(v) && visited.add(v)) { // Check neighbor validity and visit status
                         queue.offer(v);
                     }
                 }
             }
         }
         return order; // Returns traversal order for the connected component of startNodeId
     }

    /** Iterative DFS traversal starting from `startNodeId`. Requires graph definition (`adj`, `allNodeIds`). */
     public static List<Integer> dfsTraversalOrderIterativeById(int startNodeId, Map<Integer, List<Integer>> adj, Set<Integer> allNodeIds) {
         List<Integer> order = new ArrayList<>();
         Stack<Integer> stack = new Stack<>();
         Set<Integer> visited = new HashSet<>();

         if (!allNodeIds.contains(startNodeId)) return order;

         stack.push(startNodeId);

         while (!stack.isEmpty()) {
             int u = stack.pop();

             if (visited.contains(u)) continue; // Skip if already visited

             visited.add(u);
             order.add(u);

             if (adj.containsKey(u)) {
                 // Push neighbors in reverse order to maintain natural DFS order
                 List<Integer> neighbors = adj.getOrDefault(u, Collections.emptyList());
                 for (int i = neighbors.size() - 1; i >= 0; i--) {
                     int v = neighbors.get(i);
                     if (allNodeIds.contains(v) && !visited.contains(v)) {
                         stack.push(v);
                     }
                 }
             }
         }
         return order; // Returns traversal order for the connected component of startNodeId
     }

     /** Detects cycle in a DIRECTED graph using DFS state tracking. Requires graph definition. */
    public static boolean hasCycleDirectedDFS(Set<Integer> allNodes, Map<Integer, List<Integer>> adj) {
        Map<Integer, Integer> visitState = new HashMap<>(); // 0: unvisited, 1: visiting, 2: visited
        for (int node : allNodes) visitState.put(node, 0);

        for (int node : allNodes) {
            if (visitState.get(node) == 0) {
                if (hasCycleDirectedDFSHelper(node, adj, visitState, allNodes)) {
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean hasCycleDirectedDFSHelper(int u, Map<Integer, List<Integer>> adj, Map<Integer, Integer> visitState, Set<Integer> allNodes) {
        visitState.put(u, 1); // Mark as visiting

        for (int v : adj.getOrDefault(u, Collections.emptyList())) {
            if (!allNodes.contains(v)) continue; // Ignore edges to nodes outside defined set

            int neighborState = visitState.get(v);
            if (neighborState == 1) return true; // Back edge found -> cycle
            if (neighborState == 0) {
                if (hasCycleDirectedDFSHelper(v, adj, visitState, allNodes)) {
                    return true; // Cycle found deeper
                }
            }
            // If neighborState is 2, node v has been fully explored from, no cycle via this path
        }

        visitState.put(u, 2); // Mark as fully visited
        return false;
    }

    /** Detects cycle in an UNDIRECTED graph using DFS and parent tracking. Requires graph definition. */
     public static boolean hasCycleUndirectedDFS(Set<Integer> allNodes, Map<Integer, List<Integer>> adj) {
         Set<Integer> visited = new HashSet<>();
         for (int node : allNodes) {
             if (!visited.contains(node)) {
                 if (hasCycleUndirectedDFSHelper(node, -1, adj, visited, allNodes)) {
                     return true;
                 }
             }
         }
         return false;
     }
     private static boolean hasCycleUndirectedDFSHelper(int u, int parent, Map<Integer, List<Integer>> adj, Set<Integer> visited, Set<Integer> allNodes) {
         visited.add(u);
         for (int v : adj.getOrDefault(u, Collections.emptyList())) {
             if (!allNodes.contains(v)) continue; // Ignore edges outside defined nodes

             if (!visited.contains(v)) {
                 if (hasCycleUndirectedDFSHelper(v, u, adj, visited, allNodes)) {
                     return true; // Cycle found deeper
                 }
             } else if (v != parent) {
                 // Visited node that is not the immediate parent -> back edge -> cycle
                 return true;
             }
         }
         return false;
     }


    /**
     * Topological Sort (Kahn's Algorithm - BFS based). Reusable for DAG processing.
     * Assumes nodes are represented by the keys in the inDegree map and adj map.
     * Requires pre-computed `inDegree` map. Returns empty list if cycle detected.
     */
    public static List<Integer> topologicalSortKahn(Map<Integer, List<Integer>> adj, Map<Integer, Integer> inDegree) {
        List<Integer> sortedOrder = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> currentInDegree = new HashMap<>(inDegree); // Work on a copy
        Set<Integer> allNodes = currentInDegree.keySet();

        for (Map.Entry<Integer, Integer> entry : currentInDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        int count = 0;
        while (!queue.isEmpty()) {
            int u = queue.poll();
            sortedOrder.add(u);
            count++;

            for (int v : adj.getOrDefault(u, Collections.emptyList())) {
                if (currentInDegree.containsKey(v)) { // Process only neighbors part of the initial graph definition
                    currentInDegree.put(v, currentInDegree.get(v) - 1);
                    if (currentInDegree.get(v) == 0) {
                        queue.offer(v);
                    }
                }
            }
        }

        return (count == allNodes.size()) ? sortedOrder : new ArrayList<>(); // Check if all nodes were processed
    }

    /**
     * Dijkstra's Algorithm for Shortest Paths from a single source. Reusable SSSP algorithm.
     * Handles non-negative edge weights. Requires graph definition.
     */
    public static Map<Integer, Integer> dijkstraShortestPaths(Map<Integer, List<Pair<Integer, Integer>>> adj, int startNode, Set<Integer> allNodeIds) {
        Map<Integer, Integer> dist = new HashMap<>();
        for (int id : allNodeIds) dist.put(id, Integer.MAX_VALUE);

        if (!allNodeIds.contains(startNode)) return dist; // Return defaults if start node invalid

        dist.put(startNode, 0);
        PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(PAIR_INTEGER_KEY_COMPARATOR);
        pq.offer(new Pair<>(0, startNode)); // Pair<distance, nodeID>

        while (!pq.isEmpty()) {
            Pair<Integer, Integer> top = pq.poll();
            int d = top.key;    // current distance to u
            int u = top.value;  // node id

            if (d > dist.get(u)) continue; // Skip if found shorter path already

            for (Pair<Integer, Integer> edge : adj.getOrDefault(u, Collections.emptyList())) {
                int v = edge.key;      // neighbor id
                int weight = edge.value; // weight u->v

                if (dist.containsKey(v) && weight >= 0) { // Ensure neighbor is valid and weight non-negative
                    // Relaxation
                    if (dist.get(u) != Integer.MAX_VALUE && dist.get(u) + weight < dist.get(v)) {
                        dist.put(v, dist.get(u) + weight);
                        pq.offer(new Pair<>(dist.get(v), v));
                    }
                }
            }
        }
        return dist; // Map NodeID -> Shortest distance (Integer.MAX_VALUE if unreachable)
    }

    /**
     * Prim's Algorithm for Minimum Spanning Tree (MST). Reusable MST algorithm.
     * Assumes connected, undirected, weighted graph. Requires graph definition.
     * Returns total weight (long) or -1 if not connected.
     */
     public static long primsMST(Map<Integer, List<Pair<Integer, Integer>>> adj, Set<Integer> allNodeIds) {
         if (allNodeIds == null || allNodeIds.isEmpty()) return 0;

         long mstWeight = 0;
         int edgeCount = 0;
         Set<Integer> inMST = new HashSet<>();
         PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(PAIR_INTEGER_KEY_COMPARATOR); // Pair<weight, nodeID>
         Map<Integer, Integer> minEdgeToNode = new HashMap<>(); // Min edge weight connecting node outside MST
         for(int node : allNodeIds) minEdgeToNode.put(node, Integer.MAX_VALUE);

         // Start from an arbitrary node
         int startNode = allNodeIds.iterator().next();
         minEdgeToNode.put(startNode, 0);
         pq.offer(new Pair<>(0, startNode));

         while (!pq.isEmpty() && edgeCount < allNodeIds.size()) {
             Pair<Integer, Integer> top = pq.poll();
             int weight = top.key;
             int u = top.value;

             if (inMST.contains(u)) continue; // Already included

             inMST.add(u);
             mstWeight += weight;
             edgeCount++;

             for (Pair<Integer, Integer> edge : adj.getOrDefault(u, Collections.emptyList())) {
                 int v = edge.key;
                 int edgeWeight = edge.value;
                 if (allNodeIds.contains(v) && !inMST.contains(v) && edgeWeight < minEdgeToNode.get(v)) {
                     minEdgeToNode.put(v, edgeWeight);
                     pq.offer(new Pair<>(edgeWeight, v));
                 }
             }
         }

         return (edgeCount == allNodeIds.size()) ? mstWeight : -1; // Check connectivity
     }

    /**
      * Kruskal's Algorithm for Minimum Spanning Tree (MST). Reusable MST algorithm.
      * Requires list of all edges and total number of nodes (0 to numNodes-1).
      * Returns total weight (long) or -1 if not connected.
      */
     public static long kruskalMST(List<Edge> edges, int numNodes) {
         if (edges == null || numNodes <= 0) return 0;
         if (numNodes == 1 && edges.isEmpty()) return 0; // Single node case

         Collections.sort(edges); // Sort edges by weight
         UnionFind uf = new UnionFind(numNodes);
         long mstWeight = 0;
         int edgesInMST = 0;

         for (Edge edge : edges) {
             if (edge.u < 0 || edge.u >= numNodes || edge.v < 0 || edge.v >= numNodes) continue; // Skip invalid edges
             if (uf.union(edge.u, edge.v)) { // If edge connects two components
                 mstWeight += edge.weight;
                 edgesInMST++;
                 if (edgesInMST == numNodes - 1) break; // MST complete
             }
         }

         // Check if a valid spanning tree was formed (V-1 edges AND all nodes connected if graph was connected initially)
         // A more robust check might involve verifying uf.getCount() == 1 if the original graph was guaranteed connected.
         // For potentially disconnected graphs, returning MST weight for the main component might be desired,
         // but typically Kruskal returns based on adding V-1 edges.
         return (edgesInMST == numNodes - 1) ? mstWeight : -1;
     }


    // --- Heap Operations / Patterns --- Reusable for Kth element problems
    /** Finds the Kth largest element using a MinHeap (O(N log K)). */
    public static int findKthLargestHeap(int[] nums, int k) {
        if (nums == null || k <= 0 || k > nums.length) throw new IllegalArgumentException("Invalid input");
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k); // Min-heap of size k
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll(); // Keep only the k largest elements
            }
        }
        return minHeap.peek(); // Root is the Kth largest
    }
    /** Finds the Kth smallest element using a MaxHeap (O(N log K)). */
     public static int findKthSmallestHeap(int[] nums, int k) {
         if (nums == null || k <= 0 || k > nums.length) throw new IllegalArgumentException("Invalid input");
         PriorityQueue<Integer> maxHeap = new PriorityQueue<>(k, Comparator.reverseOrder()); // Max-heap of size k
         for (int num : nums) {
             maxHeap.offer(num);
             if (maxHeap.size() > k) {
                 maxHeap.poll(); // Keep only the k smallest elements
             }
         }
         return maxHeap.peek(); // Root is the Kth smallest
     }

    /**
     * Finds the Kth smallest element using QuickSelect algorithm (average O(N), worst O(N^2)).
     * Modifies the input array `nums` in place. Reusable alternative for Kth element.
     * @param k 1-based rank (1st smallest, 2nd smallest, etc.)
     */
    public static int quickSelectKthSmallest(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
            throw new IllegalArgumentException("Invalid input");
        }
        return quickSelectHelper(nums, 0, nums.length - 1, k - 1); // k-1 for 0-based index
    }

    private static int quickSelectHelper(int[] nums, int left, int right, int targetIndex) {
        while (left <= right) {
            if (left == right) return nums[left]; // Base case: single element
            int pivotIndex = partitionHoare(nums, left, right); // Find partition index

            if (pivotIndex == targetIndex) {
                return nums[pivotIndex];
            } else if (pivotIndex < targetIndex) {
                left = pivotIndex + 1; // Search right subarray
            } else {
                right = pivotIndex; // Search left subarray (inclusive for Hoare partition)
                // Note: if using Lomuto, right would be pivotIndex - 1
            }
        }
        // This part should ideally not be reached with correct logic/input
        throw new RuntimeException("QuickSelect logic error or invalid input.");
    }

    // Hoare partition scheme implementation
    private static int partitionHoare(int[] nums, int left, int right) {
        int pivotValue = nums[left + (right - left) / 2]; // Choose pivot (e.g., middle)
        int i = left - 1;
        int j = right + 1;
        while (true) {
            do { i++; } while (nums[i] < pivotValue);
            do { j--; } while (nums[j] > pivotValue);
            if (i >= j) return j; // Partition point
            // Swap elements nums[i] and nums[j]
            int temp = nums[i]; nums[i] = nums[j]; nums[j] = temp;
        }
    }


     // --- Interval Operations --- Core reusable function for Interval problems
     /**
      * Merges overlapping intervals. Input list `intervals` should contain [start, end] pairs.
      * Sorts the intervals by start time first. Returns a new list of merged intervals.
      */
     public static List<int[]> mergeIntervals(List<int[]> intervals) {
         if (intervals == null || intervals.size() <= 1) {
             return new ArrayList<>(intervals); // Return copy or original if safe
         }

         // Sort intervals by start time (uses static comparator)
         intervals.sort(INTERVAL_START_COMPARATOR_ARRAY);

         LinkedList<int[]> merged = new LinkedList<>(); // Use LinkedList for efficient getLast()
         for (int[] interval : intervals) {
             // If merged list is empty or current interval does not overlap with the last merged interval
             if (merged.isEmpty() || interval[0] > merged.getLast()[1]) {
                 merged.add(interval); // Add as a new non-overlapping interval
             } else {
                 // Overlap exists, merge by extending the end of the last interval
                 merged.getLast()[1] = Math.max(merged.getLast()[1], interval[1]);
             }
         }
         return merged;
     }

     // --- Stack-based Patterns --- Reusable monotonic stack patterns
    /**
     * Finds the next greater element for each element (to its right) using a Monotonic Decreasing Stack.
     * Returns an array where result[i] is the NGE for nums[i], or -1 if none exists.
     */
    public static int[] nextGreaterElement(int[] nums) {
        if (nums == null) return null;
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1); // Default if no NGE
        Stack<Integer> stack = new Stack<>(); // Stores indices, keeps elements in decreasing order

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
                result[stack.pop()] = nums[i]; // Found NGE for element at stack.peek()
            }
            stack.push(i); // Push current index
        }
        // Indices remaining in stack have no NGE
        return result;
    }

    /**
     * Finds the previous smaller element for each element (to its left) using a Monotonic Increasing Stack.
     * Returns an array where result[i] is the PSE for nums[i], or -1 if none exists.
     */
    public static int[] previousSmallerElement(int[] nums) {
        if (nums == null) return null;
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1); // Default if no PSE
        Stack<Integer> stack = new Stack<>(); // Stores indices, keeps elements in increasing order

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[i] <= nums[stack.peek()]) {
                stack.pop(); // Pop elements greater than or equal to current
            }
            if (!stack.isEmpty()) {
                result[i] = nums[stack.peek()]; // Top element is the PSE
            }
            stack.push(i); // Push current index
        }
        return result;
    }

    // =========================================================================
    // 3. Core Recursive / Backtracking Templates - Encapsulate common structures
    // =========================================================================

    /** Template helper for generating Subsets (Power Set). */
    public static void backtrackSubsetsHelper(List<List<Integer>> resultList, List<Integer> currentSubset, int[] nums, int startIndex) {
        resultList.add(new ArrayList<>(currentSubset)); // Add the subset found so far
        for (int i = startIndex; i < nums.length; i++) {
            currentSubset.add(nums[i]); // Choose
            backtrackSubsetsHelper(resultList, currentSubset, nums, i + 1); // Explore
            currentSubset.remove(currentSubset.size() - 1); // Unchoose (backtrack)
        }
    }
    /** Template helper for Subsets w/ duplicates (requires sorted nums). */
    public static void backtrackSubsetsWithDupHelper(List<List<Integer>> resultList, List<Integer> currentSubset, int[] sortedNums, int startIndex) {
        resultList.add(new ArrayList<>(currentSubset));
        for (int i = startIndex; i < sortedNums.length; i++) {
            // Skip duplicates: If this element is the same as previous and we're not processing the first occurrence in this loop level
            if (i > startIndex && sortedNums[i] == sortedNums[i - 1]) continue;
            currentSubset.add(sortedNums[i]);
            backtrackSubsetsWithDupHelper(resultList, currentSubset, sortedNums, i + 1);
            currentSubset.remove(currentSubset.size() - 1);
        }
    }

    /** Template helper for generating Combinations (Choose k elements from 1 to n). */
     public static void backtrackCombinationsHelper(List<List<Integer>> resultList, List<Integer> currentCombination, int n, int k, int startNum) {
         if (currentCombination.size() == k) {
             resultList.add(new ArrayList<>(currentCombination));
             return;
         }
         // Optimization: If remaining elements + current size < k, prune
         if (currentCombination.size() + (n - startNum + 1) < k) {
             return;
         }
         for (int i = startNum; i <= n; i++) {
             currentCombination.add(i);
             backtrackCombinationsHelper(resultList, currentCombination, n, k, i + 1); // Next choice starts from i+1
             currentCombination.remove(currentCombination.size() - 1);
         }
     }
     /** Template helper for generating Combinations (Choose k elements from input array nums). */
     public static void backtrackCombinationsFromArrayHelper(List<List<Integer>> resultList, List<Integer> currentCombination, int[] nums, int k, int startIndex) {
         if (currentCombination.size() == k) {
             resultList.add(new ArrayList<>(currentCombination));
             return;
         }
         if (currentCombination.size() + (nums.length - startIndex) < k) {
             return;
         }
         for (int i = startIndex; i < nums.length; i++) {
             // Add logic here to handle duplicates in nums if necessary (similar to subsetsWithDup)
             currentCombination.add(nums[i]);
             backtrackCombinationsFromArrayHelper(resultList, currentCombination, nums, k, i + 1);
             currentCombination.remove(currentCombination.size() - 1);
         }
     }


     /** Template helper for generating Permutations of distinct elements. */
     public static void backtrackPermutationsHelper(List<List<Integer>> resultList, List<Integer> currentPermutation, int[] nums, boolean[] used) {
         if (currentPermutation.size() == nums.length) {
             resultList.add(new ArrayList<>(currentPermutation));
             return;
         }
         for (int i = 0; i < nums.length; i++) {
             if (!used[i]) { // If element at index i is available
                 used[i] = true;
                 currentPermutation.add(nums[i]);
                 backtrackPermutationsHelper(resultList, currentPermutation, nums, used);
                 currentPermutation.remove(currentPermutation.size() - 1); // Backtrack
                 used[i] = false;
             }
         }
     }

      /** Template helper for Permutations w/ duplicates (requires sorted nums). */
     public static void backtrackPermutationsWithDupHelper(List<List<Integer>> resultList, List<Integer> currentPermutation, int[] sortedNums, boolean[] used) {
         if (currentPermutation.size() == sortedNums.length) {
             resultList.add(new ArrayList<>(currentPermutation));
             return;
         }
         for (int i = 0; i < sortedNums.length; i++) {
             // Skip if element already used OR if it's a duplicate and the previous identical element hasn't been used yet in this path
             if (used[i] || (i > 0 && sortedNums[i] == sortedNums[i - 1] && !used[i - 1])) {
                 continue;
             }
             used[i] = true;
             currentPermutation.add(sortedNums[i]);
             backtrackPermutationsWithDupHelper(resultList, currentPermutation, sortedNums, used);
             currentPermutation.remove(currentPermutation.size() - 1); // Backtrack
             used[i] = false;
         }
     }


    /**
     * Template helper for DFS on a grid. Adapt parameters, return type, base cases,
     * and processing logic based on the specific problem (e.g., counting islands, finding paths).
     * @return Example boolean return, often void or int (count).
     */
     public static void gridDfsTemplate(char[][] grid, int r, int c, boolean[][] visited /*, other params like count */) {
         int R = grid.length; int C = grid[0].length;

         // Base Cases / Boundary Checks (Adapt conditions as needed)
         if (r < 0 || r >= R || c < 0 || c >= C || visited[r][c] /* || grid[r][c] == '0' */ ) {
             return; // Stop exploration for this path
         }

         visited[r][c] = true; // Mark current cell visited

         // Process Current Cell (Adapt as needed)
         // e.g., if (grid[r][c] == '1') { count++; }

         // Explore Neighbors (4 directions typical)
         int[] dr = {-1, 1, 0, 0}; // Up, Down, Left, Right
         int[] dc = {0, 0, -1, 1};
         for (int i = 0; i < 4; i++) {
             gridDfsTemplate(grid, r + dr[i], c + dc[i], visited /*, other params */);
         }

         // Backtracking Step (Usually not needed for simple grid DFS if visited handles cycles/revisits)
         // If state needs to be reset for alternative paths (e.g., pathfinding with unique steps), add here.
         // visited[r][c] = false;
     }

} // End of NeetCodeStandardUtilsV9 class
