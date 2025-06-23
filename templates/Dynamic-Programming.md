# Dynamic Programming Templates 🚀

Complete DP pattern collection for mastering dynamic programming through pattern recognition.

---

## 📋 **All DP Patterns**

| Pattern | Identification Tips | Example Problems |
|---------|-------------------|------------------|
| **[Decision Making](./dp/decision-making.md)** | "Can't take adjacent/consecutive items" | House Robber, Climbing Stairs |
| **[Knapsack](./dp/knapsack.md)** | "Limited budget/capacity + items to choose" | Coin Change, Target Sum |
| **[Grid Path](./dp/grid-path.md)** | "Move in 2D grid from top-left to bottom-right" | Unique Paths, Min Path Sum |
| **[String DP](./dp/string-dp.md)** | "Two strings + words like 'common', 'edit', 'transform'" | Edit Distance, LCS |
| **[Tree DP](./dp/tree-dp.md)** | "Binary tree + 'maximum path sum' in problem title" | Binary Tree Max Path Sum |
| **[Graph DP](./dp/graph-dp.md)** | "Matrix + 'longest increasing path' or similar" | Longest Path in Matrix |
| **[Interval DP](./dp/interval-dp.md)** | "Array + 'burst', 'split optimally', 'chain multiplication'" | Burst Balloons, Matrix Chain |
| **[LIS Pattern](./dp/lis-pattern.md)** | "'Longest increasing subsequence' or 'arrange in order'" | LIS, Russian Doll Envelopes |

---

## 🔍 **Pattern Recognition Process**

### **Step 1: Is it DP? (10 seconds)**
```
✓ Optimization present? (maximize/minimize/count ways)
✓ Choices at each step?
✓ Overlapping subproblems?
→ If YES → It's DP!
```

### **Step 2: Count Variables (10 seconds)**
```
One variable changes (i, amount, index) → 1D DP
Two variables change (i,j) → 2D DP
```

### **Step 3: Identify Pattern (10 seconds)**
```
What am I deciding at each step?
- Include/exclude with constraint → Decision Making
- Items with capacity limit → Knapsack  
- Movement in grid → Grid Path
- Compare two sequences → String DP
- Tree structure choices → Tree DP
- Graph traversal optimization → Graph DP
- Split ranges optimally → Interval DP
- Maintain increasing order → LIS
```

---

## 🎯 **Quick Start**

1. **Read any DP problem**
2. **Apply 3-step recognition process** 
3. **Click on the matching pattern template**
4. **Follow the template approach**

Each template includes thinking process, recognition tips, LeetCode problems, code templates, and practice problems.

---

**Master DP through patterns! 🎯**
