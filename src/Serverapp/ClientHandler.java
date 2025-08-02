package Serverapp;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.logging.Level;
import rougelike.game.dungeon.Dungeon;



public class ClientHandler implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
    private static final String GET_ALL_DUNGEONS = "GET_ALL_DUNGEONS";
    private static final String UPLOAD_DUNGEON = "UPLOAD_DUNGEON";
    private static final String DOWNLOAD_DUNGEON = "DOWNLOAD_DUNGEON";
    
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Runnable broadcastDungeonList;
    private final Runnable removeClientCallback;

    public ClientHandler(Socket socket, Runnable broadcastDungeonList, Runnable removeClientCallback) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.broadcastDungeonList = broadcastDungeonList;
        this.removeClientCallback = removeClientCallback;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object request = in.readObject();

                if (request instanceof String) {
                    String command = (String) request;
                    handleCommand(command);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.INFO, "Client disconnected: " + e.getMessage());
        } finally {
            close();
            if (removeClientCallback != null) {
                removeClientCallback.run();
            }
        }
    }

    private void handleCommand(String command) throws IOException, ClassNotFoundException {
        LOGGER.log(Level.INFO, "Received command: " + command);
        if (GET_ALL_DUNGEONS.equals(command)) {
            sendMessage(ServerDungeonDatabase.getDungeons().stream().map(Dungeon::getName).toArray(String[]::new));
        } else if (UPLOAD_DUNGEON.equals(command)) {
            Dungeon receivedDungeon = (Dungeon) in.readObject();
            LOGGER.log(Level.INFO, "Received dungeon from client: " + receivedDungeon.getName());
            
            if (ServerDungeonDatabase.getDungeonByName(receivedDungeon.getName()) == null) {
                ServerDungeonDatabase.addDungeon(receivedDungeon);
                LOGGER.log(Level.INFO, "Added dungeon to server database: " + receivedDungeon.getName());
                broadcastDungeonList.run();
            } else {
                LOGGER.log(Level.INFO, "Dungeon already exists on server: " + receivedDungeon.getName());
            }
        
        } else if (DOWNLOAD_DUNGEON.equals(command)) {
            String dungeonName = (String) in.readObject();
            LOGGER.log(Level.INFO, "Received dungeon name: " + dungeonName);
            Dungeon dungeon = ServerDungeonDatabase.getDungeonByName(dungeonName);

            if (dungeon != null) {
                LOGGER.log(Level.INFO, "Sending dungeon: " + dungeon.getName());
                sendMessage(dungeon);
            } else {
                LOGGER.log(Level.WARNING, "Dungeon not found: " + dungeonName);
                sendMessage("Dungeon not found");
            }
        } else {
            LOGGER.log(Level.WARNING, "Unknown command: " + command);
        }
    }

    public void sendMessage(Object message) throws IOException {
        out.writeObject(message);
        out.flush();
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
            LOGGER.log(Level.WARNING, "Error closing client handler: " + e.getMessage());
        }
    }
}