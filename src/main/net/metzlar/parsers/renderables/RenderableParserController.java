package net.metzlar.parsers.renderables;

import net.metzlar.parsers.ParseController;
import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.scene.Scene;
import net.metzlar.renderEngine.scene.renderable.Renderable;
import org.jsoup.nodes.Element;

/**
 * Created by Jelle-laptop on 19-Feb-17.
 */
public class RenderableParserController extends ParseController<Renderable, Parser<Renderable>> {

    public RenderableParserController(Scene scene) {
        super(scene);

        parsers.put("sphere", new ParserSphere());
        parsers.put("plane", new ParserPlane());
        parsers.put("modelOBJ", new ParserOBJ());
    }

    @Override
    public Renderable parse(Element docElement) {
        Renderable renderable = super.parse(docElement);

        if (renderable == null) {
            return null;
        }

        if (docElement.hasAttr("material")) {
            renderable.setMaterial(scene.getMaterial(docElement.attr("material")));
        }

        return renderable;
    }
}
