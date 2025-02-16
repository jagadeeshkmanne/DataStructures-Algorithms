package dev.jagadeesh.leetcode.kadens;

public class LC53MaximumSubarray {
    public static void main(String[] args) {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int result = new LC53Solution().maxSubArray(nums);
        System.out.println("Maximum Subarray Sum: " + result);
    }
}

class LC53Solution {
    public int maxSubArray(int[] nums) {
        // TODO: implement Kadane's algorithm for Maximum Subarray
        return 0;
    }
}
