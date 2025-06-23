# Dynamic Programming - LIS Pattern

## 🎯 Pattern Overview

The **LIS (Longest Increasing Subsequence) Pattern** is for problems where you need to find the longest subsequence with a specific property (usually increasing). The key insight is that for each element, you check all previous elements that can form a valid subsequence.

### Core Concept:
```
Array/sequence: Elements to choose from
Goal: Find longest subsequence with property
Key: For each element, check all valid previous elements
Formula: dp[i] = longest valid subsequence ending at index i
```

---

## 🔍 Pattern Recognition

### ✅ Quick Recognition Checklist
- [ ] Need to find longest subsequence?
- [ ] Elements must follow certain order/property?
- [ ] Can skip elements (not subarray)?
- [ ] Order matters but not continuity?
→ If YES → LIS Pattern!

### Magic Keywords:
| Keyword | Example Problem |
|---------|----------------|
| "longest increasing subsequence" | Classic LIS |
| "longest chain" | Maximum Length of Pair Chain |
| "russian doll" | Russian Doll Envelopes |
| "maximum height by stacking" | Maximum Height by Stacking Cuboids |
| "longest divisible subset" | Largest Divisible Subset |

### Quick Test:
```
✓ Finding longest sequence?
✓ Can skip elements?
✓ Must maintain order/property?
→ LIS Pattern! ✅
```

---

## 🚀 Clean Reusable Templates

### Template 1: Classic LIS (O(n²) Solution)

```java
class Solution {
    public int longestIncreasingSubsequence(int[] nums) {
        if (nums.length == 0) return 0;
        
        int[] dp = new int[nums.length];
        initializeBaseCase(dp);
        
        int globalMax = 1;
        
        for (int i = 1; i < nums.length; i++) {
            int maxLength = findMaxValidPrevious(nums, dp, i);
            dp[i] = maxLength;
            globalMax = Math.max(globalMax, dp[i]);
        }
        
        return globalMax;
    }
    
    private void initializeBaseCase(int[] dp) {
        // Each element is a subsequence of length 1
        Arrays.fill(dp, 1);
    }
    
    private int findMaxValidPrevious(int[] nums, int[] dp, int current) {
        int maxLength = 1;  // At least the element itself
        
        for (int prev = 0; prev < current; prev++) {
            if (canExtendSubsequence(nums, prev, current)) {
                int lengthIfExtended = dp[prev] + 1;
                maxLength = Math.max(maxLength, lengthIfExtended);
            }
        }
        
        return maxLength;
    }
    
    private boolean canExtendSubsequence(int[] nums, int prev, int current) {
        // Check if we can add current after prev
        return nums[prev] < nums[current];
    }
}

// Direct usage:
public int lengthOfLIS(int[] nums) {
    return longestIncreasingSubsequence(nums);
}
```

---

### Template 2: LIS with Binary Search (O(n log n) Solution)

```java
class Solution {
    public int lisOptimized(int[] nums) {
        List<Integer> subsequence = new ArrayList<>();
        
        for (int num : nums) {
            updateSubsequence(subsequence, num);
        }
        
        return subsequence.size();
    }
    
    private void updateSubsequence(List<Integer> subseq, int num) {
        int position = findPosition(subseq, num);
        
        if (shouldAppend(subseq, position)) {
            subseq.add(num);
        } else {
            replaceElement(subseq, position, num);
        }
    }
    
    private int findPosition(List<Integer> subseq, int target) {
        // Binary search for leftmost position where target can be placed
        int left = 0, right = subseq.size();
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (subseq.get(mid) < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        return left;
    }
    
    private boolean shouldAppend(List<Integer> subseq, int position) {
        return position == subseq.size();
    }
    
    private void replaceElement(List<Integer> subseq, int position, int num) {
        subseq.set(position, num);
    }
}

// Direct usage:
public int lengthOfLIS(int[] nums) {
    return lisOptimized(nums);
}
```

---

### Template 3: LIS with Path Reconstruction

