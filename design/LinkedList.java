/**
 * Basic LinkedList implementation with fundamental operations
 */
public class LinkedList<T> {
    // Node class
    private class Node {
        T data;
        Node next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node head;
    private Node tail;
    private int size;
    
    // Constructor
    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    
    // Get size
    public int size() {
        return size;
    }
    
    // Check if empty
    public boolean isEmpty() {
        return head == null;
    }
    
    // Add element at the beginning
    public void addFirst(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
    }
    
    // Add element at the end
    public void addLast(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }
    
    // Add element (default to end)
    public boolean add(T data) {
        addLast(data);
        return true;
    }
    
    // Get element at index
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        
        return current.data;
    }
    
    // Remove first element
    public T removeFirst() {
        if (isEmpty()) {
            throw new RuntimeException("List is empty");
        }
        
        T removedData = head.data;
        head = head.next;
        size--;
        
        if (head == null) {
            tail = null;
        }
        
        return removedData;
    }
    
    // Remove element at index
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        if (index == 0) {
            return removeFirst();
        }
        
        Node current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        
        T removedData = current.next.data;
        
        if (current.next == tail) {
            tail = current;
        }
        
        current.next = current.next.next;
        size--;
        
        return removedData;
    }
    
    // Clear all elements
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
    
    // Simple toString implementation
    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        
        StringBuilder sb = new StringBuilder("[");
        Node current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
