package net.metzlar.renderEngine.types;

public class Ray {
    public Vec3 origin;
    public Vec3 direction;

    public Ray(Vec3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }
}
