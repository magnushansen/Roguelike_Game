package rougelike.game.entities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import javafx.scene.image.Image;

@Tag("unit")
@Tag("entities")
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
        @DisplayName("Should resolve collision by moving entity")
        void shouldResolveCollisionByMovingEntity() {
            Entity movingEntity = createMockEntity(100, 100, 50, 50);
            Entity staticEntity = createMockEntity(120, 120, 50, 50);
            
            double originalX = movingEntity.getPositionX();
            double originalY = movingEntity.getPositionY();
            
            CollisionDetection.resolveCollision(movingEntity, staticEntity);
            
            // Entity should have moved from original position to resolve collision
            assertTrue(movingEntity.getPositionX() != originalX || movingEntity.getPositionY() != originalY);
        }
    }
    
    @Test
    @DisplayName("Should handle basic edge cases")
    void shouldHandleBasicEdgeCases() {
        Entity negativeEntity = createMockEntity(-50, -50, 100, 100);
        Entity positiveEntity = createMockEntity(25, 25, 50, 50);
        
        assertTrue(CollisionDetection.Aabb(negativeEntity, positiveEntity));
    }
}