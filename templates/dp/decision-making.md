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

### ✅ Quick Recognition Checklist
- [ ] Binary choice at each element? (take/skip)
- [ ] Taking current affects future choices? (constraint)
- [ ] Optimization goal? (max/min/count)
→ If all YES → Decision Making Pattern!

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

## 🚀 Tabulation Approach (Bottom-Up)

### Universal Template for Most Cases:

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

## 🚀 Memoization Approach (Top-Down)

### Recursive Template with Caching:

```java
class DecisionMakingMemo {
    Map<Integer, Integer> memo = new HashMap<>();
    
    public int rob(int[] nums) {
        return robHelper(nums, 0);
    }
    
    private int robHelper(int[] nums, int index) {
        // Base cases
        if (index >= nums.length) return 0;
        
        // Check memo
        if (memo.containsKey(index)) return memo.get(index);
        
        // Choice: rob current house OR skip it
        int robCurrent = nums[index] + robHelper(nums, index + 2);
        int skipCurrent = robHelper(nums, index + 1);
        
        // Store result and return
        int result = Math.max(robCurrent, skipCurrent);
        memo.put(index, result);
        return result;
    }
}
```

### Alternative Memoization (Different State):

```java
class DecisionMakingMemo2 {
    Map<String, Integer> memo = new HashMap<>();
    
    private int robHelper(int[] nums, int index, boolean canTakePrev) {
        if (index >= nums.length) return 0;
        
        String key = index + "," + canTakePrev;
        if (memo.containsKey(key)) return memo.get(key);
        
        // Choice 1: Skip current
        int skip = robHelper(nums, index + 1, true);
        
        // Choice 2: Take current (only if allowed)
        int take = 0;
        if (canTakePrev) {
            take = nums[index] + robHelper(nums, index + 1, false);
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
- ✅ **Clear iterative pattern** (most Decision Making problems)
- ✅ **Need space optimization** (Google loves this follow-up)
- ✅ **Interview clarity** (easier to explain step by step)
- ✅ **All subproblems needed** (like basic House Robber)

### Use Memoization When:
- ✅ **Natural recursive thinking** (easier to conceptualize)
- ✅ **Complex state representation** (multiple parameters)
- ✅ **Not all subproblems needed** (sparse solution space)
- ✅ **Tree-based variations** (House Robber III)

### Interview Strategy:
```
1. Start with tabulation (clearer explanation)
2. Mention: "I can also solve this recursively with memoization"
3. If asked: Show memoization approach
4. Explain trade-offs when asked
```

---

## 📋 Common Problem Types

### 1. **Basic Adjacent Constraint**
Can't take adjacent elements.

```java
// Tabulation Version
public int houseRobber(int[] nums) {
    return decisionMakingDP(nums, 0, nums.length);
}

// Memoization Version  
public int houseRobberMemo(int[] nums) {
    DecisionMakingMemo solver = new DecisionMakingMemo();
    return solver.rob(nums);
}
```

**LeetCode Problems:**
- [House Robber](https://leetcode.com/problems/house-robber/)
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

**LeetCode Problems:**
- [House Robber II](https://leetcode.com/problems/house-robber-ii/)

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

**LeetCode Problems:**
- [Delete and Earn](https://leetcode.com/problems/delete-and-earn/)
- [Stickler Thief](https://practice.geeksforgeeks.org/problems/stickler-theif-1587115621/1)

---

### 4. **Counting Variants** (Same Pattern, Different Goal)
Instead of maximizing value, count number of ways. These follow the same pattern but use ADDITION instead of MAX.

#### Climbing Stairs (Tabulation)
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

#### Climbing Stairs (Memoization)
```java
class ClimbingStairsMemo {
    Map<Integer, Integer> memo = new HashMap<>();
    
    public int climbStairs(int n) {
        return climbHelper(n);
    }
    
    private int climbHelper(int n) {
        if (n <= 2) return n;
        if (memo.containsKey(n)) return memo.get(n);
        
        int result = climbHelper(n - 1) + climbHelper(n - 2);
        memo.put(n, result);
        return result;
    }
}
```

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

**LeetCode Problems:**
- [Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)
- [Decode Ways](https://leetcode.com/problems/decode-ways/)
- [Fibonacci Number](https://leetcode.com/problems/fibonacci-number/)

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

// ❌ Memoization without proper key
// Use unique key combining all state variables
```

### Interview Tips:
1. **Start simple**: Explain the take/skip choice
2. **Draw example**: Show how prevTwo and prevOne work
3. **Mention both approaches**: "I can solve this iteratively or recursively"
4. **Mention optimization**: O(n) time, O(1) space for tabulation
5. **Test edge cases**: [], [5], [1,2]

---

## 📊 Pattern Variations

| Type | Frequency | Difficulty | Best Approach | Key Insight |
|------|-----------|------------|---------------|-------------|
| Basic Adjacent | ⭐⭐⭐⭐⭐ | Easy | Tabulation | Direct template |
| Delete and Earn | ⭐⭐⭐⭐ | Medium | Tabulation | Transform first |
| Circular | ⭐⭐⭐ | Medium | Tabulation | Solve twice |
| Counting Ways | ⭐⭐⭐⭐⭐ | Easy | Either | ADD instead of MAX |
| Decode Ways | ⭐⭐⭐ | Medium | Tabulation | Counting with validation |
| Tree Variations | ⭐⭐ | Hard | Memoization | Natural recursion |

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

### The Golden Formula (Tabulation):
```java
current = Math.max(nums[i] + prevTwo, prevOne)
```

### The Golden Formula (Memoization):
```java
result = Math.max(
    nums[index] + solve(index + 2),  // take current
    solve(index + 1)                 // skip current
)
```

### Problem Identification:
```
Adjacent constraint? → Basic template
Circular mentioned? → Solve twice
Value-based constraint? → Transform first
Count ways? → Use ADD instead of MAX
```

### Time & Space:
- **Tabulation**: O(n) time, O(1) space
- **Memoization**: O(n) time, O(n) space

---

## 📚 Practice Problems (In Order)

### Start Here (Optimization Problems):
1. **[House Robber](https://leetcode.com/problems/house-robber/)** - Basic pattern
2. **[Delete and Earn](https://leetcode.com/problems/delete-and-earn/)** - Transform technique
3. **[House Robber II](https://leetcode.com/problems/house-robber-ii/)** - Circular constraint

### Then Try (Counting Problems):
4. **[Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)** - Count ways
5. **[Decode Ways](https://leetcode.com/problems/decode-ways/)** - Advanced counting

### Advanced (Optional):
6. **[House Robber III](https://leetcode.com/problems/house-robber-iii/)** - Tree DP variation

**Master these 5 core problems and you'll handle 95% of Decision Making DP problems!**

---

## 🎯 Interview Communication Script

```
"I notice this is a Decision Making DP problem because:
1. I have a binary choice at each element (take or skip)
2. Taking current element affects future choices (can't take adjacent)
3. I'm trying to optimize (maximize sum)

I'll solve this using dynamic programming with a bottom-up approach.
My state will be: dp[i] = maximum sum from elements 0 to i.
The recurrence is: dp[i] = max(nums[i] + dp[i-2], dp[i-1])

[Implement solution]

This can also be solved recursively with memoization if preferred.
Time complexity: O(n), Space complexity: O(1) for tabulation."
```
