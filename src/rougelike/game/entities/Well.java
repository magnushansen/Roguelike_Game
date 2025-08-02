package rougelike.game.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import rougelike.game.graphics.ImageSprite;

public class Well extends GameElement {
    private int healAmount;
    private boolean activated;
    private ImageSprite sprite;

    public Well(double positionX, double positionY, double width, double height, Image[] animationFrames, int healAmount, double timePerImage) {
        super(positionX, positionY, width, height, animationFrames[0]); // Use the first frame as the default image
        this.healAmount = healAmount;
        this.activated = false;
        this.sprite = new ImageSprite(timePerImage, animationFrames); // Initialize the animation
    }

    @Override
    public boolean isOccupying() {
        return true;
    }

    @Override
    public InteractionResult interact(Entity entity) {
        InteractionResultType[] interactionResultType = {};
        // Check if the Well has already been used
        if (!this.activated) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                // Only heal and mark as used if the player is not at max health
                if (player.getHealth() < player.getMaxHealth()) {
                    interactionResultType = new InteractionResultType[]{
                            InteractionResultType.HEAL
                    };
                    this.hasBeenUsed();
                }
            }
        }

        return new InteractionResult(interactionResultType, entity, this);
    }

    @Override
    public void render(GraphicsContext gc) {
        sprite.render(gc, getPositionX(), getPositionY(), getWidth(), getHeight());
    }

    /**
     * Update the animation state.
     *
     * @param deltaTime Time elapsed since the last frame, in seconds.
     */
    public void update(double deltaTime) {
        sprite.update(deltaTime);
    }

    public void hasBeenUsed() {
        this.activated = true; 
    }

    public boolean isActivated() {
        return activated;
    }

    public int getHealAmount() {
        return healAmount;
    }
}
