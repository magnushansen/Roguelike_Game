package utils;

import javafx.scene.image.Image;
import rougelike.game.entities.Entity;
import rougelike.game.entities.Player;
import rougelike.game.entities.Enemy;
import rougelike.game.entities.Projectile;

import static org.mockito.Mockito.*;

public class TestUtils {
    
    public static final double DEFAULT_POSITION_X = 100.0;
    public static final double DEFAULT_POSITION_Y = 100.0;
    public static final double DEFAULT_WIDTH = 32.0;
    public static final double DEFAULT_HEIGHT = 32.0;
    public static final int DEFAULT_HEALTH = 100;
    public static final int DEFAULT_DAMAGE = 25;
    public static final double DEFAULT_SPEED = 1.0;
    
    public static Image createMockImage() {
        return mock(Image.class);
    }
    
    public static Image[] createMockImageArray(int size) {
        Image[] images = new Image[size];
        for (int i = 0; i < size; i++) {
            images[i] = createMockImage();
        }
        return images;
    }
    
    public static Entity createMockEntity(double x, double y, double width, double height) {
        Image mockImage = createMockImage();
        return new Entity(x, y, width, height, mockImage) {
            @Override
            public void render(javafx.scene.canvas.GraphicsContext gc) {
                // Mock implementation
            }
        };
    }
    
