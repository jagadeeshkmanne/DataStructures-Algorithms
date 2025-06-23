# Dynamic Programming - Interval Pattern

## 🎯 Pattern Overview

The **Interval DP Pattern** is for problems where you need to find optimal solutions for intervals/ranges. You typically build solutions for larger intervals from smaller ones. The key insight is that the optimal solution for an interval [i,j] depends on splitting it at some point k.

### Core Concept:
```
Problem involves: Array or sequence
Goal: Optimize over intervals/subarrays
Key insight: Try all possible ways to split the interval
Formula: dp[i][j] = optimal way to handle interval from i to j
```

---

## 🔍 Pattern Recognition

### ✅ Quick Recognition Checklist
- [ ] Need to find optimal way to split/merge array?
- [ ] Problem mentions intervals or ranges?
- [ ] Can divide problem into subproblems on smaller ranges?
- [ ] Order of operations matters?
→ If YES → Interval DP Pattern!

### Magic Keywords:
| Keyword | Example Problem |
|---------|----------------|
| "burst", "remove" with neighbors affected | Burst Balloons |
| "matrix chain multiplication" | Matrix Chain Multiplication |
| "optimal way to split" | Minimum Cost to Merge Stones |
| "triangulation" | Minimum Score Triangulation |
| "remove boxes" | Remove Boxes |

### Quick Test:
```
✓ Do I need to consider all subarrays?
✓ Does the order of operations matter?
✓ Can I split the problem at different points?
→ Interval DP Pattern! ✅
```

---

## 🚀 Clean Reusable Templates

### Template 1: Classic Interval DP (Gap Strategy)

```java
class Solution {
    public int intervalDP(int[] nums) {
        int n = nums.length;
        int[][] dp = new int[n][n];
        
        // Process by increasing interval length
        for (int length = 1; length <= n; length++) {
            processIntervalsOfLength(nums, dp, length);
        }
        
        return dp[0][n-1];
    }
    
    private void processIntervalsOfLength(int[] nums, int[][] dp, int length) {
        int n = nums.length;
        
        for (int i = 0; i <= n - length; i++) {
            int j = i + length - 1;
            
            if (length == 1) {
                dp[i][j] = handleBaseCase(nums, i);
            } else {
                dp[i][j] = findOptimalSplit(nums, dp, i, j);
            }
        }
    }
    
    private int handleBaseCase(int[] nums, int i) {
        // Problem-specific base case
        return 0;  // Often 0 for single element
    }
    
    private int findOptimalSplit(int[] nums, int[][] dp, int i, int j) {
        int optimal = Integer.MAX_VALUE;  // or MIN_VALUE for maximization
        
        // Try all possible split points
        for (int k = i; k < j; k++) {
            int cost = calculateSplitCost(nums, dp, i, j, k);
            optimal = Math.min(optimal, cost);  // or max for maximization
        }
        
        return optimal;
    }
    
    private int calculateSplitCost(int[] nums, int[][] dp, int i, int j, int k) {
        int leftCost = dp[i][k];
        int rightCost = dp[k+1][j];
        int mergeCost = getMergeCost(nums, i, j, k);
        
        return leftCost + rightCost + mergeCost;
    }
    
    private int getMergeCost(int[] nums, int i, int j, int k) {
        // Problem-specific merge cost
        return 0;
    }
}
```

---

### Template 2: Burst Balloons Type (Add Padding)

