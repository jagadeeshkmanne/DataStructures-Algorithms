package dev.jagadeesh.leetcode;

public class LC363MaximumSumRectangleNoLargerThanK {
    public static void main(String[] args) {
        int[][] matrix = {
                {1,  0, 1},
                {0, -2, 3}
        };
        int k = 2;
        int result = new LC363Solution().maxSumSubmatrix(matrix, k);
        System.out.println("Maximum Sum of Rectangle No Larger Than K: " + result);
    }
}

class LC363Solution {
    public int maxSumSubmatrix(int[][] matrix, int k) {
        // TODO: implement solution for Maximum Sum of Rectangle No Larger Than K
        return 0;
    }
}
