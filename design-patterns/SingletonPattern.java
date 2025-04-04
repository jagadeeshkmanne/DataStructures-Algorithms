import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AppLogger {
    // 1. volatile ensures visibility across threads
    private static volatile AppLogger instance;
    private FileWriter logFile;
    
    // 2. Private constructor
    private AppLogger() {
        try {
            // Create logs directory if it doesn't exist
            new File("logs").mkdir();
            // Initialize log file (append mode)
            logFile = new FileWriter("logs/app.log", true);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }
    
    // 3. Thread-safe global access point with double-checked locking
    public static AppLogger getInstance() {
        if (instance == null) {                          // First check (no locking)
            synchronized (AppLogger.class) {             // Synchronize only when needed
                if (instance == null) {                  // Second check (locked)
                    instance = new AppLogger();
                }
            }
        }
        return instance;
    }
    
    // Synchronized business method for thread-safe logging
    public synchronized void log(String message) {
        try {
            String timestamp = LocalDateTime.now().toString();
            logFile.write(timestamp + " - " + message + "\n");
            logFile.flush();
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }
    
    // Synchronized cleanup method
    public synchronized void close() {
        try {
            if (logFile != null) {
                logFile.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to close logger: " + e.getMessage());
        }
    }
}

// Usage Example
public class Main {
    public static void main(String[] args) {
        // Get logger instance - now thread-safe
        AppLogger logger = AppLogger.getInstance();
        
        // Log some messages
        logger.log("Application started");
        logger.log("User logged in");
        logger.log("Processing data...");
        
        // All components use the same thread-safe logger
        DatabaseService dbService = new DatabaseService();
        dbService.connect();
        
        // Close when done
        logger.close();
    }
}

class DatabaseService {
    public void connect() {
        AppLogger.getInstance().log("Connecting to database...");
        // Database connection logic
    }
}
