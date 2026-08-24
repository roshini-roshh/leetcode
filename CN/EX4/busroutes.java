import java.util.*;

public class Solution {
    public int numBusesToDestination(int[][] routes, int source, int target) {
        // Edge Case: If you are already at the destination, no buses are needed
        if (source == target) {
            return 0;
        }

        // Step 1: Build a map linking each stop to all bus routes that pass through it
        Map<Integer, List<Integer>> stopToBuses = new HashMap<>();
        for (int busId = 0; busId < routes.length; busId++) {
            for (int stop : routes[busId]) {
                stopToBuses.computeIfAbsent(stop, x -> new ArrayList<>()).add(busId);
            }
        }

        // If the source or target stop doesn't exist in any route, travel is impossible
        if (!stopToBuses.containsKey(source) || !stopToBuses.containsKey(target)) {
            return -1;
        }

        // Step 2: Initialize BFS structures
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visitedStops = new HashSet<>();
        boolean[] visitedBuses = new boolean[routes.length];

        // Start from the source stop
        queue.offer(source);
        visitedStops.add(source);
        int busCount = 0;

        // Step 3: Run the level-order BFS traversal
        while (!queue.isEmpty()) {
            int size = queue.size();
            busCount++; // Increment the bus boarding count for this entire depth layer

            for (int i = 0; i < size; i++) {
                int currentStop = queue.poll();

                // Get all buses that cross the current stop
                for (int busId : stopToBuses.get(currentStop)) {
                    if (visitedBuses[busId]) {
                        continue;
                    }
                    visitedBuses[busId] = true; // Mark the bus route as boarded

                    // Traverse all stops accessible along this specific bus loop
                    for (int nextStop : routes[busId]) {
                        if (visitedStops.contains(nextStop)) {
                            continue;
                        }

                        // Target reached successfully
                        if (nextStop == target) {
                            return busCount;
                        }

                        visitedStops.add(nextStop);
                        queue.offer(nextStop);
                    }
                }
            }
        }

        return -1;
    }
}
