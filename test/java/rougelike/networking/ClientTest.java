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
    
    @Test
    @DisplayName("Should handle connection to non-existent server")
    void shouldHandleConnectionToNonExistentServer() {
        assertDoesNotThrow(() -> {
            client.connectClient("localhost", 9999);
            assertFalse(client.isConnected());
        });
    }
    
    @Test
    @DisplayName("Should handle disconnection gracefully")
    void shouldHandleDisconnectionGracefully() {
        assertDoesNotThrow(() -> {
            client.close();
            assertFalse(client.isConnected());
        });
    }
    
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
            client.downloadDungeon("Test Dungeon");
        });
    }
}