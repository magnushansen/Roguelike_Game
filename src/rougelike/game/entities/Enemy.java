package rougelike.game.entities;

import java.time.Duration;
import java.time.Instant;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import rougelike.game.graphics.ImageSprite;

public class Enemy extends GameElement {
    private int health;
    private int damage;
    private double previousPositionX;
    private double previousPositionY;
    private double velocityX;
    private double velocityY;
    private double speed; 
    private double detectionRadius;
    private static final Duration attackSpeed = Duration.ofMillis(500);
    private static Instant lastEventTime = null;

    private ImageSprite sprite;

    public Enemy(double positionX, double positionY, double width, double height, Image[] animationFrames, int health, int damage,
            double speed, double detectionRadius, double timePerImage) {
        super(positionX, positionY, width, height, animationFrames[0]);
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.detectionRadius = detectionRadius;
        this.sprite = new ImageSprite(timePerImage, animationFrames);

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

    public void move(long timeElapsedMilli, double playerX, double playerY) {
        // Update position
        previousPositionX = getPositionX();
        previousPositionY = getPositionY();

        // Calculate the distance to the player
        double distanceToPlayer = Math
                .sqrt(Math.pow(playerX - getPositionX(), 2) + Math.pow(playerY - getPositionY(), 2));

        if (distanceToPlayer <= getdetectionRadius()) {
            // If the player is within the detection radius, move toward the player
            double angleToPlayer = Math.atan2(playerY - getPositionY(), playerX - getPositionX()); 
                                                                                                  
            velocityX = Math.cos(angleToPlayer) * speed;
            velocityY = Math.sin(angleToPlayer) * speed;
        } else {
            // Randomly change direction with a low probability
            if (Math.random() < 0.05) { 
                double angle = Math.random() * 2 * Math.PI;
                velocityX = Math.cos(angle) * speed;
                velocityY = Math.sin(angle) * speed;
            }
        }

        setPositionX(getPositionX() + velocityX * timeElapsedMilli);
        setPositionY(getPositionY() + velocityY * timeElapsedMilli);
    }

    public void undoMove() {
        setPositionX(previousPositionX);
        setPositionY(previousPositionY);
    }

    @Override
    public boolean isOccupying() {
        return true;
    }

    @Override
    public InteractionResult interact(Entity entity) {
        InteractionResultType[] interactionResultType = {};
        if (entity instanceof Player) {

            interactionResultType = new InteractionResultType[] {
                    InteractionResultType.TAKE_DAMAGE
            };
        }
        return new InteractionResult(interactionResultType, entity);
    }


    public int getEnemyDamage() {
        return damage;
    }

    public int getEnemyHealth() {
        return health;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public double getdetectionRadius() {
        return detectionRadius;
    }

        public void update(double deltaTime) {
        sprite.update(deltaTime);
    }

    @Override
    public void render(GraphicsContext gc) {
        sprite.render(gc, getPositionX(), getPositionY(), getWidth(), getHeight());
    }
}
