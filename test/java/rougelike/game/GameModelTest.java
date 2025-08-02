package rougelike.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import rougelike.game.entities.Entity;
import rougelike.game.entities.Player;

@DisplayName("Game Model Tests")
class GameModelTest {
    
    private GameModel gameModel;
    private Entity mockEntity;
    private Player mockPlayer;
    private Image mockImage;
    
    @BeforeEach
    void setUp() {
        gameModel = new GameModel();
        mockImage = mock(Image.class);
        mockEntity = createMockEntity(100, 100, 32, 32);
        mockPlayer = mock(Player.class);
    }
    
    private Entity createMockEntity(double x, double y, double width, double height) {
        return new Entity(x, y, width, height, mockImage) {
            @Override
            public void render(javafx.scene.canvas.GraphicsContext gc) {
                // Mock implementation
            }
        };
    }
    
    @Nested
    @DisplayName("Entity Management Tests")
    class EntityManagementTests {
        
        @Test
        @DisplayName("Should initialize with empty entity collections")
        void shouldInitializeWithEmptyEntityCollections() {
            assertTrue(gameModel.getEntities().isEmpty());
            assertTrue(gameModel.getFloorEntities().isEmpty());
        }
        
        @Test
        @DisplayName("Should add entity to entities collection")
        void shouldAddEntityToEntitiesCollection() {
            gameModel.addEntity(mockEntity);
            
            assertEquals(1, gameModel.getEntities().size());
            assertTrue(gameModel.getEntities().contains(mockEntity));
        }
        
        @Test
        @DisplayName("Should add entity to floor entities collection")
        void shouldAddEntityToFloorEntitiesCollection() {
            gameModel.addFloorEntity(mockEntity);
            
            assertEquals(1, gameModel.getFloorEntities().size());
            assertTrue(gameModel.getFloorEntities().contains(mockEntity));
        }
        
        @Test
        @DisplayName("Should handle multiple entities")
        void shouldHandleMultipleEntities() {
            Entity entity1 = createMockEntity(100, 100, 32, 32);
            Entity entity2 = createMockEntity(200, 200, 32, 32);
            Entity entity3 = createMockEntity(300, 300, 32, 32);
            
            gameModel.addEntity(entity1);
            gameModel.addEntity(entity2);
            gameModel.addFloorEntity(entity3);
            
            assertEquals(2, gameModel.getEntities().size());
            assertEquals(1, gameModel.getFloorEntities().size());
        }
        
        @Test
        @DisplayName("Should return observable lists")
        void shouldReturnObservableLists() {
            ObservableList<Entity> entities = gameModel.getEntities();
            ObservableList<Entity> floorEntities = gameModel.getFloorEntities();
            
            assertNotNull(entities);
            assertNotNull(floorEntities);
            assertTrue(entities instanceof ObservableList);
            assertTrue(floorEntities instanceof ObservableList);
        }
        
