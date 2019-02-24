package net.metzlar.renderEngine.scene.renderable;

import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.types.Angle;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;

public class Model extends Renderable {


    Model(Vec3 position, Angle orientation, Material material) {
        super(position, orientation, material);
    }

    @Override
    public Intersection intersectRay(Ray r) {
        return null;
    }
}
