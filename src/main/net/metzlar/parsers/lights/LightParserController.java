package net.metzlar.parsers.lights;

import net.metzlar.parsers.ParseController;
import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.scene.Scene;
import net.metzlar.renderEngine.scene.light.Light;

public class LightParserController extends ParseController<Light, Parser<Light>> {

    public LightParserController(Scene scene) {
        super(scene);

        parsers.put("point light", new ParserPointLight());
    }
}
