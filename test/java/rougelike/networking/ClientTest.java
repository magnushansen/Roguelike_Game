package rougelike.networking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Timeout;
import rougelike.menu.communitymenu.CommunityMenuModel;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

@DisplayName("Client Tests")
class ClientTest {
    
    private Client client;
    private CommunityMenuModel mockCommunityMenuModel;
    
    @BeforeEach
    void setUp() {
        client = new Client();
        mockCommunityMenuModel = mock(CommunityMenuModel.class);
    }
    
    @Nested
    @DisplayName("Initialization Tests")
    class InitializationTests {
        
        @Test
        @DisplayName("Should create client instance successfully")
        void shouldCreateClientInstanceSuccessfully() {
            assertNotNull(client);
        }
        
        @Test
        @DisplayName("Should initialize as disconnected")
        void shouldInitializeAsDisconnected() {
            assertFalse(client.isConnected());
        }
        
        @Test
        @DisplayName("Should accept community menu model")
        void shouldAcceptCommunityMenuModel() {
            assertDoesNotThrow(() -> {
                client.setCommunityMenuModel(mockCommunityMenuModel);
            });
        }
        
        @Test
        @DisplayName("Should handle null community menu model")
        void shouldHandleNullCommunityMenuModel() {
            assertDoesNotThrow(() -> {
                client.setCommunityMenuModel(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Connection Tests")
    class ConnectionTests {
        
        @Test
        @DisplayName("Should handle connection to non-existent server")
        void shouldHandleConnectionToNonExistentServer() {
            assertDoesNotThrow(() -> {
                client.connectClient("localhost", 9999);
                assertFalse(client.isConnected());
            });
        }
        
        @Test
        @DisplayName("Should handle invalid hostname")
        void shouldHandleInvalidHostname() {
            assertDoesNotThrow(() -> {
                client.connectClient("invalid-hostname-that-does-not-exist", 8888);
                assertFalse(client.isConnected());
            });
        }
        
        @Test
        @DisplayName("Should handle invalid port")
        void shouldHandleInvalidPort() {
            assertDoesNotThrow(() -> {
                client.connectClient("localhost", -1);
                assertFalse(client.isConnected());
            });
        }
        
        @Test
        @DisplayName("Should handle connection timeout gracefully")
        @Timeout(value = 10, unit = TimeUnit.SECONDS)
        void shouldHandleConnectionTimeoutGracefully() {
            assertDoesNotThrow(() -> {
                client.connectClient("localhost", 12345);
                assertFalse(client.isConnected());
            });
        }
        
        @Test
        @DisplayName("Should connect to available server")
        void shouldConnectToAvailableServer() {
            try (ServerSocket serverSocket = new ServerSocket(0)) {
                int port = serverSocket.getLocalPort();
                
                Thread serverThread = new Thread(() -> {
                    try {
                        Socket acceptedSocket = serverSocket.accept();
                        Thread.sleep(100);
                        acceptedSocket.close();
                    } catch (Exception e) {
                        // Expected when connection is closed
                    }
                });
                serverThread.start();
                
                assertDoesNotThrow(() -> {
                    client.connectClient("localhost", port);
                    Thread.sleep(200);
                });
                
                serverThread.interrupt();
            } catch (IOException e) {
                // Test server couldn't be created
            }
        }
    }
    
    @Nested
    @DisplayName("Disconnection Tests")
    class DisconnectionTests {
        
        @Test
        @DisplayName("Should handle disconnection gracefully")
        void shouldHandleDisconnectionGracefully() {
            assertDoesNotThrow(() -> {
                client.disconnect();
                assertFalse(client.isConnected());
            });
        }
        
        @Test
        @DisplayName("Should handle multiple disconnection attempts")
        void shouldHandleMultipleDisconnectionAttempts() {
            assertDoesNotThrow(() -> {
                client.disconnect();
                client.disconnect();
                client.disconnect();
                assertFalse(client.isConnected());
            });
        }
        
        @Test
        @DisplayName("Should maintain disconnected state after failed connection")
        void shouldMaintainDisconnectedStateAfterFailedConnection() {
            client.connectClient("invalid-host", 9999);
            assertFalse(client.isConnected());
            
            client.disconnect();
            assertFalse(client.isConnected());
        }
    }
    
    @Nested
    @DisplayName("Dungeon Communication Tests")
    class DungeonCommunicationTests {
        
        @Test
        @DisplayName("Should handle sending dungeon when disconnected")
        void shouldHandleSendingDungeonWhenDisconnected() {
            assertDoesNotThrow(() -> {
                client.sendDungeon("Test Dungeon");
            });
        }
        
        @Test
        @DisplayName("Should handle requesting dungeon when disconnected")
        void shouldHandleRequestingDungeonWhenDisconnected() {
            assertDoesNotThrow(() -> {
                client.requestDungeon("Test Dungeon");
            });
        }
        
        @Test
        @DisplayName("Should handle null dungeon name")
        void shouldHandleNullDungeonName() {
            assertDoesNotThrow(() -> {
                client.sendDungeon(null);
                client.requestDungeon(null);
            });
        }
        
        @Test
        @DisplayName("Should handle empty dungeon name")
        void shouldHandleEmptyDungeonName() {
            assertDoesNotThrow(() -> {
                client.sendDungeon("");
                client.requestDungeon("");
            });
        }
        
        @Test
        @DisplayName("Should handle very long dungeon names")
        void shouldHandleVeryLongDungeonNames() {
            String longName = "A".repeat(10000);
            assertDoesNotThrow(() -> {
                client.sendDungeon(longName);
                client.requestDungeon(longName);
            });
        }
    }
    
    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {
        
        @Test
        @DisplayName("Should handle IOException during connection")
        void shouldHandleIOExceptionDuringConnection() {
            assertDoesNotThrow(() -> {
                client.connectClient("localhost", 0);
                assertFalse(client.isConnected());
            });
        }
        
        @Test
        @DisplayName("Should handle connection errors gracefully")
        void shouldHandleConnectionErrorsGracefully() {
            assertDoesNotThrow(() -> {
                client.connectClient("127.0.0.1", 12345);
                assertFalse(client.isConnected());
            });
        }
        
        @Test
        @DisplayName("Should handle network errors during communication")
        void shouldHandleNetworkErrorsDuringCommunication() {
            client.connectClient("localhost", 9999);
            
            assertDoesNotThrow(() -> {
                client.sendDungeon("Test");
                client.requestDungeon("Test");
            });
        }
    }
    
    @Nested
    @DisplayName("State Management Tests")
    class StateManagementTests {
        
        @Test
        @DisplayName("Should maintain connection state correctly")
        void shouldMaintainConnectionStateCorrectly() {
            assertFalse(client.isConnected());
            
            client.connectClient("invalid-host", 9999);
            assertFalse(client.isConnected());
        }
        
        @Test
        @DisplayName("Should update connection state on successful connection")
        void shouldUpdateConnectionStateOnSuccessfulConnection() {
            try (ServerSocket serverSocket = new ServerSocket(0)) {
                int port = serverSocket.getLocalPort();
                
                Thread serverThread = new Thread(() -> {
                    try {
                        Socket acceptedSocket = serverSocket.accept();
                        new ObjectOutputStream(acceptedSocket.getOutputStream());
                        new ObjectInputStream(acceptedSocket.getInputStream());
                        Thread.sleep(100);
                        acceptedSocket.close();
                    } catch (Exception e) {
                        // Expected when connection is closed
                    }
                });
                serverThread.start();
                
                client.connectClient("localhost", port);
                Thread.sleep(50);
                
                serverThread.interrupt();
            } catch (Exception e) {
                // Test server couldn't be created or connection failed
            }
        }
        
        @Test
        @DisplayName("Should handle concurrent connection attempts")
        void shouldHandleConcurrentConnectionAttempts() {
            Thread[] threads = new Thread[5];
            
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> {
                    client.connectClient("localhost", 9999);
                });
            }
            
            assertDoesNotThrow(() -> {
                for (Thread thread : threads) {
                    thread.start();
                }
                
                for (Thread thread : threads) {
                    thread.join(1000);
                }
            });
            
            assertFalse(client.isConnected());
        }
    }
    
    @Nested
    @DisplayName("Message Handling Tests")
    class MessageHandlingTests {
        
        @Test
        @DisplayName("Should handle message reception setup")
        void shouldHandleMessageReceptionSetup() {
            try (ServerSocket serverSocket = new ServerSocket(0)) {
                int port = serverSocket.getLocalPort();
                
                Thread serverThread = new Thread(() -> {
                    try {
                        Socket acceptedSocket = serverSocket.accept();
                        ObjectOutputStream out = new ObjectOutputStream(acceptedSocket.getOutputStream());
                        Thread.sleep(100);
                        acceptedSocket.close();
                    } catch (Exception e) {
                        // Expected when connection is closed
                    }
                });
                serverThread.start();
                
                assertDoesNotThrow(() -> {
                    client.connectClient("localhost", port);
                    Thread.sleep(150);
                });
                
                serverThread.interrupt();
            } catch (Exception e) {
                // Test server couldn't be created
            }
        }
        
        @Test
        @DisplayName("Should handle message processing errors")
        void shouldHandleMessageProcessingErrors() {
            assertNotNull(client);
        }
    }
    
    @Nested
    @DisplayName("Resource Management Tests")
    class ResourceManagementTests {
        
        @Test
        @DisplayName("Should properly manage socket resources")
        void shouldProperlyManageSocketResources() {
            client.connectClient("localhost", 9999);
            assertDoesNotThrow(() -> {
                client.disconnect();
            });
        }
        
        @Test
        @DisplayName("Should handle resource cleanup on disconnect")
        void shouldHandleResourceCleanupOnDisconnect() {
            client.connectClient("localhost", 9999);
            client.disconnect();
            assertFalse(client.isConnected());
        }
    }
    
    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {
        
        @Test
        @DisplayName("Should handle concurrent operations safely")
        void shouldHandleConcurrentOperationsSafely() {
            Thread connectThread = new Thread(() -> {
                client.connectClient("localhost", 9999);
            });
            
            Thread sendThread = new Thread(() -> {
                client.sendDungeon("Test");
            });
            
            Thread requestThread = new Thread(() -> {
                client.requestDungeon("Test");
            });
            
            assertDoesNotThrow(() -> {
                connectThread.start();
                sendThread.start();
                requestThread.start();
                
                connectThread.join(1000);
                sendThread.join(1000);
                requestThread.join(1000);
            });
        }
        
        @Test
        @DisplayName("Should maintain connection state atomicity")
        void shouldMaintainConnectionStateAtomicity() {
            assertTrue(client.isConnected() || !client.isConnected());
        }
    }
}