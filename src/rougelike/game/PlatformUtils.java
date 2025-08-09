package rougelike.game;

import javafx.application.Platform;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for platform-dependent operations that need to behave differently
 * in test mode vs normal application mode. Helps with JavaFX testing challenges.
 */
public class PlatformUtils {
    
    private static final String TEST_MODE_PROPERTY = "test.mode";
    
    /**
     * Executes a runnable on the JavaFX Application Thread.
     * In test mode, executes immediately to avoid threading issues.
     * In normal mode, uses Platform.runLater().
     */
    public static void runLater(Runnable runnable) {
        if (isTestMode()) {
            // Execute immediately in test mode to avoid timing issues
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }
    
    /**
     * Executes a runnable on the JavaFX Application Thread and waits for completion.
     * Useful when you need to ensure the operation completes before continuing.
     */
    public static void runAndWait(Runnable runnable) throws InterruptedException {
        if (isTestMode()) {
            // Execute immediately in test mode
            runnable.run();
            return;
        }
        
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            final CountDownLatch latch = new CountDownLatch(1);
            final Exception[] exception = new Exception[1];
            
            Platform.runLater(() -> {
                try {
                    runnable.run();
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
    
    /**
     * Checks if the application is running in test mode.
     */
    public static boolean isTestMode() {
        return "true".equals(System.getProperty(TEST_MODE_PROPERTY));
    }
    
    /**
     * Checks if we're currently on the JavaFX Application Thread.
     * In test mode, always returns true to simplify threading.
     */
    public static boolean isFxApplicationThread() {
        if (isTestMode()) {
            return true; // Simplify threading in tests
        }
        return Platform.isFxApplicationThread();
    }
    
    /**
     * Sets test mode flag. Should only be called from test setup.
     */
    public static void setTestMode(boolean testMode) {
        System.setProperty(TEST_MODE_PROPERTY, String.valueOf(testMode));
    }
    
    /**
     * Safely exits the platform. In test mode, does nothing to avoid
     * shutting down the platform during test suite execution.
     */
    public static void exit() {
        if (!isTestMode()) {
            Platform.exit();
        }
    }
}