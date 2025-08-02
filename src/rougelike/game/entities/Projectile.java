package rougelike.game.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import rougelike.game.graphics.ImageSprite;

public class Projectile extends GameElement {
    private double velocityX;
    private double velocityY;
    private int damage;
    private ImageSprite sprite;

    public Projectile(double positionX, double positionY, double width, double height, Image image, double speed,
            double directionX, double directionY, int damage) {
        super(positionX, positionY, width, height, image);
        this.velocityX = directionX * speed;
        this.velocityY = directionY * speed;
        this.damage = damage;

        // Initialize the sprite with a single image
        this.sprite = new ImageSprite(1.0, new Image[] { image });
    }

    @Override
    public boolean isOccupying() {
        return false;
    }

    @Override
    public InteractionResult interact(Entity entity) {
        InteractionResultType[] interactionResultType = {};

        if (entity instanceof Enemy) {
            interactionResultType = new InteractionResultType[] {
                    InteractionResultType.TAKE_DAMAGE
            };
            System.out.println("Projectile hit an enemy and dealt " + damage + " damage.");
        }
        return new InteractionResult(interactionResultType, entity);
    }

    public void updatePosition(long timeElapsedMilli) {
        setPositionX(getPositionX() + velocityX * timeElapsedMilli);
        setPositionY(getPositionY() + velocityY * timeElapsedMilli);
    }

    @Override
    public void render(GraphicsContext gc) {
        sprite.render(gc, getPositionX(), getPositionY(), getWidth(), getHeight());
    }
}
