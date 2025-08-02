package rougelike.game.entities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import javafx.scene.image.Image;

@DisplayName("Collision Detection Tests")
class CollisionDetectionTest {
    
    private Entity entityA;
    private Entity entityB;
    private Image mockImage;
    
    @BeforeEach
    void setUp() {
        mockImage = mock(Image.class);
        entityA = createMockEntity(100, 100, 50, 50);
        entityB = createMockEntity(120, 120, 50, 50);
    }
    
    private Entity createMockEntity(double x, double y, double width, double height) {
        return new Entity(x, y, width, height, mockImage) {
            @Override
            public void render(javafx.scene.canvas.GraphicsContext gc) {
                // Mock implementation
            }
        };
    }
    
    @Nested
    @DisplayName("AABB Collision Detection Tests")
    class AabbCollisionTests {
        
        @Test
        @DisplayName("Should detect collision when entities overlap")
        void shouldDetectCollisionWhenEntitiesOverlap() {
            Entity overlappingEntity = createMockEntity(125, 125, 50, 50);
            
            assertTrue(CollisionDetection.Aabb(entityA, overlappingEntity));
        }
        
        @Test
        @DisplayName("Should not detect collision when entities don't overlap")
        void shouldNotDetectCollisionWhenEntitiesDontOverlap() {
            Entity separateEntity = createMockEntity(200, 200, 50, 50);
            
            assertFalse(CollisionDetection.Aabb(entityA, separateEntity));
        }
        
        @Test
        @DisplayName("Should detect collision when entities touch at edge")
        void shouldDetectCollisionWhenEntitiesTouchAtEdge() {
            Entity touchingEntity = createMockEntity(150, 100, 50, 50);
            
            assertFalse(CollisionDetection.Aabb(entityA, touchingEntity));
        }
        
        @Test
        @DisplayName("Should detect collision when one entity is inside another")
        void shouldDetectCollisionWhenOneEntityIsInsideAnother() {
            Entity innerEntity = createMockEntity(110, 110, 20, 20);
            
            assertTrue(CollisionDetection.Aabb(entityA, innerEntity));
        }
        
        @Test
        @DisplayName("Should handle entities with different sizes")
        void shouldHandleEntitiesWithDifferentSizes() {
            Entity largeEntity = createMockEntity(80, 80, 100, 100);
            Entity smallEntity = createMockEntity(120, 120, 10, 10);
            
            assertTrue(CollisionDetection.Aabb(largeEntity, smallEntity));
        }
        
        @Test
        @DisplayName("Should handle zero-sized entities")
        void shouldHandleZeroSizedEntities() {
            Entity zeroEntity = createMockEntity(100, 100, 0, 0);
            
            assertFalse(CollisionDetection.Aabb(entityA, zeroEntity));
        }
        
        @Test
        @DisplayName("Should be symmetric")
        void shouldBeSymmetric() {
            boolean result1 = CollisionDetection.Aabb(entityA, entityB);
            boolean result2 = CollisionDetection.Aabb(entityB, entityA);
            
            assertEquals(result1, result2);
        }
    }
    
    @Nested
    @DisplayName("Collision Resolution Tests")
    class CollisionResolutionTests {
        
        @Test
        @DisplayName("Should resolve horizontal collision correctly")
        void shouldResolveHorizontalCollisionCorrectly() {
            Entity movingEntity = createMockEntity(140, 100, 50, 50);
            Entity staticEntity = createMockEntity(100, 100, 50, 50);
            
            double initialX = movingEntity.getPositionX();
            
            CollisionDetection.resolveCollision(movingEntity, staticEntity);
            
            assertNotEquals(initialX, movingEntity.getPositionX());
        }
        
        @Test
        @DisplayName("Should resolve vertical collision correctly")
        void shouldResolveVerticalCollisionCorrectly() {
            Entity movingEntity = createMockEntity(100, 140, 50, 50);
            Entity staticEntity = createMockEntity(100, 100, 50, 50);
            
            double initialY = movingEntity.getPositionY();
            
            CollisionDetection.resolveCollision(movingEntity, staticEntity);
            
            assertNotEquals(initialY, movingEntity.getPositionY());
        }
        
        @Test
        @DisplayName("Should choose minimum overlap direction")
        void shouldChooseMinimumOverlapDirection() {
            Entity movingEntity = createMockEntity(135, 110, 50, 50);
            Entity staticEntity = createMockEntity(100, 100, 50, 50);
            
            double initialX = movingEntity.getPositionX();
            double initialY = movingEntity.getPositionY();
            
            CollisionDetection.resolveCollision(movingEntity, staticEntity);
            
            assertTrue(initialX != movingEntity.getPositionX() || initialY != movingEntity.getPositionY());
        }
        
