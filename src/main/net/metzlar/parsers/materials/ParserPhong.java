package net.metzlar.parsers.materials;

import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.scene.material.Phong;
import org.jsoup.nodes.Element;

public class ParserPhong implements Parser<Material> {

    @Override
    public Material parse(Element docElement) {
        return new Phong(
                null,
                Double.parseDouble(docElement.select("kd").html()),
                Double.parseDouble(docElement.select("ks").html()),
                Double.parseDouble(docElement.select("n").html())
        );
    }
}
