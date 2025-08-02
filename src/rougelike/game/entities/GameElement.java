package rougelike.game.entities;

import javafx.scene.image.Image;

public abstract class GameElement extends Entity {
    public GameElement(double positionX, double positionY, double width, double height, Image image) {
        super(positionX, positionY, width, height, image);
    }

    public abstract boolean isOccupying();

    public abstract InteractionResult interact(Entity entity);
}
