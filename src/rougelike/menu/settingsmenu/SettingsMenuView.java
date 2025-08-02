package rougelike.menu.settingsmenu;

import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import rougelike.Model;
import javafx.util.StringConverter;

public class SettingsMenuView {
    private final Model rougelikeModel;
    ComboBox<Image> backgroundComboBox;
    ComboBox<String> dungeonComboBox;
    Runnable menuButtonClicked;
    Consumer<Image> backgroundChanged;
    Consumer<String> dungeonChanged;

    public SettingsMenuView(Model rougelikeModel, Runnable menuButtonClicked, Consumer<Image> backgroundChanged,
            Consumer<String> dungeonChanged) {
        this.rougelikeModel = rougelikeModel;
        this.backgroundChanged = backgroundChanged;
        this.menuButtonClicked = menuButtonClicked;
        this.dungeonChanged = dungeonChanged;
    }

    @SuppressWarnings("unused")
    public ComboBox<String> createDungeonComboBox() {
        dungeonComboBox = new ComboBox<>(rougelikeModel.getAvailableDungeons());
        dungeonComboBox.setPromptText("Change Dungeon");
        dungeonComboBox.setOnAction(evt -> dungeonChanged.accept(dungeonComboBox.getValue()));
        return dungeonComboBox;
    }

    @SuppressWarnings("unused")
    public ComboBox<Image> createBackgroundComboBox() {
        backgroundComboBox = new ComboBox<>(rougelikeModel.getAvailableBackgrounds());
        backgroundComboBox.setPromptText("Change Background");
        backgroundComboBox.setConverter(new StringConverter<Image>() {
            @Override
            public String toString(Image image) {
                // Custom display value for background
                String url = image.getUrl();
                return url.substring(url.lastIndexOf('/') + 1);
            }

            @Override
            public Image fromString(String string) {
                // This method is not used in this context
                return null;
            }
        });
        backgroundComboBox.setOnAction(evt -> backgroundChanged.accept(backgroundComboBox.getValue()));
        return backgroundComboBox;
    }

    @SuppressWarnings("unused")
    public Button createButton() {
        Button backButton = new Button("Main Menu");
        backButton.setOnAction(evt -> menuButtonClicked.run());
        return backButton;
    }

    private VBox layout() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(createBackgroundComboBox(), createDungeonComboBox(), createButton());
        return layout;
    }

    public Region build() {
        return layout();
    }
}
