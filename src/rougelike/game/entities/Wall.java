package rougelike.game.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import rougelike.game.graphics.ImageSprite;

/**
 * Represents a wall in the game that blocks movement.
 */
public class Wall extends GameElement {
    private ImageSprite sprite;

    public Wall(double positionX, double positionY, double width, double height, Image image) {
        super(positionX, positionY, width, height, image);

        this.sprite = new ImageSprite(1.0, new Image[] { image });

        
    }

    @Override
    public boolean isOccupying() {
        return true;
    }

    @Override
    public InteractionResult interact(Entity entity) {
        return new InteractionResult();
    }

 
    @Override
    public void render(GraphicsContext gc) {
        sprite.render(gc, getPositionX(), getPositionY(), getWidth(), getHeight());

    }

}
