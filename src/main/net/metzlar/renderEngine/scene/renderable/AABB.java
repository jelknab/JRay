package net.metzlar.renderEngine.scene.renderable;

import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;

/**
 * Axis Aligned Bounding Box
 */
public class AABB extends Renderable {
    private Vec3 min, max;

    public AABB(Vec3 min, Vec3 max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Intersection intersectRay(Ray r) {
        double dirfracX = 1.0 / r.getDirection().getX();
        double dirfracY = 1.0 / r.getDirection().getY();
        double dirfracZ = 1.0 / r.getDirection().getZ();

        double[] t = {
                (min.getX() - r.getOrigin().getX()) * dirfracX,
                (max.getX() - r.getOrigin().getX()) * dirfracX,
                (min.getY() - r.getOrigin().getY()) * dirfracY,
                (max.getY() - r.getOrigin().getY()) * dirfracY,
                (min.getZ() - r.getOrigin().getZ()) * dirfracZ,
                (max.getZ() - r.getOrigin().getZ()) * dirfracZ,
        };

        double tmin = Math.max(Math.max(Math.min(t[0], t[1]), Math.min(t[2], t[3])), Math.min(t[4], t[5]));
        double tmax = Math.min(Math.min(Math.max(t[0], t[1]), Math.max(t[2], t[3])), Math.max(t[4], t[5]));

        if (tmax < 0) return null; // box is behind ray

        if (tmin > tmax) return null; // No intersection

        return new Intersection(r, null, null, null, this, tmin);
    }

    public AABB[] splitOverX(double x) {
        AABB left = new AABB(
                this.min,
                new Vec3(x, this.max.getY(), this.max.getZ())
        );
        AABB right = new AABB(
                new Vec3(x, this.min.getY(), this.min.getZ()),
                this.max
        );

        return new AABB[] {left, right};
    }

    public AABB[] splitOverY(double y) {
        AABB left = new AABB(
                this.min,
                new Vec3(this.max.getX(), y, this.max.getZ())
        );
        AABB right = new AABB(
                new Vec3(this.min.getX(), y, this.min.getZ()),
                this.max
        );

        return new AABB[] {left, right};
    }

    public AABB[] splitOverZ(double z) {
        AABB left = new AABB(
                this.min,
                new Vec3(this.max.getX(), this.max.getY(), z)
        );
        AABB right = new AABB(
                new Vec3(this.min.getX(), this.min.getY(), z),
                this.max
        );

        return new AABB[] {left, right};
    }
}
