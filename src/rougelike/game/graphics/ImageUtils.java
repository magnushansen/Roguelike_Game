package rougelike.game.graphics;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;

public class ImageUtils {

    /**
     * Splits a given sprite sheet into an array of individual frames.
     *
     * @param image The sprite sheet image to partition, cannot be null
     * @param numImages The number of frames to partition the image into, must be positive
     * @return An array of images, each representing a frame
     * @throws IllegalArgumentException if image is null or numImages is not positive
     */
    public static Image[] partitionImage(Image image, int numImages) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        if (numImages <= 0) {
            throw new IllegalArgumentException("Number of images must be positive");
        }
        if (image.getWidth() <= 0 || image.getHeight() <= 0) {
            throw new IllegalArgumentException("Image must have positive dimensions");
        }
        
        Image[] images = new Image[numImages];
        PixelReader pixelReader = image.getPixelReader();
        if (pixelReader == null) {
            throw new IllegalArgumentException("Unable to read pixels from image");
        }
        
        int w = (int) (image.getWidth() / numImages);
        int h = (int) image.getHeight();
        
        if (w <= 0) {
            throw new IllegalArgumentException("Image width too small for requested number of partitions");
        }           

        for (int i = 0; i < numImages; i++) {
            WritableImage imageSection = new WritableImage(w, h);
            PixelWriter pixelWriter = imageSection.getPixelWriter();
            
            int stride = i * w;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int sourceX = stride + x;
                    if (sourceX < image.getWidth()) {
                        pixelWriter.setColor(x, y, pixelReader.getColor(sourceX, y));
                    }
                }
            }
            images[i] = imageSection;
        }
        return images;
    }
}
