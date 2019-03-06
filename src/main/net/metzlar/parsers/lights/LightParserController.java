package net.metzlar.parsers.lights;

import net.metzlar.parsers.ParseController;
import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.scene.SceneSettings;
import net.metzlar.renderEngine.scene.light.Light;

public class LightParserController extends ParseController<Light, Parser<Light>> {

    public LightParserController(SceneSettings sceneSettings) {
        super(sceneSettings);

        parsers.put("point light", new ParserPointLight());
    }
}
