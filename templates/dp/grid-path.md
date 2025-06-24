# Grid Path DP Templates - Optimized with Descriptive Variables

## 🎯 Pattern Recognition
- 2D grid/matrix problems
- Can only move in restricted directions (usually right/down)
- Need to find optimal path or count paths
- Current cell depends on previous cells

---

## 📋 Template 1: Minimum Path Sum

```java
// Use this for: minimum path sum, minimum cost path
public int minPathSum(int[][] grid) {
    int totalRows = grid.length;
    int totalCols = grid[0].length;
    int[][] minCostToReach = new int[totalRows][totalCols];
    
    // Starting cell cost
    minCostToReach[0][0] = grid[0][0];
    
    // First row - can only come from left, accumulate costs
    for (int currentCol = 1; currentCol < totalCols; currentCol++) {
        int costFromLeft = minCostToReach[0][currentCol - 1];
        int currentCellCost = grid[0][currentCol];
        minCostToReach[0][currentCol] = costFromLeft + currentCellCost;
    }
    
    // First column - can only come from top, accumulate costs
    for (int currentRow = 1; currentRow < totalRows; currentRow++) {
        int costFromTop = minCostToReach[currentRow - 1][0];
        int currentCellCost = grid[currentRow][0];
        minCostToReach[currentRow][0] = costFromTop + currentCellCost;
    }
    
    // Fill remaining cells - take minimum from two directions
    for (int currentRow = 1; currentRow < totalRows; currentRow++) {
        for (int currentCol = 1; currentCol < totalCols; currentCol++) {
            int costFromTop = minCostToReach[currentRow - 1][currentCol];
            int costFromLeft = minCostToReach[currentRow][currentCol - 1];
            int currentCellCost = grid[currentRow][currentCol];
            
            int minimumCostToHere = Math.min(costFromTop, costFromLeft);
            minCostToReach[currentRow][currentCol] = currentCellCost + minimumCostToHere;
        }
    }
    
    return minCostToReach[totalRows - 1][totalCols - 1];
}
// Time: O(m × n), Space: O(m × n)
```

---

## 📋 Template 2: Maximum Path Sum

```java
// Use this for: maximum path sum, maximum value collection
public int maxPathSum(int[][] grid) {
    int totalRows = grid.length;
    int totalCols = grid[0].length;
    int[][] maxValueToReach = new int[totalRows][totalCols];
    
    // Starting cell value
    maxValueToReach[0][0] = grid[0][0];
    
    // First row - can only come from left, accumulate values
    for (int currentCol = 1; currentCol < totalCols; currentCol++) {
        int valueFromLeft = maxValueToReach[0][currentCol - 1];
        int currentCellValue = grid[0][currentCol];
        maxValueToReach[0][currentCol] = valueFromLeft + currentCellValue;
    }
    
    // First column - can only come from top, accumulate values
    for (int currentRow = 1; currentRow < totalRows; currentRow++) {
        int valueFromTop = maxValueToReach[currentRow - 1][0];
        int currentCellValue = grid[currentRow][0];
        maxValueToReach[currentRow][0] = valueFromTop + currentCellValue;
    }
    
    // Fill remaining cells - take maximum from two directions
    for (int currentRow = 1; currentRow < totalRows; currentRow++) {
        for (int currentCol = 1; currentCol < totalCols; currentCol++) {
            int valueFromTop = maxValueToReach[currentRow - 1][currentCol];
            int valueFromLeft = maxValueToReach[currentRow][currentCol - 1];
            int currentCellValue = grid[currentRow][currentCol];
            
            int maximumValueToHere = Math.max(valueFromTop, valueFromLeft);
            maxValueToReach[currentRow][currentCol] = currentCellValue + maximumValueToHere;
        }
    }
    
    return maxValueToReach[totalRows - 1][totalCols - 1];
}
// Time: O(m × n), Space: O(m × n)
```

---

## 📋 Template 3: Count Unique Paths

