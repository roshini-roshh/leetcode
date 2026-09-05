import java.util.Deque;
import java.util.ArrayDeque;

class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n - k + 1];
        int ri = 0; // Result index pointer
        
        // Deque stores array indices, maintaining a decreasing value order
        Deque<Integer> deque = new ArrayDeque<>();
        
        for (int i = 0; i < n; i++) {
            // 1. Remove indices that are out of the current sliding window range
            if (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            
            // 2. Remove elements from the back that are smaller than the current element
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }
            
            // 3. Add the current element's index to the back
            deque.offerLast(i);
            
            // 4. Once the window reaches size k, append the maximum to the result array
            if (i >= k - 1) {
                result[ri++] = nums[deque.peekFirst()];
            }
        }
        
        return result;
    }
}
