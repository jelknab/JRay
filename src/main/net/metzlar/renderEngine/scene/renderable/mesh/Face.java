package net.metzlar.renderEngine.scene.renderable.mesh;

import net.metzlar.renderEngine.scene.renderable.Intersectable;
import net.metzlar.renderEngine.scene.renderable.Plane;
import net.metzlar.renderEngine.types.*;

import java.io.Serializable;
import java.util.Arrays;

public class Face implements Intersectable,Serializable {
    private Vec3[] v;
    private Vec3[] vn;
    private Vec2[] uv;

    private NormalTactic normalTactic;
    private TextureTactic textureTactic;

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

        if (vn == null || Arrays.asList(vn).contains(null)) {
            this.vn = null;
            this.normalTactic = new StaticNormal((v[1].subtract(v[0])).cross(v[2].subtract(v[0])).getNormalized());
        } else {
            this.normalTactic = new SmoothNormal(vn);
        }

        if (uv == null || Arrays.asList(uv).contains(null)) {
            this.uv = null;
            this.textureTactic = new DefaultUV();
        } else {
            this.textureTactic = new ModelUV(uv);
        }
    }

    //https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-rendering-a-triangle/moller-trumbore-ray-triangle-intersection
    @Override
    public Intersection intersectRay(Ray ray) {
        Vec3 v0v1 = v[1].subtract(v[0]);
        Vec3 v0v2 = v[2].subtract(v[0]);
        Vec3 pvec = ray.direction.cross(v0v2);
        double det = v0v1.dot(pvec);

        if (det > -1e-6 && det < 1e-6) return null;

        double invDet = 1 / det;

        Vec3 tvec = ray.origin.subtract(v[0]);
        double u = tvec.dot(pvec) * invDet;
        if (u < 0 || u > 1) return null;

        Vec3 qvec = tvec.cross(v0v1);
        double v = ray.direction.dot(qvec) * invDet;
        if (v < 0 || u + v > 1) return null;

        double dist = v0v2.dot(qvec) * invDet;

        Vec3 hitpos = ray.origin.add(ray.direction.multiply(dist));

        double w = 1 - u - v;

        Vec3 normal = this.normalTactic.getNormal(u, v, w);
        Vec2 uv = this.textureTactic.getUV(u, v, w);

        return new Intersection(
                ray,
                hitpos,
                normal,
                uv,
                null,
                dist
        );
    }

    public Vec3[] getVertices() {
        return v;
    }

    //http://geomalgorithms.com/a05-_intersect-1.html
    public double[] intersectEdgesByPlane(Plane pn) {
        Vec3[][] edges = {
                {v[0], v[1]},
                {v[0], v[2]},
                {v[1], v[2]}
        };
        double[] intersections = new double[3];

        for (int i = 0; i < 3; i++) {
            Vec3[] segment = edges[i];

            Vec3 u = segment[0].subtract(segment[1]);
            Vec3 w = segment[1].subtract(pn.position);

            double d = pn.normal.dot(u);
            double n = -pn.normal.dot(w);

            if (Math.abs(d) < 1e-6) continue;

            double sI = n / d;
            if (sI < 0 || sI > 1) continue;

            intersections[i] = sI;
        }

        return intersections;
    }

    private interface NormalTactic {
        Vec3 getNormal(double u, double v, double w);
    }

    private class SmoothNormal implements NormalTactic, Serializable {
        private Vec3[] normals;

        private SmoothNormal(Vec3[] normals) {
            this.normals = normals;
        }

        @Override
        public Vec3 getNormal(double u, double v, double w) {
            return normals[0].multiply(w)
                    .add(normals[1].multiply(u))
                    .add(normals[2].multiply(v))
                    .getNormalized();
        }
    }

    private class StaticNormal implements NormalTactic, Serializable {
        private Vec3 normal;

        private StaticNormal(Vec3 normal) {
            this.normal = normal;
        }

        @Override
        public Vec3 getNormal(double u, double v, double w) {
            return normal;
        }
    }

    private interface TextureTactic {
        Vec2 getUV(double u, double v, double w);
    }

    private class ModelUV implements TextureTactic, Serializable {
        private Vec2[] uvs;

        private ModelUV(Vec2[] uvs) {
            this.uvs = uvs;
        }

        @Override
        public Vec2 getUV(double u, double v, double w) {
            return uvs[0].multiply(u)
                    .add(uvs[1].multiply(v))
                    .add(uvs[2].multiply(w));
        }
    }

    private class DefaultUV implements TextureTactic, Serializable {

        @Override
        public Vec2 getUV(double u, double v, double w) {
            return new Vec2(u, v);
        }
    }
}
