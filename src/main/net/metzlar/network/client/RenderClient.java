package net.metzlar.network.client;
import net.metzlar.renderEngine.RenderTile;

import java.io.IOException;

public class RenderClient {

    private ServerConnection serverConnection;
    private Client client;
    private RenderTile activeTile;

    public RenderClient(Client client) {
        this.client = client;
    }

    public void openConnection() throws IOException {
        serverConnection = new ServerConnection(client.getHost(), client.getPort(), client);
        serverConnection.open();
    }

    public void requestTile() {
        serverConnection.requestTile()
                .ifPresentOrElse(
                        tile -> this.activeTile = tile,
                        () -> activeTile = null
                );
    }

    public void submitTile() {
        serverConnection.submitTile(activeTile);
    }

    public Client getClient() {
        return client;
    }

    public boolean hasActiveTile() {
        return activeTile != null;
    }

    public RenderTile getActiveTile() {
        return activeTile;
    }
}
