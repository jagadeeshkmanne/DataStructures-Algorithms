# Dynamic Programming - Graph Pattern

## 🎯 Pattern Overview

The **Graph DP Pattern** combines graph traversal with dynamic programming. You explore a graph (often a 2D matrix) where each node's optimal value depends on its neighbors, but unlike regular traversal, you cache results to avoid recomputation.

### Core Concept:
```
Graph structure: Nodes with connections (often 2D grid)
DP aspect: Optimal value at each node depends on neighbors
Goal: Find longest path, count paths, or optimize traversal
Key: Use memoization with DFS or build dependency order
```

---

## 🔍 Pattern Recognition

### ✅ Quick Recognition Checklist
- [ ] Graph/matrix with traversal needed?
- [ ] Each position depends on neighbors?
- [ ] Need optimal (longest/shortest) path?
- [ ] Can revisit nodes but need to avoid recomputation?
→ If YES → Graph DP Pattern!

### Magic Keywords:
| Keyword | Example Problem |
|---------|----------------|
| "longest increasing path" in matrix | Longest Increasing Path in Matrix |
| "number of paths" with conditions | Unique Paths with Obstacles |
| "maximum gold" collection | Path with Maximum Gold |
| "cheapest path" with constraints | Minimum Path Cost in Grid |
| "word search" with DP | Word Break II |

### Quick Test:
```
✓ Do I traverse a graph/matrix?
✓ Does each node depend on neighbor values?
✓ Am I optimizing path length/count/sum?
→ Graph DP Pattern! ✅
```

---

## 🚀 Clean Reusable Templates

### Template 1: Longest Path in DAG (Most Common)

```java
class Solution {
    private int[][] memo;
    private int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};
    
    public int longestPath(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        memo = new int[m][n];
        
        return findMaxPathFromAllCells(matrix);
    }
    
    private int findMaxPathFromAllCells(int[][] matrix) {
        int maxPath = 0;
        
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                int pathFromThisCell = findLongestPathFrom(matrix, i, j);
                maxPath = Math.max(maxPath, pathFromThisCell);
            }
        }
        
        return maxPath;
    }
    
    private int findLongestPathFrom(int[][] matrix, int i, int j) {
        if (alreadyComputed(i, j)) {
            return memo[i][j];
        }
        
        int pathLength = calculateMaxPath(matrix, i, j);
        cacheResult(i, j, pathLength);
        
        return pathLength;
    }
    
    private int calculateMaxPath(int[][] matrix, int i, int j) {
        int maxLength = 1;  // At least the cell itself
        
        for (int[] dir : directions) {
            int neighborI = i + dir[0];
            int neighborJ = j + dir[1];
            
            if (canMoveToNeighbor(matrix, i, j, neighborI, neighborJ)) {
                int pathThroughNeighbor = 1 + findLongestPathFrom(matrix, neighborI, neighborJ);
                maxLength = Math.max(maxLength, pathThroughNeighbor);
            }
        }
        
        return maxLength;
    }
    
    private boolean alreadyComputed(int i, int j) {
        return memo[i][j] != 0;
    }
    
    private void cacheResult(int i, int j, int result) {
        memo[i][j] = result;
    }
    
    private boolean canMoveToNeighbor(int[][] matrix, int currentI, int currentJ, int neighborI, int neighborJ) {
        return isInBounds(matrix, neighborI, neighborJ) && 
               isIncreasingPath(matrix, currentI, currentJ, neighborI, neighborJ);
    }
    
    private boolean isInBounds(int[][] matrix, int i, int j) {
        return i >= 0 && i < matrix.length && j >= 0 && j < matrix[0].length;
    }
    
    private boolean isIncreasingPath(int[][] matrix, int fromI, int fromJ, int toI, int toJ) {
        return matrix[toI][toJ] > matrix[fromI][fromJ];
    }
}

// Direct usage:
public int longestIncreasingPath(int[][] matrix) {
    return longestPath(matrix);
}
```

---

### Template 2: Path Counting with Memoization

