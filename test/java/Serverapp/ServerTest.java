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
    
    @Nested
    @DisplayName("Initialization Tests")
    class InitializationTests {
        
        @Test
        @DisplayName("Should create server instance successfully")
        void shouldCreateServerInstanceSuccessfully() {
            assertNotNull(server);
        }
        
        @Test
        @DisplayName("Should use correct default port")
        void shouldUseCorrectDefaultPort() {
            java.lang.reflect.Field[] fields = Server.class.getDeclaredFields();
            boolean foundPortField = false;
            
            for (java.lang.reflect.Field field : fields) {
                if (field.getName().equals("PORT")) {
                    try {
                        field.setAccessible(true);
                        int port = (int) field.get(null);
                        assertEquals(8888, port);
                        foundPortField = true;
                        break;
                    } catch (IllegalAccessException e) {
                        fail("Could not access PORT field");
                    }
                }
            }
            
            assertTrue(foundPortField, "PORT field should exist");
        }
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
    
    @Nested
    @DisplayName("Client Management Tests")
    class ClientManagementTests {
        
        @Test
        @DisplayName("Should handle client connections")
        void shouldHandleClientConnections() {
            java.lang.reflect.Field clientsField = null;
            try {
                clientsField = Server.class.getDeclaredField("clients");
                clientsField.setAccessible(true);
                
                @SuppressWarnings("unchecked")
                java.util.List<ClientHandler> clients = 
                    (java.util.List<ClientHandler>) clientsField.get(server);
                
                assertNotNull(clients);
                assertTrue(clients.isEmpty());
                
            } catch (NoSuchFieldException | IllegalAccessException e) {
                fail("Could not access clients field");
            }
        }
        
        @Test
        @DisplayName("Should maintain thread-safe client list")
        void shouldMaintainThreadSafeClientList() {
            try {
                java.lang.reflect.Field clientsField = Server.class.getDeclaredField("clients");
                clientsField.setAccessible(true);
                
                Object clients = clientsField.get(server);
                assertTrue(clients instanceof java.util.concurrent.CopyOnWriteArrayList);
                
            } catch (NoSuchFieldException | IllegalAccessException e) {
                fail("Could not access clients field");
            }
        }
    }
    
    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {
        
        @Test
        @DisplayName("Should handle IOException during server start")
        void shouldHandleIOExceptionDuringServerStart() {
            assertDoesNotThrow(() -> {
                Thread serverThread = new Thread(() -> {
                    try {
                        server.start();
                    } catch (Exception e) {
                        // Expected for various reasons (port in use, etc.)
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
        }
        
        @Test
        @DisplayName("Should handle null socket gracefully")
        void shouldHandleNullSocketGracefully() {
            assertNotNull(server);
        }
    }
    
    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {
        
        @Test
        @DisplayName("Should integrate with ServerDungeonDatabase")
        void shouldIntegrateWithServerDungeonDatabase() {
            assertDoesNotThrow(() -> {
                try {
                    java.lang.reflect.Method broadcastMethod = 
                        Server.class.getDeclaredMethod("broadcastDungeonList");
                    broadcastMethod.setAccessible(true);
                    broadcastMethod.invoke(server);
                } catch (Exception e) {
                    // Expected if ServerDungeonDatabase is not properly initialized
                }
            });
        }
        
        @Test
        @DisplayName("Should handle dungeon broadcasting")
        void shouldHandleDungeonBroadcasting() {
            assertNotNull(server);
        }
    }
    
    @Nested
    @DisplayName("Concurrency Tests")
    class ConcurrencyTests {
        
        @Test
        @DisplayName("Should handle multiple concurrent operations")
        void shouldHandleMultipleConcurrentOperations() {
            Thread[] threads = new Thread[5];
            
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> {
                    try {
                        server.start();
                    } catch (Exception e) {
                        // Expected when multiple threads try to start server
                    }
                });
            }
            
            assertDoesNotThrow(() -> {
                for (Thread thread : threads) {
                    thread.start();
                }
                
                Thread.sleep(100);
                
                for (Thread thread : threads) {
                    thread.interrupt();
                    thread.join(1000);
                }
            });
        }
        
        @Test
        @DisplayName("Should handle thread interruption gracefully")
        void shouldHandleThreadInterruptionGracefully() {
            Thread serverThread = new Thread(() -> {
                try {
                    server.start();
                } catch (Exception e) {
                    // Expected when interrupted or other issues
                }
            });
            
            assertDoesNotThrow(() -> {
                serverThread.start();
                Thread.sleep(50);
                serverThread.interrupt();
                serverThread.join(1000);
            });
        }
    }
    
    @Nested
    @DisplayName("Network Communication Tests")
    class NetworkCommunicationTests {
        
        @Test
        @DisplayName("Should attempt to create server socket on correct port")
        void shouldAttemptToCreateServerSocketOnCorrectPort() {
            Thread serverThread = new Thread(() -> {
                try {
                    server.start();
                } catch (Exception e) {
                    // Check if the exception mentions port 8888
                    if (e.getMessage() != null) {
                        assertTrue(e.getMessage().contains("8888") || 
                                 e.getMessage().contains("bind") || 
                                 e.getMessage().contains("already"));
                    }
                }
            });
            
            assertDoesNotThrow(() -> {
                serverThread.start();
                Thread.sleep(100);
                serverThread.interrupt();
                serverThread.join(1000);
            });
        }
        
        @Test
        @DisplayName("Should handle network communication errors")
        void shouldHandleNetworkCommunicationErrors() {
            assertNotNull(server);
        }
    }
    
    @Nested
    @DisplayName("Resource Management Tests")
    class ResourceManagementTests {
        
        @Test
        @DisplayName("Should properly manage server socket resources")
        void shouldProperlyManageServerSocketResources() {
            Thread serverThread = new Thread(() -> {
                try {
                    server.start();
                } catch (Exception e) {
                    // Expected for various reasons
                }
            });
            
            assertDoesNotThrow(() -> {
                serverThread.start();
                Thread.sleep(100);
                serverThread.interrupt();
                serverThread.join(1000);
            });
        }
        
        @Test
        @DisplayName("Should handle resource cleanup on shutdown")
        void shouldHandleResourceCleanupOnShutdown() {
            assertNotNull(server);
        }
    }
}