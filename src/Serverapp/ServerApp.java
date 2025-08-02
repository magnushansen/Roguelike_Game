package Serverapp;

import java.util.logging.Logger;
import java.util.logging.Level;

public class ServerApp {
    private static final Logger LOGGER = Logger.getLogger(ServerApp.class.getName());
    
    public static void main(String[] args) {
        Server server = new Server();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.log(Level.INFO, "Shutdown signal received, stopping server...");
            server.shutdown();
        }));
        
        try {
            server.start();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Server failed to start: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}
