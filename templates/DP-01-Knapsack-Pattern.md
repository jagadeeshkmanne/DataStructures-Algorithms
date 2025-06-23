# Dynamic Programming - 0/1 Knapsack Pattern

## 🎯 Pattern Overview

The **0/1 Knapsack Pattern** is a Dynamic Programming approach where you have limited resources and items with costs/benefits. For each item, you make a binary choice (take it or leave it) to optimize the total benefit while staying within resource constraints.

### Core Concept:
```
You have: Limited resources (capacity, budget, time)
Items have: Resource cost and benefit value
Goal: Maximize benefit without exceeding resource limit
Each item: Can be taken 0 or 1 times (not fractional)
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
| "partition array" | Partition Equal Subset |

### Quick Test:
```
✓ Do I have limited resources?
✓ Does each item consume resources?
✓ Am I trying to maximize/optimize?
✓ Can I take each item only once?
→ 0/1 Knapsack Pattern! ✅
```

---

## 🚀 Universal Template

### Generic Template for All Resource-Constraint Problems:

```java
public int resourceOptimizationDP(int[] resources, int[] values, int totalResources) {
    // Edge cases
    if (resources == null || resources.length == 0) return 0;
    if (totalResources <= 0) return 0;
    if (resources.length != values.length) {
        throw new IllegalArgumentException("Arrays must have same length");
    }
    
    int n = resources.length;
    // maxValue[r] = maximum value achievable with r resources
    int[] maxValue = new int[totalResources + 1];
    
    // Process each item
    for (int i = 0; i < n; i++) {
        // Skip invalid items
        if (resources[i] > totalResources || resources[i] < 0) continue;
        
        // Update all resource states (backward to avoid using item twice)
        for (int currentResources = totalResources; currentResources >= resources[i]; currentResources--) {
            // Option 1: Take current item
            int remainingResources = currentResources - resources[i];
            int valueIfTake = values[i] + maxValue[remainingResources];
            
            // Option 2: Skip current item
            int valueIfSkip = maxValue[currentResources];
            
            // Choose better option
            maxValue[currentResources] = Math.max(valueIfTake, valueIfSkip);
        }
    }
    
    return maxValue[totalResources];
}
```

---

## 📋 Common Problem Types

### 1. **Classic Knapsack**
Given items with weights and values, maximize value within weight capacity.

```java
public int knapsack(int[] weights, int[] values, int capacity) {
    return resourceOptimizationDP(weights, values, capacity);
}
```

**Example Problems:**
- [0/1 Knapsack](https://practice.geeksforgeeks.org/problems/0-1-knapsack-problem/0) ⭐⭐⭐⭐⭐
- Fractional Knapsack (Greedy, not DP)

---

### 2. **Subset Sum**
Can we select numbers that sum exactly to target?

```java
public boolean canMakeSum(int[] nums, int target) {
    // Transform to knapsack: resources = values = nums
    // If we can achieve value equal to target, return true
    return resourceOptimizationDP(nums, nums, target) == target;
}
```

**Example Problems:**
- [Subset Sum Problem](https://practice.geeksforgeeks.org/problems/subset-sum-problem/0) ⭐⭐⭐⭐
- [Target Sum - LeetCode 494](https://leetcode.com/problems/target-sum/) ⭐⭐⭐

---

### 3. **Partition Equal Subset Sum**
Can we partition array into two subsets with equal sum?

```java
public boolean canPartition(int[] nums) {
    int totalSum = 0;
    for (int num : nums) totalSum += num;
    
    // If total sum is odd, can't partition equally
    if (totalSum % 2 != 0) return false;
    
    // Find if we can make subset with sum = totalSum/2
    int target = totalSum / 2;
    return resourceOptimizationDP(nums, nums, target) == target;
}
```

**Example Problems:**
- [Partition Equal Subset Sum - LeetCode 416](https://leetcode.com/problems/partition-equal-subset-sum/) ⭐⭐⭐⭐
- [Last Stone Weight II - LeetCode 1049](https://leetcode.com/problems/last-stone-weight-ii/) ⭐⭐⭐

---

### 4. **Target Sum**
Assign + or - to each number to achieve target sum.

```java
public int findTargetSumWays(int[] nums, int target) {
    int sum = 0;
    for (int num : nums) sum += num;
    
    // Mathematical insight: 
    // Let P = sum of positive numbers, N = sum of negative numbers
    // P - N = target and P + N = sum
    // Therefore: P = (target + sum) / 2
    
    // Check if valid
    if ((target + sum) % 2 != 0 || target > sum || target < -sum) return 0;
    
    int newTarget = (target + sum) / 2;
    
    // Now count subsets with sum = newTarget
    // Need modified template that counts instead of maximizing
    return countSubsets(nums, newTarget);
}

