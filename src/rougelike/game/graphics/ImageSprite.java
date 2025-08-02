package rougelike.game.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ImageSprite {
    private Image[] images;  
    private double timePerImage; 
    private double elapsedTime; 
    private int currentIndex; 

    public ImageSprite(double timePerImage, Image[] images) {
        this.timePerImage = timePerImage;
        this.images = images;
        this.elapsedTime = 0;
        this.currentIndex = 0;
    }

    /**
     * Update the sprite's state based on the elapsed time.
     *
     * @param deltaTime Time elapsed since the last update, in seconds.
     */
    public void update(double deltaTime) {
        elapsedTime += deltaTime;

        // Check if we need to move to the next frame
        if (elapsedTime >= timePerImage) {
            currentIndex = (currentIndex + 1) % images.length; // Loop back to start
            elapsedTime -= timePerImage; // Reset elapsed time for the next frame
        }
    }

    /**
     * Render the current image of the sprite to the GraphicsContext.
     *
     * @param gc 
     * @param x 
     * @param y  
     * @param width 
     * @param height 
     */
    public void render(GraphicsContext gc, double x, double y, double width, double height) {
        if (images.length > 0 && currentIndex < images.length) {
            gc.drawImage(images[currentIndex], x, y, width, height);
        }
    }
}
