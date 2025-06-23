# Dynamic Programming - String Pattern

## 🎯 Pattern Overview

The **String Pattern** is for problems involving two strings where you need to find similarities, differences, or transformations between them. Each cell `dp[i][j]` represents a relationship between the first `i` characters of string1 and first `j` characters of string2.

### Core Concept:
```
Two strings: s1 and s2
Compare: Character by character
Goal: Find longest common part, minimum edits, or check matching
Key: Current position depends on previous comparisons
```

---

## 🔍 Pattern Recognition

### ✅ Quick Recognition Checklist
- [ ] Two strings to compare?
- [ ] Need relationship between strings?
- [ ] Building solution character by character?
- [ ] Current decision depends on previous characters?
→ If YES → String DP Pattern!

### Magic Keywords:
| Keyword | Example Problem |
|---------|----------------|
| "longest common" | Longest Common Subsequence |
| "edit", "convert", "transform" | Edit Distance |
| "minimum operations" | Edit Distance |
| "subsequence" of two strings | LCS |
| "matching" with wildcards | Wildcard Matching |

---

## 🚀 Copy-Paste Templates with Helper Methods

### Template 1: Longest Common Subsequence (LCS) Type

```java
class Solution {
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (charactersMatch(text1, text2, i, j)) {
                    dp[i][j] = includeBothCharacters(dp, i, j);
                } else {
                    dp[i][j] = skipOneCharacter(dp, i, j);
                }
            }
        }
        
        return dp[m][n];
    }
    
    private boolean charactersMatch(String s1, String s2, int i, int j) {
        return s1.charAt(i-1) == s2.charAt(j-1);
    }
    
    private int includeBothCharacters(int[][] dp, int i, int j) {
        // When chars match, include both and add 1
        return dp[i-1][j-1] + 1;
    }
    
    private int skipOneCharacter(int[][] dp, int i, int j) {
        // When no match, skip from either string and take max
        int skipFromFirst = dp[i-1][j];
        int skipFromSecond = dp[i][j-1];
        return Math.max(skipFromFirst, skipFromSecond);
    }
}
```

**Variations:**
- **Longest Palindromic Subsequence**: Use `longestCommonSubsequence(s, reverse(s))`
- **Longest Common Substring**: Change `skipOneCharacter` to return 0

---

### Template 2: Edit Distance Type

```java
class Solution {
    public int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        initializeBaseCases(dp, m, n);
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (charactersMatch(word1, word2, i, j)) {
                    dp[i][j] = noOperationNeeded(dp, i, j);
                } else {
                    dp[i][j] = chooseMinOperation(dp, i, j);
                }
            }
        }
        
        return dp[m][n];
    }
    
    private void initializeBaseCases(int[][] dp, int m, int n) {
        // Convert from empty string = insert all
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        // Convert to empty string = delete all
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
    }
    
    private boolean charactersMatch(String s1, String s2, int i, int j) {
        return s1.charAt(i-1) == s2.charAt(j-1);
    }
    
    private int noOperationNeeded(int[][] dp, int i, int j) {
        // Characters already match
        return dp[i-1][j-1];
    }
    
    private int chooseMinOperation(int[][] dp, int i, int j) {
        int insert = dp[i][j-1];      // Insert char to word1
        int delete = dp[i-1][j];      // Delete char from word1
        int replace = dp[i-1][j-1];   // Replace char in word1
        
        return 1 + Math.min(insert, Math.min(delete, replace));
    }
}
```

**Variations:**
- **Delete Only**: Remove `replace` from `chooseMinOperation`
- **Minimum ASCII Delete**: Track sum of ASCII values instead of count

---

### Template 3: Counting Subsequences Type

```java
class Solution {
    public int numDistinct(String s, String t) {
        int m = s.length();
        int n = t.length();
        int[][] dp = new int[m + 1][n + 1];
        
        initializeBaseCases(dp, m);
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (charactersMatch(s, t, i, j)) {
                    dp[i][j] = countBothOptions(dp, i, j);
                } else {
                    dp[i][j] = mustSkipCharacter(dp, i, j);
                }
            }
        }
        
        return dp[m][n];
    }
    
    private void initializeBaseCases(int[][] dp, int m) {
        // Empty target can be formed in 1 way (delete all source chars)
        for (int i = 0; i <= m; i++) {
            dp[i][0] = 1;
        }
    }
    
    private boolean charactersMatch(String s, String t, int i, int j) {
        return s.charAt(i-1) == t.charAt(j-1);
    }
    
    private int countBothOptions(int[][] dp, int i, int j) {
        // Can either use this matching char or skip it
        int useThisMatch = dp[i-1][j-1];
        int findAnotherMatch = dp[i-1][j];
        return useThisMatch + findAnotherMatch;
    }
    
    private int mustSkipCharacter(int[][] dp, int i, int j) {
        // No match, must skip this character in source
        return dp[i-1][j];
    }
}
```

