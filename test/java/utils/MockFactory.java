package utils;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import rougelike.game.entities.*;
import rougelike.game.GameModel;
import rougelike.Model;
import rougelike.networking.Client;
import rougelike.menu.communitymenu.CommunityMenuModel;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeAll;

public class MockFactory {
    
    static {
        setupHeadless();
    }
    
    @BeforeAll
    static void setupHeadless() {
        // Set headless system properties BEFORE any JavaFX initialization
        System.setProperty("java.awt.headless", "true");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.verbose", "false");
        System.setProperty("javafx.macosx.embedded", "true");
        System.setProperty("prism.allowhidpi", "false");
        System.setProperty("quantum.multithreaded", "false");
        
        // Set test mode flag
        System.setProperty("test.mode", "true");
        
        // Initialize JavaFX platform in headless mode
        try {
            // Try to start JavaFX Platform if not already running
            if (!javafx.application.Platform.isFxApplicationThread()) {
                javafx.application.Platform.startup(() -> {});
            }
        } catch (Exception e) {
            // Platform might already be initialized or not available, that's OK
            System.err.println("JavaFX Platform initialization failed: " + e.getMessage());
        }
    }
    
    public static Image createMockImage(double width, double height) {
        Image mockImage = mock(Image.class);
        when(mockImage.getWidth()).thenReturn(width);
        when(mockImage.getHeight()).thenReturn(height);
        return mockImage;
    }
    
    public static Image createMockImage() {
        return createMockImage(32.0, 32.0);
    }
    
    public static Image[] createMockImageArray(int count) {
        Image[] images = new Image[count];
        for (int i = 0; i < count; i++) {
            images[i] = createMockImage();
        }
        return images;
    }
    
    public static GraphicsContext createMockGraphicsContext() {
        return mock(GraphicsContext.class);
    }
    
    public static Player createMockPlayer() {
        Player mockPlayer = mock(Player.class);
        
        when(mockPlayer.getPositionX()).thenReturn(100.0);
        when(mockPlayer.getPositionY()).thenReturn(100.0);
        when(mockPlayer.getWidth()).thenReturn(32.0);
        when(mockPlayer.getHeight()).thenReturn(32.0);
        when(mockPlayer.getPlayerDamage()).thenReturn(25);
        when(mockPlayer.isDead()).thenReturn(false);
        
        Projectile mockProjectile = createMockProjectile();
        when(mockPlayer.attack()).thenReturn(mockProjectile);
        
        return mockPlayer;
    }
    
    public static Player createMockPlayer(double x, double y, int health, int damage) {
        Player mockPlayer = createMockPlayer();
        when(mockPlayer.getPositionX()).thenReturn(x);
        when(mockPlayer.getPositionY()).thenReturn(y);
        when(mockPlayer.getPlayerDamage()).thenReturn(damage);
        when(mockPlayer.isDead()).thenReturn(health <= 0);
        return mockPlayer;
    }
    
    public static Enemy createMockEnemy() {
        Enemy mockEnemy = mock(Enemy.class);
        
        when(mockEnemy.getPositionX()).thenReturn(200.0);
        when(mockEnemy.getPositionY()).thenReturn(200.0);
        when(mockEnemy.getWidth()).thenReturn(32.0);
        when(mockEnemy.getHeight()).thenReturn(32.0);
        when(mockEnemy.getEnemyDamage()).thenReturn(15);
        when(mockEnemy.getEnemyHealth()).thenReturn(50);
        when(mockEnemy.getdetectionRadius()).thenReturn(100.0);
        when(mockEnemy.isDead()).thenReturn(false);
        when(mockEnemy.isOccupying()).thenReturn(true);
        
        return mockEnemy;
    }
    
    public static Enemy createMockEnemy(double x, double y, int health, int damage) {
        Enemy mockEnemy = createMockEnemy();
        when(mockEnemy.getPositionX()).thenReturn(x);
        when(mockEnemy.getPositionY()).thenReturn(y);
        when(mockEnemy.getEnemyHealth()).thenReturn(health);
        when(mockEnemy.getEnemyDamage()).thenReturn(damage);
        when(mockEnemy.isDead()).thenReturn(health <= 0);
        return mockEnemy;
    }
    
    public static Projectile createMockProjectile() {
        Projectile mockProjectile = mock(Projectile.class);
        
        when(mockProjectile.getPositionX()).thenReturn(150.0);
        when(mockProjectile.getPositionY()).thenReturn(150.0);
        when(mockProjectile.getWidth()).thenReturn(8.0);
        when(mockProjectile.getHeight()).thenReturn(8.0);
        when(mockProjectile.isOccupying()).thenReturn(false);
        
        return mockProjectile;
    }
    
