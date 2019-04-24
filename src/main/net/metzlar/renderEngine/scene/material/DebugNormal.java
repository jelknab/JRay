package net.metzlar.renderEngine.scene.material;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.SampleDirect;
import net.metzlar.renderEngine.scene.Scene;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;

public class DebugNormal extends Material {

    public DebugNormal() {
        super(null);
    }

    @Override
    public void init(Scene scene) {

    }

    @Override
    public Color render(Intersection intersection, Render render, SampleDirect sample) {
        return new Color(
                (1 + Color.RED.multiply(intersection.normal.getX()).getR()) / 2,
                (1 + Color.GREEN.multiply(intersection.normal.getY()).getG()) / 2,
                (1 + Color.BLUE.multiply(intersection.normal.getZ()).getB()) / 2
        );
    }
}
