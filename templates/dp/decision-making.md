# Decision Making DP Pattern - Complete Guide

## 🎯 Pattern Identification

### When to Use Decision Making Pattern:
- **Binary choice** at each element (take it or skip it)
- **Constraint** that prevents taking certain elements together
- **Sequential** processing of elements
- **Optimization** goal (maximize/minimize sum or count ways)

### Keywords That Indicate This Pattern:
- "can't take adjacent/consecutive"
- "rob houses" (can't rob adjacent)
- "delete and earn" (deleting X removes X-1 and X+1)
- "climb stairs" (can take 1 or 2 steps)
- "decode ways" (current choice affects what's valid next)

### Quick Recognition Test:
```
✓ Can I take or skip each element?
✓ Does taking one restrict others?
✓ Am I maximizing/minimizing?
→ Decision Making Pattern! ✅
```

---

## ⏱️ Time & Space Complexity

- **Time Complexity**: O(n) - Process each element once
- **Space Complexity**: O(1) - Only need two variables (optimized)
- **Before optimization**: O(n) space for DP array

---

## 📋 The Universal Template

```java
// COPY THIS TEMPLATE AND USE IT
public int decisionMakingDP(int[] nums, int start, int end) {
    // Edge case: empty range
    if (start >= end) return 0;
    
    // Edge case: single element
    if (end - start == 1) return nums[start];
    
    // Edge case: two elements - take the maximum
    if (end - start == 2) return Math.max(nums[start], nums[start + 1]);
    
    // Initialize for first two elements
    int prevTwo = nums[start];                          // dp[i-2]
    int prevOne = Math.max(nums[start], nums[start + 1]); // dp[i-1]
    
    // Process remaining elements
    for (int i = start + 2; i < end; i++) {
        // Core decision: take current + prevTwo OR skip current
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

## 📚 Example Problems Using This Template

### 1. House Robber (Basic)
```java
public int rob(int[] nums) {
    // Direct template usage - rob all houses
    return decisionMakingDP(nums, 0, nums.length);
}
```

### 2. House Robber II (Circular)
```java
public int rob(int[] nums) {
    if (nums.length == 1) return nums[0];
    
    // Case 1: Rob houses 0 to n-2 (exclude last)
    int robExcludingLast = decisionMakingDP(nums, 0, nums.length - 1);
    
    // Case 2: Rob houses 1 to n-1 (exclude first)
    int robExcludingFirst = decisionMakingDP(nums, 1, nums.length);
    
    return Math.max(robExcludingLast, robExcludingFirst);
}
```

### 3. Delete and Earn
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
    
    // Now it's a house robber problem!
    return decisionMakingDP(points, 0, points.length);
}
```

### 4. Maximum Sum Non-Adjacent
```java
public int maxSumNonAdjacent(int[] nums) {
    // Direct template usage
    return decisionMakingDP(nums, 0, nums.length);
}
```

### 5. House Robber with Range
```java
public int robRange(int[] nums, int start, int end) {
    // Template handles any range!
    return decisionMakingDP(nums, start, end);
}
```

---

## 🔄 Counting Variants (Different Goal, Same Pattern)

For problems that ask "how many ways" instead of "maximum value", we modify the template slightly:

### Template for Counting:
```java
public int countingDP(int n) {
    if (n <= 2) return n;
    
    int prevTwo = 1;  // ways to reach position i-2
    int prevOne = 2;  // ways to reach position i-1
    
    for (int i = 3; i <= n; i++) {
        // ADD instead of MAX for counting
        int current = prevTwo + prevOne;
        prevTwo = prevOne;
        prevOne = current;
    }
    
    return prevOne;
}
```

### 6. Climbing Stairs
```java
public int climbStairs(int n) {
    return countingDP(n);
}
```

### 7. Decode Ways

**Analysis**: This follows the counting DP pattern (similar to Climbing Stairs) but with validation checks. We can't use the template directly because:
- Need to validate if digits form valid letters (1-26)
- Leading zeros are invalid
- The recurrence depends on string content, not just position

**Custom Solution Required**:
```java
public int numDecodings(String s) {
    if (s.length() == 0 || s.charAt(0) == '0') return 0;
    
    int n = s.length();
    int prevTwo = 1;  // empty string has 1 way to decode
    int prevOne = 1;  // first character (already validated non-zero)
    
    for (int i = 2; i <= n; i++) {
        int current = 0;
        
        // Option 1: Decode current position as single digit (1-9)
        if (s.charAt(i-1) != '0') {
            current += prevOne;  // Add ways from previous position
        }
        
        // Option 2: Decode current + previous as two digits (10-26)
        int twoDigit = Integer.parseInt(s.substring(i-2, i));
        if (twoDigit >= 10 && twoDigit <= 26) {
            current += prevTwo;  // Add ways from two positions back
        }
        
        // Slide window (same as counting template)
        prevTwo = prevOne;
        prevOne = current;
    }
    
    return prevOne;
}
```

**Why it's still Decision Making pattern**:
- Core recurrence: `current = f(prevOne) + g(prevTwo)`
- Space optimization: Only track last two values
- But validation logic prevents direct template use

---

## 💡 Key Insights

### Why This Template Works:
1. **Optimal Substructure**: Best solution at position i depends on solutions at i-1 and i-2
2. **No Overlapping**: Once we decide for position i, it doesn't affect positions before i-2
3. **Space Optimization**: Only need last two values, not entire array

### The Core Formula:
```java
current = Math.max(
    nums[i] + prevTwo,  // take current (must skip previous)
    prevOne             // skip current (can take previous)
)
```

### For Counting Problems:
```java
current = prevTwo + prevOne  // Sum all ways instead of taking max
```

---

## 🎯 Quick Decision Guide

```
Problem Type                    | Use Template As-Is | Modification
-------------------------------|-------------------|---------------
Basic house robber             | YES               | None
Circular array                 | YES               | Call twice with different ranges
Value affects neighbors        | YES               | Transform array first
Count ways (not max)           | NO                | Change MAX to ADD
With additional constraints    | MAYBE             | Might need extra state
```

---

## 📝 Interview Tips

1. **Start with recognition**: "This is a decision making problem because we have binary choice with constraints"

2. **Mention the template**: "I'll use the standard decision making DP template"

3. **Explain the recurrence**: "At each position, we either take current + dp[i-2] or skip current and take dp[i-1]"

4. **Handle edge cases**: Template already handles them!

5. **Optimize space**: "We only need the last two values, so O(1) space"

---

## 🏆 Summary: Template Usage Guide

### ✅ Can Use Template Directly:
| Problem | Template | Call |
|---------|----------|------|
| House Robber | `decisionMakingDP` | `decisionMakingDP(nums, 0, nums.length)` |
| Max Sum Non-Adjacent | `decisionMakingDP` | `decisionMakingDP(nums, 0, nums.length)` |
| Climbing Stairs | `countingDP` | `countingDP(n)` |

### ✅ Can Use Template with Modification:
| Problem | How to Use |
|---------|------------|
| House Robber II | Call template twice with different ranges |
| Delete and Earn | Transform array first, then use template |
| Fibonacci | Modify initial values in counting template |

### ❌ Need Custom Solution (But Same Pattern):
| Problem | Why Can't Use Template | Still Follows Pattern? |
|---------|------------------------|----------------------|
| Decode Ways | Validation logic for digits | Yes - same recurrence |
| Paint House | Multiple colors/states | Yes - extended pattern |
| Stock with Cooldown | Complex state transitions | Yes - state machine |

### 🔑 Key Insight:
Even when we can't use the template directly, these problems still follow the Decision Making pattern:
- Space optimization with `prevOne` and `prevTwo`
- Recurrence relation based on previous states
- Sequential processing

The template gives us the framework - we just add problem-specific logic!