    public static Projectile createMockProjectile(double x, double y, double directionX, double directionY) {
        Projectile mockProjectile = createMockProjectile();
        when(mockProjectile.getPositionX()).thenReturn(x);
        when(mockProjectile.getPositionY()).thenReturn(y);
        return mockProjectile;
    }
    
    public static Wall createMockWall() {
        Wall mockWall = mock(Wall.class);
        
        when(mockWall.getPositionX()).thenReturn(0.0);
        when(mockWall.getPositionY()).thenReturn(0.0);
        when(mockWall.getWidth()).thenReturn(32.0);
        when(mockWall.getHeight()).thenReturn(32.0);
        when(mockWall.isOccupying()).thenReturn(true);
        
        return mockWall;
    }
    
    public static Floor createMockFloor() {
        Floor mockFloor = mock(Floor.class);
        
        when(mockFloor.getPositionX()).thenReturn(32.0);
        when(mockFloor.getPositionY()).thenReturn(32.0);
        when(mockFloor.getWidth()).thenReturn(32.0);
        when(mockFloor.getHeight()).thenReturn(32.0);
        when(mockFloor.isOccupying()).thenReturn(false);
        
        return mockFloor;
    }
    
    public static Exit createMockExit() {
        Exit mockExit = mock(Exit.class);
        
        when(mockExit.getPositionX()).thenReturn(64.0);
        when(mockExit.getPositionY()).thenReturn(64.0);
        when(mockExit.getWidth()).thenReturn(32.0);
        when(mockExit.getHeight()).thenReturn(32.0);
        when(mockExit.isOccupying()).thenReturn(false);
        
        return mockExit;
    }
    
    public static Ladder createMockLadder() {
        Ladder mockLadder = mock(Ladder.class);
        
        when(mockLadder.getPositionX()).thenReturn(96.0);
        when(mockLadder.getPositionY()).thenReturn(96.0);
        when(mockLadder.getWidth()).thenReturn(32.0);
        when(mockLadder.getHeight()).thenReturn(32.0);
        when(mockLadder.isOccupying()).thenReturn(false);
        
        return mockLadder;
    }
    
    public static Well createMockWell() {
        Well mockWell = mock(Well.class);
        
        when(mockWell.getPositionX()).thenReturn(128.0);
        when(mockWell.getPositionY()).thenReturn(128.0);
        when(mockWell.getWidth()).thenReturn(32.0);
        when(mockWell.getHeight()).thenReturn(32.0);
        when(mockWell.isOccupying()).thenReturn(false);
        
        return mockWell;
    }
    
    public static GameModel createMockGameModel() {
        GameModel mockGameModel = mock(GameModel.class);
        
        javafx.collections.ObservableList<Entity> mockEntities = 
            javafx.collections.FXCollections.observableArrayList();
        javafx.collections.ObservableList<Entity> mockFloorEntities = 
            javafx.collections.FXCollections.observableArrayList();
        
        when(mockGameModel.getEntities()).thenReturn(mockEntities);
        when(mockGameModel.getFloorEntities()).thenReturn(mockFloorEntities);
        when(mockGameModel.getPlayer()).thenReturn(null);
        
        return mockGameModel;
    }
    
    public static Model createMockModel() {
        Model mockModel = mock(Model.class);
        
        // Create mock client first to avoid nested mocking issues
        Client mockClient = createMockClient();
        
        when(mockModel.getSelectedDungeon()).thenReturn("Dungeon 1");
        when(mockModel.activeMenuProperty()).thenReturn(new javafx.beans.property.SimpleObjectProperty<>(rougelike.GuiState.MAINMENU));
        when(mockModel.getClient()).thenReturn(mockClient);
        
        // Create simple mock background property that doesn't cause JavaFX issues
        @SuppressWarnings("unchecked")
        javafx.beans.property.ObjectProperty<javafx.scene.layout.Background> mockBackgroundProperty = 
            mock(javafx.beans.property.ObjectProperty.class);
        when(mockModel.backgroundProperty()).thenReturn(mockBackgroundProperty);
        
        // Create mock available backgrounds
        javafx.collections.ObservableList<javafx.scene.image.Image> mockBackgrounds = 
            javafx.collections.FXCollections.observableArrayList();
        mockBackgrounds.add(createMockImage());
        when(mockModel.getAvailableBackgrounds()).thenReturn(mockBackgrounds);
        
        javafx.collections.ObservableList<String> mockDungeons = 
            javafx.collections.FXCollections.observableArrayList();
        mockDungeons.addAll("Dungeon 1", "Dungeon 2", "Test Dungeon");
        when(mockModel.getAvailableDungeons()).thenReturn(mockDungeons);
        
        return mockModel;
    }
    
