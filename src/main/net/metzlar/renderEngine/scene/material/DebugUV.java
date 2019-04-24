package net.metzlar.renderEngine.scene.material;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.SampleDirect;
import net.metzlar.renderEngine.scene.Scene;
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
    public Color render(Intersection intersection, Render render, SampleDirect sample) {
        return new Color(
                intersection.texturePos.getX(),
                intersection.texturePos.getY(),
                1 - intersection.texturePos.getX() - intersection.texturePos.getY()
        );
    }
}
