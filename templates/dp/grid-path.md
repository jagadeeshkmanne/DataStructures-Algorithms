# Dynamic Programming - Grid Path Pattern

## 🎯 Pattern Overview

The **Grid Path Pattern** is for 2D grid problems where you move from one position to another (usually top-left to bottom-right). The value at each cell depends on values from previous cells.

### Core Concept:
```
Start: Top-left corner (0,0)
End: Bottom-right corner (m-1, n-1)
Movement: Can only move right or down (usually)
Goal: Count paths, minimize cost, or maximize value
```

---

## 🔍 Pattern Recognition

### ✅ Quick Recognition Checklist
- [ ] 2D grid/matrix problem?
- [ ] Need to go from point A to point B?
- [ ] Limited movement directions?
- [ ] Each cell depends on previous cells?
→ If all YES → Grid Path Pattern!

### Magic Keywords:
| Keyword | Example |
|---------|---------|
| "grid", "matrix" | Grid traversal |
| "paths from top-left to bottom-right" | Unique Paths |
| "minimum/maximum path sum" | Min Path Sum |
| "can only move right/down" | Classic grid DP |

### Quick Test:
```
✓ Am I in a 2D grid?
✓ Do I move between cells?
✓ Does current cell depend on neighbors?
→ Grid Path Pattern! ✅
```

---

## 🚀 Universal Template

### The Core Template (Works for 90% of problems):

```java
public int gridDP(int[][] grid, String operation) {
    int m = grid.length;
    int n = grid[0].length;
    int[][] dp = new int[m][n];
    
    // Base case: starting cell
    dp[0][0] = (operation.equals("count")) ? 1 : grid[0][0];
    
    // Initialize first row (can only come from left)
    for (int j = 1; j < n; j++) {
        if (operation.equals("count")) {
            dp[0][j] = dp[0][j-1];
        } else {
            dp[0][j] = dp[0][j-1] + grid[0][j];
        }
    }
    
    // Initialize first column (can only come from top)
    for (int i = 1; i < m; i++) {
        if (operation.equals("count")) {
            dp[i][0] = dp[i-1][0];
        } else {
            dp[i][0] = dp[i-1][0] + grid[i][0];
        }
    }
    
    // Fill the grid
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            if (operation.equals("count")) {
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            } else if (operation.equals("min")) {
                dp[i][j] = grid[i][j] + Math.min(dp[i-1][j], dp[i][j-1]);
            } else if (operation.equals("max")) {
                dp[i][j] = grid[i][j] + Math.max(dp[i-1][j], dp[i][j-1]);
            }
        }
    }
    
    return dp[m-1][n-1];
}
```

---

## 📋 Common Problem Types

### 1. **Count Paths**
Count number of unique paths from top-left to bottom-right.

```java
public int uniquePaths(int m, int n) {
    // Create dummy grid since we're just counting
    int[][] dummyGrid = new int[m][n];
    return gridDP(dummyGrid, "count");
}
```

