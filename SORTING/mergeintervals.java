import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }

        // 1. Sort intervals by their start times
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> merged = new ArrayList<>();
        
        // Initialize with the first interval
        int[] currentInterval = intervals[0];
        merged.add(currentInterval);

        // 2. Iterate and merge overlapping intervals
        for (int[] nextInterval : intervals) {
            int currentEnd = currentInterval[1];
            int nextStart = nextInterval[0];
            int nextEnd = nextInterval[1];

            if (nextStart <= currentEnd) {
                // Overlap detected: update the end time of the current interval
                currentInterval[1] = Math.max(currentEnd, nextEnd);
            } else {
                // No overlap: move to the next interval and add it to the list
                currentInterval = nextInterval;
                merged.add(currentInterval);
            }
        }

        // 3. Convert the list back to a 2D array
        return merged.toArray(new int[merged.size()][]);
    }
}
