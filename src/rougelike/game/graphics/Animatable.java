package rougelike.game.graphics;

import javafx.scene.canvas.GraphicsContext;

public interface Animatable {
    void update(double deltaTime); 
    void renderAnimation(GraphicsContext gc); 
    boolean isActive(); 
}
