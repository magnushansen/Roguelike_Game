package utils;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * JUnit 5 Extension for proper JavaFX test lifecycle management.
 * Ensures JavaFX Platform is initialized before tests that need it.
 */
public class JavaFXTestExtension implements BeforeAllCallback, BeforeEachCallback {
    
    private static volatile boolean toolkitInitialized = false;
    private static final Object LOCK = new Object();
    
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        initializeJavaFXToolkit();
    }
    
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        // Ensure we're ready for each test
        if (!toolkitInitialized) {
            initializeJavaFXToolkit();
        }
    }
    
    private void initializeJavaFXToolkit() throws InterruptedException {
        if (toolkitInitialized) {
            return;
        }
        
        synchronized (LOCK) {
            if (toolkitInitialized) {
                return;
            }
            
            // Set headless properties before any JavaFX initialization
            setupHeadlessProperties();
            
            // Initialize JavaFX Platform
            if (!Platform.isFxApplicationThread()) {
                final CountDownLatch latch = new CountDownLatch(1);
                
                // Start the JavaFX Platform
                Platform.startup(() -> {
                    // Platform is now initialized
                    latch.countDown();
                });
                
                // Wait for initialization to complete
                if (!latch.await(10, TimeUnit.SECONDS)) {
                    throw new RuntimeException("JavaFX Platform failed to initialize within 10 seconds");
                }
            }
            
            toolkitInitialized = true;
        }
    }
    
    private void setupHeadlessProperties() {
        // Only set if not already set (respect existing configuration)
        setPropertyIfNotSet("java.awt.headless", "true");
        setPropertyIfNotSet("testfx.robot", "glass");
        setPropertyIfNotSet("testfx.headless", "true");
        setPropertyIfNotSet("prism.order", "sw");
        setPropertyIfNotSet("prism.text", "t2k");
        setPropertyIfNotSet("glass.platform", "Monocle");
        setPropertyIfNotSet("monocle.platform", "Headless");
        setPropertyIfNotSet("prism.verbose", "false");
        setPropertyIfNotSet("javafx.macosx.embedded", "true");
        setPropertyIfNotSet("prism.allowhidpi", "false");
        setPropertyIfNotSet("quantum.multithreaded", "false");
        setPropertyIfNotSet("test.mode", "true");
    }
    
    private void setPropertyIfNotSet(String key, String value) {
        if (System.getProperty(key) == null) {
            System.setProperty(key, value);
        }
    }
    
    /**
     * Utility method to run code on the JavaFX Application Thread
     * and wait for completion. Useful for tests that need to interact
     * with JavaFX components.
     */
    public static void runAndWait(Runnable action) throws InterruptedException {
        if (Platform.isFxApplicationThread()) {
            action.run();
        } else {
            final CountDownLatch latch = new CountDownLatch(1);
            final Exception[] exception = new Exception[1];
            
            Platform.runLater(() -> {
                try {
                    action.run();
                } catch (Exception e) {
                    exception[0] = e;
                } finally {
                    latch.countDown();
                }
            });
            
            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("JavaFX operation timed out");
            }
            
            if (exception[0] != null) {
                throw new RuntimeException("JavaFX operation failed", exception[0]);
            }
        }
    }
}