```java
class Solution {
    public List<Integer> longestIncreasingSubsequenceWithPath(int[] nums) {
        if (nums.length == 0) return new ArrayList<>();
        
        int[] dp = new int[nums.length];
        int[] parent = new int[nums.length];
        
        initializeArrays(dp, parent);
        
        int endIndex = findLISEndIndex(nums, dp, parent);
        
        return reconstructPath(nums, parent, endIndex);
    }
    
    private void initializeArrays(int[] dp, int[] parent) {
        Arrays.fill(dp, 1);
        Arrays.fill(parent, -1);  // No parent initially
    }
    
    private int findLISEndIndex(int[] nums, int[] dp, int[] parent) {
        int maxLength = 1;
        int endIndex = 0;
        
        for (int i = 1; i < nums.length; i++) {
            updateDPAndParent(nums, dp, parent, i);
            
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                endIndex = i;
            }
        }
        
        return endIndex;
    }
    
    private void updateDPAndParent(int[] nums, int[] dp, int[] parent, int current) {
        for (int prev = 0; prev < current; prev++) {
            if (canExtend(nums, prev, current) && wouldImprove(dp, prev, current)) {
                dp[current] = dp[prev] + 1;
                parent[current] = prev;
            }
        }
    }
    
    private boolean canExtend(int[] nums, int prev, int current) {
        return nums[prev] < nums[current];
    }
    
    private boolean wouldImprove(int[] dp, int prev, int current) {
        return dp[prev] + 1 > dp[current];
    }
    
    private List<Integer> reconstructPath(int[] nums, int[] parent, int index) {
        List<Integer> path = new ArrayList<>();
        
        while (index != -1) {
            path.add(0, nums[index]);  // Add to front
            index = parent[index];
        }
        
        return path;
    }
}
```

---

### Template 4: LIS Variants (Different Properties)

```java
class Solution {
    // Template for variants like divisible subset, chain problems, etc.
    public int lisVariant(int[][] items) {
        // First, sort if needed
        sortByProperty(items);
        
        int n = items.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        
        int globalMax = 1;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (canFormSequence(items, j, i)) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            globalMax = Math.max(globalMax, dp[i]);
        }
        
        return globalMax;
    }
    
    private void sortByProperty(int[][] items) {
        // Sort by first dimension, then by second if needed
        Arrays.sort(items, (a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            return a[1] - b[1];  // or b[1] - a[1] based on problem
        });
    }
    
    private boolean canFormSequence(int[][] items, int prev, int current) {
        // Problem-specific condition
        // Examples:
        // - Envelopes: items[prev][0] < items[current][0] && items[prev][1] < items[current][1]
        // - Pairs: items[prev][1] < items[current][0]
        // - Divisible: items[current] % items[prev] == 0
        return true;  // Placeholder
    }
}
```

---

## 📋 Common Problem Types

### 1. **Classic LIS**
Find longest strictly increasing subsequence.

```java
public int lengthOfLIS(int[] nums) {
    return longestIncreasingSubsequence(nums);  // Template 1 or 2
}
```

