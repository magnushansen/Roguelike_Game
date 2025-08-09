package rougelike.game.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ExplosionAnimation implements Animatable {
    private final ImageSprite sprite;
    private final double duration;
    private double elapsedTime;
    private final double x, y, width, height; 

    public ExplosionAnimation(double duration, Image[] frames, double timePerFrame, double x, double y, double width, double height) {
        this.sprite = new ImageSprite(timePerFrame, frames);
        this.duration = duration;
        this.elapsedTime = 0;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void update(double deltaTime) {
        elapsedTime += deltaTime;
        sprite.update(deltaTime);
    }

    @Override
    public void renderAnimation(GraphicsContext gc) {
        sprite.render(gc, x, y, width, height);
    }

    @Override
    public boolean isActive() {
        return elapsedTime < duration;
    }
}
