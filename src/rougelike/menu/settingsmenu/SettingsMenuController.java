package rougelike.menu.settingsmenu;

import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import rougelike.GuiState;
import rougelike.Model;

public class SettingsMenuController {
    private final Model rougelikeModel;
    private final SettingsMenuView view;

    public SettingsMenuController(Model rougelikeModel) {
        this.rougelikeModel = rougelikeModel;
        this.view = new SettingsMenuView(rougelikeModel, this::switchToMainMenu, this::changeBackground,
                this::changeDungeon);
    }

    private void changeDungeon(String string) {
        rougelikeModel.setSelectedDungeon(string);
    }

    private void changeBackground(Image image) {
        rougelikeModel.setBackgroundImage(image);
    }

    private void switchToMainMenu() {
        rougelikeModel.activeMenuProperty().set(GuiState.MAINMENU);
    }

    public Region getView() {
        return view.build();
    }
}