```java
class Solution {
    private int[][] memo;
    
    public int burstBalloons(int[] nums) {
        // Add padding to handle boundaries
        int[] balloons = addPadding(nums);
        int n = balloons.length;
        memo = new int[n][n];
        
        // Solve for the entire range (excluding padding)
        return findMaxCoins(balloons, 1, n - 2);
    }
    
    private int[] addPadding(int[] nums) {
        int n = nums.length;
        int[] padded = new int[n + 2];
        
        padded[0] = 1;  // Left boundary
        padded[n + 1] = 1;  // Right boundary
        
        for (int i = 0; i < n; i++) {
            padded[i + 1] = nums[i];
        }
        
        return padded;
    }
    
    private int findMaxCoins(int[] balloons, int left, int right) {
        if (outOfRange(left, right)) {
            return 0;
        }
        
        if (alreadyComputed(left, right)) {
            return memo[left][right];
        }
        
        int maxCoins = tryAllLastBalloons(balloons, left, right);
        cacheResult(left, right, maxCoins);
        
        return maxCoins;
    }
    
    private int tryAllLastBalloons(int[] balloons, int left, int right) {
        int maxCoins = 0;
        
        // Try each balloon as the last to burst in this range
        for (int lastBurst = left; lastBurst <= right; lastBurst++) {
            int coins = calculateCoinsForLastBurst(balloons, left, right, lastBurst);
            maxCoins = Math.max(maxCoins, coins);
        }
        
        return maxCoins;
    }
    
    private int calculateCoinsForLastBurst(int[] balloons, int left, int right, int lastBurst) {
        // Coins from bursting lastBurst with only boundaries remaining
        int coinsFromLast = balloons[left - 1] * balloons[lastBurst] * balloons[right + 1];
        
        // Coins from bursting all balloons to the left and right first
        int leftCoins = findMaxCoins(balloons, left, lastBurst - 1);
        int rightCoins = findMaxCoins(balloons, lastBurst + 1, right);
        
        return coinsFromLast + leftCoins + rightCoins;
    }
    
    private boolean outOfRange(int left, int right) {
        return left > right;
    }
    
    private boolean alreadyComputed(int left, int right) {
        return memo[left][right] != 0;
    }
    
    private void cacheResult(int left, int right, int result) {
        memo[left][right] = result;
    }
}

// Direct usage:
public int maxCoins(int[] nums) {
    return burstBalloons(nums);
}
```

---

### Template 3: Matrix Chain Type (Adjacent Operations)

```java
class Solution {
    public int matrixChainOrder(int[] dimensions) {
        int n = dimensions.length - 1;  // n matrices
        int[][] dp = new int[n][n];
        
        // No cost for single matrix
        // dp[i][i] = 0 (already initialized)
        
        // Process by chain length
        for (int chainLength = 2; chainLength <= n; chainLength++) {
            processChainLength(dimensions, dp, chainLength);
        }
        
        return dp[0][n-1];
    }
    
    private void processChainLength(int[] dims, int[][] dp, int length) {
        int n = dp.length;
        
        for (int i = 0; i <= n - length; i++) {
            int j = i + length - 1;
            dp[i][j] = findMinMultiplications(dims, dp, i, j);
        }
    }
    
    private int findMinMultiplications(int[] dims, int[][] dp, int i, int j) {
        int minCost = Integer.MAX_VALUE;
        
        // Try all positions to split the chain
        for (int splitPoint = i; splitPoint < j; splitPoint++) {
            int cost = calculateCostWithSplit(dims, dp, i, j, splitPoint);
            minCost = Math.min(minCost, cost);
        }
        
        return minCost;
    }
    
    private int calculateCostWithSplit(int[] dims, int[][] dp, int i, int j, int k) {
        // Cost to multiply chains (i..k) and (k+1..j)
        int leftChainCost = dp[i][k];
        int rightChainCost = dp[k+1][j];
        
        // Cost to multiply the two resulting matrices
        int multiplicationCost = getMultiplicationCost(dims, i, j, k);
        
        return leftChainCost + rightChainCost + multiplicationCost;
    }
    
    private int getMultiplicationCost(int[] dims, int i, int j, int k) {
        // Dimensions: (i..k) is dims[i] x dims[k+1]
        //            (k+1..j) is dims[k+1] x dims[j+1]
        // Result: dims[i] x dims[j+1]
        return dims[i] * dims[k+1] * dims[j+1];
    }
}
```

---

## 📋 Common Problem Types

### 1. **Burst Balloons**
Maximize coins by bursting balloons in optimal order.

```java
public int maxCoins(int[] nums) {
    return burstBalloons(nums);  // Use Template 2
}
```

