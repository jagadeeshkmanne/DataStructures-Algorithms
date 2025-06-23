# Dynamic Programming - Grid/Path Pattern (Java 6 Compatible)

## 🎯 Pattern Overview

Grid DP is for 2D grid problems where you move from top-left to bottom-right. Current cell depends on previous cells (usually top and left).

---

## 🚀 Reusable Helper Methods (Java 6 Style)

```java
public class GridDPHelpers {
    
    // Helper 1: Initialize boundaries with value
    public static void initBoundaries(int[][] dp, int m, int n, int value) {
        // First row
        for (int j = 0; j < n; j++) {
            dp[0][j] = value;
        }
        // First column
        for (int i = 0; i < m; i++) {
            dp[i][0] = value;
        }
    }
    
    // Helper 2: Initialize boundaries with obstacles
    public static void initBoundariesWithObstacles(int[][] dp, int[][] obstacles, int m, int n) {
        dp[0][0] = obstacles[0][0] == 0 ? 1 : 0;
        
        // First row
        for (int j = 1; j < n; j++) {
            if (obstacles[0][j] == 0 && dp[0][j-1] == 1) {
                dp[0][j] = 1;
            } else {
                dp[0][j] = 0;
            }
        }
        
        // First column
        for (int i = 1; i < m; i++) {
            if (obstacles[i][0] == 0 && dp[i-1][0] == 1) {
                dp[i][0] = 1;
            } else {
                dp[i][0] = 0;
            }
        }
    }
    
    // Helper 3: Initialize boundaries by accumulating grid values
    public static void initBoundariesWithSum(int[][] dp, int[][] grid, int m, int n) {
        dp[0][0] = grid[0][0];
        
        // First row - accumulate
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j-1] + grid[0][j];
        }
        
        // First column - accumulate
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i-1][0] + grid[i][0];
        }
    }
    
    // Helper 4: Copy first row from source to dp
    public static void copyFirstRow(int[][] dp, int[][] source, int n) {
        for (int j = 0; j < n; j++) {
            dp[0][j] = source[0][j];
        }
    }
    
    // Helper 5: Standard grid filling loop (reusable across all templates!)
    public static void fillGrid(int[][] dp, int m, int n, int startRow, int startCol) {
        // Standard nested loop that ALL grid problems use
        // startRow and startCol are usually 1, but can be 0 for special cases
        for (int i = startRow; i < m; i++) {
            for (int j = startCol; j < n; j++) {
                // The actual formula will be applied in the template
                // This just provides the iteration structure
            }
        }
    }
}
```

---

## 🚀 Universal Templates

### Template 1: Path Counting (With/Without Obstacles)

```java
public int gridPathCount(int m, int n, int[][] obstacles) {
    int[][] dp = new int[m][n];
    
    // Initialize boundaries based on obstacles
    if (obstacles == null) {
        GridDPHelpers.initBoundaries(dp, m, n, 1);
    } else {
        if (obstacles[0][0] == 1 || obstacles[m-1][n-1] == 1) {
            return 0;  // Can't start or end
        }
        GridDPHelpers.initBoundariesWithObstacles(dp, obstacles, m, n);
    }
    
    // Fill the grid
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            if (obstacles == null || obstacles[i][j] == 0) {
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }
    }
    
    return dp[m-1][n-1];
}
```

### Template 2: Path Value Optimization (Min/Max)

```java
public int gridPathValue(int[][] grid, boolean minimize) {
    int m = grid.length;
    int n = grid[0].length;
    int[][] dp = new int[m][n];
    
    // Initialize boundaries
    GridDPHelpers.initBoundariesWithSum(dp, grid, m, n);
    
    // Fill the grid
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            int fromTop = dp[i-1][j];
            int fromLeft = dp[i][j-1];
            
            if (minimize) {
                dp[i][j] = grid[i][j] + Math.min(fromTop, fromLeft);
            } else {
                dp[i][j] = grid[i][j] + Math.max(fromTop, fromLeft);
            }
        }
    }
    
    return dp[m-1][n-1];
}
```

### Template 3: Multiple End Points (Like Falling Path)

