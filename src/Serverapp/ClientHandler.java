package Serverapp;

import java.io.*;
import java.net.Socket;
import rougelike.game.dungeon.Dungeon;



public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Runnable broadcastDungeonList;

    public ClientHandler(Socket socket, Runnable broadcastDungeonList) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.broadcastDungeonList = broadcastDungeonList;
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
            System.err.println("Client disconnected: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void handleCommand(String command) throws IOException, ClassNotFoundException {
        System.out.println("Received command: " + command);
        if ("GET_ALL_DUNGEONS".equals(command)) {
            sendMessage(ServerDungeonDatabase.getDungeons().stream().map(Dungeon::getName).toArray(String[]::new));
        } else if ("UPLOAD_DUNGEON".equals(command)) {
            Dungeon receivedDungeon = (Dungeon) in.readObject();
            System.out.println("Received dungeon from client: " + receivedDungeon.getName());
            
            if (ServerDungeonDatabase.getDungeonByName(receivedDungeon.getName()) == null) {
                ServerDungeonDatabase.getDungeons().add(receivedDungeon);
                System.out.println("Added dungeon to server database: " + receivedDungeon.getName());
                broadcastDungeonList.run();
            } else {
                System.out.println("Dungeon already exists on server: " + receivedDungeon.getName());
            }
        
        } else if ("DOWNLOAD_DUNGEON".equals(command)) {
            String dungeonName = (String) in.readObject();
            System.out.println("Received dungeon name: " + dungeonName);
            Dungeon dungeon = ServerDungeonDatabase.getDungeonByName(dungeonName);

            if (dungeon != null) {
                System.out.println("Sending dungeon: " + dungeon.getName());
                sendMessage(dungeon);
            } else {
                System.out.println("Dungeon not found: " + dungeonName);
                sendMessage("Dungeon not found");
            }
        } else {
            System.out.println("Unknown command: " + command);
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
            System.err.println("Error closing client handler: " + e.getMessage());
        }
    }
}