```java
// Use this for: counting paths, number of ways to reach destination
public int uniquePaths(int gridRows, int gridCols) {
    int[][] pathCount = new int[gridRows][gridCols];
    
    // First row - only one way to reach any cell (keep going right)
    for (int currentCol = 0; currentCol < gridCols; currentCol++) {
        pathCount[0][currentCol] = 1;
    }
    
    // First column - only one way to reach any cell (keep going down)
    for (int currentRow = 0; currentRow < gridRows; currentRow++) {
        pathCount[currentRow][0] = 1;
    }
    
    // Fill remaining cells - sum paths from top and left
    for (int currentRow = 1; currentRow < gridRows; currentRow++) {
        for (int currentCol = 1; currentCol < gridCols; currentCol++) {
            int pathsFromTop = pathCount[currentRow - 1][currentCol];
            int pathsFromLeft = pathCount[currentRow][currentCol - 1];
            pathCount[currentRow][currentCol] = pathsFromTop + pathsFromLeft;
        }
    }
    
    return pathCount[gridRows - 1][gridCols - 1];
}
// Time: O(m × n), Space: O(m × n)
```

---

## 📋 Template 4: Paths with Obstacles

```java
// Use this for: paths with blocked cells
public int uniquePathsWithObstacles(int[][] obstacleGrid) {
    int totalRows = obstacleGrid.length;
    int totalCols = obstacleGrid[0].length;
    
    // If start or end is blocked, no path exists
    boolean startBlocked = obstacleGrid[0][0] == 1;
    boolean endBlocked = obstacleGrid[totalRows - 1][totalCols - 1] == 1;
    if (startBlocked || endBlocked) {
        return 0;
    }
    
    int[][] pathCount = new int[totalRows][totalCols];
    pathCount[0][0] = 1;
    
    // First row - blocked cell stops further propagation
    for (int currentCol = 1; currentCol < totalCols; currentCol++) {
        boolean cellIsOpen = obstacleGrid[0][currentCol] == 0;
        int pathsFromLeft = pathCount[0][currentCol - 1];
        
        if (cellIsOpen) {
            pathCount[0][currentCol] = pathsFromLeft;
        }
        // else pathCount[0][currentCol] remains 0
    }
    
    // First column - blocked cell stops further propagation
    for (int currentRow = 1; currentRow < totalRows; currentRow++) {
        boolean cellIsOpen = obstacleGrid[currentRow][0] == 0;
        int pathsFromTop = pathCount[currentRow - 1][0];
        
        if (cellIsOpen) {
            pathCount[currentRow][0] = pathsFromTop;
        }
        // else pathCount[currentRow][0] remains 0
    }
    
    // Fill remaining cells
    for (int currentRow = 1; currentRow < totalRows; currentRow++) {
        for (int currentCol = 1; currentCol < totalCols; currentCol++) {
            boolean cellIsOpen = obstacleGrid[currentRow][currentCol] == 0;
            
            if (cellIsOpen) {
                int pathsFromTop = pathCount[currentRow - 1][currentCol];
                int pathsFromLeft = pathCount[currentRow][currentCol - 1];
                pathCount[currentRow][currentCol] = pathsFromTop + pathsFromLeft;
            }
            // else pathCount[currentRow][currentCol] remains 0
        }
    }
    
    return pathCount[totalRows - 1][totalCols - 1];
}
// Time: O(m × n), Space: O(m × n)
```

---

## 📋 Template 5: Falling Path (Diagonal Movement)

