package rougelike;

import javafx.beans.binding.Bindings;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class View {
        private Model model;
        private Region mainMenu;
        private Region game;
        private Region communityMenu;
        private Region settingsMenu;
        private Region VICT;
        private Region LOSS;
        private Region loginMenu;

        // Region game,
        public View(Model model,
                        Region game, Region mainMenu, Region loginMenu, Region communityMenu, Region settingsMenu,
                        Region VICT, Region LOSS) {
                this.model = model;
                this.game = game;
                this.mainMenu = mainMenu;
                this.communityMenu = communityMenu;
                this.loginMenu = loginMenu;
                this.settingsMenu = settingsMenu;
                this.VICT = VICT;
                this.LOSS = LOSS;

        }

        public Region build() {
                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(game, mainMenu, communityMenu, settingsMenu, VICT, loginMenu, LOSS);

                game.visibleProperty().bind(Bindings.createBooleanBinding(
                                () -> model.activeMenuProperty().get() == GuiState.GAME,
                                model.activeMenuProperty()));
                mainMenu.visibleProperty().bind(Bindings.createBooleanBinding(
                                () -> model.activeMenuProperty().get() == GuiState.MAINMENU,
                                model.activeMenuProperty()));
                communityMenu.visibleProperty().bind(Bindings.createBooleanBinding(
                                () -> model.activeMenuProperty().get() == GuiState.COMMUNITYMENU,
                                model.activeMenuProperty()));
                VICT.visibleProperty().bind(Bindings.createBooleanBinding(
                                () -> model.activeMenuProperty().get() == GuiState.VICTORY,
                                model.activeMenuProperty()));
                LOSS.visibleProperty().bind(Bindings.createBooleanBinding(
                                () -> model.activeMenuProperty().get() == GuiState.LOSS,
                                model.activeMenuProperty()));
                settingsMenu.visibleProperty().bind(Bindings.createBooleanBinding(
                                () -> model.activeMenuProperty().get() == GuiState.SETTINGSMENU,
                                model.activeMenuProperty()));
                loginMenu.visibleProperty().bind(Bindings.createBooleanBinding(
                                () -> model.activeMenuProperty().get() == GuiState.LOGINMENU,
                                model.activeMenuProperty()));
                stackPane.backgroundProperty().bind(model.backgroundProperty());
                return stackPane;
        }

}
