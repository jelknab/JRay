package net.metzlar.renderEngine.scene.renderable;

import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.types.Angle;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model extends Renderable {
    private List<Renderable> faces = new ArrayList<>();

    public Model(Vec3 position, Angle orientation, Material material) {
        super(position, orientation, material);
    }

    public void addFaces(List<Face> faces) {
        this.faces.addAll(faces);
    }

    @Override
    public Intersection intersectRay(Ray r) {
        return Renderable.closestIntersection(r, this.faces);
    }
}
