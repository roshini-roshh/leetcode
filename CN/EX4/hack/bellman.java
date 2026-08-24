import java.util.*;

public class BellmanFord {

    // Helper class to represent a directed edge in the graph
    static class Edge {
        int source;
        int destination;
        int weight;

        Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    /**
     * Executes the Bellman-Ford algorithm.
     * @param vertices Total number of vertices (0-indexed).
     * @param edges List of all directed edges in the graph.
     * @param source The starting node index.
     */
    public static void findShortestPaths(int vertices, List<Edge> edges, int source) {
        int[] distances = new int[vertices];
        
        // Step 1: Initialize distances from source to all other vertices as INFINITY
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;

        // Step 2: Relax all edges |V| - 1 times
        for (int i = 1; i < vertices; i++) {
            for (Edge edge : edges) {
                int u = edge.source;
                int v = edge.destination;
                int w = edge.weight;

                // Ensure the source node of the edge has been reached before relaxing
                if (distances[u] != Integer.MAX_VALUE && distances[u] + w < distances[v]) {
                    distances[v] = distances[u] + w;
                }
            }
        }

        // Step 3: Check for negative-weight cycles
        // If we can still relax an edge, then a negative cycle exists.
        boolean hasNegativeCycle = false;
        for (Edge edge : edges) {
            int u = edge.source;
            int v = edge.destination;
            int w = edge.weight;

            if (distances[u] != Integer.MAX_VALUE && distances[u] + w < distances[v]) {
                hasNegativeCycle = true;
                break;
            }
        }

        // Step 4: Display the calculated results
        if (hasNegativeCycle) {
            System.out.println("Graph contains a negative weight cycle! Shortest paths are undefined.");
        } else {
            System.out.println("Vertex \t Distance from Source " + source);
            for (int i = 0; i < vertices; i++) {
                System.out.println(i + " \t\t " + (distances[i] == Integer.MAX_VALUE ? "INF" : distances[i]));
            }
        }
    }

    public static void main(String[] args) {
        int vertices = 5;
        List<Edge> edges = new ArrayList<>();

        // Graph Example configuration layout
        edges.add(new Edge(0, 1, -1));
        edges.add(new Edge(0, 2, 4));
        edges.add(new Edge(1, 2, 3));
        edges.add(new Edge(1, 3, 2));
        edges.add(new Edge(1, 4, 2));
        edges.add(new Edge(3, 2, 5));
        edges.add(new Edge(3, 1, 1));
        edges.add(new Edge(4, 3, -3));

        int sourceNode = 0;
        findShortestPaths(vertices, edges, sourceNode);
    }
}
