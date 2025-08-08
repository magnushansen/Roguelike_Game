package rougelike.game.entities;

import rougelike.Global;
import rougelike.game.GameModel;

public class CollisionDetection {
    private static final double GAME_BOUNDARY_LEFT = 0 + GameModel.getTileWidth();
    private static final double GAME_BOUNDARY_TOP = 0 + GameModel.getTileHeight();
    private static final double GAME_BOUNDARY_RIGHT = Global.WINDOW_WIDTH - GameModel.getTileWidth(); // Example width
    private static final double GAME_BOUNDARY_BOTTOM = Global.WINDOW_HEIGHT - GameModel.getTileHeight(); // Example
                                                                                                        // height

    /**
     * @param a an entity
     * @param b a second entity
     * @return return true if a collides with b using aabb collision detection
     */
    public static boolean Aabb(Entity a, Entity b) {
        return a.getPositionX() + a.getWidth() > b.getPositionX() &&
                b.getPositionX() + b.getWidth() > a.getPositionX() &&
                a.getPositionY() + a.getHeight() > b.getPositionY() &&
                b.getPositionY() + b.getHeight() > a.getPositionY();
    }

    /**
     * Resolves the collision between two entities by calculating the minimum
     * translation vector needed to separate them.
     *
     * @param movingEntity The entity being moved (e.g., the player).
     * @param staticEntity The entity it collided with (e.g., a wall or enemy).
     */
    public static void resolveCollision(Entity movingEntity, Entity staticEntity) {
        double overlapX = calculateHorizontalOverlap(movingEntity, staticEntity);
        double overlapY = calculateVerticalOverlap(movingEntity, staticEntity);

        if (Math.abs(overlapX) < Math.abs(overlapY)) {
            movingEntity.setPositionX(movingEntity.getPositionX() + overlapX);
        } else {
            movingEntity.setPositionY(movingEntity.getPositionY() + overlapY);
        }

        // Clamp the position to prevent moving out of bounds
        clampPosition(movingEntity);
    }

    /**
     * Clamps the position of the entity within the game boundaries.
     *
     * @param entity The entity to clamp.
     */
    private static void clampPosition(Entity entity) {
        double clampedX = Math.max(GAME_BOUNDARY_LEFT,
                Math.min(entity.getPositionX(), GAME_BOUNDARY_RIGHT - entity.getWidth()));
        double clampedY = Math.max(GAME_BOUNDARY_TOP,
                Math.min(entity.getPositionY(), GAME_BOUNDARY_BOTTOM - entity.getHeight()));

        entity.setPositionX(clampedX);
        entity.setPositionY(clampedY);
    }

    /**
     * Calculates the horizontal overlap between two entities.
     *
     * @param a The first entity.
     * @param b The second entity.
     * @return The horizontal overlap distance.
     */
    private static double calculateHorizontalOverlap(Entity a, Entity b) {
        double leftOverlap = a.getPositionX() + a.getWidth() - b.getPositionX();
        double rightOverlap = b.getPositionX() + b.getWidth() - a.getPositionX();
        return leftOverlap < rightOverlap ? -leftOverlap : rightOverlap;
    }

    /**
     * Calculates the vertical overlap between two entities.
     *
     * @param a The first entity.
     * @param b The second entity.
     * @return The vertical overlap distance.
     */
    private static double calculateVerticalOverlap(Entity a, Entity b) {
        double topOverlap = a.getPositionY() + a.getHeight() - b.getPositionY();
        double bottomOverlap = b.getPositionY() + b.getHeight() - a.getPositionY();
        return topOverlap < bottomOverlap ? -topOverlap : bottomOverlap;
    }
}