```java
class Solution {
    private int[][] memo;
    private int MOD = 1_000_000_007;
    private int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};
    
    public int countPaths(int[][] grid, int startX, int startY, int endX, int endY) {
        initializeMemo(grid);
        return countPathsFrom(grid, startX, startY, endX, endY);
    }
    
    private void initializeMemo(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        memo = new int[m][n];
        
        for (int i = 0; i < m; i++) {
            Arrays.fill(memo[i], -1);  // -1 means unvisited
        }
    }
    
    private int countPathsFrom(int[][] grid, int x, int y, int endX, int endY) {
        if (reachedDestination(x, y, endX, endY)) {
            return 1;
        }
        
        if (alreadyComputed(x, y)) {
            return memo[x][y];
        }
        
        int totalPaths = exploreAllDirections(grid, x, y, endX, endY);
        cacheResult(x, y, totalPaths);
        
        return totalPaths;
    }
    
    private int exploreAllDirections(int[][] grid, int x, int y, int endX, int endY) {
        int paths = 0;
        
        for (int[] dir : directions) {
            int nextX = x + dir[0];
            int nextY = y + dir[1];
            
            if (canMoveTo(grid, nextX, nextY)) {
                int pathsFromNeighbor = countPathsFrom(grid, nextX, nextY, endX, endY);
                paths = addPaths(paths, pathsFromNeighbor);
            }
        }
        
        return paths;
    }
    
    private boolean reachedDestination(int x, int y, int endX, int endY) {
        return x == endX && y == endY;
    }
    
    private boolean alreadyComputed(int x, int y) {
        return memo[x][y] != -1;
    }
    
    private void cacheResult(int x, int y, int paths) {
        memo[x][y] = paths;
    }
    
    private boolean canMoveTo(int[][] grid, int x, int y) {
        return isInBounds(grid, x, y) && isEmptyCell(grid, x, y);
    }
    
    private boolean isInBounds(int[][] grid, int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }
    
    private boolean isEmptyCell(int[][] grid, int x, int y) {
        return grid[x][y] == 0;  // Assuming 0 means empty
    }
    
    private int addPaths(int currentPaths, int newPaths) {
        return (currentPaths + newPaths) % MOD;
    }
}
```

---

### Template 3: Maximum Collection (Path with Backtracking)

```java
class Solution {
    private boolean[][] visited;
    private int maxGold;
    private int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};
    
    public int maxCollection(int[][] grid) {
        initializeSearch(grid);
        exploreAllStartingPoints(grid);
        return maxGold;
    }
    
    private void initializeSearch(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        visited = new boolean[m][n];
        maxGold = 0;
    }
    
    private void exploreAllStartingPoints(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (canStartFrom(grid, i, j)) {
                    explorePathFrom(grid, i, j, grid[i][j]);
                }
            }
        }
    }
    
    private void explorePathFrom(int[][] grid, int i, int j, int currentGold) {
        markVisited(i, j);
        updateMaximum(currentGold);
        
        exploreNeighbors(grid, i, j, currentGold);
        
        unmarkVisited(i, j);  // backtrack
    }
    
    private void exploreNeighbors(int[][] grid, int i, int j, int currentGold) {
        for (int[] dir : directions) {
            int nextI = i + dir[0];
            int nextJ = j + dir[1];
            
            if (canVisit(grid, nextI, nextJ)) {
                int goldAtNext = grid[nextI][nextJ];
                explorePathFrom(grid, nextI, nextJ, currentGold + goldAtNext);
            }
        }
    }
    
    private boolean canStartFrom(int[][] grid, int i, int j) {
        return hasGold(grid, i, j);
    }
    
    private boolean canVisit(int[][] grid, int i, int j) {
        return isInBounds(grid, i, j) && 
               hasGold(grid, i, j) && 
               !isVisited(i, j);
    }
    
    private boolean isInBounds(int[][] grid, int i, int j) {
        return i >= 0 && i < grid.length && j >= 0 && j < grid[0].length;
    }
    
    private boolean hasGold(int[][] grid, int i, int j) {
        return grid[i][j] != 0;
    }
    
    private boolean isVisited(int i, int j) {
        return visited[i][j];
    }
    
    private void markVisited(int i, int j) {
        visited[i][j] = true;
    }
    
    private void unmarkVisited(int i, int j) {
        visited[i][j] = false;
    }
    
    private void updateMaximum(int currentGold) {
        maxGold = Math.max(maxGold, currentGold);
    }
}

// Direct usage:
public int getMaximumGold(int[][] grid) {
    return maxCollection(grid);
}
```

