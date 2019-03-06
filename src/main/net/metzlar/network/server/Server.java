package net.metzlar.network.server;

import net.metzlar.Image;
import net.metzlar.JRay;
import net.metzlar.renderEngine.RenderTile;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * Controls the writing to the final image and manages the cloud
 */
public class Server {
    private Image image;


    private Map<String, RenderClient> clients = new HashMap<>();
    TileManager tileManager = new TileManager();

    public String settingsXML;
    private SocketServer socketServer;

    private boolean finished = false;
    private boolean saveOnFinished;

    public Server(String settingsXML, Image image, boolean saveOnFinished) {
        this.settingsXML = settingsXML;
        this.image = image;
        this.saveOnFinished = saveOnFinished;
    }

    public void start(int port) {
        socketServer = new SocketServer(port, this);
        socketServer.start();

        tileManager.generateTiles(this.image.settings.imageWidth, this.image.settings.imageHeight, this.image.settings.tileSize);
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

    public void assignTileToClient(String id, RenderTile renderTile) {
        RenderClient client = clients.get(id);
    }

    public synchronized void submitTile(String clientID, RenderTile tile) {
        RenderClient client = clients.get(clientID);

        tileManager.removeTile(tile);
        image.AddToImage(tile);
        checkRenderFinished();
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

    private boolean checkRenderFinished() {
        if (finished) return true;

        if (!tileManager.tileAvailable()) {
            if (tileManager.hasActiveTiles())
                return false;

            System.out.println(JRay.statistics);

            finished = true;
            System.out.println("COMPLETED!");
            saveImage();
            return true;
        }

        return false;
    }

    public void shutdownClient(String id) {
        System.out.printf("Client disconnected: %s\n", id);

        if (!checkRenderFinished()) return;

        for (String clientId : clients.keySet())
            if (!clients.get(clientId).isShutdown())
                return;

        socketServer.terminate();
    }

    public synchronized Optional<RenderTile> getTileIfAvailable() {
        if (tileManager.tileAvailable())
            return Optional.of(tileManager.getOptionalTile());

        return Optional.empty();
    }
}
