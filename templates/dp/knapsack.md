# Dynamic Programming - Knapsack Patterns

## 🎯 Pattern Overview

The **Knapsack Pattern** is a Dynamic Programming approach where you have limited resources and items with costs/benefits. There are two main variants:

1. **0/1 Knapsack**: Each item can be taken 0 or 1 times
2. **Unbounded Knapsack**: Each item can be taken unlimited times

### Core Concept:
```
You have: Limited resources (capacity, budget, time)
Items have: Resource cost and benefit value
Goal: Maximize benefit without exceeding resource limit
Difference: How many times you can use each item
```

---

## 🔍 Pattern Recognition

### ✅ Quick Recognition Checklist
- [ ] Limited resource constraint? (capacity/budget/weight)
- [ ] Items with cost and value?
- [ ] Include/exclude items to optimize?
→ If all YES → Knapsack Pattern!

### Key Indicators:
- **Limited resource** constraint (weight, space, money, time)
- **Items with two properties**: cost (resource consumption) and benefit
- **Binary choice** for each item (include or exclude)
- **Optimization goal** (maximize value/benefit)

### Magic Keywords:
| Keyword | Example |
|---------|---------|
| "capacity", "weight limit" | Classic Knapsack |
| "budget", "cost constraint" | Shopping/Course Selection |
| "select items to maximize" | Resource Optimization |
| "subset sum equals target" | Subset Sum Problem |
| "use items multiple times" | Unbounded Knapsack |
| "minimum coins" | Coin Change (Unbounded) |

### Quick Test:
```
✓ Do I have limited resources?
✓ Does each item consume resources?
✓ Am I trying to maximize/optimize?
✓ Can I take each item only once? → 0/1 Knapsack
✓ Can I take items multiple times? → Unbounded Knapsack
```

---

## 🚀 0/1 Knapsack - Tabulation Approach

### Use When:
- Each item can be used **at most once**
- Need to maximize value with limited resources

### Tabulation Template (Space Optimized):

```java
public int knapsack01Tabulation(int[] weights, int[] values, int capacity) {
    // Edge cases
    if (weights == null || weights.length == 0) return 0;
    if (capacity <= 0) return 0;
    
    int n = weights.length;
    int[] dp = new int[capacity + 1];
    
    // Process each item
    for (int i = 0; i < n; i++) {
        // Backward iteration to avoid using item twice
        for (int w = capacity; w >= weights[i]; w--) {
            dp[w] = Math.max(
                dp[w],                              // skip item
                values[i] + dp[w - weights[i]]      // take item
            );
        }
    }
    
    return dp[capacity];
}
```

### Tabulation Template (2D - For Understanding):

```java
public int knapsack01Full(int[] weights, int[] values, int capacity) {
    int n = weights.length;
    int[][] dp = new int[n + 1][capacity + 1];
    
    for (int i = 1; i <= n; i++) {
        for (int w = 1; w <= capacity; w++) {
            if (weights[i-1] <= w) {
                dp[i][w] = Math.max(
                    dp[i-1][w],                                    // skip item
                    values[i-1] + dp[i-1][w - weights[i-1]]      // take item
                );
            } else {
                dp[i][w] = dp[i-1][w];  // can't take item
            }
        }
    }
    
    return dp[n][capacity];
}
```

---

## 🚀 0/1 Knapsack - Memoization Approach

### Recursive Template with Caching:

```java
class Knapsack01Memo {
    Map<String, Integer> memo = new HashMap<>();
    
    public int knapsack(int[] weights, int[] values, int capacity) {
        return knapsackHelper(weights, values, 0, capacity);
    }
    
    private int knapsackHelper(int[] weights, int[] values, int index, int remainingCapacity) {
        // Base cases
        if (index >= weights.length || remainingCapacity <= 0) {
            return 0;
        }
        
        // Check memo
        String key = index + "," + remainingCapacity;
        if (memo.containsKey(key)) return memo.get(key);
        
        // Choice 1: Skip current item
        int skip = knapsackHelper(weights, values, index + 1, remainingCapacity);
        
        // Choice 2: Take current item (if it fits)
        int take = 0;
        if (weights[index] <= remainingCapacity) {
            take = values[index] + knapsackHelper(weights, values, index + 1, 
                                                remainingCapacity - weights[index]);
        }
        
        int result = Math.max(skip, take);
        memo.put(key, result);
        return result;
    }
}
```

