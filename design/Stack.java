/**
 * Basic Stack implementation with fundamental operations
 */
public class Stack<T> {
    // Node class
    private class Node {
        T data;
        Node next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node top;
    private int size;
    
    // Constructor
    public Stack() {
        this.top = null;
        this.size = 0;
    }
    
    // Get size
    public int size() {
        return size;
    }
    
    // Check if empty
    public boolean isEmpty() {
        return top == null;
    }
    
    // Push element to the top
    public void push(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            top = newNode;
        } else {
            newNode.next = top;
            top = newNode;
        }
        size++;
    }
    
    // Pop element from the top
    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        
        T removedData = top.data;
        top = top.next;
        size--;
        
        return removedData;
    }
    
    // Peek at the top element without removing it
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        
        return top.data;
    }
    
    // Clear all elements
    public void clear() {
        top = null;
        size = 0;
    }
    
    // Simple toString implementation
    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        
        StringBuilder sb = new StringBuilder("[");
        Node current = top;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("] <- Top");
        return sb.toString();
    }
}