    public static Client createMockClient() {
        Client mockClient = mock(Client.class);
        
        when(mockClient.isConnected()).thenReturn(false);
        
        return mockClient;
    }
    
    public static CommunityMenuModel createMockCommunityMenuModel() {
        CommunityMenuModel mockModel = mock(CommunityMenuModel.class);
        
        javafx.collections.ObservableList<String> mockCommunityDungeons = 
            javafx.collections.FXCollections.observableArrayList();
        mockCommunityDungeons.addAll("Community Dungeon 1", "Community Dungeon 2");
        when(mockModel.getDungeons()).thenReturn(mockCommunityDungeons);
        
        return mockModel;
    }
    
    public static InteractionResult createMockInteractionResult(Entity entity) {
        InteractionResultType[] types = {InteractionResultType.TAKE_DAMAGE};
        return new InteractionResult(types, entity);
    }
    
    public static InteractionResult createEmptyInteractionResult(Entity entity) {
        InteractionResultType[] types = {};
        return new InteractionResult(types, entity);
    }
    
    public static java.io.ObjectInputStream createMockObjectInputStream() {
        return mock(java.io.ObjectInputStream.class);
    }
    
    public static java.io.ObjectOutputStream createMockObjectOutputStream() {
        return mock(java.io.ObjectOutputStream.class);
    }
    
    public static java.net.Socket createMockSocket() {
        java.net.Socket mockSocket = mock(java.net.Socket.class);
        
        try {
            when(mockSocket.getInputStream()).thenReturn(mock(java.io.InputStream.class));
            when(mockSocket.getOutputStream()).thenReturn(mock(java.io.OutputStream.class));
            when(mockSocket.isConnected()).thenReturn(true);
            when(mockSocket.isClosed()).thenReturn(false);
            when(mockSocket.getInetAddress()).thenReturn(java.net.InetAddress.getLocalHost());
        } catch (java.io.IOException e) {
            // Mock setup failed, but that's okay for testing
        }
        
        return mockSocket;
    }
    
    public static rougelike.game.dungeon.Dungeon createMockDungeon(String name) {
        char[][][] layout = {
            {
                {'W', 'W', 'W'},
                {'W', ' ', 'W'},
                {'W', 'E', 'W'}
            }
        };
        return new rougelike.game.dungeon.Dungeon(name, layout);
    }
    
    public static rougelike.game.dungeon.Dungeon createMockDungeon() {
        return createMockDungeon("Mock Dungeon");
    }
    
    public static java.util.List<Entity> createMockEntityList() {
        java.util.List<Entity> entities = new java.util.ArrayList<>();
        entities.add(createMockPlayer());
        entities.add(createMockEnemy());
        entities.add(createMockWall());
        entities.add(createMockFloor());
        return entities;
    }
    
    public static void configureMockPlayerForMovement(Player mockPlayer) {
        doAnswer(invocation -> {
            // Simulate movement by changing position
            when(mockPlayer.getPositionX()).thenReturn(mockPlayer.getPositionX() - 1);
            return null;
        }).when(mockPlayer).moveLeft();
        
        doAnswer(invocation -> {
            when(mockPlayer.getPositionX()).thenReturn(mockPlayer.getPositionX() + 1);
            return null;
        }).when(mockPlayer).moveRight();
        
        doAnswer(invocation -> {
            when(mockPlayer.getPositionY()).thenReturn(mockPlayer.getPositionY() - 1);
            return null;
        }).when(mockPlayer).moveUp();
        
        doAnswer(invocation -> {
            when(mockPlayer.getPositionY()).thenReturn(mockPlayer.getPositionY() + 1);
            return null;
        }).when(mockPlayer).moveDown();
    }
    
    public static void configureMockEnemyForMovement(Enemy mockEnemy) {
        doAnswer(invocation -> {
            long timeElapsed = invocation.getArgument(0);
            double playerX = invocation.getArgument(1);
            double playerY = invocation.getArgument(2);
            
            // Simulate simple movement toward player
            double currentX = mockEnemy.getPositionX();
            double currentY = mockEnemy.getPositionY();
            
            if (playerX > currentX) {
                when(mockEnemy.getPositionX()).thenReturn(currentX + 0.1);
            } else if (playerX < currentX) {
                when(mockEnemy.getPositionX()).thenReturn(currentX - 0.1);
            }
            
            if (playerY > currentY) {
                when(mockEnemy.getPositionY()).thenReturn(currentY + 0.1);
            } else if (playerY < currentY) {
                when(mockEnemy.getPositionY()).thenReturn(currentY - 0.1);
            }
            
            return null;
        }).when(mockEnemy).move(anyLong(), anyDouble(), anyDouble());
    }
    
    private MockFactory() {
        // Utility class - prevent instantiation
    }
}