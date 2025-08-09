package rougelike.menu.mainmenu;

import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class MainMenuView {
    Runnable startGame;
    Runnable showCommunity;
    Runnable showSettings;
    Runnable showLoginScreen;
    Runnable exit;

    public MainMenuView(
            Runnable startGame,
            Runnable showSettings,
            Runnable showLoginScreen,
            Runnable exit) {
        this.startGame = startGame;
        this.showSettings = showSettings;
        this.showLoginScreen = showLoginScreen;
        this.exit = exit;
    }



    @SuppressWarnings("unused")
    public Button createButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setOnAction(e -> action.run());
        return button;
    }

    public Region build() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                createButton("Start Game", startGame),
                createButton("Community", showLoginScreen),
                createButton("Settings", showSettings),
                createButton("Exit", exit));

        return layout;
    }
}
