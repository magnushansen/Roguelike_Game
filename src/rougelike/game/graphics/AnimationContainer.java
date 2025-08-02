package rougelike.game.graphics;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnimationContainer<T extends Animatable> {
    private List<T> animations; 

    public AnimationContainer() {
        this.animations = new ArrayList<>();
    }

    /**
     * Add a new animation to the container.
     *
     * @param animation 
     */
    public void addAnimation(T animation) {
        animations.add(animation);
    }

    /**
     * Update and render all animations.
     * Removes animations that are no longer active.
     *
     * @param gc GraphicsContext for rendering.
     * @param deltaTime Time elapsed since the last frame.
     */
    public void renderAnimations(GraphicsContext gc, double deltaTime) {
        Iterator<T> iterator = animations.iterator();
        while (iterator.hasNext()) {
            T animation = iterator.next();
            animation.update(deltaTime); 
            if (animation.isActive()) {
                animation.renderAnimation(gc); // Render if still active
            } else {
                iterator.remove(); 
            }
        }
    }
}
