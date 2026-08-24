import java.util.Arrays;

public class Solution {
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // Step 1: Initialize the tracking array for minimum cost to reach each node
        int[] prices = new int[n];
        Arrays.fill(prices, Integer.MAX_VALUE);
        prices[src] = 0;

        // Step 2: Relax edges up to K + 1 times
        for (int i = 0; i <= k; i++) {
            // Create a temporary array to store current iteration updates safely
            int[] tempPrices = Arrays.copyOf(prices, n);

            for (int[] flight : flights) {
                int u = flight[0];     // Source city
                int v = flight[1];     // Destination city
                int price = flight[2]; // Cost

                // Only relax if the source node has been reached in previous iterations
                if (prices[u] != Integer.MAX_VALUE) {
                    if (prices[u] + price < tempPrices[v]) {
                        tempPrices[v] = prices[u] + price;
                    }
                }
            }
            // Update the main tracking array for the next stop iteration loop
            prices = tempPrices;
        }

        // Return -1 if destination node was never reached under the limits
        return prices[dst] == Integer.MAX_VALUE ? -1 : prices[dst];
    }
}
