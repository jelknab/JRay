package net.metzlar.parsers.textures;

import net.metzlar.parsers.ParseController;
import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.scene.texture.Texture;

public class TextureParserController extends ParseController<Texture, Parser<Texture>> {

    public TextureParserController() {
        super(null);

        parsers.put("checker", new ParserChecker());
        parsers.put("solid color", new ParserSolidColor());
        //parsers.put("file", new ParserPlane());
    }

}
