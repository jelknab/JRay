package net.metzlar.network.client;

import net.metzlar.Settings;
import net.metzlar.renderEngine.RenderThread;
import net.metzlar.renderEngine.RenderTile;
import net.metzlar.renderEngine.Statistics;

import java.io.IOException;
import java.util.Optional;

/**
 * This is the class that will initiate and control the process of ray casting
 */
public class Client {
    private String host;
    private int port;
    private Settings settings;
    private boolean finished = false;
    private Statistics statistics;

    private ServerConnection serverConnection;

    public Client(String serverAddress) {
        String[] hostPortSplit = serverAddress.split(":");
        this.host = hostPortSplit[0];
        this.port = Integer.parseInt(hostPortSplit[1]);
        this.statistics = new Statistics();

        this.serverConnection = new ServerConnection(this.host, this.port, this);
    }

    public void start() throws IOException {
        System.out.printf("Starting client. Server at: %s:%d\n", host, port);

        serverConnection.open();
        serverConnection.requestSettings().ifPresent(settings -> {
            if (load(settings)) {
                render();
            }
        });
    }

    public boolean load(Settings settings) {
        this.settings = settings;

        //todo: check if all needed files are available

        return true;
    }

    private void render() {
        RenderThread[] renderThreads = new RenderThread[Math.max(Runtime.getRuntime().availableProcessors()-5, 1)];

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
    }

    public void finish() {
        if (!this.finished) {
            this.finished = true;
            System.out.println("Received shutdown signal from server.");

            System.out.println(statistics);
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Settings getSettings() {
        return settings;
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
