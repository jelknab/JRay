package net.metzlar.renderEngine.scene;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.scene.light.Light;
import net.metzlar.renderEngine.scene.renderable.Renderable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Scene implements Serializable {
    private ArrayList<Renderable> renderables;
    private HashMap<String, Material> materials;
    private Camera camera;
    private ArrayList<Light> lights;

    public Scene() {
        this.renderables = new ArrayList<>();
        this.lights = new ArrayList<>();
        this.materials = new HashMap<>();
    }

    /**
     * @param ray ray to check intersections with
     * @param render render for updating intersection totals
     * @return null for no intersection or the closest intersected object.
     */
    public Intersection intersectScene(Ray ray, Render render) {
        Intersection closest = null;

        // Loop over all the objects we could intersect with
        for (Renderable renderable : this.renderables) {
            Intersection intersection = renderable.intersectRay(ray);

            // if we intersect
            if (intersection != null) {

                // Check if object is closer as current closest object and greater than epsilon (usually self intersection)
                if ((closest == null || intersection.getDistance() < closest.getDistance()) && intersection.getDistance() > 1e-6) {
                    closest = intersection;
                }
            }
        }

        render.getStatistics().addIntersections(this.renderables.size());

        return closest;
    }

    public void addRenderable(Renderable obj) {
        this.renderables.add(obj);
    }

    public ArrayList<Renderable> getRenderables() {
        return renderables;
    }

    public void addMaterial(String name, Material material) {
        this.materials.put(name, material);
    }

    public Material getMaterial(String name) {
        return this.materials.get(name);
    }

    public HashMap<String, Material> getMaterials() {
        return materials;
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
