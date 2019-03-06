package net.metzlar;

import net.metzlar.parsers.SceneParser;
import net.metzlar.renderEngine.scene.SceneSettings;
import net.metzlar.settings.ImageSettings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

public class Settings implements Serializable {
    // Image settingsXML

    private final Document settingsDocument;

    public Settings(Document settingsDocument) {
        this.settingsDocument = settingsDocument;
    }

    public ImageSettings parseImageSettings() {
        return new ImageSettings(
                Integer.parseInt(settingsDocument.select("settingsXML > rendersettings > resolution > width").html()),
                Integer.parseInt(settingsDocument.select("settingsXML > rendersettings > resolution > height").html()),
                Integer.parseInt(settingsDocument.select("rendersettings blockSize").html()),
                Integer.parseInt(settingsDocument.select("settingsXML > rendersettings > resolution > subSamples").html())
        );
    }

    public SceneSettings parseSceneSettings() {
        return new SceneParser().parse(settingsDocument.select("settingsXML > sceneSettings").first());
    }
}
