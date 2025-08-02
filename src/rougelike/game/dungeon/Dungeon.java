package rougelike.game.dungeon;

import java.io.Serializable;
import java.util.Arrays;

public class Dungeon implements Serializable {
    private static final long serialVersionUID = 1L; // Ensure compatibility during serialization
    private final String name;
    private final char[][][] layout;

    public Dungeon(String name, char[][][] layout) {
        this.name = name;
        this.layout = layout;
    }

    public String getName() {
        return name;
    }

    public char[][][] getLayout() {
        return layout;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Dungeon{name='").append(name).append("', layout=\n");
        for (char[][] layer : layout) {
            sb.append(Arrays.deepToString(layer)).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}