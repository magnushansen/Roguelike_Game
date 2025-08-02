package rougelike.menu.loginmenu;

import javafx.scene.layout.Region;
import rougelike.GuiState;
import rougelike.Model;

public class LoginMenuController {
    private final Model roguelikeModel;

    private LoginMenuModel model;
    private LoginMenuView view;

    public LoginMenuController(Model rougelikeModel) {
        this.roguelikeModel = rougelikeModel;
        this.model = new LoginMenuModel();
        this.view = new LoginMenuView(
                model,
                () -> rougelikeModel.activeMenuProperty().set(GuiState.MAINMENU),
                this::attemptLogin,
                () -> rougelikeModel.activeMenuProperty().set(GuiState.LOGINMENU));

    }

    public void loginFunction(String address, int port) {
        roguelikeModel.getClient().connectClient(address, port);

        if (roguelikeModel.getClient().getLoginStatus() == false) {
            view.showFailedLogin();
        }
        if (roguelikeModel.getClient().getLoginStatus() == true) {
            loginSucessfull(() -> {
                roguelikeModel.activeMenuProperty().set(GuiState.COMMUNITYMENU);
            });
        }

        // loginSucessfull(null);

    }

    public void loginSucessfull(Runnable action) {

        action.run();

    }

    public void attemptLogin(String address, Integer port) {

        loginFunction(address, port);

    }

    public Region getView() {
        return view.build();
    }

}
