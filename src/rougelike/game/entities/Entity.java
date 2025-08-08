package rougelike.game.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Entity {
    private double positionX;
    private double positionY;
    protected double width;
    protected double height;
    private Image image;

    public Entity(double positionX, double positionY, double width, double height, Image image) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public abstract void render(GraphicsContext gc);

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public double getWidth() {
        return width;
    }

    protected void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    protected void setHeight(double height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    protected void setImage(Image image) {
        this.image = image;
    }
}
