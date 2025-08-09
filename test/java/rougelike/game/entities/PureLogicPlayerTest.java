package rougelike.game.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import utils.TestPlayer;
import utils.TestEnemy;
import utils.TestProjectile;
import utils.TestUtils;

/**
 * Pure logic tests for Player behavior using TestEntity hierarchy.
 * These tests avoid JavaFX dependencies and focus on game mechanics.
 */
@Tag("unit")
@Tag("entities") 
@Tag("logic")
@DisplayName("Pure Logic Player Tests")
class PureLogicPlayerTest {
    
    private TestPlayer player;
    
    @BeforeEach
    void setUp() {
        player = TestUtils.createTestPlayer();
    }
    
    @Nested
    @DisplayName("Movement Logic Tests")
    class MovementLogicTests {
        
        @Test
        @DisplayName("Should move left when moveLeft called")
        void shouldMoveLeftWhenMoveLeftCalled() {
            double initialX = player.getPositionX();
            
            player.moveLeft();
            player.move(1000); // 1 second
            
            assertTrue(player.getPositionX() < initialX);
            assertTrue(player.isMoving());
        }
        
        @Test
        @DisplayName("Should move right when moveRight called")
        void shouldMoveRightWhenMoveRightCalled() {
            double initialX = player.getPositionX();
            
            player.moveRight();
            player.move(1000);
            
            assertTrue(player.getPositionX() > initialX);
            assertTrue(player.isMoving());
        }
        
        @Test
        @DisplayName("Should handle diagonal movement")
        void shouldHandleDiagonalMovement() {
            double initialX = player.getPositionX();
            double initialY = player.getPositionY();
            
            player.moveRight();
            player.moveDown();
            player.move(1000);
            
            assertTrue(player.getPositionX() > initialX);
            assertTrue(player.getPositionY() > initialY);
            assertTrue(player.isMoving());
        }
        
        @Test
        @DisplayName("Should stop moving when stop methods called")
        void shouldStopMovingWhenStopMethodsCalled() {
            player.moveRight();
            player.moveDown();
            
            assertTrue(player.isMoving());
            
            player.stopMoving();
            
            assertFalse(player.isMoving());
            assertEquals(0, player.getVelocityX());
            assertEquals(0, player.getVelocityY());
        }
        
        @Test
        @DisplayName("Should update facing direction based on movement")
        void shouldUpdateFacingDirectionBasedOnMovement() {
            player.moveRight();
            player.move(100);
            
            assertEquals(1, player.getFacingDirectionX());
            
            player.stopMoving();
            player.moveLeft();
            player.move(100);
            
            assertEquals(-1, player.getFacingDirectionX());
        }
    }
    
    @Nested
    @DisplayName("Health Management Tests")
    class HealthManagementTests {
        
        @Test
        @DisplayName("Should start with full health")
        void shouldStartWithFullHealth() {
            assertEquals(player.getMaxHealth(), player.getHealth());
            assertFalse(player.isDead());
        }
        
        @Test
        @DisplayName("Should take damage correctly")
        void shouldTakeDamageCorrectly() {
            int initialHealth = player.getHealth();
            int damage = 25;
            
            player.takeDamage(damage);
            
            assertEquals(initialHealth - damage, player.getHealth());
            assertFalse(player.isDead());
        }
        
        @Test
        @DisplayName("Should die when health reaches zero")
        void shouldDieWhenHealthReachesZero() {
            player.takeDamage(player.getHealth());
            
            assertTrue(player.isDead());
            assertEquals(0, player.getHealth());
        }
        
        @Test
        @DisplayName("Should heal correctly")
        void shouldHealCorrectly() {
            player.takeDamage(50);
            int healthAfterDamage = player.getHealth();
            
            player.heal(25);
            
            assertEquals(healthAfterDamage + 25, player.getHealth());
            assertFalse(player.isDead());
        }
        