    public static Entity createDefaultMockEntity() {
        return createMockEntity(DEFAULT_POSITION_X, DEFAULT_POSITION_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public static Player createMockPlayer() {
        return createMockPlayer(DEFAULT_POSITION_X, DEFAULT_POSITION_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT, 
                               DEFAULT_HEALTH, DEFAULT_DAMAGE);
    }
    
    public static Player createMockPlayer(double x, double y, double width, double height, 
                                        int maxHealth, int damage) {
        Image mockIdleImage = createMockImage();
        Image[] mockMovingFrames = createMockImageArray(4);
        Image[] mockIdleFrames = createMockImageArray(1);
        
        return new Player(x, y, width, height, mockIdleImage, mockMovingFrames, 
                         mockIdleFrames, maxHealth, damage);
    }
    
    public static Enemy createMockEnemy() {
        return createMockEnemy(DEFAULT_POSITION_X, DEFAULT_POSITION_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT,
                              DEFAULT_HEALTH, DEFAULT_DAMAGE, DEFAULT_SPEED, 100.0);
    }
    
    public static Enemy createMockEnemy(double x, double y, double width, double height,
                                      int health, int damage, double speed, double detectionRadius) {
        Image[] mockAnimationFrames = createMockImageArray(4);
        return new Enemy(x, y, width, height, mockAnimationFrames, health, damage, 
                        speed, detectionRadius, 0.5);
    }
    
    public static Projectile createMockProjectile() {
        return createMockProjectile(DEFAULT_POSITION_X, DEFAULT_POSITION_Y, 16.0, 16.0,
                                   DEFAULT_SPEED, 1.0, 0.0, DEFAULT_DAMAGE);
    }
    
    public static Projectile createMockProjectile(double x, double y, double width, double height,
                                                 double speed, double directionX, double directionY, 
                                                 int damage) {
        Image mockImage = createMockImage();
        return new Projectile(x, y, width, height, mockImage, speed, directionX, directionY, damage);
    }
    
    public static char[][][] createSimpleDungeonLayout() {
        return new char[][][] {
            {
                {'W', 'W', 'W', 'W', 'W'},
                {'W', ' ', ' ', ' ', 'W'},
                {'W', ' ', 'E', ' ', 'W'},
                {'W', ' ', ' ', ' ', 'W'},
                {'W', 'W', 'W', 'W', 'W'}
            }
        };
    }
    
    public static char[][][] createMultiLevelDungeonLayout() {
        return new char[][][] {
            {
                {'W', 'W', 'W'},
                {'W', ' ', 'W'},
                {'W', 'L', 'W'}
            },
            {
                {'W', 'W', 'W'},
                {'W', 'E', 'W'},
                {'W', ' ', 'W'}
            }
        };
    }
    
    public static char[][][] createEmptyDungeonLayout() {
        return new char[][][] {
            {
                {' ', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
            }
        };
    }
    
    public static void assertPositionEquals(double expectedX, double expectedY, Entity entity) {
        assertPositionEquals(expectedX, expectedY, entity, 0.001);
    }
    
    public static void assertPositionEquals(double expectedX, double expectedY, Entity entity, double delta) {
        if (Math.abs(entity.getPositionX() - expectedX) > delta) {
            throw new AssertionError(String.format("Expected X position: %f, but was: %f", 
                                                  expectedX, entity.getPositionX()));
        }
        if (Math.abs(entity.getPositionY() - expectedY) > delta) {
            throw new AssertionError(String.format("Expected Y position: %f, but was: %f", 
                                                  expectedY, entity.getPositionY()));
        }
    }
    
    public static void assertDimensionsEqual(double expectedWidth, double expectedHeight, Entity entity) {
        if (Math.abs(entity.getWidth() - expectedWidth) > 0.001) {
            throw new AssertionError(String.format("Expected width: %f, but was: %f", 
                                                  expectedWidth, entity.getWidth()));
        }
        if (Math.abs(entity.getHeight() - expectedHeight) > 0.001) {
            throw new AssertionError(String.format("Expected height: %f, but was: %f", 
                                                  expectedHeight, entity.getHeight()));
        }
    }
    
    public static boolean isWithinBounds(Entity entity, double minX, double minY, double maxX, double maxY) {
        return entity.getPositionX() >= minX && entity.getPositionY() >= minY &&
               entity.getPositionX() + entity.getWidth() <= maxX && 
               entity.getPositionY() + entity.getHeight() <= maxY;
    }
    
    public static double calculateDistance(Entity entity1, Entity entity2) {
        double dx = entity1.getPositionX() - entity2.getPositionX();
        double dy = entity1.getPositionY() - entity2.getPositionY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public static double calculateCenterDistance(Entity entity1, Entity entity2) {
        double center1X = entity1.getPositionX() + entity1.getWidth() / 2;
        double center1Y = entity1.getPositionY() + entity1.getHeight() / 2;
        double center2X = entity2.getPositionX() + entity2.getWidth() / 2;
        double center2Y = entity2.getPositionY() + entity2.getHeight() / 2;
        
        double dx = center1X - center2X;
        double dy = center1Y - center2Y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public static void waitForMilliseconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static void assertThrowsWithMessage(Class<? extends Throwable> expectedType, 
                                             String expectedMessage, 
                                             Runnable executable) {
        try {
            executable.run();
            throw new AssertionError("Expected " + expectedType.getSimpleName() + " to be thrown");
        } catch (Throwable actualException) {
            if (!expectedType.isInstance(actualException)) {
                throw new AssertionError("Expected " + expectedType.getSimpleName() + 
                                       " but got " + actualException.getClass().getSimpleName());
            }
            if (expectedMessage != null && !actualException.getMessage().contains(expectedMessage)) {
                throw new AssertionError("Expected message to contain '" + expectedMessage + 
                                       "' but was '" + actualException.getMessage() + "'");
            }
        }
    }
    
    public static <T> void assertEventuallyTrue(java.util.function.Supplier<T> condition, 
                                              java.util.function.Predicate<T> predicate, 
                                              long timeoutMs) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            T value = condition.get();
            if (predicate.test(value)) {
                return;
            }
            waitForMilliseconds(10);
        }
        throw new AssertionError("Condition was not met within " + timeoutMs + "ms");
    }
    
    public static void assertEventuallyTrue(java.util.function.BooleanSupplier condition, long timeoutMs) {
        assertEventuallyTrue(() -> condition.getAsBoolean(), Boolean::booleanValue, timeoutMs);
    }
    
    public static java.util.List<Entity> createEntityList(Entity... entities) {
        java.util.List<Entity> list = new java.util.ArrayList<>();
        java.util.Collections.addAll(list, entities);
        return list;
    }
    
    public static void performanceTest(Runnable task, long maxTimeMs, String testName) {
        long startTime = System.nanoTime();
        task.run();
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        
        if (durationMs > maxTimeMs) {
            throw new AssertionError(String.format("%s took %dms, expected less than %dms", 
                                                  testName, durationMs, maxTimeMs));
        }
    }
    
    public static void memoryTest(Runnable task, String testName) {
        System.gc();
        long beforeMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        task.run();
        
        System.gc();
        long afterMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        long memoryUsed = afterMemory - beforeMemory;
        System.out.println(String.format("%s used %d bytes of memory", testName, memoryUsed));
    }
    
    private TestUtils() {
        // Utility class - prevent instantiation
    }
}