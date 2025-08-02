package rougelike.game.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import rougelike.game.graphics.ImageSprite;


public class Floor extends GameElement {
    private ImageSprite sprite;


    public Floor(double positionX, double positionY, double width, double height, Image image) {
        super(positionX, positionY, width, height, image);
        this.sprite = new ImageSprite(1.0, new Image[] { image });

    }


    @Override
    public boolean isOccupying() {
        return false;
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
