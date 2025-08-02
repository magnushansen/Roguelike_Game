package rougelike.game.entities;

import java.util.concurrent.Callable;

public class CollisionTask implements Callable<Boolean> {
    private final Entity a;
    private final Entity b;

    public CollisionTask(Entity a, Entity b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Boolean call() {
        return CollisionDetection.Aabb(a, b);
    }
}
