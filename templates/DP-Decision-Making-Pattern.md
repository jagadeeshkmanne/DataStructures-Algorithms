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

### 4. **Counting Variants** (Same Pattern, Different Goal)
Instead of maximizing value, count number of ways. These follow the same pattern but use ADDITION instead of MAX.

#### Climbing Stairs
```java
public int climbStairs(int n) {
    if (n <= 2) return n;
    
    // Same structure as decisionMakingDP but with addition
    int twoBack = 1;  // ways to reach step 1
    int oneBack = 2;  // ways to reach step 2
    
    for (int i = 3; i <= n; i++) {
        // Core difference: ADD instead of MAX
        int current = twoBack + oneBack;
        twoBack = oneBack;
        oneBack = current;
    }
    
    return oneBack;
}
```

**Why not use universal template?**
- Universal template uses `Math.max()` for optimization
- Counting uses addition
- Same pattern, different operation

#### Decode Ways
```java
public int numDecodings(String s) {
    if (s.length() == 0 || s.charAt(0) == '0') return 0;
    
    int n = s.length();
    int twoBack = 1;  // empty string
    int oneBack = 1;  // first character
    
    for (int i = 2; i <= n; i++) {
        int current = 0;
        
        // Single digit decode
        if (s.charAt(i-1) != '0') {
            current += oneBack;  // ADD ways
        }
        
        // Two digit decode  
        int twoDigit = Integer.parseInt(s.substring(i-2, i));
        if (twoDigit >= 10 && twoDigit <= 26) {
            current += twoBack;  // ADD ways
        }
        
        twoBack = oneBack;
        oneBack = current;
    }
    
    return oneBack;
}
```

**Key Insight**: These problems follow the Decision Making pattern (binary choices affecting future) but optimize differently:
- **House Robber types**: Find maximum value → use MAX
- **Climbing Stairs types**: Count total ways → use SUM

**Example Problems:**
- [Climbing Stairs - LeetCode 70](https://leetcode.com/problems/climbing-stairs/) ⭐⭐⭐⭐⭐
- [Decode Ways - LeetCode 91](https://leetcode.com/problems/decode-ways/) ⭐⭐⭐⭐
- [Fibonacci Number - LeetCode 509](https://leetcode.com/problems/fibonacci-number/) ⭐⭐⭐

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
| Counting Ways | ⭐⭐⭐⭐⭐ | Easy | ADD instead of MAX |
| Decode Ways | ⭐⭐⭐ | Medium | Counting with validation |
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

### Optimization Problems:
1. **Start Here**: [House Robber](https://leetcode.com/problems/house-robber/)
2. **Then**: [Delete and Earn](https://leetcode.com/problems/delete-and-earn/)
3. **Finally**: [House Robber II](https://leetcode.com/problems/house-robber-ii/)

### Counting Problems:
1. **Start Here**: [Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)
2. **Then**: [Decode Ways](https://leetcode.com/problems/decode-ways/)

Master these 5 and you'll handle 95% of Decision Making DP problems!
