class Solution {
    public int removeElement(int[] nums, int val) {
        // This pointer tracks the position for the next valid element
        int index = 0; 
        
        // Loop runs through the entire array (fast pointer)
        for (int i = 0; i < nums.length; i++) {
            // If the current element is NOT the value we want to remove
            if (nums[i] != val) {
                nums[index] = nums[i]; // Move valid element to the front
                index++;               // Move our slow pointer forward
            }
        }
        
        // index naturally represents the count of elements that are not equal to val
        return index; 
    }
}
