package rougelike.game.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ImageSprite {
    private final Image[] images;  
    private final double timePerImage; 
    private double elapsedTime; 
    private int currentIndex; 

    public ImageSprite(double timePerImage, Image[] images) {
        if (images == null || images.length == 0) {
            throw new IllegalArgumentException("Images array cannot be null or empty");
        }
        if (timePerImage <= 0) {
            throw new IllegalArgumentException("Time per image must be positive");
        }
        
        this.timePerImage = timePerImage;
        this.images = images.clone();
        this.elapsedTime = 0;
        this.currentIndex = 0;
    }

    /**
     * Update the sprite's state based on the elapsed time.
     *
     * @param deltaTime Time elapsed since the last update, in seconds. Must be non-negative.
     * @throws IllegalArgumentException if deltaTime is negative
     */
    public void update(double deltaTime) {
        if (deltaTime < 0) {
            throw new IllegalArgumentException("Delta time cannot be negative");
        }
        
        elapsedTime += deltaTime;

        // Check if we need to move to the next frame
        if (elapsedTime >= timePerImage) {
            currentIndex = (currentIndex + 1) % images.length;
            elapsedTime -= timePerImage;
        }
    }

    /**
     * Render the current image of the sprite to the GraphicsContext.
     *
     * @param gc The graphics context to render to
     * @param x The x-coordinate to render at
     * @param y The y-coordinate to render at
     * @param width The width to render the image
     * @param height The height to render the image
     * @throws IllegalArgumentException if gc is null or dimensions are negative
     */
    public void render(GraphicsContext gc, double x, double y, double width, double height) {
        if (gc == null) {
            throw new IllegalArgumentException("Graphics context cannot be null");
        }
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and height must be non-negative");
        }
        
        if (currentIndex >= 0 && currentIndex < images.length && images[currentIndex] != null) {
            gc.drawImage(images[currentIndex], x, y, width, height);
        }
    }
}
