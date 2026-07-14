import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        // 1. Sort to bring duplicates next to each other
        Arrays.sort(nums);
        
        // Track whether an index has already been chosen in the current path
        boolean[] used = new boolean[nums.length];
        
        backtrack(result, new ArrayList<>(), nums, used);
        return result;
    }

    private void backtrack(List<List<Integer>> result, List<Integer> current, int[] nums, boolean[] used) {
        // Base Case: If the path length matches the input length, we found a full permutation
        if (current.size() == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            // Skip if this index is already used in the current path
            if (used[i]) {
                continue;
            }

            // CRITICAL DUPLICATE PRUNING CONDITION:
            // Skip if the current number is the same as the previous number
            // AND the previous number was NOT used in this recursive step.
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                continue;
            }

            // Choose
            used[i] = true;
            current.add(nums[i]);

            // Explore
            backtrack(result, current, nums, used);

            // Backtrack (Undo choice)
            used[i] = false;
            current.remove(current.size() - 1);
        }
    }
}
