package net.metzlar.renderEngine.scene.renderable;

import net.metzlar.renderEngine.scene.Object3D;
import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.scene.material.Lambert;
import net.metzlar.renderEngine.scene.texture.CheckerBoard;
import net.metzlar.renderEngine.types.*;

import java.io.Serializable;

/**
 * Renderable is the class that all objects that can be intersected by rays should extend.
 */
public abstract class Renderable extends Object3D implements Serializable {
    Vec3 orientation;
    Material material;

    Renderable() {
        this(new Vec3());
    }

    Renderable(Vec3 position) {
        this(position, new Angle(), new Lambert(new CheckerBoard(Color.RED, 20, 20)));
    }

    Renderable(Vec3 position, Angle orientation) {
        this(position, orientation, new Lambert(new CheckerBoard(Color.RED, 20, 20)));
    }

    Renderable(Vec3 position, Material material) {
        this(position, new Angle(), material);
    }

    Renderable(Vec3 position, Angle orientation, Material material) {
        super(position);
        this.material = material;
    }

    /**
     * @param r ray to check intersection with
     * @return null if ray does not hit, or intersection if it does.
     */
    public abstract Intersection intersectRay(Ray r);

    public Vec3 getOrientation() {
        return orientation;
    }

    public void setOrientation(Vec3 orientation) {
        this.orientation = orientation;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
