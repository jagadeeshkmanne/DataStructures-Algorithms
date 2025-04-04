import java.util.HashMap;
import java.util.Map;

/**
 * Prototype Pattern Example: UI Theme Customization
 * Real-world use: Allow users to clone and modify predefined themes
 */
public class PrototypePatternExample {

    // 1. Prototype Interface
    interface UITheme extends Cloneable {
        UITheme clone();
        void apply();
        void customize(String primaryColor, String font);
    }

    // 2. Concrete Prototype
    static class DefaultTheme implements UITheme {
        private String name;
        private String primaryColor;
        private String backgroundColor;
        private String font;

        public DefaultTheme(String name, String primaryColor, String backgroundColor, String font) {
            this.name = name;
            this.primaryColor = primaryColor;
            this.backgroundColor = backgroundColor;
            this.font = font;
            System.out.println("Initializing theme: " + name); // Simulate expensive setup
        }

        @Override
        public UITheme clone() {
            try {
                return (DefaultTheme) super.clone(); // Shallow copy
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Cloning failed", e);
            }
        }

        @Override
        public void apply() {
            System.out.printf("Applied %s theme [Color: %s, BG: %s, Font: %s]%n",
                    name, primaryColor, backgroundColor, font);
        }

        @Override
        public void customize(String primaryColor, String font) {
            this.primaryColor = primaryColor;
            this.font = font;
            this.name = "Custom " + name;
        }
    }

    // 3. Prototype Registry
    static class ThemeRegistry {
        private static final Map<String, UITheme> themes = new HashMap<>();

        static {
            // Predefined themes
            themes.put("Light", new DefaultTheme("Light", "Blue", "White", "Arial"));
            themes.put("Dark", new DefaultTheme("Dark", "White", "#121212", "Roboto"));
            themes.put("High Contrast", new DefaultTheme("High Contrast", "Yellow", "Black", "Verdana"));
        }

        public static UITheme getTheme(String name) {
            UITheme theme = themes.get(name);
            if (theme == null) {
                throw new IllegalArgumentException("Unknown theme: " + name);
            }
            return theme.clone();
        }
    }

    // 4. Usage Example
    public static void main(String[] args) {
        System.out.println("=== Prototype Pattern Demo ===");
        
        // User 1: Gets Dark theme and customizes it
        UITheme user1Theme = ThemeRegistry.getTheme("Dark");
        user1Theme.customize("Purple", "Helvetica");
        user1Theme.apply();

        // User 2: Uses original Dark theme
        UITheme user2Theme = ThemeRegistry.getTheme("Dark");
        user2Theme.apply();

        // User 3: Creates custom High Contrast theme
        UITheme user3Theme = ThemeRegistry.getTheme("High Contrast");
        user3Theme.customize("Cyan", "Courier New");
        user3Theme.apply();
    }
}