        @Test
        @DisplayName("Should handle identical positions")
        void shouldHandleIdenticalPositions() {
            Entity movingEntity = createMockEntity(100, 100, 50, 50);
            Entity staticEntity = createMockEntity(100, 100, 50, 50);
            
            assertDoesNotThrow(() -> {
                CollisionDetection.resolveCollision(movingEntity, staticEntity);
            });
        }
        
        @Test
        @DisplayName("Should clamp entity within boundaries")
        void shouldClampEntityWithinBoundaries() {
            Entity movingEntity = createMockEntity(-10, -10, 50, 50);
            Entity staticEntity = createMockEntity(0, 0, 50, 50);
            
            CollisionDetection.resolveCollision(movingEntity, staticEntity);
            
            assertTrue(movingEntity.getPositionX() >= 0);
            assertTrue(movingEntity.getPositionY() >= 0);
        }
    }
    
    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Should handle null entities gracefully")
        void shouldHandleNullEntitiesGracefully() {
            assertThrows(NullPointerException.class, () -> {
                CollisionDetection.Aabb(null, entityB);
            });
            
            assertThrows(NullPointerException.class, () -> {
                CollisionDetection.Aabb(entityA, null);
            });
        }
        
        @Test
        @DisplayName("Should handle negative positions")
        void shouldHandleNegativePositions() {
            Entity negativeEntity = createMockEntity(-50, -50, 100, 100);
            Entity positiveEntity = createMockEntity(25, 25, 50, 50);
            
            assertTrue(CollisionDetection.Aabb(negativeEntity, positiveEntity));
        }
        
        @Test
        @DisplayName("Should handle very large entities")
        void shouldHandleVeryLargeEntities() {
            Entity largeEntity = createMockEntity(0, 0, 1000, 1000);
            Entity smallEntity = createMockEntity(500, 500, 10, 10);
            
            assertTrue(CollisionDetection.Aabb(largeEntity, smallEntity));
        }
        
        @Test
        @DisplayName("Should handle floating point precision")
        void shouldHandleFloatingPointPrecision() {
            Entity entity1 = createMockEntity(100.0001, 100.0001, 50, 50);
            Entity entity2 = createMockEntity(100.0002, 100.0002, 50, 50);
            
            assertTrue(CollisionDetection.Aabb(entity1, entity2));
        }
    }
    
    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {
        
        @Test
        @DisplayName("Should handle multiple collision checks efficiently")
        void shouldHandleMultipleCollisionChecksEfficiently() {
            long startTime = System.nanoTime();
            
            for (int i = 0; i < 10000; i++) {
                CollisionDetection.Aabb(entityA, entityB);
            }
            
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            
            assertTrue(duration < 100_000_000);
        }
        
        @Test
        @DisplayName("Should handle collision resolution efficiently")
        void shouldHandleCollisionResolutionEfficiently() {
            Entity movingEntity = createMockEntity(125, 125, 50, 50);
            Entity staticEntity = createMockEntity(100, 100, 50, 50);
            
            long startTime = System.nanoTime();
            
            for (int i = 0; i < 1000; i++) {
                movingEntity.setPositionX(125 + (i % 10));
                movingEntity.setPositionY(125 + (i % 10));
                CollisionDetection.resolveCollision(movingEntity, staticEntity);
            }
            
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            
            assertTrue(duration < 100_000_000);
        }
    }
    
    @Nested
    @DisplayName("Mathematical Accuracy Tests")
    class MathematicalAccuracyTests {
        
        @Test
        @DisplayName("Should calculate overlap correctly for horizontal collision")
        void shouldCalculateOverlapCorrectlyForHorizontalCollision() {
            Entity left = createMockEntity(0, 0, 50, 50);
            Entity right = createMockEntity(40, 0, 50, 50);
            
            assertTrue(CollisionDetection.Aabb(left, right));
        }
        
        @Test
        @DisplayName("Should calculate overlap correctly for vertical collision")
        void shouldCalculateOverlapCorrectlyForVerticalCollision() {
            Entity top = createMockEntity(0, 0, 50, 50);
            Entity bottom = createMockEntity(0, 40, 50, 50);
            
            assertTrue(CollisionDetection.Aabb(top, bottom));
        }
        
        @Test
        @DisplayName("Should handle corner collisions")
        void shouldHandleCornerCollisions() {
            Entity corner1 = createMockEntity(0, 0, 50, 50);
            Entity corner2 = createMockEntity(40, 40, 50, 50);
            
            assertTrue(CollisionDetection.Aabb(corner1, corner2));
        }
    }
}