package rougelike.game.entities;

import java.util.HashMap;
import javafx.scene.image.Image;
import rougelike.game.PlatformUtils;

public class ImageDatabase {
    private static HashMap<Character, Image> images;
    private static HashMap<Character, Image[]> animations;
    
    // Lazy initialization to handle test mode properly
    static {
        initializeImages();
        initializeAnimations();
    }
    
    private static void initializeImages() {
        images = new HashMap<>();
        
        if (PlatformUtils.isTestMode()) {
            // Use test resource manager for consistent mock images
            try {
                Class<?> testResourceManagerClass = Class.forName("utils.TestResourceManager");
                @SuppressWarnings("unchecked")
                java.util.Map<Character, Image> testImages = 
                    (java.util.Map<Character, Image>) testResourceManagerClass
                        .getMethod("createCharacterImageMap")
                        .invoke(null);
                images.putAll(testImages);
            } catch (Exception e) {
                // Fallback to basic null images if TestResourceManager not available
                images.put(' ', null);
                images.put('W', null);
                images.put('E', null);
                images.put('L', null);
                images.put('w', null);
                images.put('p', null);
                images.put('e', null);
            }
        } else {
            // Production image loading
            images.put(' ', new Image("file:assets/misc/floor_1.png"));
            images.put('W', new Image("file:assets/misc/wall_mid.png"));
            images.put('E', new Image("file:assets/misc/tiny_zombie_idle_anim_f3.png"));
            images.put('L', new Image("file:assets/misc/floor_ladder.png"));
            images.put('w', new Image("file:assets/misc/wall_fountain_mid_blue_anim_f2.png"));
            images.put('p', new Image("file:assets/misc/weapon_throwing_axe.png"));
            images.put('e', new Image("file:assets/misc/crate.png"));
        }
    }
    
    private static void initializeAnimations() {
        animations = new HashMap<>();
        
        if (PlatformUtils.isTestMode()) {
            // Use test resource manager for consistent mock animations
            try {
                Class<?> testResourceManagerClass = Class.forName("utils.TestResourceManager");
                @SuppressWarnings("unchecked")
                java.util.Map<Character, Image[]> testAnimations = 
                    (java.util.Map<Character, Image[]>) testResourceManagerClass
                        .getMethod("createCharacterAnimationMap")
                        .invoke(null);
                animations.putAll(testAnimations);
            } catch (Exception e) {
                // Fallback to basic null animations if TestResourceManager not available
                animations.put('E', new Image[4]);
                animations.put('P', new Image[4]);
                animations.put('w', new Image[3]);
                animations.put('R', new Image[4]);
            }
        } else {
            // Production animation loading
            animations.put('E', new Image[] {
                new Image("file:assets/misc/big_zombie_idle_anim_f0.png"),
                new Image("file:assets/misc/big_zombie_idle_anim_f1.png"),
                new Image("file:assets/misc/big_zombie_idle_anim_f2.png"),
                new Image("file:assets/misc/big_zombie_idle_anim_f3.png")
            });

            animations.put('P', new Image[] {
                new Image("file:assets/misc/wizzard_f_idle_anim_f0.png"),
                new Image("file:assets/misc/wizzard_f_idle_anim_f1.png"),
                new Image("file:assets/misc/wizzard_f_idle_anim_f2.png"),
                new Image("file:assets/misc/wizzard_f_idle_anim_f3.png")
            });

            animations.put('w', new Image[] {
                new Image("file:assets/misc/wall_fountain_mid_blue_anim_f0.png"),
                new Image("file:assets/misc/wall_fountain_mid_blue_anim_f1.png"),
                new Image("file:assets/misc/wall_fountain_mid_blue_anim_f2.png")
            });

            animations.put('R', new Image[] {
                new Image("file:assets/misc/wizzard_f_run_anim_f0.png"),
                new Image("file:assets/misc/wizzard_f_run_anim_f1.png"),
                new Image("file:assets/misc/wizzard_f_run_anim_f2.png"),
                new Image("file:assets/misc/wizzard_f_run_anim_f3.png")
            });
        }
    }

    public static Image[] getAnimationFrames(char key) {
        return animations.get(key);
    }

    public static Image getImage(char key) {
        return images.get(key);
    }
    
    /**
     * Reinitialize images and animations. Useful for switching between
     * test and production modes.
     */
    public static void reinitialize() {
        initializeImages();
        initializeAnimations();
    }
}
