package net.metzlar.renderEngine.scene.material;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.Sample;
import net.metzlar.renderEngine.scene.texture.CheckerBoard;
import net.metzlar.renderEngine.scene.texture.SolidColor;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.scene.texture.Texture;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.scene.Scene;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Material classes determine the appearance of an object given an intersection
 * All Materials should implement this class
 */
public abstract class Material implements Serializable {
    public static final Material DEFAULT = new Lambert(new CheckerBoard(Color.RED, 5, 5));

    private Texture texture;
    private String name;

    public Material(Texture texture) {
        this.texture = texture;
    }

    public abstract void init(Scene scene);

    public abstract Color render(Intersection intersection, Render render, Sample sample);

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
