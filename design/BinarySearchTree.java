/**
 * Basic Binary Search Tree implementation with fundamental operations
 */
public class BinarySearchTree<T extends Comparable<T>> {
    // Node class
    private class Node {
        T data;
        Node left;
        Node right;
        
        Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }
    
    private Node root;
    
    // Constructor
    public BinarySearchTree() {
        this.root = null;
    }
    
    // Check if empty
    public boolean isEmpty() {
        return root == null;
    }
    
    // Insert a value
    public void insert(T data) {
        root = insertRec(root, data);
    }
    
    private Node insertRec(Node root, T data) {
        if (root == null) {
            return new Node(data);
        }
        
        int compareResult = data.compareTo(root.data);
        
        if (compareResult < 0) {
            root.left = insertRec(root.left, data);
        } else if (compareResult > 0) {
            root.right = insertRec(root.right, data);
        }
        
        return root;
    }
    
    // Search for a value
    public boolean search(T data) {
        return searchRec(root, data);
    }
    
    private boolean searchRec(Node root, T data) {
        if (root == null) {
            return false;
        }
        
        int compareResult = data.compareTo(root.data);
        
        if (compareResult == 0) {
            return true;
        } else if (compareResult < 0) {
            return searchRec(root.left, data);
        } else {
            return searchRec(root.right, data);
        }
    }
    
    // Delete a value
    public void delete(T data) {
        root = deleteRec(root, data);
    }
    
    private Node deleteRec(Node root, T data) {
        if (root == null) {
            return null;
        }
        
        int compareResult = data.compareTo(root.data);
        
        if (compareResult < 0) {
            root.left = deleteRec(root.left, data);
        } else if (compareResult > 0) {
            root.right = deleteRec(root.right, data);
        } else {
            // Node with only one child or no child
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
            
            // Node with two children: Get the inorder successor
            root.data = findMin(root.right);
            
            // Delete the inorder successor
            root.right = deleteRec(root.right, root.data);
        }
        
        return root;
    }
    
    // Find the minimum value in a tree
    private T findMin(Node root) {
        T minValue = root.data;
        while (root.left != null) {
            minValue = root.left.data;
            root = root.left;
        }
        return minValue;
    }
    
    // In-order traversal
    public void inOrder() {
        System.out.print("In-order: ");
        inOrderRec(root);
        System.out.println();
    }
    
    private void inOrderRec(Node root) {
        if (root != null) {
            inOrderRec(root.left);
            System.out.print(root.data + " ");
            inOrderRec(root.right);
        }
    }
}
