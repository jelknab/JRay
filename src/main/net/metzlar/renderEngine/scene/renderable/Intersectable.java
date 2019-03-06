package net.metzlar.renderEngine.scene.renderable;

import net.metzlar.JRay;
import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.types.*;

import java.util.List;

/**
 * Intersectable is the class that all objects that can be intersected by rays should extend.
 */
public interface Intersectable {

    /**
     * @param r ray to check intersection with
     * @return null if ray does not hit, or intersection if it does.
     */
    Intersection intersectRay(Ray r);

    static Intersection closestIntersection(Ray r, List<? extends Intersectable> renderableList) {
        Intersection closest = null;

        // Loop over all the objects we could intersect with
        for (Intersectable renderable : renderableList) {
            Intersection intersection = renderable.intersectRay(r);

            // if we intersect
            if (intersection != null) {

                // Check if object is closer as current closest object and greater than epsilon (usually self intersection)
                if ((closest == null || intersection.distance < closest.distance) && intersection.distance > 1e-2) {
                    closest = intersection;
                }
            }
        }

        JRay.statistics.intersections += renderableList.size();

        return closest;
    }
}
