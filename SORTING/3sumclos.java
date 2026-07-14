import java.util.Arrays;

class Solution {
    public int threeSumClosest(int[] nums, int target) {
        // 1. Sort the array to use the two-pointer technique
        Arrays.sort(nums);
        
        // Initialize with the sum of the first three elements
        int closestSum = nums[0] + nums[1] + nums[2];
        
        for (int i = 0; i < nums.length - 2; i++) {
            // Optional optimization: skip duplicate fixed elements
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            int left = i + 1;
            int right = nums.length - 1;
            
            // 2. Two-pointer sweep
            while (left < right) {
                int currentSum = nums[i] + nums[left] + nums[right];
                
                // If we find an exact match to target, return immediately
                if (currentSum == target) {
                    return currentSum;
                }
                
                // Update closestSum if the current triplet is closer to the target
                if (Math.abs(currentSum - target) < Math.abs(closestSum - target)) {
                    closestSum = currentSum;
                }
                
                // Move pointers based on how currentSum compares to target
                if (currentSum < target) {
                    left++;  // Need a larger sum
                } else {
                    right--; // Need a smaller sum
                }
            }
        }
        
        return closestSum;
    }
}
