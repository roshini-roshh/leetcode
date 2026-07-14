import java.util.*;

class Solution {
    // Graph representation using a Map of PriorityQueues to enforce lexical ordering
    private Map<String, PriorityQueue<String>> graph = new HashMap<>();
    private List<String> itinerary = new LinkedList<>();

    public List<String> findItinerary(List<List<String>> tickets) {
        // 1. Build the graph adjacency lists
        for (List<String> ticket : tickets) {
            String origin = ticket.get(0);
            String destination = ticket.get(1);
            
            // PriorityQueue automatically keeps destinations sorted alphabetically
            graph.computeIfAbsent(origin, k -> new PriorityQueue<>()).add(destination);
        }

        // 2. Start Eulerian path traversal from "JFK"
        dfs("JFK");
        
        return itinerary;
    }

    private void dfs(String airport) {
        PriorityQueue<String> destinations = graph.get(airport);
        
        // 3. Greedily visit the lexicographically smallest neighbor first
        while (destinations != null && !destinations.isEmpty()) {
            // poll() deletes the edge so it cannot be used again
            String nextAirport = destinations.poll();
            dfs(nextAirport);
        }
        
        // 4. Post-order insertion: Add to the front of the list to reverse the sequence
        itinerary.add(0, airport);
    }
}