**LeetCode Problems:**
- [Unique Paths](https://leetcode.com/problems/unique-paths/)
- [Unique Paths II](https://leetcode.com/problems/unique-paths-ii/) (with obstacles)

---

### 2. **Minimum Path Sum**
Find path with minimum sum from top-left to bottom-right.

```java
public int minPathSum(int[][] grid) {
    return gridDP(grid, "min");
}
```

**LeetCode Problems:**
- [Minimum Path Sum](https://leetcode.com/problems/minimum-path-sum/)

---

### 3. **Maximum Path Sum**
Find path with maximum sum (less common but same pattern).

```java
public int maxPathSum(int[][] grid) {
    return gridDP(grid, "max");
}
```

---

### 4. **With Obstacles** (Slight modification)
When grid has obstacles (usually marked as 1).

```java
public int uniquePathsWithObstacles(int[][] obstacleGrid) {
    if (obstacleGrid[0][0] == 1) return 0;
    
    int m = obstacleGrid.length;
    int n = obstacleGrid[0].length;
    int[][] dp = new int[m][n];
    
    dp[0][0] = 1;
    
    // First row
    for (int j = 1; j < n; j++) {
        dp[0][j] = (obstacleGrid[0][j] == 0 && dp[0][j-1] == 1) ? 1 : 0;
    }
    
    // First column
    for (int i = 1; i < m; i++) {
        dp[i][0] = (obstacleGrid[i][0] == 0 && dp[i-1][0] == 1) ? 1 : 0;
    }
    
    // Fill the grid
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            if (obstacleGrid[i][j] == 0) {
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }
    }
    
    return dp[m-1][n-1];
}
```

**LeetCode Problems:**
- [Unique Paths II](https://leetcode.com/problems/unique-paths-ii/)

---

### 5. **Special Movement Pattern** (Triangle/Falling Path)
When you can move to multiple cells below (not just right/down).

```java
public int minFallingPathSum(int[][] matrix) {
    int n = matrix.length;
    int[][] dp = new int[n][n];
    
    // Copy first row
    for (int j = 0; j < n; j++) {
        dp[0][j] = matrix[0][j];
    }
    
    // Process each row
    for (int i = 1; i < n; i++) {
        for (int j = 0; j < n; j++) {
            // Can come from: directly above, diagonally left, diagonally right
            int fromAbove = dp[i-1][j];
            int fromLeft = (j > 0) ? dp[i-1][j-1] : Integer.MAX_VALUE;
            int fromRight = (j < n-1) ? dp[i-1][j+1] : Integer.MAX_VALUE;
            
            dp[i][j] = matrix[i][j] + Math.min(fromAbove, Math.min(fromLeft, fromRight));
        }
    }
    
    // Find minimum in last row
    int minPath = dp[n-1][0];
    for (int j = 1; j < n; j++) {
        minPath = Math.min(minPath, dp[n-1][j]);
    }
    
    return minPath;
}
```

**LeetCode Problems:**
- [Minimum Falling Path Sum](https://leetcode.com/problems/minimum-falling-path-sum/)
- [Triangle](https://leetcode.com/problems/triangle/)

---

## 💡 Tips & Strategies

### The Golden Formula:
```java
// For counting paths:
dp[i][j] = dp[i-1][j] + dp[i][j-1]

// For min/max sum:
dp[i][j] = grid[i][j] + Math.min(dp[i-1][j], dp[i][j-1])
```

### Common Patterns:
1. **Standard movement**: Right or Down only
2. **With obstacles**: Check if cell is valid before processing
3. **Multiple directions**: Consider all possible previous positions
4. **Different start/end**: Adjust base cases accordingly

### Space Optimization:
```java
// Can often use just 1D array instead of 2D
// Since we only need previous row
int[] dp = new int[n];
int[] temp = new int[n];
```

---

## 📊 Pattern Variations

| Type | Movement | Example | Key Insight |
|------|----------|---------|-------------|
| Basic Path Count | Right/Down | Unique Paths | Simple addition |
| With Obstacles | Right/Down + blocked cells | Unique Paths II | Check validity |
| Min/Max Sum | Right/Down | Min Path Sum | Add current cell value |
| Multiple Directions | 3+ choices | Falling Path | Check all sources |
| Variable Start/End | Any cell to any cell | Custom problems | Adjust boundaries |

---

## 🧠 Mental Model

Think of it as:
> "I'm at a cell. Where could I have come from? Take the best option and add my current value."

This works for ALL grid problems:
- **Counting**: Add ways from all sources
- **Min sum**: Take minimum from sources + current
- **Max sum**: Take maximum from sources + current

---

## 🎯 Quick Reference

### Problem Identification:
```
2D grid + movement? → Grid DP
Count paths? → Add previous cells
Min/Max path? → Min/Max of previous + current
Obstacles? → Check validity first
Multiple movements? → Consider all sources
```

### Time & Space:
- Time: O(m × n)
- Space: O(m × n) or O(n) with optimization

---

## 📚 Practice Problems (In Order)

### Start Here:
1. **[Unique Paths](https://leetcode.com/problems/unique-paths/)** - Basic counting
2. **[Minimum Path Sum](https://leetcode.com/problems/minimum-path-sum/)** - Basic optimization
3. **[Unique Paths II](https://leetcode.com/problems/unique-paths-ii/)** - With obstacles

### Then Try:
4. **[Triangle](https://leetcode.com/problems/triangle/)** - Different shape
5. **[Minimum Falling Path Sum](https://leetcode.com/problems/minimum-falling-path-sum/)** - Multiple directions

### Advanced:
6. **[Maximal Square](https://leetcode.com/problems/maximal-square/)** - Different recurrence
7. **[Dungeon Game](https://leetcode.com/problems/dungeon-game/)** - Backward DP

---

## 🎯 Interview Communication Script

```
"I notice this is a Grid Path DP problem because:
1. We have a 2D grid
2. Need to find path from one position to another
3. Each cell's value depends on previous cells

I'll use dynamic programming where dp[i][j] represents 
[the number of paths to / minimum cost to reach] cell (i,j).

The recurrence relation is:
- For counting: dp[i][j] = dp[i-1][j] + dp[i][j-1]
- For optimization: dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1])

Time complexity: O(m×n), Space complexity: O(m×n)"
```

**Master these patterns and you'll handle 95% of Grid Path DP problems!**
