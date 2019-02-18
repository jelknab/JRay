package net.metzlar.parsers.textures;

import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.scene.texture.CheckerBoard;
import net.metzlar.renderEngine.scene.texture.Texture;
import net.metzlar.renderEngine.types.Color;
import org.jsoup.nodes.Element;

public class ParserChecker implements Parser<Texture> {
    @Override
    public Texture parse(Element docElement) {
        return new CheckerBoard(
                new Color(docElement.attr("color")),
                Integer.parseInt(docElement.attr("u")),
                Integer.parseInt(docElement.attr("v"))
        );
    }
}
