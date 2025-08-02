package rougelike.game.entities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import javafx.scene.image.Image;

@DisplayName("Projectile Entity Tests")
class ProjectileTest {
    
    private Projectile projectile;
    private Image mockImage;
    
    private static final double INITIAL_X = 150.0;
    private static final double INITIAL_Y = 150.0;
    private static final double WIDTH = 16.0;
    private static final double HEIGHT = 16.0;
    private static final double SPEED = 2.0;
    private static final double DIRECTION_X = 1.0;
    private static final double DIRECTION_Y = 0.0;
    private static final int DAMAGE = 25;
    
    @BeforeEach
    void setUp() {
        mockImage = mock(Image.class);
        
        projectile = new Projectile(INITIAL_X, INITIAL_Y, WIDTH, HEIGHT, 
                                   mockImage, SPEED, DIRECTION_X, DIRECTION_Y, DAMAGE);
    }
    
    @Nested
    @DisplayName("Initialization Tests")
    class InitializationTests {
        
        @Test
        @DisplayName("Should initialize projectile with correct values")
        void shouldInitializeProjectileCorrectly() {
            assertEquals(INITIAL_X, projectile.getPositionX());
            assertEquals(INITIAL_Y, projectile.getPositionY());
            assertEquals(WIDTH, projectile.getWidth());
            assertEquals(HEIGHT, projectile.getHeight());
            assertEquals(mockImage, projectile.getImage());
        }
        
        @Test
        @DisplayName("Should not occupy space")
        void shouldNotOccupySpace() {
            assertFalse(projectile.isOccupying());
        }
    }
    
    @Nested
    @DisplayName("Movement Tests")
    class MovementTests {
        
        @Test
        @DisplayName("Should move in correct direction")
        void shouldMoveInCorrectDirection() {
            double initialX = projectile.getPositionX();
            
            projectile.updatePosition(1000);
            
            assertTrue(projectile.getPositionX() > initialX);
        }
        
        @Test
        @DisplayName("Should move correct distance based on time")
        void shouldMoveCorrectDistanceBasedOnTime() {
            double initialX = projectile.getPositionX();
            long timeElapsed = 1000;
            
            projectile.updatePosition(timeElapsed);
            
            double expectedNewX = initialX + (DIRECTION_X * SPEED * timeElapsed);
            assertEquals(expectedNewX, projectile.getPositionX(), 0.001);
        }
        
        @Test
        @DisplayName("Should handle zero time elapsed")
        void shouldHandleZeroTimeElapsed() {
            double initialX = projectile.getPositionX();
            double initialY = projectile.getPositionY();
            
            projectile.updatePosition(0);
            
            assertEquals(initialX, projectile.getPositionX());
            assertEquals(initialY, projectile.getPositionY());
        }
        
        @Test
        @DisplayName("Should handle diagonal movement")
        void shouldHandleDiagonalMovement() {
            Projectile diagonalProjectile = new Projectile(
                INITIAL_X, INITIAL_Y, WIDTH, HEIGHT, 
                mockImage, SPEED, 0.707, 0.707, DAMAGE
            );
            
            double initialX = diagonalProjectile.getPositionX();
            double initialY = diagonalProjectile.getPositionY();
            
            diagonalProjectile.updatePosition(1000);
            
            assertTrue(diagonalProjectile.getPositionX() > initialX);
            assertTrue(diagonalProjectile.getPositionY() > initialY);
        }
        
        @Test
        @DisplayName("Should handle negative direction")
        void shouldHandleNegativeDirection() {
            Projectile leftProjectile = new Projectile(
                INITIAL_X, INITIAL_Y, WIDTH, HEIGHT, 
                mockImage, SPEED, -1.0, 0.0, DAMAGE
            );
            
            double initialX = leftProjectile.getPositionX();
            
            leftProjectile.updatePosition(1000);
            
            assertTrue(leftProjectile.getPositionX() < initialX);
        }
    }
    
    @Nested
    @DisplayName("Interaction Tests")
    class InteractionTests {
        
