package rougelike.game;

import rougelike.game.entities.CollisionDetection;
import rougelike.game.entities.Enemy;
import rougelike.game.entities.Entity;
import rougelike.game.entities.GameElement;
import rougelike.game.entities.InteractionResult;
import static rougelike.game.GameConstants.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import rougelike.Global;
import rougelike.GuiState;
import rougelike.game.graphics.AnimationContainer;
import rougelike.game.graphics.Animatable;
import rougelike.game.graphics.ExplosionAnimation;
import rougelike.Model;
import rougelike.game.entities.Player;
import rougelike.game.entities.Projectile;
import rougelike.game.entities.Well;
import rougelike.game.graphics.ImageUtils;

public class Game {
    private GraphicsContext gc;
    private Model model;
    private GameModel gameModel;
    private Loader loader;
    private ArrayList<InteractionResult> interactionResults;
    private ExecutorService executorService;

    Canvas canvas;
    private AnimationTimer gameLoop;
    private boolean gameOver = false;
    private int level;
    private AnimationContainer<Animatable> animationContainer = new AnimationContainer<>();

    public Game(Model model) {
        this.model = model;
        this.gameModel = new GameModel();
        this.loader = new Loader(model, this.gameModel);
        this.interactionResults = new ArrayList<>();
        this.canvas = new Canvas(Global.WINDOW_HEIGHT, Global.WINDOW_WIDTH);
        this.gc = canvas.getGraphicsContext2D();
        this.level = STARTING_LEVEL;
        this.animationContainer = new AnimationContainer<>();

        initializeKeyBindings(canvas);
        initializeGameLoop();
    }

    private void initializeKeyBindings(Canvas canvas) {
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(evt -> {
            if (gameOver)
                return;

            switch (evt.getCode()) {
                case A -> gameModel.getPlayer().moveLeft();
                case D -> gameModel.getPlayer().moveRight();
                case W -> gameModel.getPlayer().moveUp();
                case S -> gameModel.getPlayer().moveDown();
                case SPACE -> gameModel.addEntity(gameModel.getPlayer().attack());
                default -> {  
                }
            }
        });

        canvas.setOnKeyReleased(evt -> {
            if (gameOver)
                return; 

            switch (evt.getCode()) {
                case A -> gameModel.getPlayer().stopMovingLeft();
                case D -> gameModel.getPlayer().stopMovingRight();
                case W -> gameModel.getPlayer().stopMovingUp();
                case S -> gameModel.getPlayer().stopMovingDown();
                default -> {
                }
            }
        });
    }

    private void initializeGameLoop() {
        gameLoop = new AnimationTimer() {
            long lastNanoTime = 0;
            boolean firstFrame = true;

            public void handle(long currentNanoTime) {
                if (gameOver) {
                    return; 
                }
                if (firstFrame) {
                    lastNanoTime = currentNanoTime;
                    firstFrame = false;
                }
                long timeElapsedMilli = (currentNanoTime - lastNanoTime) / NANOS_TO_MILLIS;
                lastNanoTime = currentNanoTime;

                updateMovement(timeElapsedMilli);
                processCollisions();
                updateGameState();
                renderGame(timeElapsedMilli);
            }
        };
    }

