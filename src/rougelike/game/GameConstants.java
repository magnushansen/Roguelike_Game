package rougelike.game;

public final class GameConstants {
    
    // Combat Constants
    public static final int DEFAULT_DAMAGE = 10;
    public static final int DEFAULT_HEAL_AMOUNT = 10;
    
    // Threading Constants
    public static final int MAX_COLLISION_THREADS = 4;
    
    // Animation Constants
    public static final double EXPLOSION_DURATION = 1.0;
    public static final double EXPLOSION_FRAME_DURATION = 0.2;
    public static final int EXPLOSION_FRAME_COUNT = 4;
    
    // Time Conversion
    public static final long NANOS_TO_MILLIS = 1000000L;
    public static final double MILLIS_TO_SECONDS = 1000.0;
    
    // Asset Paths
    public static final String EXPLOSION_SPRITE_PATH = "file:assets/misc/explosion.png";
    
    // UI Constants
    public static final int STATUS_BAR_TEXT_Y_OFFSET = 25;
    public static final int HP_TEXT_X = 10;
    public static final int AP_TEXT_X = 120;
    public static final int INVENTORY_TEXT_X = 200;
    public static final int STATUS_BAR_FONT_SIZE = 14;
    
    // Level Constants
    public static final int STARTING_LEVEL = 0;
    public static final int LEVEL_INCREMENT = 1;
    
    // Entity Update Constants
    public static final double STATIC_SPRITE_DURATION = 1.0;
    
    // Error Messages
    public static final String MOVEMENT_UPDATE_INTERRUPTED = "Movement update was interrupted";
    public static final String MOVEMENT_UPDATE_FAILED = "Movement update failed";
    public static final String COLLISION_CHECK_INTERRUPTED = "Collision check was interrupted";
    public static final String COLLISION_CHECK_FAILED = "Collision check failed";
    
    // Prevent instantiation
    private GameConstants() {
        throw new AssertionError("GameConstants should not be instantiated");
    }
}