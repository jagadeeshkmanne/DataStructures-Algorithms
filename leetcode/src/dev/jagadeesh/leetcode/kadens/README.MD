# Kadane's Algorithm - Essential Guide

## What is Kadane's Algorithm?
A dynamic programming algorithm used to find the maximum sum of a contiguous subarray within a one-dimensional array.  
Time Complexity: O(n)  
Space Complexity: O(1)

## When to Use?
1. Finding maximum sum subarray
2. Problem mentions "contiguous" or "consecutive" elements
3. Need optimal solution for subarray problems
4. Array has both positive and negative numbers

## Prerequisites
- Basic array manipulation
- Understanding of loops in Java
- Basic math operations
- Understanding of variables and updating them
# Key Points to Remember for Kadane's Algorithm

## Core Idea
At each position, decide whether to:
- **Start new subarray** (take current element)
- **Extend existing subarray** (add current element)

## Variables Used
- **maxEndingHere:** tracks current subarray sum
- **maxSoFar:** tracks overall maximum found

## Important Test Cases
- All negative numbers
- All positive numbers
- Mix of positive and negative
- Single element array
- Empty array

## Common Mistakes
- Forgetting to handle empty/null arrays
- Not initializing variables with the first element
- Incorrect comparison in max calculations
- Not updating maxSoFar in every iteration

## Practice Problems Progression
- **Basic:** Maximum Subarray Sum
- **Intermediate:** Maximum Product Subarray
- **Advanced:** Circular Array Maximum Sum

**Remember:** The key to mastering Kadane's Algorithm is understanding the concept of local vs global maximum and when to start a new subarray versus extending the existing one.

## Basic Implementation
```java
public class Kadane {
    public static int maxSubArray(int[] arr) {
        // Handle empty or null array
        if (arr == null || arr.length == 0) return 0;
        
        int maxEndingHere = arr[0];
        int maxSoFar = arr[0];
        
        for (int i = 1; i < arr.length; i++) {
            // Either extend previous subarray or start new one
            maxEndingHere = Math.max(arr[i], maxEndingHere + arr[i]);
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }
        
        return maxSoFar;
    }
}


