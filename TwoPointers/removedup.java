class Solution {
    public int removeDuplicates(int[] nums) {
        // Edge case: if the array is empty, return 0
        if (nums.length == 0) {
            return 0;
        }
        
        // Slow pointer tracks the index of the last unique element found
        int index = 0;
        
        // Fast pointer 'i' scans the array starting from the second element
        for (int i = 1; i < nums.length; i++) {
            // If we find a value different from our last unique element
            if (nums[i] != nums[index]) {
                index++;               // Move slow pointer to the next empty slot
                nums[index] = nums[i]; // Copy the unique value forward
            }
        }
        
        // Length of unique elements is index + 1 (since index is 0-based)
        return index + 1;
    }
}
