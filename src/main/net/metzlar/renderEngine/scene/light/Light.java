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

    /**
     * @param render
     * @param intersection
     * @return A color with rgb values between 0 and 1 representing the normalized color over distance
     */
    public abstract Color getNormIntensity(Render render, Intersection intersection);

    /**
     * @param render
     * @param intersection
     * @return A color with rgb values that represent the raw intensity of the light, good for global illumination
     */
    public abstract Color getRawIntensity(Render render, Intersection intersection);

    public Color getColor() {
        return color;
    }

}