---

### Template 4: Bottom-Up Graph DP (Topological Order)

```java
class Solution {
    private int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};
    
    public int graphDPBottomUp(int[][] matrix) {
        int[][] dp = initializeDP(matrix);
        List<int[]> cellsInOrder = getCellsInTopologicalOrder(matrix);
        
        return processAllCells(matrix, dp, cellsInOrder);
    }
    
    private int[][] initializeDP(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        return new int[m][n];
    }
    
    private List<int[]> getCellsInTopologicalOrder(int[][] matrix) {
        List<int[]> cells = collectAllCells(matrix);
        sortCellsByValue(cells, matrix);
        return cells;
    }
    
    private List<int[]> collectAllCells(int[][] matrix) {
        List<int[]> cells = new ArrayList<>();
        
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                cells.add(new int[]{i, j});
            }
        }
        
        return cells;
    }
    
    private void sortCellsByValue(List<int[]> cells, int[][] matrix) {
        // Process cells in increasing order of their values
        cells.sort((cell1, cell2) -> {
            int value1 = matrix[cell1[0]][cell1[1]];
            int value2 = matrix[cell2[0]][cell2[1]];
            return value1 - value2;
        });
    }
    
    private int processAllCells(int[][] matrix, int[][] dp, List<int[]> cells) {
        int maxPath = 0;
        
        for (int[] cell : cells) {
            int pathLength = calculateLongestPathEndingAt(matrix, dp, cell[0], cell[1]);
            maxPath = Math.max(maxPath, pathLength);
        }
        
        return maxPath;
    }
    
    private int calculateLongestPathEndingAt(int[][] matrix, int[][] dp, int i, int j) {
        int maxPathToHere = 1;  // At least the cell itself
        
        for (int[] dir : directions) {
            int prevI = i + dir[0];
            int prevJ = j + dir[1];
            
            if (canComeFrom(matrix, i, j, prevI, prevJ)) {
                int pathThroughPrev = dp[prevI][prevJ] + 1;
                maxPathToHere = Math.max(maxPathToHere, pathThroughPrev);
            }
        }
        
        dp[i][j] = maxPathToHere;
        return maxPathToHere;
    }
    
    private boolean canComeFrom(int[][] matrix, int currentI, int currentJ, int prevI, int prevJ) {
        return isInBounds(matrix, prevI, prevJ) && 
               hasValidTransition(matrix, prevI, prevJ, currentI, currentJ);
    }
    
    private boolean isInBounds(int[][] matrix, int i, int j) {
        return i >= 0 && i < matrix.length && j >= 0 && j < matrix[0].length;
    }
    
    private boolean hasValidTransition(int[][] matrix, int fromI, int fromJ, int toI, int toJ) {
        // Can come from smaller values
        return matrix[fromI][fromJ] < matrix[toI][toJ];
    }
}
```

---

## 📋 Common Problem Types

### 1. **Longest Increasing Path**
Find longest path where values strictly increase.

```java
public int longestIncreasingPath(int[][] matrix) {
    return longestPath(matrix);  // Use Template 1
}
```

