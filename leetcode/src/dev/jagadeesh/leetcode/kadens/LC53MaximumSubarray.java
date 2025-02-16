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

        if (nums.length == 0) {
            return 0;
        }

        int maxCurrentSum = nums[0];
        int maxGlobalSum = nums[0];
        for (int i = 1; i < nums.length; i++) {
            maxCurrentSum  = Math.max(nums[i], nums[i] + maxCurrentSum);
            maxGlobalSum = Math.max(maxGlobalSum, maxCurrentSum);
        }
        return maxGlobalSum;
    }
}
