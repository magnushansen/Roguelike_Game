package rougelike.menu.victorymenu;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import rougelike.GuiState;
import rougelike.Model;

public class VictoryController {
    private final VBox view;

    public VictoryController(Model model) {
        this.view = new VBox(20);
        this.view.setAlignment(Pos.CENTER);

        this.view.backgroundProperty().bind(model.backgroundProperty());

        Label victoryLabel = new Label("Congratulations");
        victoryLabel.setStyle("-fx-font-size: 44px; -fx-text-fill: gold; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 3, 0, 2, 2);");
        Label victoryLabel2 = new Label("You Won!");
        victoryLabel2.setStyle("-fx-font-size: 44px; -fx-text-fill: gold; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 3, 0, 2, 2);");
        
        Button returnToMenuButton = new Button("Return to Main Menu");
        returnToMenuButton.setOnAction(e -> model.activeMenuProperty().set(GuiState.MAINMENU));

        Button quitButton = new Button("Quit Game");
        quitButton.setOnAction(e -> System.exit(0));

        this.view.getChildren().addAll(victoryLabel, victoryLabel2, returnToMenuButton, quitButton);
    }

    public Region getView() {
        return view;
    }
}
