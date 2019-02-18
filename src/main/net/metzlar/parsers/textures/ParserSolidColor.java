package net.metzlar.parsers.textures;

import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.scene.texture.SolidColor;
import net.metzlar.renderEngine.scene.texture.Texture;
import net.metzlar.renderEngine.types.Color;
import org.jsoup.nodes.Element;

public class ParserSolidColor implements Parser<Texture> {
    @Override
    public Texture parse(Element docElement) {
        return new SolidColor(
                new Color(docElement.attr("color"))
        );
    }
}
