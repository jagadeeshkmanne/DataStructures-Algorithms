import java.util.*;

/**
 * Basic Graph implementation with fundamental operations
 */
public class Graph<T> {
    private Map<T, List<T>> adjacencyList;
    private boolean isDirected;
    
    // Constructor
    public Graph(boolean isDirected) {
        this.adjacencyList = new HashMap<>();
        this.isDirected = isDirected;
    }
    
    // Add a vertex
    public void addVertex(T vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new ArrayList<>());
        }
    }
    
    // Add an edge
    public void addEdge(T source, T destination) {
        // Add vertices if they don't exist
        if (!adjacencyList.containsKey(source)) {
            addVertex(source);
        }
        
        if (!adjacencyList.containsKey(destination)) {
            addVertex(destination);
        }
        
        // Add edge
        adjacencyList.get(source).add(destination);
        
        // If undirected, add edge in opposite direction
        if (!isDirected) {
            adjacencyList.get(destination).add(source);
        }
    }
    
    // Remove an edge
    public void removeEdge(T source, T destination) {
        if (adjacencyList.containsKey(source)) {
            adjacencyList.get(source).remove(destination);
        }
        
        if (!isDirected && adjacencyList.containsKey(destination)) {
            adjacencyList.get(destination).remove(source);
        }
    }
    
    // Remove a vertex
    public void removeVertex(T vertex) {
        // Remove vertex as a destination from all other vertices
        for (T v : adjacencyList.keySet()) {
            adjacencyList.get(v).remove(vertex);
        }
        
        // Remove the vertex and its adjacency list
        adjacencyList.remove(vertex);
    }
    
    // Get adjacent vertices
    public List<T> getAdjacentVertices(T vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }
    
    // Check if edge exists
    public boolean hasEdge(T source, T destination) {
        if (!adjacencyList.containsKey(source)) {
            return false;
        }
        return adjacencyList.get(source).contains(destination);
    }
    
    // BFS traversal
    public void bfs(T startVertex) {
        if (!adjacencyList.containsKey(startVertex)) {
            return;
        }
        
        Set<T> visited = new HashSet<>();
        Queue<T> queue = new LinkedList<>();
        
        visited.add(startVertex);
        queue.add(startVertex);
        
        System.out.print("BFS: ");
        while (!queue.isEmpty()) {
            T vertex = queue.poll();
            System.out.print(vertex + " ");
            
            for (T neighbor : adjacencyList.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        System.out.println();
    }
    
    // DFS traversal
    public void dfs(T startVertex) {
        if (!adjacencyList.containsKey(startVertex)) {
            return;
        }
        
        Set<T> visited = new HashSet<>();
        
        System.out.print("DFS: ");
        dfsUtil(startVertex, visited);
        System.out.println();
    }
    
    private void dfsUtil(T vertex, Set<T> visited) {
        visited.add(vertex);
        System.out.print(vertex + " ");
        
        for (T neighbor : adjacencyList.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsUtil(neighbor, visited);
            }
        }
    }
    
    // Print the graph
    public void printGraph() {
        for (Map.Entry<T, List<T>> entry : adjacencyList.entrySet()) {
            System.out.print(entry.getKey() + " -> ");
            List<T> neighbors = entry.getValue();
            
            if (neighbors.isEmpty()) {
                System.out.println("No edges");
            } else {
                for (T neighbor : neighbors) {
                    System.out.print(neighbor + " ");
                }
                System.out.println();
            }
        }
    }
}
