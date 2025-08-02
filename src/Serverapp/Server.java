package Serverapp;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.logging.Level;
import rougelike.game.dungeon.Dungeon;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static final int PORT = 8888;
    private static final int THREAD_POOL_SIZE = 10;
    
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private volatile boolean running = true;

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            LOGGER.log(Level.INFO, "Server is listening on port " + PORT);

            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    Socket socket = serverSocket.accept();
                    LOGGER.log(Level.INFO, "New client connected: " + socket.getInetAddress());
                    
                    ClientHandler clientHandler = new ClientHandler(
                            socket,
                            this::broadcastDungeonList,
                            null);
                    clients.add(clientHandler);
                    executorService.submit(clientHandler);

                    broadcastDungeonList();
                } catch (IOException e) {
                    if (running) {
                        LOGGER.log(Level.WARNING, "Error accepting client connection: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server error: " + e.getMessage());
        } finally {
            shutdown();
        }
    }

    private void broadcastDungeonList() {
        List<Dungeon> dungeons = ServerDungeonDatabase.getDungeons();
        String[] dungeonNames = dungeons.stream().map(Dungeon::getName).toArray(String[]::new);
        
        Iterator<ClientHandler> iterator = clients.iterator();
        while (iterator.hasNext()) {
            ClientHandler client = iterator.next();
            try {
                client.sendMessage(dungeonNames);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Failed to send dungeon list to a client: " + e.getMessage());
                iterator.remove();
            }
        }
    }
    
    
    public void shutdown() {
        running = false;
        
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error closing server socket: " + e.getMessage());
        }
        
        executorService.shutdown();
        
        for (ClientHandler client : clients) {
            client.close();
        }
        clients.clear();
        
        LOGGER.log(Level.INFO, "Server shutdown complete");
    }
}