package net.metzlar;

import net.metzlar.renderEngine.RenderTile;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.settings.ImageSettings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Image {
    public interface ImageListener {
        void onChanged();
    }

    public ImageSettings settings;

    private Color[][] image;
    private ArrayList<ImageListener> listeners;

    public Image(ImageSettings settings) {
        this.settings = settings;

        this.image = new Color[settings.imageWidth][settings.imageHeight];
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
        BufferedImage returnImage = new BufferedImage(settings.imageWidth, settings.imageHeight, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < this.settings.imageWidth; x++) {
            for (int y = 0; y < this.settings.imageHeight; y++) {
                Color color = this.image[x][y];

                if (color != null) {
                    try {
                        java.awt.Color imageColor = new java.awt.Color(
                                Math.min(color.getR() / exposure, 1),
                                Math.min(color.getG() / exposure, 1),
                                Math.min(color.getB() / exposure, 1)
                        );

                        returnImage.setRGB(x, y, imageColor.getRGB());
                    } catch (IllegalArgumentException e) {
                        System.out.println(color.getR() / exposure);
                        System.out.println(color.getG() / exposure);
                        System.out.println(color.getB() / exposure);
                    }
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
