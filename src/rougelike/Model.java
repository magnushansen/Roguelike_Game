package rougelike;

import java.io.File;
import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import rougelike.game.dungeon.DungeonDatabase;
import rougelike.networking.Client;
import javafx.scene.image.Image;

public class Model {
    private final Client client = new Client();
    private final StringProperty selectedDungeon = new SimpleStringProperty("Dungeon 1");
    private final ObjectProperty<GuiState> activeMenuProperty = new SimpleObjectProperty<>(GuiState.MAINMENU);
    private final ObjectProperty<Background> backgroundProperty = new SimpleObjectProperty<>(
            new Background(new BackgroundImage(
                    new Image("file:assets/backgrounds/background4.png"),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(100, 100, true, true, false, true))));

    private final ObservableList<Image> availableBackgrounds = FXCollections.observableArrayList();

    @SuppressWarnings("unused")
    public Model() {
        File dir = new File("assets/backgrounds");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".png"));
        if (files != null) {
            Arrays.sort(files); // Sort files by name first
            for (File file : files) {
                availableBackgrounds.add(new Image(file.toURI().toString()));
            }
        }
    }

    public Client getClient() {
        return client;
    }

    private final ObservableList<String> availableDungeons = FXCollections.observableArrayList();
    {
        availableDungeons.addAll(DungeonDatabase.getAllDungeonNames());
    }

    public ObservableList<String> getAvailableDungeons() {
        System.out.println("Available dungeons: " + availableDungeons + " model main");
        return availableDungeons;
    }

    public void setSelectedDungeon(String dungeon) {
        selectedDungeon.set(dungeon);
    }

    public String getSelectedDungeon() {
        return selectedDungeon.get();
    }

    public ObjectProperty<GuiState> activeMenuProperty() {
        return activeMenuProperty;
    }

    public ObjectProperty<Background> backgroundProperty() {
        return backgroundProperty;
    }

    public ObservableList<Image> getAvailableBackgrounds() {
        return availableBackgrounds;
    }

    public void setBackgroundImage(Image image) {
        backgroundProperty.set(new Background(new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true))));
    }
}