```java
// Use this for: problems where you can move diagonally (3 directions down)
public int minFallingPathSum(int[][] matrix) {
    int gridSize = matrix.length;
    int[][] minSumToReach = new int[gridSize][gridSize];
    
    // First row = matrix values (starting points)
    for (int currentCol = 0; currentCol < gridSize; currentCol++) {
        minSumToReach[0][currentCol] = matrix[0][currentCol];
    }
    
    // Fill remaining rows
    for (int currentRow = 1; currentRow < gridSize; currentRow++) {
        for (int currentCol = 0; currentCol < gridSize; currentCol++) {
            // Can come from: top-left, top, top-right
            int fromDirectlyAbove = minSumToReach[currentRow - 1][currentCol];
            
            int fromDiagonalLeft = (currentCol > 0) 
                ? minSumToReach[currentRow - 1][currentCol - 1] 
                : Integer.MAX_VALUE;
                
            int fromDiagonalRight = (currentCol < gridSize - 1) 
                ? minSumToReach[currentRow - 1][currentCol + 1] 
                : Integer.MAX_VALUE;
            
            int currentCellValue = matrix[currentRow][currentCol];
            int minimumPathToHere = Math.min(fromDirectlyAbove, 
                                    Math.min(fromDiagonalLeft, fromDiagonalRight));
            
            minSumToReach[currentRow][currentCol] = currentCellValue + minimumPathToHere;
        }
    }
    
    // Find minimum in last row (can end at any column)
    int overallMinimum = minSumToReach[gridSize - 1][0];
    for (int finalCol = 1; finalCol < gridSize; finalCol++) {
        int pathSumAtCol = minSumToReach[gridSize - 1][finalCol];
        overallMinimum = Math.min(overallMinimum, pathSumAtCol);
    }
    
    return overallMinimum;
}
// Time: O(n²), Space: O(n²)
```

---

## 📋 Template 6: Space Optimized Version

```java
// Use when asked to optimize space - only need previous row
public int minPathSumOptimized(int[][] grid) {
    int totalRows = grid.length;
    int totalCols = grid[0].length;
    int[] minCostToCurrRow = new int[totalCols];
    
    // Initialize with first row values
    minCostToCurrRow[0] = grid[0][0];
    for (int currentCol = 1; currentCol < totalCols; currentCol++) {
        int costFromLeft = minCostToCurrRow[currentCol - 1];
        minCostToCurrRow[currentCol] = costFromLeft + grid[0][currentCol];
    }
    
    // Process row by row, updating the array in-place
    for (int currentRow = 1; currentRow < totalRows; currentRow++) {
        // Update first column (can only come from top)
        minCostToCurrRow[0] += grid[currentRow][0];
        
        for (int currentCol = 1; currentCol < totalCols; currentCol++) {
            // minCostToCurrRow[currentCol] currently holds value from previous row (top)
            // minCostToCurrRow[currentCol-1] holds value from current row (left)
            int costFromTop = minCostToCurrRow[currentCol];
            int costFromLeft = minCostToCurrRow[currentCol - 1];
            int currentCellCost = grid[currentRow][currentCol];
            
            minCostToCurrRow[currentCol] = currentCellCost + Math.min(costFromTop, costFromLeft);
        }
    }
    
    return minCostToCurrRow[totalCols - 1];
}
// Time: O(m × n), Space: O(n)
```

---

## 🎯 Quick Reference - Which Template to Use

| Problem Type | Template | Key Variables |
|--------------|----------|---------------|
| Minimum Path Sum | Template 1 | `minCostToReach[row][col]` |
| Maximum Path Sum | Template 2 | `maxValueToReach[row][col]` |
| Count Paths | Template 3 | `pathCount[row][col]` |
| Paths with Obstacles | Template 4 | `pathCount[row][col]` + `cellIsOpen` |
| Minimum Falling Path | Template 5 | `minSumToReach[row][col]` + diagonal checks |
| Space Optimization | Template 6 | `minCostToCurrRow[col]` (1D array) |

---

## 📊 Common Problems Mapping

| LeetCode Problem | Template to Use | Key Variable Names |
|-----------------|-----------------|-------------------|
| 64. Minimum Path Sum | Template 1 | `minCostToReach` |
| 62. Unique Paths | Template 3 | `pathCount` |
| 63. Unique Paths II | Template 4 | `pathCount`, `cellIsOpen` |
| 931. Minimum Falling Path Sum | Template 5 | `minSumToReach`, `fromDiagonalLeft/Right` |
| 120. Triangle | Template 5 variant | `minSumToLevel` |
| 174. Dungeon Game | Custom | `minHealthRequired` (works backwards) |
| 221. Maximal Square | Custom | `maxSquareSideAt` |

