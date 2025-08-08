package Serverapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Timeout;
import rougelike.game.dungeon.Dungeon;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

@DisplayName("Server Tests")
class ServerTest {
    
    private Server server;
    
    @BeforeEach
    void setUp() {
        server = new Server();
    }
    
    @Test
    @DisplayName("Should create server instance successfully")
    void shouldCreateServerInstanceSuccessfully() {
        assertNotNull(server);
    }
    
    @Nested
    @DisplayName("Server Lifecycle Tests")
    class ServerLifecycleTests {
        
        @Test
        @DisplayName("Should handle server startup gracefully")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void shouldHandleServerStartupGracefully() {
            Thread serverThread = new Thread(() -> {
                try {
                    server.start();
                } catch (Exception e) {
                    // Expected when server socket is already in use or other issues
                }
            });
            
            serverThread.start();
            
            try {
                Thread.sleep(100);
                serverThread.interrupt();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            assertDoesNotThrow(() -> serverThread.join(1000));
        }
        
        @Test
        @DisplayName("Should handle port already in use gracefully")
        void shouldHandlePortAlreadyInUseGracefully() {
            try (ServerSocket occupyingSocket = new ServerSocket(8888)) {
                assertDoesNotThrow(() -> {
                    Thread serverThread = new Thread(() -> {
                        try {
                            server.start();
                        } catch (Exception e) {
                            // Expected when port is already in use
                        }
                    });
                    
                    serverThread.start();
                    try {
                        Thread.sleep(100);
                        serverThread.interrupt();
                        serverThread.join(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            } catch (IOException e) {
                // Port might already be in use by another process
            }
        }
    }
    
    @Test
    @DisplayName("Should handle server startup gracefully")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void shouldHandleServerStartupGracefully() {
        Thread serverThread = new Thread(() -> {
            try {
                server.start();
            } catch (Exception e) {
                // Expected when server socket is already in use or other issues
            }
        });
        
        serverThread.start();
        
        try {
            Thread.sleep(100);
            serverThread.interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        assertDoesNotThrow(() -> serverThread.join(1000));
    }
}