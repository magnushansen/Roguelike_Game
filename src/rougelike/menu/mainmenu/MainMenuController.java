package rougelike.menu.mainmenu;

import javafx.application.Platform;
import javafx.scene.layout.Region;
import rougelike.GuiState;
import rougelike.Model;

public class MainMenuController {
    MainMenuModel model;
    MainMenuView view;

    public MainMenuController(Model rougelikeModel, Runnable startGame) {
        this.model = new MainMenuModel();
        this.view = new MainMenuView(
                model,
                () -> {
                    rougelikeModel.activeMenuProperty().set(GuiState.GAME);
                    startGame.run();
                },
                () -> rougelikeModel.activeMenuProperty().set(GuiState.SETTINGSMENU),
                () -> rougelikeModel.activeMenuProperty().set(GuiState.LOGINMENU),
                Platform::exit);
    }

    public Region getView() {
        return view.build();
    }
}