**LeetCode Problems:**
- [Longest Increasing Path in a Matrix](https://leetcode.com/problems/longest-increasing-path-in-a-matrix/)

---

### 2. **Count Paths with Conditions**
Count number of valid paths from source to destination.

```java
public int uniquePathsIII(int[][] grid) {
    // Find start and end positions, then use Template 2
    return countPaths(grid, startX, startY, endX, endY);
}
```

**LeetCode Problems:**
- [Unique Paths III](https://leetcode.com/problems/unique-paths-iii/)
- [Number of Increasing Paths in a Grid](https://leetcode.com/problems/number-of-increasing-paths-in-a-grid/)

---

### 3. **Maximum Collection**
Collect maximum value along a path.

```java
public int getMaximumGold(int[][] grid) {
    return maxCollection(grid);  // Use Template 3
}
```

**LeetCode Problems:**
- [Path with Maximum Gold](https://leetcode.com/problems/path-with-maximum-gold/)
- [Cherry Pickup](https://leetcode.com/problems/cherry-pickup/)

---

## 💡 Key Insights

### DFS + Memoization vs Bottom-Up:

**Use DFS + Memo when:**
- Natural recursive structure
- Not all cells need to be computed
- Starting point varies

**Use Bottom-Up when:**
- Need all cell values
- Can determine processing order
- Want to avoid recursion stack

### Common Patterns:

1. **DAG Property**: Many graph DP problems have implicit DAG (like increasing paths)
2. **Multi-Source**: Often need to try all possible starting points
3. **Path Constraints**: Valid moves defined by problem (increasing, non-zero, etc.)

---

## 📊 Template Selection Guide

| Problem Type | Template | Key Feature |
|--------------|----------|-------------|
| Longest path with condition | Template 1 | DFS + memo from each cell |
| Count paths | Template 2 | DFS + memo with counter |
| Collect maximum | Template 3 | Backtracking + visited |
| Known dependency order | Template 4 | Bottom-up processing |

---

## 🧠 Mental Model

Think of Graph DP as:
> "Regular graph traversal but with a cache. Each cell remembers its optimal value so we don't recalculate."

Key differences from regular DFS:
- **Memoization**: Cache results
- **No visited set**: Can revisit (that's why we need memo)
- **Optimization goal**: Not just reaching, but optimizing

---

## 🎯 Quick Reference

### Common Edge Cases:
- Empty matrix
- Single cell
- No valid path exists
- All cells have same value

### Time & Space:
- Time: O(m × n) for most problems
- Space: O(m × n) for memoization
- Each cell visited once (thanks to memo)

---

## 📚 Practice Problems (In Order)

### Start Here:
1. **[Longest Increasing Path in a Matrix](https://leetcode.com/problems/longest-increasing-path-in-a-matrix/)** - Classic graph DP
2. **[Number of Increasing Paths in a Grid](https://leetcode.com/problems/number-of-increasing-paths-in-a-grid/)** - Count variant
3. **[Path with Maximum Gold](https://leetcode.com/problems/path-with-maximum-gold/)** - Collection variant

### Then Try:
4. **[Unique Paths III](https://leetcode.com/problems/unique-paths-iii/)** - Path counting with obstacles
5. **[Minimum Path Cost in a Grid](https://leetcode.com/problems/minimum-path-cost-in-a-grid/)** - Cost optimization

### Advanced:
6. **[Cherry Pickup](https://leetcode.com/problems/cherry-pickup/)** - Complex state
7. **[Dungeon Game](https://leetcode.com/problems/dungeon-game/)** - Reverse DP
8. **[Number of Paths with Max Score](https://leetcode.com/problems/number-of-paths-with-max-score/)** - Combined optimization

---

## 🎯 Interview Communication Script

```
"I notice this is a Graph DP problem because:
1. We have a graph/matrix to traverse
2. Each cell's optimal value depends on its neighbors
3. We need to find [longest path/count paths/maximum collection]

I'll use DFS with memoization because:
- It handles the recursive nature well
- Avoids recomputing the same cell
- Works from any starting point

For each cell, I'll:
1. Check if already computed (memoization)
2. Explore valid neighbors
3. Cache and return the optimal value

Time complexity: O(m×n), Space complexity: O(m×n)"
```

**Master these templates and you'll handle 95% of Graph DP problems!**
