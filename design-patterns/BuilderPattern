public class Email {
    // Required fields
    private final String to;
    private final String subject;
    
    // Optional fields
    private final String body;
    private final boolean isHtml;
    private final String cc;

    private Email(Builder builder) {
        this.to = builder.to;
        this.subject = builder.subject;
        this.body = builder.body;
        this.isHtml = builder.isHtml;
        this.cc = builder.cc;
    }

    public static class Builder {
        // Required parameters
        private final String to;
        private final String subject;
        
        // Optional parameters
        private String body = "";
        private boolean isHtml = false;
        private String cc = null;

        public Builder(String to, String subject) {
            if (to == null || subject == null) {
                throw new IllegalArgumentException("To and subject are required");
            }
            this.to = to;
            this.subject = subject;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder isHtml(boolean isHtml) {
            this.isHtml = isHtml;
            return this;
        }

        public Builder cc(String cc) {
            this.cc = cc;
            return this;
        }

        public Email build() {
            return new Email(this);
        }
    }

    // Getters
    public String getTo() { return to; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public boolean isHtml() { return isHtml; }
    public String getCc() { return cc; }
}

// Usage Example
public class Main {
    public static void main(String[] args) {
        // Simple email
        Email welcomeEmail = new Email.Builder("user@example.com", "Welcome!")
                .body("Thank you for joining us!")
                .build();

        // HTML email with CC
        Email promoEmail = new Email.Builder("client@example.com", "Special Offer!")
                .body("<h1>50% Discount!</h1><p>Limited time offer</p>")
                .isHtml(true)
                .cc("manager@example.com")
                .build();
    }
}
