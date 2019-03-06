package net.metzlar.network.client;

import net.metzlar.Settings;
import net.metzlar.renderEngine.RenderThread;
import net.metzlar.renderEngine.scene.SceneSettings;
import net.metzlar.settings.ImageSettings;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * This is the class that will initiate and control the process of ray casting
 */
public class Client {
    public String host;
    public int port;
    public ImageSettings imageSettings;
    public SceneSettings sceneSettings;
    private boolean finished = false;

    private ServerConnection serverConnection;

    public Client(String serverAddress) {
        String[] hostPortSplit = serverAddress.split(":");
        this.host = hostPortSplit[0];
        this.port = Integer.parseInt(hostPortSplit[1]);

        this.serverConnection = new ServerConnection(this.host, this.port, this);
    }

    public void start() throws IOException {
        System.out.printf("Starting client. Server at: %s:%d\n", host, port);

        serverConnection.open();
        serverConnection.RequestSettingsXMLFromServer().ifPresent(settingsXML -> {
            if (loadSettingsDocument(settingsXML)) {
                this.serverConnection.close();
                render();
            }
        });
    }

    public boolean loadSettingsDocument(String settingsXML) {
        System.out.println("Parsing settings file...");

        Settings settings = new Settings(Jsoup.parse(settingsXML));
        this.imageSettings = settings.parseImageSettings();
        this.sceneSettings = settings.parseSceneSettings();

        return true;
    }

    private void render() {
        RenderThread[] renderThreads = new RenderThread[Math.max(Runtime.getRuntime().availableProcessors()-1, 1)];

        for (int thread = 0; thread < renderThreads.length; thread++) {
            RenderClient renderClient = new RenderClient(this);

            try {
                renderClient.openConnection();
            } catch (IOException e) {
                System.out.printf("Could not initiate connection with server: %s\n", e.getMessage());
                continue;
            }

            renderThreads[thread] = new RenderThread(renderClient);

            renderThreads[thread].start();
        }

        for (RenderThread renderThread : renderThreads) {
            try {
                renderThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All render threads are done.");
    }

    public void finish() {
        if (!this.finished) {
            this.finished = true;
            System.out.println("Received shutdown signal from server.");
        }
    }
}