        @Test
        @DisplayName("Should interact with enemy correctly")
        void shouldInteractWithEnemyCorrectly() {
            Enemy mockEnemy = mock(Enemy.class);
            
            InteractionResult result = projectile.interact(mockEnemy);
            
            assertNotNull(result);
            assertEquals(mockEnemy, result.getEntity());
        }
        
        @Test
        @DisplayName("Should return empty interaction for non-enemy entities")
        void shouldReturnEmptyInteractionForNonEnemyEntities() {
            Player mockPlayer = mock(Player.class);
            
            InteractionResult result = projectile.interact(mockPlayer);
            
            assertNotNull(result);
            assertEquals(mockPlayer, result.getEntity());
        }
        
        @Test
        @DisplayName("Should handle null entity interaction")
        void shouldHandleNullEntityInteraction() {
            assertDoesNotThrow(() -> {
                InteractionResult result = projectile.interact(null);
                assertNotNull(result);
            });
        }
    }
    
    @Nested
    @DisplayName("Physics Tests")
    class PhysicsTests {
        
        @Test
        @DisplayName("Should maintain constant velocity")
        void shouldMaintainConstantVelocity() {
            double firstX = projectile.getPositionX();
            projectile.updatePosition(1000);
            double secondX = projectile.getPositionX();
            
            projectile.updatePosition(1000);
            double thirdX = projectile.getPositionX();
            
            double firstDistance = secondX - firstX;
            double secondDistance = thirdX - secondX;
            
            assertEquals(firstDistance, secondDistance, 0.001);
        }
        
        @Test
        @DisplayName("Should handle very small time intervals")
        void shouldHandleVerySmallTimeIntervals() {
            double initialX = projectile.getPositionX();
            
            projectile.updatePosition(1);
            
            assertTrue(projectile.getPositionX() > initialX);
        }
        
        @Test
        @DisplayName("Should handle large time intervals")
        void shouldHandleLargeTimeIntervals() {
            double initialX = projectile.getPositionX();
            
            projectile.updatePosition(10000);
            
            assertTrue(projectile.getPositionX() > initialX);
        }
    }
    
    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Should handle zero speed")
        void shouldHandleZeroSpeed() {
            Projectile stationaryProjectile = new Projectile(
                INITIAL_X, INITIAL_Y, WIDTH, HEIGHT, 
                mockImage, 0.0, DIRECTION_X, DIRECTION_Y, DAMAGE
            );
            
            double initialX = stationaryProjectile.getPositionX();
            double initialY = stationaryProjectile.getPositionY();
            
            stationaryProjectile.updatePosition(1000);
            
            assertEquals(initialX, stationaryProjectile.getPositionX());
            assertEquals(initialY, stationaryProjectile.getPositionY());
        }
        
        @Test
        @DisplayName("Should handle zero direction")
        void shouldHandleZeroDirection() {
            Projectile zeroDirectionProjectile = new Projectile(
                INITIAL_X, INITIAL_Y, WIDTH, HEIGHT, 
                mockImage, SPEED, 0.0, 0.0, DAMAGE
            );
            
            double initialX = zeroDirectionProjectile.getPositionX();
            double initialY = zeroDirectionProjectile.getPositionY();
            
            zeroDirectionProjectile.updatePosition(1000);
            
            assertEquals(initialX, zeroDirectionProjectile.getPositionX());
            assertEquals(initialY, zeroDirectionProjectile.getPositionY());
        }
        
        @Test
        @DisplayName("Should handle negative time elapsed")
        void shouldHandleNegativeTimeElapsed() {
            double initialX = projectile.getPositionX();
            double initialY = projectile.getPositionY();
            
            projectile.updatePosition(-1000);
            
            assertTrue(projectile.getPositionX() < initialX);
        }
    }
    
    @Nested
    @DisplayName("Rendering Tests")
    class RenderingTests {
        
        @Test
        @DisplayName("Should not throw exception when rendering")
        void shouldNotThrowExceptionWhenRendering() {
            javafx.scene.canvas.GraphicsContext mockGC = mock(javafx.scene.canvas.GraphicsContext.class);
            
            assertDoesNotThrow(() -> {
                projectile.render(mockGC);
            });
        }
    }
}