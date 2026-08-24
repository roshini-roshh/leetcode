import java.util.*;

public class Solution {
    public int minReorder(int n, int[][] connections) {
        // Step 1: Create an adjacency list tracking neighbor node and edge directionality
        // int[] entry represents {neighbor_node, is_original_direction}
        // is_original_direction = 1 if the edge goes from u -> v, 0 if it is v -> u
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] conn : connections) {
            int u = conn[0];
            int v = conn[1];
            // Original directed edge u -> v (Needs to be flipped if we traverse u -> v)
            graph.get(u).add(new int[]{v, 1});
            // Reverse direction edge v -> u (Correct path heading towards 0)
            graph.get(v).add(new int[]{u, 0});
        }
        
        // Step 2: Initialize BFS queue and visited array starting from city 0
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[n];
        
        queue.offer(0);
        visited[0] = true;
        
        int changeCount = 0;
        
        // Step 3: Spread outward from city 0
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            for (int[] neighbor : graph.get(current)) {
                int nextNode = neighbor[0];
                int isOriginalDirection = neighbor[1];
                
                if (!visited[nextNode]) {
                    visited[nextNode] = true;
                    // If the edge points away from 0 (original direction), we must invert it
                    changeCount += isOriginalDirection;
                    queue.offer(nextNode);
                }
            }
        }
        
        return changeCount;
    }
}
