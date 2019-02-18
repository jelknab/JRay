package net.metzlar.renderEngine.scene.light;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.scene.Object3D;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Vec3;
import net.metzlar.renderEngine.scene.Scene;

import java.io.Serializable;

/**
 *
 */
public abstract class Light extends Object3D implements Serializable {
    Color color;

    public Light(Vec3 position, Color color) {
        super(position);
        this.color = color;
    }

    public abstract Color getIntensity(Render render, Intersection intersection);

    public Color getColor() {
        return color;
    }

}
