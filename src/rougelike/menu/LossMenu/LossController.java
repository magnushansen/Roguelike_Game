package rougelike.menu.LossMenu;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import rougelike.GuiState;
import rougelike.Model;

public class LossController {
    private final VBox view;

    public LossController(Model model) {
        this.view = new VBox(20);
        this.view.setAlignment(Pos.CENTER);

        this.view.backgroundProperty().bind(model.backgroundProperty());

        Label victoryLabel = new Label("You Lost!");
        victoryLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: red;");

        Button returnToMenuButton = new Button("Return to Main Menu");
        returnToMenuButton.setOnAction(e -> model.activeMenuProperty().set(GuiState.MAINMENU));

        Button quitButton = new Button("Quit Game");
        quitButton.setOnAction(e -> System.exit(0));

        this.view.getChildren().addAll(victoryLabel, returnToMenuButton, quitButton);
    }

    public Region getView() {
        return view;
    }
}