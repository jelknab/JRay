package net.metzlar.renderEngine.scene.material;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.Sample;
import net.metzlar.renderEngine.scene.Scene;
import net.metzlar.renderEngine.scene.texture.Texture;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;

public class DebugUV extends Material {
    public DebugUV() {
        super(null);
    }

    @Override
    public void init(Scene scene) {

    }

    @Override
    public Color render(Intersection intersection, Render render, Sample sample) {
        return new Color(
                intersection.getTexturePos().getX(),
                intersection.getTexturePos().getY(),
                1 - intersection.getTexturePos().getX() - intersection.getTexturePos().getY()
        );
    }
}
