import java.util.Arrays;

public class FloydWarshall {
    // Using a large value that won't cause integer overflow during additions
    private static final int INF = 9999999; 

    /**
     * Executes the Floyd-Warshall all-pairs shortest path algorithm.
     * @param vertices Total number of vertices in the graph.
     * @param graph The initial adjacency matrix representing edge weights.
     */
    public static void computeAllPairsShortestPath(int vertices, int[][] graph) {
        // Step 1: Initialize the distance matrix with the input graph configuration
        int[][] dist = new int[vertices][vertices];
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                dist[i][j] = graph[i][j];
            }
        }

        // Step 2: Core Three-Loop Relaxation Process
        // k represents the intermediate stepping stone vertex
        for (int k = 0; k < vertices; k++) {
            // i represents the source vertex
            for (int i = 0; i < vertices; i++) {
                // j represents the destination vertex
                for (int j = 0; j < vertices; j++) {
                    // If vertex k is on the shortest path from i to j, then update dist[i][j]
                    if (dist[i][k] != INF && dist[k][j] != INF && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        // Step 3: Check for Negative Weight Cycles
        // If the distance from a node to itself becomes negative, a negative cycle exists.
        for (int i = 0; i < vertices; i++) {
            if (dist[i][i] < 0) {
                System.out.println("Error: Graph contains a negative weight cycle!");
                return;
            }
        }

        // Step 4: Display the calculated path grid output
        printMatrix(vertices, dist);
    }

    private static void printMatrix(int vertices, int[][] dist) {
        System.out.println("========================================= ");
        System.out.println("       ALL-PAIRS SHORTEST PATH MATRIX     ");
        System.out.println("========================================= ");
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (dist[i][j] == INF) {
                    System.out.print("INF\t");
                } else {
                    System.out.print(dist[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int vertices = 4;
        
        // Initial setup for the graph configuration matrix
        // Self-loops are 0, unlinked paths are labeled INF
        int[][] graph = {
            {0,   3,   INF, 5},
            {2,   0,   INF, INF},
            {INF, 1,   0,   2},
            {INF, INF, 2,   0}
        };

        computeAllPairsShortestPath(vertices, graph);
    }
}
