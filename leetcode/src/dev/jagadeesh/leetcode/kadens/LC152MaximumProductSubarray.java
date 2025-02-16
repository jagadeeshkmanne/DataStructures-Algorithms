package dev.jagadeesh.leetcode.kadens;

public class LC152MaximumProductSubarray {
    public static void main(String[] args) {
        int[] nums = {2, 3, -2, 4};
        int result = new LC152Solution().maxProduct(nums);
        System.out.println("Maximum Product Subarray: " + result);
    }
}

class LC152Solution {
    public int maxProduct(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int maxProd = nums[0];
        int minProd = nums[0];
        int result  = nums[0];

        for (int i = 1; i < nums.length; i++) {
            int current = nums[i];

            // Pre-compute the candidate products using old values of maxProd and minProd.
            int candidate1 = maxProd * current;
            int candidate2 = minProd * current;

            int maxAtCurrentElement = Math.max(candidate1, candidate2);
            int minAtCurrentElement = Math.min(candidate1, candidate2);

            // Directly update maxProd and minProd using the pre-computed candidates.
            maxProd = Math.max(current, maxAtCurrentElement);
            minProd = Math.min(current, minAtCurrentElement);

            result = Math.max(result, maxProd);
        }

        return result;
    }
}
