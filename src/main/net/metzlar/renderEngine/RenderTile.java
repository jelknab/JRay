package net.metzlar.renderEngine;

import net.metzlar.renderEngine.types.Color;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class RenderTile implements Serializable {
    private static AtomicInteger nextId = new AtomicInteger();

    private int id;

    private int startX;
    private int endX;
    private int startY;
    private int endY;

    private int width;
    private int height;
    private int size;

    private int position; //Next pixel to get taken
    private Color[] pixels;

    public RenderTile(int startX, int endX, int startY, int endY) {
        this.id = nextId.incrementAndGet();

        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;

        this.width = endX - startX + 1;
        this.height = endY - startY + 1;
        this.size = (width * height);

        this.pixels = new Color[this.size];
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Tile: [%d, %d] to [%d, %d]", startX, startY, endX, endY);
    }

    public int getStartX() {
        return startX;
    }

    public int getEndX() {
        return endX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndY() {
        return endY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public synchronized int takePosition() {
        return (position < this.size ? position++ : -1);
    }

    //Writes color to the data array
    public void submit(int position, Color color) {
        this.pixels[position] = color;
    }

    public Color[] getPixels() {
        return this.pixels;
    }

    public int getId() {
        return id;
    }

    public byte[] toByteArray() {
        // 24 bytes for tile data
        // 4 byte for amount of colors
        // n * 12 bytes for color data
        int byteSize = 20 + size*12;

        ByteBuffer buffer = ByteBuffer.allocate(byteSize);
        buffer.putInt(byteSize);
        buffer.putInt(startX).putInt(endX).putInt(startY).putInt(endY);
        buffer.putInt(id);
        buffer.putInt(size);

        for (Color color : pixels) {
            buffer.putFloat(color.getR());
            buffer.putFloat(color.getG());
            buffer.putFloat(color.getB());
        }

        return buffer.array();
    }

//    public static RenderTile fromInputputStream(InputStream is) {
//        ByteBuffer buffer = ByteBuffer.wrap(data);
//
//        RenderTile tile = new RenderTile(
//                buffer.getInt(),
//                buffer.getInt(),
//                buffer.getInt(),
//                buffer.getInt()
//        );
//
//        tile.id = buffer.getInt();
//
//        int colorCount = buffer.getInt();
//
//        Color[] colors = new Color[colorCount];
//        for (int i = 0; i < colorCount; i++) {
//            colors[i] = new Color(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
//        }
//
//        tile.pixels = colors;
//
//        return tile;
//    }
}
