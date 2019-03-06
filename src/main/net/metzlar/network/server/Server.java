package net.metzlar.network.server;

import net.metzlar.Image;
import net.metzlar.JRay;
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
public class Server {
    private Image image;

    private Queue<RenderTile> remainingTiles;
    private Queue<RenderTile> activeTiles = new ConcurrentLinkedQueue<>();
    private Map<String, RenderClient> clients = new HashMap<>();

    public String settingsXML;
    private SocketServer server;

    private boolean finished = false;
    private boolean saveOnFinished;

    public Server(String settingsXML, Image image, boolean saveOnFinished) {
        this.settingsXML = settingsXML;
        this.image = image;
        this.saveOnFinished = saveOnFinished;
    }

    public void start(int port) {
        generateTiles(this.image.settings.imageWidth, this.image.settings.imageHeight, this.image.settings.tileSize);
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
        return Math.sqrt(Math.pow(tile.getStartX()-width/2.0, 2) + Math.pow(tile.getStartY()-height/2.0, 2));
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
            System.out.println(JRay.statistics);

            finished = true;
            return true;
        }

        return false;
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
