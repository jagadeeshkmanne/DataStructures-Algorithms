/**
 * Basic Deque (Double-ended Queue) implementation with fundamental operations
 */
public class Deque<T> {
    // Node class
    private class Node {
        T data;
        Node prev;
        Node next;
        
        Node(T data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }
    
    private Node front;  // For removal from front
    private Node rear;   // For insertion at rear
    private int size;
    
    // Constructor
    public Deque() {
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
        return size == 0;
    }
    
    // Add element to the front
    public void addFirst(T data) {
        Node newNode = new Node(data);
        
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            newNode.next = front;
            front.prev = newNode;
            front = newNode;
        }
        
        size++;
    }
    
    // Add element to the rear
    public void addLast(T data) {
        Node newNode = new Node(data);
        
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            newNode.prev = rear;
            rear = newNode;
        }
        
        size++;
    }
    
    // Remove element from the front
    public T removeFirst() {
        if (isEmpty()) {
            throw new RuntimeException("Deque is empty");
        }
        
        T removedData = front.data;
        
        if (front == rear) {
            // Only one element
            front = null;
            rear = null;
        } else {
            front = front.next;
            front.prev = null;
        }
        
        size--;
        return removedData;
    }
    
    // Remove element from the rear
    public T removeLast() {
        if (isEmpty()) {
            throw new RuntimeException("Deque is empty");
        }
        
        T removedData = rear.data;
        
        if (front == rear) {
            // Only one element
            front = null;
            rear = null;
        } else {
            rear = rear.prev;
            rear.next = null;
        }
        
        size--;
        return removedData;
    }
    
    // Get the front element without removing
    public T peekFirst() {
        if (isEmpty()) {
            throw new RuntimeException("Deque is empty");
        }
        
        return front.data;
    }
    
    // Get the rear element without removing
    public T peekLast() {
        if (isEmpty()) {
            throw new RuntimeException("Deque is empty");
        }
        
        return rear.data;
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
