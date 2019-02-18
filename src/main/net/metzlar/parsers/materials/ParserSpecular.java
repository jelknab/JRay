package net.metzlar.parsers.materials;

import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.scene.material.Specular;
import org.jsoup.nodes.Element;

public class ParserSpecular implements Parser<Material> {
    @Override
    public Material parse(Element docElement) {
        return new Specular(
                docElement.select("base_material").html(),
                Double.parseDouble(docElement.select("kd").html()),
                Double.parseDouble(docElement.select("kr").html())
        );
    }
}
