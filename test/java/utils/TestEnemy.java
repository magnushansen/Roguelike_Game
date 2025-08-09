package utils;

import java.time.Duration;
import java.time.Instant;

/**
 * Test-only Enemy implementation for pure logic testing without JavaFX dependencies.
 */
public class TestEnemy extends TestEntity {
    
    private int health;
    private int maxHealth;
    private int damage;
    
    private double speed;
    private double detectionRadius;
    
    private double velocityX = 0;
    private double velocityY = 0;
    
    private double previousPositionX;
    private double previousPositionY;
    
    private static final Duration ATTACK_COOLDOWN = Duration.ofMillis(500);
    private Instant lastAttackTime = null;
    
    public TestEnemy(double positionX, double positionY, double width, double height,
                    int health, int damage, double speed, double detectionRadius) {
        super(positionX, positionY, width, height);
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.speed = speed;
        this.detectionRadius = detectionRadius;
        this.isOccupying = true; // Enemies occupy space
    }
    
    /**
     * Update enemy movement based on player position
     */
    public void move(long timeElapsedMilli, double playerX, double playerY) {
        // Store previous position for collision resolution
        previousPositionX = positionX;
        previousPositionY = positionY;
        
        double distanceToPlayer = Math.sqrt(
            Math.pow(playerX - positionX, 2) + 
            Math.pow(playerY - positionY, 2)
        );
        
        if (distanceToPlayer <= detectionRadius) {
            // Move toward player
            double angleToPlayer = Math.atan2(playerY - positionY, playerX - positionX);
            velocityX = Math.cos(angleToPlayer) * speed;
            velocityY = Math.sin(angleToPlayer) * speed;
        } else {
            // Random movement with low probability
            if (Math.random() < 0.05) {
                double angle = Math.random() * 2 * Math.PI;
                velocityX = Math.cos(angle) * speed;
                velocityY = Math.sin(angle) * speed;
            }
        }
        
        // Update position
        positionX += velocityX * timeElapsedMilli;
        positionY += velocityY * timeElapsedMilli;
    }
    
    /**
     * Undo the last movement (used for collision resolution)
     */
    public void undoMove() {
        positionX = previousPositionX;
        positionY = previousPositionY;
    }
    
    /**
     * Take damage from attacks
     */
    public void takeDamage(int amount) {
        Instant now = Instant.now();
        if (lastAttackTime != null && Duration.between(lastAttackTime, now).compareTo(ATTACK_COOLDOWN) < 0) {
            return; // Damage immunity period
        }
        
        if (amount > 0) {
            health -= amount;
            if (health < 0) health = 0;
        }
        lastAttackTime = now;
    }
    
    /**
     * Check if enemy can detect a target at the given position
     */
    public boolean canDetect(double targetX, double targetY) {
        double distance = Math.sqrt(
            Math.pow(targetX - getCenterX(), 2) + 
            Math.pow(targetY - getCenterY(), 2)
        );
        return distance <= detectionRadius;
    }
    
    /**
     * Check if enemy can attack (not on cooldown)
     */
    public boolean canAttack() {
        if (lastAttackTime == null) return true;
        return Duration.between(lastAttackTime, Instant.now()).compareTo(ATTACK_COOLDOWN) >= 0;
    }
    
    /**
     * Perform an attack (for testing attack mechanics)
     */
    public boolean attack(TestEntity target) {
        if (!canAttack()) return false;
        if (!overlaps(target)) return false;
        
        lastAttackTime = Instant.now();
        return true; // Attack successful
    }
    
    // Getters
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getEnemyHealth() { return health; }
    public int getEnemyDamage() { return damage; }
    public double getSpeed() { return speed; }
    public double getDetectionRadius() { return detectionRadius; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    
    public boolean isDead() { return health <= 0; }
    public boolean isMoving() { return velocityX != 0 || velocityY != 0; }
    
    // Setters for testing
    public void setHealth(int health) { 
        this.health = Math.max(0, Math.min(maxHealth, health)); 
    }
    public void setDamage(int damage) { this.damage = damage; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setDetectionRadius(double radius) { this.detectionRadius = radius; }
    
    public void setVelocity(double vx, double vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }
    
    /**
     * Reset attack cooldown (useful for testing)
     */
    public void resetAttackCooldown() {
        this.lastAttackTime = null;
    }
    
    @Override
    public String toString() {
        return "TestEnemy[x=" + positionX + ", y=" + positionY + 
               ", health=" + health + "/" + maxHealth + 
               ", damage=" + damage + ", speed=" + speed + 
               ", detection=" + detectionRadius + "]";
    }
}