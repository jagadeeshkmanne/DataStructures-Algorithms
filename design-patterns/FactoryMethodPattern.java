// Payment.java - Product Interface
public interface Payment {
    void processPayment(double amount);
}

// Concrete Payment Implementations
public class CreditCardPayment implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing credit card payment of $" + amount);
        // Actual credit card processing logic
    }
}

public class PayPalPayment implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing PayPal payment of $" + amount);
        // Actual PayPal processing logic
    }
}

public class CryptoPayment implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing cryptocurrency payment of $" + amount);
        // Actual crypto processing logic
    }
}

// PaymentProcessor.java - Abstract Creator
public abstract class PaymentProcessor {
    protected String paymentMethod;
    
    public PaymentProcessor(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    // Factory Method - to be implemented by subclasses
    protected abstract Payment createPayment();
    
    // Template method that uses the factory method
    public final void processOrder(double amount) {
        System.out.println("\n=== Processing " + paymentMethod + " order ===");
        
        // Create payment using factory method
        Payment payment = createPayment();
        
        // Validate payment
        if (!validatePayment()) {
            throw new RuntimeException("Payment validation failed");
        }
        
        // Process payment
        payment.processPayment(amount);
        
        // Finalize order
        System.out.println("Order completed successfully with " + paymentMethod);
    }
    
    // Hook method - can be overridden by subclasses
    protected boolean validatePayment() {
        // Default validation logic
        return true;
    }
    
    // Common implementation
    public void refundOrder(double amount) {
        System.out.println("Issuing refund of $" + amount + " for " + paymentMethod);
    }
}

// Concrete Creators
public class CreditCardProcessor extends PaymentProcessor {
    public CreditCardProcessor() {
        super("Credit Card");
    }
    
    @Override
    protected Payment createPayment() {
        // Can add credit-card specific initialization here
        return new CreditCardPayment();
    }
    
    @Override
    protected boolean validatePayment() {
        System.out.println("Validating credit card details...");
        // Additional credit card specific validation
        return true;
    }
}

public class PayPalProcessor extends PaymentProcessor {
    public PayPalProcessor() {
        super("PayPal");
    }
    
    @Override
    protected Payment createPayment() {
        // Can add PayPal-specific initialization here
        return new PayPalPayment();
    }
}

public class CryptoProcessor extends PaymentProcessor {
    public CryptoProcessor() {
        super("Cryptocurrency");
    }
    
    @Override
    protected Payment createPayment() {
        // Can add crypto-specific initialization here
        return new CryptoPayment();
    }
    
    @Override
    protected boolean validatePayment() {
        System.out.println("Verifying blockchain network...");
        // Additional crypto validation
        return true;
    }
}

// Demo.java - Client Code
public class Demo {
    public static void main(String[] args) {
        // Example usage
        PaymentProcessor creditCardProcessor = new CreditCardProcessor();
        creditCardProcessor.processOrder(100.00);
        
        PaymentProcessor payPalProcessor = new PayPalProcessor();
        payPalProcessor.processOrder(75.50);
        
        PaymentProcessor cryptoProcessor = new CryptoProcessor();
        cryptoProcessor.processOrder(250.00);
        
        // Example of handling error and refund
        try {
            PaymentProcessor invalidProcessor = new CreditCardProcessor();
            invalidProcessor.processOrder(-50.00); // Would fail validation
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            // Demonstrate refund
            new CreditCardProcessor().refundOrder(50.00);
        }
    }
}
