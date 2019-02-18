package net.metzlar.parsers.lights;

import net.metzlar.parsers.FunctionParser;
import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Vec3;
import net.metzlar.renderEngine.scene.light.Light;
import net.metzlar.renderEngine.scene.light.PointLight;
import org.jsoup.nodes.Element;

public class ParserPointLight extends FunctionParser implements Parser<Light> {
    @Override
    public Light parse(Element element) {
        return new PointLight(
                new Vec3(
                        parseFunction(element.select("position > x").html()),
                        parseFunction(element.select("position > y").html()),
                        parseFunction(element.select("position > z").html())
                ),
                new Color(element.selectFirst("color").html()),
                Double.parseDouble(element.selectFirst("intensity").html())
        );
    }
}
