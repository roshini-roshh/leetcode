import java.util.*;

class Solution {

    private TreeMap<Integer, Integer> map = new TreeMap<>();
    private int size = 0;

    // Add number to the TreeMap
    private void add(int num) {
        map.put(num, map.getOrDefault(num, 0) + 1);
        size++;
    }

    // Remove number from the TreeMap
    private void remove(int num) {

        int count = map.get(num);

        if (count == 1) {
            map.remove(num);
        } else {
            map.put(num, count - 1);
        }

        size--;
    }

    // Find median
    private double getMedian() {

        int count = 0;

        if (size % 2 == 1) {

            int middle = size / 2;

            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {

                count += entry.getValue();

                if (count > middle) {
                    return entry.getKey();
                }
            }

        } else {

            int middle1 = size / 2 - 1;
            int middle2 = size / 2;

            int first = 0;
            int second = 0;

            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {

                count += entry.getValue();

                if (first == 0 && count > middle1) {
                    first = entry.getKey();
                }

                if (count > middle2) {
                    second = entry.getKey();
                    break;
                }
            }

            return ((double) first + second) / 2.0;
        }

        return 0;
    }

    public double[] medianSlidingWindow(int[] nums, int k) {

        int n = nums.length;

        double[] result = new double[n - k + 1];

        // First window
        for (int i = 0; i < k; i++) {
            add(nums[i]);
        }

        result[0] = getMedian();

        // Slide the window
        for (int i = k; i < n; i++) {

            // Remove outgoing element
            remove(nums[i - k]);

            // Add incoming element
            add(nums[i]);

            result[i - k + 1] = getMedian();
        }

        return result;
    }
}
