package rougelike.menu.communitymenu;

import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

// the GUI builder
public class CommunityMenuView {
    private final CommunityMenuModel model;
    private final Runnable showMainMenu;
    Consumer<String> downloadobject;
    Consumer<String> uploadobject;
    ListView<String> listView;
    TextField dungeonTextfield;

    public CommunityMenuView(CommunityMenuModel model, Runnable showMainMenu, Consumer<String> downloadobject,
            Consumer<String> uploadobject) {
        this.model = model;
        this.showMainMenu = showMainMenu;
        this.downloadobject = downloadobject;
        this.uploadobject = uploadobject;

    }

    @SuppressWarnings("unused")
    public VBox createButton() {
        VBox buttons = new VBox();
        HBox btn_tf = new HBox();
        Button downloadButton = new Button("Download selected dungeon");
        downloadButton.setOnAction(evt -> downloadobject.accept(listView.getSelectionModel().getSelectedItem()));
        downloadButton.setAlignment(Pos.CENTER);
        dungeonTextfield = createTextFields();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(evt -> uploadobject.accept(dungeonTextfield.getText()));

        btn_tf.getChildren().addAll(submitButton, dungeonTextfield);
        btn_tf.setSpacing(10);

        Button menuButton = new Button("Main menu");
        menuButton.setOnAction(e -> {
            showMainMenu.run();
        });

        buttons.getChildren().addAll(downloadButton, btn_tf, menuButton);
        buttons.setSpacing(10);
        buttons.setPadding(new Insets(10, 0, 0, 55));
        return buttons;

    }

    public Label createLabel() {
        Label label = new Label("Available dungeons");

        label.setStyle("-fx-font-size: 20px;");
        label.setFont(new Font("Arial", 20));
        label.setPadding(new Insets(10, 0, 0, 55));
        label.setTextFill(Color.WHITE);

        return label;

    }

    public TextField createTextFields() {
        TextField dungeonTextfield = new TextField();
        dungeonTextfield.setPromptText("Enter Dungeon Name");
        dungeonTextfield.setPrefWidth(150);
        dungeonTextfield.setMaxWidth(250);
        return dungeonTextfield;
    }

    public StackPane createListView() {

        listView = new ListView<>();
        listView.itemsProperty().bind(model.dungeonsProperty());
        listView.setPrefWidth(300);
        listView.setPrefHeight(300);
        listView.setMaxWidth(400);

        StackPane listViewContainer = new StackPane(listView);

        listViewContainer.setAlignment(Pos.CENTER);

        return listViewContainer;
    }

    public Region build() {

        StackPane listView = createListView();

        VBox layout = new VBox();
        layout.getChildren().addAll(
                createLabel(),
                listView,
                createButton());

        return layout;
    }

}
