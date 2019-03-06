package net.metzlar.renderEngine.scene.renderable.mesh;

import net.metzlar.renderEngine.scene.renderable.Intersectable;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Axis Aligned Bounding Box
 */
public class AABB implements Intersectable, Serializable {
    private Vec3 min, max;

    public AABB(Vec3 min, Vec3 max) {
        this.min = min;
        this.max = max;
    }

    public AABB(List<Face> faces) {
        this.min = new Vec3(
                faces.stream().mapToDouble(face -> Arrays.stream(face.getVertices()).mapToDouble(Vec3::getX).min().getAsDouble()).min().getAsDouble(),
                faces.stream().mapToDouble(face -> Arrays.stream(face.getVertices()).mapToDouble(Vec3::getY).min().getAsDouble()).min().getAsDouble(),
                faces.stream().mapToDouble(face -> Arrays.stream(face.getVertices()).mapToDouble(Vec3::getZ).min().getAsDouble()).min().getAsDouble()
        );

        this.max = new Vec3(
                faces.stream().mapToDouble(face -> Arrays.stream(face.getVertices()).mapToDouble(Vec3::getX).max().getAsDouble()).max().getAsDouble(),
                faces.stream().mapToDouble(face -> Arrays.stream(face.getVertices()).mapToDouble(Vec3::getY).max().getAsDouble()).max().getAsDouble(),
                faces.stream().mapToDouble(face -> Arrays.stream(face.getVertices()).mapToDouble(Vec3::getZ).max().getAsDouble()).max().getAsDouble()
        );
    }

    @Override
    public Intersection intersectRay(Ray ray) {
        double dirfracX = 1.0 / ray.direction.getX();
        double dirfracY = 1.0 / ray.direction.getY();
        double dirfracZ = 1.0 / ray.direction.getZ();

        double[] t = {
                (min.getX() - ray.origin.getX()) * dirfracX,
                (max.getX() - ray.origin.getX()) * dirfracX,
                (min.getY() - ray.origin.getY()) * dirfracY,
                (max.getY() - ray.origin.getY()) * dirfracY,
                (min.getZ() - ray.origin.getZ()) * dirfracZ,
                (max.getZ() - ray.origin.getZ()) * dirfracZ,
        };

        double tmin = Math.max(Math.max(Math.min(t[0], t[1]), Math.min(t[2], t[3])), Math.min(t[4], t[5]));
        double tmax = Math.min(Math.min(Math.max(t[0], t[1]), Math.max(t[2], t[3])), Math.max(t[4], t[5]));

        if (tmax < 0) return null; // box is behind ray

        if (tmin > tmax) return null; // No intersection

        return new Intersection(ray, null, null, null, null, tmin);
    }
}
