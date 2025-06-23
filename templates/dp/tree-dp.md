# Dynamic Programming - Tree Pattern

## 🎯 Pattern Overview

The **Tree DP Pattern** is for optimization problems on tree structures where you need to make decisions at each node. Unlike regular tree traversal, you're optimizing some value (max/min sum, ways to do something) considering the entire subtree.

### Core Concept:
```
Tree structure: Each node has children
Choice at each node: Include/exclude in result
Goal: Optimize value across the tree
Key: Use post-order traversal (process children first)
```

---

## 🔍 Pattern Recognition

### ✅ Quick Recognition Checklist
- [ ] Problem involves a tree structure?
- [ ] Need to optimize (max/min) across tree?
- [ ] Each node contributes to result?
- [ ] Parent decision depends on children?
→ If YES → Tree DP Pattern!

### Magic Keywords:
| Keyword | Example Problem |
|---------|----------------|
| "maximum path sum" in tree | Binary Tree Maximum Path Sum |
| "rob houses" in tree | House Robber III |
| "minimum cost" tree operations | Minimum Cost Tree From Leaf Values |
| "distribute coins" in tree | Distribute Coins in Binary Tree |
| "diameter" of tree | Diameter of Binary Tree |

### Quick Test:
```
✓ Is it a tree problem?
✓ Am I optimizing something?
✓ Do I need info from children to decide parent?
→ Tree DP Pattern! ✅
```

---

## 🚀 Clean Reusable Templates

### Template 1: Path Optimization (Max Path Sum Type)

```java
class Solution {
    private int globalMax;
    
    public int treeMaxPath(TreeNode root) {
        globalMax = Integer.MIN_VALUE;
        dfs(root);
        return globalMax;
    }
    
    private int dfs(TreeNode node) {
        if (node == null) return 0;
        
        // Get the best path sum from children (ignore negative)
        int leftGain = Math.max(0, dfs(node.left));
        int rightGain = Math.max(0, dfs(node.right));
        
        // Path through current node
        int pathThroughNode = node.val + leftGain + rightGain;
        globalMax = Math.max(globalMax, pathThroughNode);
        
        // Return max path that can continue through parent
        return node.val + Math.max(leftGain, rightGain);
    }
}

// Direct usage:
public int maxPathSum(TreeNode root) {
    return treeMaxPath(root);
}
```

---

### Template 2: Tree Metrics (Diameter/Height Type)

```java
class Solution {
    private int globalMetric;
    
    public int treeMetric(TreeNode root) {
        globalMetric = 0;
        calculateDepth(root);
        return globalMetric;
    }
    
    private int calculateDepth(TreeNode node) {
        if (node == null) return 0;
        
        int leftDepth = calculateDepth(node.left);
        int rightDepth = calculateDepth(node.right);
        
        // Update global metric (e.g., diameter = left + right)
        globalMetric = Math.max(globalMetric, leftDepth + rightDepth);
        
        // Return depth for parent's calculation
        return 1 + Math.max(leftDepth, rightDepth);
    }
}

// Direct usage:
public int diameterOfBinaryTree(TreeNode root) {
    return treeMetric(root);
}
```

---

### Template 3: Include/Exclude (Alternating Constraints)

```java
class Solution {
    public int treeAlternating(TreeNode root) {
        int[] result = dfs(root);
        return Math.max(result[0], result[1]);
    }
    
    private int[] dfs(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};  // [exclude, include]
        }
        
        int[] left = dfs(node.left);
        int[] right = dfs(node.right);
        
        // Option 1: Exclude current node
        int exclude = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        
        // Option 2: Include current node (children must be excluded)
        int include = node.val + left[0] + right[0];
        
        return new int[]{exclude, include};
    }
}

// Direct usage:
public int rob(TreeNode root) {
    return treeAlternating(root);
}
```

---

### Template 4: Flow/Balance (Distribution Type)

```java
class Solution {
    private int globalMoves;
    
    public int treeBalance(TreeNode root) {
        globalMoves = 0;
        calculateBalance(root);
        return globalMoves;
    }
    
    private int calculateBalance(TreeNode node) {
        if (node == null) return 0;
        
        int leftBalance = calculateBalance(node.left);
        int rightBalance = calculateBalance(node.right);
        
        // Count moves needed (coins passing through edges)
        globalMoves += Math.abs(leftBalance) + Math.abs(rightBalance);
        
        // Return excess/deficit for parent
        return node.val + leftBalance + rightBalance - 1;
    }
}

// Direct usage:
public int distributeCoins(TreeNode root) {
    return treeBalance(root);
}
```

---

## 📋 Common Problem Mappings

### Which Template to Use?

