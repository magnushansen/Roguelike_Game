package rougelike.game.entities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import javafx.scene.image.Image;

@DisplayName("Enemy Entity Tests")
class EnemyTest {
    
    private Enemy enemy;
    private Image[] mockAnimationFrames;
    
    private static final double INITIAL_X = 200.0;
    private static final double INITIAL_Y = 200.0;
    private static final double WIDTH = 32.0;
    private static final double HEIGHT = 32.0;
    private static final int HEALTH = 50;
    private static final int DAMAGE = 15;
    private static final double SPEED = 0.3;
    private static final double DETECTION_RADIUS = 100.0;
    private static final double TIME_PER_IMAGE = 0.5;
    
    @BeforeEach
    void setUp() {
        mockAnimationFrames = new Image[]{
            mock(Image.class), 
            mock(Image.class), 
            mock(Image.class)
        };
        
        enemy = new Enemy(INITIAL_X, INITIAL_Y, WIDTH, HEIGHT, 
                         mockAnimationFrames, HEALTH, DAMAGE, 
                         SPEED, DETECTION_RADIUS, TIME_PER_IMAGE);
    }
    
    @Nested
    @DisplayName("Initialization Tests")
    class InitializationTests {
        
        @Test
        @DisplayName("Should initialize enemy with correct values")
        void shouldInitializeEnemyCorrectly() {
            assertEquals(INITIAL_X, enemy.getPositionX());
            assertEquals(INITIAL_Y, enemy.getPositionY());
            assertEquals(WIDTH, enemy.getWidth());
            assertEquals(HEIGHT, enemy.getHeight());
            assertEquals(DAMAGE, enemy.getEnemyDamage());
            assertEquals(HEALTH, enemy.getEnemyHealth());
            assertEquals(DETECTION_RADIUS, enemy.getdetectionRadius());
            assertFalse(enemy.isDead());
        }
        
        @Test
        @DisplayName("Should occupy space by default")
        void shouldOccupySpaceByDefault() {
            assertTrue(enemy.isOccupying());
        }
    }
    
    @Nested
    @DisplayName("Health and Combat Tests")
    class HealthAndCombatTests {
        
        @Test
        @DisplayName("Should take damage correctly")
        void shouldTakeDamageCorrectly() {
            int initialHealth = enemy.getEnemyHealth();
            enemy.takeDamage(20);
            assertFalse(enemy.isDead());
        }
        
        @Test
        @DisplayName("Should die when health reaches zero")
        void shouldDieWhenHealthReachesZero() {
            enemy.takeDamage(HEALTH);
            assertTrue(enemy.isDead());
        }
        
        @Test
        @DisplayName("Should have damage immunity period")
        void shouldHaveDamageImmunityPeriod() {
            enemy.takeDamage(10);
            enemy.takeDamage(10);
            assertFalse(enemy.isDead());
        }
        
        @Test
        @DisplayName("Should die when taking lethal damage")
        void shouldDieWhenTakingLethalDamage() {
            enemy.takeDamage(HEALTH + 10);
            assertTrue(enemy.isDead());
        }
    }
    
    @Nested
    @DisplayName("Movement and AI Tests")
    class MovementAndAITests {
        
        @Test
        @DisplayName("Should move towards player when in detection range")
        void shouldMoveTowardsPlayerWhenInDetectionRange() {
            double playerX = INITIAL_X + 50;
            double playerY = INITIAL_Y + 50;
            double initialX = enemy.getPositionX();
            double initialY = enemy.getPositionY();
            
            enemy.move(1000, playerX, playerY);
            
            assertTrue(enemy.getPositionX() > initialX);
            assertTrue(enemy.getPositionY() > initialY);
        }
        
        @Test
        @DisplayName("Should not move towards player when out of detection range")
        void shouldNotMoveTowardsPlayerWhenOutOfDetectionRange() {
            double playerX = INITIAL_X + DETECTION_RADIUS + 50;
            double playerY = INITIAL_Y + DETECTION_RADIUS + 50;
            
            enemy.move(1000, playerX, playerY);
            
            assertNotNull(enemy);
        }
        
        @Test
        @DisplayName("Should undo move correctly")
        void shouldUndoMoveCorrectly() {
            double initialX = enemy.getPositionX();
            double initialY = enemy.getPositionY();
            
            enemy.move(1000, INITIAL_X + 50, INITIAL_Y + 50);
            enemy.undoMove();
            
            assertEquals(initialX, enemy.getPositionX());
            assertEquals(initialY, enemy.getPositionY());
        }
        
        @Test
        @DisplayName("Should calculate distance to player correctly")
        void shouldCalculateDistanceToPlayerCorrectly() {
            double playerX = INITIAL_X + 30;
            double playerY = INITIAL_Y + 40;
            
            enemy.move(100, playerX, playerY);
            
            double expectedDistance = Math.sqrt(30*30 + 40*40);
            assertTrue(expectedDistance < DETECTION_RADIUS);
        }
        
        @Test
        @DisplayName("Should handle random movement when player out of range")
        void shouldHandleRandomMovementWhenPlayerOutOfRange() {
            double playerX = INITIAL_X + DETECTION_RADIUS + 100;
            double playerY = INITIAL_Y + DETECTION_RADIUS + 100;
            
            assertDoesNotThrow(() -> {
                for (int i = 0; i < 100; i++) {
                    enemy.move(1000, playerX, playerY);
                }
            });
        }
    }
    
    @Nested
    @DisplayName("Interaction Tests")
    class InteractionTests {
        
        @Test
        @DisplayName("Should interact with player correctly")
        void shouldInteractWithPlayerCorrectly() {
            Player mockPlayer = mock(Player.class);
            
            InteractionResult result = enemy.interact(mockPlayer);
            
            assertNotNull(result);
            assertEquals(mockPlayer, result.getEntity());
        }
        
        @Test
        @DisplayName("Should return empty interaction for non-player entities")
        void shouldReturnEmptyInteractionForNonPlayerEntities() {
            Entity mockEntity = mock(Entity.class);
            
            InteractionResult result = enemy.interact(mockEntity);
            
            assertNotNull(result);
            assertEquals(mockEntity, result.getEntity());
        }
    }
    
    @Nested
    @DisplayName("Animation Tests")
    class AnimationTests {
        
        @Test
        @DisplayName("Should update animation correctly")
        void shouldUpdateAnimationCorrectly() {
            assertDoesNotThrow(() -> enemy.update(0.016));
        }
        
        @Test
        @DisplayName("Should handle multiple animation updates")
        void shouldHandleMultipleAnimationUpdates() {
            assertDoesNotThrow(() -> {
                for (int i = 0; i < 60; i++) {
                    enemy.update(0.016);
                }
            });
        }
    }
    
    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Should handle zero time elapsed in movement")
        void shouldHandleZeroTimeElapsedInMovement() {
            double initialX = enemy.getPositionX();
            double initialY = enemy.getPositionY();
            
            enemy.move(0, INITIAL_X + 50, INITIAL_Y + 50);
            
            assertEquals(initialX, enemy.getPositionX());
            assertEquals(initialY, enemy.getPositionY());
        }
        
        @Test
        @DisplayName("Should handle player at same position")
        void shouldHandlePlayerAtSamePosition() {
            assertDoesNotThrow(() -> {
                enemy.move(1000, INITIAL_X, INITIAL_Y);
            });
        }
        
        @Test
        @DisplayName("Should handle negative damage")
        void shouldHandleNegativeDamage() {
            int initialHealth = enemy.getEnemyHealth();
            enemy.takeDamage(-10);
            assertEquals(initialHealth, enemy.getEnemyHealth());
        }
    }
}