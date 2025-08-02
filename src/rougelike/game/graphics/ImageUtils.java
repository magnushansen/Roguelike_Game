package rougelike.game.graphics;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;

public class ImageUtils {

    /**
     * Splits a given sprite sheet into an array of individual frames.
     *
     * @param image The sprite sheet image to partition.
     * @param numImages The number of frames to partition the image into.
     * @return An array of images, each representing a frame.
     */
    public static Image[] partitionImage(Image image, int numImages) {
        Image[] images = new Image[numImages];
        PixelReader pixelReader = image.getPixelReader();
        int w = (int) (image.getWidth() / numImages); 
        int h = (int) image.getHeight();           

        for (int i = 0; i < numImages; i++) {
            WritableImage imageSection = new WritableImage(w, h);
            PixelWriter pixelWriter = imageSection.getPixelWriter();
            for (int y = 0; y < h; y++) {
                int stride = i * w;
                for (int x = stride; x < stride + w; x++) {
                    pixelWriter.setColor(
                        x - stride, y, pixelReader.getColor(x, y)
                    );
                }
            }
            images[i] = imageSection;
        }
        return images;
    }
}
