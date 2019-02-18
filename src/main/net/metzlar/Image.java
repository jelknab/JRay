package net.metzlar;

import net.metzlar.renderEngine.RenderTile;
import net.metzlar.renderEngine.types.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Image {
    public interface ImageListener {
        void onChanged();
    }

    private int width;
    private int height;
    private Color[][] image;
    private ArrayList<ImageListener> listeners;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;

        this.image = new Color[width][height];
        this.listeners = new ArrayList<>();
    }

    /**
     * Adds data from rendered tile send by client to final image.
     * @param tile Tile which has finished rendering
     */
    public void AddToImage(RenderTile tile) {
        int i = 0;
        for (int y = tile.getStartY(); y <= tile.getEndY(); y++) {
            for (int x = tile.getStartX(); x <= tile.getEndX(); x++) {
                try {
                    this.image[x][y] = tile.getPixels()[i];
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.printf("oob: %d %d\n", x, y);
                }

                i++;
            }
        }

        notifyListeners();
    }

    public BufferedImage getBufferedImage(float exposure) {
        BufferedImage returnImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                Color color = this.image[x][y];

                if (color != null) {
                    java.awt.Color imageColor = new java.awt.Color(
                            Math.min(color.getR() / exposure, 1),
                            Math.min(color.getG() / exposure, 1),
                            Math.min(color.getB() / exposure, 1)
                    );

                    returnImage.setRGB(x, y, imageColor.getRGB());
                }
            }
        }

        return returnImage;
    }

    public void saveToFile(File file, float exposure) throws IOException {
        ImageIO.write(getBufferedImage(exposure), "png", file);
    }

    public void addListener(ImageListener listener) {
        this.listeners.add(listener);
    }

    private void notifyListeners() {
        for (ImageListener listener : this.listeners) {
            listener.onChanged();
        }
    }
}
