package utils;

/**
 * Test-only Projectile implementation for pure logic testing without JavaFX dependencies.
 */
public class TestProjectile extends TestEntity {
    
    private final double speed;
    private final double directionX;
    private final double directionY;
    private final int damage;
    
    private boolean active = true;
    private double distanceTraveled = 0;
    private final double maxRange;
    
    private static final double DEFAULT_MAX_RANGE = 500.0;
    
    public TestProjectile(double positionX, double positionY, double width, double height,
                         double speed, double directionX, double directionY, int damage) {
        this(positionX, positionY, width, height, speed, directionX, directionY, damage, DEFAULT_MAX_RANGE);
    }
    
    public TestProjectile(double positionX, double positionY, double width, double height,
                         double speed, double directionX, double directionY, int damage, double maxRange) {
        super(positionX, positionY, width, height);
        this.speed = speed;
        this.directionX = directionX;
        this.directionY = directionY;
        this.damage = damage;
        this.maxRange = maxRange;
        this.isOccupying = false; // Projectiles don't block movement
        
        // Normalize direction vector
        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
        if (magnitude == 0) {
            throw new IllegalArgumentException("Direction vector cannot be zero");
        }
    }
    
    /**
     * Update projectile position based on time elapsed
     */
    public void move(long timeElapsedMilli) {
        if (!active) return;
        
        double distance = speed * timeElapsedMilli;
        positionX += directionX * distance;
        positionY += directionY * distance;
        
        distanceTraveled += distance;
        
        // Deactivate if max range exceeded
        if (distanceTraveled >= maxRange) {
            active = false;
        }
    }
    
    /**
     * Handle collision with target
     */
    public boolean collideWith(TestEntity target) {
        if (!active || !overlaps(target)) {
            return false;
        }
        
        // Projectile hits target and becomes inactive
        active = false;
        return true;
    }
    
    /**
     * Handle collision with environment (walls, etc.)
     */
    public void hitEnvironment() {
        active = false;
    }
    
    /**
     * Check if projectile is within bounds
     */
    public boolean isWithinBounds(double minX, double minY, double maxX, double maxY) {
        return positionX >= minX && positionX + width <= maxX &&
               positionY >= minY && positionY + height <= maxY;
    }
    
    /**
     * Predict future position after given time
     */
    public TestProjectile predictPosition(long timeElapsedMilli) {
        double futureDistance = speed * timeElapsedMilli;
        double futureX = positionX + directionX * futureDistance;
        double futureY = positionY + directionY * futureDistance;
        
        TestProjectile future = new TestProjectile(futureX, futureY, width, height,
                                                  speed, directionX, directionY, damage, maxRange);
        future.distanceTraveled = this.distanceTraveled + futureDistance;
        future.active = this.active && future.distanceTraveled < maxRange;
        
        return future;
    }
    
    // Getters
    public double getSpeed() { return speed; }
    public double getDirectionX() { return directionX; }
    public double getDirectionY() { return directionY; }
    public int getDamage() { return damage; }
    public boolean isActive() { return active; }
    public double getDistanceTraveled() { return distanceTraveled; }
    public double getMaxRange() { return maxRange; }
    
    public double getRemainingRange() { return maxRange - distanceTraveled; }
    public boolean hasReachedMaxRange() { return distanceTraveled >= maxRange; }
    
    // Setters for testing
    public void setActive(boolean active) { this.active = active; }
    public void setDistanceTraveled(double distance) { this.distanceTraveled = distance; }
    
    /**
     * Calculate time to reach a target position
     */
    public double timeToReach(double targetX, double targetY) {
        double dx = targetX - positionX;
        double dy = targetY - positionY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance / speed;
    }
    
    /**
     * Check if projectile will hit a target moving at given velocity
     */
    public boolean willHit(TestEntity target, double targetVelX, double targetVelY, long timeElapsedMilli) {
        // Predict both positions
        TestProjectile futureProjectile = predictPosition(timeElapsedMilli);
        
        double futureTargetX = target.getPositionX() + targetVelX * timeElapsedMilli;
        double futureTargetY = target.getPositionY() + targetVelY * timeElapsedMilli;
        
        return futureProjectile.contains(futureTargetX + target.getWidth() / 2, 
                                        futureTargetY + target.getHeight() / 2);
    }
    
    @Override
    public String toString() {
        return "TestProjectile[x=" + positionX + ", y=" + positionY + 
               ", damage=" + damage + ", speed=" + speed + 
               ", direction=(" + directionX + "," + directionY + ")" +
               ", active=" + active + ", traveled=" + distanceTraveled + "]";
    }
}