**LeetCode Problems:**
- [Burst Balloons](https://leetcode.com/problems/burst-balloons/)

---

### 2. **Matrix Chain Multiplication**
Minimize multiplications to compute matrix product.

```java
public int matrixMultiplication(int[] p) {
    return matrixChainOrder(p);  // Use Template 3
}
```

**LeetCode Problems:**
- [Minimum Score Triangulation](https://leetcode.com/problems/minimum-score-triangulation-of-polygon/)

---

### 3. **Minimum Cost to Merge Stones**
Merge stones with minimum cost.

```java
// Similar to matrix chain but with different merge rules
public int mergeStones(int[] stones, int K) {
    // Use modified Template 1 with K-way merge
}
```

**LeetCode Problems:**
- [Minimum Cost to Merge Stones](https://leetcode.com/problems/minimum-cost-to-merge-stones/)

---

## 💡 Key Insights

### Why Interval DP Works:

1. **Optimal Substructure**: Optimal solution for [i,j] can be built from optimal solutions of subintervals
2. **Independence**: Once we decide how to split, subproblems are independent
3. **Overlapping**: Same subintervals appear in different larger intervals

### Common Patterns:

**Pattern 1: Last Operation**
```java
// What if element k is processed last?
for (int k = i; k <= j; k++) {
    // Process [i,k-1] and [k+1,j] first
    // Then process k with only boundaries
}
```

**Pattern 2: Split Point**
```java
// Where to split interval [i,j]?
for (int k = i; k < j; k++) {
    // Process [i,k] and [k+1,j]
    // Then merge results
}
```

### Gap Strategy vs Recursion:
- **Gap Strategy**: Build bottom-up by interval length
- **Recursion + Memo**: Top-down, more intuitive for some problems

---

## 📊 Template Selection Guide

| Problem Type | Template | Key Feature |
|--------------|----------|-------------|
| Need padding/boundaries | Template 2 | Burst balloons style |
| Adjacent elements multiply | Template 3 | Matrix chain style |
| General interval optimization | Template 1 | Classic gap strategy |

---

## 🧠 Mental Model

Think of Interval DP as:
> "For each interval, try all possible ways to split/process it, and pick the best. Build larger intervals from smaller ones."

Key insights:
- **Gap strategy**: Process intervals by increasing length
- **Split enumeration**: Try all valid split points
- **Subproblem combination**: Merge results optimally

---

## 🎯 Quick Reference

### The Framework:
```
1. Define dp[i][j] = optimal result for interval [i,j]
2. Base case: Single elements or empty intervals
3. For each interval length:
   - For each starting position:
     - Try all ways to split/process
     - Take optimal among all choices
```

### Common Mistakes:
- Forgetting base cases
- Wrong interval boundaries
- Not considering all split points
- Incorrect merge cost calculation

### Time & Space:
- Time: O(n³) for most problems
- Space: O(n²) for the DP table

---

## 📚 Practice Problems (In Order)

### Start Here:
1. **[Burst Balloons](https://leetcode.com/problems/burst-balloons/)** - Classic interval DP
2. **[Minimum Score Triangulation](https://leetcode.com/problems/minimum-score-triangulation-of-polygon/)** - Polygon triangulation
3. **[Matrix Chain Multiplication](https://practice.geeksforgeeks.org/problems/matrix-chain-multiplication/0)** - Classic problem

### Then Try:
4. **[Minimum Cost to Merge Stones](https://leetcode.com/problems/minimum-cost-to-merge-stones/)** - K-way merge
5. **[Palindrome Partitioning II](https://leetcode.com/problems/palindrome-partitioning-ii/)** - Minimum cuts

### Advanced:
6. **[Remove Boxes](https://leetcode.com/problems/remove-boxes/)** - Complex state
7. **[Strange Printer](https://leetcode.com/problems/strange-printer/)** - Unique transitions

---

## 🎯 Interview Communication Script

```
"I notice this is an Interval DP problem because:
1. We need to find optimal way to process an array/sequence
2. The order of operations matters
3. We can split the problem into subintervals

I'll use dynamic programming where dp[i][j] represents 
the optimal result for interval from i to j.

For each interval, I'll try all possible ways to split or 
process it, then take the optimal choice.

Using gap strategy: process intervals by increasing length.

Time complexity: O(n³), Space complexity: O(n²)"
```

**Master these templates and you'll handle 95% of Interval DP problems!**
