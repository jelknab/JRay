package net.metzlar.renderEngine.scene.renderable.mesh;

import net.metzlar.renderEngine.scene.renderable.Intersectable;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;

import java.io.Serializable;
import java.util.List;

public class KDTree implements Intersectable, Serializable {
    private KDNode root;

    public KDTree(List<Face> faces) {
        this.root = new KDNode(0, faces);
    }

    @Override
    public Intersection intersectRay(Ray r) {
        return root.traverse(r);
    }
}
