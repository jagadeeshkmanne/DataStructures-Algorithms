# Dynamic Programming Templates 🚀

Complete DP pattern collection for mastering dynamic programming through pattern recognition.

---

## 📋 **All DP Patterns**

### ✅ **TRUE TEMPLATES** (Copy-Paste Ready)
| Pattern | Identification Tips | Example Problems | Reusability |
|---------|-------------------|------------------|-------------|
| **[Decision Making](./dp/decision-making.md)** | "Can't take adjacent/consecutive items" | House Robber, Climbing Stairs | ⭐⭐⭐⭐⭐ |
| **[Grid Path](./dp/grid-path.md)** | "Move in 2D grid from top-left to bottom-right" | Unique Paths, Min Path Sum | ⭐⭐⭐⭐⭐ |
| **[LIS Pattern](./dp/lis-pattern.md)** | "'Longest increasing subsequence' or 'arrange in order'" | LIS, Russian Doll Envelopes | ⭐⭐⭐⭐ |
| **[String DP](./dp/string-dp.md)** | "Two strings + words like 'common', 'edit', 'transform'" | Edit Distance, LCS | ⭐⭐⭐⭐ |
| **[Knapsack](./dp/knapsack.md)** | "Limited budget/capacity + items to choose" | Coin Change, Target Sum | ⭐⭐⭐ |

### ⚠️ **APPROACHES** (Not True Templates)
| Pattern | Why It's an Approach | What to Learn |
|---------|---------------------|---------------|
| **[Tree DP](./dp/tree-dp.md)** | Each tree problem needs custom logic | Learn the post-order pattern + global variable technique |
| **[Graph DP](./dp/graph-dp.md)** | Graph structure varies too much | Learn DFS + memoization concept |
| **[Interval DP](./dp/interval-dp.md)** | Very problem-specific | Learn the "try all split points" idea |

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
- Include/exclude with constraint → Decision Making ✅ TEMPLATE
- Items with capacity limit → Knapsack ✅ TEMPLATE
- Movement in grid → Grid Path ✅ TEMPLATE
- Compare two sequences → String DP ✅ TEMPLATE
- Maintain increasing order → LIS ✅ TEMPLATE
- Tree structure choices → Tree DP ⚠️ APPROACH
- Graph traversal optimization → Graph DP ⚠️ APPROACH
- Split ranges optimally → Interval DP ⚠️ APPROACH
```

---

## 💡 **Why Some Patterns Can't Be Templates**

### **Tree/Graph/Interval DP Challenges:**

1. **Tree DP**: 
   - Trees have different structures (binary, n-ary, with values, etc.)
   - Each problem has unique parent-child relationships
   - **What works**: The concept of post-order + global variable

2. **Graph DP**:
   - Graphs vary wildly (DAG, cyclic, weighted, matrix)
   - Movement rules differ per problem
   - **What works**: DFS + memoization pattern

3. **Interval DP**:
   - Very problem-specific (burst balloons vs matrix chain)
   - Different state definitions
   - **What works**: Try all split points concept

---

## 🎯 **How to Use This Guide**

### **For TRUE TEMPLATES:**
```java
// 1. Recognize pattern
// 2. Copy template
// 3. Modify one method (canExtend, canMove, etc.)
// 4. Done!
```

### **For APPROACHES:**
```java
// 1. Recognize pattern
// 2. Apply the core concept:
//    - Tree DP: post-order + global
//    - Graph DP: DFS + memo
//    - Interval DP: try all splits
// 3. Write custom logic
```

---

## 📊 **Quick Decision Chart**

```
Is it in the TRUE TEMPLATES list?
├─ YES → Use template directly (90% work done)
└─ NO → Use approach concept (50% work done)
```

---

## 🚀 **Quick Start**

1. **Read any DP problem**
2. **Apply 3-step recognition process** 
3. **If TRUE TEMPLATE** → Copy template, modify one method
4. **If APPROACH** → Apply core concept, write custom logic

Each pattern includes thinking process, recognition tips, LeetCode problems, and practice problems.

---

**Remember**: Even "approaches" give you a huge head start. Knowing "this is Tree DP, I need post-order + global variable" is 50% of the solution!

**Master the templates, understand the approaches! 🎯**
