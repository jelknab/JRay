package net.metzlar.parsers.materials;

import net.metzlar.parsers.ParseController;
import net.metzlar.parsers.Parser;
import net.metzlar.parsers.textures.TextureParserController;
import net.metzlar.renderEngine.scene.Scene;
import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.scene.texture.SolidColor;
import net.metzlar.renderEngine.types.Color;
import org.jsoup.nodes.Element;

public class MaterialParserController extends ParseController<Material, Parser<Material>> {
    public MaterialParserController(Scene scene) {
        super(scene);

        parsers.put("lambert", new ParserLambert());
        parsers.put("phong", new ParserPhong());
        parsers.put("specular", new ParserSpecular());
        parsers.put("debug", new ParserDebug());
    }

    @Override
    public Material parse(Element docElement) {
        Material material = super.parse(docElement);

        // Load texture
        Element textureElement = docElement.selectFirst("texture");

        if (textureElement != null) {
            material.setTexture(
                    new TextureParserController().parse(textureElement)
            );
        } else {
            material.setTexture(new SolidColor(Color.BLACK));
        }

        return material;
    }
}
