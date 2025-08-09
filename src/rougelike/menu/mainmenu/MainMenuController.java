package rougelike.menu.mainmenu;

import javafx.application.Platform;
import javafx.scene.layout.Region;
import rougelike.GuiState;
import rougelike.Model;

public class MainMenuController {
    MainMenuView view;

    public MainMenuController(Model rougelikeModel, Runnable startGame) {
        this.view = new MainMenuView(
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