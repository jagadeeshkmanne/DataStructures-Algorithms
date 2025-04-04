import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AppLogger {
    // 1. Single static instance
    private static AppLogger instance;
    private FileWriter logFile;
    
    // 2. Private constructor
    private AppLogger() {
        try {
            // Create logs directory if it doesn't exist
            new File("logs").mkdir();
            // Initialize log file
            logFile = new FileWriter("logs/app.log", true);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }
    
    // 3. Global access point
    public static AppLogger getInstance() {
        if (instance == null) {
            instance = new AppLogger();
        }
        return instance;
    }
    
    // Business method
    public void log(String message) {
        try {
            String timestamp = LocalDateTime.now().toString();
            logFile.write(timestamp + " - " + message + "\n");
            logFile.flush();
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }
    
    // Cleanup method
    public void close() {
        try {
            logFile.close();
        } catch (IOException e) {
            System.err.println("Failed to close logger: " + e.getMessage());
        }
    }
}

// Usage Example
public class Main {
    public static void main(String[] args) {
        // Get logger instance
        AppLogger logger = AppLogger.getInstance();
        
        // Log some messages
        logger.log("Application started");
        logger.log("User logged in");
        logger.log("Processing data...");
        
        // All components use the same logger
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
