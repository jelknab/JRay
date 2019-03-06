package net.metzlar.settings;

public class ImageSettings {
    public int imageWidth;
    public int imageHeight;
    public double aspectRatio;
    public int tileSize;
    public int subSamples;

    public ImageSettings(int imageWidth, int imageHeight, int tileSize, int subSamples) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.aspectRatio = (double) this.imageWidth / this.imageHeight;
        this.tileSize = tileSize;
        this.subSamples = subSamples;
    }
}