---

### Template 4: Boolean Pattern Matching

```java
class Solution {
    public boolean isMatch(String s, String p) {
        int m = s.length();
        int n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        
        initializeBaseCases(dp, p, n);
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (isDirectMatch(s, p, i, j)) {
                    dp[i][j] = dp[i-1][j-1];
                } else if (isWildcard(p, j)) {
                    dp[i][j] = handleWildcard(dp, i, j);
                }
                // else remains false
            }
        }
        
        return dp[m][n];
    }
    
    private void initializeBaseCases(boolean[][] dp, String p, int n) {
        dp[0][0] = true;
        // Handle patterns like a* or a*b*c*
        for (int j = 1; j <= n; j++) {
            if (p.charAt(j-1) == '*') {
                dp[0][j] = dp[0][j-1];
            }
        }
    }
    
    private boolean isDirectMatch(String s, String p, int i, int j) {
        return p.charAt(j-1) == s.charAt(i-1) || p.charAt(j-1) == '?';
    }
    
    private boolean isWildcard(String p, int j) {
        return p.charAt(j-1) == '*';
    }
    
    private boolean handleWildcard(boolean[][] dp, int i, int j) {
        boolean matchEmpty = dp[i][j-1];    // * matches empty sequence
        boolean matchOne = dp[i-1][j];      // * matches 1+ characters
        return matchEmpty || matchOne;
    }
}
```

---

## 💡 Understanding Helper Methods

### Why Helper Methods?

1. **Self-documenting code** - Method names explain the logic
2. **Easy to modify** - Change one specific behavior
3. **Interview friendly** - Shows clear thinking
4. **Reusable logic** - Can extract common patterns

### Common Patterns:

```java
// Pattern 1: Character comparison
private boolean charactersMatch(String s1, String s2, int i, int j) {
    return s1.charAt(i-1) == s2.charAt(j-1);
}

// Pattern 2: Base case initialization
private void initializeBaseCases(int[][] dp, ...) {
    // Problem-specific initialization
}

// Pattern 3: Match handling
private int handleMatch(int[][] dp, int i, int j) {
    // What to do when characters match
}

// Pattern 4: No match handling
private int handleNoMatch(int[][] dp, int i, int j) {
    // What to do when characters don't match
}
```

---

## 📊 Quick Modification Guide

To adapt templates for different problems:

### 1. Change Return Type
```java
// From int to boolean
boolean[][] dp = new boolean[m + 1][n + 1];
```

### 2. Change Match Logic
```java
// For substring (not subsequence)
private int skipOneCharacter(int[][] dp, int i, int j) {
    return 0;  // Reset instead of taking max
}
```

### 3. Change Operations
```java
// For delete-only (no replace)
private int chooseMinOperation(int[][] dp, int i, int j) {
    int insert = dp[i][j-1];
    int delete = dp[i-1][j];
    return 1 + Math.min(insert, delete);
}
```

---

## 🎯 Interview Strategy

1. **Start with template structure**
2. **Write helper method signatures** - Shows organization
3. **Implement main loop** - Standard structure
4. **Fill in helper methods** - Clear, focused logic
5. **Test with example** - Walk through dp table

**Benefits:**
- Interviewer sees your thought process
- Easy to debug specific parts
- Can discuss optimizations for each helper
- Shows good coding practices

---

## 📚 Practice Problems

### Direct Template Use:
1. **[Longest Common Subsequence](https://leetcode.com/problems/longest-common-subsequence/)** - Template 1
2. **[Edit Distance](https://leetcode.com/problems/edit-distance/)** - Template 2
3. **[Distinct Subsequences](https://leetcode.com/problems/distinct-subsequences/)** - Template 3
4. **[Wildcard Matching](https://leetcode.com/problems/wildcard-matching/)** - Template 4

### With Modifications:
5. **[Delete Operation for Two Strings](https://leetcode.com/problems/delete-operation-for-two-strings/)** - Modify Template 2
6. **[Interleaving String](https://leetcode.com/problems/interleaving-string/)** - Boolean variant
7. **[Regular Expression Matching](https://leetcode.com/problems/regular-expression-matching/)** - Complex matching

**Copy a template, adjust helper methods, and solve!**
