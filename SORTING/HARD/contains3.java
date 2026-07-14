import java.util.TreeSet;

class Solution {
    public boolean containsNearbyAlmostDuplicate(int[] nums, int indexDiff, int valueDiff) {
        // TreeSet maintains elements in sorted order for quick range lookup checks
        TreeSet<Long> set = new TreeSet<>();
        
        for (int i = 0; i < nums.length; i++) {
            long current = (long) nums[i];
            
            // Find the smallest element in the set that is >= current
            Long ceiling = set.ceiling(current);
            if (ceiling != null && ceiling - current <= valueDiff) {
                return true;
            }
            
            // Find the largest element in the set that is <= current
            Long floor = set.floor(current);
            if (floor != null && current - floor <= valueDiff) {
                return true;
            }
            
            // Add current element to our sliding window collection
            set.add(current);
            
            // Maintain sliding window size limits using indexDiff
            if (i >= indexDiff) {
                set.remove((long) nums[i - indexDiff]);
            }
        }
        
        return false;
    }
}
