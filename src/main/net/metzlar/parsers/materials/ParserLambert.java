package net.metzlar.parsers.materials;

import net.metzlar.parsers.Parser;
import net.metzlar.renderEngine.scene.material.Lambert;
import net.metzlar.renderEngine.scene.material.Material;
import org.jsoup.nodes.Element;

public class ParserLambert implements Parser<Material> {
    @Override
    public Material parse(Element docElement) {
        return new Lambert(null);
    }
}
