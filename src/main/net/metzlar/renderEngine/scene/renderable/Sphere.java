package net.metzlar.renderEngine.scene.renderable;

import net.metzlar.renderEngine.types.*;

import java.io.Serializable;

public class Sphere extends RenderObject implements Intersectable, Serializable {
    private static final double PI2 = Math.PI * 2d;

    private double radius;

    public Sphere(Vec3 position, double radius) {
        super(position);
        this.radius = radius;
    }

    public Intersection intersectRay(Ray ray) {
        Vec3 oc = ray.origin.subtract(this.position);
        double a = ray.direction.dot(ray.direction);
        double b = 2.0 * oc.dot(ray.direction);
        double c = oc.dot(oc) - radius*radius;
        double discriminant = b*b - 4*a*c;

        if (discriminant < 0) {
            return null;
        }

        double dist = (-b - Math.sqrt(discriminant)) / (2.0*a);
        Vec3 hitPos = ray.origin.add(ray.direction.multiply(dist));
        Vec3 nHit = hitPos.subtract(this.position).getNormalized();

        //get texture hitpos
        Vec2 texturePosition = new Vec2(
                (0.5d + Math.atan2(nHit.getZ(), nHit.getX()) / PI2) * radius,
                (0.5d - Math.asin(nHit.getY()) / Math.PI) * radius
        );

        return new Intersection(ray, hitPos, nHit, texturePosition, this, dist);
    }

}
