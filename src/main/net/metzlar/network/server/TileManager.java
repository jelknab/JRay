package net.metzlar.network.server;

import net.metzlar.renderEngine.RenderTile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class TileManager {
    private Queue<RenderTile> remainingTiles;
    private List<RenderTile> activeTiles;

    public void generateTiles(int width, int height, int tileSize) {
        int tileColumns = (width + tileSize - 1) / tileSize;
        int tileRows = (height + tileSize - 1) / tileSize;

        remainingTiles = new PriorityBlockingQueue<>(
                tileColumns * tileRows,
                Comparator.comparingDouble(o -> tileDistanceToCenter(o, width, height))
        );
        activeTiles = new ArrayList<>(tileColumns * tileRows);

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

    public double tileDistanceToCenter(RenderTile tile, int width, int height) {
        return Math.sqrt(Math.pow(tile.getStartX()-width/2.0, 2) + Math.pow(tile.getStartY()-height/2.0, 2));
    }

    public void removeTile(RenderTile tile) {
        activeTiles.removeIf(listTile -> listTile.getId() == tile.getId());
    }

    public boolean hasActiveTiles() {
        return !activeTiles.isEmpty();
    }

    public boolean tileAvailable() {
        return remainingTiles.peek() != null;
    }

    public RenderTile getOptionalTile() {
        RenderTile renderTile = remainingTiles.poll();
        this.activeTiles.add(renderTile);

        return renderTile;
    }
}
