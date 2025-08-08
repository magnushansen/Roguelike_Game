package utils;

import java.util.HashMap;
import javafx.scene.image.Image;

/**
 * Test-friendly version of ImageDatabase that doesn't initialize JavaFX Images
 */
public class TestImageDatabase {
    private static HashMap<Character, Image> images = new HashMap<>();
    private static HashMap<Character, Image[]> animations = new HashMap<>();
    
    // Static initialization is done lazily to avoid JavaFX issues
    private static boolean initialized = false;
    
    public static void initializeWithMocks() {
        if (initialized) return;
        
        // Use MockFactory to create mock images instead of real JavaFX Images
        images.put(' ', MockFactory.createMockImage());
        images.put('W', MockFactory.createMockImage());
        images.put('E', MockFactory.createMockImage());
        images.put('L', MockFactory.createMockImage());
        images.put('w', MockFactory.createMockImage());
        images.put('p', MockFactory.createMockImage());
        images.put('e', MockFactory.createMockImage());
        
        animations.put('E', MockFactory.createMockImageArray(4));
        animations.put('P', MockFactory.createMockImageArray(4));
        animations.put('w', MockFactory.createMockImageArray(3));
        animations.put('R', MockFactory.createMockImageArray(4));
        
        initialized = true;
    }
    
    public static Image[] getAnimationFrames(char key) {
        initializeWithMocks();
        return animations.get(key);
    }
    
    public static Image getImage(char key) {
        initializeWithMocks();
        return images.get(key);
    }
    
    public static void reset() {
        initialized = false;
        images.clear();
        animations.clear();
    }
}