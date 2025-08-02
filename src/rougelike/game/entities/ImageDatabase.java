package rougelike.game.entities;

import java.util.HashMap;
import javafx.scene.image.Image;

public class ImageDatabase {
    private static HashMap<Character, Image> images = new HashMap<>() {
        {
            put(' ', new Image("file:assets/misc/floor_1.png"));
            put('W', new Image("file:assets/misc/wall_mid.png"));
            put('E', new Image("file:assets/misc/tiny_zombie_idle_anim_f3.png"));
            put('L', new Image("file:assets/misc/floor_ladder.png"));
            put('w', new Image("file:assets/misc/wall_fountain_mid_blue_anim_f2.png"));
            put('p', new Image("file:assets/misc/weapon_throwing_axe.png"));
            put('e', new Image("file:assets/misc/crate.png"));
        }
    };

    private static HashMap<Character, Image[]> animations = new HashMap<>() {
        {
            put('E', new Image[] {
                new Image("file:assets/misc/big_zombie_idle_anim_f0.png"),
                new Image("file:assets/misc/big_zombie_idle_anim_f1.png"),
                new Image("file:assets/misc/big_zombie_idle_anim_f2.png"),
                new Image("file:assets/misc/big_zombie_idle_anim_f3.png")
            });

            put('P', new Image[] {
                new Image("file:assets/misc/wizzard_f_idle_anim_f0.png"),
                new Image("file:assets/misc/wizzard_f_idle_anim_f1.png"),
                new Image("file:assets/misc/wizzard_f_idle_anim_f2.png"),
                new Image("file:assets/misc/wizzard_f_idle_anim_f3.png")
            });

            put('w', new Image[] {
                new Image("file:assets/misc/wall_fountain_mid_blue_anim_f0.png"),
                new Image("file:assets/misc/wall_fountain_mid_blue_anim_f1.png"),
                new Image("file:assets/misc/wall_fountain_mid_blue_anim_f2.png")
            });

            put('R', new Image[] {
                new Image("file:assets/misc/wizzard_f_run_anim_f0.png"),
                new Image("file:assets/misc/wizzard_f_run_anim_f1.png"),
                new Image("file:assets/misc/wizzard_f_run_anim_f2.png"),
                new Image("file:assets/misc/wizzard_f_run_anim_f3.png")
            });
        }
    };

    public static Image[] getAnimationFrames(char key) {
        return animations.get(key);
    }

    public static Image getImage(char key) {
        return images.get(key);
    }
}
