package net.metzlar.parsers.renderables;

import net.metzlar.parsers.ParseController;
import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.scene.SceneSettings;
import net.metzlar.renderEngine.scene.renderable.RenderObject;
import org.jsoup.nodes.Element;

/**
 * Created by Jelle-laptop on 19-Feb-17.
 */
public class RenderableParserController extends ParseController<RenderObject, Parser<RenderObject>> {

    public RenderableParserController(SceneSettings sceneSettings) {
        super(sceneSettings);

        parsers.put("sphere", new ParserSphere());
        parsers.put("plane", new ParserPlane());
        parsers.put("modelOBJ", new ParserOBJ());
    }

    @Override
    public RenderObject parse(Element docElement) {
        RenderObject renderObject = super.parse(docElement);

        if (renderObject == null) {
            return null;
        }

        if (docElement.hasAttr("material")) {
            renderObject.material = sceneSettings.materials.get(docElement.attr("material"));
        }

        return renderObject;
    }
}