private int countSubsets(int[] nums, int target) {
    int[] dp = new int[target + 1];
    dp[0] = 1; // One way to make sum 0: take nothing
    
    for (int num : nums) {
        for (int sum = target; sum >= num; sum--) {
            dp[sum] += dp[sum - num];
        }
    }
    
    return dp[target];
}
```

**Why this works:**
- Problem asks: How many ways to assign +/-
- We transform to: How many subsets sum to (target + totalSum) / 2
- This becomes a counting variant of subset sum

**Example Problems:**
- [Target Sum - LeetCode 494](https://leetcode.com/problems/target-sum/) ⭐⭐⭐⭐

---

## 💡 Tips & Strategies

### Recognition Tips:
1. **"Limited capacity/budget"** → Classic knapsack
2. **"Select elements to make sum"** → Subset sum variant
3. **"Partition/divide into groups"** → Often reduces to subset sum
4. **"Maximize/minimize with constraint"** → Resource optimization

### Common Mistakes to Avoid:
```java
// ❌ Wrong: Going forward in capacity loop
for (int r = resources[i]; r <= totalResources; r++) // May use item multiple times!

// ✅ Correct: Going backward
for (int r = totalResources; r >= resources[i]; r--) // Uses each item at most once

// ❌ Wrong: Not checking if item fits
if (resources[i] > currentResources) // Should skip this item

// ❌ Wrong: Modifying input arrays
Arrays.sort(resources); // Don't modify unless problem allows
```

### Interview Tips:
1. **Start with brute force**: Explain 2^n possibilities
2. **Identify overlapping subproblems**: Same capacity appears multiple times
3. **Build solution incrementally**: Start with capacity 0, build up
4. **Mention optimization**: Can optimize space for some variants

---

## 📊 Pattern Variations

| Type | Frequency | Difficulty | Key Insight |
|------|-----------|------------|-------------|
| Classic Knapsack | ⭐⭐⭐⭐⭐ | Medium | Direct template |
| Subset Sum | ⭐⭐⭐⭐ | Medium | Resources = values |
| Partition Problems | ⭐⭐⭐ | Medium | Transform to subset sum |
| Bounded Knapsack | ⭐⭐ | Hard | Multiple copies allowed |
| Unbounded Knapsack | ⭐⭐ | Medium | Infinite copies (different template) |

---

## 🧠 Mental Model

Think of it as:
> "You're shopping with limited money. Each item has a price (resource) and satisfaction value (benefit). What should you buy to maximize happiness within budget?"

This works for ALL variations:
- **Classic**: Weight limit in bag
- **Subset Sum**: Exact amount needed
- **Partition**: Divide resources equally

---

## 🎯 Quick Reference

### The Core Formula:
```java
maxValue[r] = Math.max(
    values[i] + maxValue[r - resources[i]],  // take item
    maxValue[r]                              // skip item
)
```

### Problem Identification:
```
Resource limit? → Knapsack pattern
Need exact sum? → Subset sum variant
Partition array? → Transform to subset sum
Optimize selection? → Classic knapsack
```

### Time & Space:
- Time: O(n × W) where n = items, W = total resources
- Space: O(W) for the DP array

---

## 📚 Practice Problems (In Order)

1. **Start Here**: [0/1 Knapsack (GFG)](https://practice.geeksforgeeks.org/problems/0-1-knapsack-problem/0)
2. **Then**: [Subset Sum Problem](https://practice.geeksforgeeks.org/problems/subset-sum-problem/0)
3. **Next**: [Partition Equal Subset Sum](https://leetcode.com/problems/partition-equal-subset-sum/)
4. **Advanced**: [Target Sum](https://leetcode.com/problems/target-sum/)

Master these 4 and you'll handle 90% of 0/1 Knapsack problems!

---

## 🔄 Related Patterns

- **Unbounded Knapsack**: Can use items multiple times (Coin Change)
- **Decision Making DP**: Adjacent constraint vs resource constraint
- **2D DP**: When you need to track two changing parameters