**LeetCode Problems:**
- [Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/)
- [Number of Longest Increasing Subsequence](https://leetcode.com/problems/number-of-longest-increasing-subsequence/)

---

### 2. **Russian Doll Envelopes**
2D version of LIS.

```java
public int maxEnvelopes(int[][] envelopes) {
    // Sort by width ascending, height descending for same width
    Arrays.sort(envelopes, (a, b) -> {
        if (a[0] != b[0]) return a[0] - b[0];
        return b[1] - a[1];  // Descending height
    });
    
    // LIS on heights
    int[] heights = new int[envelopes.length];
    for (int i = 0; i < envelopes.length; i++) {
        heights[i] = envelopes[i][1];
    }
    
    return lisOptimized(heights);  // Template 2
}
```

**LeetCode Problems:**
- [Russian Doll Envelopes](https://leetcode.com/problems/russian-doll-envelopes/)

---

### 3. **Maximum Length of Pair Chain**
Find longest chain where pair[i][1] < pair[j][0].

```java
public int findLongestChain(int[][] pairs) {
    Arrays.sort(pairs, (a, b) -> a[0] - b[0]);
    return lisVariant(pairs);  // Template 4 with chain condition
}
```

**LeetCode Problems:**
- [Maximum Length of Pair Chain](https://leetcode.com/problems/maximum-length-of-pair-chain/)

---

### 4. **Largest Divisible Subset**
Find largest subset where every pair is divisible.

```java
public List<Integer> largestDivisibleSubset(int[] nums) {
    Arrays.sort(nums);  // Important!
    // Use Template 3 with divisibility condition
}
```

**LeetCode Problems:**
- [Largest Divisible Subset](https://leetcode.com/problems/largest-divisible-subset/)

---

## 💡 Key Insights

### The Core Pattern:
```java
for (int i = 1; i < n; i++) {
    for (int j = 0; j < i; j++) {
        if (canExtend(j, i)) {
            dp[i] = Math.max(dp[i], dp[j] + 1);
        }
    }
}
```

### Common Variations:

1. **Different Properties**: Change `canExtend()` condition
2. **Multiple Dimensions**: Sort first, then apply LIS
3. **Count Instead of Length**: Track count alongside length
4. **Path Reconstruction**: Keep parent pointers

### O(n log n) Optimization:
- Use binary search + patience sorting
- Doesn't give actual subsequence easily
- Perfect when only length needed

---

## 📊 Template Selection Guide

| Problem Type | Template | Time Complexity |
|--------------|----------|-----------------|
| Basic LIS, need flexibility | Template 1 | O(n²) |
| Only need length, want speed | Template 2 | O(n log n) |
| Need actual subsequence | Template 3 | O(n²) |
| Special conditions | Template 4 | O(n²) |

---

## 🧠 Mental Model

Think of LIS as:
> "For each element, look at all previous elements that can come before it in a valid subsequence. Take the best option and extend."

Key insights:
- **Not contiguous**: Can skip elements
- **Order matters**: Must maintain relative positions
- **Optimal substructure**: Best ending at i depends on best ending before i

---

## 🎯 Quick Reference

### When to Use LIS:
```
"Longest" + "subsequence" → LIS
"Maximum chain/subset" → LIS variant
Multiple dimensions → Sort + LIS
Need to skip elements → LIS (not sliding window)
```

### Common Tricks:
- Sort first for subset/chain problems
- For 2D: Sort by one dimension, LIS on other
- For count: Track both length and count
- For non-strict: Change < to <=

### Time & Space:
- Classic: O(n²) time, O(n) space
- Optimized: O(n log n) time, O(n) space

---

## 📚 Practice Problems (In Order)

### Start Here:
1. **[Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/)** - Classic problem
2. **[Maximum Length of Pair Chain](https://leetcode.com/problems/maximum-length-of-pair-chain/)** - Chain variant
3. **[Longest Increasing Path in Matrix](https://leetcode.com/problems/longest-increasing-path-in-a-matrix/)** - 2D variant

### Then Try:
4. **[Russian Doll Envelopes](https://leetcode.com/problems/russian-doll-envelopes/)** - 2D LIS
5. **[Largest Divisible Subset](https://leetcode.com/problems/largest-divisible-subset/)** - With path
6. **[Number of LIS](https://leetcode.com/problems/number-of-longest-increasing-subsequence/)** - Count variant

### Advanced:
7. **[Maximum Height by Stacking Cuboids](https://leetcode.com/problems/maximum-height-by-stacking-cuboids/)** - 3D variant
8. **[Tallest Billboard](https://leetcode.com/problems/tallest-billboard/)** - Complex state

---

## 🎯 Interview Communication Script

```
"I notice this is an LIS problem because:
1. We need to find the longest subsequence
2. Elements must follow a certain property [increasing/chain/divisible]
3. We can skip elements (not contiguous)

I'll use dynamic programming where dp[i] represents 
the longest valid subsequence ending at index i.

For each element, I'll check all previous elements that
can form a valid subsequence and take the maximum.

[If needed: I'll sort first because order doesn't matter for the final result]

Time complexity: O(n²), Space complexity: O(n)
[Or O(n log n) if using binary search optimization]"
```

**Master these templates and you'll handle 95% of LIS problems!**