---

## 🚀 Unbounded Knapsack - Tabulation Approach

### Use When:
- Each item can be used **unlimited times**
- Need to optimize with unlimited supply

### Tabulation Template:

```java
public int knapsackUnboundedTabulation(int[] weights, int[] values, int capacity) {
    // Edge cases
    if (weights == null || weights.length == 0) return 0;
    if (capacity <= 0) return 0;
    
    int n = weights.length;
    int[] dp = new int[capacity + 1];
    
    // Process each capacity amount
    for (int w = 1; w <= capacity; w++) {
        for (int i = 0; i < n; i++) {
            if (weights[i] <= w) {
                dp[w] = Math.max(
                    dp[w],                              // skip item
                    values[i] + dp[w - weights[i]]      // take item (can reuse)
                );
            }
        }
    }
    
    return dp[capacity];
}
```

### Alternative Tabulation (Item-wise):

```java
public int knapsackUnboundedAlt(int[] weights, int[] values, int capacity) {
    int[] dp = new int[capacity + 1];
    
    // Process each item
    for (int i = 0; i < weights.length; i++) {
        // Forward iteration allows reuse
        for (int w = weights[i]; w <= capacity; w++) {
            dp[w] = Math.max(dp[w], values[i] + dp[w - weights[i]]);
        }
    }
    
    return dp[capacity];
}
```

---

## 🚀 Unbounded Knapsack - Memoization Approach

### Recursive Template with Caching:

```java
class KnapsackUnboundedMemo {
    Map<String, Integer> memo = new HashMap<>();
    
    public int knapsack(int[] weights, int[] values, int capacity) {
        return knapsackHelper(weights, values, 0, capacity);
    }
    
    private int knapsackHelper(int[] weights, int[] values, int index, int remainingCapacity) {
        // Base cases
        if (remainingCapacity <= 0) return 0;
        if (index >= weights.length) return 0;
        
        // Check memo
        String key = index + "," + remainingCapacity;
        if (memo.containsKey(key)) return memo.get(key);
        
        // Choice 1: Skip current item type
        int skip = knapsackHelper(weights, values, index + 1, remainingCapacity);
        
        // Choice 2: Take current item (stay at same index for reuse)
        int take = 0;
        if (weights[index] <= remainingCapacity) {
            take = values[index] + knapsackHelper(weights, values, index, 
                                                remainingCapacity - weights[index]);
        }
        
        int result = Math.max(skip, take);
        memo.put(key, result);
        return result;
    }
}
```

---

## 🎯 When to Use Which Approach

### Use Tabulation When:
- ✅ **Standard knapsack problems** (most common)
- ✅ **Need space optimization** (1D array)
- ✅ **Interview clarity** (easier to explain)
- ✅ **All subproblems needed**

### Use Memoization When:
- ✅ **Complex constraints** (multiple parameters)
- ✅ **Natural recursive thinking**
- ✅ **Not all subproblems needed** (sparse solution space)
- ✅ **Easier to handle edge cases**

### Key Difference Between 0/1 and Unbounded:
```java
// 0/1 Knapsack - BACKWARD iteration (no reuse)
for (int w = capacity; w >= weights[i]; w--)

// Unbounded - FORWARD iteration (allow reuse)  
for (int w = weights[i]; w <= capacity; w++)
```

---

## 📋 0/1 Knapsack Problem Types

### 1. **Classic Knapsack**
```java
public int knapsack(int[] weights, int[] values, int capacity) {
    return knapsack01Tabulation(weights, values, capacity);
}
```

