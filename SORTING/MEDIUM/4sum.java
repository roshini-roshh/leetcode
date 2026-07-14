import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 1. Sort the array to use two-pointer strategy and skip duplicates
        Arrays.sort(nums);
        int n = nums.length;
        
        // First fixed element
        for (int i = 0; i < n - 3; i++) {
            // Skip duplicates for the first element
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            
            // Early pruning optimizations to speed up execution
            if ((long) nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target) break; // Smallest sum possible is too big
            if ((long) nums[i] + nums[n - 3] + nums[n - 2] + nums[n - 1] < target) continue; // Largest sum possible is too small
            
            // Second fixed element
            for (int j = i + 1; j < n - 2; j++) {
                // Skip duplicates for the second element
                if (j > i + 1 && nums[j] == nums[j - 1]) continue;
                
                // Further pruning inside the second loop
                if ((long) nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target) break;
                if ((long) nums[i] + nums[j] + nums[n - 2] + nums[n - 1] < target) continue;
                
                int left = j + 1;
                int right = n - 1;
                
                // 2. Two-pointer sweep for the last two elements
                while (left < right) {
                    long currentSum = (long) nums[i] + nums[j] + nums[left] + nums[right];
                    
                    if (currentSum == target) {
                        result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
                        
                        // Skip duplicates for the third element
                        while (left < right && nums[left] == nums[left + 1]) left++;
                        // Skip duplicates for the fourth element
                        while (left < right && nums[right] == nums[right - 1]) right--;
                        
                        left++;
                        right--;
                    } else if (currentSum < target) {
                        left++; // Increase the sum
                    } else {
                        right--; // Decrease the sum
                    }
                }
            }
        }
        
        return result;
    }
}
