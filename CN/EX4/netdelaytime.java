import java.util.*;

public class Solution {
    public int networkDelayTime(int[][] times, int n, int k) {
        // Step 1: Build the adjacency list representation of the graph
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] time : times) {
            int source = time[0];
            int destination = time[1];
            int weight = time[2];
            graph.computeIfAbsent(source, x -> new ArrayList<>()).add(new int[]{destination, weight});
        }

        // Step 2: Initialize a min-heap tracking [accumulated_time, current_node]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        minHeap.offer(new int[]{0, k});

        // Step 3: Initialize tracking for the shortest time to visit each node
        Map<Integer, Integer> visitedTime = new HashMap<>();

        // Step 4: Execute Dijkstra's search loop
        while (!minHeap.isEmpty()) {
            int[] current = minHeap.poll();
            int currTime = current[0];
            int currNode = current[1];

            // If we have already found a shorter path to this node, skip it
            if (visitedTime.containsKey(currNode)) {
                continue;
            }
            visitedTime.put(currNode, currTime);

            // Explore all direct outward neighbors of the current node
            if (graph.containsKey(currNode)) {
                for (int[] neighbor : graph.get(currNode)) {
                    int nextNode = neighbor[0];
                    int edgeWeight = neighbor[1];
                    
                    if (!visitedTime.containsKey(nextNode)) {
                        minHeap.offer(new int[]{currTime + edgeWeight, nextNode});
                    }
                }
            }
        }

        // Step 5: Check if all 'n' nodes were successfully reached
        if (visitedTime.size() != n) {
            return -1;
        }

        // Step 6: Find the maximum time value in the map (time for last node to receive signal)
        int maxDelay = 0;
        for (int time : visitedTime.values()) {
            maxDelay = Math.max(maxDelay, time);
        }

        return maxDelay;
    }
}
