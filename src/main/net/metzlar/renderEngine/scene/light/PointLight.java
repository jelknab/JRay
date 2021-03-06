package net.metzlar.renderEngine.scene.light;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;

public class PointLight extends Light {
    public PointLight(Vec3 position, Color color, double intensity) {
        super(position, color, intensity);
    }

    @Override
    public Color getNormIntensity(Render render, Intersection originalIntersection) {
        Vec3    point2Light = this.position.subtract(originalIntersection.hitPos);
        double  lightDistance2 = point2Light.norm();
        double  lightDistance = Math.sqrt(lightDistance2);

        // Is the light too far to affect the point?
        if (lightDistance >= intensity) return Color.BLACK;

        Vec3 point2LightNormalized = point2Light.getNormalized();

        // Ray from light towards point
        Ray r = new Ray(originalIntersection.hitPos, point2LightNormalized);
        Intersection point2LightIntersection = render.scene.intersect(r, render);

        // Check if we hit anything (we should hit our intersection object)
        if (point2LightIntersection != null) {

            // If the light ray distance is greater than the distance from light to hitpoint, it is not blocked
            if (lightDistance - point2LightIntersection.distance < 1e-6) {
                return this.color.multiply(intensity).divide(4 * Math.PI * lightDistance2);
            }

            // TODO: remove this, as this should not happen.
//            if (point2LightIntersection.getRenderable() == originalIntersection.getRenderable()) {
//                return Color.GREEN;
//            }
        } else {// No intersection (void behind the light), light not blocked
            return this.color.multiply(intensity).divide(4 * Math.PI * lightDistance2);
        }

        return Color.BLACK;
    }

    @Override
    public Photon getIntensity(Render render, Intersection originalIntersection) {
        Vec3    point2Light = this.position.subtract(originalIntersection.hitPos);
        double  lightDistance = point2Light.length();

        // Is the light too far to affect the point?
        if (lightDistance >= intensity) return null;

        Vec3 point2LightNormalized = point2Light.getNormalized();

        // Ray from light towards point
        Ray r = new Ray(originalIntersection.hitPos, point2LightNormalized);
        Intersection point2LightIntersection = render.scene.intersect(r, render);

        // Check if we hit anything (we should hit our intersection object)
        if (point2LightIntersection != null) {

            // If the light ray distance is greater than the distance from light to hitpoint, it is not blocked
            if (lightDistance - point2LightIntersection.distance < 1e-6) {
                return new Photon(this, lightDistance);
            }

        } else {// No intersection (void behind the light), light not blocked
            return new Photon(this, lightDistance);
        }

        return null;
    }
}