| Problem Type | Template | Example |
|--------------|----------|---------|
| Path can start/end anywhere | Template 1 | Binary Tree Maximum Path Sum |
| Tree measurement | Template 2 | Diameter of Binary Tree |
| Can't use adjacent nodes | Template 3 | House Robber III |
| Distribute/balance resources | Template 4 | Distribute Coins |

### Direct Usage Examples:

```java
// 1. Maximum Path Sum
public int maxPathSum(TreeNode root) {
    return treeMaxPath(root);  // Template 1
}

// 2. Diameter
public int diameterOfBinaryTree(TreeNode root) {
    return treeMetric(root);  // Template 2
}

// 3. House Robber III
public int rob(TreeNode root) {
    return treeAlternating(root);  // Template 3
}

// 4. Distribute Coins
public int distributeCoins(TreeNode root) {
    return treeBalance(root);  // Template 4
}
```

---

## 💡 Understanding Each Template

### Template 1: Path Optimization
- **Use when**: Path can start/end at any node
- **Global tracks**: Best path found so far
- **Returns**: Best path continuing through parent
- **Key insight**: Consider negative paths as 0

### Template 2: Tree Metrics  
- **Use when**: Measuring tree properties
- **Global tracks**: The metric (diameter, width, etc.)
- **Returns**: Height/depth for parent's use
- **Key insight**: Metric might not go through root

### Template 3: Include/Exclude
- **Use when**: Can't use adjacent nodes
- **No global needed**: Returns both options
- **Returns**: [best without node, best with node]
- **Key insight**: Parent chooses based on what child did

### Template 4: Flow/Balance
- **Use when**: Resources flow through tree
- **Global tracks**: Total moves/cost
- **Returns**: Excess or deficit
- **Key insight**: Count absolute flow through edges

---

## 📊 Key Differences Between Templates

| Aspect | Path | Metric | Include/Exclude | Flow |
|--------|------|--------|-----------------|------|
| Global Variable | Yes (max) | Yes (metric) | No | Yes (moves) |
| Return Type | int | int | int[2] | int |
| Negative Handling | max(0, child) | N/A | N/A | abs(balance) |
| Main Calculation | left + right + val | left + right | alternatives | flow counting |

---

## 🧠 Mental Model

Think of Tree DP as:
> "Post-order traversal where each node makes a decision based on children's results"

For each template:
- **Path**: "What's the best path through me?"
- **Metric**: "What can I measure using my children?"
- **Include/Exclude**: "Should I participate or let children handle it?"
- **Flow**: "How much needs to pass through me?"

---

## 🎯 Quick Reference

### Template Selection Guide:
```
Path sum problems → Template 1
Tree measurements → Template 2  
Alternating constraints → Template 3
Distribution/balance → Template 4
```

### Common Modifications:
- Change `Integer.MIN_VALUE` to `Integer.MAX_VALUE` for minimization
- Change `Math.max` to `Math.min` for minimization
- Add conditions for node values (e.g., same value paths)

### Time & Space:
- All templates: O(n) time, O(h) space
- No extra data structures needed

---

## 📚 Practice Problems (In Order)

### Start Here (One from each template):
1. **[Binary Tree Maximum Path Sum](https://leetcode.com/problems/binary-tree-maximum-path-sum/)** - Template 1
2. **[Diameter of Binary Tree](https://leetcode.com/problems/diameter-of-binary-tree/)** - Template 2
3. **[House Robber III](https://leetcode.com/problems/house-robber-iii/)** - Template 3
4. **[Distribute Coins in Binary Tree](https://leetcode.com/problems/distribute-coins-in-binary-tree/)** - Template 4

### Then Try:
5. **[Longest Univalue Path](https://leetcode.com/problems/longest-univalue-path/)** - Template 2 variant
6. **[Binary Tree Maximum Path Sum II](https://leetcode.com/problems/path-sum-iii/)** - Template 1 variant
7. **[Count Good Nodes](https://leetcode.com/problems/count-good-nodes-in-binary-tree/)** - Modified Template 2

### Advanced:
8. **[Binary Tree Cameras](https://leetcode.com/problems/binary-tree-cameras/)** - Complex states
9. **[Minimum Cost Tree From Leaf Values](https://leetcode.com/problems/minimum-cost-tree-from-leaf-values/)** - Different approach

---

## 🎯 Interview Communication Script

```
"I notice this is a Tree DP problem because:
1. We have a tree structure
2. Need to optimize something across the tree
3. Each node's decision depends on its subtrees

Based on the constraint:
- Path problems → I'll use the path optimization template
- Tree metrics → I'll use the metric calculation template
- Can't use adjacent → I'll use include/exclude template
- Distribution → I'll use the flow/balance template

All templates use post-order traversal to process children first.

Time complexity: O(n), Space complexity: O(h) for recursion."
```

**Master these 4 clean templates and you'll handle 95% of Tree DP problems!**
