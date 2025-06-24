# Grid DP Templates - Final Clean Version

## 🎯 Pattern Recognition

### When to Use Grid DP?
- Problem involves a 2D grid/matrix
- Need to find optimal path (min/max) or count paths
- Movement is restricted (can't go everywhere)
- Current cell depends on previous cells

### Quick Decision Tree
```
What's the movement pattern?
├─ Can only move right/down → Template 1 (Standard Grid Path)
├─ Can move diagonally → Template 2 (Falling Path)
├─ Has obstacles/conditions → Template 3 (Path Counting)
└─ Other complex patterns → Custom solution
```

---

## 📋 Template 1: Standard Grid Path (Right/Down Only)

Use for: Minimum Path Sum, Maximum Path Sum, Basic Unique Paths

**Time Complexity: O(m × n)** - Visit each cell once  
**Space Complexity: O(m × n)** - DP table (can optimize to O(n))

```java
class Solution {
    abstract class GridPathTemplate {
        // Define problem-specific logic
        protected abstract int getStartValue(int[][] grid);
        protected abstract int getDefaultValue(); // For empty grid
        protected abstract int fillFirstRow(int leftResult, int currentGridValue);
        protected abstract int fillFirstCol(int topResult, int currentGridValue);
        protected abstract int fillCell(int currentGridValue, int topResult, int leftResult);
        
        public int solve(int[][] grid) {
            // Handle empty grid
            if (grid == null || grid.length == 0 || grid[0] == null || grid[0].length == 0) {
                return getDefaultValue();
            }
            
            int rows = grid.length;
            int cols = grid[0].length;
            int[][] dp = new int[rows][cols];
            
            // Starting cell
            dp[0][0] = getStartValue(grid);
            
            // Fill first row (can only come from left)
            for (int col = 1; col < cols; col++) {
                int leftResult = dp[0][col-1];
                int currentGridValue = grid[0][col];
                dp[0][col] = fillFirstRow(leftResult, currentGridValue);
            }
            
            // Fill first column (can only come from top)
            for (int row = 1; row < rows; row++) {
                int topResult = dp[row-1][0];
                int currentGridValue = grid[row][0];
                dp[row][0] = fillFirstCol(topResult, currentGridValue);
            }
            
            // Fill rest of grid (can come from top or left)
            for (int row = 1; row < rows; row++) {
                for (int col = 1; col < cols; col++) {
                    int currentGridValue = grid[row][col];
                    int topResult = dp[row-1][col];
                    int leftResult = dp[row][col-1];
                    dp[row][col] = fillCell(currentGridValue, topResult, leftResult);
                }
            }
            
            return dp[rows-1][cols-1];
        }
    }
}
```

### Example 1.1: Minimum Path Sum (LeetCode 64)
```java
public int minPathSum(int[][] grid) {
    return new GridPathTemplate() {
        protected int getStartValue(int[][] grid) {
            return grid[0][0];
        }
        
        protected int getDefaultValue() {
            return 0;
        }
        
        protected int fillFirstRow(int leftResult, int currentValue) {
            return leftResult + currentValue;
        }
        
        protected int fillFirstCol(int topResult, int currentValue) {
            return topResult + currentValue;
        }
        
        protected int fillCell(int currentValue, int topResult, int leftResult) {
            return currentValue + Math.min(topResult, leftResult);
        }
    }.solve(grid);
}
```

### Example 1.2: Maximum Path Sum
```java
public int maxPathSum(int[][] grid) {
    return new GridPathTemplate() {
        protected int getStartValue(int[][] grid) {
            return grid[0][0];
        }
        
        protected int getDefaultValue() {
            return 0;
        }
        
        protected int fillFirstRow(int leftResult, int currentValue) {
            return leftResult + currentValue;
        }
        
        protected int fillFirstCol(int topResult, int currentValue) {
            return topResult + currentValue;
        }
        
        protected int fillCell(int currentValue, int topResult, int leftResult) {
            return currentValue + Math.max(topResult, leftResult);
        }
    }.solve(grid);
}
```

### Example 1.3: Unique Paths (LeetCode 62)
```java
public int uniquePaths(int m, int n) {
    int[][] dummyGrid = new int[m][n];
    
    return new GridPathTemplate() {
        protected int getStartValue(int[][] grid) {
            return 1;
        }
        
        protected int getDefaultValue() {
            return 0;
        }
        
        protected int fillFirstRow(int leftResult, int currentValue) {
            return leftResult; // Only one way along edge
        }
        
        protected int fillFirstCol(int topResult, int currentValue) {
            return topResult; // Only one way along edge
        }
        
        protected int fillCell(int currentValue, int topResult, int leftResult) {
            return topResult + leftResult;
        }
    }.solve(dummyGrid);
}
```

---

## 📋 Template 2: Falling Path (Diagonal Movement)

Use for: Minimum Falling Path Sum, Maximum Falling Path Sum

**Time Complexity: O(n²)** - Visit each cell once + O(n) to find result in last row  
**Space Complexity: O(n²)** - DP table (can optimize to O(n))

```java
class Solution {
    abstract class FallingPathTemplate {
        // Define problem-specific logic
        protected abstract int getCellStartValue(int[][] grid, int row, int col);
        protected abstract int getDefaultValue(); // For empty grid
        protected abstract int getInvalidPathValue(); // For out of bounds
        protected abstract int fillCell(int currentValue, int fromTopLeft, int fromTop, int fromTopRight);
        protected abstract int findBestInRow(int current, int candidate); // For finding best in last row
        
        public int solve(int[][] grid) {
            // Handle empty grid
            if (grid == null || grid.length == 0 || grid[0] == null || grid[0].length == 0) {
                return getDefaultValue();
            }
            
            int rows = grid.length;
            int cols = grid[0].length;
            int[][] dp = new int[rows][cols];
            
            // Initialize first row
            for (int col = 0; col < cols; col++) {
                dp[0][col] = getCellStartValue(grid, 0, col);
            }
            
            // Fill remaining rows
            for (int row = 1; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    int currentValue = grid[row][col];
                    int fromTop = dp[row-1][col];
                    int fromTopLeft = (col > 0) ? dp[row-1][col-1] : getInvalidPathValue();
                    int fromTopRight = (col < cols-1) ? dp[row-1][col+1] : getInvalidPathValue();
                    
                    dp[row][col] = fillCell(currentValue, fromTopLeft, fromTop, fromTopRight);
                }
            }
            
            // Find best value in last row
            int result = dp[rows-1][0];
            for (int col = 1; col < cols; col++) {
                result = findBestInRow(result, dp[rows-1][col]);
            }
            
            return result;
        }
    }
}
```

### Example 2.1: Minimum Falling Path Sum (LeetCode 931)
```java
public int minFallingPathSum(int[][] matrix) {
    return new FallingPathTemplate() {
        protected int getCellStartValue(int[][] grid, int row, int col) {
            return grid[row][col];
        }
        
        protected int getDefaultValue() {
            return 0;
        }
        
        protected int getInvalidPathValue() {
            return Integer.MAX_VALUE; // For minimum problems
        }
        
        protected int fillCell(int currentValue, int fromTopLeft, int fromTop, int fromTopRight) {
            int minFromAbove = fromTop;
            if (fromTopLeft != Integer.MAX_VALUE) {
                minFromAbove = Math.min(minFromAbove, fromTopLeft);
            }
            if (fromTopRight != Integer.MAX_VALUE) {
                minFromAbove = Math.min(minFromAbove, fromTopRight);
            }
            return currentValue + minFromAbove;
        }
        
        protected int findBestInRow(int current, int candidate) {
            return Math.min(current, candidate);
        }
    }.solve(matrix);
}
```

### Example 2.2: Maximum Falling Path Sum
```java
public int maxFallingPathSum(int[][] matrix) {
    return new FallingPathTemplate() {
        protected int getCellStartValue(int[][] grid, int row, int col) {
            return grid[row][col];
        }
        
        protected int getDefaultValue() {
            return 0;
        }
        
        protected int getInvalidPathValue() {
            return Integer.MIN_VALUE; // For maximum problems
        }
        
        protected int fillCell(int currentValue, int fromTopLeft, int fromTop, int fromTopRight) {
            int maxFromAbove = fromTop;
            if (fromTopLeft != Integer.MIN_VALUE) {
                maxFromAbove = Math.max(maxFromAbove, fromTopLeft);
            }
            if (fromTopRight != Integer.MIN_VALUE) {
                maxFromAbove = Math.max(maxFromAbove, fromTopRight);
            }
            return currentValue + maxFromAbove;
        }
        
        protected int findBestInRow(int current, int candidate) {
            return Math.max(current, candidate);
        }
    }.solve(matrix);
}
```

---

## 📋 Template 3: Path Counting with Obstacles

Use for: Unique Paths with Obstacles, conditional path counting

**Time Complexity: O(m × n)** - Visit each cell once  
**Space Complexity: O(m × n)** - DP table (can optimize to O(n))

```java
class Solution {
    abstract class PathCountingTemplate {
        protected abstract boolean isBlocked(int[][] grid, int row, int col);
        protected abstract int getValidPathCount(int fromTop, int fromLeft);
        protected abstract int getDefaultValue();
        
        public int solve(int[][] grid) {
            // Handle empty grid
            if (grid == null || grid.length == 0 || grid[0] == null || grid[0].length == 0) {
                return getDefaultValue();
            }
            
            int rows = grid.length;
            int cols = grid[0].length;
            
            // Check if start or end is blocked
            if (isBlocked(grid, 0, 0) || isBlocked(grid, rows-1, cols-1)) {
                return 0;
            }
            
            int[][] dp = new int[rows][cols];
            dp[0][0] = 1;
            
            // Fill first row
            for (int col = 1; col < cols; col++) {
                if (isBlocked(grid, 0, col)) {
                    dp[0][col] = 0;
                } else {
                    dp[0][col] = dp[0][col-1];
                }
            }
            
            // Fill first column
            for (int row = 1; row < rows; row++) {
                if (isBlocked(grid, row, 0)) {
                    dp[row][0] = 0;
                } else {
                    dp[row][0] = dp[row-1][0];
                }
            }
            
            // Fill rest of grid
            for (int row = 1; row < rows; row++) {
                for (int col = 1; col < cols; col++) {
                    if (isBlocked(grid, row, col)) {
                        dp[row][col] = 0;
                    } else {
                        int fromTop = dp[row-1][col];
                        int fromLeft = dp[row][col-1];
                        dp[row][col] = getValidPathCount(fromTop, fromLeft);
                    }
                }
            }
            
            return dp[rows-1][cols-1];
        }
    }
}
```

### Example 3.1: Unique Paths II (LeetCode 63)
```java
public int uniquePathsWithObstacles(int[][] obstacleGrid) {
    return new PathCountingTemplate() {
        protected boolean isBlocked(int[][] grid, int row, int col) {
            return grid[row][col] == 1;
        }
        
        protected int getValidPathCount(int fromTop, int fromLeft) {
            return fromTop + fromLeft;
        }
        
        protected int getDefaultValue() {
            return 0;
        }
    }.solve(obstacleGrid);
}
```

---

## ⏱️ Time & Space Complexity Summary

| Template | Time | Space | Space Optimized |
|----------|------|-------|-----------------|
| Template 1 (Standard Path) | O(m×n) | O(m×n) | O(n) |
| Template 2 (Falling Path) | O(n²) | O(n²) | O(n) |
| Template 3 (Path Counting) | O(m×n) | O(m×n) | O(n) |

### Space Optimization Example (Template 1)
```java
public int minPathSumOptimized(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[] dp = new int[n];
    
    dp[0] = grid[0][0];
    
    // First row
    for (int j = 1; j < n; j++) {
        dp[j] = dp[j-1] + grid[0][j];
    }
    
    // Remaining rows
    for (int i = 1; i < m; i++) {
        dp[0] += grid[i][0]; // Update first column
        for (int j = 1; j < n; j++) {
            dp[j] = grid[i][j] + Math.min(dp[j], dp[j-1]);
        }
    }
    
    return dp[n-1];
}
```

---

## ⚠️ Edge Cases - Handled Naturally!

The templates handle these edge cases automatically:

| Edge Case | How It's Handled |
|-----------|-----------------|
| Empty/null grid | Explicit check returns default value |
| Single cell (1×1) | First row/column loops don't run, returns dp[0][0] |
| Single row (1×n) | First column loop doesn't run, works naturally |
| Single column (m×1) | First row loop doesn't run, works naturally |
| Start/end blocked | Template 3 checks explicitly |

No special if-conditions needed - the loop structure handles everything!

---

## 🎯 Practice Problems

### Beginner (Start Here)
1. **[Minimum Path Sum (LC 64)](https://leetcode.com/problems/minimum-path-sum/)** - Template 1
2. **[Unique Paths (LC 62)](https://leetcode.com/problems/unique-paths/)** - Template 1
3. **[Unique Paths II (LC 63)](https://leetcode.com/problems/unique-paths-ii/)** - Template 3

### Intermediate
4. **[Minimum Falling Path Sum (LC 931)](https://leetcode.com/problems/minimum-falling-path-sum/)** - Template 2
5. **[Triangle (LC 120)](https://leetcode.com/problems/triangle/)** - Modified Template 2
6. **[Maximal Square (LC 221)](https://leetcode.com/problems/maximal-square/)** - Custom variation

### Advanced
7. **[Dungeon Game (LC 174)](https://leetcode.com/problems/dungeon-game/)** - Work backwards
8. **[Cherry Pickup (LC 741)](https://leetcode.com/problems/cherry-pickup/)** - 3D DP
9. **[Minimum Path Cost in Grid (LC 2304)](https://leetcode.com/problems/minimum-path-cost-in-a-grid/)** - Variable costs

---

## 💡 Key Insights

1. **Clean and readable** - Variable names explain what they contain
2. **Edge cases handled naturally** - Loop structure takes care of special cases
3. **Consistent pattern** - All templates follow same structure
4. **Easy to modify** - Just implement the abstract methods
5. **Interview friendly** - Clear logic, easy to explain

---

## 🚀 Quick Usage Guide

```
1. Identify movement pattern
2. Copy appropriate template
3. Implement abstract methods:
   - getStartValue(): Initial value at start
   - getDefaultValue(): What to return for empty grid
   - fillFirstRow/Col(): How to fill edges
   - fillCell(): Main logic for each cell
4. Test and submit!
```

Remember: The templates handle the algorithm. You just define the problem-specific logic!
