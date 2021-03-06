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
    public double intensity;

    public Light(Vec3 position, Color color, double intensity) {
        super(position, null);
        this.color = color;
        this.intensity = intensity;
    }

    public abstract Color getNormIntensity(Render render, Intersection intersection);

    public abstract Photon getIntensity(Render render, Intersection intersection);

    public Color getColor() {
        return color;
    }

}
