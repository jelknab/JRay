package net.metzlar.network.server;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class RenderClient {
    private String id;
    private boolean shutdown = false;

    private Socket socket;
    private ClientConnection connection;

    public RenderClient(Socket socket, ClientManager clientManager) throws IOException {
        this.socket = socket;
        this.id = UUID.randomUUID().toString();

        this.connection = new ClientConnection(this, clientManager);
    }

    public String getId() {
        return id;
    }

    public void shutdown() {
        shutdown = true;
    }

    public void start() {
        try {
            connection.open(socket);
        } catch (IOException e) {
            System.out.println("Could not initiate connection to client");
            e.printStackTrace();
        }
    }

    public boolean isShutdown() {
        return shutdown;
    }
}
