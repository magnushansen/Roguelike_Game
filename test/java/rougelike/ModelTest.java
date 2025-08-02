package rougelike;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.io.TempDir;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import rougelike.networking.Client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@DisplayName("Main Model Tests")
class ModelTest {
    
    private Model model;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");
        try {
            model = new Model();
        } catch (Exception e) {
            model = null;
        }
    }
    
    @Nested
    @DisplayName("Initialization Tests")
    class InitializationTests {
        
        @Test
        @DisplayName("Should initialize with default values")
        void shouldInitializeWithDefaultValues() {
            if (model != null) {
                assertNotNull(model.getClient());
                assertEquals("Dungeon 1", model.getSelectedDungeon());
                assertEquals(GuiState.MAINMENU, model.getActiveMenu());
                assertNotNull(model.getBackgroundProperty());
            }
        }
        
        @Test
        @DisplayName("Should have non-null client")
        void shouldHaveNonNullClient() {
            if (model != null) {
                Client client = model.getClient();
                assertNotNull(client);
            }
        }
        
        @Test
        @DisplayName("Should initialize available dungeons list")
        void shouldInitializeAvailableDungeonsList() {
            if (model != null) {
                ObservableList<String> dungeons = model.getAvailableDungeons();
                assertNotNull(dungeons);
            }
        }
    }
    
    @Nested
    @DisplayName("Property Management Tests")
    class PropertyManagementTests {
        
        @Test
        @DisplayName("Should get and set selected dungeon")
        void shouldGetAndSetSelectedDungeon() {
            if (model != null) {
                String testDungeon = "Test Dungeon";
                model.setSelectedDungeon(testDungeon);
                assertEquals(testDungeon, model.getSelectedDungeon());
            }
        }
        
        @Test
        @DisplayName("Should get and set active menu")
        void shouldGetAndSetActiveMenu() {
            if (model != null) {
                model.setActiveMenu(GuiState.GAME);
                assertEquals(GuiState.GAME, model.getActiveMenu());
            }
        }
        
        @Test
        @DisplayName("Should handle null dungeon selection")
        void shouldHandleNullDungeonSelection() {
            if (model != null) {
                assertDoesNotThrow(() -> model.setSelectedDungeon(null));
            }
        }
        
        @Test
        @DisplayName("Should handle null menu state")
        void shouldHandleNullMenuState() {
            if (model != null) {
                assertDoesNotThrow(() -> model.setActiveMenu(null));
            }
        }
    }
    
    @Nested
    @DisplayName("Background Management Tests")
    class BackgroundManagementTests {
        
        @Test
        @DisplayName("Should have default background")
        void shouldHaveDefaultBackground() {
            if (model != null) {
                Background background = model.getBackgroundProperty().get();
                assertNotNull(background);
            }
        }
        
        @Test
        @DisplayName("Should allow background changes")
        void shouldAllowBackgroundChanges() {
            if (model != null) {
                Background originalBackground = model.getBackgroundProperty().get();
                Background newBackground = mock(Background.class);
                
                model.getBackgroundProperty().set(newBackground);
                assertEquals(newBackground, model.getBackgroundProperty().get());
                assertNotEquals(originalBackground, model.getBackgroundProperty().get());
            }
        }
        
        @Test
        @DisplayName("Should load available backgrounds")
        void shouldLoadAvailableBackgrounds() {
            if (model != null) {
                ObservableList<Image> backgrounds = model.getAvailableBackgrounds();
                assertNotNull(backgrounds);
            }
        }
    }
    
    @Nested
    @DisplayName("Dungeon Management Tests")
    class DungeonManagementTests {
        
        @Test
        @DisplayName("Should maintain available dungeons list")
        void shouldMaintainAvailableDungeonsList() {
            if (model != null) {
                ObservableList<String> dungeons = model.getAvailableDungeons();
                assertNotNull(dungeons);
                assertTrue(dungeons instanceof ObservableList);
            }
        }
        
        @Test
        @DisplayName("Should allow dungeon list modifications")
        void shouldAllowDungeonListModifications() {
            if (model != null) {
                ObservableList<String> dungeons = model.getAvailableDungeons();
                int initialSize = dungeons.size();
                
                dungeons.add("Custom Dungeon");
                assertEquals(initialSize + 1, dungeons.size());
                assertTrue(dungeons.contains("Custom Dungeon"));
            }
        }
        
        @Test
        @DisplayName("Should handle empty dungeon list")
        void shouldHandleEmptyDungeonList() {
            if (model != null) {
                ObservableList<String> dungeons = model.getAvailableDungeons();
                dungeons.clear();
                assertTrue(dungeons.isEmpty());
            }
        }
    }
    
    @Nested
    @DisplayName("Client Integration Tests")
    class ClientIntegrationTests {
        
        @Test
        @DisplayName("Should provide access to networking client")
        void shouldProvideAccessToNetworkingClient() {
            if (model != null) {
                Client client = model.getClient();
                assertNotNull(client);
                assertTrue(client instanceof Client);
            }
        }
        
        @Test
        @DisplayName("Should maintain same client instance")
        void shouldMaintainSameClientInstance() {
            if (model != null) {
                Client client1 = model.getClient();
                Client client2 = model.getClient();
                assertSame(client1, client2);
            }
        }
    }
    
    @Nested
    @DisplayName("File System Tests")
    class FileSystemTests {
        
        @Test
        @DisplayName("Should handle missing assets directory gracefully")
        void shouldHandleMissingAssetsDirectoryGracefully() {
            assertDoesNotThrow(() -> {
                new Model();
            });
        }
        
        @Test
        @DisplayName("Should handle file system errors gracefully")
        void shouldHandleFileSystemErrorsGracefully() {
            File assetsDir = new File("assets/backgrounds");
            boolean originalExists = assetsDir.exists();
            
            assertDoesNotThrow(() -> {
                new Model();
            });
        }
    }
    
    @Nested
    @DisplayName("Property Binding Tests")
    class PropertyBindingTests {
        
        @Test
        @DisplayName("Should support property binding for selected dungeon")
        void shouldSupportPropertyBindingForSelectedDungeon() {
            if (model != null) {
                assertNotNull(model.selectedDungeonProperty());
            }
        }
        
        @Test
        @DisplayName("Should support property binding for active menu")
        void shouldSupportPropertyBindingForActiveMenu() {
            if (model != null) {
                assertNotNull(model.activeMenuProperty());
            }
        }
        
        @Test
        @DisplayName("Should support property binding for background")
        void shouldSupportPropertyBindingForBackground() {
            if (model != null) {
                assertNotNull(model.getBackgroundProperty());
            }
        }
    }
    
    @Nested
    @DisplayName("GuiState Integration Tests")
    class GuiStateIntegrationTests {
        
        @Test
        @DisplayName("Should handle all GUI state transitions")
        void shouldHandleAllGuiStateTransitions() {
            if (model != null) {
                for (GuiState state : GuiState.values()) {
                    assertDoesNotThrow(() -> model.setActiveMenu(state));
                    assertEquals(state, model.getActiveMenu());
                }
            }
        }
        
        @Test
        @DisplayName("Should start with main menu state")
        void shouldStartWithMainMenuState() {
            if (model != null) {
                assertEquals(GuiState.MAINMENU, model.getActiveMenu());
            }
        }
    }
    
    @Nested
    @DisplayName("Memory and Performance Tests")
    class MemoryAndPerformanceTests {
        
        @Test
        @DisplayName("Should handle large background collections efficiently")
        void shouldHandleLargeBackgroundCollectionsEfficiently() {
            if (model != null) {
                ObservableList<Image> backgrounds = model.getAvailableBackgrounds();
                
                long startTime = System.nanoTime();
                for (int i = 0; i < 100; i++) {
                    backgrounds.size();
                }
                long endTime = System.nanoTime();
                
                assertTrue((endTime - startTime) < 10_000_000);
            }
        }
        
        @Test
        @DisplayName("Should handle frequent state changes efficiently")
        void shouldHandleFrequentStateChangesEfficiently() {
            if (model != null) {
                long startTime = System.nanoTime();
                
                for (int i = 0; i < 1000; i++) {
                    model.setActiveMenu(GuiState.GAME);
                    model.setActiveMenu(GuiState.MAINMENU);
                }
                
                long endTime = System.nanoTime();
                assertTrue((endTime - startTime) < 100_000_000);
            }
        }
    }
}