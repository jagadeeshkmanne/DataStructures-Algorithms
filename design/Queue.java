/**
 * Basic Queue implementation with fundamental operations
 */
public class Queue<T> {
    // Node class
    private class Node {
        T data;
        Node next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node front;  // For removal
    private Node rear;   // For insertion
    private int size;
    
    // Constructor
    public Queue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }
    
    // Get size
    public int size() {
        return size;
    }
    
    // Check if empty
    public boolean isEmpty() {
        return front == null;
    }
    
    // Add element to the rear (enqueue)
    public void enqueue(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }
    
    // Remove element from the front (dequeue)
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        
        T removedData = front.data;
        front = front.next;
        size--;
        
        if (front == null) {
            rear = null;
        }
        
        return removedData;
    }
    
    // Peek at the front element without removing it
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        
        return front.data;
    }
    
    // Clear all elements
    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }
    
    // Simple toString implementation
    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        
        StringBuilder sb = new StringBuilder("Front -> [");
        Node current = front;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("] <- Rear");
        return sb.toString();
    }
}
