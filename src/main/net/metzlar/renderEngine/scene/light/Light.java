package net.metzlar.renderEngine.scene.light;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.scene.Object3D;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Vec3;

import java.io.Serializable;

/**
 *
 */
public abstract class Light extends Object3D implements Serializable {
    Color color;

    public Light(Vec3 position, Color color) {
        super(position, null);
        this.color = color;
    }

    public abstract Color getNormIntensity(Render render, Intersection intersection);

    public abstract Color getRawIntensity(Render render, Intersection intersection);

    public Color getColor() {
        return color;
    }

}
