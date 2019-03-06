package net.metzlar.renderEngine.scene.renderable;

import net.metzlar.renderEngine.types.*;

public class Plane extends RenderObject implements Intersectable {
    public Vec3 normal;

    public Plane(Vec3 position, Angle orientation) {
        this(
                position,
                new Vec3(0, 1, 0)
                .rotateAxisX(Math.toRadians(orientation.getPitch()))
                .rotateAxisY(Math.toRadians(orientation.getYaw()))
                .rotateAxisZ(Math.toRadians(orientation.getRoll()))
        );
    }

    public Plane(Vec3 position, Vec3 normal) {
        super(position);
        this.normal = normal;
    }

    @Override
    public Intersection intersectRay(Ray ray) {
        double denominator = this.normal.dot(ray.direction);

        if (denominator > 1e-6) {//greater than epsilon
            double dist = this.position.subtract(ray.origin).dot(this.normal) / denominator;

            if (dist >= 0) {
                //get texture coordinate
                Vec3 uAxis = new Vec3(normal.getY(), normal.getZ(), -normal.getX());
                Vec3 vAxis = uAxis.cross(normal);

                Vec3 hitPos = ray.origin.add(ray.direction.multiply(dist));

                Vec2 uv = new Vec2(
                        hitPos.dot(uAxis),
                        hitPos.dot(vAxis)
                );

                return new Intersection(
                        ray,
                        hitPos,
                        normal.multiply(-1),
                        uv,
                        this
                );
            }
        }

        return null;
    }
}