```java
public int gridMultipleEnds(int[][] grid, boolean minimize) {
    int n = grid.length;
    int[][] dp = new int[n][n];
    
    // Copy first row
    GridDPHelpers.copyFirstRow(dp, grid, n);
    
    // Process each row
    for (int i = 1; i < n; i++) {
        for (int j = 0; j < n; j++) {
            int fromAbove = dp[i-1][j];
            int fromLeft = j > 0 ? dp[i-1][j-1] : Integer.MAX_VALUE;
            int fromRight = j < n-1 ? dp[i-1][j+1] : Integer.MAX_VALUE;
            
            if (minimize) {
                dp[i][j] = grid[i][j] + Math.min(fromAbove, Math.min(fromLeft, fromRight));
            } else {
                // For maximize version
                if (j == 0) fromLeft = Integer.MIN_VALUE;
                if (j == n-1) fromRight = Integer.MIN_VALUE;
                dp[i][j] = grid[i][j] + Math.max(fromAbove, Math.max(fromLeft, fromRight));
            }
        }
    }
    
    // Find best value in last row - track while processing for O(1)
    int result = dp[n-1][0];
    for (int j = 1; j < n; j++) {
        if (minimize) {
            result = Math.min(result, dp[n-1][j]);
        } else {
            result = Math.max(result, dp[n-1][j]);
        }
    }
    return result;
}
```

---

## 📋 Clean Solutions Using Templates

### 1. **Unique Paths**
```java
public int uniquePaths(int m, int n) {
    return gridPathCount(m, n, null);
}
```

### 2. **Unique Paths with Obstacles**
```java
public int uniquePathsWithObstacles(int[][] obstacleGrid) {
    return gridPathCount(obstacleGrid.length, obstacleGrid[0].length, obstacleGrid);
}
```

### 3. **Minimum Path Sum**
```java
public int minPathSum(int[][] grid) {
    return gridPathValue(grid, true);
}
```

### 4. **Maximum Path Sum**
```java
public int maxPathSum(int[][] grid) {
    return gridPathValue(grid, false);
}
```

### 5. **Minimum Falling Path Sum**
```java
public int minFallingPathSum(int[][] matrix) {
    return gridMultipleEnds(matrix, true);
}
```

---

## 📊 Special Cases (Need Custom Code)

### Maximal Square (Different Formula)
```java
public int maximalSquare(char[][] matrix) {
    if (matrix.length == 0) return 0;
    
    int m = matrix.length;
    int n = matrix[0].length;
    int[][] dp = new int[m][n];
    int maxSide = 0;
    
    // Can't use standard templates - different pattern
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (matrix[i][j] == '1') {
                if (i == 0 || j == 0) {
                    dp[i][j] = 1;
                } else {
                    int top = dp[i-1][j];
                    int left = dp[i][j-1];
                    int diagonal = dp[i-1][j-1];
                    dp[i][j] = Math.min(Math.min(top, left), diagonal) + 1;
                }
                maxSide = Math.max(maxSide, dp[i][j]);
            }
        }
    }
    
    return maxSide * maxSide;
}
```

---

## 💡 Pattern Recognition

**Which template to use?**

1. **Counting paths?** → `gridPathCount()`
   - No obstacles: pass `null`
   - With obstacles: pass obstacle array

2. **Min/Max path sum?** → `gridPathValue()`
   - Minimize: pass `true`
   - Maximize: pass `false`

3. **Multiple choices per cell?** → `gridMultipleEnds()`
   - Can move to multiple positions below

4. **Special pattern?** → Write custom
   - Like maximal square (uses 3 neighbors differently)

---

## 🎯 Interview Tips

1. **The helpers save time** - No need to write initialization loops repeatedly
2. **Templates handle 80% of problems** - Just call with right parameters
3. **Time complexity**: Always O(m×n)
4. **Space optimization**: Mention you can use 1D array for most problems

---

## 📚 Key Benefits

With these templates and helpers:
- **No repeated code** for initialization
- **Clean solutions** - just call the template
- **Easy to modify** - clear structure
