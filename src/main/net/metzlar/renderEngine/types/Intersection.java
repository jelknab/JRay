package net.metzlar.renderEngine.types;

import net.metzlar.renderEngine.scene.renderable.Renderable;

public class Intersection {
    private Ray ray;
    private Vec3 hitPos;
    private Vec3 normal;
    private Vec2 texturePos;
    private Renderable renderable;
    private double distance;

    public Intersection(Ray ray, Vec3 hitPos, Vec3 normal, Vec2 texturePos, Renderable renderable, double distance) {
        this.ray = ray;
        this.hitPos = hitPos;
        this.normal = normal;
        this.renderable = renderable;
        this.texturePos = texturePos;
        this.distance = distance;
    }

    public Intersection(Ray ray, Vec3 hitPos, Vec3 normal, Vec2 texturePos, Renderable renderable) {
        this(ray, hitPos, normal, texturePos, renderable, ray.getOrigin().subtract(hitPos).length());
    }

    public Ray getRay() {
        return ray;
    }

    public Vec3 getHitPos() {
        return hitPos;
    }

    public Vec3 getNormal() {
        return normal;
    }

    public Renderable getRenderable() {
        return renderable;
    }

    public double getDistance() {
        return distance;
    }

    public Vec2 getTexturePos() {
        return texturePos;
    }
}
