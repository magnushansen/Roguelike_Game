package rougelike.game;

import javafx.scene.image.Image;
import rougelike.Global;
import rougelike.Model;
import rougelike.game.entities.Enemy;
import rougelike.game.entities.Entity;
import rougelike.game.entities.Exit;
import rougelike.game.entities.Floor;
import rougelike.game.entities.ImageDatabase;
import rougelike.game.entities.Ladder;
import rougelike.game.entities.Player;
import rougelike.game.entities.Projectile;
import rougelike.game.entities.Wall;
import rougelike.game.entities.Well;
import rougelike.game.dungeon.DungeonDatabase;

public class Loader {
    private final Model model;
    private final GameModel gameModel;

    public Loader(Model model, GameModel gameModel) {
        this.model = model;
        this.gameModel = gameModel;
    }

    private void calculateTileWidth(int columns) {
        gameModel.setTileWidth(Global.WINDOW_WIDTH / columns);
    }

    private void calculateTileHeight(int rows) {
        gameModel.setTileHeight(Global.WINDOW_HEIGHT / rows);
    }

    public void loadDungeon(int level) {
        // Store current player health before clearing
        Player currentPlayer = gameModel.getPlayer();
        int currentHealth = (currentPlayer != null) ? currentPlayer.getHealth() : 100;
        
        gameModel.clear();

        char[][][] dungeon = DungeonDatabase.getDungeonLayoutByName(model.getSelectedDungeon());

        if (dungeon == null || level < 0 || level >= dungeon.length || dungeon[level] == null || dungeon[level].length == 0) {
            System.err.println("Invalid level index or dungeon data: " + level);
            return;
        }

        calculateTileWidth(dungeon[level][0].length);
        calculateTileHeight(dungeon[level].length);

        for (int row = 0; row < dungeon[level].length; row++) {
            for (int col = 0; col < dungeon[level][row].length; col++) {
                char key = dungeon[level][row][col];
                double positionX = col * GameModel.getTileWidth();
                double positionY = row * GameModel.getTileHeight();

                gameModel.addFloorEntity(
                    loadEntity(' ', positionX, positionY, GameModel.getTileWidth(), GameModel.getTileHeight()));

                if (key == 'P') {
                    Player newPlayer = (Player) loadEntity(key, positionX, positionY, GameModel.getTileWidth(), GameModel.getTileHeight());
                    // Restore the previous health instead of starting with max health
                    if (currentPlayer != null) {
                        newPlayer.setHealth(currentHealth);
                    }
                    gameModel.setPlayer(newPlayer);
                    continue;
                }

                if (key != ' ') {
                    gameModel.addEntity(
                        loadEntity(key, positionX, positionY, GameModel.getTileWidth(), GameModel.getTileHeight()));
                }
            }
        }
    }

    private Entity loadEntity(char key, double positionX, double positionY, double tileWidth, double tileHeight) {
        Image image = ImageDatabase.getImage(key);
        Image[] idleFrames = ImageDatabase.getAnimationFrames('P');
        Image[] movingFrames = ImageDatabase.getAnimationFrames('R');
        Image[] animationFrames = ImageDatabase.getAnimationFrames(key);

        switch (key) {
            case ' ':
                return new Floor(positionX, positionY, tileWidth, tileHeight, image);
            case 'P':
                return new Player(positionX, positionY, tileWidth, tileHeight, image, idleFrames, movingFrames, 100, 10);
            case 'W':
                return new Wall(positionX, positionY, tileWidth, tileHeight, image);
            case 'E':
                if (animationFrames != null) {
                    return new Enemy(positionX, positionY, tileWidth, tileHeight, animationFrames, 50, 5, 0.05, 10000.0, 0.2);
                }
            case 'L':
                return new Ladder(positionX, positionY, tileWidth, tileHeight, image);
            case 'w':
                return new Well(positionX, positionY, tileWidth, tileHeight, animationFrames, 10, 0.2);         
            case 'p':
                return new Projectile(positionX, positionY, tileWidth, tileHeight, image, positionY, tileWidth,
                    tileHeight, 10);
            case 'e':
                return new Exit(positionX, positionY, tileWidth, tileHeight, image);
            default:
                throw new IllegalArgumentException("Invalid character in dungeon file: " + key);
        }
    }
}
