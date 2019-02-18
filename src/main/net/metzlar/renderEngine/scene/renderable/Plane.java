package net.metzlar.renderEngine.scene.renderable;

import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.types.*;

public class Plane extends Renderable {
    private final double textureScale;

    public Plane(Vec3 center, Angle orientation, double textureScale) {
        super(center, orientation, Material.DEFAULT);
        this.textureScale = textureScale;

        this.orientation = new Vec3(0, 1, 0)
                .rotateAxisX(Math.toRadians(orientation.getPitch()))
                .rotateAxisY(Math.toRadians(orientation.getYaw()))
                .rotateAxisZ(Math.toRadians(orientation.getRoll()));
    }

    @Override
    public Intersection intersectRay(Ray r) {
        double denominator = this.orientation.dot(r.getDirection());

        if (denominator > 1e-6) {//greater than epsilon
            double dist = this.getPosition().subtract(r.getOrigin()).dot(this.orientation) / denominator;

            if (dist >= 0) {
                //get texture coordinate
                Vec3 uAxis = new Vec3(orientation.getY(), orientation.getZ(), -orientation.getX());
                Vec3 vAxis = uAxis.cross(orientation);

                Vec3 hitPos = r.getOrigin().add(r.getDirection().multiply(dist));

                Vec2 uv = new Vec2(
                        hitPos.dot(uAxis) * textureScale,
                        hitPos.dot(vAxis) * textureScale
                );

                return new Intersection(
                        r,
                        hitPos,
                        orientation.multiply(-1),
                        uv,
                        this
                );
            }
        }

        return null;
    }
}
