import java.util.*;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class NeetCodeProUtils {

    // =========================================================================
    // 0. Common Data Structures
    // =========================================================================

    // TreeNode class for binary tree problems
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
        public TreeNode parent; // Sometimes useful

        public TreeNode() {}
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // ListNode class for linked list problems
    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode prev; // For Doubly Linked List problems
        public ListNode() {}
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    // Graph node class (Adjacency List)
    public static class Node {
        public int val;
        public List<Node> neighbors;
        public Node() {
            val = 0;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val) {
            val = _val;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val, ArrayList<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }

    // Interval class for interval problems
    public static class Interval {
        public int start;
        public int end;
        public Interval() { start = 0; end = 0; }
        public Interval(int s, int e) { start = s; end = e; }

        @Override
        public String toString() {
            return "[" + start + "," + end + "]";
        }
    }

    // TrieNode (Internal for Trie utility)
    private static class TrieNode {
        Map<Character, TrieNode> children;
        boolean isWord;
        public TrieNode() {
            children = new HashMap<>();
            isWord = false;
        }
    }


    // =========================================================================
    // 1. Arrays & Hashing / Two Pointers / Sliding Window
    // =========================================================================

    // Two Pointers: Opposite ends converging (e.g., Two Sum II, Valid Palindrome)
    public static boolean twoPointersConverging(int[] sortedArr, int target) {
        int left = 0;
        int right = sortedArr.length - 1;
        while (left < right) {
            int sum = sortedArr[left] + sortedArr[right];
            if (sum == target) {
                // Found pair: potentially return indices or true
                return true;
            } else if (sum < target) {
                left++; // Need larger sum
            } else {
                right--; // Need smaller sum
            }
        }
        return false; // No pair found
    }

    // Two Pointers: Fast and Slow (e.g., finding duplicates in array treated like list cycle)
    // Example: Find duplicate in [1,3,4,2,2] (numbers in [1, n], array size n+1)
    public static int findDuplicateFloyd(int[] nums) {
         int slow = nums[0];
         int fast = nums[0];
         do {
             slow = nums[slow];
             fast = nums[nums[fast]];
         } while (slow != fast);

         // Find the entrance to the cycle
         slow = nums[0];
         while (slow != fast) {
             slow = nums[slow];
             fast = nums[fast];
         }
         return slow; // or fast
    }


    // Sliding Window: Fixed Size (e.g., Max Sum Subarray of Size K)
    public static int maxSumFixedSizeWindow(int[] nums, int k) {
        int currentSum = 0;
        int maxSum = Integer.MIN_VALUE;
        for(int i = 0; i < nums.length; i++) {
            currentSum += nums[i];
            if (i >= k - 1) {
                maxSum = Math.max(maxSum, currentSum);
                currentSum -= nums[i - (k - 1)]; // Slide window by removing leftmost element
            }
        }
        return maxSum;
    }

    // Sliding Window: Variable Size (e.g., Min Size Subarray Sum >= target)
    public static int minSizeSubarraySum(int[] nums, int target) {
        int minLen = Integer.MAX_VALUE;
        int currentSum = 0;
        int start = 0;
        for (int end = 0; end < nums.length; end++) {
            currentSum += nums[end];
            while (currentSum >= target) {
                minLen = Math.min(minLen, end - start + 1);
                currentSum -= nums[start]; // Shrink window from left
                start++;
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    // Sliding Window: Generic Template (from original utils - good for char counts like Min Window Substring)
     public static int slidingWindowCharCountingTemplate(String s, String t) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : t.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        int counter = map.size(); // Number of unique chars in t we need
        int begin = 0, end = 0;
        int minLen = Integer.MAX_VALUE;
        int resultStart = 0; // To store start index of the result window

        while (end < s.length()) {
            char endChar = s.charAt(end);
            if (map.containsKey(endChar)) {
                map.put(endChar, map.get(endChar) - 1);
                if (map.get(endChar) == 0) counter--; // Met the requirement for this char
            }
            end++;

            while (counter == 0) { // Window is valid
                // Try to shrink the window
                if (end - begin < minLen) {
                    minLen = end - begin;
                    resultStart = begin; // Update result start index
                }

                char beginChar = s.charAt(begin);
                if (map.containsKey(beginChar)) {
                    map.put(beginChar, map.get(beginChar) + 1);
                    if (map.get(beginChar) > 0) counter++; // No longer satisfy this char's requirement
                }
                begin++;
            }
        }
        // Return minLen or the substring s.substring(resultStart, resultStart + minLen)
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    // Frequency Map (Common Hashing Pattern)
    public static Map<Integer, Integer> getFrequencyMap(int[] nums) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        return freqMap;
    }


    // =========================================================================
    // 2. Stack Utilities
    // =========================================================================

    // Monotonic Increasing Stack (e.g., Next Greater Element)
    // Returns map of element -> next greater element, or array of NGEs
    public static Map<Integer, Integer> nextGreaterElement(int[] nums) {
        Map<Integer, Integer> ngeMap = new HashMap<>();
        Stack<Integer> stack = new Stack<>(); // Stores indices or values depending on needs
        for (int i = 0; i < nums.length; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                ngeMap.put(nums[stack.pop()], nums[i]);
            }
            stack.push(i);
        }
        // Elements left in stack have no greater element to their right
        while(!stack.isEmpty()) {
             ngeMap.put(nums[stack.pop()], -1); // Or some indicator
        }
        return ngeMap;
    }

    // Parentheses Matching (e.g., Valid Parentheses)
    public static boolean isValidParentheses(String s) {
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');

        for (char c : s.toCharArray()) {
            if (map.containsValue(c)) { // It's an opening bracket
                stack.push(c);
            } else if (map.containsKey(c)) { // It's a closing bracket
                if (stack.isEmpty() || stack.pop() != map.get(c)) {
                    return false;
                }
            }
            // Ignore other characters if needed
        }
        return stack.isEmpty(); // Stack must be empty at the end
    }


    // =========================================================================
    // 3. Binary Search Utilities
    // =========================================================================

    // Standard Binary Search (from original utils)
    public static int binarySearch(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2; // Avoid overflow
            if (nums[mid] == target) return mid;
            if (nums[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1; // Or return left for insertion point
    }

    // Binary Search on Rotated Array (from original utils)
    public static int binarySearchRotated(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) return mid;

            // Check which half is sorted
            if (nums[left] <= nums[mid]) { // Left half is sorted
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1; // Target is in the sorted left half
                } else {
                    left = mid + 1; // Target is in the right half
                }
            } else { // Right half is sorted
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1; // Target is in the sorted right half
                } else {
                    right = mid - 1; // Target is in the left half
                }
            }
        }
        return -1;
    }

    // Binary Search on Answer Space (Template, e.g., Koko Eating Bananas, Min Gas Stations)
    // Need a function `canAchieve(value)` that returns true if `value` is a possible answer
    public static int binarySearchOnAnswer(int low, int high, /* params for canAchieve */ Object... params) {
         int ans = high; // Initialize with a possible maximum or default
         while (low <= high) {
             int mid = low + (low + high) / 2;
             if (canAchieve(mid, params)) {
                 ans = mid;      // Found a potential answer, try for a better one (smaller/larger depending on problem)
                 high = mid - 1; // Example: Trying to minimize the answer
                 // Or: low = mid + 1; // Example: Trying to maximize the answer
             } else {
                 low = mid + 1;  // Current value 'mid' doesn't work, need a larger one
                 // Or: high = mid - 1; // Need a smaller one
             }
         }
         return ans;
    }
    // Example helper function for the template above (must be implemented per problem)
    private static boolean canAchieve(int value, Object... params) {
        // Implement the logic here based on the problem
        // e.g., Can Koko eat all bananas at speed 'value' within H hours?
        // e.g., Does placing stations with max distance 'value' require K or fewer stations?
        return true; // Placeholder
    }


    // =========================================================================
    // 4. Linked List Utilities
    // =========================================================================

    // Reverse Linked List (from original utils)
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
        return prev;
    }

    // Find Middle of Linked List (from original utils - finds first middle)
    public static ListNode findMiddle(ListNode head) {
        if (head == null) return null;
        ListNode slow = head;
        ListNode fast = head;
        // Careful condition: fast.next != null for first middle, fast != null for second middle
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    // Merge Two Sorted Lists (e.g., Merge Two Sorted Lists, core of Merge Sort)
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
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
        // Append remaining nodes
        current.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    // Detect Cycle (Floyd's Tortoise and Hare)
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;
        ListNode slow = head;
        ListNode fast = head.next; // Start fast one step ahead
        while (fast != null && fast.next != null) {
            if (slow == fast) {
                return true; // Cycle detected
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return false; // No cycle
    }

     // Detect Cycle and return start node (Floyd's + second phase)
    public static ListNode detectCycleStart(ListNode head) {
        if (head == null || head.next == null) return null;
        ListNode slow = head;
        ListNode fast = head;
        boolean hasCycle = false;

        // Phase 1: Detect cycle
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }

        // Phase 2: Find cycle start
        if (!hasCycle) return null;

        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow; // or fast
    }


    // =========================================================================
    // 5. Tree Utilities
    // =========================================================================

    // --- Traversals ---
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        inorderHelper(root, res);
        return res;
    }
    private static void inorderHelper(TreeNode node, List<Integer> res) {
        if (node == null) return;
        inorderHelper(node.left, res);
        res.add(node.val);
        inorderHelper(node.right, res);
    }

    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        preorderHelper(root, res);
        return res;
    }
    private static void preorderHelper(TreeNode node, List<Integer> res) {
        if (node == null) return;
        res.add(node.val);
        preorderHelper(node.left, res);
        preorderHelper(node.right, res);
    }

    public static List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        postorderHelper(root, res);
        return res;
    }
    private static void postorderHelper(TreeNode node, List<Integer> res) {
        if (node == null) return;
        postorderHelper(node.left, res);
        postorderHelper(node.right, res);
        res.add(node.val);
    }

    // Level Order Traversal (BFS - from original utils)
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                TreeNode currentNode = queue.poll();
                currentLevel.add(currentNode.val);
                if (currentNode.left != null) queue.offer(currentNode.left);
                if (currentNode.right != null) queue.offer(currentNode.right);
            }
            result.add(currentLevel);
        }
        return result;
    }

    // --- Common Properties / Algorithms ---

    // Max Depth / Height of a Binary Tree
    public static int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    // Check if two trees are the same
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null) return false;
        if (p.val != q.val) return false;
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    // Check if a tree is a subtree of another
     public static boolean isSubtree(TreeNode root, TreeNode subRoot) {
        if (root == null) return subRoot == null;
        if (isSameTree(root, subRoot)) {
            return true;
        }
        // Check if subRoot is a subtree of left or right child
        return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }

    // Lowest Common Ancestor (LCA) of a Binary Search Tree (BST)
    public static TreeNode lowestCommonAncestorBST(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        if (p.val < root.val && q.val < root.val) {
            return lowestCommonAncestorBST(root.left, p, q);
        } else if (p.val > root.val && q.val > root.val) {
            return lowestCommonAncestorBST(root.right, p, q);
        } else {
            // Found the split point, or one node is the ancestor
            return root;
        }
    }

    // Lowest Common Ancestor (LCA) of a Binary Tree
     public static TreeNode lowestCommonAncestorBT(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root; // Found p or q, or reached the end
        }
        TreeNode left = lowestCommonAncestorBT(root.left, p, q);
        TreeNode right = lowestCommonAncestorBT(root.right, p, q);

        if (left != null && right != null) {
            return root; // p is in one subtree, q is in the other
        }
        return (left != null) ? left : right; // LCA is in the subtree where a node was found
    }

    // Validate Binary Search Tree (BST)
    public static boolean isValidBST(TreeNode root) {
        return isValidBSTHelper(root, null, null);
    }
    private static boolean isValidBSTHelper(TreeNode node, Integer lowerBound, Integer upperBound) {
        if (node == null) return true;
        if (lowerBound != null && node.val <= lowerBound) return false;
        if (upperBound != null && node.val >= upperBound) return false;
        return isValidBSTHelper(node.left, lowerBound, node.val) &&
               isValidBSTHelper(node.right, node.val, upperBound);
    }

    // Serialize and Deserialize Binary Tree (Level Order)
    public static String serialize(TreeNode root) {
        if (root == null) return "null";
        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        sb.append(root.val);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();

            if (node.left != null) {
                sb.append(",").append(node.left.val);
                queue.offer(node.left);
            } else {
                sb.append(",null");
            }

            if (node.right != null) {
                sb.append(",").append(node.right.val);
                queue.offer(node.right);
            } else {
                sb.append(",null");
            }
        }
        // Could optimize by removing trailing nulls, but this is simpler for matching deserialize
        return sb.toString();
    }

    public static TreeNode deserialize(String data) {
        if (data == null || data.equals("null") || data.isEmpty()) return null;
        String[] values = data.split(",");
        TreeNode root = new TreeNode(Integer.parseInt(values[0]));
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;

        while (!queue.isEmpty() && i < values.length) {
            TreeNode node = queue.poll();

            // Process left child
            if (!values[i].equals("null")) {
                TreeNode leftChild = new TreeNode(Integer.parseInt(values[i]));
                node.left = leftChild;
                queue.offer(leftChild);
            }
            i++;

            if (i >= values.length) break;

            // Process right child
            if (!values[i].equals("null")) {
                TreeNode rightChild = new TreeNode(Integer.parseInt(values[i]));
                node.right = rightChild;
                queue.offer(rightChild);
            }
            i++;
        }
        return root;
    }


    // =========================================================================
    // 6. Trie Utilities
    // =========================================================================
    public static class Trie {
        private TrieNode root;
        public Trie() {
            root = new TrieNode();
        }

        // Insert a word into the trie
        public void insert(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
            }
            node.isWord = true;
        }

        // Search for a word in the trie
        public boolean search(String word) {
            TrieNode node = findNode(word);
            return node != null && node.isWord;
        }

        // Check if there is any word in the trie that starts with the given prefix
        public boolean startsWith(String prefix) {
            return findNode(prefix) != null;
        }

        // Helper to find the node corresponding to the end of a string
        private TrieNode findNode(String str) {
             TrieNode node = root;
            for (char c : str.toCharArray()) {
                if (!node.children.containsKey(c)) return null;
                node = node.children.get(c);
            }
            return node;
        }
        
        // Add search with '.' wildcard (for Add and Search Word DS problem)
        public boolean searchWithWildcard(String word) {
            return searchRecursive(word, 0, root);
        }

        private boolean searchRecursive(String word, int index, TrieNode node) {
            if (node == null) return false;
            if (index == word.length()) {
                return node.isWord;
            }

            char c = word.charAt(index);

            if (c == '.') {
                for (TrieNode child : node.children.values()) {
                    if (searchRecursive(word, index + 1, child)) {
                        return true;
                    }
                }
                return false; // No child path worked for wildcard
            } else {
                return searchRecursive(word, index + 1, node.children.get(c));
            }
        }
    }


    // =========================================================================
    // 7. Heap / Priority Queue Utilities
    // =========================================================================

    // Find Kth Largest Element (from original utils - uses MinHeap)
    public static int findKthLargest(int[] nums, int k) {
        // Min-heap stores the K largest elements seen so far
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k); // Or PriorityQueue<>(Comparator.naturalOrder());
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll(); // Remove the smallest of the k+1 elements
            }
        }
        return minHeap.peek(); // Root is the Kth largest
    }

    // Find Kth Smallest Element (uses MaxHeap)
     public static int findKthSmallest(int[] nums, int k) {
        // Max-heap stores the K smallest elements seen so far
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(k, Comparator.reverseOrder());
        for (int num : nums) {
            maxHeap.offer(num);
            if (maxHeap.size() > k) {
                maxHeap.poll(); // Remove the largest of the k+1 elements
            }
        }
        return maxHeap.peek(); // Root is the Kth smallest
    }

    // Median Finder Data Structure (Template for Find Median from Data Stream)
    public static class MedianFinder {
        private PriorityQueue<Integer> minHeap; // Stores the larger half
        private PriorityQueue<Integer> maxHeap; // Stores the smaller half

        public MedianFinder() {
            minHeap = new PriorityQueue<>();                             // Natural order
            maxHeap = new PriorityQueue<>(Comparator.reverseOrder());    // Reverse order
        }

        public void addNum(int num) {
            maxHeap.offer(num);              // Add to maxHeap first
            minHeap.offer(maxHeap.poll());   // Balance by moving largest from maxHeap to minHeap
            
            // Ensure maxHeap size >= minHeap size
            if (maxHeap.size() < minHeap.size()) {
                maxHeap.offer(minHeap.poll()); // Balance by moving smallest from minHeap to maxHeap
            }
        }

        public double findMedian() {
            if (maxHeap.size() > minHeap.size()) {
                return maxHeap.peek(); // Odd number of elements
            } else {
                // Even number of elements
                return (maxHeap.peek() + minHeap.peek()) / 2.0;
            }
        }
    }


    // =========================================================================
    // 8. Backtracking Utilities
    // =========================================================================

    // Subsets (from original utils)
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackSubsets(result, new ArrayList<>(), nums, 0);
        return result;
    }
    private static void backtrackSubsets(List<List<Integer>> result, List<Integer> tempList, int[] nums, int start) {
        result.add(new ArrayList<>(tempList)); // Add the current subset
        for (int i = start; i < nums.length; i++) {
            tempList.add(nums[i]);                   // Include nums[i]
            backtrackSubsets(result, tempList, nums, i + 1); // Recurse
            tempList.remove(tempList.size() - 1); // Backtrack: Remove nums[i]
        }
    }

    // Subsets II (Handles Duplicates)
    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums); // Sort to handle duplicates easily
        List<List<Integer>> result = new ArrayList<>();
        backtrackSubsetsWithDup(result, new ArrayList<>(), nums, 0);
        return result;
    }
    private static void backtrackSubsetsWithDup(List<List<Integer>> result, List<Integer> tempList, int[] nums, int start) {
        result.add(new ArrayList<>(tempList));
        for (int i = start; i < nums.length; i++) {
             // Skip duplicates: if current element is same as previous, and we didn't choose previous (i > start), skip it
            if (i > start && nums[i] == nums[i - 1]) continue;
            tempList.add(nums[i]);
            backtrackSubsetsWithDup(result, tempList, nums, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

    // Combinations (Choose k elements from n)
    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackCombine(result, new ArrayList<>(), n, k, 1);
        return result;
    }
    private static void backtrackCombine(List<List<Integer>> result, List<Integer> tempList, int n, int k, int start) {
        if (tempList.size() == k) {
            result.add(new ArrayList<>(tempList));
            return;
        }
        // Optimization: if remaining elements needed > remaining elements available, prune
        if (k - tempList.size() > n - start + 1) {
             return;
        }

        for (int i = start; i <= n; i++) {
            tempList.add(i);
            backtrackCombine(result, tempList, n, k, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

    // Combination Sum (Numbers can be reused)
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates); // Optional but helps pruning
        backtrackCombinationSum(result, new ArrayList<>(), candidates, target, 0);
        return result;
    }
    private static void backtrackCombinationSum(List<List<Integer>> result, List<Integer> tempList, int[] candidates, int remain, int start) {
        if (remain < 0) return; // Target exceeded
        if (remain == 0) {
            result.add(new ArrayList<>(tempList)); // Found a combination
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            // Optimization: if candidates[i] > remain, no need to check further (if sorted)
            if (candidates[i] > remain) break;
            tempList.add(candidates[i]);
            // Pass 'i' (not i+1) because we can reuse the same number
            backtrackCombinationSum(result, tempList, candidates, remain - candidates[i], i);
            tempList.remove(tempList.size() - 1);
        }
    }

    // Permutations (All unique permutations)
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackPermute(result, new ArrayList<>(), nums, new boolean[nums.length]);
        return result;
    }
    private static void backtrackPermute(List<List<Integer>> result, List<Integer> tempList, int[] nums, boolean[] used) {
        if (tempList.size() == nums.length) {
            result.add(new ArrayList<>(tempList));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue; // Skip if element already used in this permutation
            used[i] = true;
            tempList.add(nums[i]);
            backtrackPermute(result, tempList, nums, used);
            tempList.remove(tempList.size() - 1);
            used[i] = false; // Backtrack
        }
    }

     // Permutations II (Handles duplicate numbers)
    public static List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums); // Sort to handle duplicates
        backtrackPermuteUnique(result, new ArrayList<>(), nums, new boolean[nums.length]);
        return result;
    }
    private static void backtrackPermuteUnique(List<List<Integer>> result, List<Integer> tempList, int[] nums, boolean[] used) {
        if (tempList.size() == nums.length) {
            result.add(new ArrayList<>(tempList));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
             // Skip duplicate permutations: if current num is same as prev num,
             // and prev num was *not* used (meaning it's part of a different branch), skip
            if (i > 0 && nums[i] == nums[i-1] && !used[i-1]) continue;

            used[i] = true;
            tempList.add(nums[i]);
            backtrackPermuteUnique(result, tempList, nums, used);
            tempList.remove(tempList.size() - 1);
            used[i] = false;
        }
    }

    // Word Search (Board, Word -> boolean) - Template Structure
    public static boolean exist(char[][] board, String word) {
        if (board == null || board.length == 0 || board[0].length == 0 || word == null || word.isEmpty()) {
            return false;
        }
        int rows = board.length;
        int cols = board[0].length;
        boolean[][] visited = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == word.charAt(0)) {
                     if (searchWordDFS(board, word, i, j, 0, visited)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private static boolean searchWordDFS(char[][] board, String word, int r, int c, int index, boolean[][] visited) {
        // Base Case: Found the whole word
        if (index == word.length()) {
            return true;
        }

        // Boundary checks, visited check, character match check
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length ||
            visited[r][c] || board[r][c] != word.charAt(index)) {
            return false;
        }

        // Mark as visited
        visited[r][c] = true;

        // Explore neighbors (Up, Down, Left, Right)
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        boolean found = false;
        for(int i=0; i<4; i++){
             if(searchWordDFS(board, word, r + dr[i], c + dc[i], index + 1, visited)){
                  found = true;
                  break; // Optimization: stop if found in one direction
             }
        }


        // Backtrack
        visited[r][c] = false;

        return found;
    }


    // =========================================================================
    // 9. Graph Utilities (using Adjacency List 'Node' or Adjacency Matrix int[][])
    // =========================================================================

    // --- Standard Traversals ---

    // DFS Traversal (Recursive - for visiting nodes)
    public static void dfsTraversal(Node startNode, Set<Node> visited) {
        if (startNode == null || visited.contains(startNode)) {
            return;
        }
        visited.add(startNode);
        System.out.print(startNode.val + " "); // Process node (Example: print)

        for (Node neighbor : startNode.neighbors) {
            dfsTraversal(neighbor, visited);
        }
    }
    // Overload for Adjacency Matrix representation (int graph[][], int startVertex)
    public static void dfsTraversalMatrix(int[][] graph, int u, boolean[] visited) {
        visited[u] = true;
        System.out.print(u + " "); // Process node

        for (int v = 0; v < graph.length; v++) {
             // Check graph[u][v] == 1 for unweighted, or > 0 for weighted
            if (graph[u][v] > 0 && !visited[v]) {
                dfsTraversalMatrix(graph, v, visited);
            }
        }
    }


    // BFS Traversal (Iterative - for visiting nodes, shortest path unweighted)
    public static void bfsTraversal(Node startNode) {
        if (startNode == null) return;
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();

        queue.offer(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            System.out.print(current.val + " "); // Process node

            for (Node neighbor : current.neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
    }
    // Overload for Adjacency Matrix
     public static void bfsTraversalMatrix(int[][] graph, int startVertex) {
        int numVertices = graph.length;
        boolean[] visited = new boolean[numVertices];
        Queue<Integer> queue = new LinkedList<>();

        visited[startVertex] = true;
        queue.offer(startVertex);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            System.out.print(u + " "); // Process node

            for (int v = 0; v < numVertices; v++) {
                 // Check graph[u][v] == 1 for unweighted, or > 0 for weighted
                if (graph[u][v] > 0 && !visited[v]) {
                    visited[v] = true;
                    queue.offer(v);
                }
            }
        }
    }


    // --- Graph Algorithms ---

    // Clone Graph (from original utils - DFS version)
    public static Node cloneGraphDFS(Node node) {
        if (node == null) return null;
        Map<Node, Node> visited = new HashMap<>(); // Map original node -> cloned node
        return cloneGraphDFSHelper(node, visited);
    }
    private static Node cloneGraphDFSHelper(Node node, Map<Node, Node> visited) {
        if (visited.containsKey(node)) {
            return visited.get(node); // Return already cloned node
        }
        // Clone the node
        Node clone = new Node(node.val);
        visited.put(node, clone);

        // Clone neighbors
        for (Node neighbor : node.neighbors) {
            clone.neighbors.add(cloneGraphDFSHelper(neighbor, visited));
        }
        return clone;
    }

    // Clone Graph (BFS version - provided in original util needs slight modification for stand-alone)
     public static Node cloneGraphBFS(Node node) {
        if (node == null) return null;

        Map<Node, Node> map = new HashMap<>(); // Map original node -> cloned node
        Queue<Node> queue = new LinkedList<>();

        // Clone the start node and add to queue and map
        Node clonedStartNode = new Node(node.val);
        map.put(node, clonedStartNode);
        queue.offer(node);

        while (!queue.isEmpty()) {
            Node currentOriginal = queue.poll();
            Node currentCloned = map.get(currentOriginal);

            for (Node neighborOriginal : currentOriginal.neighbors) {
                if (!map.containsKey(neighborOriginal)) {
                    // Clone neighbor if not already cloned
                    Node clonedNeighbor = new Node(neighborOriginal.val);
                    map.put(neighborOriginal, clonedNeighbor);
                    queue.offer(neighborOriginal); // Add original neighbor to queue for processing its neighbors later
                }
                // Add the cloned neighbor to the cloned current node's neighbors list
                currentCloned.neighbors.add(map.get(neighborOriginal));
            }
        }
        return clonedStartNode;
    }

    // Topological Sort (Kahn's Algorithm - BFS based)
    // Requires graph represented as adjacency list Map<Integer, List<Integer>> and in-degrees Map<Integer, Integer>
    public static List<Integer> topologicalSort(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> adj = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();

        // Initialize adjacency list and in-degree map for all courses
        for (int i = 0; i < numCourses; i++) {
            adj.put(i, new ArrayList<>());
            inDegree.put(i, 0);
        }

        // Build graph and calculate in-degrees
        for (int[] prereq : prerequisites) {
            int course = prereq[0];
            int prerequisite = prereq[1];
            adj.get(prerequisite).add(course); // Edge from prereq to course
            inDegree.put(course, inDegree.get(course) + 1);
        }

        // Initialize queue with nodes having 0 in-degree
        Queue<Integer> queue = new LinkedList<>();
        for (int course : inDegree.keySet()) {
            if (inDegree.get(course) == 0) {
                queue.offer(course);
            }
        }

        List<Integer> sortedOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int course = queue.poll();
            sortedOrder.add(course);

            // Process neighbors
            if (adj.containsKey(course)) {
                for (int neighbor : adj.get(course)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }

        // Check if topological sort is possible (cycle detection)
        if (sortedOrder.size() == numCourses) {
            return sortedOrder;
        } else {
            return new ArrayList<>(); // Cycle detected
        }
    }

    // Dijkstra's Algorithm (Shortest Path in Weighted Graph)
    // Requires graph represented as Map<Integer, List<Pair<Integer, Integer>>> where Pair is <neighbor, weight>
    // Returns Map<Integer, Integer> distances from source
     public static Map<Integer, Integer> dijkstra(Map<Integer, List<Pair<Integer, Integer>>> adj, int startNode, int numNodes) {
        Map<Integer, Integer> distances = new HashMap<>();
        for(int i=0; i<numNodes; i++) distances.put(i, Integer.MAX_VALUE); // Initialize distances
        distances.put(startNode, 0);

        // Priority Queue stores Pair<distance, node>, ordered by distance
        PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(Comparator.comparingInt(Pair::getKey));
        pq.offer(new Pair<>(0, startNode));

        while (!pq.isEmpty()) {
            Pair<Integer, Integer> currentPair = pq.poll();
            int d = currentPair.getKey();
            int u = currentPair.getValue();

            // If we found a shorter path already, skip
            if (d > distances.get(u)) {
                continue;
            }

            // Explore neighbors
            if (adj.containsKey(u)) {
                for (Pair<Integer, Integer> edge : adj.get(u)) {
                    int v = edge.getKey();
                    int weight = edge.getValue();
                    if (distances.get(u) != Integer.MAX_VALUE && distances.get(u) + weight < distances.get(v)) {
                        distances.put(v, distances.get(u) + weight);
                        pq.offer(new Pair<>(distances.get(v), v));
                    }
                }
            }
        }
        return distances;
    }
    // Simple Pair class for Dijkstra
    public static class Pair<K, V> {
        private K key; private V value;
        public Pair(K key, V value) { this.key = key; this.value = value; }
        public K getKey() { return key; }
        public V getValue() { return value; }
    }


    // Union-Find (Disjoint Set Union) - (from original utils)
    public static class UnionFind {
        private int[] parent;
        private int[] rank; // or size
        private int count;    // Optional: number of disjoint sets

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            count = size;
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 1; // Initialize rank to 1 (or 0 depending on convention)
            }
        }

        // Find with Path Compression
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        // Union by Rank
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX] += 1; // Increase rank only when ranks are equal
                }
                count--; // Decrement set count
                return true; // Union performed
            }
            return false; // Already in the same set
        }
        public int getCount() { // Get number of disjoint sets
             return count;
        }
         public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }


    // =========================================================================
    // 10. Dynamic Programming (DP) Templates
    // =========================================================================

    // --- 1D DP ---

    // Climbing Stairs / Fibonacci (from original utils)
    public static int climbStairs(int n) {
        if (n <= 1) return 1;
        int[] dp = new int[n + 1];
        dp[0] = 1; // Base case adjustment if needed
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
    // Optimization: Using only two variables instead of array
    public static int climbStairsOptimized(int n) {
        if (n <= 1) return 1;
        int prev1 = 1; // dp[i-1]
        int prev2 = 1; // dp[i-2]
        for (int i = 2; i <= n; i++) {
            int current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        return prev1; // Final result is in prev1
    }

    // House Robber Pattern (Cannot rob adjacent houses)
    public static int rob(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];

        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);

        for (int i = 2; i < n; i++) {
            // Max of: (robbing current + max from i-2) OR (not robbing current = max from i-1)
            dp[i] = Math.max(nums[i] + dp[i - 2], dp[i - 1]);
        }
        return dp[n - 1];
    }
     // Optimization: Using two variables
     public static int robOptimized(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int robPrev = 0; // Max loot ending at i-1 (equivalent to dp[i-1])
        int notRobPrev = 0; // Max loot ending at i-2 (equivalent to dp[i-2])

        for (int num : nums) {
            // Current max is either:
            // 1. Rob current: num + max loot ending at i-2 (notRobPrev)
            // 2. Don't rob current: max loot ending at i-1 (robPrev)
            int currentMax = Math.max(num + notRobPrev, robPrev);
            notRobPrev = robPrev;    // Update for next iteration: dp[i-2] becomes dp[i-1]
            robPrev = currentMax; // Update for next iteration: dp[i-1] becomes dp[i]
        }
        return robPrev; // Final result
    }


    // Longest Increasing Subsequence (LIS) - O(n^2) DP
    public static int lengthOfLIS_DP(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int n = nums.length;
        int[] dp = new int[n]; // dp[i] = length of LIS ending at index i
        Arrays.fill(dp, 1); // Minimum LIS length is 1 (the element itself)
        int maxLIS = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLIS = Math.max(maxLIS, dp[i]);
        }
        return maxLIS;
    }
    // LIS - O(n log n) using Patience Sorting / Binary Search
    public static int lengthOfLIS_Optimized(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        // 'tails' array stores the smallest tail of all increasing subsequences of length i+1
        List<Integer> tails = new ArrayList<>();
        tails.add(nums[0]);

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > tails.get(tails.size() - 1)) {
                // If nums[i] is greater than all tails, extend the longest subsequence
                tails.add(nums[i]);
            } else {
                // Find the smallest tail >= nums[i] and replace it
                // Binary search to find the insertion point (first element >= nums[i])
                int left = 0, right = tails.size() - 1;
                while (left <= right) {
                    int mid = left + (right - left) / 2;
                    if (tails.get(mid) < nums[i]) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
                // 'left' is the index to replace
                tails.set(left, nums[i]);
            }
        }
        return tails.size(); // The length of the tails list is the length of LIS
    }

    // Coin Change (Min coins to make amount)
    public static int coinChange(int[] coins, int amount) {
        if (amount < 0) return -1;
        if (amount == 0) return 0;
        int[] dp = new int[amount + 1]; // dp[i] = min coins for amount i
        Arrays.fill(dp, amount + 1); // Initialize with a value larger than max possible coins
        dp[0] = 0; // Base case: 0 coins for amount 0

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i >= coin) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // --- 2D DP ---

    // Unique Paths (Grid Traversal)
    public static int uniquePaths(int m, int n) { // m rows, n cols
        if (m <= 0 || n <= 0) return 0;
        int[][] dp = new int[m][n]; // dp[i][j] = number of paths to reach (i, j)

        // Base cases: Fill first row and first column with 1s
        for (int i = 0; i < m; i++) dp[i][0] = 1;
        for (int j = 0; j < n; j++) dp[0][j] = 1;

        // Fill the rest of the grid
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1]; // Can come from top or left
            }
        }
        return dp[m - 1][n - 1];
    }
     // Optimization: Using 1D DP array
    public static int uniquePathsOptimized(int m, int n) {
        if (m <= 0 || n <= 0) return 0;
        int[] dp = new int[n]; // Represents one row
        Arrays.fill(dp, 1); // Initialize first row

        for (int i = 1; i < m; i++) { // Iterate through rows (excluding first)
            for (int j = 1; j < n; j++) { // Iterate through columns (excluding first)
                 // dp[j] = dp[j] (from top) + dp[j-1] (from left)
                 // Note: dp[j] already holds the value from the previous row (top)
                 // dp[j-1] holds the updated value from the current row (left)
                dp[j] = dp[j] + dp[j-1];
            }
        }
        return dp[n-1];
    }

    // Longest Common Subsequence (LCS)
     public static int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1]; // dp[i][j] = LCS of text1[0..i-1] and text2[0..j-1]

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = 1 + dp[i - 1][j - 1]; // Characters match, extend LCS
                } else {
                    // Characters don't match, take max from excluding one char from either string
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }


    // =========================================================================
    // 11. Greedy Utilities / Interval Problems
    // =========================================================================

    // Merge Intervals
    public static List<Interval> mergeIntervals(List<Interval> intervals) {
        if (intervals == null || intervals.size() <= 1) {
            return intervals;
        }

        // Sort intervals by start time
        intervals.sort(Comparator.comparingInt(i -> i.start));

        List<Interval> merged = new LinkedList<>();
        Interval currentInterval = intervals.get(0);
        merged.add(currentInterval);

        for (int i = 1; i < intervals.size(); i++) {
            Interval nextInterval = intervals.get(i);
            // Check for overlap: next start <= current end
            if (nextInterval.start <= currentInterval.end) {
                // Merge: update current interval's end to max of current end and next end
                currentInterval.end = Math.max(currentInterval.end, nextInterval.end);
            } else {
                // No overlap, add next interval as the new current interval
                currentInterval = nextInterval;
                merged.add(currentInterval);
            }
        }
        return merged;
    }
    // Overload using int[][] if needed
     public static int[][] mergeIntervalsArray(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        List<int[]> mergedList = new LinkedList<>();
        int[] currentInterval = intervals[0];
        mergedList.add(currentInterval);

        for (int i = 1; i < intervals.length; i++) {
            int[] nextInterval = intervals[i];
            if (nextInterval[0] <= currentInterval[1]) {
                currentInterval[1] = Math.max(currentInterval[1], nextInterval[1]);
            } else {
                currentInterval = nextInterval;
                mergedList.add(currentInterval);
            }
        }
        return mergedList.toArray(new int[mergedList.size()][]);
    }

    // Insert Interval
    public static List<Interval> insertInterval(List<Interval> intervals, Interval newInterval) {
        List<Interval> result = new ArrayList<>();
        int i = 0;
        int n = intervals.size();

        // 1. Add all intervals ending before newInterval starts
        while (i < n && intervals.get(i).end < newInterval.start) {
            result.add(intervals.get(i));
            i++;
        }

        // 2. Merge overlapping intervals
        // Merge newInterval with overlapping intervals
        while (i < n && intervals.get(i).start <= newInterval.end) {
            newInterval.start = Math.min(newInterval.start, intervals.get(i).start);
            newInterval.end = Math.max(newInterval.end, intervals.get(i).end);
            i++;
        }
        result.add(newInterval); // Add the merged or original newInterval

        // 3. Add remaining intervals
        while (i < n) {
            result.add(intervals.get(i));
            i++;
        }
        return result;
    }

    // Non-overlapping Intervals (Find min removals to make intervals non-overlapping)
    // Greedy approach: Sort by end time. Keep track of the end of the last selected interval.
     public static int eraseOverlapIntervals(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return 0;

        // Sort by end times
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));

        int nonOverlapCount = 1; // First interval is always included initially
        int lastEnd = intervals[0][1];
        int removals = 0;

        for (int i = 1; i < intervals.length; i++) {
            // If the current interval's start is after or equal to the last included interval's end
            if (intervals[i][0] >= lastEnd) {
                // Include this interval
                nonOverlapCount++;
                lastEnd = intervals[i][1];
            } else {
                // Overlap detected, remove the current interval (since we sorted by end,
                // keeping the one that ends earlier is greedily better)
                removals++;
            }
        }
        // Return total intervals - non-overlapping count, or directly count removals
        // return intervals.length - nonOverlapCount;
        return removals;
    }

    // =========================================================================
    // 12. Math & Geometry / Bit Manipulation
    // =========================================================================

    // Check if Power of Two
    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    // Count Set Bits (Hamming Weight)
    public static int hammingWeight(int n) {
        int count = 0;
        while (n != 0) {
            n &= (n - 1); // Brian Kernighan's algorithm: clears the least significant set bit
            count++;
        }
        return count;
        // Alternative: Integer.bitCount(n);
    }

     // Reverse Bits
    public static int reverseBits(int n) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            result <<= 1;      // Shift result left
            if ((n & 1) == 1) { // Check the least significant bit of n
                result |= 1;   // Set the least significant bit of result
            }
            n >>>= 1;         // Shift n right (unsigned)
        }
        return result;
    }

     // Single Number (Array where every element appears twice except one)
    public static int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num; // XOR cancels out pairs
        }
        return result;
    }

    // Rotate Image (Matrix Rotation 90 degrees clockwise)
    public static void rotateMatrix(int[][] matrix) {
        int n = matrix.length;
        if (n == 0) return;

        // 1. Transpose the matrix (rows become columns)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) { // Only iterate upper/lower triangle
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        // 2. Reverse each row
        for (int i = 0; i < n; i++) {
            int left = 0, right = n - 1;
            while (left < right) {
                int temp = matrix[i][left];
                matrix[i][left] = matrix[i][right];
                matrix[i][right] = temp;
                left++;
                right--;
            }
        }
    }

    // Set Matrix Zeroes (If an element is 0, set its entire row and col to 0)
     public static void setZeroes(int[][] matrix) {
        int rows = matrix.length;
        if (rows == 0) return;
        int cols = matrix[0].length;
        if (cols == 0) return;

        boolean firstRowZero = false;
        boolean firstColZero = false;

        // 1. Check if first row/col need to be zeroed
        for(int j=0; j<cols; j++) if(matrix[0][j] == 0) firstRowZero = true;
        for(int i=0; i<rows; i++) if(matrix[i][0] == 0) firstColZero = true;

        // 2. Use first row/col as markers for other rows/cols
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0; // Mark row i
                    matrix[0][j] = 0; // Mark col j
                }
            }
        }

        // 3. Zero out rows/cols based on markers (skip first row/col)
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }

        // 4. Zero out first row/col if needed
        if (firstRowZero) for(int j=0; j<cols; j++) matrix[0][j] = 0;
        if (firstColZero) for(int i=0; i<rows; i++) matrix[i][0] = 0;
    }

} // End of NeetCodeProUtils class
