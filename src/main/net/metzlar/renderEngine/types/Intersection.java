package net.metzlar.renderEngine.types;

import net.metzlar.renderEngine.scene.renderable.Intersectable;
import net.metzlar.renderEngine.scene.renderable.RenderObject;

public class Intersection {
    public Ray ray;
    public Vec3 hitPos;
    public Vec3 normal;
    public Vec2 texturePos;
    public RenderObject renderable;
    public double distance;

    public Intersection(Ray ray, Vec3 hitPos, Vec3 normal, Vec2 texturePos, RenderObject renderable, double distance) {
        this.ray = ray;
        this.hitPos = hitPos;
        this.normal = normal;
        this.renderable = renderable;
        this.texturePos = texturePos;
        this.distance = distance;
    }

    public Intersection(Ray ray, Vec3 hitPos, Vec3 normal, Vec2 texturePos, RenderObject renderable) {
        this(ray, hitPos, normal, texturePos, renderable, ray.origin.subtract(hitPos).length());
    }
}
