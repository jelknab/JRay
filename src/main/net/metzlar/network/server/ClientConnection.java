package net.metzlar.network.server;

import net.metzlar.network.MessageType;
import net.metzlar.network.RequestHandler;
import net.metzlar.renderEngine.RenderTile;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class ClientConnection extends Thread {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final RenderClient renderClient;
    private final ClientManager clientManager;

    public ClientConnection(RenderClient renderClient, ClientManager clientManager) {
        this.renderClient = renderClient;
        this.clientManager = clientManager;
    }

    @Override
    public void run() {
        super.run();

        HashMap<Integer, RequestHandler> handlerMap = new HashMap<>();
        handlerMap.put(MessageType.GET_SETTINGS.getId(), new RequestHandler() {
            @Override
            public Void receive(ObjectInputStream ois) {
                return null;
            }

            @Override
            public void send(ObjectOutputStream oos) throws IOException {
                oos.writeObject(clientManager.getSettings());
            }
        });
        handlerMap.put(MessageType.GET_TILE.getId(), new RequestHandler() {
            @Override
            public Void receive(ObjectInputStream ois) {
                return null;
            }

            @Override
            public void send(ObjectOutputStream oos) throws IOException {
                if (!clientManager.tileAvailable()) {
                    oos.writeInt(MessageType.SCENE_COMPLETE.getId());
                    oos.flush();

                    renderClient.shutdown();
                    return;
                }

                RenderTile tile = clientManager.assignNextTileToClient(renderClient.getId());

                oos.writeInt(MessageType.TILE_AVAILABLE.getId());
                oos.writeObject(tile);
            }
        });
        handlerMap.put(MessageType.SUBMIT_TILE.getId(), new RequestHandler() {
            @Override
            public Void receive(ObjectInputStream ois) throws IOException {
                try {
                    RenderTile tile = (RenderTile) ois.readObject();
                    clientManager.submitTile(renderClient.getId(), tile);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public void send(ObjectOutputStream oos) {}
        });

        try {
            try {
                while (socket.isConnected() && !socket.isClosed()) {
                    RequestHandler handler = handlerMap.get(ois.readInt());
                    handler.receive(ois);
                    handler.send(oos);
                    oos.flush();

                    if (renderClient.isShutdown()) {
                        close();
                    }
                }
            } catch (SocketException e) {
                if (socket.isClosed()) {
                    System.out.println("Could not get next client request. Socket closed due to completion of image?");
                } else {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open(Socket socket) throws IOException {
        this.socket = socket;
        this.socket.setKeepAlive(true);

        this.oos = new ObjectOutputStream(this.socket.getOutputStream());
        this.ois = new ObjectInputStream(this.socket.getInputStream());

        start();
    }

    public void close() {
        try {
            this.oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