**LeetCode Problems:**
- [0/1 Knapsack (GFG)](https://practice.geeksforgeeks.org/problems/0-1-knapsack-problem/0)

### 2. **Subset Sum**
```java
public boolean canMakeSum(int[] nums, int target) {
    int[] dp = new boolean[target + 1];
    dp[0] = true;
    
    for (int num : nums) {
        for (int t = target; t >= num; t--) {
            dp[t] = dp[t] || dp[t - num];
        }
    }
    
    return dp[target];
}
```

**LeetCode Problems:**
- [Subset Sum Problem](https://practice.geeksforgeeks.org/problems/subset-sum-problem-1611555638/1)

### 3. **Partition Equal Subset Sum**
```java
public boolean canPartition(int[] nums) {
    int sum = 0;
    for (int num : nums) sum += num;
    
    if (sum % 2 != 0) return false;
    
    return canMakeSum(nums, sum / 2);
}
```

**LeetCode Problems:**
- [Partition Equal Subset Sum](https://leetcode.com/problems/partition-equal-subset-sum/)

### 4. **Target Sum** (Counting Variation)
```java
public int findTargetSumWays(int[] nums, int target) {
    int sum = 0;
    for (int num : nums) sum += num;
    
    if ((target + sum) % 2 != 0 || Math.abs(target) > sum) return 0;
    
    int newTarget = (target + sum) / 2;
    
    // Count subsets - modified 0/1 knapsack
    int[] dp = new int[newTarget + 1];
    dp[0] = 1;
    
    for (int num : nums) {
        for (int t = newTarget; t >= num; t--) {
            dp[t] += dp[t - num];  // ADD for counting
        }
    }
    
    return dp[newTarget];
}
```

**LeetCode Problems:**
- [Target Sum](https://leetcode.com/problems/target-sum/)

---

## 📋 Unbounded Knapsack Problem Types

### 1. **Coin Change** (Minimum - Optimization)
```java
public int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1);  // Initialize with impossible value
    dp[0] = 0;
    
    for (int amt = 1; amt <= amount; amt++) {
        for (int coin : coins) {
            if (coin <= amt) {
                dp[amt] = Math.min(dp[amt], 1 + dp[amt - coin]);
            }
        }
    }
    
    return dp[amount] > amount ? -1 : dp[amount];
}
```

**LeetCode Problems:**
- [Coin Change](https://leetcode.com/problems/coin-change/)

### 2. **Coin Change II** (Count Ways)
```java
public int change(int amount, int[] coins) {
    int[] dp = new int[amount + 1];
    dp[0] = 1;
    
    // Important: iterate coins first to avoid counting duplicates
    for (int coin : coins) {
        for (int amt = coin; amt <= amount; amt++) {
            dp[amt] += dp[amt - coin];
        }
    }
    
    return dp[amount];
}
```

**LeetCode Problems:**
- [Coin Change II](https://leetcode.com/problems/coin-change-2/)

### 3. **Rod Cutting**
```java
public int rodCutting(int[] prices, int length) {
    int[] dp = new int[length + 1];
    
    for (int len = 1; len <= length; len++) {
        for (int i = 0; i < prices.length && i + 1 <= len; i++) {
            dp[len] = Math.max(dp[len], prices[i] + dp[len - (i + 1)]);
        }
    }
    
    return dp[length];
}
```

### 4. **Perfect Squares**
```java
public int numSquares(int n) {
    int[] dp = new int[n + 1];
    Arrays.fill(dp, n + 1);
    dp[0] = 0;
    
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j * j <= i; j++) {
            dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
        }
    }
    
    return dp[n];
}
```

**LeetCode Problems:**
- [Perfect Squares](https://leetcode.com/problems/perfect-squares/)

---

## 💡 Tips & Strategies

### Recognition Tips:
1. **"Capacity/Weight"** → 0/1 Knapsack
2. **"Unlimited/Multiple times"** → Unbounded Knapsack
3. **"Subset sum"** → 0/1 Knapsack variant
4. **"Coin change"** → Unbounded Knapsack
5. **"Count ways"** → Modify template to use addition

### Common Mistakes to Avoid:
```java
// ❌ Wrong loop direction
// 0/1: Must iterate backwards to avoid reuse
for (int w = weights[i]; w <= capacity; w++)  // WRONG for 0/1

// Unbounded: Must iterate forwards to allow reuse
for (int w = capacity; w >= weights[i]; w--)  // WRONG for Unbounded

// ❌ Wrong initialization for minimization
int[] dp = new int[amount + 1];  // Should fill with large value

// ❌ Forgetting base case
dp[0] = 1;  // Often needed for counting problems
```

### Interview Tips:
1. **Clarify constraints**: Can items be reused?
2. **Choose approach**: "I'll use tabulation for clarity"
3. **Explain loop direction**: Why backward for 0/1, forward for unbounded
4. **Mention variations**: Counting vs optimization
5. **Space optimization**: Can reduce 2D to 1D

---

## 📊 Pattern Summary

| Type | Items Usage | Loop Direction | Best For | Example |
|------|-------------|----------------|----------|---------|
| **0/1 Knapsack** | Once | Backward | Subset problems | Partition Equal Sum |
| **Unbounded** | Unlimited | Forward | Coin problems | Coin Change |

### Problem Mapping Quick Guide:
- **"Subset sum"** → 0/1 Knapsack with boolean DP
- **"Partition equal"** → 0/1 Knapsack with sum/2 target
- **"Target sum"** → 0/1 Knapsack with counting (+= instead of max)
- **"Coin change minimum"** → Unbounded with Math.min
- **"Coin change count"** → Unbounded with counting
- **"Perfect squares"** → Unbounded with squares as items

---

## 🎯 Quick Reference

### Time & Space Complexity:
- **Time**: O(n × W) where n = items, W = capacity
- **Space**: O(W) for 1D DP, O(n × W) for 2D DP

### The Golden Rules:
1. **0/1 Knapsack**: Can't reuse → Backward loop
2. **Unbounded**: Can reuse → Forward loop  
3. **Counting**: Use += instead of Math.max
4. **Minimization**: Use Math.min with proper initialization

### Loop Templates:
```java
// 0/1 Knapsack
for (int i = 0; i < items; i++) {
    for (int w = capacity; w >= weights[i]; w--) {
        dp[w] = Math.max(dp[w], values[i] + dp[w - weights[i]]);
    }
}

// Unbounded Knapsack
for (int w = 1; w <= capacity; w++) {
    for (int i = 0; i < items; i++) {
        if (weights[i] <= w) {
            dp[w] = Math.max(dp[w], values[i] + dp[w - weights[i]]);
        }
    }
}
```

---

## 📚 Practice Problems (In Order)

### Start Here (0/1 Knapsack):
1. **[Partition Equal Subset Sum](https://leetcode.com/problems/partition-equal-subset-sum/)** - Boolean DP
2. **[Target Sum](https://leetcode.com/problems/target-sum/)** - Counting variant
3. **[0/1 Knapsack (GFG)](https://practice.geeksforgeeks.org/problems/0-1-knapsack-problem/0)** - Classic

### Then Try (Unbounded Knapsack):
4. **[Coin Change](https://leetcode.com/problems/coin-change/)** - Minimization
5. **[Coin Change II](https://leetcode.com/problems/coin-change-2/)** - Counting
6. **[Perfect Squares](https://leetcode.com/problems/perfect-squares/)** - Mathematical variant

### Advanced:
7. **[Combination Sum IV](https://leetcode.com/problems/combination-sum-iv/)** - Order matters
8. **[Word Break](https://leetcode.com/problems/word-break/)** - String variant

**Master these 6 core problems and you'll handle 95% of Knapsack DP problems!**

---

## 🎯 Interview Communication Script

```
"I recognize this as a Knapsack problem because:
1. I have limited resources (capacity/budget)
2. Items have cost and value properties  
3. I need to optimize by including/excluding items

Since each item can be used [once/multiple times], I'll use [0/1/Unbounded] Knapsack.

I'll solve using dynamic programming with tabulation.
State: dp[w] = maximum value achievable with capacity w
Transition: For each item, decide to take it or skip it
Loop direction: [Backward for 0/1 / Forward for Unbounded] to [prevent/allow] reuse

[Implement solution]

Time: O(n×W), Space: O(W) with space optimization.
Could also solve with memoization if recursion feels more natural."
```
