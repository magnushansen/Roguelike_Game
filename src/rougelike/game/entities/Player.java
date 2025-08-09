package rougelike.game.entities;

import java.time.Duration;
import java.time.Instant;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import rougelike.game.graphics.ImageSprite;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    private int health;
    private int maxHealth;
    private int damage;
    private ImageSprite movingSprite;
    private ImageSprite idleSprite;
    private ImageSprite currentSprite;
    private double movementSpeed = 0.3;
    private double velocityX;
    private double velocityY;
    private double facingDirectionX = 1;
    private double facingDirectionY = 0;
    private static final Duration attackSpeed = Duration.ofMillis(500);
    private Instant lastEventTime = null;
    private List<String> inventory;

    public Player(double positionX, double positionY, double width, double height, Image idleImage, Image[] movingFrames, Image[] idleFrames, int maxHealth, int damage) {
        super(positionX, positionY, width, height, idleImage);
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.damage = damage;
        velocityX = 0;
        velocityY = 0;

        this.movingSprite = new ImageSprite(0.2, movingFrames); // Moving animation, 0.2 seconds per frame
        this.idleSprite = new ImageSprite(0.5, idleFrames);     // Idle animation, static frame
        this.currentSprite = idleSprite; // Start with idle animation
        this.inventory = new ArrayList<>();
    }

    public void move(long timeElapsedMilli) {

        if (velocityX != 0 || velocityY != 0) {
            setPositionX(getPositionX() + velocityX * timeElapsedMilli);
            setPositionY(getPositionY() + velocityY * timeElapsedMilli);

            // Switch to moving animation
            currentSprite = movingSprite;
        } else {
            // Switch to idle animation
            currentSprite = idleSprite;
        }

        currentSprite.update(timeElapsedMilli / 1000.0); // Convert ms to seconds
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
        System.out.println("Player healed by " + amount + " points. Current health: " + health);
    }

    public Projectile attack() {
        double directionX = 0;
        double directionY = 0;

        if (velocityX != 0 || velocityY != 0) {
            directionX = velocityX != 0 ? velocityX / Math.abs(velocityX) : 0;
            directionY = velocityY != 0 ? velocityY / Math.abs(velocityY) : 0;
            facingDirectionX = directionX;
            facingDirectionY = directionY;
        } else {
            directionX = facingDirectionX;
            directionY = facingDirectionY;
        }

        if (directionX == 0 && directionY == 0) {
            directionX = 1; // Default facing direction
        }

        return new Projectile(
                getPositionX() + getWidth() / 2,
                getPositionY() + getHeight() / 2,
                height / 2, width / 2,
                ImageDatabase.getImage('p'),
                1,
                directionX,
                directionY,
                damage);
    }

  public void takeDamage(int amount) {
      Instant now = Instant.now();
      if (lastEventTime == null || Duration.between(lastEventTime, now).compareTo(attackSpeed) >= 0) {
          health -= amount;
          lastEventTime = now;
          if (isDead()) {
              System.out.println("Player defeated!");
          }
      }
  }

    public boolean isDead() {
        return health <= 0;
    }


    public void moveLeft() {
        velocityX = -movementSpeed;
        facingDirectionX = -1;
        if (velocityY == 0)
            facingDirectionY = 0;
    }

    public void moveRight() {
        velocityX = movementSpeed;
        facingDirectionX = 1;
        if (velocityY == 0)
            facingDirectionY = 0;
    }

    public void moveUp() {
        velocityY = -movementSpeed;
        facingDirectionY = -1;
        if (velocityX == 0)
            facingDirectionX = 0;
    }

    public void moveDown() {
        velocityY = movementSpeed;
        facingDirectionY = 1;
        if (velocityX == 0)
            facingDirectionX = 0;
    }

    public void stopMovingLeft() {
        velocityX = 0;
    }

    public void stopMovingRight() {
        velocityX = 0;
    }

    public void stopMovingUp() {
        velocityY = 0;
    }

    public void stopMovingDown() {
        velocityY = 0;
    }

    public int getPlayerDamage() {
        return damage;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health) {
        this.health = Math.min(health, maxHealth);
    }

    public List<String> getInventory() {
        return new ArrayList<>(inventory);
    }

    @Override
    public void render(GraphicsContext gc) {
        currentSprite.render(gc, getPositionX(), getPositionY(), getWidth(), getHeight());
    }
}