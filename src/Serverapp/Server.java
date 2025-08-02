package Serverapp;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import rougelike.game.dungeon.Dungeon;

public class Server {
    private static final int PORT = 8888;
    @SuppressWarnings("unused")
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            this.serverSocket = serverSocket;
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(
                        socket,
                        this::broadcastDungeonList);
                clients.add(clientHandler);
                new Thread(clientHandler).start();

                broadcastDungeonList();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private void broadcastDungeonList() {
        List<Dungeon> dungeons = ServerDungeonDatabase.getDungeons();
        for (ClientHandler client : clients) {
            try {
                client.sendMessage(dungeons.stream().map(Dungeon::getName).toArray(String[]::new));
            } catch (IOException e) {
                System.err.println("Failed to send dungeon list to a client: " + e.getMessage());
            }
        }
    }
}