        @Test
        @DisplayName("Should not heal above max health")
        void shouldNotHealAboveMaxHealth() {
            int maxHealth = player.getMaxHealth();
            
            player.heal(999);
            
            assertEquals(maxHealth, player.getHealth());
        }
    }
    
    @Nested
    @DisplayName("Combat Logic Tests")
    class CombatLogicTests {
        
        @Test
        @DisplayName("Should create projectile when attacking")
        void shouldCreateProjectileWhenAttacking() {
            TestProjectile projectile = player.attack();
            
            assertNotNull(projectile);
            assertEquals(player.getPlayerDamage(), projectile.getDamage());
            assertTrue(projectile.isActive());
        }
        
        @Test
        @DisplayName("Should attack in facing direction when stationary")
        void shouldAttackInFacingDirectionWhenStationary() {
            // Player faces right by default
            TestProjectile projectile = player.attack();
            
            assertNotNull(projectile);
            assertTrue(projectile.getPositionX() > player.getCenterX());
            assertEquals(1.0, projectile.getDirectionX());
        }
        
        @Test
        @DisplayName("Should attack in movement direction when moving")
        void shouldAttackInMovementDirectionWhenMoving() {
            player.moveLeft();
            TestProjectile projectile = player.attack();
            
            assertNotNull(projectile);
            assertEquals(-1.0, projectile.getDirectionX());
        }
        
        @Test
        @DisplayName("Should respect attack cooldown")
        void shouldRespectAttackCooldown() {
            TestProjectile firstAttack = player.attack();
            TestProjectile secondAttack = player.attack(); // Immediate second attack
            
            assertNotNull(firstAttack);
            assertNull(secondAttack); // Should be null due to cooldown
            
            assertFalse(player.canAttack()); // Should be on cooldown
        }
    }
    
    @Nested
    @DisplayName("Inventory Tests")
    class InventoryTests {
        
        @Test
        @DisplayName("Should start with empty inventory")
        void shouldStartWithEmptyInventory() {
            assertTrue(player.getInventory().isEmpty());
        }
        
        @Test
        @DisplayName("Should add items to inventory")
        void shouldAddItemsToInventory() {
            player.addToInventory("Health Potion");
            
            assertTrue(player.hasItem("Health Potion"));
            assertEquals(1, player.getInventory().size());
        }
        
        @Test
        @DisplayName("Should handle multiple items")
        void shouldHandleMultipleItems() {
            player.addToInventory("Health Potion");
            player.addToInventory("Mana Potion");
            player.addToInventory("Key");
            
            assertTrue(player.hasItem("Health Potion"));
            assertTrue(player.hasItem("Mana Potion"));
            assertTrue(player.hasItem("Key"));
            assertEquals(3, player.getInventory().size());
        }
    }
    
    @Nested
    @DisplayName("Collision Logic Tests")
    class CollisionLogicTests {
        
        @Test
        @DisplayName("Should detect overlap with enemy")
        void shouldDetectOverlapWithEnemy() {
            TestEnemy enemy = TestUtils.createTestEnemy(
                player.getPositionX(), player.getPositionY(), 32, 32, 50, 10, 1.0, 100.0
            );
            
            assertTrue(player.overlaps(enemy));
        }
        
        @Test
        @DisplayName("Should not overlap with distant entity")
        void shouldNotOverlapWithDistantEntity() {
            TestEnemy enemy = TestUtils.createTestEnemy(500, 500, 32, 32, 50, 10, 1.0, 100.0);
            
            assertFalse(player.overlaps(enemy));
        }
        
        @Test
        @DisplayName("Should calculate distance correctly")
        void shouldCalculateDistanceCorrectly() {
            TestEnemy enemy = TestUtils.createTestEnemy(200, 200, 32, 32, 50, 10, 1.0, 100.0);
            
            // Player center: (116, 116), Enemy center: (216, 216)
            double expectedDistance = Math.sqrt(Math.pow(100, 2) + Math.pow(100, 2)); // Distance between centers
            double actualDistance = player.distanceTo(enemy);
            
            assertEquals(expectedDistance, actualDistance, 0.01);
        }
    }
}