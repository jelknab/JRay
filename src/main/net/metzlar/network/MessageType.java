package net.metzlar.network;

import java.util.Arrays;

public enum MessageType {
    GET_SETTINGS(1),
    GET_TILE(2),
    SUBMIT_TILE(3),
    SCENE_COMPLETE(4),
    TILE_AVAILABLE(5);


    private final int id;

    MessageType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MessageType getByID(int id) {
        return Arrays.stream(MessageType.values()).filter(messageType -> messageType.id == id).findFirst().get();
    }
}
