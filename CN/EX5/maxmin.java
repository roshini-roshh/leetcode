import java.util.*;

public class Solution {

    public static int maxMin(int k, List<Integer> arr) {

        // Step 1: Sort the array
        Collections.sort(arr);

        int minUnfairness = Integer.MAX_VALUE;

        // Step 2: Check every window of size k
        for (int i = 0; i <= arr.size() - k; i++) {

            int unfairness = arr.get(i + k - 1) - arr.get(i);

            minUnfairness = Math.min(minUnfairness, unfairness);
        }

        return minUnfairness;
    }

    public static void main(String[] args) {

        List<Integer> arr = Arrays.asList(
            10, 100, 300, 200, 1000, 20, 30
        );

        int k = 3;

        System.out.println(maxMin(k, arr));
    }
}
