import java.io.*;
import java.util.*;

public class Solution {

    // Helper class representing an edge connection target and its travel cost weight
    static class Edge {
        int node;
        int weight;

        Edge(int node, int weight) {
            this.node = node;
            this.weight = weight;
        }
    }

    public static List<Integer> shortestReach(int n, List<List<Integer>> edges, int s) {
        // Step 1: Build an adjacency list mapping nodes to their minimum unique edge weights
        Map<Integer, Map<Integer, Integer>> adjacencyMap = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            adjacencyMap.put(i, new HashMap<>());
        }

        // Deduplicate overlapping edges from input by retaining the lower weight
        for (List<Integer> edge : edges) {
            int u = edge.get(0);
            int v = edge.get(1);
            int w = edge.get(2);

            // Handle undirected graph mapping for edge endpoints
            if (!adjacencyMap.get(u).containsKey(v) || w < adjacencyMap.get(u).get(v)) {
                adjacencyMap.get(u).put(v, w);
                adjacencyMap.get(v).put(u, w);
            }
        }

        // Step 2: Initialize tracking arrays for definitive shortest distances
        int[] distances = new int[n + 1];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[s] = 0;

        // Min-Heap tracking elements arranged by total path weight: [node_id, accumulated_cost]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        minHeap.offer(new int[]{s, 0});

        // Step 3: Core Dijkstra execution loop
        while (!minHeap.isEmpty()) {
            int[] current = minHeap.poll();
            int currNode = current[0];
            int currDist = current[1];

            // If a tighter path has already been processed for this node, skip stale entry
            if (currDist > distances[currNode]) {
                continue;
            }

            // Iterate over all valid neighbors mapped to the current node
            for (Map.Entry<Integer, Integer> neighbor : adjacencyMap.get(currNode).entrySet()) {
                int nextNode = neighbor.getKey();
                int edgeWeight = neighbor.getValue();

                if (distances[currNode] + edgeWeight < distances[nextNode]) {
                    distances[nextNode] = distances[currNode] + edgeWeight;
                    minHeap.offer(new int[]{nextNode, distances[nextNode]});
                }
            }
        }

        // Step 4: Assemble results sequentially according to HackerRank specification instructions
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (i == s) {
                continue; // Do not include the distance from the source node to itself
            }
            if (distances[i] == Integer.MAX_VALUE) {
                result.add(-1); // Node is completely unreachable
            } else {
                result.add(distances[i]);
            }
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine().trim());

        while (t-- > 0) {
            String[] nm = br.readLine().trim().split("\\s+");
            int n = Integer.parseInt(nm[0]);
            int m = Integer.parseInt(nm[1]);

            List<List<Integer>> edges = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                String[] edgeInput = br.readLine().trim().split("\\s+");
                List<Integer> edge = new ArrayList<>();
                edge.add(Integer.parseInt(edgeInput[0]));
                edge.add(Integer.parseInt(edgeInput[1]));
                edge.add(Integer.parseInt(edgeInput[2]));
                edges.add(edge);
            }

            int s = Integer.parseInt(br.readLine().trim());
            List<Integer> result = shortestReach(n, edges, s);

            // Print output list separated by blank spaces
            for (int i = 0; i < result.size(); i++) {
                System.out.print(result.get(i) + (i == result.size() - 1 ? "" : " "));
            }
            System.out.println();
        }
    }
}
