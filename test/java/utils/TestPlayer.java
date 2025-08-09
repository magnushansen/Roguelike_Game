package utils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Test-only Player implementation that focuses on game logic without JavaFX dependencies.
 * Use this for unit tests that need to verify player behavior, movement, combat, etc.
 */
public class TestPlayer extends TestEntity {
    
    private int health;
    private int maxHealth;
    private int damage;
    
    private double velocityX = 0;
    private double velocityY = 0;
    private double movementSpeed = 0.3;
    
    private double facingDirectionX = 1;
    private double facingDirectionY = 0;
    
    private static final Duration ATTACK_COOLDOWN = Duration.ofMillis(500);
    private Instant lastAttackTime = null;
    
    private List<String> inventory;
    
    public TestPlayer(double positionX, double positionY, double width, double height, 
                     int maxHealth, int damage) {
        super(positionX, positionY, width, height);
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.damage = damage;
        this.inventory = new ArrayList<>();
        this.isOccupying = true; // Player occupies space
    }
    
    // Movement methods
    public void moveLeft() { velocityX = -movementSpeed; }
    public void moveRight() { velocityX = movementSpeed; }
    public void moveUp() { velocityY = -movementSpeed; }
    public void moveDown() { velocityY = movementSpeed; }
    
    public void stopMovingLeft() { if (velocityX < 0) velocityX = 0; }
    public void stopMovingRight() { if (velocityX > 0) velocityX = 0; }
    public void stopMovingUp() { if (velocityY < 0) velocityY = 0; }
    public void stopMovingDown() { if (velocityY > 0) velocityY = 0; }
    
    public void stopMoving() {
        velocityX = 0;
        velocityY = 0;
    }
    
    /**
     * Update player position based on velocity and time elapsed
     */
    public void move(long timeElapsedMilli) {
        if (velocityX != 0 || velocityY != 0) {
            positionX += velocityX * timeElapsedMilli;
            positionY += velocityY * timeElapsedMilli;
            
            // Update facing direction
            if (velocityX != 0) facingDirectionX = velocityX > 0 ? 1 : -1;
            if (velocityY != 0) facingDirectionY = velocityY > 0 ? 1 : -1;
        }
    }
    
    // Health management
    public void heal(int amount) {
        if (amount > 0) {
            health += amount;
            if (health > maxHealth) {
                health = maxHealth;
            }
        }
    }
    
    public void takeDamage(int amount) {
        if (amount > 0) {
            health -= amount;
            if (health < 0) {
                health = 0;
            }
        }
    }
    
    public boolean isDead() {
        return health <= 0;
    }
    
    // Combat
    public TestProjectile attack() {
        Instant now = Instant.now();
        if (lastAttackTime != null && Duration.between(lastAttackTime, now).compareTo(ATTACK_COOLDOWN) < 0) {
            return null; // Attack on cooldown
        }
        
        lastAttackTime = now;
        
        // Determine attack direction
        double directionX, directionY;
        if (velocityX != 0 || velocityY != 0) {
            // Use movement direction if moving
            directionX = velocityX != 0 ? (velocityX > 0 ? 1 : -1) : 0;
            directionY = velocityY != 0 ? (velocityY > 0 ? 1 : -1) : 0;
        } else {
            // Use facing direction if stationary
            directionX = facingDirectionX;
            directionY = facingDirectionY;
        }
        
        // Default to right if no direction
        if (directionX == 0 && directionY == 0) {
            directionX = 1;
        }
        
        // Create projectile at player center, offset in attack direction
        double projectileX = getCenterX() + directionX * (width / 2 + 8);
        double projectileY = getCenterY() + directionY * (height / 2 + 8);
        
        return new TestProjectile(projectileX, projectileY, 8, 8, 2.0, directionX, directionY, damage);
    }
    
    // Inventory
    public void addToInventory(String item) {
        inventory.add(item);
    }
    
    public boolean hasItem(String item) {
        return inventory.contains(item);
    }
    
    public List<String> getInventory() {
        return new ArrayList<>(inventory);
    }
    
    // Getters
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getPlayerDamage() { return damage; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    public double getMovementSpeed() { return movementSpeed; }
    public double getFacingDirectionX() { return facingDirectionX; }
    public double getFacingDirectionY() { return facingDirectionY; }
    
    public boolean isMoving() {
        return velocityX != 0 || velocityY != 0;
    }
    
    public boolean canAttack() {
        if (lastAttackTime == null) return true;
        return Duration.between(lastAttackTime, Instant.now()).compareTo(ATTACK_COOLDOWN) >= 0;
    }
    
    // Setters for testing
    public void setHealth(int health) { this.health = Math.max(0, Math.min(maxHealth, health)); }
    public void setDamage(int damage) { this.damage = damage; }
    public void setMovementSpeed(double speed) { this.movementSpeed = speed; }
    public void setFacingDirection(double x, double y) { 
        this.facingDirectionX = x; 
        this.facingDirectionY = y; 
    }
    
    @Override
    public String toString() {
        return "TestPlayer[x=" + positionX + ", y=" + positionY + 
               ", health=" + health + "/" + maxHealth + 
               ", damage=" + damage + ", moving=" + isMoving() + "]";
    }
}