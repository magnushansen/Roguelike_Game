package rougelike;

import javafx.scene.layout.Region;
import rougelike.game.Game;
import rougelike.menu.LossMenu.LossController;
import rougelike.menu.communitymenu.CommunityMenuController;
import rougelike.menu.loginmenu.LoginMenuController;
import rougelike.menu.mainmenu.MainMenuController;
import rougelike.menu.settingsmenu.SettingsMenuController;
import rougelike.menu.victorymenu.VictoryController;

public class Controller {
    Model model;
    View view;
    Game game;

    public Controller() {
        this.model = new Model();
        this.game = new Game(model);
        this.view = new View(
                model,
                game.getView(),
                new MainMenuController(model, game::startGame).getView(),
                new LoginMenuController(model).getView(),
                new CommunityMenuController(model).getView(),
                new SettingsMenuController(model).getView(),
                new VictoryController(model).getView(),
                new LossController(model).getView());
    }

    public Region getView() {
        return view.build();
    }
}