---

## 💡 Key Improvements Made

1. **Descriptive Variable Names**:
   - `dp` → `minCostToReach`, `pathCount`, `maxValueToReach`
   - `i, j` → `currentRow`, `currentCol`
   - `m, n` → `totalRows`, `totalCols`
   - Values → `costFromTop`, `pathsFromLeft`, `valueFromDiagonalRight`

2. **Clear Intent**:
   - `grid[i][j]` → `currentCellCost`, `currentCellValue`
   - `dp[i-1][j]` → `costFromTop`, `pathsFromTop`
   - Boolean checks → `cellIsOpen`, `startBlocked`

3. **Self-Documenting Code**:
   - Variable names explain what they represent
   - Intermediate variables show calculation steps
   - Comments explain the "why" not just "what"

---

## 📋 Template 7: Dungeon Game (Backwards DP)

```java
// Use this for: minimum initial value needed to reach destination
// Works backwards from destination to start
public int calculateMinimumHP(int[][] dungeon) {
    int totalRows = dungeon.length;
    int totalCols = dungeon[0].length;
    int[][] minHealthRequired = new int[totalRows][totalCols];
    
    // Bottom-right cell (destination)
    int destinationDamage = dungeon[totalRows - 1][totalCols - 1];
    minHealthRequired[totalRows - 1][totalCols - 1] = 
        Math.max(1, 1 - destinationDamage);
    
    // Last row - can only go right
    for (int currentCol = totalCols - 2; currentCol >= 0; currentCol--) {
        int healthNeededAtRight = minHealthRequired[totalRows - 1][currentCol + 1];
        int currentCellDamage = dungeon[totalRows - 1][currentCol];
        
        // Need enough health to survive current cell and have required health for next
        int healthBeforeCell = healthNeededAtRight - currentCellDamage;
        minHealthRequired[totalRows - 1][currentCol] = Math.max(1, healthBeforeCell);
    }
    
    // Last column - can only go down
    for (int currentRow = totalRows - 2; currentRow >= 0; currentRow--) {
        int healthNeededBelow = minHealthRequired[currentRow + 1][totalCols - 1];
        int currentCellDamage = dungeon[currentRow][totalCols - 1];
        
        // Need enough health to survive current cell and have required health for next
        int healthBeforeCell = healthNeededBelow - currentCellDamage;
        minHealthRequired[currentRow][0] = Math.max(1, healthBeforeCell);
    }
    
    // Fill remaining cells - work backwards
    for (int currentRow = totalRows - 2; currentRow >= 0; currentRow--) {
        for (int currentCol = totalCols - 2; currentCol >= 0; currentCol--) {
            // Check both paths: right and down
            int healthNeededIfGoRight = minHealthRequired[currentRow][currentCol + 1];
            int healthNeededIfGoDown = minHealthRequired[currentRow + 1][currentCol];
            
            // Choose the path that requires less initial health
            int minHealthForNextCell = Math.min(healthNeededIfGoRight, healthNeededIfGoDown);
            
            int currentCellDamage = dungeon[currentRow][currentCol];
            int healthBeforeCell = minHealthForNextCell - currentCellDamage;
            
            // Must have at least 1 health at all times
            minHealthRequired[currentRow][currentCol] = Math.max(1, healthBeforeCell);
        }
    }
    
    return minHealthRequired[0][0];
}
// Time: O(m × n), Space: O(m × n)

// Key insight: Work backwards because we need to ensure we never drop to 0 health
// Formula: minHealth[i][j] = max(1, min(right, down) - dungeon[i][j])
```

---

## 📋 Template 8: Maximal Square

