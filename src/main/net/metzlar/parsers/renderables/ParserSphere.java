package net.metzlar.parsers.renderables;

import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.scene.renderable.Intersectable;
import net.metzlar.renderEngine.scene.renderable.RenderObject;
import net.metzlar.renderEngine.types.Vec3;
import net.metzlar.renderEngine.scene.renderable.Sphere;
import org.jsoup.nodes.Element;

/**
 * Created by Jelle-laptop on 19-Feb-17.
 */
public class ParserSphere implements Parser<RenderObject> {
    @Override
    public RenderObject parse(Element docElement) {
        return new Sphere(
                new Vec3(
                        Double.parseDouble(docElement.select("position > x").html()),
                        Double.parseDouble(docElement.select("position > y").html()),
                        Double.parseDouble(docElement.select("position > z").html())
                ),
                Double.parseDouble(docElement.select("radius").html())
        );
    }
}
