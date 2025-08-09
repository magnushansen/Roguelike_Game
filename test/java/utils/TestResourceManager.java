package utils;

import javafx.scene.image.Image;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Manages test resources, particularly mock images, to avoid JavaFX initialization
 * issues during testing. Provides consistent mock resources across test runs.
 */
public class TestResourceManager {
    
    private static final Map<String, Image> mockImages = new ConcurrentHashMap<>();
    private static final Map<String, Image[]> mockAnimations = new ConcurrentHashMap<>();
    
    // Default dimensions for mock images
    private static final double DEFAULT_WIDTH = 32.0;
    private static final double DEFAULT_HEIGHT = 32.0;
    
    /**
     * Gets a mock image for testing. Creates one if it doesn't exist.
     * Uses the path as a key to ensure consistency across test runs.
     */
    public static Image getTestImage(String path) {
        return mockImages.computeIfAbsent(path, k -> MockFactory.createMockImage(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    }
    
    /**
     * Gets a mock image with specific dimensions.
     */
    public static Image getTestImage(String path, double width, double height) {
        String key = path + "_" + width + "_" + height;
        return mockImages.computeIfAbsent(key, k -> MockFactory.createMockImage(width, height));
    }
    
    /**
     * Gets a mock animation array for testing. Creates one if it doesn't exist.
     */
    public static Image[] getTestAnimation(String path, int frameCount) {
        return mockAnimations.computeIfAbsent(path, k -> MockFactory.createMockImageArray(frameCount));
    }
    
    /**
     * Gets a mock animation array with specific dimensions.
     */
    public static Image[] getTestAnimation(String path, int frameCount, double width, double height) {
        String key = path + "_" + frameCount + "_" + width + "_" + height;
        return mockAnimations.computeIfAbsent(key, k -> {
            Image[] frames = new Image[frameCount];
            for (int i = 0; i < frameCount; i++) {
                frames[i] = MockFactory.createMockImage(width, height);
            }
            return frames;
        });
    }
    
    /**
     * Creates mock images for all standard game entities.
     * Call this in test setup to pre-populate common resources.
     */
    public static void initializeStandardResources() {
        // Floor and wall images
        getTestImage("floor");
        getTestImage("wall");
        
        // Player animations
        getTestAnimation("player_idle", 4);
        getTestAnimation("player_moving", 4);
        
        // Enemy animations
        getTestAnimation("enemy_idle", 4);
        getTestAnimation("enemy_moving", 4);
        
        // Interactive elements
        getTestImage("exit");
        getTestImage("ladder");
        getTestImage("well");
        getTestImage("projectile");
        
        // Effects
        getTestImage("explosion");
    }
    
    /**
     * Creates a set of character-based mock images for the ImageDatabase.
     * Maps characters to consistent mock images.
     */
    public static Map<Character, Image> createCharacterImageMap() {
        Map<Character, Image> map = new ConcurrentHashMap<>();
        
        map.put(' ', getTestImage("floor_1"));
        map.put('W', getTestImage("wall_mid"));
        map.put('E', getTestImage("enemy_idle"));
        map.put('L', getTestImage("floor_ladder"));
        map.put('w', getTestImage("wall_fountain"));
        map.put('p', getTestImage("projectile"));
        map.put('e', getTestImage("crate"));
        
        return map;
    }
    
    /**
     * Creates a set of character-based mock animations for the ImageDatabase.
     */
    public static Map<Character, Image[]> createCharacterAnimationMap() {
        Map<Character, Image[]> map = new ConcurrentHashMap<>();
        
        map.put('E', getTestAnimation("enemy_idle", 4));
        map.put('P', getTestAnimation("player_idle", 4));
        map.put('w', getTestAnimation("wall_fountain", 3));
        map.put('R', getTestAnimation("player_run", 4));
        
        return map;
    }
    
    /**
     * Clears all cached test resources. Useful for cleanup between test classes.
     */
    public static void clearCache() {
        mockImages.clear();
        mockAnimations.clear();
    }
    
    /**
     * Gets the number of cached resources. Useful for testing memory usage.
     */
    public static int getCacheSize() {
        return mockImages.size() + mockAnimations.size();
    }
    
    /**
     * Creates a mock background image for testing.
     */
    public static Image createMockBackground(String name) {
        return getTestImage("background_" + name, 512, 512);
    }
    
    /**
     * Creates multiple mock background images for testing.
     */
    public static Image[] createMockBackgrounds(int count) {
        Image[] backgrounds = new Image[count];
        for (int i = 0; i < count; i++) {
            backgrounds[i] = createMockBackground("bg" + i);
        }
        return backgrounds;
    }
    
    private TestResourceManager() {
        // Utility class - prevent instantiation
    }
}