    private void updateMovement(long timeElapsedMilli) {
        List<Future<?>> movementFutures = new ArrayList<>();
        movementFutures.add(executorService.submit(() -> gameModel.getPlayer().move(timeElapsedMilli)));
        movementFutures.add(executorService.submit(() -> projectiles(timeElapsedMilli)));
        movementFutures.add(executorService.submit(() -> enemies(timeElapsedMilli)));
        movementFutures.add(executorService.submit(() -> wells(timeElapsedMilli)));

        // Wait for all movement updates to complete
        for (Future<?> future : movementFutures) {
            try {
                future.get(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(MOVEMENT_UPDATE_INTERRUPTED + ": " + e.getMessage());
            } catch (ExecutionException e) {
                System.err.println(MOVEMENT_UPDATE_FAILED + ": " + e.getCause());
            }
        }
    }

    private void processCollisions() {
        playerCollisionCheck();
        enemyCollisionCheck();
    }

    private void updateGameState() {
        processInteractionResults();
        removeDeadEntities();

        if (gameModel.getPlayer().isDead()) {
            stopGame();
        }
    }

    private void renderGame(long timeElapsedMilli) {
        renderFloor();
        renderEntities();
        renderStatusBar();
        animationContainer.renderAnimations(gc, timeElapsedMilli / MILLIS_TO_SECONDS);
    }

    public void startGame() {
        resetGame(); 
        if (executorService == null || executorService.isShutdown()) {
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
        initializeGameLoop();
        loader.loadDungeon(level);
        gameLoop.start(); // Start the game loop
    }

    private void resetGame() {
        gameOver = false; 
        level = STARTING_LEVEL; 
        gameModel = new GameModel(); 
        interactionResults.clear(); 
        loader = new Loader(model, gameModel); 
    }

        /**
     * Stop the game.
     * 
     * This method will stop the game loop and set the game over flag to true.
     */
    private void stopGame() {
        System.out.println("That's all folks!");
        gameLoop.stop();
        gameOver = true;
        model.activeMenuProperty().set(GuiState.LOSS);
    }

        /**
     * Method to be called when the player has won the game.
     * 
     * This method will stop the game loop and display a victory message.
     */
    private void winGame() {
        System.out.println("Congratulations! You have successfully exited the dungeon.");
        gameLoop.stop();
        gameOver = true;
        model.activeMenuProperty().set(GuiState.VICTORY);
        shutdownExecutorService();
    }



    /**
     * Move the projectiles in the game.
     * 
     * This method will move the projectiles in the game. The projectiles will move
     * in the direction they were fired.
     * 
     * @param timeElapsedMilli The time elapsed in milliseconds since the last
     *                         frame.
     */
    private void projectiles(long timeElapsedMilli) {
        List<Entity> entitySnapshot = createEntitySnapshot();
        for (Entity entity : entitySnapshot) {
            if (entity instanceof Projectile) {
                ((Projectile) entity).updatePosition(timeElapsedMilli);
            }
        }
    }

    /**
     * Move the enemies in the game.
     * 
     * This method will move the enemies in the game. The enemies will move towards
     * the player's position.
     * 
     * @param timeElapsedMilli The time elapsed in milliseconds since the last
     *                         frame.
     */
    private void enemies(long timeElapsedMilli) {
        List<Entity> entitySnapshot = createEntitySnapshot();
        for (Entity entity : entitySnapshot) {
            if (entity instanceof Enemy) {
                Enemy enemy = (Enemy) entity;
                enemy.move(timeElapsedMilli, gameModel.getPlayer().getPositionX(), gameModel.getPlayer().getPositionY());
                enemy.update(timeElapsedMilli / MILLIS_TO_SECONDS); 
            }
        }
    }

    private void wells(long timeElapsedMilli) {
        List<Entity> entitySnapshot = createEntitySnapshot();
        for (Entity entity : entitySnapshot) {
            if (entity instanceof Well) {
                ((Well) entity).update(timeElapsedMilli / MILLIS_TO_SECONDS); 
            }
        }
    }
    

    /**
     * Check for collisions between the player and other entities.
     * Check for collisions between the enemy and other entities.
     * 
     * This method will check for collisions between the player and other entities
     * in the game. If a collision is detected, the collision will be processed.
     */
    private void playerCollisionCheck() {
        // Create a snapshot of entities to avoid concurrent modification
        List<Entity> entitySnapshot = createEntitySnapshot();

        // Divide entities into chunks for parallel processing
        int numThreads = Math.min(MAX_COLLISION_THREADS, entitySnapshot.size()); 
        int chunkSize = Math.max(1, entitySnapshot.size() / numThreads);
        
        List<Future<List<InteractionResult>>> futures = new ArrayList<>();
        
        for (int i = 0; i < entitySnapshot.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, entitySnapshot.size());
            List<Entity> chunk = entitySnapshot.subList(i, end);
            
            futures.add(executorService.submit(() -> {
                List<InteractionResult> results = new ArrayList<>();
                for (Entity entity : chunk) {
                    if (!(entity instanceof GameElement)) {
                        continue; 
                    }

                    if (!(CollisionDetection.Aabb(gameModel.getPlayer(), entity))) {
                        continue; 
                    }

                    GameElement gameElement = (GameElement) entity;

                    InteractionResult result = gameElement.interact(gameModel.getPlayer());
                    if (result != null) {
                        results.add(result);
                    }

                    if (gameElement.isOccupying()) {
                        CollisionDetection.resolveCollision(gameModel.getPlayer(), gameElement);
                    }
                }
                return results;
            }));
        }

        // Collect results from all threads
        for (Future<List<InteractionResult>> future : futures) {
            try {
                List<InteractionResult> results = future.get();
                synchronized (interactionResults) {
                    interactionResults.addAll(results);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Check for collisions between the enemy and other entities.
     * 
     * This method will check for collisions between the enemy and other entities
     * in the game. If a collision is detected, the collision will be processed.
     */
    private void enemyCollisionCheck() {
        // Create snapshots to avoid concurrent modification
        List<Entity> entitySnapshot = createEntitySnapshot();
        List<Enemy> enemies = new ArrayList<>();
        
        synchronized (gameModel.getEntities()) {

            for (Entity entity : entitySnapshot) {
                if (entity instanceof Enemy) {
                    enemies.add((Enemy) entity);
                }
            }
        }

        if (enemies.isEmpty()) return;

        List<Future<List<InteractionResult>>> futures = new ArrayList<>();
        
        for (Enemy enemy : enemies) {
            futures.add(executorService.submit(() -> {
                List<InteractionResult> results = new ArrayList<>();
                
                for (Entity entity : entitySnapshot) {
                    if (entity == enemy) {
                        continue;
                    }

                    if (!(entity instanceof GameElement)) {
                        continue; 
                    }

                    if (!(CollisionDetection.Aabb(enemy, entity))) {
                        continue; 
                    }

                    GameElement gameElement = (GameElement) entity;

                    InteractionResult result = gameElement.interact(enemy);
                    if (result != null) {
                        results.add(result);
                    }

                    if (gameElement.isOccupying()) {
                        CollisionDetection.resolveCollision(enemy, gameElement);
                    }
                }
                return results;
            }));
        }

        // Collect results from all threads
        for (Future<List<InteractionResult>> future : futures) {
            try {
                List<InteractionResult> results = future.get();
                synchronized (interactionResults) {
                    interactionResults.addAll(results);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Process the results of interactions.
     * 
     * This method will process the results of interactions between the player and
     * other entities in the game.
     */
    private void processInteractionResults() {
        // Use an explicit iterator for safe removal
        Iterator<InteractionResult> iterator = interactionResults.iterator();
        while (iterator.hasNext()) {
            InteractionResult result = iterator.next();
            if (result != null) {
                if (result.getGameWon()) {
                    processWinResult();
                }
                if (result.getLevelUp()) {
                    if (processLevelUpResult(result, iterator)) {
                        return; // Exit early after reloading to avoid further processing
                    }
                }
                if (result.getHeal()) {
                    processHealResult(result);
                }
                if (result.getTakeDamage()) {
                    processDamageResult(result);
                }
            }
            iterator.remove(); 
        }
    }

    private void processWinResult() {
        winGame();
    }

    private boolean processLevelUpResult(InteractionResult result, Iterator<InteractionResult> iterator) {
        iterator.remove(); 
        level += LEVEL_INCREMENT;
        loader.loadDungeon(level); 
        return true; // Indicates early return needed
    }

    private void processHealResult(InteractionResult result) {
        Entity sourceEntity = result.getSource();
        if (sourceEntity instanceof Well) {
            Well well = (Well) sourceEntity;
            gameModel.getPlayer().heal(well.getHealAmount());
        }
    }

    private void processDamageResult(InteractionResult result) {
        if (result.getEntity() instanceof Player) {
            ((Player) result.getEntity()).takeDamage(DEFAULT_DAMAGE);
        }
        if (result.getEntity() instanceof Enemy) {
            Enemy enemy = (Enemy) result.getEntity();
            enemy.takeDamage(DEFAULT_DAMAGE);

            // If the enemy is dead, add an explosion animation
            if (enemy.isDead()) {
                createExplosionAnimation(enemy);
            }
        }
    }

    private void createExplosionAnimation(Enemy enemy) {
        Image explosionSpriteSheet = new Image(EXPLOSION_SPRITE_PATH);
        Image[] explosionFrames = ImageUtils.partitionImage(explosionSpriteSheet, EXPLOSION_FRAME_COUNT);
        double x = enemy.getPositionX();
        double y = enemy.getPositionY();
        double width = enemy.getWidth();
        double height = enemy.getHeight();
        animationContainer.addAnimation(
            new ExplosionAnimation(EXPLOSION_DURATION, explosionFrames, EXPLOSION_FRAME_DURATION, x, y, width, height));
    }

    private List<Entity> createEntitySnapshot() {
        synchronized (gameModel.getEntities()) {
            return new ArrayList<>(gameModel.getEntities());
        }
    }

    private void shutdownExecutorService() {
        executorService.shutdown();
    }

    /**
     * Remove dead entities from the game.
     * 
     * This method will remove dead entities from the game. Dead entities are
     * entities that have been marked as dead and should be removed from the game.
     */
    private void removeDeadEntities() {
        synchronized (gameModel.getEntities()) {
            Iterator<Entity> iterator = gameModel.getEntities().iterator();
            while (iterator.hasNext()) {
                Entity entity = iterator.next();
                if ((entity instanceof Enemy && ((Enemy) entity).isDead()) ||
                        (entity instanceof Player && ((Player) entity).isDead()) ||
                        (entity instanceof Projectile && ((Projectile) entity).shouldRemove())) {
                    iterator.remove();
                }
            }
        }
    }


    /**
     * Render the floor of the game.
     * 
     * This method will render the floor of the game, which is the background of the
     * game.
     */
    public void renderFloor() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        List<Entity> floorSnapshot;
        synchronized (gameModel.getFloorEntities()) {
            floorSnapshot = new ArrayList<>(gameModel.getFloorEntities());
        }
        for (Entity entity : floorSnapshot) {
            entity.render(gc);
        }
    }

    /**
     * Render all entities in the game.
     * 
     * This method will render all entities in the game, including the player.
     */
    public void renderEntities() {
        List<Entity> entitySnapshot;
        synchronized (gameModel.getEntities()) {
            entitySnapshot = new ArrayList<>(gameModel.getEntities());
        }
        for (Entity entity : entitySnapshot) {
            entity.render(gc);
        }
        gameModel.getPlayer().render(gc);
    }

    /**
     * Render the status bar at the bottom of the screen showing HP, AP, and Inventory.
     */
    private void renderStatusBar() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, Global.GAME_AREA_HEIGHT, Global.WINDOW_WIDTH, Global.STATUS_BAR_HEIGHT);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, STATUS_BAR_FONT_SIZE));
        
        Player player = gameModel.getPlayer();
        String hpText = "HP: " + player.getHealth();
        String apText = "AP: " + player.getPlayerDamage();
        String inventoryText = "Inventory: " + (player.getInventory().isEmpty() ? "" : String.join(", ", player.getInventory()));
        
        int textY = Global.GAME_AREA_HEIGHT + STATUS_BAR_TEXT_Y_OFFSET; 
        gc.fillText(hpText, HP_TEXT_X, textY);
        gc.fillText(apText, AP_TEXT_X, textY);
        gc.fillText(inventoryText, INVENTORY_TEXT_X, textY);
    }

    public Region build() {
        VBox layout = new VBox();
        layout.getChildren().addAll(canvas);
        return layout;
    }

    public Region getView() {
        return build();
    }
}