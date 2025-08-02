package rougelike.networking;

import rougelike.game.dungeon.Dungeon;
import javafx.application.Platform;
import rougelike.game.dungeon.DungeonDatabase;
import rougelike.menu.communitymenu.CommunityMenuModel;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private CommunityMenuModel communityMenuModel;
    private volatile boolean connected = false;

    public Client() {

    }

    public void connectClient(String address, int port) {
        try {
            socket = new Socket(address, port);
            connected = true;
            System.out.println("Connected to the server");

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            new Thread(new MessageReceiver(in, this::handleReceivedDungeonNames, this::handleReceivedDungeonLayout))
                    .start();

        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            connected = false;
        }
    }

    public void setCommunityMenuModel(CommunityMenuModel communityMenuModel) {
        this.communityMenuModel = communityMenuModel;
    }

    public boolean isConnected() {
        return connected;
    }

    public void sendDungeon(String dungeonName) {
        if (!connected) {
            System.err.println("Cannot send dungeon. Not connected to server.");
            return;
        }
        
        Dungeon dungeonToSend = DungeonDatabase.getDungeonByName(dungeonName);
        if (dungeonToSend == null) {
            System.err.println("Dungeon not found in local database: " + dungeonName);
            return;
        }
        
        try {
            out.writeObject("UPLOAD_DUNGEON");
            out.writeObject(dungeonToSend);
            out.flush();
            System.out.println("Dungeon sent: " + dungeonName);
        } catch (IOException e) {
            System.err.println("Error sending dungeon: " + e.getMessage());
        }
    }

    public boolean getLoginStatus() {
        return connected;
    }

    public void close() {
        try {
            if (socket != null)
                socket.close();
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        } catch (IOException e) {
            System.err.println("Error closing client resources: " + e.getMessage());
        } finally {
            connected = false;
        }
    }

    private void handleReceivedDungeonNames(String[] dungeonNames) {
        System.out.println("Received dungeon list from server:");
        for (String name : dungeonNames) {
            System.out.println("- " + name);
            Platform.runLater(() -> {
                if (!communityMenuModel.dungeonsProperty().contains(name)) {
                    communityMenuModel.dungeonsProperty().add(name);
                }
            });
        }
    }

    public void handleReceivedDungeonLayout(Dungeon downloadedDungeon) {
        System.out.println("Received dungeon layout from server: " + downloadedDungeon.getName());
        Platform.runLater(() -> {

            System.out.println("Test");
            DungeonDatabase.getDungeons().add(downloadedDungeon);
            if (!communityMenuModel.getDungeons().contains(downloadedDungeon.getName())) {
                communityMenuModel.getDungeons().add(downloadedDungeon.getName());

            }

            System.out.println("Dungeon added to database: " + downloadedDungeon.getName());
        });
    }

    public void downloadDungeon(String dungeonName) {
        try {
            out.writeObject("DOWNLOAD_DUNGEON");
            out.writeObject(dungeonName);
            out.flush();
            System.out.println("Download request sent for dungeon: " + dungeonName);
        } catch (IOException e) {
            System.err.println("Error sending download request: " + e.getMessage());
        }
    }

}