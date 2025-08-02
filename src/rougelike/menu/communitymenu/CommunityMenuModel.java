package rougelike.menu.communitymenu;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CommunityMenuModel {
    private ListProperty<String> dungeons;

    public CommunityMenuModel() {
        dungeons = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public ObservableList<String> getDungeons() {
        return dungeons;
    }

    public ListProperty<String> dungeonsProperty() {
        return dungeons;
    }
}
