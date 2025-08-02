package rougelike.menu.communitymenu;


import javafx.scene.layout.Region;
import rougelike.GuiState;
import rougelike.Model;


public class CommunityMenuController {
    private final CommunityMenuModel model;
    private final CommunityMenuView view;
    private final Model roguelikeModel;

    public CommunityMenuController(Model roguelikeModel) {
        this.roguelikeModel = roguelikeModel;
        this.model = new CommunityMenuModel();
        roguelikeModel.getClient().setCommunityMenuModel(model);
        this.view = new CommunityMenuView(
                model,
                () -> roguelikeModel.activeMenuProperty().set(GuiState.MAINMENU),
                this::downloadFunction,
                this::uploadFunction);
    }

    public CommunityMenuModel getModel() {
        return model;
    }

    public void downloadFunction(String dungeonName) {
        if (dungeonName == null) {
            return;
        }
        System.out.println("Downloading dungeon: " + dungeonName);
        roguelikeModel.getClient().downloadDungeon(dungeonName);
        if (!roguelikeModel.getAvailableDungeons().contains(dungeonName)) {
            roguelikeModel.getAvailableDungeons().add(dungeonName);
        }
        System.out.println("Downloaded dungeon: " + dungeonName);
    }

    public void uploadFunction(String dungeonName) {
        if (dungeonName == null || dungeonName.trim().isEmpty()) {
            System.err.println("Dungeon name cannot be null or empty.");
            return;
        }
    

    
        System.out.println("Uploading dungeon: " + dungeonName);
        roguelikeModel.getClient().sendDungeon(dungeonName); // Send dungeon to server
    }
    

    public Region getView() {
        return view.build();
    }
}
