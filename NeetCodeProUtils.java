import java.util.*;
import java.util.LinkedList; // Explicit import

/**
 * NeetCodeStandardUtils (v7 - Final Strict): A utility class providing
 * fundamental, highly reusable data structures, algorithms, and core patterns
 * frequently needed for competitive programming and interview preparation
 * (e.g., NeetCode 150).
 *
 * Focuses on building blocks analogous to standard library utilities,
 * aiming for reuse across multiple DIFFERENT problem types based on analysis
 * of the NeetCode 150 list. Excludes utilities mapped primarily to only one
 * problem type in that analysis.
 * Adheres to standard Java syntax, avoiding Streams and Lambda Expressions.
 */
public class NeetCodeStandardUtilsV7 {

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

    /** Node for Adjacency List Graph representation (using Integer IDs). */
    // Note: Graph algorithms below often use Maps for adjacency lists:
    // Map<Integer, List<Integer>> adj = ...;
    // Or sometimes: Map<Integer, List<Pair<Integer, Integer>>> adjWeighted = ...;

    /** Basic Generic Pair class, useful for graph algorithms like Dijkstra. */
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

    // --- Static Comparators ---
    /** Comparator for int[] intervals by start time. Reusable for sorting intervals. */
    public static final Comparator<int[]> INTERVAL_START_COMPARATOR_ARRAY = new Comparator<int[]>() {
         @Override
         public int compare(int[] a, int[] b) {
             return Integer.compare(a[0], b[0]);
         }
    };