        @Test
        @DisplayName("Should handle null entities gracefully")
        void shouldHandleNullEntitiesGracefully() {
            assertDoesNotThrow(() -> {
                gameModel.addEntity(null);
                gameModel.addFloorEntity(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Player Management Tests")
    class PlayerManagementTests {
        
        @Test
        @DisplayName("Should initialize with null player")
        void shouldInitializeWithNullPlayer() {
            assertNull(gameModel.getPlayer());
        }
        
        @Test
        @DisplayName("Should set and get player correctly")
        void shouldSetAndGetPlayerCorrectly() {
            gameModel.setPlayer(mockPlayer);
            
            assertEquals(mockPlayer, gameModel.getPlayer());
        }
        
        @Test
        @DisplayName("Should handle player replacement")
        void shouldHandlePlayerReplacement() {
            Player firstPlayer = mock(Player.class);
            Player secondPlayer = mock(Player.class);
            
            gameModel.setPlayer(firstPlayer);
            assertEquals(firstPlayer, gameModel.getPlayer());
            
            gameModel.setPlayer(secondPlayer);
            assertEquals(secondPlayer, gameModel.getPlayer());
        }
        
        @Test
        @DisplayName("Should handle null player assignment")
        void shouldHandleNullPlayerAssignment() {
            gameModel.setPlayer(mockPlayer);
            gameModel.setPlayer(null);
            
            assertNull(gameModel.getPlayer());
        }
    }
    
    @Nested
    @DisplayName("Tile Dimension Tests")
    class TileDimensionTests {
        
        @Test
        @DisplayName("Should set and get tile width correctly")
        void shouldSetAndGetTileWidthCorrectly() {
            double testWidth = 32.0;
            gameModel.setTileWidth(testWidth);
            
            assertEquals(testWidth, GameModel.getTileWidth());
        }
        
        @Test
        @DisplayName("Should set and get tile height correctly")
        void shouldSetAndGetTileHeightCorrectly() {
            double testHeight = 32.0;
            gameModel.setTileHeight(testHeight);
            
            assertEquals(testHeight, GameModel.getTileHeight());
        }
        
        @Test
        @DisplayName("Should handle different tile dimensions")
        void shouldHandleDifferentTileDimensions() {
            gameModel.setTileWidth(16.0);
            gameModel.setTileHeight(24.0);
            
            assertEquals(16.0, GameModel.getTileWidth());
            assertEquals(24.0, GameModel.getTileHeight());
        }
        
        @Test
        @DisplayName("Should handle zero tile dimensions")
        void shouldHandleZeroTileDimensions() {
            gameModel.setTileWidth(0.0);
            gameModel.setTileHeight(0.0);
            
            assertEquals(0.0, GameModel.getTileWidth());
            assertEquals(0.0, GameModel.getTileHeight());
        }
        
        @Test
        @DisplayName("Should handle negative tile dimensions")
        void shouldHandleNegativeTileDimensions() {
            gameModel.setTileWidth(-10.0);
            gameModel.setTileHeight(-15.0);
            
            assertEquals(-10.0, GameModel.getTileWidth());
            assertEquals(-15.0, GameModel.getTileHeight());
        }
        
        @Test
        @DisplayName("Should maintain static tile dimensions across instances")
        void shouldMaintainStaticTileDimensionsAcrossInstances() {
            GameModel gameModel1 = new GameModel();
            GameModel gameModel2 = new GameModel();
            
            gameModel1.setTileWidth(50.0);
            gameModel2.setTileHeight(60.0);
            
            assertEquals(50.0, GameModel.getTileWidth());
            assertEquals(60.0, GameModel.getTileHeight());
        }
    }
    
    @Nested
    @DisplayName("Collection Behavior Tests")
    class CollectionBehaviorTests {
        
        @Test
        @DisplayName("Should maintain entity order in collections")
        void shouldMaintainEntityOrderInCollections() {
            Entity entity1 = createMockEntity(100, 100, 32, 32);
            Entity entity2 = createMockEntity(200, 200, 32, 32);
            Entity entity3 = createMockEntity(300, 300, 32, 32);
            
            gameModel.addEntity(entity1);
            gameModel.addEntity(entity2);
            gameModel.addEntity(entity3);
            
            ObservableList<Entity> entities = gameModel.getEntities();
            assertEquals(entity1, entities.get(0));
            assertEquals(entity2, entities.get(1));
            assertEquals(entity3, entities.get(2));
        }
        
        @Test
        @DisplayName("Should allow duplicate entities")
        void shouldAllowDuplicateEntities() {
            gameModel.addEntity(mockEntity);
            gameModel.addEntity(mockEntity);
            
            assertEquals(2, gameModel.getEntities().size());
        }
        
        @Test
        @DisplayName("Should support collection modifications")
        void shouldSupportCollectionModifications() {
            gameModel.addEntity(mockEntity);
            ObservableList<Entity> entities = gameModel.getEntities();
            
            entities.remove(mockEntity);
            assertTrue(entities.isEmpty());
        }
    }
    
    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {
        
        @Test
        @DisplayName("Should handle concurrent entity additions")
        void shouldHandleConcurrentEntityAdditions() {
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    Entity entity = createMockEntity(i, i, 32, 32);
                    gameModel.addEntity(entity);
                }
            });
            
            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    Entity entity = createMockEntity(i + 100, i + 100, 32, 32);
                    gameModel.addFloorEntity(entity);
                }
            });
            
            assertDoesNotThrow(() -> {
                thread1.start();
                thread2.start();
                thread1.join();
                thread2.join();
            });
            
            assertEquals(100, gameModel.getEntities().size());
            assertEquals(100, gameModel.getFloorEntities().size());
        }
    }
    
    @Nested
    @DisplayName("Memory Management Tests")
    class MemoryManagementTests {
        
        @Test
        @DisplayName("Should handle large number of entities")
        void shouldHandleLargeNumberOfEntities() {
            final int LARGE_NUMBER = 1000;
            
            for (int i = 0; i < LARGE_NUMBER; i++) {
                Entity entity = createMockEntity(i, i, 32, 32);
                gameModel.addEntity(entity);
            }
            
            assertEquals(LARGE_NUMBER, gameModel.getEntities().size());
        }
        
        @Test
        @DisplayName("Should allow entity collection clearing")
        void shouldAllowEntityCollectionClearing() {
            gameModel.addEntity(mockEntity);
            gameModel.addFloorEntity(mockEntity);
            
            gameModel.getEntities().clear();
            gameModel.getFloorEntities().clear();
            
            assertTrue(gameModel.getEntities().isEmpty());
            assertTrue(gameModel.getFloorEntities().isEmpty());
        }
    }
}