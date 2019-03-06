package net.metzlar.renderEngine.scene.material;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.Sample;
import net.metzlar.renderEngine.scene.SceneSettings;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;

public class DebugUV extends Material {
    public DebugUV() {
        super(null);
    }

    @Override
    public void init(SceneSettings sceneSettings) {

    }

    @Override
    public Color render(Intersection intersection, Render render, Sample sample) {
        return new Color(
                intersection.texturePos.getX(),
                intersection.texturePos.getY(),
                1 - intersection.texturePos.getX() - intersection.texturePos.getY()
        );
    }
}
