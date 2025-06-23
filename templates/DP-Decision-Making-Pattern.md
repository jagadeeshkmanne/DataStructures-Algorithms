# Dynamic Programming - Decision Making Pattern

## 🎯 Pattern Overview

The **Decision Making Pattern** is a Dynamic Programming approach where you make binary choices (include/exclude) at each element with constraints that affect future choices. The goal is to optimize (maximize/minimize) the total value.

### Core Concept:
```
At each position: Take it or Skip it
If you take it: You can't take certain future elements (constraint)
Goal: Find optimal total
```

---

## 🔍 Pattern Recognition

### Key Indicators:
- **Binary choice** at each element (include/exclude)
- **Constraint** that prevents taking certain elements together
- **Optimization** goal (maximize/minimize sum)
- **Sequential** processing of elements

### Magic Keywords:
| Keyword | Example |
|---------|---------|
| "can't take adjacent/consecutive" | House Robber |
| "affects neighboring values" | Delete and Earn |
| "circular array/constraint" | House Robber II |
| "no two adjacent" | Maximum Non-Adjacent Sum |

### Quick Test:
```
✓ Can I take or skip each element?
✓ Does taking one restrict others?
✓ Am I maximizing/minimizing?
→ Decision Making Pattern! ✅
```

---

## 🚀 Universal Template

### One Template for All Common Cases:

```java
public int decisionMakingDP(int[] nums, int start, int end) {
    // Edge case: empty range
    if (start >= end) return 0;
    
    // Edge case: single element
    if (end - start == 1) return nums[start];
    
    // Edge case: two elements - take the maximum
    if (end - start == 2) return Math.max(nums[start], nums[start + 1]);
    
    // Initialize for first two elements
    // prevTwo: best sum up to i-2
    // prevOne: best sum up to i-1
    int prevTwo = nums[start];
    int prevOne = Math.max(nums[start], nums[start + 1]);
    
    // Process remaining elements
    for (int i = start + 2; i < end; i++) {
        // Core formula: take current + prevTwo OR skip current
        int current = Math.max(
            nums[i] + prevTwo,  // take current element
            prevOne             // skip current element
        );
        
        // Slide the window
        prevTwo = prevOne;
        prevOne = current;
    }
    
    return prevOne;
}
```

---

## 📋 Common Problem Types

### 1. **Basic Adjacent Constraint**
Can't take adjacent elements.

```java
public int houseRobber(int[] nums) {
    return decisionMakingDP(nums, 0, nums.length);
}
```

**Example Problems:**
- [House Robber - LeetCode 198](https://leetcode.com/problems/house-robber/) ⭐⭐⭐⭐⭐
- Maximum Sum of Non-adjacent Elements
- [Maximum Sum with No Two Adjacent](https://practice.geeksforgeeks.org/problems/max-sum-without-adjacents/0)

---

### 2. **Circular Constraint**
First and last elements are also adjacent.

```java
public int houseRobberII(int[] nums) {
    // Edge case: single element
    if (nums.length == 1) return nums[0];
    
    // Try two scenarios:
    // 1. Exclude last element (can take first)
    int excludeLast = decisionMakingDP(nums, 0, nums.length - 1);
    
    // 2. Exclude first element (can take last)
    int excludeFirst = decisionMakingDP(nums, 1, nums.length);
    
    return Math.max(excludeLast, excludeFirst);
}
```

**Example Problems:**
- [House Robber II - LeetCode 213](https://leetcode.com/problems/house-robber-ii/) ⭐⭐⭐⭐
- Circular Array Maximum Sum

---

### 3. **Value-Based Constraint**
Taking value X affects all occurrences of X-1 and X+1.

```java
public int deleteAndEarn(int[] nums) {
    if (nums.length == 0) return 0;
    
    // Find max value to size our array
    int maxVal = 0;
    for (int num : nums) {
        maxVal = Math.max(maxVal, num);
    }
    
    // Transform: points[i] = sum of all occurrences of value i
    int[] points = new int[maxVal + 1];
    for (int num : nums) {
        points[num] += num;
    }
    
    // Now it's a basic adjacent constraint problem!
    return decisionMakingDP(points, 0, points.length);
}
```

**Example Problems:**
- [Delete and Earn - LeetCode 740](https://leetcode.com/problems/delete-and-earn/) ⭐⭐⭐⭐
- [Stickler Thief](https://practice.geeksforgeeks.org/problems/stickler-theif-1587115621/1)

---

## 💡 Tips & Strategies

### Recognition Tips:
1. **"Adjacent"** keyword → Basic template
2. **"Circular/Ring"** → Solve twice (exclude first/last)
3. **"Value affects others"** → Transform to adjacent problem
4. **"Pick elements"** + constraint → Check if Decision Making

### Common Mistakes to Avoid:
```java
// ❌ Wrong initialization
int prevOne = nums[start + 1];  // Should be max(nums[start], nums[start + 1])

// ❌ Forgetting edge cases
// Always handle: empty array, single element, two elements

// ❌ Off-by-one in circular
// Exclude last: [0, n-1)  NOT [0, n-2)
// Exclude first: [1, n)   NOT [2, n)
```

### Interview Tips:
1. **Start simple**: Explain the take/skip choice
2. **Draw example**: Show how prevTwo and prevOne work
3. **Mention optimization**: O(n) time, O(1) space
4. **Test edge cases**: [], [5], [1,2]

---

## 📊 Pattern Variations

| Type | Frequency | Difficulty | Key Insight |
|------|-----------|------------|-------------|
| Basic Adjacent | ⭐⭐⭐⭐⭐ | Easy | Direct template |
| Delete and Earn | ⭐⭐⭐⭐ | Medium | Transform first |
| Circular | ⭐⭐⭐ | Medium | Solve twice |
| K-distance | ⭐ | Hard | Different template |

---

## 🧠 Mental Model

Think of it as:
> "Walking down a street. At each house, decide: take the money (and skip some houses) or walk past. What's the maximum money?"

This works for ALL variations:
- **Basic**: Skip next house
- **Circular**: Street is a circle
- **Value-based**: Taking $3 removes all $2 and $4 houses

---

## 🎯 Quick Reference

### The Golden Formula:
```java
current = Math.max(nums[i] + prevTwo, prevOne)
```

### Problem Identification:
```
Adjacent constraint? → Basic template
Circular mentioned? → Solve twice
Value-based constraint? → Transform first
```

### Time & Space:
- Time: O(n) - single pass
- Space: O(1) - only two variables

---

## 📚 Practice Problems (In Order)

1. **Start Here**: [House Robber](https://leetcode.com/problems/house-robber/)
2. **Then**: [Delete and Earn](https://leetcode.com/problems/delete-and-earn/)
3. **Finally**: [House Robber II](https://leetcode.com/problems/house-robber-ii/)

Master these 3 and you'll handle 90% of Decision Making DP problems!
