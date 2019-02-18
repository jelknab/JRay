package net.metzlar.renderEngine.scene.material;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.Sample;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.scene.texture.Texture;
import net.metzlar.renderEngine.scene.Scene;

public class DebugNormal extends Material {

    public DebugNormal() {
        super(null);
    }

    @Override
    public void init(Scene scene) {

    }

    @Override
    public Color render(Intersection intersection, Render render, Sample sample) {
        return new Color(
                (1 + Color.RED.multiply(intersection.getNormal().getX()).getR()) / 2,
                (1 + Color.GREEN.multiply(intersection.getNormal().getY()).getG()) / 2,
                (1 + Color.BLUE.multiply(intersection.getNormal().getZ()).getB()) / 2
        );
    }
}
