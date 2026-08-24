import java.util.*;

public class Solution {
    public int minimumEffortPath(int[][] heights) {
        int left = 0;
        int right = 1000000; // Maximum possible height value given in problem constraints
        int ans = right;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (canReachDestination(heights, mid)) {
                ans = mid;
                right = mid - 1; // Try to find a smaller maximum effort
            } else {
                left = mid + 1;  // Increase the allowed effort threshold
            }
        }
        return ans;
    }

    private boolean canReachDestination(int[][] heights, int maxEffort) {
        int rows = heights.length;
        int cols = heights[0].length;
        
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];
        
        queue.offer(new int[]{0, 0});
        visited[0][0] = true;
        
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0];
            int c = curr[1];

            if (r == rows - 1 && c == cols - 1) {
                return true;
            }

            for (int[] dir : directions) {
                int nextR = r + dir[0];
                int nextC = c + dir[1];

                if (nextR >= 0 && nextR < rows && nextC >= 0 && nextC < cols && !visited[nextR][nextC]) {
                    // Check if the jump effort is within our current mid threshold limit
                    if (Math.abs(heights[r][c] - heights[nextR][nextC]) <= maxEffort) {
                        visited[nextR][nextC] = true;
                        queue.offer(new int[]{nextR, nextC});
                    }
                }
            }
        }
        return false;
    }
}
