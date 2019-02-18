package net.metzlar.parsers.renderables;

import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.types.Angle;
import net.metzlar.renderEngine.types.Vec3;
import net.metzlar.renderEngine.scene.renderable.Plane;
import net.metzlar.renderEngine.scene.renderable.Renderable;
import org.jsoup.nodes.Element;

public class ParserPlane implements Parser<Renderable> {
    @Override
    public Renderable parse(Element docElement) {
        return new Plane(
                new Vec3(
                        Double.parseDouble(docElement.select("position > x").html()),
                        Double.parseDouble(docElement.select("position > y").html()),
                        Double.parseDouble(docElement.select("position > z").html())
                ),
                new Angle(
                        Double.parseDouble(docElement.select("angle > pitch").html()),
                        Double.parseDouble(docElement.select("angle > yaw").html()),
                        Double.parseDouble(docElement.select("angle > roll").html())
                ),
                Double.parseDouble(docElement.select("texture_size").html())
        );
    }
}
