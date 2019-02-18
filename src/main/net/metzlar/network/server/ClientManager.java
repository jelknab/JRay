package net.metzlar.network.server;

import net.metzlar.Image;
import net.metzlar.Settings;
import net.metzlar.renderEngine.RenderTile;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Controls the writing to the final image and manages the cloud
 */
public class ClientManager {
    private Image image;

    private Queue<RenderTile> remainingTiles;
    private Queue<RenderTile> activeTiles = new ConcurrentLinkedQueue<>();
    private Map<String, RenderClient> clients = new HashMap<>();

    private Settings settings;
    private SocketServer server;

    private boolean finished = false;
    private boolean saveOnFinished;

    public ClientManager(Settings settings, Image image, boolean saveOnFinished) {
        this.settings = settings;
        this.image = image;
        this.saveOnFinished = saveOnFinished;
    }

    public void startServer(int port) {
        generateTiles(this.settings.getImageWidth(), this.settings.getImageHeight(), this.settings.getTileSize());
        server = new SocketServer(port, this);
        server.start();
    }

    //Registers new client and returns it's id
    public void registerClient(Socket clientSocket) throws IOException {
        RenderClient newClient = new RenderClient(clientSocket, this);
        clients.put(newClient.getId(), newClient);

        System.out.printf("Registered new client: %s\n", newClient.getId());

        newClient.start();
    }

    public RenderClient getClientById(String id) {
        return clients.get(id);
    }

    public synchronized boolean tileAvailable() {
        return remainingTiles.peek() != null;
    }

    public synchronized RenderTile assignNextTileToClient(String id) {
        RenderClient client = clients.get(id);
        RenderTile assignedTile = remainingTiles.poll();
        activeTiles.add(assignedTile);

        return assignedTile;
    }

    public void submitTile(String clientID, RenderTile tile) {
        RenderClient client = clients.get(clientID);
        activeTiles.removeIf(listTile -> listTile.getId() == tile.getId());

        image.AddToImage(tile);

        if (!finished && finished()) {
            System.out.println("COMPLETED!");
            saveImage();
        }
    }

    private double tileDistanceToCenter(RenderTile tile, int width, int height) {
        return Math.sqrt(Math.pow(tile.getStartX()-width/2, 2) + Math.pow(tile.getStartY()-height/2, 2));
    }

    private void generateTiles(int width, int height, int tileSize) {
        int tileColumns = (width + tileSize - 1) / tileSize;
        int tileRows = (height + tileSize - 1) / tileSize;

        remainingTiles = new PriorityBlockingQueue<>(
                tileColumns * tileRows,
                Comparator.comparingDouble(o -> tileDistanceToCenter(o, width, height))
        );

        System.out.printf("Generating tiles. size: %d, columns: %d, rows: %d\n", tileSize, tileColumns, tileRows);
        for (int column = 0; column < tileColumns; column++) {
            for (int row = 0; row < tileRows; row++) {
                int startX = column * tileSize;
                int startY = row * tileSize;

                int endX = Math.min(startX + tileSize, width) - 1;
                int endY = Math.min(startY + tileSize, height) - 1;

                remainingTiles.add(new RenderTile(startX, endX, startY, endY));
            }
        }
    }

    private void saveImage() {
        if (saveOnFinished) {
            System.out.print("Saving image.");

            File saveFile = new File("JRAY - " + System.currentTimeMillis() + ".png");

            try {
                image.saveToFile(saveFile, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.printf("\n\tsaved as: %s", saveFile.getAbsolutePath());
        }
    }

    private synchronized boolean finished() {
        if (finished) return true;

        if (remainingTiles.isEmpty()) {
            if (!activeTiles.isEmpty()) {
                return false;
            }

            clients.values().forEach(RenderClient::shutdown);

            finished = true;
            return true;
        }

        return false;
    }

    public Settings getSettings() {
        return settings;
    }

    public void shutdownClient(String id) {
        System.out.println("shutting down");

        clients.get(id).shutdown();

        for (String clientId : clients.keySet()) {
            if (!clients.get(clientId).isShutdown()) {
                return;
            }
        }

        server.terminate();
    }
}
