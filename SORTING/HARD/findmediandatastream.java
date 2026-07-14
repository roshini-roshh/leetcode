import java.util.Collections;
import java.util.PriorityQueue;

class MedianFinder {
    private PriorityQueue<Integer> smallMaxHeap; // Stores the smaller half
    private PriorityQueue<Integer> largeMinHeap; // Stores the larger half

    public MedianFinder() {
        // Max-Heap needs a reverse order comparator
        this.smallMaxHeap = new PriorityQueue<>(Collections.reverseOrder());
        this.largeMinHeap = new PriorityQueue<>();
    }
    
    public void addNum(int num) {
        // 1. Add to max-heap first
        smallMaxHeap.add(num);
        
        // 2. Balancing step: Ensure every element in smallMaxHeap <= every element in largeMinHeap
        largeMinHeap.add(smallMaxHeap.poll());
        
        // 3. Size step: Maintain size property where smallMaxHeap can only have at most 1 more element than largeMinHeap
        if (smallMaxHeap.size() < largeMinHeap.size()) {
            smallMaxHeap.add(largeMinHeap.poll());
        }
    }
    
    public double findMedian() {
        // If odd number of total elements, max-heap holds the exact median
        if (smallMaxHeap.size() > largeMinHeap.size()) {
            return smallMaxHeap.peek();
        }
        // If even number of elements, average the roots of both heaps
        return (smallMaxHeap.peek() + largeMinHeap.peek()) / 2.0;
    }
}