```java
// Use this for: finding largest square of 1s in binary matrix
public int maximalSquare(char[][] matrix) {
    if (matrix.length == 0) return 0;
    
    int totalRows = matrix.length;
    int totalCols = matrix[0].length;
    int[][] maxSquareSideAt = new int[totalRows][totalCols];
    int largestSquareSide = 0;
    
    // First row - can only form squares of size 1
    for (int currentCol = 0; currentCol < totalCols; currentCol++) {
        if (matrix[0][currentCol] == '1') {
            maxSquareSideAt[0][currentCol] = 1;
            largestSquareSide = 1;
        }
    }
    
    // First column - can only form squares of size 1  
    for (int currentRow = 0; currentRow < totalRows; currentRow++) {
        if (matrix[currentRow][0] == '1') {
            maxSquareSideAt[currentRow][0] = 1;
            largestSquareSide = 1;
        }
    }
    
    // Fill remaining cells - check all three neighbors
    for (int currentRow = 1; currentRow < totalRows; currentRow++) {
        for (int currentCol = 1; currentCol < totalCols; currentCol++) {
            if (matrix[currentRow][currentCol] == '1') {
                // Square side depends on minimum of three neighbors
                int squareFromLeft = maxSquareSideAt[currentRow][currentCol - 1];
                int squareFromTop = maxSquareSideAt[currentRow - 1][currentCol];
                int squareFromDiagonal = maxSquareSideAt[currentRow - 1][currentCol - 1];
                
                // Current cell extends the smallest neighboring square by 1
                int minNeighborSquare = Math.min(squareFromLeft, 
                                       Math.min(squareFromTop, squareFromDiagonal));
                
                maxSquareSideAt[currentRow][currentCol] = minNeighborSquare + 1;
                largestSquareSide = Math.max(largestSquareSide, 
                                           maxSquareSideAt[currentRow][currentCol]);
            }
            // else maxSquareSideAt[currentRow][currentCol] remains 0
        }
    }
    
    // Return area (side * side)
    return largestSquareSide * largestSquareSide;
}
// Time: O(m × n), Space: O(m × n)

// Space optimized version using only previous row
public int maximalSquareOptimized(char[][] matrix) {
    if (matrix.length == 0) return 0;
    
    int totalRows = matrix.length;
    int totalCols = matrix[0].length;
    int[] previousRow = new int[totalCols + 1];  // +1 for easier boundary handling
    int[] currentRow = new int[totalCols + 1];
    int largestSquareSide = 0;
    
    for (int currentRowIdx = 1; currentRowIdx <= totalRows; currentRowIdx++) {
        for (int currentColIdx = 1; currentColIdx <= totalCols; currentColIdx++) {
            if (matrix[currentRowIdx - 1][currentColIdx - 1] == '1') {
                // Get values from three directions
                int fromLeft = currentRow[currentColIdx - 1];
                int fromTop = previousRow[currentColIdx];
                int fromDiagonal = previousRow[currentColIdx - 1];
                
                currentRow[currentColIdx] = Math.min(fromLeft, 
                                          Math.min(fromTop, fromDiagonal)) + 1;
                
                largestSquareSide = Math.max(largestSquareSide, currentRow[currentColIdx]);
            } else {
                currentRow[currentColIdx] = 0;
            }
        }
        // Swap arrays for next iteration
        int[] temp = previousRow;
        previousRow = currentRow;
        currentRow = temp;
        Arrays.fill(currentRow, 0);  // Reset current row
    }
    
    return largestSquareSide * largestSquareSide;
}
// Time: O(m × n), Space: O(n)

// Key insight: A square at (i,j) can only be as large as the smallest square 
// formed by its three neighbors (left, top, diagonal) plus 1
```

---

## 🏆 Interview Communication Tips

When using these templates in interviews:

1. **Explain Variable Names**: "I'm using `minCostToReach` to represent the minimum cost to reach each cell"

2. **Break Down Calculations**: "First I get `costFromTop` and `costFromLeft`, then add `currentCellCost`"

3. **Show Understanding**: "The `pathCount` array stores how many unique paths reach each cell"

4. **Be Consistent**: Use similar naming patterns across problems (e.g., always use `currentRow/currentCol`)

These optimized templates with descriptive variables make the code self-explanatory and easier to understand during interviews!
