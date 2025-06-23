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

## 🚀 0/1 Knapsack Template

### Use When:
- Each item can be used **at most once**
- Need to maximize value with limited resources

### Generic Template:

```java
public int knapsack01(int[] resources, int[] values, int totalResources) {
    // Edge cases
    if (resources == null || resources.length == 0) return 0;
    if (totalResources <= 0) return 0;
    
    int n = resources.length;
    int[] dp = new int[totalResources + 1];
    
    // Process each item
    for (int i = 0; i < n; i++) {
        // Backward iteration to avoid using item twice
        for (int r = totalResources; r >= resources[i]; r--) {
            dp[r] = Math.max(
                dp[r],                              // skip item
                values[i] + dp[r - resources[i]]    // take item
            );
        }
    }
    
    return dp[totalResources];
}
```

---

## 📋 0/1 Knapsack Problems

### 1. **Classic Knapsack**
```java
public int knapsack(int[] weights, int[] values, int capacity) {
    return knapsack01(weights, values, capacity);
}
```

### 2. **Subset Sum**
```java
public boolean canMakeSum(int[] nums, int target) {
    // Use same array for both resources and values
    return knapsack01(nums, nums, target) == target;
}
```

### 3. **Partition Equal Subset Sum**
```java
public boolean canPartition(int[] nums) {
    int sum = 0;
    for (int num : nums) sum += num;
    
    if (sum % 2 != 0) return false;
    
    return knapsack01(nums, nums, sum/2) == sum/2;
}
```

### 4. **Target Sum** (with modification for counting)
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

---

## 🚀 Unbounded Knapsack Template

### Use When:
- Each item can be used **unlimited times**
- Need to optimize with unlimited supply

### Generic Template:

```java
public int knapsackUnbounded(int[] resources, int[] values, int totalResources) {
    // Edge cases
    if (resources == null || resources.length == 0) return 0;
    if (totalResources <= 0) return 0;
    
    int n = resources.length;
    int[] dp = new int[totalResources + 1];
    
    // Process each resource amount
    for (int r = 1; r <= totalResources; r++) {
        for (int i = 0; i < n; i++) {
            if (resources[i] <= r) {
                dp[r] = Math.max(
                    dp[r],                              // skip item
                    values[i] + dp[r - resources[i]]    // take item (can reuse)
                );
            }
        }
    }
    
    return dp[totalResources];
}
```

---

## 📋 Unbounded Knapsack Problems

### 1. **Rod Cutting** (Direct use)
```java
public int rodCutting(int[] prices, int length) {
    // lengths: [1, 2, 3, ..., n]
    int[] lengths = new int[prices.length];
    for (int i = 0; i < prices.length; i++) {
        lengths[i] = i + 1;
    }
    
    return knapsackUnbounded(lengths, prices, length);
}
```

### 2. **Coin Change** (Minimum - needs modification)
```java
public int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1);
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

### 3. **Coin Change II** (Count ways - needs modification)
```java
public int change(int amount, int[] coins) {
    int[] dp = new int[amount + 1];
    dp[0] = 1;
    
    for (int coin : coins) {
        for (int amt = coin; amt <= amount; amt++) {
            dp[amt] += dp[amt - coin];
        }
    }
    
    return dp[amount];
}
```

---

## 💡 Key Differences

### Loop Direction is Critical:

```java
// 0/1 Knapsack - BACKWARD (no reuse)
for (int r = totalResources; r >= resources[i]; r--)

// Unbounded - FORWARD (allow reuse)
for (int r = resources[i]; r <= totalResources; r++)
```

### When Templates Don't Apply Directly:
- **Minimization**: Change `Math.max` to `Math.min`
- **Counting**: Change `Math.max` to `+=`
- **Different initialization**: Based on problem needs

---

## 📊 Pattern Summary

| Type | Items Usage | Loop Direction | Example |
|------|-------------|----------------|---------|
| **0/1 Knapsack** | Once | Backward | Classic Knapsack, Subset Sum |
| **Unbounded** | Unlimited | Forward | Coin Change, Rod Cutting |

### Problem Mapping:
- **Classic Knapsack** → 0/1 template directly
- **Subset Sum** → 0/1 template (resources = values)
- **Partition Equal** → 0/1 template with sum/2
- **Coin Change** → Unbounded with MIN
- **Coin Change II** → Unbounded with COUNT

---

## 🎯 Quick Reference

### Time & Space:
- Time: O(n × W) where n = items, W = total resources
- Space: O(W) for the DP array

### Remember:
1. **0/1**: Can't reuse → Backward loop
2. **Unbounded**: Can reuse → Forward loop
3. **Modifications**: MIN for minimization, += for counting

---

## 📚 Practice Problems

### 0/1 Knapsack:
1. [0/1 Knapsack (GFG)](https://practice.geeksforgeeks.org/problems/0-1-knapsack-problem/0)
2. [Partition Equal Subset Sum - LeetCode 416](https://leetcode.com/problems/partition-equal-subset-sum/)
3. [Target Sum - LeetCode 494](https://leetcode.com/problems/target-sum/)

### Unbounded Knapsack:
1. [Coin Change - LeetCode 322](https://leetcode.com/problems/coin-change/)
2. [Coin Change II - LeetCode 518](https://leetcode.com/problems/coin-change-2/)
3. [Perfect Squares - LeetCode 279](https://leetcode.com/problems/perfect-squares/)

Master these patterns and you'll handle 95% of knapsack problems!
