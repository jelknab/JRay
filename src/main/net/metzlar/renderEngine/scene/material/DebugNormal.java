package net.metzlar.renderEngine.scene.material;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.Sample;
import net.metzlar.renderEngine.scene.SceneSettings;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;

public class DebugNormal extends Material {

    public DebugNormal() {
        super(null);
    }

    @Override
    public void init(SceneSettings sceneSettings) {

    }

    @Override
    public Color render(Intersection intersection, Render render, Sample sample) {
        return new Color(
                (1 + Color.RED.multiply(intersection.normal.getX()).getR()) / 2,
                (1 + Color.GREEN.multiply(intersection.normal.getY()).getG()) / 2,
                (1 + Color.BLUE.multiply(intersection.normal.getZ()).getB()) / 2
        );
    }
}
