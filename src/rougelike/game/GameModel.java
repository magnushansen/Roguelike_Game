package rougelike.game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rougelike.game.entities.Entity;
import rougelike.game.entities.Player;

public class GameModel {
    private final ObservableList<Entity> floorEntities = FXCollections.observableArrayList();
    private final ObservableList<Entity> entities = FXCollections.observableArrayList();
    private Player player;
    private static double tileHeight;
    private static double tileWidth;

    public ObservableList<Entity> getFloorEntities() {
        return floorEntities;
    }

    public ObservableList<Entity> getEntities() {
        return entities;
    }

    public static double getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(double tileHeight) {
        GameModel.tileHeight = tileHeight;
    }

    public static double getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(double tileWidth) {
        GameModel.tileWidth = tileWidth;
    }

    public Player getPlayer() {
        return player;
    }


    public void addFloorEntity(Entity entity) {
        floorEntities.add(entity);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void setPlayer(Entity player) {
        this.player = (Player) player;
    }

    public void clear() {
        floorEntities.clear();
        entities.clear();
    }
}
