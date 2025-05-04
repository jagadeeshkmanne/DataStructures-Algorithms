import java.util.ArrayList;
import java.util.Comparator;

/**
 * PriorityQueue implementation that can function as either MinHeap or MaxHeap
 * based on the provided comparator
 */
public class PriorityQueue<T extends Comparable<T>> {
    private ArrayList<T> heap;
    private Comparator<T> comparator;
    private boolean isMinHeap;
    
    /**
     * Default constructor - creates a min heap
     */
    public PriorityQueue() {
        this.heap = new ArrayList<>();
        this.isMinHeap = true;
        this.comparator = Comparator.naturalOrder(); // For min heap
    }
    
    /**
     * Constructor to specify heap type
     * @param isMinHeap true for min heap, false for max heap
     */
    public PriorityQueue(boolean isMinHeap) {
        this.heap = new ArrayList<>();
        this.isMinHeap = isMinHeap;
        this.comparator = isMinHeap ? 
                Comparator.naturalOrder() : // For min heap 
                Comparator.reverseOrder();  // For max heap
    }
    
    /**
     * Constructor with custom comparator
     * @param comparator the comparator to determine ordering
     */
    public PriorityQueue(Comparator<T> comparator) {
        this.heap = new ArrayList<>();
        this.comparator = comparator;
        // Based on the logic of the comparator, it could be either min or max heap
        this.isMinHeap = true; // This flag is less relevant when a custom comparator is used
    }
    
    /**
     * Get the current size of the heap
     * @return number of elements in the heap
     */
    public int size() {
        return heap.size();
    }
    
    /**
     * Check if the heap is empty
     * @return true if the heap is empty, false otherwise
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }
    
    /**
     * Get the index of the parent node
     */
    private int parent(int index) {
        return (index - 1) / 2;
    }
    
    /**
     * Get the index of the left child
     */
    private int leftChild(int index) {
        return 2 * index + 1;
    }
    
    /**
     * Get the index of the right child
     */
    private int rightChild(int index) {
        return 2 * index + 2;
    }
    
    /**
     * Swap two elements in the heap
     */
    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
    
    /**
     * Insert an element into the priority queue
     * @param element the element to insert
     */
    public void add(T element) {
        heap.add(element);
        int current = heap.size() - 1;
        
        // Heapify up
        while (current > 0) {
            int parentIdx = parent(current);
            // Compare based on the comparator
            if (comparator.compare(heap.get(current), heap.get(parentIdx)) >= 0) {
                break;
            }
            swap(current, parentIdx);
            current = parentIdx;
        }
    }
    
    /**
     * Add an element to the priority queue (alias for add)
     * @param element the element to insert
     */
    public void offer(T element) {
        add(element);
    }
    
    /**
     * Heapify down from the given index
     */
    private void heapifyDown(int index) {
        int smallest = index;
        int left = leftChild(index);
        int right = rightChild(index);
        int size = heap.size();
        
        // Check left child
        if (left < size && comparator.compare(heap.get(left), heap.get(smallest)) < 0) {
            smallest = left;
        }
        
        // Check right child
        if (right < size && comparator.compare(heap.get(right), heap.get(smallest)) < 0) {
            smallest = right;
        }
        
        // If a child is smaller/larger (depending on heap type)
        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }
    
    /**
     * Remove and return the root element (min for MinHeap, max for MaxHeap)
     * @return the root element
     * @throws RuntimeException if the heap is empty
     */
    public T poll() {
        if (isEmpty()) {
            throw new RuntimeException("Priority queue is empty");
        }
        
        T root = heap.get(0);
        
        // If only one element
        if (heap.size() == 1) {
            heap.remove(0);
            return root;
        }
        
        // Replace root with the last element and heapify down
        T lastElement = heap.remove(heap.size() - 1);
        heap.set(0, lastElement);
        heapifyDown(0);
        
        return root;
    }
    
    /**
     * Peek at the root element without removing it
     * @return the root element
     * @throws RuntimeException if the heap is empty
     */
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Priority queue is empty");
        }
        
        return heap.get(0);
    }
    
    /**
     * Remove a specific element from the heap
     * @param element the element to remove
     * @return true if the element was found and removed, false otherwise
     */
    public boolean remove(T element) {
        int index = -1;
        
        // Find the index of the element
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).equals(element)) {
                index = i;
                break;
            }
        }
        
        if (index == -1) {
            return false; // Element not found
        }
        
        // If it's the last element, simply remove it
        if (index == heap.size() - 1) {
            heap.remove(index);
            return true;
        }
        
        // Replace with the last element
        heap.set(index, heap.remove(heap.size() - 1));
        
        // If the new element at this position is "better" than its parent,
        // heapify up, otherwise heapify down
        int parentIdx = parent(index);
        if (index > 0 && comparator.compare(heap.get(index), heap.get(parentIdx)) < 0) {
            // Heapify up
            while (index > 0 && comparator.compare(heap.get(index), heap.get(parentIdx)) < 0) {
                swap(index, parentIdx);
                index = parentIdx;
                parentIdx = parent(index);
            }
        } else {
            // Heapify down
            heapifyDown(index);
        }
        
        return true;
    }
    
    /**
     * Clear all elements from the heap
     */
    public void clear() {
        heap.clear();
    }
    
    /**
     * Check if the priority queue contains the specified element
     * @param element the element to check for
     * @return true if the element is in the queue, false otherwise
     */
    public boolean contains(T element) {
        return heap.contains(element);
    }
    
    /**
     * Convert to array
     * @return array containing all elements in the priority queue
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        return (T[]) heap.toArray();
    }
    
    /**
     * Get a string representation of the heap
     */
    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < heap.size(); i++) {
            sb.append(heap.get(i));
            if (i < heap.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Example usage
     */
    public static void main(String[] args) {
        // Min heap example
        System.out.println("Min Heap Example:");
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(true);
        minHeap.add(5);
        minHeap.add(2);
        minHeap.add(8);
        minHeap.add(1);
        minHeap.add(10);
        
        System.out.println("Min Heap: " + minHeap);
        System.out.println("Peek: " + minHeap.peek());
        System.out.println("Poll: " + minHeap.poll());
        System.out.println("Min Heap after poll: " + minHeap);
        
        // Max heap example
        System.out.println("\nMax Heap Example:");
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(false);
        maxHeap.add(5);
        maxHeap.add(2);
        maxHeap.add(8);
        maxHeap.add(1);
        maxHeap.add(10);
        
        System.out.println("Max Heap: " + maxHeap);
        System.out.println("Peek: " + maxHeap.peek());
        System.out.println("Poll: " + maxHeap.poll());
        System.out.println("Max Heap after poll: " + maxHeap);
        
        // Custom comparator example
        System.out.println("\nCustom Comparator Example (by String length):");
        PriorityQueue<String> lengthPQ = new PriorityQueue<>(
                Comparator.comparingInt(String::length));
        lengthPQ.add("apple");
        lengthPQ.add("banana");
        lengthPQ.add("kiwi");
        lengthPQ.add("strawberry");
        
        System.out.println("Priority Queue by length: " + lengthPQ);
        System.out.println("Polling elements in order:");
        while (!lengthPQ.isEmpty()) {
            System.out.println("Polled: " + lengthPQ.poll());
        }
    }
}
