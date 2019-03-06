package net.metzlar.renderEngine.scene.renderable.mesh;

import net.metzlar.renderEngine.scene.renderable.RenderObject;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;

import java.io.Serializable;
import java.util.List;

public class Mesh extends RenderObject implements Serializable {
    private KDTree kdTree;

    public Mesh(Vec3 position, List<Face> faces) {
        super(position);
        this.kdTree = new KDTree(faces);
    }

    @Override
    public Intersection intersectRay(Ray r) {
        Intersection intersection = kdTree.intersectRay(r);
        if (intersection != null)
            intersection.renderable = this;

        return intersection;
    }
}