    /** Comparator for Pairs where Key is Integer (used in Dijkstra). Reusable pattern. */
     public static final Comparator<Pair<Integer, Integer>> PAIR_INTEGER_KEY_COMPARATOR = new Comparator<Pair<Integer, Integer>>() {
         @Override
         public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
             return Integer.compare(p1.getKey(), p2.getKey()); // Compares by the Integer key (distance)
         }
     };


    // =========================================================================
    // 1. Fundamental Data Structure Implementations
    // =========================================================================

    /**
     * Union-Find (Disjoint Set Union) data structure implementation.
     * Highly reusable for connectivity, cycle detection, and grouping problems.
     */
    public static class UnionFind {
        private int[] parent;
        private int[] rank;
        private int count;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            count = size;
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                // rank[i] defaults to 0
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

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
                    rank[rootX]++;
                }
                count--;
                return true; // Union performed
            }
            return false; // Already in the same set
        }

        public int getCount() { return count; }
        public boolean isConnected(int x, int y) { return find(x) == find(y); }
    }

    /**
     * Basic Trie (Prefix Tree) implementation.
     * Reusable for prefix searching, autocomplete, and related string problems.
     */
    public static class Trie {
        private static class TrieNode {
            Map<Character, TrieNode> children = new HashMap<>();
            boolean isWord = false;
        }
        private final TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        public void insert(String word) {
            TrieNode node = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
            }
            node.isWord = true;
        }

        public boolean search(String word) {
            TrieNode node = findNode(word);
            return node != null && node.isWord;
        }

        public boolean startsWith(String prefix) {
            return findNode(prefix) != null;
        }

        private TrieNode findNode(String str) {
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

        // Wildcard search relevant for specific problems but included as fundamental Trie extension.
        public boolean searchWithWildcard(String word) {
            return searchRecursiveWildcardHelper(word, 0, root);
        }
        private boolean searchRecursiveWildcardHelper(String word, int index, TrieNode node) {
            if (node == null) return false;
            if (index == word.length()) return node.isWord;
            char c = word.charAt(index);
            if (c == '.') {
                // Check all children if wildcard
                for (TrieNode child : node.children.values()) {
                    if (searchRecursiveWildcardHelper(word, index + 1, child)) {
                        return true;
                    }
                }
                return false; // Wildcard path failed
            } else {
                // Check specific child
                return searchRecursiveWildcardHelper(word, index + 1, node.children.get(c));
            }
        }
    }

    // =========================================================================
    // 2. Fundamental Algorithms & Operations
    // =========================================================================

    // --- Array Utilities ---
    /** Swaps two elements in an integer array. Fundamental operation. */
    public static void swap(int[] arr, int i, int j) {
        if (arr == null || i < 0 || j < 0 || i >= arr.length || j >= arr.length) return;
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /** Creates a frequency map for integers in an array. Highly reusable helper. */
    public static Map<Integer, Integer> getFrequencyMapInt(int[] nums) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        if (nums == null) return freqMap;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
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

    // --- Two Pointer Patterns ---
    /** Checks if a pair summing to target exists in a SORTED array using two pointers. */
    public static boolean twoPointerSumCheckSorted(int[] sortedArr, int target) {
        if (sortedArr == null || sortedArr.length < 2) return false;
        int left = 0;
        int right = sortedArr.length - 1;
        while (left < right) {
            int sum = sortedArr[left] + sortedArr[right];
            if (sum == target) {
                return true;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return false;
    }

     /** Helper to check if a substring is a palindrome (case-sensitive). */
     public static boolean isPalindromeHelper(String s, int left, int right) {
         if (s == null) return false; // Or true if empty is palindrome? Decide convention.
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
        int left = 0;
        int right = sortedNums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (sortedNums[mid] == target) {
                return mid;
            } else if (sortedNums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // Not found
    }

    /** Functional interface for the check method in binarySearchOnAnswer. */
    public interface CanAchieveChecker {
        boolean check(int value, Object... params);
    }
    /** Template for Binary Search on the Answer Space. Finds minimum value satisfying check. */
    public static int binarySearchOnAnswerMin(int low, int high, CanAchieveChecker checker, Object... params) {
         int ans = -1; // Indicate not found, or use high + 1 if appropriate
         while (low <= high) {
             int mid = low + (high - low) / 2;
             if (checker.check(mid, params)) { // If mid works...
                 ans = mid;       // Store it as a potential answer
                 high = mid - 1; // Try for an even smaller value
             } else {
                 low = mid + 1;   // Mid doesn't work, need a larger value
             }
         }
         return ans; // Return the best (minimum) answer found, or -1
    }

    // --- Linked List Operations ---
    /** Reverses a singly linked list iteratively. Returns the new head. */
    public static ListNode reverseListIterative(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        ListNode next = null;
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        return prev;
    }

    /** Merges two sorted linked lists iteratively. */
    public static ListNode mergeTwoSortedLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1);
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
        current.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    /** Detects if a cycle exists in a linked list using Floyd's algorithm. */
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null) {
            if (slow == fast) return true;
            slow = slow.next;
            fast = fast.next.next;
        }
        return false;
    }

    /** Finds the starting node of a cycle in a linked list, or null if no cycle. */
    public static ListNode detectCycleStart(ListNode head) {
        if (head == null || head.next == null) return null;
        ListNode slow = head, fast = head, intersection = null;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
            if (slow == fast) { intersection = slow; break; }
        }
        if (intersection == null) return null;
        slow = head;
        while (slow != intersection) { slow = slow.next; intersection = intersection.next; }
        return slow;
    }

    /** Finds the intersection node of two linked lists (or null if no intersection). */
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;
        ListNode pA = headA;
        ListNode pB = headB;
        // If pA & pB have different lengths, this ensures they will meet after at most two passes.
        while (pA != pB) {
            pA = (pA == null) ? headB : pA.next;
            pB = (pB == null) ? headA : pB.next;
        }
        return pA; // Either the intersection node or null if they both reached null
    }


    // --- Tree Operations ---
    /** Calculates the maximum depth (height) of a binary tree recursively. */
    public static int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    /** Checks if two binary trees are structurally and value-wise identical recursively. */
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null || p.val != q.val) return false;
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    /** Finds the Lowest Common Ancestor in a general Binary Tree recursively. */
     public static TreeNode lowestCommonAncestorBT(TreeNode root, TreeNode p, TreeNode q) {
         if (root == null || root == p || root == q) return root;
         TreeNode left = lowestCommonAncestorBT(root.left, p, q);
         TreeNode right = lowestCommonAncestorBT(root.right, p, q);
         if (left != null && right != null) return root;
         return (left != null) ? left : right;
     }

     /** Helper for checking if a tree is height-balanced. Returns height or -1 if unbalanced. */
     public static int isBalancedHelper(TreeNode node) {
         if (node == null) return 0;
         int leftH = isBalancedHelper(node.left);
         if (leftH == -1) return -1;
         int rightH = isBalancedHelper(node.right);
         if (rightH == -1) return -1;
         if (Math.abs(leftH - rightH) > 1) return -1;
         return 1 + Math.max(leftH, rightH);
     }
      /** Main function to check if tree is balanced using the helper. */
     public static boolean isTreeBalanced(TreeNode root) {
         return isBalancedHelper(root) != -1;
     }

     /** Serializes a binary tree to a string (Level Order with null markers). */
    public static String serializeTreeLevelOrder(TreeNode root) {
        if (root == null) return "null";
        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node == null) {
                sb.append("null,");
                continue;
            }
            sb.append(node.val).append(",");
            queue.offer(node.left);
            queue.offer(node.right);
        }
        // Minimal cleanup: remove trailing comma if exists
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /** Deserializes a string (from serializeTreeLevelOrder format) back to a binary tree. */
    public static TreeNode deserializeTreeLevelOrder(String data) {
        if (data == null || data.equals("null") || data.isEmpty()) return null;
        String[] values = data.split(",");
        if (values[0].equals("null")) return null;

        TreeNode root = new TreeNode(Integer.parseInt(values[0]));
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;

        while (!queue.isEmpty() && i < values.length) {
            TreeNode node = queue.poll();

            // Process left child
            if (i < values.length && !values[i].equals("null")) {
                TreeNode leftChild = new TreeNode(Integer.parseInt(values[i]));
                node.left = leftChild;
                queue.offer(leftChild);
            }
            i++; // Always advance index

            // Process right child
            if (i < values.length && !values[i].equals("null")) {
                TreeNode rightChild = new TreeNode(Integer.parseInt(values[i]));
                node.right = rightChild;
                queue.offer(rightChild);
            }
            i++; // Always advance index
        }
        return root;
    }


    // --- Iterative Tree Traversals (Return List of values) ---
    public static List<Integer> preorderTraversalIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val);
            if (node.right != null) stack.push(node.right);
            if (node.left != null) stack.push(node.left);
        }
        return result;
    }

    public static List<Integer> postorderTraversalIterative(TreeNode root) {
        LinkedList<Integer> result = new LinkedList<>(); // Use addFirst
        if (root == null) return result;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.addFirst(node.val); // Add root value first
            // Push left then right, so right is processed first by stack pop
            if (node.left != null) stack.push(node.left);
            if (node.right != null) stack.push(node.right);
        }
        return result;
    }

    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();
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

    // --- Graph Operations ---
    // Note: Using Integer IDs and Adjacency Map (Map<Integer, List<Integer>>)
    // is often more practical in interviews than the GraphNode class.

    /** Iterative BFS traversal returning list of visited node IDs in BFS order. */
     public static List<Integer> bfsTraversalOrderById(int startNodeId, Map<Integer, List<Integer>> adj) {
         List<Integer> order = new ArrayList<>();
         Queue<Integer> queue = new LinkedList<>();
         Set<Integer> visited = new HashSet<>();
         if (!adj.containsKey(startNodeId) && !adj.isEmpty()) return order; // Check start node exists if graph isn't empty

         queue.offer(startNodeId);
         visited.add(startNodeId);

         while (!queue.isEmpty()) {
             int currentNodeId = queue.poll();
             order.add(currentNodeId);

             // Check if node exists in adj map before getting neighbors
             if (adj.containsKey(currentNodeId)) {
                 for (int neighborId : adj.get(currentNodeId)) {
                     if (visited.add(neighborId)) { // Add returns true if new
                         queue.offer(neighborId);
                     }
                 }
             }
         }
         return order;
     }

    /** Iterative DFS traversal returning list of visited node IDs in DFS order. */
     public static List<Integer> dfsTraversalOrderIterativeById(int startNodeId, Map<Integer, List<Integer>> adj) {
         List<Integer> order = new ArrayList<>();
         Stack<Integer> stack = new Stack<>();
         Set<Integer> visited = new HashSet<>();
         if (!adj.containsKey(startNodeId) && !adj.isEmpty()) return order;

         stack.push(startNodeId);

         while (!stack.isEmpty()) {
             int currentNodeId = stack.pop();
             if (visited.add(currentNodeId)) { // Process only if new
                 order.add(currentNodeId);
                 // Add neighbors in reverse for stack to process them "forward"
                 if (adj.containsKey(currentNodeId)) {
                    List<Integer> neighbors = adj.get(currentNodeId);
                     for (int i = neighbors.size() - 1; i >= 0; i--) {
                        int neighborId = neighbors.get(i);
                         if (!visited.contains(neighborId)) {
                             stack.push(neighborId);
                         }
                     }
                 }
             }
         }
         return order;
     }

     /** Detects cycle in a DIRECTED graph using DFS state tracking. */
    public static boolean hasCycleDirectedDFS(Set<Integer> allNodes, Map<Integer, List<Integer>> adj) {
        // 0: unvisited, 1: visiting, 2: visited
        Map<Integer, Integer> visitState = new HashMap<>();
        for(int node : allNodes) visitState.put(node, 0);

        for (int node : allNodes) {
            if (visitState.get(node) == 0) { // If unvisited
                if (hasCycleDirectedDFSHelper(node, adj, visitState)) {
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean hasCycleDirectedDFSHelper(int node, Map<Integer, List<Integer>> adj, Map<Integer, Integer> visitState) {
        visitState.put(node, 1); // Mark as visiting

        if (adj.containsKey(node)) {
            for (int neighbor : adj.get(node)) {
                if (visitState.get(neighbor) == 1) { // Found back edge to a visiting node
                    return true;
                }
                if (visitState.get(neighbor) == 0) { // If neighbor is unvisited
                    if (hasCycleDirectedDFSHelper(neighbor, adj, visitState)) {
                        return true; // Cycle detected deeper
                    }
                }
                // If neighbor is 2 (visited), do nothing - already explored from there
            }
        }

        visitState.put(node, 2); // Mark as fully visited
        return false;
    }

    /** Detects cycle in an UNDIRECTED graph using DFS and parent tracking. */
     public static boolean hasCycleUndirectedDFS(Set<Integer> allNodes, Map<Integer, List<Integer>> adj) {
         Set<Integer> visited = new HashSet<>();
         for (int node : allNodes) {
             if (!visited.contains(node)) {
                 if (hasCycleUndirectedDFSHelper(node, -1, adj, visited)) { // -1 for no parent initially
                     return true;
                 }
             }
         }
         return false;
     }
     private static boolean hasCycleUndirectedDFSHelper(int node, int parent, Map<Integer, List<Integer>> adj, Set<Integer> visited) {
         visited.add(node);
         if (adj.containsKey(node)) {
             for (int neighbor : adj.get(node)) {
                 if (!visited.contains(neighbor)) {
                     // Recurse if neighbor not visited
                     if (hasCycleUndirectedDFSHelper(neighbor, node, adj, visited)) {
                         return true;
                     }
                 } else if (neighbor != parent) {
                     // If visited neighbor is not the immediate parent, it's a cycle
                     return true;
                 }
                 // If visited neighbor IS the parent, just ignore it (it's the edge we came from)
             }
         }
         return false; // No cycle found starting from this node in this path
     }


    /** Topological Sort (Kahn's Algorithm). Requires pre-built adj map and inDegree map. */
    public static List<Integer> topologicalSortKahn(int numNodes, Map<Integer, List<Integer>> adj, Map<Integer, Integer> inDegree) {
        Queue<Integer> queue = new LinkedList<>();
        // Initialize queue with nodes having in-degree 0
        // Assumes nodes are 0 to numNodes-1 or keys in inDegree map cover all nodes
        for (Map.Entry<Integer, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        // Handle nodes potentially not in prerequisites (inDegree 0 by default)
        for (int i=0; i<numNodes; ++i) {
            if (inDegree.getOrDefault(i, 0) == 0 && !queue.contains(i)) { // Crude check if already added
                 queue.offer(i);
            }
        }


        List<Integer> sortedOrder = new ArrayList<>();
        int count = 0;
        while (!queue.isEmpty()) {
            int u = queue.poll();
            sortedOrder.add(u);
            count++;

            if (adj.containsKey(u)) {
                for (int v : adj.get(u)) {
                    int newDegree = inDegree.get(v) - 1;
                    inDegree.put(v, newDegree);
                    if (newDegree == 0) {
                        queue.offer(v);
                    }
                }
            }
        }

        // If count matches total nodes, sort is valid
        if (count == numNodes) { // Use count, not sortedOrder.size(), if nodes aren't 0..N-1
            return sortedOrder;
        } else {
            return new ArrayList<>(); // Cycle detected
        }
    }

    /** Dijkstra's Algorithm for Shortest Paths. Requires pre-built adj list map and set of all node IDs. */
    public static Map<Integer, Integer> dijkstraShortestPaths(Map<Integer, List<Pair<Integer, Integer>>> adj, int startNode, Set<Integer> allNodeIds) {
        Map<Integer, Integer> dist = new HashMap<>();
        for (int id : allNodeIds) dist.put(id, Integer.MAX_VALUE);
        if (!allNodeIds.contains(startNode)) return dist; // Or handle error

        dist.put(startNode, 0);
        // Min-heap storing Pair<distance, nodeID>
        PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(PAIR_INTEGER_KEY_COMPARATOR);
        pq.offer(new Pair<>(0, startNode));

        while (!pq.isEmpty()) {
            Pair<Integer, Integer> top = pq.poll();
            int d = top.key;
            int u = top.value;

            // If already found shorter path, skip
            if (d > dist.get(u)) continue;

            // Explore neighbors
            if (adj.containsKey(u)) {
                for (Pair<Integer, Integer> edge : adj.get(u)) {
                    int v = edge.key;
                    int weight = edge.value;

                    // Check if neighbor exists in our set of nodes
                    if (dist.containsKey(v)) {
                        // Relaxation
                        if (dist.get(u) != Integer.MAX_VALUE && dist.get(u) + weight < dist.get(v)) {
                            dist.put(v, dist.get(u) + weight);
                            pq.offer(new Pair<>(dist.get(v), v));
                        }
                    }
                }
            }
        }
        return dist;
    }

    // --- Heap Operations / Patterns ---
    /** Finds the Kth largest element using a MinHeap (Reusable Heap Pattern). */
    public static int findKthLargestHeap(int[] nums, int k) {
        if (nums == null || k <= 0 || k > nums.length) throw new IllegalArgumentException("Invalid input");
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
        for (int i = 0; i < nums.length; i++) {
            minHeap.offer(nums[i]);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return minHeap.peek();
    }
    /** Finds the Kth smallest element using a MaxHeap (Reusable Heap Pattern). */
     public static int findKthSmallestHeap(int[] nums, int k) {
        if (nums == null || k <= 0 || k > nums.length) throw new IllegalArgumentException("Invalid input");
         PriorityQueue<Integer> maxHeap = new PriorityQueue<>(k, Comparator.reverseOrder());
         for (int i = 0; i < nums.length; i++) {
             maxHeap.offer(nums[i]);
             if (maxHeap.size() > k) {
                 maxHeap.poll();
             }
         }
         return maxHeap.peek();
     }

    // =========================================================================
    // 3. Core Recursive / Backtracking Templates (Use within specific problem solutions)
    // =========================================================================

    /** Template helper for generating subsets. Call from problem setup. */
    public static void backtrackSubsetsHelper(List<List<Integer>> resultList, List<Integer> currentSubset, int[] nums, int startIndex) {
        resultList.add(new ArrayList<>(currentSubset));
        for (int i = startIndex; i < nums.length; i++) {
            currentSubset.add(nums[i]);
            backtrackSubsetsHelper(resultList, currentSubset, nums, i + 1);
            currentSubset.remove(currentSubset.size() - 1);
        }
    }
    /** Template helper for subsets w/ duplicates (requires sorted nums). Call from problem setup. */
    public static void backtrackSubsetsWithDupHelper(List<List<Integer>> resultList, List<Integer> currentSubset, int[] sortedNums, int startIndex) {
        resultList.add(new ArrayList<>(currentSubset));
        for (int i = startIndex; i < sortedNums.length; i++) {
            if (i > startIndex && sortedNums[i] == sortedNums[i - 1]) continue; // Skip duplicates
            currentSubset.add(sortedNums[i]);
            backtrackSubsetsWithDupHelper(resultList, currentSubset, sortedNums, i + 1);
            currentSubset.remove(currentSubset.size() - 1);
        }
    }
    /** Template helper for DFS on a grid. Adapt params/return/logic as needed. */
     public static boolean gridDfsTemplate(char[][] grid, int r, int c, boolean[][] visited /*, add params */) {
        int R = grid.length; int C = grid[0].length;
        // Check boundaries and visited
        if (r < 0 || r >= R || c < 0 || c >= C || visited[r][c]) {
            return false; // Or other base case return
        }
        // --- Check problem-specific goal/condition ---
        // if (isGoal(...)) return true;

        visited[r][c] = true; // Mark current cell
        // --- Process current cell (if needed) ---

        // --- Explore neighbors ---
        int[] dr = {-1, 1, 0, 0}; int[] dc = {0, 0, -1, 1};
        boolean resultFromNeighbors = false;
        for (int i = 0; i < 4; i++) {
             int nr = r + dr[i];
             int nc = c + dc[i];
             // Make recursive call, potentially combine results
             if (gridDfsTemplate(grid, nr, nc, visited /*, params */)) {
                 resultFromNeighbors = true;
                 // Optional: break if only one path/result needed
                 // break;
             }
        }

        // --- Backtrack (Optional) ---
        // visited[r][c] = false; // If state needs reset

        return resultFromNeighbors; // Return combined result
     }

} // End of NeetCodeStandardUtilsV7 class
