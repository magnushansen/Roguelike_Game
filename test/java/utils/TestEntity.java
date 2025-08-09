package utils;

/**
 * Base test entity class that provides core game logic without JavaFX dependencies.
 * Use this for pure unit tests that focus on business logic rather than graphics.
 */
public abstract class TestEntity {
    
    protected double positionX;
    protected double positionY;
    protected double width;
    protected double height;
    protected boolean isOccupying;
    
    protected TestEntity(double positionX, double positionY, double width, double height) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
        this.isOccupying = true; // Default to occupying space
    }
    
    // Getters
    public double getPositionX() { return positionX; }
    public double getPositionY() { return positionY; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public boolean isOccupying() { return isOccupying; }
    
    // Setters
    public void setPositionX(double positionX) { this.positionX = positionX; }
    public void setPositionY(double positionY) { this.positionY = positionY; }
    public void setOccupying(boolean occupying) { this.isOccupying = occupying; }
    
    // Utility methods for testing
    public double getCenterX() { return positionX + width / 2; }
    public double getCenterY() { return positionY + height / 2; }
    public double getRight() { return positionX + width; }
    public double getBottom() { return positionY + height; }
    
    /**
     * Check if this entity overlaps with another entity using AABB collision detection
     */
    public boolean overlaps(TestEntity other) {
        return positionX < other.getRight() && 
               getRight() > other.positionX &&
               positionY < other.getBottom() && 
               getBottom() > other.positionY;
    }
    
    /**
     * Calculate distance to another entity (center to center)
     */
    public double distanceTo(TestEntity other) {
        double dx = getCenterX() - other.getCenterX();
        double dy = getCenterY() - other.getCenterY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Check if a point is inside this entity
     */
    public boolean contains(double x, double y) {
        return x >= positionX && x <= getRight() && 
               y >= positionY && y <= getBottom();
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + 
               "[x=" + positionX + ", y=" + positionY + 
               ", w=" + width + ", h=" + height + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        TestEntity that = (TestEntity) obj;
        return Double.compare(that.positionX, positionX) == 0 &&
               Double.compare(that.positionY, positionY) == 0 &&
               Double.compare(that.width, width) == 0 &&
               Double.compare(that.height, height) == 0;
    }
    
    @Override
    public int hashCode() {
        int result = Double.hashCode(positionX);
        result = 31 * result + Double.hashCode(positionY);
        result = 31 * result + Double.hashCode(width);
        result = 31 * result + Double.hashCode(height);
        return result;
    }
}