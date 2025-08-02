package rougelike.game.graphics;

import javafx.scene.canvas.GraphicsContext;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Iterator;
import java.util.List;

public class AnimationContainer<T extends Animatable> {
    private final List<T> animations; 

    public AnimationContainer() {
        this.animations = new CopyOnWriteArrayList<>();
    }

    /**
     * Add a new animation to the container.
     *
     * @param animation The animation to add, cannot be null
     * @throws IllegalArgumentException if animation is null
     */
    public void addAnimation(T animation) {
        if (animation == null) {
            throw new IllegalArgumentException("Animation cannot be null");
        }
        animations.add(animation);
    }

    /**
     * Update and render all animations.
     * Removes animations that are no longer active.
     *
     * @param gc GraphicsContext for rendering, cannot be null
     * @param deltaTime Time elapsed since the last frame, must be non-negative
     * @throws IllegalArgumentException if gc is null or deltaTime is negative
     */
    public void renderAnimations(GraphicsContext gc, double deltaTime) {
        if (gc == null) {
            throw new IllegalArgumentException("Graphics context cannot be null");
        }
        if (deltaTime < 0) {
            throw new IllegalArgumentException("Delta time cannot be negative");
        }
        
        Iterator<T> iterator = animations.iterator();
        while (iterator.hasNext()) {
            T animation = iterator.next();
            animation.update(deltaTime);
            if (animation.isActive()) {
                animation.renderAnimation(gc);
            } else {
                iterator.remove();
            }
        }
    }
}
