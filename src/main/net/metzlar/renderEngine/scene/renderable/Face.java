package net.metzlar.renderEngine.scene.renderable;

import net.metzlar.renderEngine.scene.material.DebugNormal;
import net.metzlar.renderEngine.scene.material.Lambert;
import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.scene.texture.CheckerBoard;
import net.metzlar.renderEngine.types.*;

public class Face extends Renderable {
    private Vec3[] v;
    private Vec2[] uv;
    private Vec3[] vn;

    private Vec3 defaultNormal;

    public Face(Vec3[] v) {
        this(v, null, null);
    }

    public Face(Vec3[] v, Vec2[] uv) {
        this(v, uv, null);
    }

    public Face(Vec3[] v, Vec2[] uv, Vec3[] vn) {
        this.v = v;
        this.uv = uv;
        this.vn = vn;
        this.material = new DebugNormal();
    }

    //https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-rendering-a-triangle/moller-trumbore-ray-triangle-intersection
    @Override
    public Intersection intersectRay(Ray r) {
        Vec3 v0v1 = v[1].subtract(v[0]);
        Vec3 v0v2 = v[2].subtract(v[0]);
        Vec3 pvec = r.getDirection().cross(v0v2);
        double det = v0v1.dot(pvec);

        if (det < 1e-6) return null;

        double invDet = 1 / det;

        Vec3 tvec = r.getOrigin().subtract(v[0]);
        double u = tvec.dot(pvec) * invDet;
        if (u < 0 || u > 1) return null;

        Vec3 qvec = tvec.cross(v0v1);
        double v = r.getDirection().dot(qvec) * invDet;
        if (v < 0 || u + v > 1) return null;

        double dist = v0v2.dot(qvec) * invDet;

        Vec3 hitpos = r.getOrigin().add(r.getDirection().multiply(dist));
        Vec3 normal = v0v1.cross(v0v2);

        return new Intersection(r, hitpos, normal, new Vec2(u, v), this, dist);
    }
}
