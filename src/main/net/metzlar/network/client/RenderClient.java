package net.metzlar.network.client;
import net.metzlar.renderEngine.RenderTile;

import java.io.IOException;

public class RenderClient {

    private ServerConnection serverConnection;
    public Client client;
    public RenderTile activeTile;

    public RenderClient(Client client) {
        this.client = client;
    }

    public void openConnection() throws IOException {
        serverConnection = new ServerConnection(client.host, client.port, client);
        serverConnection.open();
    }

    public void requestTile() {
        serverConnection.getOptionalTileFromServer()
                .ifPresentOrElse(
                        tile -> this.activeTile = tile,
                        () -> activeTile = null
                );
    }

    public void submitTile() {
        serverConnection.submitTile(activeTile);
    }

    public boolean hasActiveTile() {
        return activeTile != null;
    }
}
