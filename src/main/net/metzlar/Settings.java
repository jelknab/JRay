package net.metzlar;

import net.metzlar.parsers.SceneParser;
import net.metzlar.renderEngine.scene.Scene;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

public class Settings implements Serializable {
    // Image settings
    private int imageWidth;
    private int imageHeight;
    private double aspectRatio;
    private int tileSize;

    private Scene scene;

    Settings(File settingsFile) throws IOException {
        this(Files.readAllBytes(settingsFile.toPath()));
    }

    private Settings(byte[] settingsBytes) {
        this(Jsoup.parse(new String(settingsBytes)));
    }

    private Settings(Document settingsDocument) {
        this.imageWidth = Integer.parseInt(settingsDocument.select("settings > rendersettings > resolution > width").html());
        this.imageHeight = Integer.parseInt(settingsDocument.select("settings > rendersettings > resolution > height").html());
        this.aspectRatio = (double) this.imageWidth / this.imageHeight;
        this.tileSize = Integer.parseInt(settingsDocument.select("rendersettings blockSize").html());

        this.scene = new SceneParser().parse(settingsDocument.select("settings > scene").first());
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public int getTileSize() {
        return tileSize;
    }

    public Scene getScene() {
        return scene;
    }
}
