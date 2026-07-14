import java.util.*;

class Solution {
    public List<List<Integer>> getSkyline(int[][] buildings) {
        List<List<Integer>> result = new ArrayList<>();
        List<int[]> events = new ArrayList<>();
        
        // 1. Convert buildings into edge events
        for (int[] b : buildings) {
            // Left edge: mark height as negative to sort it before right edge if positions match
            events.add(new int[]{b[0], -b[2]});
            // Right edge: mark height as positive
            events.add(new int[]{b[1], b[2]});
        }
        
        // 2. Sort events primarily by x-coordinate. 
        // If x matches, the custom comparator resolves tie-breakers seamlessly.
        Collections.sort(events, (a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[1], b[1]); 
        });
        
        // 3. Max-Heap to track the currently visible building heights
        // Reverse order prioritizes the tallest building
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        maxHeap.add(0); // Ground level baseline
        
        int prevMaxHeight = 0;
        
        // 4. Sweep-line processing
        for (int[] event : events) {
            int x = event[0];
            int h = event[1];
            
            if (h < 0) {
                // Left edge: a new building starts, add its actual height to heap
                maxHeap.add(-h);
            } else {
                // Right edge: a building ends, remove its height from heap
                maxHeap.remove(h);
            }
            
            int currentMaxHeight = maxHeap.peek();
            
            // If the maximum height changes, we found a key skyline turning point
            if (currentMaxHeight != prevMaxHeight) {
                result.add(Arrays.asList(x, currentMaxHeight));
                prevMaxHeight = currentMaxHeight;
            }
        }
        
        return result;
    }
}
