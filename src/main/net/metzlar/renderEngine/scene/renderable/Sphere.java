package net.metzlar.renderEngine.scene.renderable;

import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.scene.material.Phong;
import net.metzlar.renderEngine.scene.texture.CheckerBoard;
import net.metzlar.renderEngine.types.*;

public class Sphere extends Renderable {
    private static final double PI2 = Math.PI * 2d;

    private double radius;

    public Sphere(Vec3 position, double radius) {
        super(position, Material.DEFAULT);
        this.radius = radius;
    }

    public Intersection intersectRay(Ray r) {
        Vec3 oc = r.getOrigin().subtract(this.getPosition());
        double a = r.getDirection().dot(r.getDirection());
        double b = 2.0 * oc.dot(r.getDirection());
        double c = oc.dot(oc) - radius*radius;
        double discriminant = b*b - 4*a*c;

        if (discriminant < 0) {
            return null;
        }

        double dist = (-b - Math.sqrt(discriminant)) / (2.0*a);
        Vec3 hitPos = r.getOrigin().add(r.getDirection().multiply(dist));
        Vec3 nHit = hitPos.subtract(this.getPosition()).getNormalized();

        //get texture hitpos
        Vec2 texturePosition = new Vec2(
                (0.5d + Math.atan2(nHit.getZ(), nHit.getX()) / PI2) * radius,
                (0.5d - Math.asin(nHit.getY()) / Math.PI) * radius
        );

        return new Intersection(r, hitPos, nHit, texturePosition, this, dist);
    }
}
