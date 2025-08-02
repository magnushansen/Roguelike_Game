package rougelike.game.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ExplosionAnimation implements Animatable {
    private final ImageSprite sprite;
    private final double duration;
    private double elapsedTime;
    private final double x, y, width, height; 

    public ExplosionAnimation(double duration, Image[] frames, double timePerFrame, double x, double y, double width, double height) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        if (frames == null || frames.length == 0) {
            throw new IllegalArgumentException("Frames array cannot be null or empty");
        }
        if (timePerFrame <= 0) {
            throw new IllegalArgumentException("Time per frame must be positive");
        }
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and height must be non-negative");
        }
        
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
        if (deltaTime < 0) {
            throw new IllegalArgumentException("Delta time cannot be negative");
        }
        
        elapsedTime += deltaTime;
        sprite.update(deltaTime);
    }

    @Override
    public void renderAnimation(GraphicsContext gc) {
        if (gc == null) {
            throw new IllegalArgumentException("Graphics context cannot be null");
        }
        
        sprite.render(gc, x, y, width, height);
    }

    @Override
    public boolean isActive() {
        return elapsedTime < duration;
    }
}
