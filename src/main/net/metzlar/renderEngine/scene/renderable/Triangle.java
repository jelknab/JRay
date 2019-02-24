package net.metzlar.renderEngine.scene.renderable;

import net.metzlar.renderEngine.scene.material.Lambert;
import net.metzlar.renderEngine.scene.texture.CheckerBoard;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;

public class Triangle extends Renderable {
    private Vec3 v0;
    private Vec3 v1;
    private Vec3 v2;

    public Triangle(Vec3 v0, Vec3 v1, Vec3 v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;

        this.material = new Lambert(new CheckerBoard(Color.GREEN, 2, 2));
    }

    @Override
    public Intersection intersectRay(Ray r) {
        return null;
    }
}
