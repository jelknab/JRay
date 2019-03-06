package net.metzlar.renderEngine.scene.renderable;

import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.types.Vec3;

import java.io.Serializable;

public abstract class RenderObject implements Intersectable, Serializable {
    public Material material;
    public Vec3 position;

    public RenderObject(Vec3 position) {
        this(position, null);
    }

    public RenderObject(Vec3 position, Material material) {
        this.position = position;
        this.material = material;
    }
}
