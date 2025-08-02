package rougelike.networking;

import rougelike.game.dungeon.Dungeon;

import java.io.ObjectInputStream;
import java.util.function.Consumer;

public class MessageReceiver implements Runnable {
    private final ObjectInputStream in;
    private final Consumer<String[]> onDungeonListReceived;
    private final Consumer<Dungeon> onDungeonReceived;

    public MessageReceiver(ObjectInputStream in, Consumer<String[]> onDungeonListReceived,
            Consumer<Dungeon> onDungeonReceived) {
        this.in = in;
        this.onDungeonListReceived = onDungeonListReceived;
        this.onDungeonReceived = onDungeonReceived;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object received = in.readObject();
                System.out.println("Received object type: " + received.getClass().getSimpleName());

                switch (received) {
                    case String[] dungeonList -> {
                        onDungeonListReceived.accept(dungeonList);
                        System.out.println("Received dungeon list");
                    }
                    default -> {
                        if (received.getClass().getSimpleName().equals("Dungeon")) {
                            try {
                                // Use reflection to get the name and layout of the dungeon
                                String name = (String) received.getClass().getMethod("getName").invoke(received);
                                char[][][] layout = (char[][][]) received.getClass().getMethod("getLayout")
                                        .invoke(received);

                                Dungeon clientDungeon = new Dungeon(name, layout);
                                System.out.println("test");
                                onDungeonReceived.accept(clientDungeon);
                                System.out.println("Received dungeon layout: " + clientDungeon.getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.err.println("Unexpected data received: " + received.getClass().getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}