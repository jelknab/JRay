package net.metzlar.network.client;

import net.metzlar.Settings;
import net.metzlar.network.MessageType;
import net.metzlar.network.RequestHandler;
import net.metzlar.renderEngine.RenderTile;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class ServerConnection {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private String host;
    private int port;
    private Client client;

    public ServerConnection(String host, int port, Client client) {
        this.host = host;
        this.port = port;
        this.client = client;
    }

    public void open() throws IOException {
        this.socket = new Socket(host, port);
        this.socket.setKeepAlive(true);

        oos = new ObjectOutputStream((this.socket.getOutputStream()));
        ois = new ObjectInputStream((this.socket.getInputStream()));
    }

    public void close() {
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void submitTile(RenderTile tile) {
        new RequestHandlerWorker<>(new RequestHandler<Optional<Void>>() {
            @Override
            public Optional<Void> receive(ObjectInputStream ois) throws IOException {
                return Optional.empty();
            }

            @Override
            public void send(ObjectOutputStream oos) throws IOException {
                oos.writeInt(MessageType.SUBMIT_TILE.getId());
                oos.writeObject(tile);
                oos.flush();
            }
        }).handle(oos, ois);
    }

    Optional<String> RequestSettingsXMLFromServer() {
        return new RequestHandlerWorker<>(new RequestHandler<Optional<String>>() {
            @Override
            public Optional<String> receive(ObjectInputStream ois) throws IOException {
                try {
                    return Optional.of((String) ois.readObject());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                return Optional.empty();
            }

            @Override
            public void send(ObjectOutputStream oos) throws IOException {
                oos.writeInt(MessageType.GET_SETTINGS.getId());
                oos.flush();
            }
        }).handle(oos, ois);
    }

    Optional<RenderTile> getOptionalTileFromServer() {
        return new RequestHandlerWorker<>(new RequestHandler<Optional<RenderTile>>() {
            @Override
            public Optional<RenderTile> receive(ObjectInputStream ois) throws IOException {
                switch (MessageType.getByID(ois.readInt())) {
                    case TILE_AVAILABLE:
                        try {
                            return Optional.of((RenderTile) ois.readObject());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    case SCENE_COMPLETE:
                        return Optional.empty();
                }

                return Optional.empty();
            }

            @Override
            public void send(ObjectOutputStream oos) throws IOException {
                oos.writeInt(MessageType.GET_TILE.getId());
                oos.flush();
            }
        }).handle(oos, ois);
    }
}


