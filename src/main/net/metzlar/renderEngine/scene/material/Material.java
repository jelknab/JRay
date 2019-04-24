package net.metzlar.renderEngine.scene.material;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.SampleDirect;
import net.metzlar.renderEngine.scene.Scene;
import net.metzlar.renderEngine.scene.texture.SolidColor;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.scene.texture.Texture;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;

import java.io.Serializable;
import java.util.Random;

/**
 * Material classes determine the appearance of an object given an intersection
 * All Materials should implement this class
 */
public abstract class Material implements Serializable {
    private static final int SAMPLES = 16;
    public static final Material DEFAULT = new Lambert(new SolidColor(Color.RED));

    private Texture texture;
    private String name;
    private Random random = new Random();

    public Material(Texture texture) {
        this.texture = texture;
    }

    public abstract void init(Scene scene);

    public Color getSurfaceColor(Intersection intersection) {
        return this.getTexture().getColorAtPosition(intersection.texturePos);
    }

    public abstract Color render(Intersection intersection, Render render, SampleDirect sample);

    public Color getIndirectDiffuse(Intersection intersection) {
        return getSurfaceColor(intersection);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
