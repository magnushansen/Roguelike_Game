package rougelike.game.graphics;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for objects that can be animated and rendered.
 * Implementations should manage their own animation state and lifecycle.
 */
public interface Animatable {
    /**
     * Update the animation state based on elapsed time.
     *
     * @param deltaTime Time elapsed since the last update, in seconds
     */
    void update(double deltaTime);
    
    /**
     * Render the animation to the provided graphics context.
     *
     * @param gc The graphics context to render to
     */
    void renderAnimation(GraphicsContext gc);
    
    /**
     * Check if the animation is still active and should continue updating/rendering.
     *
     * @return true if the animation is active, false if it should be removed
     */
    boolean isActive();
}
